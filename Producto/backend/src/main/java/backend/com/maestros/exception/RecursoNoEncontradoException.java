package backend.com.maestros.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un recurso en la base de datos.
 * Genera automáticamente una respuesta HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {

    private final String recurso;
    private final Object identificador;

    public RecursoNoEncontradoException(String recurso, Object identificador) {
        super(String.format("%s no encontrado/a con identificador: %s", recurso, identificador));
        this.recurso = recurso;
        this.identificador = identificador;
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
        this.recurso = null;
        this.identificador = null;
    }

    public String getRecurso() { return recurso; }
    public Object getIdentificador() { return identificador; }
}
