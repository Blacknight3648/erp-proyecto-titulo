package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.produccion.infrastructure.persistence.entity.OrdenTrabajoJpaEntity;
import backend.com.produccion.infrastructure.mapper.OrdenTrabajoMapper;
import backend.com.produccion.infrastructure.persistence.repository.OrdenTrabajoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenTrabajoRepositoryImpl implements OrdenTrabajoRepository {

    private final OrdenTrabajoJpaRepository jpaRepository;
    private final OrdenTrabajoMapper mapper;

    @Override
    public OrdenTrabajo save(OrdenTrabajo ot) {
        if (ot == null)
            throw new IllegalArgumentException("La Orden de Trabajo no puede ser nula");
        OrdenTrabajoJpaEntity entity = mapper.toJpaEntity(ot);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear OrdenTrabajo a entidad JPA");
        }
        OrdenTrabajoJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrdenTrabajo> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrdenTrabajo> findByNotaVentaId(Long notaVentaId) {
        if (notaVentaId == null)
            return List.of();
        return jpaRepository.findByNotaVentaId(notaVentaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
