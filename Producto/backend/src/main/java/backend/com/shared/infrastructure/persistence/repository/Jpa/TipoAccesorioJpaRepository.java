package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.TipoAccesorioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoAccesorioJpaRepository extends JpaRepository<TipoAccesorioJpaEntity, Integer> {

    boolean existsByCodigo(String codigo);

    boolean existsByNombreIgnoreCase(String nombre);
}
