package com.entradas_cine.ffe.cine.rest.sesiones.services;

import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SesionService {

    SesionResponseDto findById(Long id);

    List<SesionResponseDto> findByPelicula(Long peliculaId);

    List<SesionResponseDto> findByPeliculaAndFecha(Long peliculaId, LocalDate fecha);

    List<SesionResponseDto> findProximasSesiones();

    Page<SesionResponseDto> findByPelicula(Long peliculaId, Pageable pageable);

    void deleteById(Long id);
}
