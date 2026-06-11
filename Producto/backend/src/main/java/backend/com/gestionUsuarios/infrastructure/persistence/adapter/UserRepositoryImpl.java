package backend.com.gestionUsuarios.infrastructure.persistence.adapter;

import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.domain.repository.UserRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.UserMapper;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.UserJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.UserJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        if (user == null) {
            throw new ValidationException("El usuario no puede ser nulo");
        }
        UserJpaEntity entity = mapper.toJpaEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByUsuarioRun(String run) {
        if (run == null) return Optional.empty();
        return jpaRepository.findByUsuarioRun(run).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsuarioEmail(String email) {
        if (email == null) return Optional.empty();
        return jpaRepository.findByUsuarioEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByUsuarioEmail(String email) {
        if (email == null) return false;
        return jpaRepository.existsByUsuarioEmail(email);
    }

    @Override
    public boolean existsByUsuarioRun(String run) {
        if (run == null) return false;
        return jpaRepository.existsByUsuarioRun(run);
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) return false;
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            jpaRepository.deleteById(id);
        }
    }
}
