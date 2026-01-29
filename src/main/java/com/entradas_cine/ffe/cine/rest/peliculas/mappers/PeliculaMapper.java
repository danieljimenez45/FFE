package com.entradas_cine.ffe.cine.rest.peliculas.mappers;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaUpdateEstadoDto;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PeliculaMapper {

    public Pelicula toPeliculaCreate (PeliculaCreateDto dto) {
        log.info("Creando Pelicula desde CreateDto");

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setGenero(dto.getGenero());
        pelicula.setSinopsis(dto.getSinopsis());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setDirector(dto.getDirector());
        pelicula.setEstreno(dto.getEstreno());
        pelicula.setClasificacionEdad(dto.getClasificacionEdad());

        log.info("Pelicula creada: {}", pelicula);
        return pelicula;
    }

    public void actualizarEstado (Pelicula pelicula , PeliculaUpdateEstadoDto dto) {
        log.info("ACtualizando estado de la pelicula");
        pelicula.setActiva(dto.getActiva());

    }

    public PeliculaResponseDto toResponseDto(Pelicula pelicula) {
        return PeliculaResponseDto.builder()
                .id(pelicula.getId())
                .titulo(pelicula.getTitulo())
                .genero(pelicula.getGenero())
                .sinopsis(pelicula.getSinopsis())
                .duracion(pelicula.getDuracion())
                .director(pelicula.getDirector())
                .estreno(pelicula.getEstreno())
                .clasificacionEdad(pelicula.getClasificacionEdad())
                .activa(pelicula.getActiva())
                .build();
    }

    public List<PeliculaResponseDto> toResponseDtoList(List<Pelicula> peliculas) {
        return peliculas.stream()
                .map(this::toResponseDto)
                .toList();

    }


}
