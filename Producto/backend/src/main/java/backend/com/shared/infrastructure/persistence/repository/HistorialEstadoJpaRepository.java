package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.HistorialEstadoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoJpaRepository extends JpaRepository<HistorialEstadoJpaEntity, Long> {
    List<HistorialEstadoJpaEntity> findByTipoEntidadAndEntidadIdOrderByFechaAsc(String tipoEntidad, Long entidadId);
}
