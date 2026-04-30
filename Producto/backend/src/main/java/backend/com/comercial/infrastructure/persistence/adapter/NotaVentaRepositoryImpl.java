package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.comercial.infrastructure.persistence.entity.NotaVentaJpaEntity;
import backend.com.comercial.infrastructure.mapper.NotaVentaMapper;
import backend.com.comercial.infrastructure.persistence.repository.NotaVentaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotaVentaRepositoryImpl implements NotaVentaRepository {

    private final NotaVentaJpaRepository jpaRepository;
    private final NotaVentaMapper mapper;

    @Override
    public NotaVenta save(NotaVenta notaVenta) {
        NotaVentaJpaEntity entity = mapper.toEntity(notaVenta);
        if (entity == null) {
            throw new IllegalArgumentException("La entidad de Nota de Venta no puede ser nula");
        }
        NotaVentaJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<NotaVenta> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public Optional<NotaVenta> findByNumero(Long numero) {
        return jpaRepository.findAll().stream()
                .filter(e -> e.getNumeroNV().equals(numero))
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Long> findMaxNumero() {
        return jpaRepository.findMaxNumero();
    }

    @Override
    public java.util.List<NotaVenta> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}
