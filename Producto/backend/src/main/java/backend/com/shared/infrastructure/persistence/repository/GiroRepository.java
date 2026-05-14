package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiroRepository extends JpaRepository<GiroJpaEntity, Long> {

    Optional<GiroJpaEntity> findByCodigoActividad(String codigoActividad);

    List<GiroJpaEntity> findByDescripcionGiroContainingIgnoreCase(String descripcionGiro);

    Optional<GiroJpaEntity> findByDescripcionGiroIgnoreCase(String descripcionGiro);

    List<GiroJpaEntity> findByTipoActividad(String tipoActividad);

    List<GiroJpaEntity> findByCategoriaTributaria(String categoriaTributaria);

    List<GiroJpaEntity> findByRegimenTributario(String regimenTributario);

    boolean existsByCodigoActividad(String codigoActividad);
}
