package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.infrastructure.persistence.entity.TipoArticuloJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoArticuloJpaRepository extends JpaRepository<TipoArticuloJpaEntity, Integer> {

    Optional<TipoArticuloJpaEntity> findByCodigo(TipoArticulo codigo);
}
