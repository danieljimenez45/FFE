package com.entradas_cine.ffe.cine.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * Datos que manda quien se registra por la API.
 * No trae rol: en el servidor todas las altas nuevas son usuarios normales.
 */
@Data
@Schema(description = "Alta de usuario; el rol lo fija la aplicación (siempre usuario estándar)")
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 150)
    private String apellidos;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    @Past
    private LocalDate fechaNacimiento;
}
