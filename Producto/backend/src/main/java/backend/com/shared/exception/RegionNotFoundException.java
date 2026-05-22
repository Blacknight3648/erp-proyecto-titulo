package backend.com.shared.exception;

public class RegionNotFoundException extends EntityNotFoundException {
    public RegionNotFoundException(Long id) {
        super("Región no encontrada con ID: " + id);
    }

    public RegionNotFoundException(String nombre) {
        super("Región no encontrada: " + nombre);
    }
}
