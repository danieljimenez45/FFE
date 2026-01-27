package com.entradas_cine.ffe.cine.sesiones.exceptions;

public class SesionNotFound extends SesionException {

    public SesionNotFound(Long id) {
        super("Sesion ccon id " + id + " no econtrada");
    }
}
