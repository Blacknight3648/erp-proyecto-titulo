package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GiroRepository extends JpaRepository<GiroJpaEntity, Long> {

    Optional<GiroJpaEntity> findByCodigoSii(String codigoSii);

    List<GiroJpaEntity> findByNombreGiroContainingIgnoreCase(String nombreGiro);

    Optional<GiroJpaEntity> findByNombreGiroIgnoreCase(String nombreGiro);

    List<GiroJpaEntity> findByDescripcionGiroContainingIgnoreCase(String descripcionGiro);

    boolean existsByCodigoSii(String codigoSii);
}
