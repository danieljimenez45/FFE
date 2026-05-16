package com.entradas_cine.ffe.cine.rest.entradas.repositories;

import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    Optional<Entrada> findEntradaById(Long id);

    List<Entrada> findBySesion(Sesion sesion);

    Page<Entrada> findBySesion(Sesion sesion, Pageable pageable);

    boolean existsBySesionAndFilaAndNumero(Sesion sesion, Integer fila, Integer numero);

    @Query("""
            SELECT DISTINCT e FROM Entrada e
            JOIN Factura f JOIN f.entradas fe
            WHERE f.usuario.id = :usuarioId AND e.sesion.id = :sesionId AND fe.id = e.id
            """)
    List<Entrada> findBySesionIdAndUsuarioId(@Param("sesionId") Long sesionId,
                                             @Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT DISTINCT e FROM Entrada e
            JOIN Factura f JOIN f.entradas fe
            WHERE f.usuario.id = :usuarioId AND e.sesion.id = :sesionId AND fe.id = e.id
            """)
    Page<Entrada> findBySesionIdAndUsuarioId(@Param("sesionId") Long sesionId,
                                            @Param("usuarioId") Long usuarioId,
                                            Pageable pageable);

    /** Elimina las filas de la tabla de unión FACTURAS_ENTRADAS para todas las
     *  entradas que pertenezcan a sesiones de la película indicada. Debe ejecutarse
     *  antes de borrar la película para evitar FK violations. */
    @Modifying
    @Query(value = """
            DELETE FROM FACTURAS_ENTRADAS
            WHERE entrada_id IN (
                SELECT e.id FROM ENTRADAS e
                JOIN SESIONES s ON e.id_sesion = s.id
                WHERE s.id_pelicula = :peliculaId
            )
            """, nativeQuery = true)
    void deleteFacturasEntradasByPeliculaId(@Param("peliculaId") Long peliculaId);
}
