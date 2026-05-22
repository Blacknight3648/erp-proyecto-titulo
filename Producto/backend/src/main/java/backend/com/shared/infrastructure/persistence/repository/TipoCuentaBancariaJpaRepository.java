package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.TipoCuentaBancariaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCuentaBancariaJpaRepository extends JpaRepository<TipoCuentaBancariaJpaEntity, Integer> {
}
