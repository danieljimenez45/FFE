package com.entradas_cine.ffe.cine.rest.facturas.services;

import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaCreateDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;

import java.util.List;

public interface FacturaService {

    FacturaResponseDto create(FacturaCreateDto dto);

    /**
     * Devuelve una factura por id.
     * Si el usuario en sesión no es ADMIN, solo puede ver sus propias facturas.
     */
    FacturaResponseDto findById(Long id);

    /** Solo ADMIN. */
    List<FacturaResponseDto> findAll();

    /**
     * Devuelve las facturas de un usuario por su id.
     * Si el usuario en sesión no es ADMIN, solo puede consultar sus propias facturas.
     */
    List<FacturaResponseDto> findByUsuarioId(Long usuarioId);

    /**
     * Devuelve las facturas del usuario actualmente autenticado.
     * Equivale a /me/facturas.
     */
    List<FacturaResponseDto> findMisFacturas();

    void deleteById(Long id);
}
