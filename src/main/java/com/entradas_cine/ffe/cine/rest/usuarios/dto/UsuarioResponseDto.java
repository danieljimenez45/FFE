package com.entradas_cine.ffe.cine.rest.usuarios.dto;

import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Usuario en respuesta")
public class UsuarioResponseDto {

    @Schema(description = "Identificador del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "jperez")
    private String username;

    @Schema(description = "Nombre", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos", example = "Perez Garcia")
    private String apellidos;

    @Schema(description = "Email", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Fecha de nacimiento", example = "1998-07-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Fecha de registro")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Rol", example = "USER")
    private Rol rol;
}
