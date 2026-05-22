package backend.com.shared.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;

@Repository
public interface TipoContactoRepository extends JpaRepository<TipoContactoJpaEntity, Long> {

    Optional<TipoContactoJpaEntity> findByDescripcionTipoContactoIgnoreCase(String descripcionTipoContacto);
}
