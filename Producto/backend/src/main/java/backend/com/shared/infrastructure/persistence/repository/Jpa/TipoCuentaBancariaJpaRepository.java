package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.TipoCuentaBancariaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TipoCuentaBancariaJpaRepository extends JpaRepository<TipoCuentaBancariaJpaEntity, Integer> {
}
