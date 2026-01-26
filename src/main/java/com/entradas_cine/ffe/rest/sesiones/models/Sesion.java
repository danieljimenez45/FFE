package com.entradas_cine.ffe.rest.sesiones.models;



import com.entradas_cine.ffe.rest.peliculas.models.Pelicula;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "SESIONES")
@Schema(name = "sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la sesión", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    @Schema(description = "Película asociada a la sesión", example = "Inception")
    private Pelicula pelicula;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de la sesión", example = "2024-12-31")
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Horario de la sesión", example = "H18_30")
    private Horario horario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Sala de la sesión", example = "SALA_1")
    private Sala sala;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de proyección de la sesión", example = "IMAX")
    private TipoProyeccion tipoProyeccion;

    @Column(nullable = false)
    @Schema(description = "Precio de la sesión", example = "10.50")
    private BigDecimal precio;

}
