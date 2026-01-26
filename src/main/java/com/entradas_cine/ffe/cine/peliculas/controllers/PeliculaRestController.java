package com.entradas_cine.ffe.cine.peliculas.controllers;

import com.entradas_cine.ffe.cine.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.peliculas.dto.PeliculaUpdateEstadoDto;
import com.entradas_cine.ffe.cine.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.peliculas.services.PeliculaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/peliculas")
@RequiredArgsConstructor
@Tag(name = "Peliculas", description = "Operaciones relacionadas con las peliculas de cine")
public class PeliculaRestController {

    private final PeliculaService peliculaService;


    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PeliculaResponseDto>> findAllActivas() {
        return ResponseEntity.ok(peliculaService.findAllActivas());
    }

    @GetMapping("/activas/page")
    public ResponseEntity<Page<PeliculaResponseDto>> findAllActivasPages(Pageable pageable) {
        return ResponseEntity.ok(peliculaService.findAllActivasPages(pageable));
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<PeliculaResponseDto>> findByTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(peliculaService.findByTitulo(titulo));
    }

    @GetMapping("/buscar/genero")
    public ResponseEntity<List<PeliculaResponseDto>> findByGenero(@RequestParam Pelicula.Genero genero) {
        return ResponseEntity.ok(peliculaService.findByGenero(genero));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<PeliculaResponseDto>> buscarAvanzado(
            @RequestParam(required = false) Pelicula.Genero genero,
            @RequestParam(required = false) Pelicula.ClasificacionEdad edad,
            @RequestParam(required = false) Boolean activa,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                peliculaService.buscarAvanzado(genero, edad, activa, pageable)
        );
    }

    @PostMapping
    public ResponseEntity<PeliculaResponseDto> create(
            @RequestBody @Valid PeliculaCreateDto peliculaCreateDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(peliculaService.create(peliculaCreateDto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PeliculaResponseDto> updateEstado(
            @PathVariable Long id,
            @RequestBody @Valid PeliculaUpdateEstadoDto peliculaUpdateEstadoDto)
    {
        return ResponseEntity.ok(
                peliculaService.updateEstado(id, peliculaUpdateEstadoDto)
        );
    }

}
