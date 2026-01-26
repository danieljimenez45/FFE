package com.entradas_cine.ffe.cine.peliculas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para activar o desactivar una pelicula")
public class PeliculaUpdateEstadoDto {

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Estado de la pelicula si esta activa en cartelera", example = "false")
    private Boolean activa;

}
