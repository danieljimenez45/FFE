package com.entradas_cine.ffe.cine.rest.sesiones.repositories;


import com.entradas_cine.ffe.cine.rest.peliculas.models.Pelicula;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {

    // Sesión concreta de una película
    Optional<Sesion> findByIdAndPelicula(Long id, Pelicula pelicula);

    // Todas las sesiones de una película
    List<Sesion> findByPelicula(Pelicula pelicula);

    // Sesiones de una película en una fecha
    List<Sesion> findByPeliculaAndFecha(Pelicula pelicula, LocalDate fecha);

    // Sesiones de una película en un horario concreto
    List<Sesion> findByPeliculaAndHorario(Pelicula pelicula, Horario horario);

    // Sesiones de una película por fecha y horario
    List<Sesion> findByPeliculaAndFechaAndHorario(
            Pelicula pelicula,
            LocalDate fecha,
            Horario horario
    );

    // Sesiones de una película paginadas
    Page<Sesion> findByPelicula(Pelicula pelicula, Pageable pageable);


    List<Sesion> findByFechaAfter(LocalDate fecha);
}
