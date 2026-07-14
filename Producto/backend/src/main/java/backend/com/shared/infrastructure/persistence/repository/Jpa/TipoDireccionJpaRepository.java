package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.TipoDireccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TipoDireccionJpaRepository extends JpaRepository<TipoDireccionJpaEntity, Integer> {
}
