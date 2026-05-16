package com.entradas_cine.ffe.cine.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fila enriquecida para la vista «Entradas» (web).
 */
public record EntradaCompradaView(
        Long numeroFactura,
        String codigoFactura,
        String tituloPelicula,
        LocalDate fechaSesion,
        String horario,
        String sala,
        Integer fila,
        Integer numero,
        Float precio,
        LocalDateTime fechaCompra
) {
}
