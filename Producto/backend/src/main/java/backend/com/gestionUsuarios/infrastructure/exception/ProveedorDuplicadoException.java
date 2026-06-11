package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ProveedorDuplicadoException extends BusinessException {

    public ProveedorDuplicadoException(String run) {
        super("Ya existe un proveedor con RUN " + run, HttpStatus.CONFLICT);
    }
}
