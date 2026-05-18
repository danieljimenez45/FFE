package com.entradas_cine.ffe.cine.rest.entradas;

import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaCreateDto;
import com.entradas_cine.ffe.cine.rest.entradas.dto.EntradaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaBadRequest;
import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaNotFound;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.repositories.FacturaRepository;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import com.entradas_cine.ffe.cine.rest.notificaciones.repositories.NotificacionUsuarioRepository;
import com.entradas_cine.ffe.cine.web.dto.AdminEntradaView;
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
class EntradaServiceImplTest {

    @Autowired
    private EntradaService entradaService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private NotificacionUsuarioRepository notificacionRepository;

    @AfterEach
    void limpiarSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void autenticarAdmin() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "admin", "x",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    // --- create ---

    @Test
    void create_deberiaCrearEntradaConPrecioYFechaAutomaticos() {
        EntradaCreateDto dto = EntradaCreateDto.builder()
                .idSesion(1L)
                .fila(3)
                .numero(7)
                .build();

        EntradaResponseDto result = entradaService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getSesion()).isEqualTo(1L);
        assertThat(result.getFila()).isEqualTo(3);
        assertThat(result.getNumero()).isEqualTo(7);
        // precio debe venir de la sesion (8.00)
        assertThat(result.getPrecio()).isEqualTo(8.00f);
        // fecha debe ser establecida automaticamente
        assertThat(result.getFecha()).isNotNull();
    }

    @Test
    void create_deberiaLanzarExcepcionSiSesionNoExiste() {
        EntradaCreateDto dto = EntradaCreateDto.builder()
                .idSesion(9999L)
                .fila(1)
                .numero(1)
                .build();

        assertThatThrownBy(() -> entradaService.create(dto))
                .isInstanceOf(EntradaNotFound.class);
    }

    @Test
    void create_deberiaLanzarExcepcionSiButacaYaEstaOcupada() {
        EntradaCreateDto dto = EntradaCreateDto.builder()
                .idSesion(1L)
                .fila(2)
                .numero(5)
                .build();

        entradaService.create(dto);

        assertThatThrownBy(() -> entradaService.create(dto))
                .isInstanceOf(EntradaBadRequest.class)
                .hasMessageContaining("ya esta ocupada");
    }

    @Test
    void create_deberiaLanzarExcepcionSiFilaSuperaMaximoDeSala() {
        // SALA_1 tiene max 10 filas
        EntradaCreateDto dto = EntradaCreateDto.builder()
                .idSesion(1L)
                .fila(11)
                .numero(1)
                .build();

        assertThatThrownBy(() -> entradaService.create(dto))
                .isInstanceOf(EntradaBadRequest.class)
                .hasMessageContaining("maximo de filas");
    }

    @Test
    void create_deberiaLanzarExcepcionSiNumeroSuperaMaximoDeSala() {
        // SALA_1 tiene max 15 numeros
        EntradaCreateDto dto = EntradaCreateDto.builder()
                .idSesion(1L)
                .fila(1)
                .numero(16)
                .build();

        assertThatThrownBy(() -> entradaService.create(dto))
                .isInstanceOf(EntradaBadRequest.class)
                .hasMessageContaining("maximo de butacas");
    }

    // --- findById ---

    @Test
    void findById_deberiaRetornarEntradaExistente() {
        EntradaCreateDto dto = EntradaCreateDto.builder()
                .idSesion(1L).fila(1).numero(1).build();
        EntradaResponseDto created = entradaService.create(dto);

        EntradaResponseDto result = entradaService.findById(created.getId());

        assertThat(result.getId()).isEqualTo(created.getId());
        assertThat(result.getFila()).isEqualTo(1);
        assertThat(result.getNumero()).isEqualTo(1);
    }

    @Test
    void findById_deberiaLanzarExcepcionSiNoExiste() {
        assertThatThrownBy(() -> entradaService.findById(9999L))
                .isInstanceOf(EntradaNotFound.class);
    }

    // --- findBySesion ---

    @Test
    void findBySesion_deberiaRetornarListaDeEntradas() {
        entradaService.create(EntradaCreateDto.builder().idSesion(1L).fila(1).numero(1).build());
        entradaService.create(EntradaCreateDto.builder().idSesion(1L).fila(1).numero(2).build());

        List<EntradaResponseDto> result = entradaService.findBySesion(1L);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(e -> e.getSesion().equals(1L));
    }

    @Test
    void findBySesion_deberiaLanzarExcepcionSiSesionNoExiste() {
        assertThatThrownBy(() -> entradaService.findBySesion(9999L))
                .isInstanceOf(EntradaNotFound.class);
    }

    // --- deleteById ---

    @Test
    void deleteById_deberiaEliminarEntradaSinFactura() {
        autenticarAdmin();
        EntradaResponseDto created = entradaService.create(
                EntradaCreateDto.builder().idSesion(1L).fila(4).numero(4).build());

        entradaService.deleteById(created.getId());

        assertThatThrownBy(() -> entradaService.findById(created.getId()))
                .isInstanceOf(EntradaNotFound.class);
    }

    @Test
    void deleteById_conFactura_deberiaBorrarFacturaVaciaYNotificarUsuario() {
        autenticarAdmin();

        EntradaResponseDto entrada = entradaService.create(
                EntradaCreateDto.builder().idSesion(1L).fila(5).numero(5).build());

        var factura = facturaService.create(FacturaCreateDto.builder()
                .idUsuario(2L)
                .idEntradas(List.of(entrada.getId()))
                .build());

        entradaService.deleteById(entrada.getId());

        assertThat(facturaRepository.findById(factura.getNumeroFactura())).isEmpty();
        assertThat(notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(2L))
                .hasSize(1);
        assertThatThrownBy(() -> entradaService.findById(entrada.getId()))
                .isInstanceOf(EntradaNotFound.class);
    }

    @Test
    void findAllForAdmin_deberiaListarEntradasConFactura() {
        autenticarAdmin();
        EntradaResponseDto entrada = entradaService.create(
                EntradaCreateDto.builder().idSesion(1L).fila(6).numero(6).build());
        facturaService.create(FacturaCreateDto.builder()
                .idUsuario(2L)
                .idEntradas(List.of(entrada.getId()))
                .build());

        List<AdminEntradaView> lista = entradaService.findAllForAdmin("es");

        assertThat(lista).anyMatch(e -> e.id().equals(entrada.getId()));
        String codigo = lista.stream()
                .filter(e -> e.id().equals(entrada.getId()))
                .findFirst()
                .orElseThrow()
                .codigoFactura();
        assertThat(codigo).startsWith("FAC-");
    }
}
