package com.entradas_cine.ffe.cine.rest.peliculas.models;

public enum ClasificacionEdad {
    TP("TP"),
    MAYORES_7("Mayores 7"),
    MAYORES_12("Mayores 12"),
    MAYORES_16("Mayores 16"),
    MAYORES_18("Mayores 18");

    private final String displayName;

    ClasificacionEdad(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
