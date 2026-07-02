package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionNegocioJpaRepository extends JpaRepository<EvaluacionNegocioJpaEntity, Long> {
    List<EvaluacionNegocioJpaEntity> findByEstado(String estado);

    /** IDs de costeos ya vinculados a algún ítem de cualquier EVN. */
    @org.springframework.data.jpa.repository.Query(
            "SELECT DISTINCT i.costeoId FROM EvaluacionNegocioItemJpaEntity i WHERE i.costeoId IS NOT NULL")
    List<Long> findLinkedCosteoIds();

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE EvaluacionNegocioJpaEntity e SET e.vendedor = null WHERE e.vendedor.idVendedor = :vendedorId")
    void desvincularVendedor(@org.springframework.data.repository.query.Param("vendedorId") Long vendedorId);

    void deleteByCliente_ClienteId(Long clienteId);
}
