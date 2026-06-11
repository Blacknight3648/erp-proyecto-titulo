package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.infrastructure.mapper.OrdenCompraMapper;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.OrdenCompraJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenCompraRepositoryImpl implements OrdenCompraRepository {

    private final OrdenCompraJpaRepository jpaRepository;
    private final OrdenCompraMapper mapper;

    @Override
    public OrdenCompra save(OrdenCompra ordenCompra) {
        if (ordenCompra == null) {
            throw new ValidationException("La Orden de Compra no puede ser nula");
        }
        OrdenCompraJpaEntity entity = mapper.toJpaEntity(ordenCompra);
        OrdenCompraJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrdenCompra> findById(Long idOC) {
        if (idOC == null) return Optional.empty();
        return jpaRepository.findById(idOC).map(mapper::toDomain);
    }

    @Override
    public List<OrdenCompra> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<OrdenCompra> findAllByEstado(EstadoOC estado) {
        if (estado == null) return List.of();
        return jpaRepository.findAllByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdenCompra> findAllByProveedorId(Long proveedorId) {
        if (proveedorId == null) return List.of();
        return jpaRepository.findAllByProveedor_ProveedorId(proveedorId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdenCompra> findAllByHcItemId(Long hcItemId) {
        if (hcItemId == null) return List.of();
        return jpaRepository.findAllByHcItemId(hcItemId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long idOC) {
        if (idOC != null) {
            jpaRepository.deleteById(idOC);
        }
    }
}
