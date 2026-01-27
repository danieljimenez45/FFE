package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.peliculas.repositories.PeliculaRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PeliculaWebController {

    private final PeliculaRepository peliculaRepository;

    public PeliculaWebController(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    @GetMapping("/")
    public String listarPeliculas(Model model){

        model.addAttribute("peliculas", peliculaRepository.findAll());
        return "index";
    }
}
