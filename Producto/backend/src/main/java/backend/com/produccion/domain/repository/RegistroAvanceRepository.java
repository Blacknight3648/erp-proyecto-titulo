package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.RegistroAvance;

import java.util.List;

public interface RegistroAvanceRepository {
    RegistroAvance save(RegistroAvance registro);

    List<RegistroAvance> findByOrdenTrabajoId(Long ordenTrabajoId);
}
