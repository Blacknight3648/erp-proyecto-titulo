package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.SolicitudCotizacionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudCotizacionJpaRepository extends JpaRepository<SolicitudCotizacionJpaEntity, Long> {
    List<SolicitudCotizacionJpaEntity> findByEstado(String estado);
    long countByTipo(String tipo);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE SolicitudCotizacionJpaEntity s SET s.vendedor = null WHERE s.vendedor.idVendedor = :vendedorId")
    void desvincularVendedor(@org.springframework.data.repository.query.Param("vendedorId") Long vendedorId);

    void deleteByCliente_ClienteId(Long clienteId);
}


