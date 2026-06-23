package backend.com.gestionUsuarios.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ClienteDuplicadoException extends BusinessException {

    public ClienteDuplicadoException(String run) {
        super("Ya existe un cliente con RUN " + run, HttpStatus.CONFLICT);
    }
}
