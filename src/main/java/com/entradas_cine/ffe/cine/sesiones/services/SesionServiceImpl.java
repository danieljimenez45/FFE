package com.entradas_cine.ffe.cine.sesiones.services;

import com.entradas_cine.ffe.cine.peliculas.exceptions.PeliculaNotFound;
import com.entradas_cine.ffe.cine.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.peliculas.repositories.PeliculaRepository;
import com.entradas_cine.ffe.cine.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.sesiones.exceptions.SesionNotFound;
import com.entradas_cine.ffe.cine.sesiones.mappers.SesionMapper;
import com.entradas_cine.ffe.cine.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.sesiones.repositories.SesionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SesionServiceImpl implements  SesionService {

    private final SesionRepository sesionRepository;
    private final SesionMapper sesionMapper;
    private final PeliculaRepository peliculaRepository;

    @Override
    public SesionResponseDto findById(Long id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new SesionNotFound(id));

        return sesionMapper.toResponseDto(sesion);
    }

    @Override
    public List<SesionResponseDto> findByPelicula(Long peliculaId) {
        log.debug("Buscando sesiones para la pelicula con ID: {}", peliculaId);

        Pelicula pelicula = peliculaRepository.findById(peliculaId)
                .orElseThrow(
                        () -> new PeliculaNotFound(peliculaId)
                );

        return sesionRepository.findByPelicula(pelicula)
                .stream()
                .map(sesionMapper::toResponseDto)
                .toList();

    }

    @Override
    public List<SesionResponseDto> findByPeliculaAndFecha(Long peliculaId, LocalDate fecha){
        log.info("Buscando sesiones para la pelicula con ID: {} en la fecha: {}", peliculaId, fecha);

        Pelicula pelicula = peliculaRepository.findById(peliculaId)
                .orElseThrow(
                        () -> new PeliculaNotFound(peliculaId)
                );

        return sesionRepository.findByPeliculaAndFecha(pelicula, fecha)
                .stream()
                .map(sesionMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<SesionResponseDto> findProximasSesiones() {
        return sesionRepository.findByFechaAfter(LocalDate.now())
                .stream()
                .map(sesionMapper::toResponseDto)
                .toList();
    }

    @Override
    public Page<SesionResponseDto> findByPelicula(Long peliculaId, Pageable pageable) {
        log.info("Buscando sesiones paginadas para la pelicula con ID: {}", peliculaId);

        Pelicula pelicula = peliculaRepository.findById(peliculaId)
                .orElseThrow(() -> new PeliculaNotFound(peliculaId));

        return sesionRepository.findByPelicula(pelicula, pageable)
                .map(sesionMapper::toResponseDto);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando sesion con ID: {}", id);

        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new SesionNotFound(id));

        sesionRepository.delete(sesion);
    }

}
