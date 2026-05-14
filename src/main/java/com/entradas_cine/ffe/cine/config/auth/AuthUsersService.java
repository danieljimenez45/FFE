package com.entradas_cine.ffe.cine.config.auth;

import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Busca en base de datos al usuario por su nombre de usuario y lo prepara
 * para que Spring Security sepa quién es y qué rol tiene (usuario normal o admin).
 * Lo usan el login con JWT y el filtro que valida el token en cada petición.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUsersService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("Cargando usuario por username: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        // Mapeo de Rol a authority de Spring Security
        String authority = switch (usuario.getRol()) {
            case ADMIN -> "ROLE_ADMIN";
            case USER  -> "ROLE_USER";
        };

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(authority)))
                .build();
    }
}
