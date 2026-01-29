package com.entradas_cine.ffe.cine.rest.entradas.models;

import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "ENTRADAS")
@Schema(name = "entradas")
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Identificador único de la entrada", example = "1")
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_sesion", nullable = false)
    @Schema(description = "Sesión de la cual es la entrada", example = "1")
    private Sesion sesion;

    @Column(nullable = false)
    @Schema(description = "Fila de butacas de donde es la entrada", example = "4")
    private Integer fila;

    @Column(nullable = false)
    @Schema(description = "Número de butaca dentro de su fila", example = "5")
    private Integer numero;

    @Column(nullable = false)
    @Schema(description = "Precio de la entrada", example = "1.99")
    private Float precio;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora en la que se compró la entrada", example = "2024-12-31")
    private LocalDateTime fecha;
}
