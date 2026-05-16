package com.entradas_cine.ffe.cine.rest.peliculas.repositories;

import com.entradas_cine.ffe.cine.rest.peliculas.models.PeliculaTraduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface PeliculaTraduccionRepository extends JpaRepository<PeliculaTraduccion, Long> {

    Optional<PeliculaTraduccion> findByPeliculaIdAndLocale(Long peliculaId, String locale);

    boolean existsByPeliculaId(Long peliculaId);

    @Modifying
    void deleteByPeliculaId(Long peliculaId);
}
