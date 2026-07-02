package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.NotaVentaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaVentaJpaRepository extends JpaRepository<NotaVentaJpaEntity, Long> {
    List<NotaVentaJpaEntity> findByEstado(String estado);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE NotaVentaJpaEntity n SET n.vendedor = null WHERE n.vendedor.idVendedor = :vendedorId")
    void desvincularVendedor(@org.springframework.data.repository.query.Param("vendedorId") Long vendedorId);

    void deleteByCliente_ClienteId(Long clienteId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query(
        "UPDATE NotaVentaItemJpaEntity i SET i.opId = :opId " +
        "WHERE i.notaVenta.idNV = :notaVentaId AND i.tipoItem = :tipoOP")
    void vincularOpAItems(
        @org.springframework.data.repository.query.Param("notaVentaId") Long notaVentaId,
        @org.springframework.data.repository.query.Param("opId") Long opId,
        @org.springframework.data.repository.query.Param("tipoOP") backend.com.comercial.domain.enums.TipoItem tipoOP);
}
