package com.entradas_cine.ffe.cine.web.dto;

import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fila enriquecida para la pestaña Entradas del panel de administración.
 */
public record AdminEntradaView(
        Long id,
        String username,
        String nombre,
        String apellidos,
        Long peliculaId,
        String tituloPelicula,
        LocalDate fechaSesion,
        Horario horario,
        Sala sala,
        Integer fila,
        Integer numero,
        Float precio,
        LocalDateTime fechaCompra,
        Long numeroFactura,
        String codigoFactura
) {
    public String nombreCompleto() {
        return nombre + " " + apellidos;
    }

    public String horarioDisplay() {
        return horario != null ? horario.getDisplayName() : "—";
    }

    public String salaDisplay() {
        return sala != null ? sala.getDisplayName() : "—";
    }
}
