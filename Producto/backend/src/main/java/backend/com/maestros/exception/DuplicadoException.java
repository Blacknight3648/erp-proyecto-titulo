package backend.com.maestros.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe,
 * violando una restricción de unicidad (código, nombre, etc.).
 * Genera automáticamente una respuesta HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicadoException extends RuntimeException {

    private final String campo;
    private final Object valor;

    public DuplicadoException(String campo, Object valor) {
        super(String.format("Ya existe un registro con %s: '%s'", campo, valor));
        this.campo = campo;
        this.valor = valor;
    }

    public String getCampo() { return campo; }
    public Object getValor() { return valor; }
}
