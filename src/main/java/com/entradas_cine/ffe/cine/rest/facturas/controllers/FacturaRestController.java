package com.entradas_cine.ffe.cine.rest.facturas.controllers;

import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
@Tag(name = "Facturas", description = "Operaciones relacionadas con las facturas")
public class FacturaRestController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaResponseDto> create(@RequestBody @Valid FacturaCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponseDto>> findAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.findById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FacturaResponseDto>> findByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(facturaService.findByUsuarioId(usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}