package com.entradas_cine.ffe.cine.rest.entradas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntradaBadRequest extends EntradaException {
    public EntradaBadRequest(String message) {
        super(message);
    }
}
