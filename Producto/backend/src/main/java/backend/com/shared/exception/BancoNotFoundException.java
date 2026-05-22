package backend.com.shared.exception;

public class BancoNotFoundException extends EntityNotFoundException {
    public BancoNotFoundException(Integer id) {
        super("Banco no encontrado con ID: " + id);
    }

    public BancoNotFoundException(String codigo) {
        super("Banco no encontrado con código: " + codigo);
    }
}
