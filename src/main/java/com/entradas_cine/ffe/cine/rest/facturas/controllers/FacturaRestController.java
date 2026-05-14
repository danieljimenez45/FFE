package com.entradas_cine.ffe.cine.rest.facturas.controllers;

import com.entradas_cine.ffe.cine.config.ApiRoutes;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
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

@Slf4j
@RestController
@RequestMapping(ApiRoutes.FACTURAS)
@RequiredArgsConstructor
@Tag(name = "Facturas", description = "Operaciones relacionadas con las facturas")
@SecurityRequirement(name = "bearerAuth")
public class FacturaRestController {

    private final FacturaService facturaService;

    // -------------------------------------------------------------------------
    // Escritura — usuario autenticado
    // -------------------------------------------------------------------------

    @Operation(summary = "Crear factura", description = "Crea una factura para el usuario autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Factura creada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FacturaResponseDto> create(@RequestBody @Valid FacturaCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.create(dto));
    }

    // -------------------------------------------------------------------------
    // Lectura — usuario solo ve las suyas; ADMIN ve todas
    // -------------------------------------------------------------------------

    /**
     * Devuelve las facturas del usuario autenticado.
     */
    @Operation(summary = "Mis facturas", description = "Devuelve las facturas del usuario en sesión")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FacturaResponseDto>> findMisFacturas() {
        return ResponseEntity.ok(facturaService.findMisFacturas());
    }

    /**
     * Devuelve todas las facturas del sistema. Solo accesible por ADMIN.
     */
    @Operation(summary = "Todas las facturas (ADMIN)", description = "Lista todas las facturas. Solo ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — requiere ADMIN")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FacturaResponseDto>> findAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    /**
     * Devuelve una factura por id.
     * Un usuario normal solo puede ver sus propias facturas;
     * si intenta acceder a la de otro usuario recibe 404.
     */
    @Operation(summary = "Factura por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "No encontrada o sin permiso")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FacturaResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.findById(id));
    }

    /**
     * Devuelve las facturas de un usuario por su id.
     * Un usuario normal solo puede consultar las suyas propias.
     */
    @Operation(summary = "Facturas de un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No puedes ver facturas de otro usuario")
    })
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FacturaResponseDto>> findByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(facturaService.findByUsuarioId(usuarioId));
    }

    // -------------------------------------------------------------------------
    // Borrado — solo ADMIN
    // -------------------------------------------------------------------------

    @Operation(summary = "Eliminar factura (ADMIN)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
