package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.SolicitudCotizacionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCotizacionJpaRepository extends JpaRepository<SolicitudCotizacionJpaEntity, Long> {
    List<SolicitudCotizacionJpaEntity> findByEstado(String estado);
    long countByTipo(String tipo);
}


