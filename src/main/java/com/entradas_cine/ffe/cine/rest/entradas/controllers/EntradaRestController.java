package com.entradas_cine.ffe.cine.rest.entradas.controllers;

import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.services.SesionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/entradas")
@RequiredArgsConstructor
public class EntradaRestController {
    private final EntradaService entradaService;
    private final SesionService sesionService;

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


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        BindingResult result = ex.getBindingResult();
        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                + "'. " + "Núm. errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        result.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }
}
