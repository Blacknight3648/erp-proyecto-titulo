package backend.com.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe,
 * violando una restricción de unicidad (código, nombre, etc.).
 * Genera una respuesta HTTP 409 Conflict.
 */
public class DuplicadoException extends BaseException {

    private final String campo;
    private final Object valor;

    public DuplicadoException(String campo, Object valor) {
        super(String.format("Ya existe un registro con %s: '%s'", campo, valor),
                "DUPLICATE_ENTITY",
                HttpStatus.CONFLICT);
        this.campo = campo;
        this.valor = valor;
    }

    public DuplicadoException(String mensaje) {
        super(mensaje, "DUPLICATE_ENTITY", HttpStatus.CONFLICT);
        this.campo = null;
        this.valor = null;
    }

    public String getCampo() {
        return campo;
    }

    public Object getValor() {
        return valor;
    }
}
