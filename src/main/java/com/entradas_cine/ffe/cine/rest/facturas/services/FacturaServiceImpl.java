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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
                        throw new FacturaBadRequest("La entrada con id " + idEntrada + " ya tiene una factura asociada");
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

    @Override
    public FacturaResponseDto findById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFound(id));
        return facturaMapper.toResponseDto(factura);
    }

    @Override
    public List<FacturaResponseDto> findAll() {
        return facturaMapper.toResponseDtoList(facturaRepository.findAll());
    }

    @Override
    public List<FacturaResponseDto> findByUsuarioId(Long usuarioId) {
        return facturaMapper.toResponseDtoList(facturaRepository.findByUsuarioId(usuarioId));
    }

    @Override
    public void deleteById(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new FacturaNotFound(id);
        }
        facturaRepository.deleteById(id);
    }

    private String generarCodigoFacturaUnico() {
        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String codigo;

        do {
            String random = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
            codigo = "FAC-" + fecha + "-" + random;
        } while (facturaRepository.existsByCodigoFactura(codigo));

        return codigo;
    }
}