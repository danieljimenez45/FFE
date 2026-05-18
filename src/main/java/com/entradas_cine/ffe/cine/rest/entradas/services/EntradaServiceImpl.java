package com.entradas_cine.ffe.cine.rest.entradas.services;

import com.entradas_cine.ffe.cine.config.auth.SecurityCurrentUser;
import com.entradas_cine.ffe.cine.rest.entradas.dto.ButacaOcupadaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaBadRequest;
import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaNotFound;
import com.entradas_cine.ffe.cine.rest.entradas.mappers.EntradaMapper;
import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.entradas.repositories.EntradaRepository;
import com.entradas_cine.ffe.cine.rest.facturas.models.Factura;
import com.entradas_cine.ffe.cine.rest.facturas.repositories.FacturaRepository;
import com.entradas_cine.ffe.cine.rest.notificaciones.services.NotificacionService;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.peliculas.services.TraduccionService;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import com.entradas_cine.ffe.cine.web.dto.AdminEntradaView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntradaServiceImpl implements EntradaService {

    private final EntradaRepository entradaRepository;
    private final EntradaMapper entradaMapper;
    private final SesionRepository sesionRepository;
    private final FacturaRepository facturaRepository;
    private final SecurityCurrentUser securityCurrentUser;
    private final TraduccionService traduccionService;
    private final NotificacionService notificacionService;

    @Override
    public EntradaResponseDto create(EntradaCreateDto entradaCreateDto) {
        log.info("Creating Entrada: {}", entradaCreateDto);

        Sesion sesion = sesionRepository.findById(entradaCreateDto.getIdSesion())
                .orElseThrow(() -> new EntradaNotFound(
                        "Sesion con id " + entradaCreateDto.getIdSesion() + " no encontrada"
                ));

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
    @Transactional(readOnly = true)
    public EntradaResponseDto findById(Long id) {
        log.info("Finding Entrada by id {}", id);

        Entrada entrada = entradaRepository.findById(id).orElseThrow(() -> new EntradaNotFound(id));
        if (!puedeVerEntrada(entrada)) {
            log.warn("Acceso denegado: usuario '{}' intentó ver la entrada {}",
                    securityCurrentUser.getUsername(), id);
            throw new EntradaNotFound(id);
        }
        return entradaMapper.toEntradaResponseDto(entrada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaResponseDto> findBySesion(Long idSesion) {
        log.info("Finding Entradas by sesión {}", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new EntradaNotFound("Sesion con id " + idSesion + " no encontrada"));

        List<Entrada> entradas = securityCurrentUser.isAdmin()
                ? entradaRepository.findBySesion(sesion)
                : entradaRepository.findBySesionIdAndUsuarioId(idSesion, securityCurrentUser.getUserId());

        return entradas.stream()
                .map(entradaMapper::toEntradaResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EntradaResponseDto> findBySesion(Long idSesion, Pageable pageable) {
        log.info("Finding Entradas by sesión {} in a Page", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new EntradaNotFound("Sesion con id " + idSesion + " no encontrada"));

        Page<Entrada> page = securityCurrentUser.isAdmin()
                ? entradaRepository.findBySesion(sesion, pageable)
                : entradaRepository.findBySesionIdAndUsuarioId(idSesion, securityCurrentUser.getUserId(), pageable);

        return page.map(entradaMapper::toEntradaResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ButacaOcupadaResponseDto> findButacasOcupadasBySesion(Long idSesion) {
        log.info("Finding occupied seats by sesión {}", idSesion);

        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new EntradaNotFound("Sesion con id " + idSesion + " no encontrada"));

        return entradaMapper.toButacasOcupadas(entradaRepository.findBySesion(sesion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminEntradaView> findAllForAdmin(String locale) {
        return entradaRepository.findAllForAdmin().stream()
                .map(row -> new AdminEntradaView(
                        row.id(),
                        row.username(),
                        row.nombre(),
                        row.apellidos(),
                        row.peliculaId(),
                        traduccionService.obtenerTituloTraducido(
                                row.peliculaId(), row.tituloPelicula(), locale),
                        row.fechaSesion(),
                        row.horario(),
                        row.sala(),
                        row.fila(),
                        row.numero(),
                        row.precio(),
                        row.fechaCompra(),
                        row.numeroFactura(),
                        row.codigoFactura()))
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting Entrada by id {}", id);

        Entrada entrada = entradaRepository.findById(id).orElseThrow(() -> new EntradaNotFound(id));
        Optional<Factura> facturaOpt = facturaRepository.findByEntradas_Id(id);

        String peliculaTitulo = null;
        var sesion = entrada.getSesion();
        if (sesion != null && sesion.getPelicula() != null) {
            peliculaTitulo = sesion.getPelicula().getTitulo();
        }

        Usuario usuarioAfectado = facturaOpt.map(Factura::getUsuario).orElse(null);
        var fechaSesion = sesion != null ? sesion.getFecha() : null;
        var horario = sesion != null && sesion.getHorario() != null
                ? sesion.getHorario().getDisplayName() : "—";
        var sala = sesion != null && sesion.getSala() != null
                ? sesion.getSala().getDisplayName() : "—";
        Float importe = entrada.getPrecio();

        if (facturaOpt.isPresent()) {
            entradaRepository.deleteFacturaEntradaLink(id);
        }

        entradaRepository.delete(entrada);
        entradaRepository.flush();

        facturaOpt.ifPresent(factura -> {
            Factura refreshed = facturaRepository.findById(factura.getId()).orElse(factura);
            if (refreshed.getEntradas() == null || refreshed.getEntradas().isEmpty()) {
                facturaRepository.delete(refreshed);
            }
        });

        if (usuarioAfectado != null && peliculaTitulo != null && fechaSesion != null) {
            notificacionService.crearAvisoReembolsoEntrada(
                    usuarioAfectado, peliculaTitulo, fechaSesion, horario, sala, importe);
        }
    }

    private boolean puedeVerEntrada(Entrada entrada) {
        if (securityCurrentUser.isAdmin()) {
            return true;
        }
        return facturaRepository.existsByUsuario_IdAndEntradas_Id(
                securityCurrentUser.getUserId(), entrada.getId());
    }
}
