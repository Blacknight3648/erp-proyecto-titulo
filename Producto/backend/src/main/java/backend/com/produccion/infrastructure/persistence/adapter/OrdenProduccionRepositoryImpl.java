package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.mapper.OrdenProduccionMapper;
import backend.com.produccion.infrastructure.persistence.repository.OrdenProduccionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenProduccionRepositoryImpl implements OrdenProduccionRepository {

    private final OrdenProduccionJpaRepository jpaRepository;
    private final OrdenProduccionMapper mapper;

    @Override
    public OrdenProduccion save(OrdenProduccion op) {
        if (op == null)
            throw new IllegalArgumentException("La Orden de Produccion no puede ser nula");
        OrdenProduccionJpaEntity entity = mapper.toJpaEntity(op);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear OrdenProduccion a entidad JPA");
        }
        OrdenProduccionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrdenProduccion> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrdenProduccion> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdenProduccion> findByNotaVentaId(Long notaVentaId) {
        if (notaVentaId == null)
            return List.of();
        return jpaRepository.findByNotaVentaId(notaVentaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findCosteoIdsEnUso() {
        return jpaRepository.findCosteoIdsEnUso();
    }
}
