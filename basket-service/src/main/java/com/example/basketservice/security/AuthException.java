package com.example.basketservice.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthException extends RuntimeException {

    public AuthException(String msg){
        super(msg);
    }
}