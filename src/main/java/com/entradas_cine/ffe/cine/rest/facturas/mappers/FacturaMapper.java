package com.entradas_cine.ffe.cine.rest.facturas.mappers;

import com.entradas_cine.ffe.cine.rest.facturas.dto.EntradaLineaDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.models.Factura;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacturaMapper {

    public FacturaResponseDto toResponseDto(Factura factura) {
        List<EntradaLineaDto> lineas = factura.getEntradas().stream()
                .map(e -> EntradaLineaDto.builder()
                        .idEntrada(e.getId())
                        .idSesion(e.getSesion().getId())
                        .fila(e.getFila())
                        .numero(e.getNumero())
                        .precio(e.getPrecio())
                        .fecha(e.getFecha())
                        .build())
                .toList();

        float total = (float) factura.getEntradas().stream()
                .mapToDouble(e -> e.getPrecio() != null ? e.getPrecio() : 0f)
                .sum();

        return FacturaResponseDto.builder()
                .numeroFactura(factura.getId())
                .codigoFactura(factura.getCodigoFactura())
                .idUsuario(factura.getUsuario().getId())
                .nombre(factura.getUsuario().getNombre())
                .apellidos(factura.getUsuario().getApellidos())
                .email(factura.getUsuario().getEmail())
                .entradas(lineas)
                .total(total)
                .build();
    }

    public List<FacturaResponseDto> toResponseDtoList(List<Factura> facturas) {
        return facturas.stream().map(this::toResponseDto).toList();
    }
}
