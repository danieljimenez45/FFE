package com.entradas_cine.ffe.cine.rest.entradas.exceptions;

public class EntradaNotFound extends EntradaException {
    public EntradaNotFound(Long id) {
        super("Entrada con id " + id + " no encontrada");
    }
}
