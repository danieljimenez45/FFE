package com.entradas_cine.ffe.cine.rest.sesiones.models;

public enum Sala {
    SALA_1(10, 15, "Sala 1"),
    SALA_2(10, 15, "Sala 2"),
    SALA_3(8, 12, "Sala 3"),
    SALA_4(8, 12, "Sala 4"),
    SALA_5(6, 10, "Sala 5"),
    SALA_6(6, 10, "Sala 6");

    private final int maxFilas;
    private final int maxNumeros;
    private final String displayName;

    Sala(int maxFilas, int maxNumeros, String displayName) {
        this.maxFilas = maxFilas;
        this.maxNumeros = maxNumeros;
        this.displayName = displayName;
    }

    public int getMaxFilas() { return maxFilas; }
    public int getMaxNumeros() { return maxNumeros; }
    public String getDisplayName() { return displayName; }
}