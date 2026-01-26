package com.entradas_cine.ffe.cine.peliculas.dto;


import com.entradas_cine.ffe.cine.peliculas.models.Pelicula;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información de una película")
public class PeliculaResponseDto {

    @Schema(description = "Identificador de la película", example = "1")
    private Long id;

    @Schema(description = "Título de la película", example = "Inception")
    private String titulo;

    @Schema(description = "Director de la película", example = "Christopher Nolan")
    private String director;

    @Schema(description = "Género de la película", example = "CIENCIA_FICCION")
    private Pelicula.Genero genero;

    @Schema(description = "Duración de la película en minutos", example = "148")
    private Integer duracion;

    @Schema(description = "Fecha de lanzamiento de la película", example = "2010-07-16")
    private LocalDate estreno;

    @Schema(description = "Sinopsis de la película", example = "Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños es dado la tarea inversa de plantar una idea en la mente de un CEO.")
    private String sinopsis;

    @Schema(description = "Clasificación de edad de la película", example = "MAYORES_12")
    private Pelicula.ClasificacionEdad clasificacionEdad;

    @Schema(description = "Indica si la película está activa", example = "true")
    private Boolean activa;

}
