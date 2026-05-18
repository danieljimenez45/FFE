package com.entradas_cine.ffe.cine.rest.sesiones;

import com.entradas_cine.ffe.cine.rest.peliculas.exceptions.PeliculaNotFound;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionResponseDto;
import com.entradas_cine.ffe.cine.rest.sesiones.dto.SesionCreateDto;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionBadRequest;
import com.entradas_cine.ffe.cine.rest.sesiones.exceptions.SesionNotFound;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Horario;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sala;
import com.entradas_cine.ffe.cine.rest.sesiones.models.TipoProyeccion;
import com.entradas_cine.ffe.cine.rest.sesiones.services.SesionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SesionServiceImplTest {

    @Autowired
    private SesionService sesionService;

    // --- findAll ---

    @Test
    void findAll_deberiaRetornarTodasLasSesiones() {
        List<SesionResponseDto> resultado = sesionService.findAll();

        // data.sql tiene al menos 54 sesiones
        assertThat(resultado).hasSizeGreaterThanOrEqualTo(54);
    }

    // --- findById ---

    @Test
    void findById_deberiaRetornarSesion() {
        SesionResponseDto result = sesionService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPelicula()).isEqualTo("pelicula.titulo.toy_story");
        assertThat(result.getFecha()).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    void findById_deberiaLanzarExcepcionSiNoExiste() {
        assertThatThrownBy(() -> sesionService.findById(9999L))
                .isInstanceOf(SesionNotFound.class);
    }

    // --- findByPelicula (List) ---

    @Test
    void findByPelicula_deberiaRetornarSesionesDeLaPelicula() {
        // data.sql: pelicula id=1 (Toy Story) tiene 4 sesiones (id 1-4)
        List<SesionResponseDto> resultado = sesionService.findByPelicula(1L);

        assertThat(resultado).hasSize(4);
        assertThat(resultado).allMatch(s -> s.getPelicula().equals("pelicula.titulo.toy_story"));
    }

    @Test
    void findByPelicula_deberiaLanzarExcepcionSiPeliculaNoExiste() {
        assertThatThrownBy(() -> sesionService.findByPelicula(9999L))
                .isInstanceOf(PeliculaNotFound.class);
    }

    // --- findByPeliculaAndFecha ---

    @Test
    void findByPeliculaAndFecha_deberiaFiltrarPorFecha() {
        // Todas las sesiones de pelicula 1 son en 2024-12-31
        List<SesionResponseDto> resultado = sesionService.findByPeliculaAndFecha(1L, LocalDate.of(2024, 12, 31));

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).allMatch(s -> s.getFecha().equals(LocalDate.of(2024, 12, 31)));
    }

    @Test
    void findByPeliculaAndFecha_deberiaRetornarListaVaciaSiFechaNoCoincide() {
        List<SesionResponseDto> resultado = sesionService.findByPeliculaAndFecha(1L, LocalDate.of(2099, 1, 1));

        assertThat(resultado).isEmpty();
    }

    // --- deleteById ---

    @Test
    void deleteById_deberiaEliminarSesion() {
        // Aseguramos que existe antes
        SesionResponseDto antes = sesionService.findById(1L);
        assertThat(antes).isNotNull();

        sesionService.deleteById(1L);

        assertThatThrownBy(() -> sesionService.findById(1L))
                .isInstanceOf(SesionNotFound.class);
    }

    @Test
    void deleteById_deberiaLanzarExcepcionSiNoExiste() {
        assertThatThrownBy(() -> sesionService.deleteById(9999L))
                .isInstanceOf(SesionNotFound.class);
    }

    // --- create: validaciones ---

    @Test
    void create_deberiaLanzarSesionBadRequestSiFechaPasada() {
        SesionCreateDto dto = SesionCreateDto.builder()
                .idPelicula(1L)
                .fecha(LocalDate.now().minusDays(1))
                .horario(Horario.H16_00)
                .sala(Sala.SALA_1)
                .tipoProyeccion(TipoProyeccion.NORMAL)
                .precio(new BigDecimal("8.00"))
                .build();

        assertThatThrownBy(() -> sesionService.create(dto))
                .isInstanceOf(SesionBadRequest.class)
                .hasMessage("admin.error.sesion.fecha.pasada");
    }

    @Test
    void create_deberiaLanzarSesionBadRequestSiPrecioInvalido() {
        SesionCreateDto dto = SesionCreateDto.builder()
                .idPelicula(1L)
                .fecha(LocalDate.now().plusDays(30))
                .horario(Horario.H16_00)
                .sala(Sala.SALA_1)
                .tipoProyeccion(TipoProyeccion.NORMAL)
                .precio(BigDecimal.ZERO)
                .build();

        assertThatThrownBy(() -> sesionService.create(dto))
                .isInstanceOf(SesionBadRequest.class)
                .hasMessage("admin.error.sesion.precio.invalido");
    }

    @Test
    void create_deberiaLanzarSesionBadRequestSiSesionDuplicada() {
        SesionResponseDto existente = sesionService.findById(1L);

        SesionCreateDto dto = SesionCreateDto.builder()
                .idPelicula(existente.getPeliculaId())
                .fecha(existente.getFecha())
                .horario(existente.getHorario())
                .sala(existente.getSala())
                .tipoProyeccion(existente.getTipoProyeccion())
                .precio(new BigDecimal("9.00"))
                .build();

        assertThatThrownBy(() -> sesionService.create(dto))
                .isInstanceOf(SesionBadRequest.class)
                .hasMessage("admin.error.sesion.duplicada");
    }
}
