package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Muestra la página de perfil del usuario autenticado.
 * No necesita recibir ningún parámetro: toma el username directamente
 * del contexto de seguridad para evitar cualquier posibilidad de IDOR.
 */
@Slf4j
@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String perfil(Authentication authentication, Model model) {
        String username = authentication.getName();
        UsuarioResponseDto usuario = usuarioService.findByUsername(username);
        model.addAttribute("usuario", usuario);
        log.debug("Perfil solicitado por '{}'", username);
        return "usuario/perfil";
    }
}
