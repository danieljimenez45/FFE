package com.entradas_cine.ffe.cine.rest.entradas.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de una entrada")
public class EntradaResponseDto {

    @Schema(name = "Identificador único de la entrada", example = "1")
    private Long id;

    @Schema(description = "Identificador de la sesión", example = "1")
    private Long sesion;

    @Schema(description = "Fila de butacas de donde es la entrada", example = "4")
    private Integer fila;

    @Schema(description = "Número de butaca dentro de su fila", example = "5")
    private Integer numero;

    @Schema(description = "Precio de la entrada", example = "1.99")
    private Float precio;

    @Schema(description = "Fecha y hora en la que se compró la entrada", example = "2024-12-31")
    private LocalDateTime fecha;
}
