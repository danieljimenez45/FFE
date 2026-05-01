package com.entradas_cine.ffe.cine.rest.entradas.controllers;

import com.entradas_cine.ffe.cine.rest.entradas.dto.ButacaOcupadaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/entradas")
@RequiredArgsConstructor
@Tag(name = "Entradas", description = "Operaciones relacionadas con la compra y consulta de entradas")
public class EntradaRestController {
    private final EntradaService entradaService;

    @PostMapping
    public ResponseEntity<EntradaResponseDto> create(@RequestBody @Valid EntradaCreateDto entradaCreateDto) {
        log.info("EntradaRestController.create: {}", entradaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(entradaService.create(entradaCreateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaResponseDto> findById(@PathVariable Long id) {
        log.info("EntradaRestController.findById: {}", id);
        return ResponseEntity.ok(entradaService.findById(id));
    }

    @GetMapping("/sesion/{id}")
    public ResponseEntity<List<EntradaResponseDto>> findBySesion(@PathVariable Long id) {
        log.info("EntradaRestController.findBySesion: {}", id);

        return ResponseEntity.ok(
                entradaService.findBySesion(id));
    }

    @GetMapping("/sesion/{id}/page")
    public ResponseEntity<Page<EntradaResponseDto>> findBySesionPage(@PathVariable Long id, Pageable pageable) {
        log.info("EntradaRestController.findBySesionPage: {} - {}", id, pageable);
        return ResponseEntity.ok(entradaService.findBySesion(id, pageable));
    }

    @GetMapping("/sesion/{id}/butacas-ocupadas")
    public ResponseEntity<List<ButacaOcupadaResponseDto>> findButacasOcupadasBySesion(@PathVariable Long id) {
        log.info("EntradaRestController.findButacasOcupadasBySesion: {}", id);
        return ResponseEntity.ok(entradaService.findButacasOcupadasBySesion(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("EntradaRestController.deleteById: {}", id);
        entradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
