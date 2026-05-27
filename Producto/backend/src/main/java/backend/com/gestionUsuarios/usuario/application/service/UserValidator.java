package backend.com.gestionUsuarios.usuario.application.service;

import backend.com.gestionUsuarios.usuario.infrastructure.exception.UserDuplicadoException;
import backend.com.gestionUsuarios.usuario.infrastructure.persistence.repository.UserRepository;
import backend.com.shared.validations.run.RunValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final RunValidator runValidator = new RunValidator();

    public void validateUniqueness(String email, String run) {
        if (userRepository.existsByUsuarioEmail(email)) {
            throw new UserDuplicadoException("email", email);
        }

        if (userRepository.existsByUsuarioRun(run)) {
            throw new UserDuplicadoException("run", run);
        }
    }

    public void validateRun(String run) {
        if (!runValidator.isValid(run, null)) {
            throw new IllegalArgumentException("RUN inválido: " + run);
        }
    }
}
