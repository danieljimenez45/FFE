package com.entradas_cine.ffe.cine.rest.entradas.mappers;


import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EntradaMapper {

    public Entrada toEntrada (EntradaCreateDto dto, Sesion sesion) {
        log.info("Mapping Entrada Create DTO to Entrada");

        return Entrada.builder()
                .id(null)
                .fila(dto.getFila())
                .numero(dto.getNumero())
                .fecha(dto.getFecha())
                .precio(dto.getPrecio())
                .sesion(sesion)
                .build();
    }

    public EntradaResponseDto toEntradaResponseDto (Entrada entrada) {
        log.info("Mapping Entrada to Entrada Response DTO");

        return EntradaResponseDto.builder()
                .id(entrada.getId())
                .fila(entrada.getFila())
                .numero(entrada.getNumero())
                .fecha(entrada.getFecha())
                .precio(entrada.getPrecio())
                .sesion(entrada.getSesion().getId())
                .build();
    }

    public List<EntradaResponseDto> toResponseDtoList (List<Entrada> entradas) {
        log.info("List Mapping Entradas to Entrada Response DTO");

        return entradas.stream()
                .map(this::toEntradaResponseDto)
                .toList();
    }

    public Page<EntradaResponseDto> toResponseDtoPage (Page<Entrada> entradas) {
        log.info("Page Mapping Entradas to Entrada Response DTO");
        return entradas.map(this::toEntradaResponseDto);
    }
}
