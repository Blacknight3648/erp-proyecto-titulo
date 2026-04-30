package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository {
    List<Proveedor> findAll();

    Optional<Proveedor> findById(Long id);

    Proveedor save(Proveedor proveedor);

    Optional<Proveedor> findByRutProveedor(String rutProveedor);

    void deleteById(Long id);
}
