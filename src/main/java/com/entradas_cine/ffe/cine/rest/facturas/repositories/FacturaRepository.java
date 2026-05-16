package com.entradas_cine.ffe.cine.rest.facturas.repositories;

import com.entradas_cine.ffe.cine.rest.entradas.models.Entrada;
import com.entradas_cine.ffe.cine.rest.facturas.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    boolean existsByCodigoFactura(String codigoFactura);

    boolean existsByEntradasContaining(Entrada entrada);

    List<Factura> findByUsuarioId(Long usuarioId);

    boolean existsByUsuario_IdAndEntradas_Id(Long usuarioId, Long entradaId);
}
