package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PeliculaWebController {

    private static final Logger logger = LoggerFactory.getLogger(PeliculaWebController.class);

    private final PeliculaRepository peliculaRepository;

    public PeliculaWebController(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    @GetMapping("/")
    @Transactional(readOnly = true)
    public String listarPeliculas(Model model) {
        List<Pelicula> peliculas = peliculaRepository.findAllWithSesiones();

        logger.info("Total películas: {}", peliculas.size());

        // Explicitly initialize sesiones to ensure they are loaded before template
        // accesses them
        for (Pelicula p : peliculas) {
            List<?> sesiones = p.getSesiones();
            logger.info("Película: {}, Sesiones: {}", p.getTitulo(),
                    sesiones != null ? sesiones.size() : "NULL");
            if (sesiones != null) {
                Hibernate.initialize(sesiones);
            }
        }

        model.addAttribute("peliculas", peliculas);
        return "index";
    }
}
