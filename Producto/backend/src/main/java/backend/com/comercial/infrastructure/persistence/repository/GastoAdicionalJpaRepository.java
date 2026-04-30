package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.GastoAdicionalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoAdicionalJpaRepository extends JpaRepository<GastoAdicionalJpaEntity, Long> {
}
