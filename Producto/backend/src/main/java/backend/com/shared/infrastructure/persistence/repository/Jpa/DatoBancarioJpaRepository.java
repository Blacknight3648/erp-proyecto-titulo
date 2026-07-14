package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.DatoBancarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatoBancarioJpaRepository extends JpaRepository<DatoBancarioJpaEntity, Integer> {
    List<DatoBancarioJpaEntity> findByBanco_BancoId(Integer bancoId);
}
