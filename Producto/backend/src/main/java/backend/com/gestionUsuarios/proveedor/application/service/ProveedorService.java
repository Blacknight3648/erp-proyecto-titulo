package backend.com.gestionUsuarios.proveedor.application.service;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    List<Proveedor> listarTodos();

    Optional<Proveedor> obtenerPorId(Long proveedorId);

    Optional<Proveedor> obtenerPorRun(String runProveedor);

    List<Proveedor> buscarPorRazonSocial(String razonSocial);

    List<Proveedor> obtenerActivos();

    List<Proveedor> obtenerInactivos();

    List<Proveedor> obtenerPorSiglaId(Long siglaId);

    List<Proveedor> obtenerPorGiroId(Long giroId);

    List<Proveedor> obtenerPorSiglaAbreviatura(String siglaAbreviatura);

    List<Proveedor> obtenerPorDescripcionGiro(String descripcionGiro);

    List<Proveedor> obtenerActivosPorSigla(Long siglaId);

    List<Proveedor> obtenerActivosPorGiro(Long giroId);

    Proveedor crear(Proveedor proveedor);

    Proveedor actualizar(Long proveedorId, Proveedor proveedor);

    void eliminar(Long proveedorId);
}
