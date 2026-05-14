package com.entradas_cine.ffe.cine.rest.facturas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FacturaNotFound extends FacturaException {

    public FacturaNotFound(Long id) {
        super("No se encontro la factura con id: " + id);
    }
}
