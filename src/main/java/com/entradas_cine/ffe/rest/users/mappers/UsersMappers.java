package com.entradas_cine.ffe.rest.users.mappers;


import com.entradas_cine.ffe.rest.users.dto.UserInfoResponse;
import com.entradas_cine.ffe.rest.users.dto.UserRequest;
import com.entradas_cine.ffe.rest.users.dto.UserResponse;
import com.entradas_cine.ffe.rest.users.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersMappers {
    public User toUser(UserRequest request) {
        return User.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .estado(request.getEstados())
                .build();
    }

    public User toUser(UserRequest request, Long id) {
        return User.builder()
                .id(id)
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .estado(request.getEstados())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .estado(request.getEstados())
                .build();
    }

    public UserInfoResponse toUserInfoResponse(User user, List<String> entradas) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .entradas(entradas)
                .estado(request.getEstados())
                .build();
    }

}