package com.entradas_cine.ffe.cine.sesiones.dto;

import com.entradas_cine.ffe.cine.sesiones.models.Sesion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@Schema(description = "Sesión a crear")
public class SesionCreateDto {

    @NotNull(message = "El id de la película es obligatorio")
    @Schema(description = "ID de la película", example = "1")
    private final Long idPelicula;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Fecha de la sesión", example = "2024-12-31")
    private final LocalDate fecha;

    @NotNull(message = "El horario es obligatorio")
    @Schema(description = "Horario de la sesión", example = "H18_30")
    private final Sesion.Horario horario;

    @NotNull(message = "La sala es obligatorio")
    @Schema(description = "Sala de la sesión", example = "SALA_1")
    private final Sesion.Sala sala;

    @NotNull(message = "El tipo de proyección es obligatorio")
    @Schema(description = "Tipo de proyección de la sesión", example = "IMAX")
    private final Sesion.TipoProyeccion tipoProyeccion;

    @NotNull(message = "El precio es obligatorio")
    @Schema(description = "Precio de la sesión", example = "10.50")
    private final BigDecimal precio;

}
