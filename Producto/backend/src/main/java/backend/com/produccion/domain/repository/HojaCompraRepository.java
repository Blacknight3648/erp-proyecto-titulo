package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.EstadoHC;
import backend.com.produccion.domain.model.HojaCompra;

import java.util.List;
import java.util.Optional;

public interface HojaCompraRepository {

    HojaCompra save(HojaCompra hojaCompra);

    Optional<HojaCompra> findById(Long idHC);

    Optional<HojaCompra> findByOpId(Long opId);

    boolean existsByOpId(Long opId);

    List<HojaCompra> findAll();

    List<HojaCompra> findAllByEstado(EstadoHC estado);

    /**
     * Recupera todas las HCs que contienen al menos uno de los hcItemIds dados.
     * Útil para consolidar OCs desde HCs aprobadas.
     */
    List<HojaCompra> findAllByItemIds(List<Long> hcItemIds);
}
