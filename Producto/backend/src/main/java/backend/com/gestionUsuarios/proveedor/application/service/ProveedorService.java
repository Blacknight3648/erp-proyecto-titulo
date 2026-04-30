package backend.com.gestionUsuarios.proveedor.application.service;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;

import java.util.List;

public interface ProveedorService {

    Proveedor crearProveedor(Proveedor proveedor);

    Proveedor actualizarProveedor(Long id, Proveedor proveedor);

    Proveedor obtenerProveedor(Long id);

    List<Proveedor> listarProveedores();

    void eliminarProveedor(Long id);

}
