package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaNotFound;
import com.entradas_cine.ffe.cine.rest.facturas.exceptions.FacturaNotFound;
import com.entradas_cine.ffe.cine.rest.peliculas.exceptions.PeliculaNotFound;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionBadRequest;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionNotFound;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioNotFound;
import com.entradas_cine.ffe.cine.web.services.I18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Captura excepciones lanzadas por los controladores web MVC y las presenta
 * en la plantilla de error con estilo, en lugar de la pantalla en blanco de Spring.
 */
@ControllerAdvice(basePackages = "com.entradas_cine.ffe.cine.web.controllers")
@RequiredArgsConstructor
public class WebExceptionHandler {

    private final I18nService i18nService;

    // -- 404 -------------------------------------------------------------

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
        PeliculaNotFound.class,
        SesionNotFound.class,
        EntradaNotFound.class,
        FacturaNotFound.class,
        UsuarioNotFound.class
    })
    public String handleNotFound(RuntimeException ex, Model model) {
        model.addAttribute("errorCode",    "404");
        model.addAttribute("errorTitle",   i18nService.getMessage("error.404"));
        model.addAttribute("errorMessage", i18nService.getMessage("error.404.message"));
        return "error";
    }

    // -- 403 --------------------------------------------------------------

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleForbidden(Model model) {
        model.addAttribute("errorCode",    "403");
        model.addAttribute("errorTitle",   i18nService.getMessage("error.403"));
        model.addAttribute("errorMessage", i18nService.getMessage("error.403.message"));
        return "error";
    }

    // -- 400 ---------------------------------------------------------------

    /**
     * Path variable con tipo incorrecto (p.ej. /pelicula/abc en vez de /pelicula/1).
     * Spring lanza esto antes de llegar al controller, pero con el scope limitado
     * al paquete MVC sí lo interceptamos y mostramos la pantalla de error propia.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleBadRequest(Model model) {
        model.addAttribute("errorCode",    "400");
        model.addAttribute("errorTitle",   i18nService.getMessage("error.400"));
        model.addAttribute("errorMessage", i18nService.getMessage("error.400.message"));
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SesionBadRequest.class)
    public String handleSesionBadRequest(SesionBadRequest ex, Model model) {
        String key = ex.getMessage();
        String message = key != null && key.startsWith("admin.")
                ? i18nService.getMessage(key)
                : (key != null ? key : i18nService.getMessage("error.400.message"));
        model.addAttribute("errorCode",    "400");
        model.addAttribute("errorTitle",   i18nService.getMessage("error.400"));
        model.addAttribute("errorMessage", message);
        return "error";
    }

    // -- 500 ---------------------------------------------------------------

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public String handleGeneric(RuntimeException ex, Model model) {
        model.addAttribute("errorCode",    "500");
        model.addAttribute("errorTitle",   i18nService.getMessage("error.500"));
        model.addAttribute("errorMessage", i18nService.getMessage("error.500.message"));
        return "error";
    }
}
