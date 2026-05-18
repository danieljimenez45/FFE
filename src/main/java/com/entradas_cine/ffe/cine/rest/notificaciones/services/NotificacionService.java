package com.entradas_cine.ffe.cine.rest.notificaciones.services;

import com.entradas_cine.ffe.cine.rest.notificaciones.models.NotificacionUsuario;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface NotificacionService {

    void crearAvisoReembolsoEntrada(
            Usuario usuario,
            String peliculaTitulo,
            LocalDate fechaSesion,
            String horario,
            String sala,
            Float importe);

    List<NotificacionUsuario> findNoLeidasDelUsuarioActual();

    void marcarLeida(Long notificacionId);
}
