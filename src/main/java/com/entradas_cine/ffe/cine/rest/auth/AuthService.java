package com.entradas_cine.ffe.cine.rest.auth;

import com.entradas_cine.ffe.cine.config.auth.AuthUsersService;
import com.entradas_cine.ffe.cine.config.auth.JwtService;
import com.entradas_cine.ffe.cine.rest.auth.dto.JwtAuthResponse;
import com.entradas_cine.ffe.cine.rest.auth.dto.SignInRequest;
import com.entradas_cine.ffe.cine.rest.auth.dto.SignUpRequest;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthUsersService authUsersService;
    private final AuthenticationManager authenticationManager;

    /**
     * Crea la cuenta con contraseña cifrada y guarda siempre el rol de usuario estándar,
     * sin fiarse de lo que venga en la petición (el cuerpo ni siquiera incluye campo rol).
     */
    public JwtAuthResponse signup(SignUpRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un usuario con username " + request.getUsername());
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un usuario con email " + request.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fechaNacimiento(request.getFechaNacimiento())
                .rol(Rol.USER)
                .build();

        usuarioRepository.save(usuario);
        log.info("Usuario registrado vía signup: {} (rol USER forzado)", usuario.getUsername());

        UserDetails userDetails =
                authUsersService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);
        return new JwtAuthResponse(token);
    }

    public JwtAuthResponse signin(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex);
        }

        UserDetails userDetails =
                authUsersService.loadUserByUsername(request.getUsername());
        return new JwtAuthResponse(jwtService.generateToken(userDetails));
    }
}
