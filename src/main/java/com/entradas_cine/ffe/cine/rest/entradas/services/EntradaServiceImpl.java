package com.entradas_cine.ffe.cine.rest.entradas.services;

import com.entradas_cine.ffe.cine.rest.entradas.dto.ButacaOcupadaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaBadRequest;
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
    public EntradaResponseDto create(EntradaCreateDto entradaCreateDto) {
        log.info("Creating Entrada: {}", entradaCreateDto);

        Sesion sesion = sesionRepository.findById(entradaCreateDto.getIdSesion())
                .orElseThrow(() -> new EntradaNotFound(
                        "Sesion con id " + entradaCreateDto.getIdSesion() + " no encontrada"
                ));

<<<<<<< HEAD
        int maxFilas = sesion.getSala().getMaxFilas();
        int maxNumeros = sesion.getSala().getMaxNumeros();
        if (entradaCreateDto.getFila() > maxFilas) {
            throw new EntradaBadRequest(
                    "La fila " + entradaCreateDto.getFila() + " supera el maximo de filas (" + maxFilas + ") de " + sesion.getSala()
            );
        }
        if (entradaCreateDto.getNumero() > maxNumeros) {
            throw new EntradaBadRequest(
                    "El numero " + entradaCreateDto.getNumero() + " supera el maximo de butacas por fila (" + maxNumeros + ") de " + sesion.getSala()
            );
        }

=======
>>>>>>> 9b0f9153605aa8e80e5568724a31fcae18ded1f8
        boolean butacaOcupada = entradaRepository.existsBySesionAndFilaAndNumero(
                sesion,
                entradaCreateDto.getFila(),
                entradaCreateDto.getNumero()
        );

        if (butacaOcupada) {
            throw new EntradaBadRequest(
                    "La butaca fila " + entradaCreateDto.getFila() + " numero " + entradaCreateDto.getNumero()
                            + " ya esta ocupada para la sesion " + sesion.getId()
            );
        }

        Entrada entrada = entradaMapper.toEntrada(entradaCreateDto, sesion);
        Entrada saved = entradaRepository.save(entrada);

        return entradaMapper.toEntradaResponseDto(saved);
    }

    @Override
    public EntradaResponseDto findById(Long id) {
        log.info("Finding Entrada by id {}", id);

        Entrada entrada = entradaRepository.findById(id).orElseThrow(() -> new EntradaNotFound(id));
        return entradaMapper.toEntradaResponseDto(entrada);
    }

    @Override
    public List<EntradaResponseDto> findBySesion(Long idSesion) {
        log.info("Finding Entradas by sesión {}", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion)
            .orElseThrow(() -> new EntradaNotFound("Sesion con id " + idSesion + " no encontrada"));

        return entradaRepository.findBySesion(sesion)
                .stream()
                .map(entradaMapper::toEntradaResponseDto)
                .toList();
    }

    @Override
    public Page<EntradaResponseDto> findBySesion(Long idSesion, Pageable pageable) {
        log.info("Finding Entradas by sesión {} in a Page", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion)
            .orElseThrow(() -> new EntradaNotFound("Sesion con id " + idSesion + " no encontrada"));

        return entradaRepository.findBySesion(sesion, pageable)
                .map(entradaMapper::toEntradaResponseDto);
    }

    @Override
    public List<ButacaOcupadaResponseDto> findButacasOcupadasBySesion(Long idSesion) {
        log.info("Finding occupied seats by sesión {}", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new EntradaNotFound("Sesion con id " + idSesion + " no encontrada"));

        return entradaMapper.toButacasOcupadas(entradaRepository.findBySesion(sesion));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting Entrada by id {}", id);

        Entrada entrada = entradaRepository.findById(id).orElseThrow(() -> new EntradaNotFound(id));

        entradaRepository.delete(entrada);
    }
}
