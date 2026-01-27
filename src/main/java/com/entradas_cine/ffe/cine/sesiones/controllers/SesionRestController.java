package com.entradas_cine.ffe.cine.sesiones.controllers;

import com.entradas_cine.ffe.cine.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.sesiones.services.SesionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sesiones")
@RequiredArgsConstructor
@Tag(name = "Sesiones", description = "Operaciones relacionadas con las sesiones de cine")
public class SesionRestController {

    private final SesionService sesionService;

    @GetMapping("/{id}")
    public ResponseEntity<SesionResponseDto> findById(@PathVariable Long id) {
      log.info("Buscando sesion con ID: {}", id);
        return ResponseEntity.ok(sesionService.findById(id));
    }

    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<List<SesionResponseDto>> findByPelicula(
            @PathVariable Long peliculaId){
        log.info("Buscando sesiones por el id de pelicula: {}", peliculaId);
        return ResponseEntity.ok(sesionService.findByPelicula(peliculaId));
    }

    @GetMapping("/pelicula/{peliculaId}/fecha")
    public ResponseEntity<List<SesionResponseDto>> findByPeliculaAndFecha(
            @PathVariable Long peliculaId,
            @RequestParam LocalDate fecha
    ){
        log.info("Buscando sesiones por el id de pelicula: {} y fecha: {}", peliculaId, fecha);
        return ResponseEntity.ok(sesionService.findByPeliculaAndFecha(peliculaId, fecha));
    }

    @GetMapping("/pelicula/{peliculaId}/page" )
    public ResponseEntity<Page<SesionResponseDto>> findByPeliculaAndPage(
            @PathVariable Long peliculaId,
            Pageable pageable
    ){
        log.info("Buscando sesiones por el id de pelicula: {} en la pagina: {}", peliculaId, pageable);
        return ResponseEntity.ok(
                sesionService.findByPelicula(peliculaId, pageable)
        );
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<SesionResponseDto>> findProximasSesiones(){
        log.info("Buscando las próximas sesiones de cine");
        return ResponseEntity.ok(sesionService.findProximasSesiones());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestParam Long id){
        log.info("Eliminando sesion con ID: {}", id);
        sesionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
