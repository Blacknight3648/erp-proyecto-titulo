package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.DatoBancarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatoBancarioJpaRepository extends JpaRepository<DatoBancarioJpaEntity, Integer> {
    List<DatoBancarioJpaEntity> findByBanco_BancoId(Integer bancoId);
}
