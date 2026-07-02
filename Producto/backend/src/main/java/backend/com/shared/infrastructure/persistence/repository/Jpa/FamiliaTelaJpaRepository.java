package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.FamiliaTelaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FamiliaTelaJpaRepository extends JpaRepository<FamiliaTelaJpaEntity, Integer> {

    Optional<FamiliaTelaJpaEntity> findByCodigoFamilia(String codigoFamilia);

    Optional<FamiliaTelaJpaEntity> findByNombreFamiliaIgnoreCase(String nombreFamilia);

    boolean existsByCodigoFamilia(String codigoFamilia);
}