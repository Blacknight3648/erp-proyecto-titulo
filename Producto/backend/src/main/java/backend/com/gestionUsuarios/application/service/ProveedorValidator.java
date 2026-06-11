package backend.com.gestionUsuarios.proveedor.application.service;

import backend.com.gestionUsuarios.proveedor.infrastructure.exception.ProveedorDuplicadoException;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProveedorValidator {

    private final ProveedorRepository proveedorRepository;

    public void validateUniqueness(String runProveedor) {
        if (proveedorRepository.existsByRunProveedor(runProveedor)) {
            throw new ProveedorDuplicadoException(runProveedor);
        }
    }
}
