package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.peliculas.services.PeliculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PeliculaWebController {

    private final PeliculaService peliculaService;

    public PeliculaWebController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping("/")
    public String listarPeliculas(Model model) {
        model.addAttribute("peliculas", peliculaService.findAll());
        return "index";
    }
}
