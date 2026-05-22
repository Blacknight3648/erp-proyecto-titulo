package backend.com.shared.exception;

public class TipoDireccionNotFoundException extends EntityNotFoundException {
    public TipoDireccionNotFoundException(Integer id) {
        super("Tipo de dirección no encontrado con ID: " + id);
    }
}
