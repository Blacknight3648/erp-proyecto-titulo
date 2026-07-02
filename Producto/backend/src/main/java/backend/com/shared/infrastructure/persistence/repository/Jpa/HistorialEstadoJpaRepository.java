package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.HistorialEstadoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialEstadoJpaRepository extends JpaRepository<HistorialEstadoJpaEntity, Long> {
    List<HistorialEstadoJpaEntity> findByTipoEntidadAndEntidadIdOrderByFechaAsc(String tipoEntidad, Long entidadId);
}
