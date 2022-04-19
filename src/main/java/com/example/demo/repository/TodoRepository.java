package com.example.demo.repository;

import com.example.demo.entity.TodoEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByUserEntity(UserEntity user);
    Boolean existsByDescription(String description);
}
