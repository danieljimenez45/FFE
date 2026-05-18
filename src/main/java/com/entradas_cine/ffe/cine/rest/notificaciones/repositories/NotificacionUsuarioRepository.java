package com.entradas_cine.ffe.cine.rest.notificaciones.repositories;

import com.entradas_cine.ffe.cine.rest.notificaciones.models.NotificacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionUsuarioRepository extends JpaRepository<NotificacionUsuario, Long> {

    List<NotificacionUsuario> findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(Long usuarioId);

    Optional<NotificacionUsuario> findByIdAndUsuarioId(Long id, Long usuarioId);
}
