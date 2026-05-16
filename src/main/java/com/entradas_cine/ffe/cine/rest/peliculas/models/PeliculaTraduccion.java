package com.entradas_cine.ffe.cine.rest.peliculas.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Almacena el título y la sinopsis de una película en un idioma distinto al español.
 * Un registro por (película, locale). El español vive directamente en PELICULAS.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "PELICULA_TRADUCCIONES",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_pelicula", "locale"})
)
public class PeliculaTraduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;

    /** Código de idioma BCP-47 en minúsculas: "en", "fr", "de", "pt" */
    @Column(nullable = false, length = 5)
    private String locale;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String sinopsis;
}
