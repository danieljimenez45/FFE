package com.entradas_cine.ffe.cine.rest.sesiones.dto;

import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Actualizar datos de una sesión")
public class SesionUpdateDto {

    @Schema(example = "H21_00")
    private Horario horario;

    @Schema(example = "SALA_3")
    private Sala sala;

    @Schema(example = "11.50")
    private BigDecimal precio;
}
