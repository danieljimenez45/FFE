package com.entradas_cine.ffe.cine.rest.peliculas.models;

public enum Genero {
    ACCION("Acción"),
    COMEDIA("Comedia"),
    DRAMA("Drama"),
    TERROR("Terror"),
    CIENCIA_FICCION("Ciencia ficción"),
    FANTASIA("Fantasía"),
    ROMANCE("Romance"),
    SUSPENSE("Suspense"),
    AVENTURAS("Aventuras");

    private final String displayName;

    Genero(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}