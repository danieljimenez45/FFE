package com.entradas_cine.ffe.cine.config;

import com.entradas_cine.ffe.cine.rest.peliculas.models.*;
import com.entradas_cine.ffe.cine.rest.peliculas.repositories.PeliculaRepository;
import com.entradas_cine.ffe.cine.rest.sesiones.models.*;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Rol;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Siembra todos los datos iniciales si la BD está vacía.
 * En dev el data.sql ya lo hace; en prod (BD persistente vacía en el primer
 * despliegue) este runner crea usuarios, películas y sesiones equivalentes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeedRunner implements ApplicationRunner {

    private final UsuarioRepository  usuarioRepository;
    private final PeliculaRepository peliculaRepository;
    private final SesionRepository   sesionRepository;
    private final PasswordEncoder    passwordEncoder;

    @Value("${admin.seed.username:admin}")
    private String adminUsername;

    @Value("${admin.seed.password:admin123}")
    private String adminPassword;

    @Value("${admin.seed.email:admin@cineffe.com}")
    private String adminEmail;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUsuarios();
        List<Pelicula> peliculas = seedPeliculas();
        seedSesiones(peliculas);
    }

    // -------------------------------------------------------------------------
    // Usuarios
    // -------------------------------------------------------------------------
    private void seedUsuarios() {
        if (usuarioRepository.count() > 0) {
            log.debug("DataSeedRunner: usuarios ya existentes, se omite seed.");
            return;
        }

        String claveAdmin   = passwordEncoder.encode(adminPassword);
        String claveUsuario = passwordEncoder.encode("clave123");

        usuarioRepository.saveAll(List.of(
            usuario(adminUsername, "Admin",   "Sistema",        adminEmail,              claveAdmin,   "1990-01-10", Rol.ADMIN),
            usuario("laura",  "Laura",   "Martinez Ruiz",  "laura@email.com",  claveUsuario, "1995-04-15", Rol.USER),
            usuario("david",  "David",   "Sanchez Lopez",  "david@email.com",  claveUsuario, "1993-07-22", Rol.USER),
            usuario("marta",  "Marta",   "Fernandez Gil",  "marta@email.com",  claveUsuario, "1998-09-30", Rol.USER),
            usuario("sergio", "Sergio",  "Diaz Perez",     "sergio@email.com", claveUsuario, "1991-12-05", Rol.USER),
            usuario("irene",  "Irene",   "Romero Navas",   "irene@email.com",  claveUsuario, "1997-03-18", Rol.USER),
            usuario("pablo",  "Pablo",   "Torres Cano",    "pablo@email.com",  claveUsuario, "1992-11-11", Rol.USER),
            usuario("nerea",  "Nerea",   "Molina Cruz",    "nerea@email.com",  claveUsuario, "1996-06-08", Rol.USER),
            usuario("carlos", "Carlos",  "Vega Pastor",    "carlos@email.com", claveUsuario, "1989-08-27", Rol.USER),
            usuario("andrea", "Andrea",  "Ortega Leon",    "andrea@email.com", claveUsuario, "2000-02-21", Rol.USER)
        ));
        log.info("DataSeedRunner: usuarios sembrados.");
    }

    private Usuario usuario(String username, String nombre, String apellidos,
                            String email, String password, String nacimiento, Rol rol) {
        return Usuario.builder()
                .username(username)
                .nombre(nombre)
                .apellidos(apellidos)
                .email(email)
                .password(password)
                .fechaNacimiento(LocalDate.parse(nacimiento))
                .rol(rol)
                .build();
    }

    // -------------------------------------------------------------------------
    // Películas
    // -------------------------------------------------------------------------
    private List<Pelicula> seedPeliculas() {
        if (peliculaRepository.count() > 0) {
            log.debug("DataSeedRunner: películas ya existentes, se omite seed.");
            return peliculaRepository.findAll();
        }

        List<Pelicula> peliculas = peliculaRepository.saveAll(List.of(
            pelicula("Toy Story",
                     Genero.AVENTURAS,
                     "Los juguetes cobran vida cuando los humanos no están.",
                     81, "John Lasseter", "1995-11-22", ClasificacionEdad.TP, true, "toystory.jpg"),

            pelicula("El Rey León",
                     Genero.AVENTURAS,
                     "Un joven león debe asumir su destino como rey.",
                     88, "Roger Allers", "1994-06-15", ClasificacionEdad.TP, false, "reyleon.jpg"),

            pelicula("Regreso al Futuro",
                     Genero.CIENCIA_FICCION,
                     "Un adolescente viaja accidentalmente al pasado.",
                     116, "Robert Zemeckis", "1985-07-03", ClasificacionEdad.TP, true, "regresoalfuturo.jpg"),

            pelicula("Forrest Gump",
                     Genero.DRAMA,
                     "La extraordinaria vida de un hombre sencillo.",
                     142, "Robert Zemeckis", "1994-07-06", ClasificacionEdad.MAYORES_7, true, "forrestgump.jpg"),

            pelicula("La La Land",
                     Genero.ROMANCE,
                     "Una historia de amor entre música y cine.",
                     128, "Damien Chazelle", "2016-12-09", ClasificacionEdad.MAYORES_7, true, "lalaland.jpg"),

            pelicula("Inception",
                     Genero.CIENCIA_FICCION,
                     "Un ladrón roba secretos a través de los sueños.",
                     148, "Christopher Nolan", "2010-07-16", ClasificacionEdad.MAYORES_12, true, "inception.jpg"),

            pelicula("Interstellar",
                     Genero.CIENCIA_FICCION,
                     "Exploradores viajan a través de un agujero de gusano.",
                     169, "Christopher Nolan", "2014-11-07", ClasificacionEdad.MAYORES_12, false, "interstellar.jpg"),

            pelicula("El Señor de los Anillos: La Comunidad del Anillo",
                     Genero.FANTASIA,
                     "Un hobbit inicia un viaje para destruir un anillo.",
                     178, "Peter Jackson", "2001-12-19", ClasificacionEdad.MAYORES_12, true, "lotr.jpg"),

            pelicula("Jurassic Park",
                     Genero.CIENCIA_FICCION,
                     "Dinosaurios clonados escapan de control.",
                     127, "Steven Spielberg", "1993-06-11", ClasificacionEdad.MAYORES_12, false, "jurassicpark.jpg"),

            pelicula("Gladiator",
                     Genero.ACCION,
                     "Un general romano busca venganza.",
                     155, "Ridley Scott", "2000-05-05", ClasificacionEdad.MAYORES_16, true, "gladiator.jpg"),

            pelicula("Matrix",
                     Genero.CIENCIA_FICCION,
                     "Un hacker descubre la verdad sobre su realidad.",
                     136, "Lana Wachowski", "1999-03-31", ClasificacionEdad.MAYORES_16, false, "matrix.jpg"),

            pelicula("Alien",
                     Genero.TERROR,
                     "Una criatura mortal acecha a una tripulación espacial.",
                     117, "Ridley Scott", "1979-05-25", ClasificacionEdad.MAYORES_16, true, "alien.jpg"),

            pelicula("El Sexto Sentido",
                     Genero.SUSPENSE,
                     "Un niño afirma ver personas muertas.",
                     107, "M. Night Shyamalan", "1999-08-06", ClasificacionEdad.MAYORES_16, true, "sextosentido.jpg"),

            pelicula("Pulp Fiction",
                     Genero.DRAMA,
                     "Historias entrelazadas del crimen en Los Ángeles.",
                     154, "Quentin Tarantino", "1994-10-14", ClasificacionEdad.MAYORES_18, true, "pulpfiction.jpg"),

            pelicula("El Lobo de Wall Street",
                     Genero.DRAMA,
                     "El ascenso y caída de un corredor de bolsa.",
                     180, "Martin Scorsese", "2013-12-25", ClasificacionEdad.MAYORES_18, false, "lobowallstreet.jpg"),

            pelicula("Joker",
                     Genero.DRAMA,
                     "El origen oscuro de un villano.",
                     122, "Todd Phillips", "2019-10-04", ClasificacionEdad.MAYORES_18, true, "joker.jpg"),

            pelicula("La Máscara",
                     Genero.COMEDIA,
                     "Un hombre descubre una máscara con poderes.",
                     101, "Chuck Russell", "1994-07-29", ClasificacionEdad.MAYORES_7, true, "lamascara.jpg"),

            pelicula("Resacón en Las Vegas",
                     Genero.COMEDIA,
                     "Una despedida de soltero se sale de control.",
                     100, "Todd Phillips", "2009-06-05", ClasificacionEdad.MAYORES_16, true, "resacon.jpg"),

            pelicula("Ocho Apellidos Vascos",
                     Genero.COMEDIA,
                     "Un andaluz viaja al País Vasco por amor.",
                     98, "Emilio Martínez-Lázaro", "2014-03-14", ClasificacionEdad.MAYORES_7, true, "ochoapellidosvascos.jpg"),

            pelicula("Seven",
                     Genero.SUSPENSE,
                     "Un asesino en serie basado en los pecados capitales.",
                     127, "David Fincher", "1995-09-22", ClasificacionEdad.MAYORES_18, true, "seven.jpg")
        ));

        log.info("DataSeedRunner: {} películas sembradas.", peliculas.size());
        return peliculas;
    }

    private Pelicula pelicula(String titulo, Genero genero, String sinopsis,
                              int duracion, String director, String estreno,
                              ClasificacionEdad clasificacion, boolean activa, String imagen) {
        return Pelicula.builder()
                .titulo(titulo)
                .genero(genero)
                .sinopsis(sinopsis)
                .duracion(duracion)
                .director(director)
                .estreno(LocalDate.parse(estreno))
                .clasificacionEdad(clasificacion)
                .activa(activa)
                .imagen(imagen)
                .build();
    }

    // -------------------------------------------------------------------------
    // Sesiones — mismas que data.sql (películas 1-11 en 2024-12-31, 12-16 en 2025-01, 17-20)
    // -------------------------------------------------------------------------
    private void seedSesiones(List<Pelicula> peliculas) {
        if (sesionRepository.count() > 0) {
            log.debug("DataSeedRunner: sesiones ya existentes, se omite seed.");
            return;
        }

        // Sesiones estándar para películas 0-10 (índice 0-based)
        LocalDate fecha1 = LocalDate.of(2024, 12, 31);
        for (int i = 0; i <= 10; i++) {
            Pelicula p = peliculas.get(i);
            sesionRepository.saveAll(List.of(
                sesion(p, fecha1, Horario.H16_00, Sala.SALA_1, TipoProyeccion.NORMAL, "8.00"),
                sesion(p, fecha1, Horario.H18_30, Sala.SALA_2, TipoProyeccion.IMAX,   "10.50"),
                sesion(p, fecha1, Horario.H21_00, Sala.SALA_3, TipoProyeccion.VOSE,   "9.00"),
                sesion(p, fecha1, Horario.H23_30, Sala.SALA_4, TipoProyeccion.TRES_D, "11.00")
            ));
        }

        // Películas 12-15 (índice 11-14) — salas 5 y 6, fechas 2025-01-01 y 2025-01-02
        Pelicula p12 = peliculas.get(11);
        Pelicula p13 = peliculas.get(12);
        Pelicula p14 = peliculas.get(13);
        Pelicula p15 = peliculas.get(14);
        Pelicula p16 = peliculas.get(15);

        sesionRepository.saveAll(List.of(
            sesion(p12, LocalDate.of(2025, 1, 1), Horario.H16_00, Sala.SALA_5, TipoProyeccion.NORMAL, "7.50"),
            sesion(p12, LocalDate.of(2025, 1, 1), Horario.H18_30, Sala.SALA_6, TipoProyeccion.VOSE,   "8.50"),
            sesion(p13, LocalDate.of(2025, 1, 1), Horario.H21_00, Sala.SALA_5, TipoProyeccion.IMAX,   "10.00"),
            sesion(p13, LocalDate.of(2025, 1, 1), Horario.H23_30, Sala.SALA_6, TipoProyeccion.TRES_D, "11.50"),
            sesion(p14, LocalDate.of(2025, 1, 2), Horario.H16_00, Sala.SALA_5, TipoProyeccion.NORMAL, "8.00"),
            sesion(p14, LocalDate.of(2025, 1, 2), Horario.H18_30, Sala.SALA_6, TipoProyeccion.IMAX,   "10.50"),
            sesion(p15, LocalDate.of(2025, 1, 2), Horario.H21_00, Sala.SALA_5, TipoProyeccion.VOSE,   "9.00"),
            sesion(p15, LocalDate.of(2025, 1, 2), Horario.H23_30, Sala.SALA_6, TipoProyeccion.TRES_D, "11.00"),
            sesion(p16, LocalDate.of(2025, 1, 3), Horario.H18_30, Sala.SALA_5, TipoProyeccion.NORMAL, "8.25"),
            sesion(p16, LocalDate.of(2025, 1, 3), Horario.H21_00, Sala.SALA_6, TipoProyeccion.IMAX,   "10.75")
        ));

        // Películas 17-20 (índice 16-19) — mismo bloque que las primeras
        for (int i = 16; i <= 19; i++) {
            Pelicula p = peliculas.get(i);
            sesionRepository.saveAll(List.of(
                sesion(p, fecha1, Horario.H16_00, Sala.SALA_1, TipoProyeccion.NORMAL, "8.00"),
                sesion(p, fecha1, Horario.H18_30, Sala.SALA_2, TipoProyeccion.IMAX,   "10.50"),
                sesion(p, fecha1, Horario.H21_00, Sala.SALA_3, TipoProyeccion.VOSE,   "9.00"),
                sesion(p, fecha1, Horario.H23_30, Sala.SALA_4, TipoProyeccion.TRES_D, "11.00")
            ));
        }

        log.info("DataSeedRunner: sesiones sembradas.");
    }

    private Sesion sesion(Pelicula pelicula, LocalDate fecha, Horario horario,
                          Sala sala, TipoProyeccion tipo, String precio) {
        return Sesion.builder()
                .pelicula(pelicula)
                .fecha(fecha)
                .horario(horario)
                .sala(sala)
                .tipoProyeccion(tipo)
                .precio(new BigDecimal(precio))
                .build();
    }
}
