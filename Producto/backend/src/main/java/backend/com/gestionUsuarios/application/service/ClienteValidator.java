package backend.com.gestionUsuarios.application.service;

import backend.com.gestionUsuarios.domain.repository.ClienteRepository;
import backend.com.gestionUsuarios.infrastructure.exception.ClienteDuplicadoException;
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
