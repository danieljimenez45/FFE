package com.entradas_cine.ffe.cine.rest.usuarios.dto;

import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@Schema(description = "Datos para actualizar usuario")
public class UsuarioUpdateDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 150, message = "Los apellidos no pueden superar los 150 caracteres")
    @Schema(description = "Apellidos del usuario", example = "Perez Garcia")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Schema(description = "Correo electronico", example = "juan.perez@email.com")
    private String email;

    @Size(min = 6, message = "La password debe tener al menos 6 caracteres")
    @Schema(description = "Contrasena opcional en claro para actualizar")
    private String password;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @Schema(description = "Fecha de nacimiento", example = "1998-07-15")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Rol rol;
}
