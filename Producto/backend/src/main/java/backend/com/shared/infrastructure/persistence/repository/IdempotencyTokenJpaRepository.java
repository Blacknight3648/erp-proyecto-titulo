package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.IdempotencyTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyTokenJpaRepository extends JpaRepository<IdempotencyTokenJpaEntity, String> {
}
