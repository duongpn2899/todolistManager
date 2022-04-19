package com.example.demo.mapping;

import com.example.demo.dto.UserGetDto;
import com.example.demo.dto.UserPostDto;
import com.example.demo.entity.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserPostDto userEntityToUserPostDto(UserEntity userEntity);

    @AfterMapping
    protected void setRole(UserEntity userEntity, @MappingTarget UserPostDto userDto) {
        userDto.setRole(userEntity.getRoles().get(0).getCode());
    }

    public abstract UserGetDto userEntityToUserGetDto(UserEntity userEntity);

}
