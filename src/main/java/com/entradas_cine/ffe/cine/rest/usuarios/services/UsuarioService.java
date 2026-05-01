package com.entradas_cine.ffe.cine.rest.usuarios.services;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDto create(UsuarioCreateDto dto);

    UsuarioResponseDto findById(Long id);

    UsuarioResponseDto findByUsername(String username);

    List<UsuarioResponseDto> findAll();

    UsuarioResponseDto update(Long id, UsuarioUpdateDto dto);

    void deleteById(Long id);
}
