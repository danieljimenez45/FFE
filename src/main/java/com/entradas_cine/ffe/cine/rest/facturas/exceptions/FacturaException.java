package com.entradas_cine.ffe.cine.rest.facturas.exceptions;

public abstract class FacturaException extends RuntimeException {

    public FacturaException(String message) {
        super(message);
    }
}
