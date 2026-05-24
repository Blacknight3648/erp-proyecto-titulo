package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.domain.model.EstadoHC;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HojaCompraJpaRepository extends JpaRepository<HojaCompraJpaEntity, Long> {

    Optional<HojaCompraJpaEntity> findByOrdenProduccion_IdOP(Long opId);

    boolean existsByOrdenProduccion_IdOP(Long opId);

    List<HojaCompraJpaEntity> findAllByEstado(EstadoHC estado);

    @Query("SELECT DISTINCT hc FROM HojaCompraJpaEntity hc JOIN hc.items i WHERE i.idHCItem IN :ids")
    List<HojaCompraJpaEntity> findAllByItemIds(@Param("ids") List<Long> ids);
}
