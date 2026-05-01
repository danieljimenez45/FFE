package com.entradas_cine.ffe.cine.rest.sesiones.models;

public enum Sala {
    SALA_1(10, 15),
    SALA_2(10, 15),
    SALA_3(8, 12),
    SALA_4(8, 12),
    SALA_5(6, 10),
    SALA_6(6, 10);

    private final int maxFilas;
    private final int maxNumeros;

    Sala(int maxFilas, int maxNumeros) {
        this.maxFilas = maxFilas;
        this.maxNumeros = maxNumeros;
    }

    public int getMaxFilas() { return maxFilas; }
    public int getMaxNumeros() { return maxNumeros; }
}