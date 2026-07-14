package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.domain.repository.SeguimientoOPRepository;
import backend.com.produccion.infrastructure.mapper.SeguimientoOPMapper;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.SeguimientoOPJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeguimientoOPRepositoryImpl implements SeguimientoOPRepository {

    private final SeguimientoOPJpaRepository jpaRepository;
    private final SeguimientoOPMapper mapper;
    private final EntityManager em;

    @Override
    public Optional<SeguimientoOP> findByOrdenProduccionId(Long opId) {
        return jpaRepository.findByOrdenProduccion_IdOP(opId)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public SeguimientoOP save(SeguimientoOP seguimiento) {
        OrdenProduccionJpaEntity opEntity = em.getReference(OrdenProduccionJpaEntity.class, seguimiento.getOrdenProduccionId());
        SeguimientoOPJpaEntity entity = mapper.toJpaEntity(seguimiento, opEntity);
        return mapper.toDomain(jpaRepository.save(entity));
    }
}
