package com.entradas_cine.ffe.cine.rest.entradas.repositories;

import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    Optional<Entrada> findEntradaById(Long id);

    List<Entrada> findBySesion(Sesion sesion);

    Page<Entrada> findBySesion(Sesion sesion, Pageable pageable);

    boolean existsBySesionAndFilaAndNumero(Sesion sesion, Integer fila, Integer numero);
}
