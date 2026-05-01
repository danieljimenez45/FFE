package com.entradas_cine.ffe.cine.rest.entradas.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Entrada a crear")
public class EntradaCreateDto {

    @NotNull(message = "El id de la sesion es obligatorio")
    @JsonAlias({"id_sesion", "sesionId", "idSesionId", "sesion"})
    @Schema(name = "Identificador de la sesion a la que pertenece la entrada", example = "1")
    private Long idSesion;

    @Positive(message = "La fila de butacas no puede ser negativa")
    @NotNull(message = "La fila de butacas es obligatoria")
    @Schema(description = "Fila de butacas de donde es la entrada", example = "4")
    private Integer fila;

    @Positive(message = "El numero de butacas no puede ser negativo")
    @NotNull(message = "El numero de la butaca es obligatorio")
    @Schema(description = "Numero de butaca dentro de su fila", example = "5")
    private Integer numero;
}
