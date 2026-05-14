package com.entradas_cine.ffe.cine.rest.facturas.models;

import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FACTURAS")
@Schema(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Numero de factura (id)", example = "1")
    private Long id;

    @Column(name = "codigo_factura", nullable = false, unique = true)
    @Schema(description = "Codigo propio de la factura para generar QR", example = "FAC-20260430-7F9A2C")
    private String codigoFactura;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @Schema(description = "Usuario asociado a la factura")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "FACTURAS_ENTRADAS",
            joinColumns = @JoinColumn(name = "factura_id"),
            inverseJoinColumns = @JoinColumn(name = "entrada_id"))
    @Schema(description = "Entradas asociadas a la factura")
    private List<Entrada> entradas;
}