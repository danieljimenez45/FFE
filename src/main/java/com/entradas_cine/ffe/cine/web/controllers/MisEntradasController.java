package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.facturas.dto.EntradaLineaDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import com.entradas_cine.ffe.cine.web.dto.EntradaCompradaView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mis-entradas")
@RequiredArgsConstructor
public class MisEntradasController {

    private final FacturaService facturaService;
    private final SesionRepository sesionRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public String misEntradas(Model model) {
        List<FacturaResponseDto> facturas = facturaService.findMisFacturas();
        Map<Long, Sesion> sesionesPorId = new HashMap<>();

        List<EntradaCompradaView> entradas = new ArrayList<>();
        for (FacturaResponseDto factura : facturas) {
            if (factura.getEntradas() == null) {
                continue;
            }
            for (EntradaLineaDto linea : factura.getEntradas()) {
                Sesion sesion = sesionesPorId.computeIfAbsent(
                        linea.getIdSesion(),
                        id -> sesionRepository.findById(id).orElse(null));
                String titulo = sesion != null && sesion.getPelicula() != null
                        ? sesion.getPelicula().getTitulo()
                        : "—";
                String horario = sesion != null ? sesion.getHorario().getDisplayName() : "—";
                String sala = sesion != null ? sesion.getSala().getDisplayName() : "—";
                LocalDate fechaSesion = sesion != null ? sesion.getFecha() : null;

                entradas.add(new EntradaCompradaView(
                        factura.getNumeroFactura(),
                        factura.getCodigoFactura(),
                        titulo,
                        fechaSesion,
                        horario,
                        sala,
                        linea.getFila(),
                        linea.getNumero(),
                        linea.getPrecio(),
                        linea.getFecha()));
            }
        }

        entradas.sort(Comparator.comparing(
                EntradaCompradaView::fechaCompra,
                Comparator.nullsLast(Comparator.reverseOrder())));

        model.addAttribute("entradas", entradas);
        return "usuario/mis_entradas";
    }
}
