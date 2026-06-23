package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.infrastructure.persistence.entity.OrdenServicioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenServicioJpaRepository extends JpaRepository<OrdenServicioJpaEntity, Long> {

    List<OrdenServicioJpaEntity> findAllByEstado(EstadoOS estado);

    List<OrdenServicioJpaEntity> findAllByOrdenProduccion_IdOP(Long opId);

    List<OrdenServicioJpaEntity> findAllByProveedor_ProveedorId(Long proveedorId);
}
