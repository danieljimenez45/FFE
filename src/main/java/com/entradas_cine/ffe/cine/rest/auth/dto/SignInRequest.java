package com.entradas_cine.ffe.cine.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credenciales de inicio de sesión")
public class SignInRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
