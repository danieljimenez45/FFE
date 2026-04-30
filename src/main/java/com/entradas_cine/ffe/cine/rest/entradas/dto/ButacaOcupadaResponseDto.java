package com.entradas_cine.ffe.cine.rest.entradas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Butaca ocupada en una sesion")
public class ButacaOcupadaResponseDto {

    @Schema(description = "Fila de la butaca", example = "4")
    private Integer fila;

    @Schema(description = "Numero de la butaca", example = "5")
    private Integer numero;
}
