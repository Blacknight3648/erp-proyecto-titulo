package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository {

    List<Proveedor> findAll();

    Optional<Proveedor> findById(Long id);

    Optional<Proveedor> findByRunProveedor(String runProveedor);

    boolean existsByRunProveedor(String runProveedor);

    boolean existsById(Long id);

    List<Proveedor> buscarPorRazonSocial(String razonSocial);

    List<Proveedor> obtenerPorSiglaId(Long siglaId);

    List<Proveedor> obtenerPorGiroId(Long giroId);

    List<Proveedor> obtenerPorSiglaAbreviatura(String siglaAbreviatura);

    List<Proveedor> obtenerPorDescripcionGiro(String descripcionGiro);

    List<Proveedor> obtenerActivos();

    List<Proveedor> obtenerInactivos();

    List<Proveedor> obtenerActivosPorSigla(Long siglaId);

    List<Proveedor> obtenerActivosPorGiro(Long giroId);

    Proveedor save(Proveedor proveedor);

    void deleteById(Long id);
}
