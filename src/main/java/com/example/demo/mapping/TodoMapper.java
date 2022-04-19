package com.example.demo.mapping;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.TodoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TodoMapper {
    @Autowired
    UserMapper userMapper;

   public abstract TodoDto todoEntityToTodoDto(TodoEntity todoEntity);

   @AfterMapping
    protected void setUserGetDto(TodoEntity todoEntity, @MappingTarget TodoDto todoDto) {
       todoDto.setUserGetDto(userMapper.userEntityToUserGetDto(todoEntity.getUserEntity()));
   }
}
