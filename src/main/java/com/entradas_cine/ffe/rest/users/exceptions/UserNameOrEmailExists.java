package com.entradas_cine.ffe.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNameOrEmailExists extends RuntimeException {
    public UserNameOrEmailExists(String message) {
        super(message);
    }
}
