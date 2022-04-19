package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {

    private Long id;

    private String description;

    private Instant created;


    private Instant modified;

    private Boolean completed;

    private UserGetDto userGetDto;



}
