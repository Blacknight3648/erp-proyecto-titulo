package backend.com.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Acción no permitida para el rol/usuario actual. Se mapea a HTTP 403 vía el
 * GlobalExceptionHandler (cualquier BaseException usa su propio status).
 */
public class ForbiddenException extends BaseException {
    public ForbiddenException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", HttpStatus.FORBIDDEN);
    }
}
