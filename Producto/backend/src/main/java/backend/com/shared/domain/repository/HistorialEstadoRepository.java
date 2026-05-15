package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.HistorialEstado;

import java.util.List;

public interface HistorialEstadoRepository {
    HistorialEstado save(HistorialEstado historial);

    List<HistorialEstado> findByEntidad(String tipoEntidad, Long entidadId);
}
