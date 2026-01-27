package com.entradas_cine.ffe.cine.rest.peliculas.services;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaUpdateEstadoDto;

import com.entradas_cine.ffe.cine.rest.peliculas.models.ClasificacionEdad;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PeliculaService {

    PeliculaResponseDto create(PeliculaCreateDto dto);

    List<PeliculaResponseDto> findAllActivas();
    Page<PeliculaResponseDto> findAllActivasPages(Pageable pageable);

    List<PeliculaResponseDto> findByTitulo(String titulo);
    List<PeliculaResponseDto> findByGenero(Genero genero);

    PeliculaResponseDto findById(Long id);

    PeliculaResponseDto updateEstado(Long id, PeliculaUpdateEstadoDto dto);

    Page<PeliculaResponseDto> buscarAvanzado(
            Genero genero,
            ClasificacionEdad edad,
            Boolean activa,
            Pageable pageable
    );

    PeliculaResponseDto deleteById(Long id);

}
