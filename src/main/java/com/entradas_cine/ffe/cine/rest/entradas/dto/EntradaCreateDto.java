package com.entradas_cine.ffe.cine.rest.entradas.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
>>>>>>> 9b0f9153605aa8e80e5568724a31fcae18ded1f8
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
<<<<<<< HEAD
=======

import java.time.LocalDateTime;
>>>>>>> 9b0f9153605aa8e80e5568724a31fcae18ded1f8

@Builder
@Data
@Schema(description = "Entrada a crear")
public class EntradaCreateDto {

<<<<<<< HEAD
    @NotNull(message = "El id de la sesion es obligatorio")
    @JsonAlias({"id_sesion", "sesionId", "idSesionId", "sesion"})
    @Schema(name = "Identificador de la sesion a la que pertenece la entrada", example = "1")
=======
    @NotNull(message = "El id de la sesión es obligatorio")
    @JsonAlias({"id_sesion", "sesionId", "idSesionId", "sesion"})
    @Schema(name = "Identificador de la sesión a la que pertenece la entrada", example = "1")
>>>>>>> 9b0f9153605aa8e80e5568724a31fcae18ded1f8
    private Long idSesion;

    @Positive(message = "La fila de butacas no puede ser negativa")
    @NotNull(message = "La fila de butacas es obligatoria")
    @Schema(description = "Fila de butacas de donde es la entrada", example = "4")
    private Integer fila;

    @Positive(message = "El numero de butacas no puede ser negativo")
    @NotNull(message = "El numero de la butaca es obligatorio")
    @Schema(description = "Numero de butaca dentro de su fila", example = "5")
    private Integer numero;
<<<<<<< HEAD
=======

    @NotNull(message = "El precio es obligatorio")
    @Schema(description = "Precio de la entrada", example = "1.99")
    private Float precio;

    @NotNull(message = "La fecha y hora son obligatorias")
    @JsonDeserialize(using = EntradaFechaDeserializer.class)
    @Schema(description = "Fecha y hora de compra. Formatos validos: yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd'T'HH:mm:ssXXX, yyyy-MM-dd, dd-MM-yyyy", example = "2024-12-31T18:30:00")
    private LocalDateTime fecha;
>>>>>>> 9b0f9153605aa8e80e5568724a31fcae18ded1f8
}
