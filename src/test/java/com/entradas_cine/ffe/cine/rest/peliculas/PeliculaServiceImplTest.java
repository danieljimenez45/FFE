package com.entradas_cine.ffe.cine.rest.peliculas;

import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaCreateDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaResponseDto;
import com.entradas_cine.ffe.cine.rest.peliculas.dto.PeliculaUpdateEstadoDto;
import com.entradas_cine.ffe.cine.rest.peliculas.exceptions.PeliculaBadRequest;
import com.entradas_cine.ffe.cine.rest.peliculas.exceptions.PeliculaNotFound;
import com.entradas_cine.ffe.cine.rest.peliculas.models.ClasificacionEdad;
import com.entradas_cine.ffe.cine.rest.peliculas.models.Genero;
import com.entradas_cine.ffe.cine.rest.peliculas.services.PeliculaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PeliculaServiceImplTest {

    @Autowired
    private PeliculaService peliculaService;

    // --- findById ---

    @Test
    void findById_deberiaRetornarPelicula() {
        PeliculaResponseDto result = peliculaService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitulo()).isEqualTo("pelicula.titulo.toy_story");
        assertThat(result.getDirector()).isEqualTo("John Lasseter");
        assertThat(result.getGenero()).isEqualTo(Genero.AVENTURAS);
    }

    @Test
    void findById_deberiaLanzarExcepcionSiNoExiste() {
        assertThatThrownBy(() -> peliculaService.findById(9999L))
                .isInstanceOf(PeliculaNotFound.class);
    }

    // --- findAllActivas ---

    @Test
    void findAllActivas_deberiaRetornarSoloActivas() {
        List<PeliculaResponseDto> activas = peliculaService.findAllActivas();

        assertThat(activas).isNotEmpty();
        assertThat(activas).allMatch(p -> p.getActiva() != null && p.getActiva());
    }

    @Test
    void findAllActivas_noDebeIncluirInactivas() {
        // Pelicula id=2 (El Rey León) esta inactiva en data.sql
        List<PeliculaResponseDto> activas = peliculaService.findAllActivas();

        assertThat(activas).noneMatch(p -> p.getTitulo().equals("El Rey León"));
    }

    // --- create ---

    @Test
    void create_deberiaCrearPeliculaActiva() {
        PeliculaCreateDto dto = PeliculaCreateDto.builder()
                .titulo("Nuevo Film de Prueba")
                .genero(Genero.COMEDIA)
                .sinopsis("Sinopsis de prueba para el test de creacion")
                .duracion(100)
                .director("Director Test")
                .estreno(LocalDate.of(2025, 6, 1))
                .clasificacionEdad(ClasificacionEdad.TP)
                .imagen("test.jpg")
                .build();

        PeliculaResponseDto result = peliculaService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("Nuevo Film de Prueba");
        assertThat(result.getActiva()).isTrue();
        assertThat(result.getGenero()).isEqualTo(Genero.COMEDIA);
    }

    @Test
    void create_deberiaFallarSiTituloYaExiste() {
        PeliculaCreateDto dto = PeliculaCreateDto.builder()
                .titulo("pelicula.titulo.toy_story") // ya existe en data.sql
                .genero(Genero.AVENTURAS)
                .sinopsis("Sinopsis duplicada")
                .duracion(90)
                .director("Otro Director")
                .estreno(LocalDate.of(2000, 1, 1))
                .clasificacionEdad(ClasificacionEdad.TP)
                .build();

        assertThatThrownBy(() -> peliculaService.create(dto))
                .isInstanceOf(PeliculaBadRequest.class);
    }

    // --- updateEstado ---

    @Test
    void updateEstado_deberiaDesactivarPelicula() {
        // Pelicula 1 (Toy Story) esta activa
        PeliculaUpdateEstadoDto dto = new PeliculaUpdateEstadoDto(false);
        PeliculaResponseDto result = peliculaService.updateEstado(1L, dto);

        assertThat(result.getActiva()).isFalse();
    }

    @Test
    void updateEstado_deberiaActivarPeliculaInactiva() {
        // Pelicula 2 (El Rey León) esta inactiva en data.sql
        PeliculaUpdateEstadoDto dto = new PeliculaUpdateEstadoDto(true);
        PeliculaResponseDto result = peliculaService.updateEstado(2L, dto);

        assertThat(result.getActiva()).isTrue();
    }

    @Test
    void updateEstado_deberiaLanzarExcepcionSiNoExiste() {
        PeliculaUpdateEstadoDto dto = new PeliculaUpdateEstadoDto(false);

        assertThatThrownBy(() -> peliculaService.updateEstado(9999L, dto))
                .isInstanceOf(PeliculaNotFound.class);
    }

    // --- findByTitulo ---

    @Test
    void findByTitulo_deberiaEncontrarPorTituloExacto() {
        List<PeliculaResponseDto> resultado = peliculaService.findByTitulo("pelicula.titulo.inception");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("pelicula.titulo.inception");
    }

    @Test
    void findByTitulo_deberiaEncontrarPorSubcadena() {
        // "Regreso al Futuro" contiene "Futuro"
        List<PeliculaResponseDto> resultado = peliculaService.findByTitulo("Futuro");

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).anyMatch(p -> p.getTitulo().toLowerCase().contains("futuro"));
    }

    // --- findByGenero ---

    @Test
    void findByGenero_deberiaFiltrarPorGenero() {
        List<PeliculaResponseDto> resultado = peliculaService.findByGenero(Genero.CIENCIA_FICCION);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).allMatch(p -> p.getGenero() == Genero.CIENCIA_FICCION);
    }
}
