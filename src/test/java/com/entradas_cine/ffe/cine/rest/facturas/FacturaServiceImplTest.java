package com.entradas_cine.ffe.cine.rest.facturas;

import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaNotFound;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.exceptions.FacturaBadRequest;
import com.entradas_cine.ffe.cine.rest.facturas.exceptions.FacturaNotFound;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FacturaServiceImplTest {

    @Autowired
    private FacturaService facturaService;

    @AfterEach
    void limpiarSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // -------------------------------------------------------------------------
    // Helpers para simular usuarios autenticados en el SecurityContext
    // -------------------------------------------------------------------------

    private void autenticarAdmin() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "admin", "irrelevante",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    private void autenticarUsuario(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        username, "irrelevante",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    // --- create ---

    @Test
    void create_deberiaCrearFacturaComoAdmin() {
        autenticarAdmin();

        // Entradas 1 y 2 existen en data.sql y no tienen factura aun
        FacturaCreateDto dto = FacturaCreateDto.builder()
                .idUsuario(1L) // admin (id=1 en data.sql)
                .idEntradas(List.of(1L, 2L))
                .build();

        FacturaResponseDto result = facturaService.create(dto);

        assertThat(result.getNumeroFactura()).isNotNull();
        assertThat(result.getCodigoFactura()).startsWith("FAC-");
        assertThat(result.getEntradas()).hasSize(2);
        assertThat(result.getTotal()).isGreaterThan(0f);
    }

    @Test
    void create_deberiaCrearFacturaComoUsuario() {
        // laura es la usuaria id=2 en data.sql
        autenticarUsuario("laura");

        FacturaCreateDto dto = FacturaCreateDto.builder()
                .idUsuario(999L) // este idUsuario sera ignorado para USER; se usa el del token
                .idEntradas(List.of(3L))
                .build();

        FacturaResponseDto result = facturaService.create(dto);

        assertThat(result.getNumeroFactura()).isNotNull();
        assertThat(result.getEntradas()).hasSize(1);
    }

    @Test
    void create_deberiaFallarSiEntradaNoExiste() {
        autenticarAdmin();

        FacturaCreateDto dto = FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(9999L))
                .build();

        assertThatThrownBy(() -> facturaService.create(dto))
                .isInstanceOf(EntradaNotFound.class);
    }

    @Test
    void create_deberiaFallarSiEntradaYaTieneFactura() {
        autenticarAdmin();

        // Crear primera factura con la entrada 1
        FacturaCreateDto dto1 = FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(1L))
                .build();
        facturaService.create(dto1);

        // Intentar crear segunda factura con la misma entrada
        FacturaCreateDto dto2 = FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(1L))
                .build();

        assertThatThrownBy(() -> facturaService.create(dto2))
                .isInstanceOf(FacturaBadRequest.class)
                .hasMessageContaining("ya tiene una factura");
    }

    @Test
    void create_deberiaFallarSiUsuarioNoExiste() {
        autenticarAdmin();

        FacturaCreateDto dto = FacturaCreateDto.builder()
                .idUsuario(9999L)
                .idEntradas(List.of(1L))
                .build();

        assertThatThrownBy(() -> facturaService.create(dto))
                .isInstanceOf(UsuarioNotFound.class);
    }

    // --- findAll ---

    @Test
    void findAll_deberiaRetornarListaVaciaInicialmente() {
        autenticarAdmin();

        List<FacturaResponseDto> resultado = facturaService.findAll();

        // En data.sql no hay facturas pre-insertadas
        assertThat(resultado).isEmpty();
    }

    @Test
    void findAll_deberiaRetornarFacturasCreadas() {
        autenticarAdmin();

        facturaService.create(FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(1L))
                .build());

        facturaService.create(FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(2L))
                .build());

        List<FacturaResponseDto> resultado = facturaService.findAll();

        assertThat(resultado).hasSize(2);
    }

    // --- findById ---

    @Test
    void findById_deberiaRetornarFacturaComoAdmin() {
        autenticarAdmin();

        FacturaCreateDto dto = FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(4L))
                .build();
        FacturaResponseDto creada = facturaService.create(dto);

        FacturaResponseDto result = facturaService.findById(creada.getNumeroFactura());

        assertThat(result.getNumeroFactura()).isEqualTo(creada.getNumeroFactura());
        assertThat(result.getCodigoFactura()).isEqualTo(creada.getCodigoFactura());
    }

    @Test
    void findById_deberiaLanzarExcepcionSiNoExiste() {
        autenticarAdmin();

        assertThatThrownBy(() -> facturaService.findById(9999L))
                .isInstanceOf(FacturaNotFound.class);
    }

    @Test
    void findById_deberiaLanzarExcepcionSiUsuarioNoEsPropietario() {
        autenticarAdmin();
        // Factura creada para el admin (id=1)
        FacturaResponseDto creada = facturaService.create(FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(5L))
                .build());

        // Autenticamos como otro usuario (laura)
        autenticarUsuario("laura");

        // laura no es propietaria de la factura del admin → 404 (por diseño)
        assertThatThrownBy(() -> facturaService.findById(creada.getNumeroFactura()))
                .isInstanceOf(FacturaNotFound.class);
    }

    // --- findByUsuarioId ---

    @Test
    void findByUsuarioId_deberiaRetornarFacturasDelUsuario() {
        autenticarAdmin();

        facturaService.create(FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(6L))
                .build());

        List<FacturaResponseDto> resultado = facturaService.findByUsuarioId(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdUsuario()).isEqualTo(1L);
    }

    @Test
    void findByUsuarioId_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        autenticarAdmin();

        assertThatThrownBy(() -> facturaService.findByUsuarioId(9999L))
                .isInstanceOf(UsuarioNotFound.class);
    }

    // --- deleteById ---

    @Test
    void deleteById_deberiaEliminarFactura() {
        autenticarAdmin();

        FacturaResponseDto creada = facturaService.create(FacturaCreateDto.builder()
                .idUsuario(1L)
                .idEntradas(List.of(7L))
                .build());

        facturaService.deleteById(creada.getNumeroFactura());

        assertThatThrownBy(() -> facturaService.findById(creada.getNumeroFactura()))
                .isInstanceOf(FacturaNotFound.class);
    }

    @Test
    void deleteById_deberiaLanzarExcepcionSiNoExiste() {
        autenticarAdmin();

        assertThatThrownBy(() -> facturaService.deleteById(9999L))
                .isInstanceOf(FacturaNotFound.class);
    }

    // --- findMisFacturas ---

    @Test
    void findMisFacturas_deberiaRetornarFacturasDelUsuarioAutenticado() {
        // Primero creamos una factura como admin para laura
        autenticarAdmin();
        facturaService.create(FacturaCreateDto.builder()
                .idUsuario(2L) // laura id=2
                .idEntradas(List.of(8L))
                .build());

        // Ahora laura consulta sus facturas
        autenticarUsuario("laura");
        List<FacturaResponseDto> resultado = facturaService.findMisFacturas();

        assertThat(resultado).hasSize(1);
    }
}
