package com.entradas_cine.ffe.cine.rest.entradas.services;

import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntradaService {

    EntradaResponseDto findById(Long id);

    List<EntradaResponseDto> findBySesion(Long idSesion);

    Page<EntradaResponseDto> findBySesion(Long idSesion, Pageable pageable);

    void deleteById(Long id);
}
