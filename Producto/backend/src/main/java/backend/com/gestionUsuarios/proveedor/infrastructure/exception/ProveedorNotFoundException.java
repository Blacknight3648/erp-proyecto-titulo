package backend.com.gestionUsuarios.proveedor.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ProveedorNotFoundException extends BusinessException {

    public ProveedorNotFoundException(Long id) {
        super("Proveedor con id " + id + " no encontrado", HttpStatus.NOT_FOUND);
    }
}
