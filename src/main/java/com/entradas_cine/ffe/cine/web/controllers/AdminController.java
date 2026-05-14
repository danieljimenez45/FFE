package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaUpdateEstadoDto;
import com.entradas_cine.ffe.cine.rest.peliculas.models.ClasificacionEdad;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Genero;
import com.entradas_cine.ffe.cine.rest.peliculas.services.PeliculaService;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionUpdateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;
import com.entradas_cine.ffe.cine.rest.sesiones.models.TipoProyeccion;
import com.entradas_cine.ffe.cine.rest.sesiones.services.SesionService;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Panel de administración web.
 * Toda la clase está protegida con rol ADMIN — cualquier petición sin ese rol
 * es rechazada antes de llegar aquí.
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioService usuarioService;
    private final PeliculaService peliculaService;
    private final SesionService sesionService;

    // -- Dashboard principal --------------------------------------------------

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("usuarios",  usuarioService.findAll());
        model.addAttribute("peliculas", peliculaService.findAll());
        model.addAttribute("sesiones",  sesionService.findAll());
        model.addAttribute("generos",         Arrays.asList(Genero.values()));
        model.addAttribute("edades",          Arrays.asList(ClasificacionEdad.values()));
        model.addAttribute("horarios",        Arrays.asList(Horario.values()));
        model.addAttribute("salas",           Arrays.asList(Sala.values()));
        model.addAttribute("tiposProyeccion", Arrays.asList(TipoProyeccion.values()));
        model.addAttribute("today",           LocalDate.now().toString());
        return "admin/index";
    }

    // -- Usuarios --------------------------------------------------

    @PostMapping("/usuarios/crear")
    public String crearUsuario(
            @RequestParam String username,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fechaNacimiento,
            RedirectAttributes ra) {

        return crearUsuarioConRol(username, nombre, apellidos, email, password,
                fechaNacimiento, false, ra);
    }

    @PostMapping("/usuarios/admin/crear")
    public String crearAdmin(
            @RequestParam String username,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fechaNacimiento,
            RedirectAttributes ra) {

        return crearUsuarioConRol(username, nombre, apellidos, email, password,
                fechaNacimiento, true, ra);
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes ra) {
        try {
            usuarioService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Usuario eliminado correctamente.");
            log.info("Admin eliminó usuario id={}", id);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/admin#tab-usuarios";
    }

    // -- Películas  --------------------------------------------------

    @PostMapping("/peliculas/{id}/editar")
    public String editarPelicula(
            @PathVariable Long id,
            @RequestParam String titulo,
            @RequestParam String director,
            @RequestParam String sinopsis,
            @RequestParam Integer duracion,
            @RequestParam String genero,
            @RequestParam String estreno,
            @RequestParam String clasificacionEdad,
            RedirectAttributes ra) {

        try {
            PeliculaCreateDto dto = PeliculaCreateDto.builder()
                    .titulo(titulo)
                    .director(director)
                    .sinopsis(sinopsis)
                    .duracion(duracion)
                    .genero(Genero.valueOf(genero))
                    .estreno(LocalDate.parse(estreno))
                    .clasificacionEdad(ClasificacionEdad.valueOf(clasificacionEdad))
                    .build();
            peliculaService.update(id, dto);
            ra.addFlashAttribute("successMsg", "Película «" + titulo + "» actualizada.");
            log.info("Admin editó película id={}", id);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo actualizar la película: " + e.getMessage());
        }
        return "redirect:/admin#tab-peliculas";
    }

    @PostMapping("/peliculas/crear")
    public String crearPelicula(
            @RequestParam String titulo,
            @RequestParam String director,
            @RequestParam String sinopsis,
            @RequestParam Integer duracion,
            @RequestParam String genero,
            @RequestParam String estreno,
            @RequestParam String clasificacionEdad,
            RedirectAttributes ra) {

        try {
            LocalDate fechaEstreno = LocalDate.parse(estreno);
            PeliculaCreateDto dto = PeliculaCreateDto.builder()
                    .titulo(titulo)
                    .director(director)
                    .sinopsis(sinopsis)
                    .duracion(duracion)
                    .genero(Genero.valueOf(genero))
                    .estreno(fechaEstreno)
                    .clasificacionEdad(ClasificacionEdad.valueOf(clasificacionEdad))
                    .build();
            peliculaService.create(dto);
            ra.addFlashAttribute("successMsg", "Película «" + titulo + "» creada correctamente.");
            log.info("Admin creó película '{}'", titulo);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("errorMsg", "Fecha de estreno no válida.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo crear la película: " + e.getMessage());
        }
        return "redirect:/admin#tab-peliculas";
    }

    @PostMapping("/peliculas/{id}/toggle")
    public String togglePelicula(@PathVariable Long id, RedirectAttributes ra) {
        try {
            var pelicula = peliculaService.findById(id);
            boolean nuevoEstado = !pelicula.getActiva();
            peliculaService.updateEstado(id, new PeliculaUpdateEstadoDto(nuevoEstado));
            ra.addFlashAttribute("successMsg",
                    "Película «" + pelicula.getTitulo() + "» "
                    + (nuevoEstado ? "activada" : "desactivada") + ".");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo cambiar el estado: " + e.getMessage());
        }
        return "redirect:/admin#tab-peliculas";
    }

    @PostMapping("/peliculas/{id}/eliminar")
    public String eliminarPelicula(@PathVariable Long id, RedirectAttributes ra) {
        try {
            var pelicula = peliculaService.findById(id);
            peliculaService.deleteById(id);
            ra.addFlashAttribute("successMsg",
                    "Película «" + pelicula.getTitulo() + "» eliminada.");
            log.info("Admin eliminó película id={}", id);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo eliminar la película: " + e.getMessage());
        }
        return "redirect:/admin#tab-peliculas";
    }

    // -- Sesiones --------------------------------------------------

    @PostMapping("/sesiones/{id}/editar")
    public String editarSesion(
            @PathVariable Long id,
            @RequestParam String fecha,
            @RequestParam String horario,
            @RequestParam String sala,
            @RequestParam String tipoProyeccion,
            @RequestParam java.math.BigDecimal precio,
            RedirectAttributes ra) {

        try {
            SesionUpdateDto dto = new SesionUpdateDto(
                    LocalDate.parse(fecha),
                    Horario.valueOf(horario),
                    Sala.valueOf(sala),
                    TipoProyeccion.valueOf(tipoProyeccion),
                    precio
            );
            sesionService.update(id, dto);
            ra.addFlashAttribute("successMsg", "Sesión #" + id + " actualizada.");
            log.info("Admin editó sesión id={}", id);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo actualizar la sesión: " + e.getMessage());
        }
        return "redirect:/admin#tab-sesiones";
    }

    @PostMapping("/sesiones/{id}/eliminar")
    public String eliminarSesion(@PathVariable Long id, RedirectAttributes ra) {
        try {
            sesionService.deleteById(id);
            ra.addFlashAttribute("successMsg", "Sesión eliminada correctamente.");
            log.info("Admin eliminó sesión id={}", id);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo eliminar la sesión: " + e.getMessage());
        }
        return "redirect:/admin#tab-sesiones";
    }

    // -- Helper privado --------------------------------------------------

    private String crearUsuarioConRol(
            String username, String nombre, String apellidos,
            String email, String password, String fechaNacimiento,
            boolean esAdmin, RedirectAttributes ra) {

        try {
            LocalDate fecha = LocalDate.parse(fechaNacimiento);
            UsuarioCreateDto dto = UsuarioCreateDto.builder()
                    .username(username)
                    .nombre(nombre)
                    .apellidos(apellidos)
                    .email(email)
                    .password(password)
                    .fechaNacimiento(fecha)
                    .build();

            if (esAdmin) {
                usuarioService.createAdmin(dto);
                ra.addFlashAttribute("successMsg",
                        "Administrador «" + username + "» creado correctamente.");
            } else {
                usuarioService.create(dto);
                ra.addFlashAttribute("successMsg",
                        "Usuario «" + username + "» creado correctamente.");
            }
            log.info("Admin creó {} '{}'", esAdmin ? "ADMIN" : "USER", username);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("errorMsg", "Fecha de nacimiento no válida.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo crear el usuario: " + e.getMessage());
        }
        return "redirect:/admin#tab-usuarios";
    }
}
