package com.entradas_cine.ffe.cine.rest.peliculas.models;

public enum ClasificacionEdad {
    TP("Todos los públicos"),
    MAYORES_7("Mayores de 7 años"),
    MAYORES_12("Mayores de 12 años"),
    MAYORES_16("Mayores de 16 años"),
    MAYORES_18("Mayores de 18 años");

    private final String displayName;

    ClasificacionEdad(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
