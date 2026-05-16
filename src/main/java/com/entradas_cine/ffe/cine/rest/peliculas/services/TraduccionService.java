package com.entradas_cine.ffe.cine.rest.peliculas.services;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;

import java.util.List;

public interface TraduccionService {

    /**
     * Genera (o regenera) las traducciones de una película en todos los idiomas
     * configurados, de forma asíncrona.
     *
     * @param peliculaId ID de la película ya persistida en BD.
     */
    void traducirPelicula(Long peliculaId);

    /**
     * Sustituye título y sinopsis de un DTO con la traducción almacenada para el locale
     * indicado. Si no existe traducción, el DTO se devuelve sin cambios (fallback español).
     */
    PeliculaResponseDto aplicarTraduccion(PeliculaResponseDto dto, String locale);

    /**
     * Aplica {@link #aplicarTraduccion} a toda una lista.
     */
    List<PeliculaResponseDto> aplicarTraducciones(List<PeliculaResponseDto> dtos, String locale);

    /**
     * Devuelve el título traducido para el locale dado.
     * Si no existe traducción, devuelve el título original (fallback español).
     */
    String obtenerTituloTraducido(Long peliculaId, String tituloOriginal, String locale);
}
