package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.auth.AuthService;
import com.entradas_cine.ffe.cine.rest.auth.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Gestiona el registro de nuevos usuarios.
 * Después de crear la cuenta hace el login automático para que el usuario
 * no tenga que volver a identificarse.
 */
@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    // Formulario de registro

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("today", LocalDate.now().toString());
        return "auth/register";
    }

    // Procesar el formulario

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam String fechaNacimiento,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Validar antes de tocar la base de datos

        if (!password.equals(passwordConfirm)) {
            return errorEnFormulario(model, "Las contraseñas no coinciden.",
                    username, nombre, apellidos, email, fechaNacimiento);
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaNacimiento);
        } catch (DateTimeParseException e) {
            return errorEnFormulario(model, "Fecha de nacimiento no válida.",
                    username, nombre, apellidos, email, fechaNacimiento);
        }

        if (!fecha.isBefore(LocalDate.now())) {
            return errorEnFormulario(model, "La fecha de nacimiento debe ser anterior a hoy.",
                    username, nombre, apellidos, email, fechaNacimiento);
        }

        // Crear el usuario

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(username);
        signUpRequest.setNombre(nombre);
        signUpRequest.setApellidos(apellidos);
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setFechaNacimiento(fecha);

        try {
            authService.signup(signUpRequest);
            log.info("Nuevo usuario registrado vía web: {}", username);
        } catch (ResponseStatusException ex) {
            String mensaje = ex.getReason() != null
                    ? ex.getReason()
                    : "No se pudo completar el registro. Inténtalo de nuevo.";
            return errorEnFormulario(model, mensaje,
                    username, nombre, apellidos, email, fechaNacimiento);
        }

        // Auto-login: autenticar y guardar el contexto en sesión antes del redirect

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            // Persistir en sesión — sin esto el contexto se pierde en el redirect
            securityContextRepository.saveContext(context, request, response);

            log.info("Auto-login exitoso tras registro de '{}'", username);
        } catch (AuthenticationException ex) {
            log.warn("Auto-login fallido tras registro de '{}': {}", username, ex.getMessage());
            return "redirect:/auth/login?registered=true";
        }

        return "redirect:/";
    }

    // Vuelve al formulario con el error y los campos previos rellenos

    private String errorEnFormulario(Model model, String errorMsg,
            String username, String nombre, String apellidos,
            String email, String fechaNacimiento) {
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("prevUsername", username);
        model.addAttribute("prevNombre", nombre);
        model.addAttribute("prevApellidos", apellidos);
        model.addAttribute("prevEmail", email);
        model.addAttribute("prevFecha", fechaNacimiento);
        model.addAttribute("today", LocalDate.now().toString());
        return "auth/register";
    }
}
