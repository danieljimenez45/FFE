package com.entradas_cine.ffe.cine.sesiones.dto;

import com.entradas_cine.ffe.cine.sesiones.models.Sesion;
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
    private Sesion.Horario horario;

    @Schema(example = "SALA_3")
    private Sesion.Sala sala;

    @Schema(example = "11.50")
    private BigDecimal precio;
}
