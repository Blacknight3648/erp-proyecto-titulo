package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrdenCompraJpaRepository extends JpaRepository<OrdenCompraJpaEntity, Long> {

    List<OrdenCompraJpaEntity> findAllByEstado(EstadoOC estado);

    List<OrdenCompraJpaEntity> findAllByProveedor_ProveedorId(Long proveedorId);

    @Query("""
            SELECT DISTINCT oc FROM OrdenCompraJpaEntity oc
            JOIN oc.items oci
            JOIN oci.hcLinks link
            WHERE link.hcItem.idHCItem = :hcItemId
            """)
    List<OrdenCompraJpaEntity> findAllByHcItemId(@Param("hcItemId") Long hcItemId);
}
