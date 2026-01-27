package com.entradas_cine.ffe.cine.rest.sesiones.dto;


import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información de una sesión")
public class SesionResponseDto {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "Inception")
    private String pelicula;

    @Schema(example = "2024-12-31")
    private LocalDate fecha;

    @Schema(example = "H18_30")
    private Sesion.Horario horario;

    @Schema(example = "SALA_1")
    private Sesion.Sala sala;

    @Schema(example = "IMAX")
    private Sesion.TipoProyeccion tipoProyeccion;

    @Schema(example = "10.50")
    private BigDecimal precio;
}
