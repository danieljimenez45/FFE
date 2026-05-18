package com.entradas_cine.ffe.cine.rest.notificaciones.services;

import com.entradas_cine.ffe.cine.config.auth.SecurityCurrentUser;
import com.entradas_cine.ffe.cine.rest.notificaciones.models.NotificacionUsuario;
import com.entradas_cine.ffe.cine.rest.notificaciones.models.TipoNotificacion;
import com.entradas_cine.ffe.cine.rest.notificaciones.repositories.NotificacionUsuarioRepository;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionUsuarioRepository notificacionRepository;
    private final SecurityCurrentUser securityCurrentUser;

    @Override
    @Transactional
    public void crearAvisoReembolsoEntrada(
            Usuario usuario,
            String peliculaTitulo,
            LocalDate fechaSesion,
            String horario,
            String sala,
            Float importe) {

        NotificacionUsuario notificacion = NotificacionUsuario.builder()
                .usuario(usuario)
                .tipo(TipoNotificacion.REEMBOLSO_ENTRADA)
                .peliculaTitulo(peliculaTitulo)
                .fechaSesion(fechaSesion)
                .horario(horario)
                .sala(sala)
                .importe(importe != null ? importe : 0f)
                .leida(false)
                .build();

        notificacionRepository.save(notificacion);
        log.info("Notificación de reembolso creada para usuario {}", usuario.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionUsuario> findNoLeidasDelUsuarioActual() {
        return notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(
                securityCurrentUser.getUserId());
    }

    @Override
    @Transactional
    public void marcarLeida(Long notificacionId) {
        NotificacionUsuario notificacion = notificacionRepository
                .findByIdAndUsuarioId(notificacionId, securityCurrentUser.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }
}
