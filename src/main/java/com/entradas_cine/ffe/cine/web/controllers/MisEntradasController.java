package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.facturas.dto.EntradaLineaDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import com.entradas_cine.ffe.cine.rest.notificaciones.models.NotificacionUsuario;
import com.entradas_cine.ffe.cine.rest.notificaciones.services.NotificacionService;
import com.entradas_cine.ffe.cine.web.services.I18nService;
import com.entradas_cine.ffe.cine.rest.peliculas.services.TraduccionService;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import com.entradas_cine.ffe.cine.web.dto.EntradaCompradaView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final FacturaService    facturaService;
    private final SesionRepository  sesionRepository;
    private final TraduccionService  traduccionService;
    private final NotificacionService notificacionService;
    private final I18nService i18n;

    @GetMapping
    @Transactional(readOnly = true)
    public String misEntradas(Model model) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
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
                String titulo = "—";
                if (sesion != null && sesion.getPelicula() != null) {
                    titulo = traduccionService.obtenerTituloTraducido(
                            sesion.getPelicula().getId(),
                            sesion.getPelicula().getTitulo(),
                            locale);
                }
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
        model.addAttribute("notificaciones", buildNotificacionesVista());
        return "usuario/mis_entradas";
    }

    @PostMapping("/notificaciones/{id}/leida")
    public String marcarNotificacionLeida(@PathVariable Long id, RedirectAttributes ra) {
        notificacionService.marcarLeida(id);
        return "redirect:/mis-entradas";
    }

    private List<Map<String, Object>> buildNotificacionesVista() {
        List<Map<String, Object>> vista = new ArrayList<>();
        for (NotificacionUsuario n : notificacionService.findNoLeidasDelUsuarioActual()) {
            String importeStr = String.format("%.2f", n.getImporte() != null ? n.getImporte() : 0f);
            Map<String, Object> item = new HashMap<>();
            item.put("id", n.getId());
            item.put("titulo", i18n.getMessage("notificacion.reembolso.titulo"));
            item.put("cuerpo", i18n.getMessage("notificacion.reembolso.cuerpo", new Object[]{
                    n.getPeliculaTitulo(),
                    n.getFechaSesion(),
                    n.getHorario(),
                    n.getSala(),
                    importeStr
            }));
            vista.add(item);
        }
        return vista;
    }
}
