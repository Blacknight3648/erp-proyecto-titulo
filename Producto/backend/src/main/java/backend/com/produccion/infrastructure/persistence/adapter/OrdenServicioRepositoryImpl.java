package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.EstadoOS;
import backend.com.produccion.domain.model.OrdenServicio;
import backend.com.produccion.domain.repository.OrdenServicioRepository;
import backend.com.produccion.infrastructure.mapper.OrdenServicioMapper;
import backend.com.produccion.infrastructure.persistence.entity.OrdenServicioJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.OrdenServicioJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenServicioRepositoryImpl implements OrdenServicioRepository {

    private final OrdenServicioJpaRepository jpaRepository;
    private final OrdenServicioMapper mapper;

    @Override
    public OrdenServicio save(OrdenServicio ordenServicio) {
        if (ordenServicio == null) {
            throw new ValidationException("La Orden de Servicio no puede ser nula");
        }
        OrdenServicioJpaEntity entity = mapper.toJpaEntity(ordenServicio);
        OrdenServicioJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrdenServicio> findById(Long idOS) {
        if (idOS == null) return Optional.empty();
        return jpaRepository.findById(idOS).map(mapper::toDomain);
    }

    @Override
    public List<OrdenServicio> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<OrdenServicio> findAllByEstado(EstadoOS estado) {
        if (estado == null) return List.of();
        return jpaRepository.findAllByEstado(estado).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<OrdenServicio> findAllByOpId(Long opId) {
        if (opId == null) return List.of();
        return jpaRepository.findAllByOrdenProduccion_IdOP(opId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<OrdenServicio> findAllByProveedorId(Long proveedorId) {
        if (proveedorId == null) return List.of();
        return jpaRepository.findAllByProveedor_ProveedorId(proveedorId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
}
