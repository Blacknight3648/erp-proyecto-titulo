package backend.com.gestionUsuarios.infrastructure.persistence.adapter;

import backend.com.gestionUsuarios.domain.model.Vendedor;
import backend.com.gestionUsuarios.domain.repository.VendedorRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.VendedorMapper;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.UserJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.UserJpaRepository;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.VendedorJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendedorRepositoryImpl implements VendedorRepository {

    private final VendedorJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final VendedorMapper mapper;

    @Override
    public Vendedor save(Vendedor vendedor) {
        if (vendedor == null) {
            throw new ValidationException("El vendedor no puede ser nulo");
        }
        VendedorJpaEntity entity = mapper.toJpaEntity(vendedor);
        if (vendedor.getUsuarioId() != null) {
            UserJpaEntity userEntity = userJpaRepository.findById(vendedor.getUsuarioId())
                    .orElseThrow(() -> new ValidationException("Usuario no encontrado para asociar al vendedor"));
            entity.setUsuario(userEntity);
        }
        VendedorJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Vendedor> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Vendedor> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Vendedor> findByCodigoVendedor(String codigoVendedor) {
        if (codigoVendedor == null) return Optional.empty();
        return jpaRepository.findByCodigoVendedor(codigoVendedor).map(mapper::toDomain);
    }

    @Override
    public Optional<Vendedor> findByUsuario_UsuarioId(Long usuarioId) {
        if (usuarioId == null) return Optional.empty();
        return jpaRepository.findByUsuario_UsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoVendedor(String codigoVendedor) {
        if (codigoVendedor == null) return false;
        return jpaRepository.existsByCodigoVendedor(codigoVendedor);
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
