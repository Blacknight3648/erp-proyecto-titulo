package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.SeguimientoOP;
import java.util.Optional;

public interface SeguimientoOPRepository {
    Optional<SeguimientoOP> findByOrdenProduccionId(Long opId);
    SeguimientoOP save(SeguimientoOP seguimiento);
}
