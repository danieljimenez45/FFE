package com.entradas_cine.ffe.cine.rest.usuarios.services;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioBadRequest;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioNotFound;
import com.entradas_cine.ffe.cine.rest.usuarios.mappers.UsuarioMapper;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDto create(UsuarioCreateDto dto) {
        log.info("Creando usuario con username {}", dto.getUsername());

        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new UsuarioBadRequest("Ya existe un usuario con username " + dto.getUsername());
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new UsuarioBadRequest("Ya existe un usuario con email " + dto.getEmail());
        }

        String passwordCifrada = passwordEncoder.encode(dto.getPassword());
        Usuario usuario = usuarioMapper.toUsuario(dto, passwordCifrada);
        Usuario saved = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDto(saved);
    }

    @Override
    public UsuarioResponseDto findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFound(id));
        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    public UsuarioResponseDto findByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNotFound(username));
        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    public List<UsuarioResponseDto> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDto)
                .toList();
    }

    @Override
    public UsuarioResponseDto update(Long id, UsuarioUpdateDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFound(id));

        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new UsuarioBadRequest("Ya existe un usuario con email " + dto.getEmail());
        }

        String passwordCifrada = null;
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            passwordCifrada = passwordEncoder.encode(dto.getPassword());
        }

        usuarioMapper.updateUsuario(usuario, dto, passwordCifrada);
        Usuario updated = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFound(id);
        }
        usuarioRepository.deleteById(id);
    }
}
