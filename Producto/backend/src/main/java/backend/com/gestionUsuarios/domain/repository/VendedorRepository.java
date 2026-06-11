package backend.com.gestionUsuarios.domain.repository;

import backend.com.gestionUsuarios.domain.model.Vendedor;

import java.util.List;
import java.util.Optional;

public interface VendedorRepository {

    Vendedor save(Vendedor vendedor);

    Optional<Vendedor> findById(Long id);

    List<Vendedor> findAll();

    Optional<Vendedor> findByCodigoVendedor(String codigoVendedor);

    Optional<Vendedor> findByUsuario_UsuarioId(Long usuarioId);

    boolean existsByCodigoVendedor(String codigoVendedor);

    boolean existsById(Long id);

    void deleteById(Long id);
}
