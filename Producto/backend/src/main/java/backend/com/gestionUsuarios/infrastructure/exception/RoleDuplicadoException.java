package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RoleDuplicadoException extends BusinessException {
    public RoleDuplicadoException(String nombre) {
        super("Ya existe un rol con el nombre: " + nombre, HttpStatus.BAD_REQUEST);
    }
}
