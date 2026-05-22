package backend.com.shared.exception;

public class ComunaNotFoundException extends EntityNotFoundException {
    public ComunaNotFoundException(Long id) {
        super("Comuna no encontrada con ID: " + id);
    }

    public ComunaNotFoundException(String nombre) {
        super("Comuna no encontrada: " + nombre);
    }
}
