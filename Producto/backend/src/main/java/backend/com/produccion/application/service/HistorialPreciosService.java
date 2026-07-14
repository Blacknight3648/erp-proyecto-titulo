package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.HistorialPrecioDTO;
import backend.com.produccion.infrastructure.persistence.repository.OrdenCompraJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Historial de precios pagados por insumo/proveedor a través de las OC emitidas,
 * para el análisis comparativo de precios en Costeos.
 */
@Service
@RequiredArgsConstructor
public class HistorialPreciosService {

    private final OrdenCompraJpaRepository ordenCompraJpaRepository;

    public List<HistorialPrecioDTO> buscar(Integer articuloId, Long proveedorId) {
        return ordenCompraJpaRepository.findHistorialPrecios(articuloId, proveedorId);
    }
}
