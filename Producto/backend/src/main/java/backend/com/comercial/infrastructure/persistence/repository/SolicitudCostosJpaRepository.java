package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.SolicitudCostosJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCostosJpaRepository extends JpaRepository<SolicitudCostosJpaEntity, Long> {
    List<SolicitudCostosJpaEntity> findByEstado(String estado);
    long countByEstado(String estado);
    long countByTipo(String tipo);
    List<SolicitudCostosJpaEntity> findByTipo(String tipo);
}


