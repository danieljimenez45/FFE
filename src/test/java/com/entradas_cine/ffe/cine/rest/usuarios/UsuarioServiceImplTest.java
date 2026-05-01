package com.entradas_cine.ffe.cine.rest.usuarios;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioBadRequest;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioNotFound;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import com.entradas_cine.ffe.cine.rest.usuarios.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsuarioServiceImplTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void create_deberiaCrearUsuarioConFechaRegistroYRolPorDefecto() {
        UsuarioCreateDto dto = UsuarioCreateDto.builder()
                .username("jperez")
                .nombre("Juan")
                .apellidos("Perez Garcia")
                .email("juan.perez@email.com")
                .password("secreto123")
                .fechaNacimiento(LocalDate.of(1998, 7, 15))
                .build();

        UsuarioResponseDto result = usuarioService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jperez");
        assertThat(result.getRol()).isEqualTo(Rol.USER);
        assertThat(result.getFechaRegistro()).isNotNull();

        Usuario guardado = usuarioRepository.findById(result.getId()).orElseThrow();
        assertThat(guardado.getPassword()).isNotEqualTo("secreto123");
        assertThat(passwordEncoder.matches("secreto123", guardado.getPassword())).isTrue();
    }

    @Test
    void create_deberiaFallarSiUsernameYaExiste() {
        UsuarioCreateDto dto = UsuarioCreateDto.builder()
                                .username("admin_test")
                .nombre("Admin")
                .apellidos("Uno")
                .email("admin1@email.com")
                .password("secreto123")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .rol(Rol.ADMIN)
                .build();

        usuarioService.create(dto);

        UsuarioCreateDto repetido = UsuarioCreateDto.builder()
                .username("admin_test")
                .nombre("Otra")
                .apellidos("Persona")
                .email("admin2@email.com")
                .password("secreto123")
                .fechaNacimiento(LocalDate.of(1991, 1, 1))
                .build();

        assertThatThrownBy(() -> usuarioService.create(repetido))
                .isInstanceOf(UsuarioBadRequest.class)
                .hasMessageContaining("username");
    }

    @Test
    void create_deberiaFallarSiEmailYaExiste() {
        UsuarioCreateDto dto = UsuarioCreateDto.builder()
                .username("u1")
                .nombre("U")
                .apellidos("Uno")
                .email("mismo@email.com")
                .password("secreto123")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        usuarioService.create(dto);

        UsuarioCreateDto repetido = UsuarioCreateDto.builder()
                .username("u2")
                .nombre("U")
                .apellidos("Dos")
                .email("mismo@email.com")
                .password("secreto123")
                .fechaNacimiento(LocalDate.of(1992, 1, 1))
                .build();

        assertThatThrownBy(() -> usuarioService.create(repetido))
                .isInstanceOf(UsuarioBadRequest.class)
                .hasMessageContaining("email");
    }

    @Test
    void update_deberiaActualizarDatosYCifrarPasswordSiSeEnvia() {
        UsuarioResponseDto created = usuarioService.create(UsuarioCreateDto.builder()
                .username("maria")
                .nombre("Maria")
                .apellidos("Lopez")
                .email("maria@email.com")
                .password("secreto123")
                .fechaNacimiento(LocalDate.of(1995, 3, 10))
                .build());

        UsuarioUpdateDto updateDto = UsuarioUpdateDto.builder()
                .nombre("Maria Jose")
                .apellidos("Lopez Diaz")
                .email("maria.jose@email.com")
                .password("nuevoSecreto")
                .fechaNacimiento(LocalDate.of(1995, 3, 10))
                .rol(Rol.ADMIN)
                .build();

        UsuarioResponseDto updated = usuarioService.update(created.getId(), updateDto);

        assertThat(updated.getNombre()).isEqualTo("Maria Jose");
        assertThat(updated.getRol()).isEqualTo(Rol.ADMIN);

        Usuario guardado = usuarioRepository.findById(created.getId()).orElseThrow();
        assertThat(passwordEncoder.matches("nuevoSecreto", guardado.getPassword())).isTrue();
    }

    @Test
    void findById_deberiaFallarSiNoExiste() {
        assertThatThrownBy(() -> usuarioService.findById(9999L))
                .isInstanceOf(UsuarioNotFound.class);
    }
}
