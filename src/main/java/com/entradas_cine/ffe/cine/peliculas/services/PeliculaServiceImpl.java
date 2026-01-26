package com.entradas_cine.ffe.cine.peliculas.services;

import com.entradas_cine.ffe.cine.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.peliculas.dto.PeliculaUpdateEstadoDto;
import com.entradas_cine.ffe.cine.peliculas.exceptions.PeliculaBadRequest;
import com.entradas_cine.ffe.cine.peliculas.exceptions.PeliculaNotFound;
import com.entradas_cine.ffe.cine.peliculas.mappers.PeliculaMapper;
import com.entradas_cine.ffe.cine.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.peliculas.repositories.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeliculaServiceImpl implements PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final PeliculaMapper peliculaMapper;

    @Override
    public PeliculaResponseDto findById(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() ->  new PeliculaNotFound(id));

        return peliculaMapper.toResponseDto(pelicula);
    }

    @Override
    public PeliculaResponseDto create(PeliculaCreateDto peliculaCreateDto) {
        log.info("Creando pelicula " + peliculaCreateDto);

        if(peliculaRepository.existsByTituloIgnoreCase(peliculaCreateDto.getTitulo())) {
            throw new PeliculaBadRequest("La pelicula ya existe");
        }

        Pelicula pelicula = peliculaMapper.toPeliculaCreate(peliculaCreateDto);
        pelicula.setActiva(true);

        Pelicula saved = peliculaRepository.save(pelicula);

        return peliculaMapper.toResponseDto(saved);
    }

    @Override
    public List<PeliculaResponseDto> findAllActivas() {
        return peliculaRepository.findByActivaTrue()
                .stream()
                .map(peliculaMapper::toResponseDto)
                .toList();
    }

    @Override
    public Page<PeliculaResponseDto> findAllActivasPages(Pageable pageable) {
        return peliculaRepository.findByActivaTrue(pageable)
                .map(peliculaMapper::toResponseDto);
    }

    @Override
    public List<PeliculaResponseDto> findByTitulo(String titulo) {
        return peliculaRepository
                .findByTituloContainingIgnoreCaseAndActivaTrue(titulo)
                .stream()
                .map(peliculaMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<PeliculaResponseDto> findByGenero(Pelicula.Genero genero) {
        return peliculaRepository
                .findByGeneroAndActivaTrue(genero)
                .stream()
                .map(peliculaMapper::toResponseDto)
                .toList();
    }

    @Override
    public PeliculaResponseDto updateEstado(Long id, PeliculaUpdateEstadoDto peliculaUpdateEstadoDto) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() ->  new PeliculaNotFound(id));

        peliculaMapper.actualizarEstado(pelicula, peliculaUpdateEstadoDto);
        return peliculaMapper.toResponseDto(peliculaRepository.save(pelicula));
    }


    @Override
    public Page<PeliculaResponseDto> buscarAvanzado(
            Pelicula.Genero genero,
            Pelicula.ClasificacionEdad edad,
            Boolean activa,
            Pageable pageable
    ){
        log.info("Buscando peliculas con genero: {}, clasificacionEdad: {}, activa: {}",
                genero, edad, activa);

        Specification<Pelicula> spec = (root, query, cb) ->
                cb.conjunction();

        if (genero != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("genero"), genero));
        }

        if (edad != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("clasificacionEdad"), edad));
        }

        if (activa != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("activa"), activa));
        }

        return peliculaRepository.findAll(spec, pageable)
                .map(peliculaMapper::toResponseDto);
    }

    @Override
    public PeliculaResponseDto deleteById(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() ->  new PeliculaNotFound(id));

        pelicula.setActiva(false);

        return peliculaMapper.toResponseDto(peliculaRepository.save(pelicula));
    }

    
}
