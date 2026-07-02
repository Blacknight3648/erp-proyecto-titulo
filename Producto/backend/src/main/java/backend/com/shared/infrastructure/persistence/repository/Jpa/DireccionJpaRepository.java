package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DireccionJpaRepository extends JpaRepository<DireccionJpaEntity, Long> {
    List<DireccionJpaEntity> findByComuna_ComunaId(Long comunaId);
}
