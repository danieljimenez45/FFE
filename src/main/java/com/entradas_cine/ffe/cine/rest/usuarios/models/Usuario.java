package com.entradas_cine.ffe.cine.rest.usuarios.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USUARIOS")
@Schema(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del usuario", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de usuario unico", example = "jperez")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Apellidos del usuario", example = "Perez Garcia")
    private String apellidos;

    @Column(nullable = false, unique = true)
    @Schema(description = "Correo electronico del usuario", example = "juan.perez@email.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Contrasena cifrada del usuario")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Fecha de nacimiento", example = "1998-07-15")
    private LocalDate fechaNacimiento;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Fecha de registro generada automaticamente")
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Rol del usuario", example = "USER")
    private Rol rol;
}
