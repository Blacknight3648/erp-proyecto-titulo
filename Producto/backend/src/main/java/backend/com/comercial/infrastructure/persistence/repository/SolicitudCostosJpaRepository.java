package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.SolicitudCostosJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudCostosJpaRepository extends JpaRepository<SolicitudCostosJpaEntity, Long> {
    List<SolicitudCostosJpaEntity> findByEstado(String estado);
    long countByEstado(String estado);
    long countByTipo(String tipo);
    List<SolicitudCostosJpaEntity> findByTipo(String tipo);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE SolicitudCostosJpaEntity s SET s.vendedor = null WHERE s.vendedor.idVendedor = :vendedorId")
    void desvincularVendedor(@org.springframework.data.repository.query.Param("vendedorId") Long vendedorId);

    void deleteByCliente_ClienteId(Long clienteId);
}


