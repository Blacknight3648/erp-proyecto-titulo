package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.RecepcionOCJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecepcionOCJpaRepository extends JpaRepository<RecepcionOCJpaEntity, Long> {

    List<RecepcionOCJpaEntity> findAllByOrdenCompra_IdOC(Long ocId);

    /**
     * Devuelve filas (ocItemId, sumRecibida) para todos los items de la OC.
     */
    @Query("""
            SELECT ri.ocItem.idOCItem AS ocItemId,
                   COALESCE(SUM(ri.cantidadRecibida), 0) AS total
            FROM RecepcionOCItemJpaEntity ri
            WHERE ri.recepcion.ordenCompra.idOC = :ocId
            GROUP BY ri.ocItem.idOCItem
            """)
    List<Object[]> sumarRecibidoPorOCItem(@Param("ocId") Long ocId);
}
