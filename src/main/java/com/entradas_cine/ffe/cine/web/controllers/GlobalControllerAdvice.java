package com.entradas_cine.ffe.cine.web.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Rellena datos comunes en todas las vistas Pebble sin repetirlos en cada controlador.
 * Así las plantillas pueden mostrar el nombre de la app, quién ha iniciado sesión
 * y si es administrador. El token CSRF lo añade Spring al modelo como {@code _csrf}.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

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
