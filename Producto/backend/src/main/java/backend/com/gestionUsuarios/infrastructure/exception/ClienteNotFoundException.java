package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ClienteNotFoundException extends BusinessException {

    public ClienteNotFoundException(Long id) {
        super("Cliente con id " + id + " no encontrado", HttpStatus.NOT_FOUND);
    }
}
