package backend.com.gestionUsuarios.vendedor.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VendedorNotFoundException extends RuntimeException {
    public VendedorNotFoundException(String message) {
        super(message);
    }
}
