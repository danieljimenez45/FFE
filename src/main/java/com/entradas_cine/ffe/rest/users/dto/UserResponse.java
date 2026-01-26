package com.entradas_cine.ffe.rest.users.dto;

import com.entradas_cine.ffe.rest.users.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String nombre;
    private String apellidos;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

}