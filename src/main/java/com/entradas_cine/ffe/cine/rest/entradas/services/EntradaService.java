package com.entradas_cine.ffe.cine.rest.entradas.services;

import com.entradas_cine.ffe.cine.rest.entradas.dto.ButacaOcupadaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.web.dto.AdminEntradaView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntradaService {

    EntradaResponseDto create(EntradaCreateDto entradaCreateDto);

    EntradaResponseDto findById(Long id);

    List<EntradaResponseDto> findBySesion(Long idSesion);

    Page<EntradaResponseDto> findBySesion(Long idSesion, Pageable pageable);

    List<ButacaOcupadaResponseDto> findButacasOcupadasBySesion(Long idSesion);

    List<AdminEntradaView> findAllForAdmin(String locale);

    void deleteById(Long id);
}
