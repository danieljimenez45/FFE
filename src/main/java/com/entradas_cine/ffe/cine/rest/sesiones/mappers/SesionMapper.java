package com.entradas_cine.ffe.cine.rest.sesiones.mappers;

import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionCreateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionUpdateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SesionMapper {

    public Sesion toSesionCreate(SesionCreateDto dto, Pelicula pelicula) {
        log.info("Creando sesión");

        Sesion sesion = new Sesion();
        sesion.setPelicula(pelicula);
        sesion.setFecha(dto.getFecha());
        sesion.setHorario(dto.getHorario());
        sesion.setSala(dto.getSala());
        sesion.setTipoProyeccion(dto.getTipoProyeccion());
        sesion.setPrecio(dto.getPrecio());

        return sesion;
    }

    public void actualizarSesion(Sesion sesion, SesionUpdateDto dto) {
        log.info("Actualizando sesión");

        if (dto.getHorario() != null) {
            sesion.setHorario(dto.getHorario());
        }
        if (dto.getSala() != null) {
            sesion.setSala(dto.getSala());
        }
        if (dto.getPrecio() != null) {
            sesion.setPrecio(dto.getPrecio());
        }
    }

    public SesionResponseDto toResponseDto(Sesion sesion) {
        return SesionResponseDto.builder()
                .id(sesion.getId())
                .pelicula(sesion.getPelicula().getTitulo())
                .fecha(sesion.getFecha())
                .horario(sesion.getHorario())
                .sala(sesion.getSala())
                .tipoProyeccion(sesion.getTipoProyeccion())
                .precio(sesion.getPrecio())
                .build();
    }

    public List<SesionResponseDto> toResponseDtoList(List<Sesion> sesiones) {
        return sesiones.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
