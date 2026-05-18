package com.entradas_cine.ffe.cine.rest.sesiones.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SesionBadRequest extends SesionException {

    public SesionBadRequest(String message) {
        super(message);
    }
}
