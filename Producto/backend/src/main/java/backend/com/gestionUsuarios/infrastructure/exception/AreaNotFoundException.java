package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AreaNotFoundException extends BusinessException {
    public AreaNotFoundException(Long id) {
        super("No se encontró el área con ID: " + id, HttpStatus.NOT_FOUND);
    }
}
