package backend.com.gestionUsuarios.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VendedorJpaRepository extends JpaRepository<VendedorJpaEntity, Long> {

    Optional<VendedorJpaEntity> findByCodigoVendedor(String codigoVendedor);

    Optional<VendedorJpaEntity> findByUsuario_UsuarioId(Long usuarioId);

    boolean existsByCodigoVendedor(String codigoVendedor);
}
