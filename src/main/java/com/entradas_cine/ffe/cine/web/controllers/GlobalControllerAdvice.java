package com.entradas_cine.ffe.cine.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Añade atributos comunes al modelo de todas las vistas para no repetirlo en cada controlador.
 * Pebble no inyecta el token CSRF automáticamente como Thymeleaf, así que se hace aquí
 * para que todos los formularios tengan acceso a él sin código extra.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Pasa el token CSRF al modelo para que los formularios Pebble puedan usarlo.
     * Se llama a getToken() aquí (antes del renderizado) para evitar que Spring Security
     * intente crear la sesión cuando la respuesta ya está comprometida.
     */
    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            token.getToken(); // fuerza la resolución eager antes de que se renderice la vista
        }
        return token;
    }

    /** Título corto de la aplicación para cabeceras y pie de página. */
    @ModelAttribute("appName")
    public String getAppName() {
        return "Entradas Cine FFE";
    }
    /** Nombre con el que se ha identificado en la web; si no hay login, en plantilla sale null. */
    @ModelAttribute("currentUser")
    public String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return auth.getName();
    }

    /** Indica si la persona que ve la página ya pasó por el login (no es el visitante anónimo). */
    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    /** Sirve para enseñar u ocultar cosas de administración solo a quien tenga rol de admin. */
    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
