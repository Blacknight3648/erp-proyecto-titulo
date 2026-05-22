package backend.com.shared.exception;

public class DatoBancarioNotFoundException extends EntityNotFoundException {
    public DatoBancarioNotFoundException(Integer id) {
        super("Dato bancario no encontrado con ID: " + id);
    }
}
