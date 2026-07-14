package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.infrastructure.persistence.entity.OrdenServicioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenServicioJpaRepository extends JpaRepository<OrdenServicioJpaEntity, Long> {

    List<OrdenServicioJpaEntity> findAllByEstado(EstadoOS estado);

    List<OrdenServicioJpaEntity> findAllByOrdenProduccion_IdOP(Long opId);

    List<OrdenServicioJpaEntity> findAllByProveedor_ProveedorId(Long proveedorId);
}
