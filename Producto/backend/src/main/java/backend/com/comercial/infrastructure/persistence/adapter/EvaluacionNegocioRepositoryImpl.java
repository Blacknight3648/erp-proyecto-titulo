package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioJpaEntity;
import backend.com.comercial.infrastructure.mapper.EvaluacionNegocioMapper;
import backend.com.comercial.infrastructure.persistence.repository.EvaluacionNegocioJpaRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EvaluacionNegocioRepositoryImpl implements EvaluacionNegocioRepository {

    private final EvaluacionNegocioJpaRepository jpaRepository;
    private final EvaluacionNegocioMapper mapper;

    @Override
    public EvaluacionNegocio save(EvaluacionNegocio evaluacionNegocio) {
        EvaluacionNegocioJpaEntity entity;
        Long id = evaluacionNegocio.getEvaluacionNegocioId();
        if (id != null) {
            entity = jpaRepository.findById(id.longValue())
                    .orElseGet(() -> mapper.toEntity(evaluacionNegocio));
            if (entity.getIdEVN() != null) {
                mapper.updateEntityFromDomain(evaluacionNegocio, entity);
            }
        } else {
            entity = mapper.toEntity(evaluacionNegocio);
        }

        if (entity == null) {
            throw new IllegalArgumentException("La entidad de Evaluación de Negocio no puede ser nula");
        }
        EvaluacionNegocioJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<EvaluacionNegocio> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<EvaluacionNegocio> findByNumero(DocumentNumber numero) {
        // This would require a custom query in JpaRepository if we want to be strict,
        // but for now we can use findByNumero if added to JpaRepository or just findAny
        // in a stream.
        // Let's assume JpaRepository has findByNumero.
        return jpaRepository.findAll().stream()
                .filter(e -> e.getNumero().equals(numero.getValue()))
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public java.util.List<EvaluacionNegocio> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<Long> findLinkedCosteoIds() {
        return jpaRepository.findLinkedCosteoIds();
    }
}
