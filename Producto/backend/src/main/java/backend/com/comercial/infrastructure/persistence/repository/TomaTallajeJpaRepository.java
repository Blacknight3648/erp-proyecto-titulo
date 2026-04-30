package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.TomaTallajeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TomaTallajeJpaRepository extends JpaRepository<TomaTallajeJpaEntity, Long> {
}
