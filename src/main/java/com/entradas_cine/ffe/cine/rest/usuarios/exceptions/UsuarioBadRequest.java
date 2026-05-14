package com.entradas_cine.ffe.cine.rest.usuarios.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioBadRequest extends UsuarioException {

    public UsuarioBadRequest(String message) {
        super(message);
    }
}
