package com.entradas_cine.ffe.rest.users.dto;

import com.entradas_cine.ffe.rest.users.models.Estado;
import com.entradas_cine.ffe.rest.users.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String nombre;
    private String apellidos;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private List<String> entradas = new ArrayList<>();
    @Builder.Default
    private Set<Estado> estados = Set.of(Estado.ACTIVO);
}