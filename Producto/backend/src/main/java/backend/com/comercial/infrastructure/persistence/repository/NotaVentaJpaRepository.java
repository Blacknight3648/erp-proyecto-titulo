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
        "UPDATE NotaVentaItemJpaEntity i SET i.opId = :opId WHERE i.idItemNV = :itemId")
    void vincularOpAItem(
        @org.springframework.data.repository.query.Param("itemId") Long itemId,
        @org.springframework.data.repository.query.Param("opId") Long opId);
}
