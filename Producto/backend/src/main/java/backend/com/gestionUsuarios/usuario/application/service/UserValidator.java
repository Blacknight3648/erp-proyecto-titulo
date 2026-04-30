package backend.com.gestionUsuarios.usuario.application.service;

import backend.com.gestionUsuarios.usuario.infrastructure.exception.UserDuplicadoException;
import backend.com.gestionUsuarios.usuario.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateUniqueness(String email, String run) {
        if (userRepository.existsByUsuarioEmail(email)) {
            throw new UserDuplicadoException("email", email);
        }

        if (userRepository.existsByUsuarioRun(run)) {
            throw new UserDuplicadoException("run", run);
        }
    }
}
