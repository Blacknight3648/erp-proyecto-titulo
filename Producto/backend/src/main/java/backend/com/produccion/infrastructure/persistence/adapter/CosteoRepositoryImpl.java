package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.infrastructure.persistence.entity.CosteoJpaEntity;
import backend.com.produccion.infrastructure.mapper.CosteoMapper;
import backend.com.produccion.infrastructure.persistence.repository.CosteoJpaRepository;
import backend.com.produccion.infrastructure.persistence.repository.CosteoVersionJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CosteoRepositoryImpl implements CosteoRepository {

    private final CosteoJpaRepository jpaRepository;
    private final CosteoVersionJpaRepository versionJpaRepository;
    private final CosteoMapper mapper;

    @Override
    public Costeo save(Costeo costeo) {
        if (costeo == null)
            throw new ValidationException("El Costeo no puede ser nulo");
        CosteoJpaEntity entity = mapper.toJpaEntity(costeo);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear Costeo a entidad JPA");
        }
        
        // Removed forced update for same scosId to allow multiple cotizaciones
        // Now it relies on the provided idCosteo for updates vs inserts

        CosteoJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public java.util.List<Costeo> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Costeo> findBySolicitudCostosId(Long solicitudCostosId) {
        if (solicitudCostosId == null)
            return Optional.empty();
        // Return only the most recent one if using this legacy method
        return jpaRepository.findAllBySolicitudCostosId(solicitudCostosId).stream()
                .sorted((a, b) -> b.getIdCosteo().compareTo(a.getIdCosteo()))
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public java.util.List<Costeo> findAllBySolicitudCostosId(Long solicitudCostosId) {
        if (solicitudCostosId == null)
            return new java.util.ArrayList<>();
        return jpaRepository.findAllBySolicitudCostosId(solicitudCostosId).stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Costeo> findByNotaVentaId(Long notaVentaId) {
        if (notaVentaId == null) return Optional.empty();
        return jpaRepository.findByNotaVentaId(notaVentaId).map(mapper::toDomain);
    }

    @Override
    public Optional<Costeo> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteBySolicitudCostosId(Long solicitudCostosId) {
        if (solicitudCostosId == null) return;
        List<CosteoJpaEntity> costeos = jpaRepository.findAllBySolicitudCostosId(solicitudCostosId);
        if (costeos.isEmpty()) return;

        // 1. Borrar las versiones (produccion_costeo_versiones) antes que el padre
        //    para evitar FK violation (costeo_id NOT NULL sin cascade en versiones).
        List<Long> costeoIds = costeos.stream()
                .map(CosteoJpaEntity::getIdCosteo)
                .collect(Collectors.toList());
        versionJpaRepository.deleteByCosteo_IdCosteoIn(costeoIds);

        // 2. Borrar cada costeo (activa orphanRemoval en sus items: produccion_costeo_items)
        costeos.forEach(jpaRepository::delete);
    }
}
