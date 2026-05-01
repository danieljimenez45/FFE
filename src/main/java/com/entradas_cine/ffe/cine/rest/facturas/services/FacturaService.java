package com.entradas_cine.ffe.cine.rest.facturas.services;

import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;

import java.util.List;

public interface FacturaService {

    FacturaResponseDto create(FacturaCreateDto dto);

    FacturaResponseDto findById(Long id);

    List<FacturaResponseDto> findAll();

    List<FacturaResponseDto> findByUsuarioId(Long usuarioId);

    void deleteById(Long id);
}
