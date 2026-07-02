package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.GastoAdicionalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GastoAdicionalJpaRepository extends JpaRepository<GastoAdicionalJpaEntity, Long> {
}
