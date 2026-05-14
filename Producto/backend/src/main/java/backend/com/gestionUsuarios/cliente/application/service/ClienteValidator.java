package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.gestionUsuarios.cliente.infrastructure.exception.ClienteDuplicadoException;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClienteValidator {

    private final ClienteRepository clienteRepository;

    public void validateUniqueness(String runCliente) {
        if (clienteRepository.existsByRunCliente(runCliente)) {
            throw new ClienteDuplicadoException(runCliente);
        }
    }
}
