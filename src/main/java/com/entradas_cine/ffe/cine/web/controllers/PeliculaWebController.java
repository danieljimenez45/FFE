package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.services.PeliculaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PeliculaWebController {

    private static final Logger logger = LoggerFactory.getLogger(PeliculaWebController.class);

    private final PeliculaService peliculaService;

    public PeliculaWebController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
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
    public String detallePelicula(@PathVariable Long id, Model model) {

        PeliculaResponseDto pelicula = peliculaService.findById(id);

        model.addAttribute("pelicula", pelicula);

        return "peliculas/detalle_pelicula";
    }

    @GetMapping("/peliculas/lista_peliculas")
    @Transactional(readOnly = true)
    public String listaDePeliculas(Model model) {
        List<PeliculaResponseDto> peliculas = peliculaService.findAll();

        logger.info("Total películas: {}", peliculas.size());

        model.addAttribute("peliculas", peliculas);

        return "peliculas/lista_peliculas";
    }

}
