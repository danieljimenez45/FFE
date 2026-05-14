package com.entradas_cine.ffe.cine.rest.facturas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Linea de detalle de una entrada dentro de una factura")
public class EntradaLineaDto {

    @Schema(description = "Id de la entrada", example = "1")
    private Long idEntrada;

    @Schema(description = "Id de la sesion", example = "3")
    private Long idSesion;

    @Schema(description = "Fila de la butaca", example = "4")
    private Integer fila;

    @Schema(description = "Numero de butaca", example = "5")
    private Integer numero;

    @Schema(description = "Precio de la entrada", example = "8.50")
    private Float precio;

    @Schema(description = "Fecha de compra de la entrada", example = "2026-05-01T15:30:00")
    private LocalDateTime fecha;
}
