package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.produccion.infrastructure.mapper.CosteoVersionMapper;
import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.CosteoVersionJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CosteoVersionRepositoryImpl implements CosteoVersionRepository {

    private final CosteoVersionJpaRepository jpaRepository;
    private final CosteoVersionMapper mapper;

    @Override
    public CosteoVersion save(CosteoVersion costeoVersion) {
        if (costeoVersion == null) {
            throw new ValidationException("La CosteoVersion no puede ser nula");
        }
        CosteoVersionJpaEntity entity = mapper.toJpaEntity(costeoVersion);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear CosteoVersion a entidad JPA");
        }
        CosteoVersionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CosteoVersion> findById(Long idCosteoVersion) {
        if (idCosteoVersion == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(idCosteoVersion).map(mapper::toDomain);
    }

    @Override
    public Optional<CosteoVersion> findUltimaPorCosteoId(Long costeoId) {
        if (costeoId == null) {
            return Optional.empty();
        }
        return jpaRepository.findTopByCosteo_IdCosteoOrderByNumeroVersionDesc(costeoId)
                .map(mapper::toDomain);
    }

    @Override
    public List<CosteoVersion> findAllByCosteoId(Long costeoId) {
        if (costeoId == null) {
            return List.of();
        }
        return jpaRepository.findByCosteo_IdCosteoOrderByNumeroVersionAsc(costeoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Integer siguienteNumeroVersion(Long costeoId) {
        if (costeoId == null) {
            return 1;
        }
        return jpaRepository.findTopByCosteo_IdCosteoOrderByNumeroVersionDesc(costeoId)
                .map(v -> v.getNumeroVersion() + 1)
                .orElse(1);
    }
}
