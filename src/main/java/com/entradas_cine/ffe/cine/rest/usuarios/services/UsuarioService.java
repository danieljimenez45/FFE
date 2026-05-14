package com.entradas_cine.ffe.cine.rest.usuarios.services;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;

import java.util.List;

public interface UsuarioService {

    /** Crea un usuario con rol USER (auto-registro o alta por admin de usuario estándar). */
    UsuarioResponseDto create(UsuarioCreateDto dto);

    /** Crea un usuario con rol ADMIN. Solo debe llamarse desde un endpoint protegido por ADMIN. */
    UsuarioResponseDto createAdmin(UsuarioCreateDto dto);

    UsuarioResponseDto findById(Long id);

    UsuarioResponseDto findByUsername(String username);

    List<UsuarioResponseDto> findAll();

    UsuarioResponseDto update(Long id, UsuarioUpdateDto dto);

    void deleteById(Long id);
}
