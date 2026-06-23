package backend.com.gestionUsuarios.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.domain.model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository {

    List<Proveedor> findAll();

    Optional<Proveedor> findById(Long id);

    Optional<Proveedor> findByRunProveedor(String runProveedor);

    boolean existsByRunProveedor(String runProveedor);

    boolean existsById(Long id);

    List<Proveedor> buscarPorRazonSocial(String razonSocial);

    List<Proveedor> obtenerPorGiroId(Long giroId);

    List<Proveedor> obtenerPorSigla(String sigla);

    List<Proveedor> obtenerPorDescripcionGiro(String descripcionGiro);

    List<Proveedor> obtenerActivos();

    List<Proveedor> obtenerInactivos();

    List<Proveedor> obtenerActivosPorSigla(String sigla);

    List<Proveedor> obtenerActivosPorGiro(Long giroId);

    Proveedor save(Proveedor proveedor);

    void deleteById(Long id);
}
