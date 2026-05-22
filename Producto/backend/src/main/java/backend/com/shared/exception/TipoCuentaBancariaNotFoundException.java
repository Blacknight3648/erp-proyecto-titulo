package backend.com.shared.exception;

public class TipoCuentaBancariaNotFoundException extends EntityNotFoundException {
    public TipoCuentaBancariaNotFoundException(Integer id) {
        super("Tipo de cuenta bancaria no encontrado con ID: " + id);
    }
}
