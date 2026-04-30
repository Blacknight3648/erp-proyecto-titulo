package backend.com.gestionUsuarios.proveedor.application.service;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.mapper.ProveedorMapper;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    @SuppressWarnings("unused")
    private final ProveedorMapper proveedorMapper;
    private final ProveedorValidator proveedorValidator;

    @Override
    public Proveedor crearProveedor(Proveedor proveedor) {
        proveedorValidator.validateUniqueness(proveedor.getRutProveedor());
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizarProveedor(Long id, Proveedor proveedor) {

        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setProveedorId(existente.getProveedorId());

        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor obtenerProveedor(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    @Override
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public void eliminarProveedor(Long id) {

        if (proveedorRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        proveedorRepository.deleteById(id);
    }
}
