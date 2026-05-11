package com.entradas_cine.ffe.cine.rest.sesiones.models;

public enum Horario {
    H16_00("16:00"),
    H18_30("18:30"),
    H21_00("21:00"),
    H23_30("23:30");

    private final String displayName;

    Horario(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}