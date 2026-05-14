package com.entradas_cine.ffe.cine.rest.entradas.controllers;

import com.entradas_cine.ffe.cine.config.ApiRoutes;
import com.entradas_cine.ffe.cine.rest.entradas.dto.ButacaOcupadaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiRoutes.ENTRADAS)
@RequiredArgsConstructor
@Tag(name = "Entradas", description = "Operaciones relacionadas con la compra y consulta de entradas")
public class EntradaRestController {

    private final EntradaService entradaService;

    // -------------------------------------------------------------------------
    // Escritura — usuario autenticado
    // -------------------------------------------------------------------------

    @Operation(summary = "Comprar entrada")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Entrada comprada"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntradaResponseDto> create(
            @RequestBody @Valid EntradaCreateDto entradaCreateDto) {
        log.info("EntradaRestController.create: {}", entradaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entradaService.create(entradaCreateDto));
    }

    // -------------------------------------------------------------------------
    // Lectura — usuario autenticado
    // -------------------------------------------------------------------------

    @Operation(summary = "Entrada por id")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntradaResponseDto> findById(@PathVariable Long id) {
        log.info("EntradaRestController.findById: {}", id);
        return ResponseEntity.ok(entradaService.findById(id));
    }

    @Operation(summary = "Entradas de una sesión")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/sesion/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EntradaResponseDto>> findBySesion(@PathVariable Long id) {
        log.info("EntradaRestController.findBySesion: {}", id);
        return ResponseEntity.ok(entradaService.findBySesion(id));
    }

    @Operation(summary = "Entradas de una sesión (paginadas)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/sesion/{id}/page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EntradaResponseDto>> findBySesionPage(
            @PathVariable Long id, Pageable pageable) {
        log.info("EntradaRestController.findBySesionPage: {} - {}", id, pageable);
        return ResponseEntity.ok(entradaService.findBySesion(id, pageable));
    }

    /**
     * Butacas ocupadas — público.
     * Este endpoint no requiere autenticación para que el selector de asientos
     * del frontend pueda mostrar qué butacas están libres antes del login.
     */
    @Operation(summary = "Butacas ocupadas de una sesión", description = "Público — no requiere autenticación")
    @GetMapping("/sesion/{id}/butacas-ocupadas")
    public ResponseEntity<List<ButacaOcupadaResponseDto>> findButacasOcupadasBySesion(
            @PathVariable Long id) {
        log.info("EntradaRestController.findButacasOcupadasBySesion: {}", id);
        return ResponseEntity.ok(entradaService.findButacasOcupadasBySesion(id));
    }

    // -------------------------------------------------------------------------
    // Borrado — solo ADMIN
    // -------------------------------------------------------------------------

    @Operation(summary = "Eliminar entrada (ADMIN)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("EntradaRestController.deleteById: {}", id);
        entradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
