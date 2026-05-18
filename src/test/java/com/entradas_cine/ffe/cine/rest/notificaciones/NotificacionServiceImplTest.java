package com.entradas_cine.ffe.cine.rest.notificaciones;

import com.entradas_cine.ffe.cine.rest.notificaciones.repositories.NotificacionUsuarioRepository;
import com.entradas_cine.ffe.cine.rest.notificaciones.services.NotificacionService;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class NotificacionServiceImplTest {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionUsuarioRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    void limpiarSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void marcarLeida_deberiaMarcarNotificacionDelUsuarioActual() {
        var laura = usuarioRepository.findByUsername("laura").orElseThrow();
        notificacionService.crearAvisoReembolsoEntrada(
                laura, "Test Film", LocalDate.of(2026, 6, 1),
                "18:00", "Sala 1", 8.5f);

        var creada = notificacionRepository
                .findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(laura.getId())
                .getFirst();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "laura", "x", List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        notificacionService.marcarLeida(creada.getId());

        assertThat(notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(laura.getId()))
                .isEmpty();
        assertThat(notificacionRepository.findById(creada.getId()).orElseThrow().isLeida()).isTrue();
    }
}
