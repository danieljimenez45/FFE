package com.entradas_cine.ffe.cine.rest.facturas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos necesarios para crear una factura")
public class FacturaCreateDto {

    @NotNull(message = "El id del usuario no puede ser nulo")
    @Schema(description = "Identificador del usuario", example = "1")
    private Long idUsuario;

    @NotEmpty(message = "La lista de entradas no puede estar vacia")
    @Schema(description = "Lista de identificadores de entradas", example = "[1, 2, 3]")
    private List<Long> idEntradas;
}
