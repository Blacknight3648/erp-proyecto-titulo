package backend.com.produccion.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraVersionJpaEntity;

import java.util.Optional;
import java.util.List;

public interface OrdenCompraVersionJpaRepository extends JpaRepository<OrdenCompraVersionJpaEntity, Long> {

    Optional<OrdenCompraVersionJpaEntity> findTopByOrdenCompra_IdOCOrderByNumeroVersionDesc(Long ocId);

    List<OrdenCompraVersionJpaEntity> findByOrdenCompra_IdOCOrderByNumeroVersionAsc(Long ocId);
}
