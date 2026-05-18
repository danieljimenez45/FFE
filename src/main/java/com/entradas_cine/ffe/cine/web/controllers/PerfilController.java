package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioResponseDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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

    @PostMapping("/editar")
    public String editarPerfil(
            Authentication authentication,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String fechaNacimiento,
            @RequestParam(required = false) String password,
            RedirectAttributes ra) {

        String username = authentication.getName();
        try {
            // El rol NUNCA se lee del request: se fuerza desde BD para que ningún
            // usuario pueda escalar sus propios privilegios enviando un parámetro rol.
            UsuarioResponseDto current = usuarioService.findByUsername(username);
            String pwd = (password != null && !password.isBlank()) ? password : null;
            UsuarioUpdateDto dto = UsuarioUpdateDto.builder()
                    .nombre(nombre)
                    .apellidos(apellidos)
                    .email(email)
                    .fechaNacimiento(LocalDate.parse(fechaNacimiento))
                    .rol(current.getRol())   // keep existing role
                    .password(pwd)
                    .build();
            usuarioService.update(current.getId(), dto);
            ra.addFlashAttribute("successMsg", "Perfil actualizado correctamente.");
            log.info("Usuario '{}' actualizó su perfil", username);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo actualizar el perfil: " + e.getMessage());
        }
        return "redirect:/perfil";
    }
}
