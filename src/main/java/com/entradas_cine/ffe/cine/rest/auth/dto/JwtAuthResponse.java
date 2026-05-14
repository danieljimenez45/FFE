package com.entradas_cine.ffe.cine.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con JWT")
public class JwtAuthResponse {

    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;
}
