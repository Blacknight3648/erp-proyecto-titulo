package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException(Long id) {
        super("No se encontró el rol con ID: " + id, HttpStatus.NOT_FOUND);
    }
}
