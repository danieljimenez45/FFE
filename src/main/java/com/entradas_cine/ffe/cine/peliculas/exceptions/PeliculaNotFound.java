package com.entradas_cine.ffe.cine.peliculas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PeliculaNotFound extends PeliculaException {

    public PeliculaNotFound(Long id) {
        super("Pelicula con id " + id + " no encontrada");
    }
}
