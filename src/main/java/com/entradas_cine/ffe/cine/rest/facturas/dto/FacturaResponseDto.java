package com.entradas_cine.ffe.cine.rest.facturas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de la factura con detalle de entradas y usuario")
public class FacturaResponseDto {

    @Schema(description = "Numero de factura (id)", example = "1")
    private Long numeroFactura;

    @Schema(description = "Codigo propio de la factura", example = "FAC-20260501-7F9A2C")
    private String codigoFactura;

    @Schema(description = "Id del usuario", example = "1")
    private Long idUsuario;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Perez Garcia")
    private String apellidos;

    @Schema(description = "Email del usuario", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Entradas incluidas en la factura")
    private List<EntradaLineaDto> entradas;

    @Schema(description = "Precio total de la factura", example = "17.00")
    private Float total;
}
