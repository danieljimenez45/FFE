package com.entradas_cine.ffe.cine.rest.entradas.services;

import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaNotFound;
import com.entradas_cine.ffe.cine.rest.entradas.mappers.EntradaMapper;
import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.entradas.repositories.EntradaRepository;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntradaServiceImpl implements EntradaService {

    private final EntradaRepository entradaRepository;
    private final EntradaMapper entradaMapper;
    private final SesionRepository sesionRepository;

    @Override
    public EntradaResponseDto findById(Long id) {
        log.info("Finding Entrada by id {}", id);

        Entrada entrada = entradaRepository.findById(id).orElseThrow(() -> new EntradaNotFound(id));
        return entradaMapper.toEntradaResponseDto(entrada);
    }

    @Override
    public List<EntradaResponseDto> findBySesion(Long idSesion) {
        log.info("Finding Entradas by sesión {}", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion).orElseThrow(() -> new EntradaNotFound(idSesion));

        return entradaRepository.findBySesion(sesion)
                .stream()
                .map(entradaMapper::toEntradaResponseDto)
                .toList();
    }

    @Override
    public Page<EntradaResponseDto> findBySesion(Long idSesion, Pageable pageable) {
        log.info("Finding Entradas by sesión {} in a Page", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion).orElseThrow(() -> new EntradaNotFound(idSesion));

        return entradaRepository.findBySesion(sesion, pageable)
                .map(entradaMapper::toEntradaResponseDto);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting Entrada by id {}", id);

        Entrada entrada = entradaRepository.findById(id).orElseThrow(() -> new EntradaNotFound(id));

        entradaRepository.delete(entrada);
    }
}
