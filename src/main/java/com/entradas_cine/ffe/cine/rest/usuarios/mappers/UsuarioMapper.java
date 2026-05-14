package com.entradas_cine.ffe.cine.rest.usuarios.mappers;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // El rol se pasa desde el servicio, nunca se lee del DTO para evitar que el cliente se lo asigne
    public Usuario toUsuario(UsuarioCreateDto dto, String passwordCifrada, Rol rol) {
        return Usuario.builder()
                .id(null)
                .username(dto.getUsername())
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .password(passwordCifrada)
                .fechaNacimiento(dto.getFechaNacimiento())
                .rol(rol)
                .build();
    }

    public void updateUsuario(Usuario usuario, UsuarioUpdateDto dto, String passwordCifrada) {
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setRol(dto.getRol());

        if (passwordCifrada != null) {
            usuario.setPassword(passwordCifrada);
        }
    }

    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .fechaRegistro(usuario.getFechaRegistro())
                .rol(usuario.getRol())
                .build();
    }
}
