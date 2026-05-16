package com.entradas_cine.ffe.cine.config;

import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaRepository;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaTraduccionRepository;
import com.entradas_cine.ffe.cine.rest.peliculas.services.TraduccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TraduccionSeedRunner implements ApplicationRunner {

    @Value("${app.traduccion.auto-seed:true}")
    private boolean autoSeed;

    /** Si true, borra todas las traducciones existentes y las regenera desde cero. */
    @Value("${app.traduccion.force-reseed:false}")
    private boolean forceReseed;

    @Value("${deepl.api.key:}")
    private String deepLApiKey;

    private final PeliculaRepository           peliculaRepository;
    private final PeliculaTraduccionRepository  traduccionRepository;
    private final TraduccionService             traduccionService;

    @Override
    public void run(ApplicationArguments args) {
        if (!autoSeed) {
            log.info("TraduccionSeedRunner deshabilitado (app.traduccion.auto-seed=false)");
            return;
        }

        if (deepLApiKey == null || deepLApiKey.isBlank()) {
            log.warn("TraduccionSeedRunner: DeepL API key no configurada — se omite el seed de traducciones.");
            return;
        }

        if (forceReseed) {
            log.info("TraduccionSeedRunner: force-reseed activo — borrando todas las traducciones existentes.");
            traduccionRepository.deleteAll();
        }

        List<Long> sinTraducir = peliculaRepository.findAll()
            .stream()
            .map(Pelicula::getId)
            .filter(id -> !traduccionRepository.existsByPeliculaId(id))
            .toList();

        if (sinTraducir.isEmpty()) {
            log.info("TraduccionSeedRunner: todas las películas ya tienen traducciones.");
            return;
        }

        log.info("TraduccionSeedRunner: generando traducciones para {} película(s)...", sinTraducir.size());
        sinTraducir.forEach(traduccionService::traducirPelicula);
    }
}
