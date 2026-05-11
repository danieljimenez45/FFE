package com.entradas_cine.ffe.cine.rest.sesiones.models;

public enum TipoProyeccion {
    NORMAL("Normal"),
    TRES_D("3D"),
    VOSE("V.O.S.E."),
    IMAX("IMAX");

    private final String displayName;

    TipoProyeccion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
