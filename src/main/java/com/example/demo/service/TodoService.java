package com.example.demo.service;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.TodoEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserTodoException;
import com.example.demo.mapping.TodoMapper;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.TodoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final String NOT_FOUND ="Todo not found!";
    private final String EXISTS =" already exists";
    private final String NOT_EXISTS =" already not exists";

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    @Autowired
    public TodoService(UserRepository userRepository, TodoRepository todoRepository, TodoMapper todoMapper) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    public List<TodoDto> getInfo(String username) {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        if (user.getTodoList() == null)
            throw new UserTodoException(HttpStatus.NOT_FOUND.value(), NOT_EXISTS);
        else if (user.getTodoList().size() == 0)
            throw new UserTodoException(HttpStatus.NOT_FOUND.value(), NOT_EXISTS);
        List<TodoDto> todoList = new ArrayList<>();
        for (TodoEntity todo : user.getTodoList()) {
            todoList.add(todoMapper.todoEntityToTodoDto(todo));
        }
        return todoList;

    }

    public List<TodoDto> findTodoByUserid(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (!userEntity.isPresent())
            throw new UserTodoException(HttpStatus.NOT_FOUND.value(), NOT_FOUND);
        else if (userEntity.get().getTodoList().size() == 0)
            throw new UserTodoException(HttpStatus.NOT_FOUND.value(),"Todo"+ NOT_EXISTS);
        else
            return this.getInfo(userEntity.get().getUserName());
    }


    public List<TodoDto> getAll() {

        List<TodoEntity> todoEntities = todoRepository.findAll();
        if (todoEntities.size() == 0)
            throw new UserTodoException(HttpStatus.NOT_FOUND.value(),"Todo"+ NOT_EXISTS);

        List<TodoDto> todoDtos = new ArrayList<>();
        for (TodoEntity todo : todoEntities) {

           todoDtos.add(todoMapper.todoEntityToTodoDto(todo));
        }
        todoDtos.sort(Comparator.comparing(TodoDto::getId));
        return todoDtos;
    }

    public TodoDto creatTodo(String username, String description) {
        Instant instant = Instant.now();
        TodoEntity todoEntity = new TodoEntity();
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        List<TodoEntity> todoEntities = todoRepository.findByUserEntity(user);

        for (TodoEntity t : todoEntities) {
            if (description.equals(t.getDescription())) {
                todoEntity = null;
                break;
            }
        }
        if (todoEntity == null) {
            throw new UserTodoException(HttpStatus.FOUND.value(),description + EXISTS);
        } else {
            todoEntity.setDescription(description);
            todoEntity.setCreated(instant);
            todoEntity.setModified(instant);
            todoEntity.setCompleted(false);
            todoEntity.setUserEntity(user);
            todoRepository.save(todoEntity);
            return todoMapper.todoEntityToTodoDto(todoEntity);
        }
    }

    public TodoDto updateTodo(String username, Long id, TodoRequest todoRequest) {
        Instant instant = Instant.now();
        int count = 0;
        Optional<TodoEntity> todoEntity = todoRepository.findById(id);
        if (todoEntity.isPresent()) {
            UserEntity user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            List<TodoEntity> todoEntities = todoRepository.findByUserEntity(user);
            for (TodoEntity t : todoEntities) {
                if (todoEntity.get().equals(t)) {
                    if (todoRequest.getDescription() == null | todoRequest.getDescription().isEmpty()) {
                        if (todoRequest.getCompleted() != null) {
                            todoEntity.get().setCompleted(todoRequest.getCompleted());
                            todoEntity.get().setModified(instant);
                            todoRepository.save(todoEntity.get());
                        }
                        return todoMapper.todoEntityToTodoDto(todoEntity.get());

                    } else {
                        for (TodoEntity todo : todoEntities) {
                            if (todoRequest.getDescription().equals(todo.getDescription())) {
                                count++;
                                break;
                            }
                        }
                        if (count != 0)
                            throw new UserTodoException(HttpStatus.BAD_REQUEST.value(), todoRequest.getDescription() + EXISTS);
                        else {
                            todoEntity.get().setDescription(todoRequest.getDescription());
                            todoEntity.get().setCompleted(false);
                            todoEntity.get().setModified(instant);
                            todoRepository.save(todoEntity.get());
                            return todoMapper.todoEntityToTodoDto(todoEntity.get());
                        }
                    }
                }

            }
        }
        throw new UserTodoException(HttpStatus.NOT_FOUND.value(), NOT_FOUND);
    }

    public TodoDto deleteTodo(String username, Long id) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(id);
        if (todoEntity.isPresent()) {
            Optional<UserEntity> user = userRepository.findByUserName(username);
            if (user.isPresent()) {
                List<TodoEntity> todoEntities = todoRepository.findByUserEntity(user.get());
                for (TodoEntity t : todoEntities) {
                    if (todoEntity.get().equals(t)) {

                        todoRepository.delete(todoEntity.get());
                        return todoMapper.todoEntityToTodoDto(todoEntity.get());
                    }
                }
            }
        }
        throw new UserTodoException(HttpStatus.NOT_FOUND.value(), NOT_FOUND);
    }

}
