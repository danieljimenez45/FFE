package com.entradas_cine.ffe.rest.peliculas.models;


import com.entradas_cine.ffe.rest.sesiones.models.Sesion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PELICULAS")
@Schema(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la película", example = "1")
    private Long id;

    @OneToMany
    @JoinColumn(name = "id_pelicula")
    @Schema(description = "Sesiones asociadas a la película", example = "Sesión de las 18:00")
    private List<Sesion> sesiones;

    @Column(nullable = false, length = 60)
    @Schema(description = "Título de la película", example = "Inception")
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Género de la película", example = "CIENCIA_FICCION")
    private Genero genero;

    @Column(nullable = false, length = 250)
    @Schema(description = "Sinopsis de la película", example = "Un ladrón que roba secretos a través del uso de la tecnología de compartir sueños...")
    private String sinopsis;

    @Column(nullable = false)
    @Schema(description = "Duración de la película en minutos", example = "148")
    private Integer duracion;

    @Column(nullable = false, length = 40)
    @Schema(description = "Director de la película", example = "Christopher Nolan")
    private String director;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Fecha de estreno de la película", example = "2010-07-16")
    private LocalDate estreno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    @Schema(description = "Clasificación de la película", example = "MAYORES_12")
    private ClasificacionEdad clasificacionEdad;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;

}