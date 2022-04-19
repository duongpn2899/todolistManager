package com.example.demo.request;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TodoRequest {

    private String description;

    private Boolean completed;
}
