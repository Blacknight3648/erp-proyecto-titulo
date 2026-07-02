package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrdenProduccionJpaRepository extends JpaRepository<OrdenProduccionJpaEntity, Long> {
    List<OrdenProduccionJpaEntity> findByNotaVentaId(Long notaVentaId);

    /** IDs de costeos que ya son usados por alguna Orden de Producción existente. */
    @Query("SELECT DISTINCT op.costeoVersion.costeo.idCosteo FROM OrdenProduccionJpaEntity op")
    List<Long> findCosteoIdsEnUso();
}
