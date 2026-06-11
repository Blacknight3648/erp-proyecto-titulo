package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.infrastructure.mapper.HojaCompraMapper;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.HojaCompraJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HojaCompraRepositoryImpl implements HojaCompraRepository {

    private final HojaCompraJpaRepository jpaRepository;
    private final HojaCompraMapper mapper;

    @Override
    public HojaCompra save(HojaCompra hojaCompra) {
        if (hojaCompra == null) {
            throw new ValidationException("La Hoja de Compra no puede ser nula");
        }
        HojaCompraJpaEntity entity = mapper.toJpaEntity(hojaCompra);
        HojaCompraJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<HojaCompra> findById(Long idHC) {
        if (idHC == null) return Optional.empty();
        return jpaRepository.findById(idHC).map(mapper::toDomain);
    }

    @Override
    public Optional<HojaCompra> findByOpId(Long opId) {
        if (opId == null) return Optional.empty();
        return jpaRepository.findByOrdenProduccion_IdOP(opId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByOpId(Long opId) {
        if (opId == null) return false;
        return jpaRepository.existsByOrdenProduccion_IdOP(opId);
    }

    @Override
    public List<HojaCompra> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<HojaCompra> findAllByEstado(EstadoHC estado) {
        if (estado == null) return List.of();
        return jpaRepository.findAllByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<HojaCompra> findAllByItemIds(List<Long> hcItemIds) {
        if (hcItemIds == null || hcItemIds.isEmpty()) return List.of();
        return jpaRepository.findAllByItemIds(hcItemIds).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
