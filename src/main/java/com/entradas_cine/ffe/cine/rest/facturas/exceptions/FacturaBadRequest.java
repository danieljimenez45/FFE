package com.entradas_cine.ffe.cine.rest.facturas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FacturaBadRequest extends FacturaException {

    public FacturaBadRequest(String message) {
        super(message);
    }
}
