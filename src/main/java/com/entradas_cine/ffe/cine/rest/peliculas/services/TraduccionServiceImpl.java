package com.entradas_cine.ffe.cine.rest.peliculas.services;

import com.entradas_cine.ffe.cine.config.DeepLService;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.rest.peliculas.models.PeliculaTraduccion;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaRepository;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaTraduccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraduccionServiceImpl implements TraduccionService {

    /** Idiomas destino — el español (es) vive directamente en la tabla PELICULAS */
    private static final List<String> LOCALES_DESTINO = List.of("en", "fr", "de", "pt");

    private final PeliculaTraduccionRepository traduccionRepository;
    private final PeliculaRepository           peliculaRepository;
    private final DeepLService                  deepLService;

    /**
     * Se ejecuta en un hilo separado para no bloquear la petición del administrador.
     * Recarga la película desde BD (el guardado del padre ya ha hecho commit antes de
     * que este metodo empiece, porque PeliculaServiceImpl no abre transacción en create/update).
     */
    @Override
    @Async("traduccionExecutor")
    @Transactional
    public void traducirPelicula(Long peliculaId) {
        Optional<Pelicula> opt = peliculaRepository.findById(peliculaId);
        if (opt.isEmpty()) {
            log.warn("traducirPelicula: película id={} no encontrada, se cancela la traducción", peliculaId);
            return;
        }
        Pelicula pelicula = opt.get();

        // Borrar traducciones previas (caso de edición)
        traduccionRepository.deleteByPeliculaId(peliculaId);

        log.info("Generando traducciones para película id={} «{}»", peliculaId, pelicula.getTitulo());

        for (String locale : LOCALES_DESTINO) {
            try {
                String titulo   = deepLService.traducir(pelicula.getTitulo(),   locale);
                String sinopsis = deepLService.traducir(pelicula.getSinopsis(), locale);

                traduccionRepository.save(
                    PeliculaTraduccion.builder()
                        .pelicula(pelicula)
                        .locale(locale)
                        .titulo(titulo)
                        .sinopsis(sinopsis)
                        .build()
                );
                log.info("  → traducción '{}' guardada para id={}", locale, peliculaId);

                // Pausa entre locales para no saturar el rate-limit de DeepL free
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.warn("  → traducción interrumpida para id={}", peliculaId);
                return;
            } catch (Exception e) {
                log.error("  → error traduciendo '{}' para id={}: {}", locale, peliculaId, e.getMessage());
            }
        }
    }

    @Override
    public PeliculaResponseDto aplicarTraduccion(PeliculaResponseDto dto, String locale) {
        if (locale == null || locale.equals("es")) return dto;

        traduccionRepository
            .findByPeliculaIdAndLocale(dto.getId(), locale)
            .ifPresent(t -> {
                dto.setTitulo(t.getTitulo());
                dto.setSinopsis(t.getSinopsis());
            });

        return dto;
    }

    @Override
    public List<PeliculaResponseDto> aplicarTraducciones(List<PeliculaResponseDto> dtos, String locale) {
        if (locale == null || locale.equals("es")) return dtos;
        dtos.forEach(dto -> aplicarTraduccion(dto, locale));
        return dtos;
    }

    @Override
    public String obtenerTituloTraducido(Long peliculaId, String tituloOriginal, String locale) {
        if (locale == null || locale.equals("es")) return tituloOriginal;
        return traduccionRepository
                .findByPeliculaIdAndLocale(peliculaId, locale)
                .map(PeliculaTraduccion::getTitulo)
                .orElse(tituloOriginal);
    }
}
