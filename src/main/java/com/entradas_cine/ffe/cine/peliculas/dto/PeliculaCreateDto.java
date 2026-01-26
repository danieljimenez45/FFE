package com.entradas_cine.ffe.cine.peliculas.dto;

import com.entradas_cine.ffe.cine.peliculas.models.Pelicula;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@Schema(description = "Pelicula a crear")
public class PeliculaCreateDto {

    @NotBlank(message = "El título no puede estar vacío")
    @Schema(description = "Título de la película", example = "Inception")
    private final String titulo;

    @NotNull(message = "El genero es obligatorio")
    @Schema(description = "Género de la película", example = "CIENCIA_FICCION")
    private final Pelicula.Genero genero;

    @NotBlank(message = "La sinopsis no puede estar vacía")
    @Schema(description = "Sinopsis de la película", example = "Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños es")
    private final String sinopsis;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor que 0")
    @Schema(description = "Duración de la película en minutos", example = "148")
    private final Integer duracion;

    @NotBlank(message = "El director no puede estar vacío")
    @Schema(description = "Director de la película", example = "Christopher Nolan")
    private final String director;

    @NotNull(message = "LA fecha de estreno es obligatorio")
    @Schema(description = "Fecha de estreno de la película", example = "2010-07-16")
    private final LocalDate estreno;

    @NotNull(message = "La clasificación de edad es obligatoria")
    @Schema(description = "Clasificación de edad de la película", example = "MAYORES_12")
    private final Pelicula.ClasificacionEdad clasificacionEdad;

}
