package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.infrastructure.persistence.entity.ProductoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ProductoJpaRepository;
import backend.com.shared.infrastructure.mapper.ProductoMapper;
import backend.com.shared.domain.model.Producto;
import backend.com.shared.infrastructure.persistence.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductoRepositoryImpl implements ProductoRepository {

    private final ProductoJpaRepository jpaRepository;
    private final ProductoMapper mapper;

    @Override
    public List<Producto> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Producto> findByCodigo(String codigo) {
        if (codigo == null || codigo.isBlank())
            return Optional.empty();
        return jpaRepository.findByCodigoProducto(codigo).map(mapper::toDomain);
    }

    @Override
    public Producto save(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        ProductoJpaEntity entity = mapper.toEntity(producto);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear producto a entidad");
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            jpaRepository.deleteById(id);
        }
    }
}
