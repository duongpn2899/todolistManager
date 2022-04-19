package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GobalExceptionHandler {
    @ExceptionHandler(UserTodoNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handle404Error(UserTodoNotFoundException e) {
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());

    }

    @ExceptionHandler(UserTodoException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handle404Error(UserTodoException e) {
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());

    }


}