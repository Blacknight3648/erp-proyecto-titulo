package backend.com.shared.exception;

public class PaisNotFoundException extends EntityNotFoundException {
    public PaisNotFoundException(Integer id) {
        super("País no encontrado con ID: " + id);
    }

    public PaisNotFoundException(String nombre) {
        super("País no encontrado: " + nombre);
    }
}
