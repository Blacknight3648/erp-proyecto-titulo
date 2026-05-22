package backend.com.shared.exception;

public class DireccionNotFoundException extends EntityNotFoundException {
    public DireccionNotFoundException(Long id) {
        super("Dirección no encontrada con ID: " + id);
    }
}
