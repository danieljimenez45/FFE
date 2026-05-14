package com.entradas_cine.ffe.cine.rest.sesiones.dto;

import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;
import com.entradas_cine.ffe.cine.rest.sesiones.models.TipoProyeccion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Actualizar datos de una sesión")
public class SesionUpdateDto {

    @Schema(example = "2025-07-20")
    private LocalDate fecha;

    @Schema(example = "H21_00")
    private Horario horario;

    @Schema(example = "SALA_3")
    private Sala sala;

    @Schema(example = "IMAX")
    private TipoProyeccion tipoProyeccion;

    @Schema(example = "11.50")
    private BigDecimal precio;
}
