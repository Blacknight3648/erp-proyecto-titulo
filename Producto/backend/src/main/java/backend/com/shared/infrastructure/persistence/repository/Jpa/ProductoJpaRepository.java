package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ProductoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoJpaEntity, Long> {
    Optional<ProductoJpaEntity> findByCodigoProducto(String codigoProducto);
}
