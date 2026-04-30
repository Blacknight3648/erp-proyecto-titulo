package backend.com.gestionUsuarios.cliente.infrastructure.exception;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ClienteDuplicadoException extends BusinessException {

    public ClienteDuplicadoException(String rut) {
        super("Ya existe un cliente con rut " + rut, HttpStatus.CONFLICT);
    }
}
