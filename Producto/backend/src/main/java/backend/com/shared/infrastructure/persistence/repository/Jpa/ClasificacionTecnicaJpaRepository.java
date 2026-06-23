package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ClasificacionTecnicaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClasificacionTecnicaJpaRepository extends JpaRepository<ClasificacionTecnicaJpaEntity, Integer> {

    Optional<ClasificacionTecnicaJpaEntity> findByNombreClasificacionIgnoreCase(String nombreClasificacion);

    boolean existsByNombreClasificacion(String nombreClasificacion);
}