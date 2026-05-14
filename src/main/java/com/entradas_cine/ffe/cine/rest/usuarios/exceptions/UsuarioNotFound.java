package com.entradas_cine.ffe.cine.rest.usuarios.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNotFound extends UsuarioException {

    public UsuarioNotFound(Long id) {
        super("Usuario con id " + id + " no encontrado");
    }

    public UsuarioNotFound(String username) {
        super("Usuario con username " + username + " no encontrado");
    }
}
