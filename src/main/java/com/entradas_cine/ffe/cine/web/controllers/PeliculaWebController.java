package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.services.PeliculaService;
import com.entradas_cine.ffe.cine.rest.peliculas.services.TraduccionService;
import com.entradas_cine.ffe.cine.rest.sesiones.services.SesionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PeliculaWebController {

    private static final Logger logger = LoggerFactory.getLogger(PeliculaWebController.class);

    private final PeliculaService    peliculaService;
    private final SesionService      sesionService;
    private final TraduccionService  traduccionService;

    public PeliculaWebController(PeliculaService peliculaService,
                                 SesionService sesionService,
                                 TraduccionService traduccionService) {
        this.peliculaService   = peliculaService;
        this.sesionService     = sesionService;
        this.traduccionService = traduccionService;
    }

    @GetMapping("/")
    @Transactional(readOnly = true)
    public String listarPeliculas(Model model) {
        List<PeliculaResponseDto> peliculas = peliculaService.findAll();
        logger.info("Total películas: {}", peliculas.size());
        model.addAttribute("peliculas", peliculas);
        return "index";
    }

    @GetMapping("/peliculas/detalle/{id}")
    @Transactional(readOnly = true)
    public String detallePelicula(@PathVariable Long id, Model model) {
        String locale = LocaleContextHolder.getLocale().getLanguage();

        PeliculaResponseDto pelicula = traduccionService.aplicarTraduccion(
            peliculaService.findById(id), locale
        );
        var sesiones = sesionService.findByPelicula(id);

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("sesiones", sesiones);
        return "peliculas/detalle_pelicula";
    }

    @GetMapping("/peliculas/lista_peliculas")
    @Transactional(readOnly = true)
    public String listaDePeliculas(Model model) {
        String locale = LocaleContextHolder.getLocale().getLanguage();

        List<PeliculaResponseDto> peliculas = traduccionService.aplicarTraducciones(
            peliculaService.findAll(), locale
        );
        logger.info("Total películas (locale={}): {}", locale, peliculas.size());

        model.addAttribute("peliculas", peliculas);
        model.addAttribute("mostrarBuscador", true);
        return "peliculas/lista_peliculas";
    }
}
