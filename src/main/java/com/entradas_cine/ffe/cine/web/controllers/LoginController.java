package com.entradas_cine.ffe.cine.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Muestra la pantalla de login y gestiona los mensajes de error/logout.
 * El POST del formulario lo procesa Spring Security en /auth/login-post.
 */
@Slf4j
@Controller
@RequestMapping("/auth")
public class LoginController {

    /** Muestra el formulario. Los parámetros error, logout y registered los añade Spring Security en la URL. */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String registered,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos.");
            log.warn("Intento de login fallido");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Has cerrado sesión correctamente.");
        }
        if (registered != null) {
            model.addAttribute("infoMsg", "Cuenta creada. Inicia sesión para continuar.");
        }

        return "auth/login";
    }
}
