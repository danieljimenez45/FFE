package com.entradas_cine.ffe.cine.rest.facturas.services;

import com.entradas_cine.ffe.cine.rest.entradas.exceptions.EntradaNotFound;
import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.entradas.repositories.EntradaRepository;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.exceptions.FacturaBadRequest;
import com.entradas_cine.ffe.cine.rest.facturas.exceptions.FacturaNotFound;
import com.entradas_cine.ffe.cine.rest.facturas.mappers.FacturaMapper;
import com.entradas_cine.ffe.cine.rest.facturas.models.Factura;
import com.entradas_cine.ffe.cine.rest.facturas.repositories.FacturaRepository;
import com.entradas_cine.ffe.cine.rest.usuarios.exceptions.UsuarioNotFound;
import com.entradas_cine.ffe.cine.rest.usuarios.models.Usuario;
import com.entradas_cine.ffe.cine.rest.usuarios.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final EntradaRepository entradaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FacturaMapper facturaMapper;

    @Override
    public FacturaResponseDto create(FacturaCreateDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioNotFound(dto.getIdUsuario()));

        List<Entrada> entradas = dto.getIdEntradas().stream()
                .map(idEntrada -> {
                    Entrada entrada = entradaRepository.findById(idEntrada)
                            .orElseThrow(() -> new EntradaNotFound(idEntrada));
                    if (facturaRepository.existsByEntradasContaining(entrada)) {
                        throw new FacturaBadRequest(
                                "La entrada con id " + idEntrada + " ya tiene una factura asociada");
                    }
                    return entrada;
                })
                .toList();

        Factura factura = Factura.builder()
                .codigoFactura(generarCodigoFacturaUnico())
                .usuario(usuario)
                .entradas(entradas)
                .build();

        return facturaMapper.toResponseDto(facturaRepository.save(factura));
    }

    /**
     * Devuelve una factura por id.
     * Control de acceso: un usuario normal solo puede ver sus propias facturas.
     * Los ADMIN pueden ver cualquier factura.
     */
    @Override
    public FacturaResponseDto findById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFound(id));

        if (!esAdminOPropietario(factura.getUsuario().getUsername())) {
            log.warn("Acceso denegado: usuario '{}' intentó ver la factura {}",
                    usernameActual(), id);
            // Devolvemos 404 en vez de 403 para no filtrar si la factura existe
            throw new FacturaNotFound(id);
        }

        return facturaMapper.toResponseDto(factura);
    }

    /** Solo accesible por ADMIN (protegido vía @PreAuthorize en el controlador). */
    @Override
    public List<FacturaResponseDto> findAll() {
        return facturaMapper.toResponseDtoList(facturaRepository.findAll());
    }

    /**
     * Devuelve las facturas de un usuario por su id.
     * Control de acceso: un usuario normal solo puede consultar sus propias facturas.
     */
    @Override
    public List<FacturaResponseDto> findByUsuarioId(Long usuarioId) {
        // Verificar que el usuario solicitado existe
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNotFound(usuarioId);
        }

        // Un USER solo puede ver sus propias facturas
        if (!esAdminOPropietarioPorId(usuarioId)) {
            log.warn("Acceso denegado: usuario '{}' intentó ver facturas del usuario {}",
                    usernameActual(), usuarioId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permiso para ver las facturas de otro usuario");
        }

        return facturaMapper.toResponseDtoList(facturaRepository.findByUsuarioId(usuarioId));
    }

    /**
     * Devuelve las facturas del usuario actualmente autenticado.
     * Ruta /me/facturas — no necesita check adicional porque siempre es el usuario en sesión.
     */
    @Override
    public List<FacturaResponseDto> findMisFacturas() {
        String username = usernameActual();
        log.info("Buscando facturas del usuario autenticado: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNotFound(username));

        return facturaMapper.toResponseDtoList(
                facturaRepository.findByUsuarioId(usuario.getId()));
    }

    @Override
    public void deleteById(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new FacturaNotFound(id);
        }
        facturaRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // Helpers de control de acceso
    // -------------------------------------------------------------------------

    /** Devuelve el username del usuario autenticado actualmente. */
    private String usernameActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        return auth.getName();
    }

    /** true si el usuario en sesión es ADMIN o es el propietario (por username). */
    private boolean esAdminOPropietario(String usernameDelRecurso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return esAdmin || auth.getName().equals(usernameDelRecurso);
    }

    /** true si el usuario en sesión es ADMIN o su id coincide con usuarioId. */
    private boolean esAdminOPropietarioPorId(Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (esAdmin) return true;

        // Cargar el id del usuario autenticado para comparar
        return usuarioRepository.findByUsername(auth.getName())
                .map(u -> u.getId().equals(usuarioId))
                .orElse(false);
    }

    private String generarCodigoFacturaUnico() {
        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String codigo;
        do {
            String random = UUID.randomUUID().toString()
                    .replace("-", "").substring(0, 6).toUpperCase();
            codigo = "FAC-" + fecha + "-" + random;
        } while (facturaRepository.existsByCodigoFactura(codigo));
        return codigo;
    }
}
