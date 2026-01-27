package com.entradas_cine.ffe.cine.peliculas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PeliculaBadRequest  extends PeliculaException {

    public PeliculaBadRequest(String message) {
        super(message);
    }
}
