package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.SeguimientoOPJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeguimientoOPJpaRepository extends JpaRepository<SeguimientoOPJpaEntity, Long> {
    Optional<SeguimientoOPJpaEntity> findByOrdenProduccion_IdOP(Long idOP);
}
