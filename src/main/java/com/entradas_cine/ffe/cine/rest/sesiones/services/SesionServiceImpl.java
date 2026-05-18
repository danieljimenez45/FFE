package com.entradas_cine.ffe.cine.rest.sesiones.services;

import com.entradas_cine.ffe.cine.rest.peliculas.exceptions.PeliculaNotFound;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaRepository;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionCreateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionUpdateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionBadRequest;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionNotFound;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;
import com.entradas_cine.ffe.cine.rest.sesiones.mappers.SesionMapper;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public List<SesionResponseDto> findAll() {
        return sesionRepository.findAll()
                .stream()
                .map(sesionMapper::toResponseDto)
                .toList();
    }

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
    public SesionResponseDto create(SesionCreateDto dto) {
        log.info("Creando nueva sesión para película id={}", dto.getIdPelicula());

        Pelicula pelicula = peliculaRepository.findById(dto.getIdPelicula())
                .orElseThrow(() -> new PeliculaNotFound(dto.getIdPelicula()));

        validarFechaSesion(dto.getFecha());
        validarPrecio(dto.getPrecio());
        validarSesionNoDuplicada(pelicula, dto.getFecha(), dto.getHorario(), dto.getSala(), null);

        Sesion sesion = sesionMapper.toSesionCreate(dto, pelicula);
        return sesionMapper.toResponseDto(sesionRepository.save(sesion));
    }

    @Override
    public SesionResponseDto update(Long id, SesionUpdateDto dto) {
        log.info("Actualizando sesión con ID: {}", id);

        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new SesionNotFound(id));

        sesionMapper.actualizarSesion(sesion, dto);

        validarFechaSesion(sesion.getFecha());
        validarPrecio(sesion.getPrecio());
        validarSesionNoDuplicada(
                sesion.getPelicula(), sesion.getFecha(), sesion.getHorario(), sesion.getSala(), id);

        return sesionMapper.toResponseDto(sesionRepository.save(sesion));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando sesion con ID: {}", id);

        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new SesionNotFound(id));

        sesionRepository.delete(sesion);
    }

    private void validarFechaSesion(LocalDate fecha) {
        if (fecha != null && fecha.isBefore(LocalDate.now())) {
            throw new SesionBadRequest("admin.error.sesion.fecha.pasada");
        }
    }

    private void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.signum() <= 0) {
            throw new SesionBadRequest("admin.error.sesion.precio.invalido");
        }
    }

    private void validarSesionNoDuplicada(
            Pelicula pelicula, LocalDate fecha, Horario horario, Sala sala, Long excludeId) {
        boolean duplicada = excludeId == null
                ? sesionRepository.existsByPeliculaAndFechaAndHorarioAndSala(pelicula, fecha, horario, sala)
                : sesionRepository.existsByPeliculaAndFechaAndHorarioAndSalaAndIdNot(
                        pelicula, fecha, horario, sala, excludeId);
        if (duplicada) {
            throw new SesionBadRequest("admin.error.sesion.duplicada");
        }
    }

}
