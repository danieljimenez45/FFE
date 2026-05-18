package com.entradas_cine.ffe.cine.rest.sesiones.controllers;

import com.entradas_cine.ffe.cine.config.ApiRoutes;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionCreateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionUpdateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.services.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiRoutes.SESIONES)
@RequiredArgsConstructor
@Tag(name = "Sesiones", description = "Operaciones relacionadas con las sesiones de cine")
public class SesionRestController {

    private final SesionService sesionService;

    // -------------------------------------------------------------------------
    // Lectura — público
    // -------------------------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<SesionResponseDto>> findAll() {
        log.info("Buscando todas las sesiones");
        return ResponseEntity.ok(sesionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionResponseDto> findById(@PathVariable Long id) {
        log.info("Buscando sesion con ID: {}", id);
        return ResponseEntity.ok(sesionService.findById(id));
    }

    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<List<SesionResponseDto>> findByPelicula(
            @PathVariable Long peliculaId) {
        log.info("Buscando sesiones por el id de pelicula: {}", peliculaId);
        return ResponseEntity.ok(sesionService.findByPelicula(peliculaId));
    }

    @GetMapping("/pelicula/{peliculaId}/fecha")
    public ResponseEntity<List<SesionResponseDto>> findByPeliculaAndFecha(
            @PathVariable Long peliculaId,
            @RequestParam LocalDate fecha) {
        log.info("Buscando sesiones por pelicula {} y fecha {}", peliculaId, fecha);
        return ResponseEntity.ok(sesionService.findByPeliculaAndFecha(peliculaId, fecha));
    }

    @GetMapping("/pelicula/{peliculaId}/page")
    public ResponseEntity<Page<SesionResponseDto>> findByPeliculaAndPage(
            @PathVariable Long peliculaId,
            Pageable pageable) {
        log.info("Buscando sesiones por pelicula {} en página {}", peliculaId, pageable);
        return ResponseEntity.ok(sesionService.findByPelicula(peliculaId, pageable));
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<SesionResponseDto>> findProximasSesiones() {
        log.info("Buscando las próximas sesiones de cine");
        return ResponseEntity.ok(sesionService.findProximasSesiones());
    }

    // -------------------------------------------------------------------------
    // Escritura — solo ADMIN
    // -------------------------------------------------------------------------

    @Operation(summary = "Crear sesión (ADMIN)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Sesión creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SesionResponseDto> create(@Valid @RequestBody SesionCreateDto dto) {
        log.info("Creando sesion para pelicula id={}", dto.getIdPelicula());
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.create(dto));
    }

    @Operation(summary = "Actualizar sesión (ADMIN)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sesión actualizada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN"),
        @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SesionResponseDto> update(
            @PathVariable Long id,
            @RequestBody SesionUpdateDto dto) {
        log.info("Actualizando sesion con ID: {}", id);
        return ResponseEntity.ok(sesionService.update(id, dto));
    }

    @Operation(summary = "Eliminar sesión (ADMIN)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Requiere ADMIN")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Eliminando sesion con ID: {}", id);
        sesionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
