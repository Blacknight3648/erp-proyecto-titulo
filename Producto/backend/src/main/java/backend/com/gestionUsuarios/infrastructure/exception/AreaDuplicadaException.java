package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AreaDuplicadaException extends BusinessException {
    public AreaDuplicadaException(String nombre) {
        super("Ya existe un área con el nombre: " + nombre, HttpStatus.BAD_REQUEST);
    }
}
