package backend.com.gestionUsuarios.cliente.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ClienteInactivoException extends BusinessException {

    public ClienteInactivoException(Long id) {
        super("El cliente con id " + id + " está inactivo", HttpStatus.FORBIDDEN);
    }
}
