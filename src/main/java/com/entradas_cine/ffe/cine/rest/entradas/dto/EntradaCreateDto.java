package com.entradas_cine.ffe.cine.rest.entradas.dto;


import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Entrada a crear")
public class EntradaCreateDto {

    @NotNull(message = "El id de la sesión es obligatorio")
    @Schema(name = "Identificador de la sesión a la que pertenece la entrada", example = "1")
    private Long idSesion;

    @Positive(message = "La fila de butacas no puede ser negativa")
    @NotNull(message = "La fila de butacas es obligatoria")
    @Schema(description = "Fila de butacas de donde es la entrada", example = "4")
    private Integer fila;

    @Positive(message = "El número de butacas no puede ser negativo")
    @NotNull(message = "El número de la butaca es obligatorio")
    @Schema(description = "Número de butaca dentro de su fila", example = "5")
    private Integer numero;

    @NotNull(message = "El precio es obligatorio")
    @Schema(description = "Precio de la entrada", example = "1.99")
    private Float precio;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Schema(description = "Fecha y hora en la que se compró la entrada", example = "2024-12-31")
    private LocalDateTime fecha;
}
