package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ComposicionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComposicionJpaRepository extends JpaRepository<ComposicionJpaEntity, Integer> {

    Optional<ComposicionJpaEntity> findByCodigoComposicion(String codigoComposicion);

    boolean existsByCodigoComposicion(String codigoComposicion);

    List<ComposicionJpaEntity> findByClasificacionIgnoreCase(String clasificacion);
}