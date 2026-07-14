package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.application.dto.HistorialPrecioDTO;
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

    @Query("""
            SELECT new backend.com.produccion.application.dto.HistorialPrecioDTO(
                oc.numeroOC, oc.fechaEmision, oci.articuloId, oci.nombreInsumo,
                oc.proveedor.proveedorId, oc.proveedor.razonSocialProveedor, oci.precioUnitario)
            FROM OrdenCompraJpaEntity oc
            JOIN oc.items oci
            WHERE (:articuloId IS NULL OR oci.articuloId = :articuloId)
              AND (:proveedorId IS NULL OR oc.proveedor.proveedorId = :proveedorId)
            ORDER BY oc.fechaEmision DESC
            """)
    List<HistorialPrecioDTO> findHistorialPrecios(@Param("articuloId") Integer articuloId,
            @Param("proveedorId") Long proveedorId);
}
