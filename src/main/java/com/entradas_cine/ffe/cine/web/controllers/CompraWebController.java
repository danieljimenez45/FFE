package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CompraWebController {

    private final EntradaService entradaService;
    private final FacturaService facturaService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Procesa la compra de butacas:
     * 1. Crea una Entrada por cada butaca seleccionada.
     * 2. Crea una Factura con todas esas entradas.
     * 3. Redirige a la página de preview del PDF de la factura.
     *
     * Requiere sesión iniciada (cubierto por SecurityConfig → anyRequest().authenticated()).
     */
    @PostMapping("/sesiones/{id}/comprar")
    public String comprar(
            @PathVariable Long id,
            @RequestParam String butacas,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttrs) {

        if (butacas == null || butacas.isBlank()) {
            redirectAttrs.addFlashAttribute("errorCompra", "Debes seleccionar al menos una butaca.");
            return "redirect:/sesiones/" + id + "/butacas";
        }

        Long idUsuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getId();

        List<Long> idEntradas = new ArrayList<>();
        for (String butacaKey : butacas.split(",")) {
            String[] parts = butacaKey.trim().split("-");
            if (parts.length != 2) continue;
            int fila   = Integer.parseInt(parts[0]);
            int numero = Integer.parseInt(parts[1]);

            EntradaResponseDto entrada = entradaService.create(
                    EntradaCreateDto.builder()
                            .idSesion(id)
                            .fila(fila)
                            .numero(numero)
                            .build());
            idEntradas.add(entrada.getId());
        }

        if (idEntradas.isEmpty()) {
            redirectAttrs.addFlashAttribute("errorCompra", "No se pudo procesar ninguna butaca.");
            return "redirect:/sesiones/" + id + "/butacas";
        }

        FacturaResponseDto factura = facturaService.create(
                FacturaCreateDto.builder()
                        .idUsuario(idUsuario)
                        .idEntradas(idEntradas)
                        .build());

        log.info("Factura {} creada para usuario {}", factura.getCodigoFactura(), userDetails.getUsername());
        return "redirect:/facturas/" + factura.getNumeroFactura() + "/pdf";
    }
}
