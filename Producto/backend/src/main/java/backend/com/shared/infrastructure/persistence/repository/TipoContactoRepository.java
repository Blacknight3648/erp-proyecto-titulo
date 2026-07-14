package backend.com.shared.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;

public interface TipoContactoRepository extends JpaRepository<TipoContactoJpaEntity, Long> {

    Optional<TipoContactoJpaEntity> findByDescripcionTipoContactoIgnoreCase(String descripcionTipoContacto);
}
