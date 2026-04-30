package com.entradas_cine.ffe.cine.rest.entradas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntradaNotFound extends EntradaException {
    public EntradaNotFound(Long id) {
        super("Entrada con id " + id + " no encontrada");
    }

    public EntradaNotFound(String message) {
        super(message);
    }
}
