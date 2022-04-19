package com.example.demo.controllers;

import com.example.demo.dto.TodoDto;
import com.example.demo.request.TodoRequest;
import com.example.demo.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/todo")
@Api(value = "/Todo", tags = "Todo-api", description = "Manage Todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/getById/{id}")
    @ApiOperation(value = "Tìm kiếm todo theo id user ")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TodoDto> getAll(@PathVariable Long id) {
        return todoService.findTodoByUserid(id);
    }
    @GetMapping("/getall")
    @ApiOperation(value = "Tìm kiếm tất cả todo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TodoDto> getAll() {
        return todoService.getAll();
    }

    @GetMapping("/getTodo")
    @ApiOperation(value = "Lấy thông tin todo người đăng nhập hiện tại")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<TodoDto> getInfo() {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        return todoService.getInfo(authentication);
    }

    @PostMapping("/creatTodo")
    @ApiOperation("Tạo todo mới")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TodoDto createTodo(@RequestBody String description) {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        return todoService.creatTodo(authentication, description);
    }

    @PutMapping("/updateTodo/{id}")
    @ApiOperation("Cập nhật mới todo")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TodoDto updateTodo(@PathVariable Long id,@RequestBody TodoRequest todoRequest) {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        return todoService.updateTodo(authentication, id, todoRequest);
    }

    @DeleteMapping("/deleteTodo/{id}")
    @ApiOperation("xóa theo id todo của người dùng")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TodoDto deleteTodo(@PathVariable Long id) {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        return todoService.deleteTodo(authentication,id);
    }
}
