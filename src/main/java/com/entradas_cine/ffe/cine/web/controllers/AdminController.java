package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaUpdateEstadoDto;
import com.entradas_cine.ffe.cine.rest.peliculas.models.ClasificacionEdad;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Genero;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.services.PeliculaService;
import com.entradas_cine.ffe.cine.rest.peliculas.services.TraduccionService;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionCreateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionUpdateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionBadRequest;
import com.entradas_cine.ffe.cine.rest.sesiones.models.TipoProyeccion;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import com.entradas_cine.ffe.cine.rest.sesiones.services.SesionService;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioCreateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.dto.UsuarioUpdateDto;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import com.entradas_cine.ffe.cine.rest.usuarios.services.UsuarioService;
import com.entradas_cine.ffe.cine.web.services.I18nService;
import com.entradas_cine.ffe.cine.web.services.PeliculaPosterStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final PeliculaPosterStorageService posterStorage;
    private final I18nService i18n;
    private final TraduccionService traduccionService;
    private final EntradaService entradaService;

    // -- Dashboard principal --------------------------------------------------

    @GetMapping
    public String dashboard(Model model) {
        String locale = LocaleContextHolder.getLocale().getLanguage();
        List<PeliculaResponseDto> peliculas = peliculaService.findAll();
        Map<Long, String> titulosTraducidos = peliculas.stream().collect(
                Collectors.toMap(
                        PeliculaResponseDto::getId,
                        p -> traduccionService.obtenerTituloTraducido(p.getId(), p.getTitulo(), locale)
                ));

        model.addAttribute("usuarios",  usuarioService.findAll());
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("titulosTraducidos", titulosTraducidos);
        model.addAttribute("sesiones",  sesionService.findAll());
        model.addAttribute("entradasAdmin", entradaService.findAllForAdmin(locale));
        model.addAttribute("generos",         Arrays.asList(Genero.values()));
        model.addAttribute("edades",          Arrays.asList(ClasificacionEdad.values()));
        model.addAttribute("horarios",        Arrays.asList(Horario.values()));
        model.addAttribute("salas",           Arrays.asList(Sala.values()));
        model.addAttribute("tiposProyeccion", Arrays.asList(TipoProyeccion.values()));
        model.addAttribute("roles",           Arrays.asList(Rol.values()));
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

    @PostMapping("/usuarios/{id}/editar")
    public String editarUsuario(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String fechaNacimiento,
            @RequestParam String rol,
            @RequestParam(required = false) String password,
            RedirectAttributes ra) {

        try {
            String pwd = (password != null && !password.isBlank()) ? password : null;
            UsuarioUpdateDto dto = UsuarioUpdateDto.builder()
                    .nombre(nombre)
                    .apellidos(apellidos)
                    .email(email)
                    .fechaNacimiento(LocalDate.parse(fechaNacimiento))
                    .rol(Rol.valueOf(rol))
                    .password(pwd)
                    .build();
            usuarioService.update(id, dto);
            ra.addFlashAttribute("successMsg", "Usuario #" + id + " actualizado correctamente.");
            log.info("Admin editó usuario id={}", id);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("errorMsg", "Fecha de nacimiento no válida.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "No se pudo actualizar el usuario: " + e.getMessage());
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
            @RequestParam(value = "caratula", required = false) MultipartFile caratula,
            RedirectAttributes ra) {

        try {
            String imagen = peliculaService.findById(id).getImagen();

            if (caratula != null && !caratula.isEmpty()) {
                posterStorage.deleteUploadedIfPresent(imagen);
                imagen = posterStorage.store(caratula, titulo);
            }
            posterStorage.requireValidPoster(imagen);
            PeliculaCreateDto dto = PeliculaCreateDto.builder()
                    .titulo(titulo)
                    .director(director)
                    .sinopsis(sinopsis)
                    .duracion(duracion)
                    .genero(Genero.valueOf(genero))
                    .estreno(LocalDate.parse(estreno))
                    .clasificacionEdad(ClasificacionEdad.valueOf(clasificacionEdad))
                    .imagen(imagen)
                    .build();
            peliculaService.update(id, dto);
            ra.addFlashAttribute("successMsg", "Película «" + titulo + "» actualizada.");
            log.info("Admin editó película id={}", id);
        } catch (IllegalArgumentException e) {
            String key = e.getMessage();
            ra.addFlashAttribute("errorMsg",
                    key != null && key.startsWith("admin.") ? i18n.getMessage(key) : key);
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
            @RequestParam("caratula") MultipartFile caratula,
            RedirectAttributes ra) {

        try {
            String imagen = posterStorage.store(caratula, titulo);
            LocalDate fechaEstreno = LocalDate.parse(estreno);
            PeliculaCreateDto dto = PeliculaCreateDto.builder()
                    .titulo(titulo)
                    .director(director)
                    .sinopsis(sinopsis)
                    .duracion(duracion)
                    .genero(Genero.valueOf(genero))
                    .estreno(fechaEstreno)
                    .clasificacionEdad(ClasificacionEdad.valueOf(clasificacionEdad))
                    .imagen(imagen)
                    .build();
            peliculaService.create(dto);
            ra.addFlashAttribute("successMsg", "Película «" + titulo + "» creada correctamente.");
            log.info("Admin creó película '{}'", titulo);
        } catch (IllegalArgumentException e) {
            String key = e.getMessage();
            ra.addFlashAttribute("errorMsg",
                    key != null && key.startsWith("admin.") ? i18n.getMessage(key) : key);
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

    @PostMapping("/sesiones/crear")
    public String crearSesion(
            @RequestParam Long idPelicula,
            @RequestParam String fecha,
            @RequestParam String horario,
            @RequestParam String sala,
            @RequestParam String tipoProyeccion,
            @RequestParam java.math.BigDecimal precio,
            RedirectAttributes ra) {

        try {
            SesionCreateDto dto = SesionCreateDto.builder()
                    .idPelicula(idPelicula)
                    .fecha(LocalDate.parse(fecha))
                    .horario(Horario.valueOf(horario))
                    .sala(Sala.valueOf(sala))
                    .tipoProyeccion(TipoProyeccion.valueOf(tipoProyeccion))
                    .precio(precio)
                    .build();
            sesionService.create(dto);
            ra.addFlashAttribute("successMsg", "Sesión creada correctamente.");
            log.info("Admin creó sesión para película id={}", idPelicula);
        } catch (SesionBadRequest e) {
            flashErrorAdmin(ra, e);
        } catch (IllegalArgumentException e) {
            flashErrorAdmin(ra, e);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("errorMsg", "Fecha no válida.");
        } catch (Exception e) {
            flashErrorGenerico(ra, e, "crear sesión");
        }
        return "redirect:/admin#tab-sesiones";
    }

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
        } catch (SesionBadRequest e) {
            flashErrorAdmin(ra, e);
        } catch (IllegalArgumentException e) {
            flashErrorAdmin(ra, e);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("errorMsg", "Fecha no válida.");
        } catch (Exception e) {
            flashErrorGenerico(ra, e, "editar sesión id=" + id);
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
            flashErrorGenerico(ra, e, "eliminar sesión id=" + id);
        }
        return "redirect:/admin#tab-sesiones";
    }

    // -- Entradas --------------------------------------------------

    @PostMapping("/entradas/{id}/eliminar")
    public String eliminarEntrada(@PathVariable Long id, RedirectAttributes ra) {
        try {
            entradaService.deleteById(id);
            ra.addFlashAttribute("successMsg", i18n.getMessage("admin.success.eliminar.entrada"));
            log.info("Admin eliminó entrada id={}", id);
        } catch (Exception e) {
            flashErrorGenerico(ra, e, "eliminar entrada id=" + id);
        }
        return "redirect:/admin#tab-entradas";
    }

    // -- Helper privado --------------------------------------------------

    private void flashErrorAdmin(RedirectAttributes ra, RuntimeException e) {
        String key = e.getMessage();
        ra.addFlashAttribute("errorMsg",
                key != null && key.startsWith("admin.") ? i18n.getMessage(key) : key);
    }

    private void flashErrorGenerico(RedirectAttributes ra, Exception e, String contexto) {
        log.warn("Admin: fallo al {} — {}", contexto, e.toString());
        log.debug("Detalle", e);
        ra.addFlashAttribute("errorMsg", i18n.getMessage("admin.error.generico"));
    }

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
