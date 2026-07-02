package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.IdempotencyTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IdempotencyTokenJpaRepository extends JpaRepository<IdempotencyTokenJpaEntity, String> {
}
