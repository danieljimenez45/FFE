package com.entradas_cine.ffe.cine.rest.entradas.exceptions;

import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionException;

public class EntradaBadRequest extends SesionException {
    public EntradaBadRequest(String message) {
        super(message);
    }
}
