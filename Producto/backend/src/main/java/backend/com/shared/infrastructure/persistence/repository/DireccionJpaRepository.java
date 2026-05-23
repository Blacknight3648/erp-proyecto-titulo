package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionJpaRepository extends JpaRepository<DireccionJpaEntity, Long> {
    List<DireccionJpaEntity> findByComuna_ComunaId(Long comunaId);
}
