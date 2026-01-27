package com.entradas_cine.ffe.cine.rest.peliculas.repositories;


import com.entradas_cine.ffe.cine.rest.peliculas.models.Genero;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long>, JpaSpecificationExecutor<Pelicula> {

    List<Pelicula> findByActivaTrue();

    List<Pelicula> findByActivaFalse();

    Page<Pelicula> findByActivaTrue(Pageable pageable);

    // ===== FILTROS =====

    List<Pelicula> findByGeneroAndActivaTrue(Genero genero);

    Page<Pelicula> findByGeneroAndActivaTrue(Genero genero, Pageable pageable);

    List<Pelicula> findByTituloContainingIgnoreCaseAndActivaTrue(String titulo);

    // ===== VALIDACIONES =====

    Optional<Pelicula> findByIdAndActivaTrue(Long id);

    boolean existsByTituloIgnoreCase(String titulo);

}
