package com.entradas_cine.ffe.cine.rest.usuarios.controllers;

import com.entradas_cine.ffe.cine.config.ApiRoutes;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST de usuarios.
 * Todas las operaciones requieren rol ADMIN (gestión interna).
 * El registro público de nuevos usuarios se realiza en AuthRestController (/api/v1/auth/signup).
 */
@Slf4j
@RestController
@RequestMapping(ApiRoutes.USUARIOS)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Usuarios", description = "Administración de usuarios — solo ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    @Operation(
        summary = "Crear usuario estándar (ADMIN)",
        description = "El administrador da de alta un usuario con rol USER. " +
                      "Para registro público usar POST /api/v1/auth/signup. " +
                      "Para crear otro administrador usar POST /api/v1/usuarios/admin.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(dto));
    }

    @Operation(
        summary = "Crear administrador (ADMIN)",
        description = "Registra un nuevo usuario con rol ADMIN. " +
                      "Solo accesible por administradores autenticados. " +
                      "El campo 'rol' del cuerpo se ignora — el rol ADMIN se asigna siempre.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Administrador creado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @PostMapping("/admin")
    public ResponseEntity<UsuarioResponseDto> createAdmin(@RequestBody @Valid UsuarioCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createAdmin(dto));
    }

    @Operation(summary = "Buscar usuario por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Buscar usuario por username")
    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponseDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.findByUsername(username));
    }

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Actualizar usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Actualizado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> update(
            @PathVariable Long id, @RequestBody @Valid UsuarioUpdateDto dto) {
        return ResponseEntity.ok(usuarioService.update(id, dto));
    }

    @Operation(summary = "Eliminar usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
