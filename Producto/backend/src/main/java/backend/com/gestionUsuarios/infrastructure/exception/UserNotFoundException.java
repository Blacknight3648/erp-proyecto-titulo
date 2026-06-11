package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id) {
        super("Usuario con id " + id + " no encontrado", HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String campo, String valor) {
        super("Usuario no encontrado con " + campo + ": " + valor, HttpStatus.NOT_FOUND);
    }
}
