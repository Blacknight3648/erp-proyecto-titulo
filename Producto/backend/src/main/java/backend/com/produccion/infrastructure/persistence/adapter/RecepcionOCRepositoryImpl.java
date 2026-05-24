package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.RecepcionOC;
import backend.com.produccion.domain.repository.RecepcionOCRepository;
import backend.com.produccion.infrastructure.mapper.RecepcionOCMapper;
import backend.com.produccion.infrastructure.persistence.entity.RecepcionOCJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.RecepcionOCJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecepcionOCRepositoryImpl implements RecepcionOCRepository {

    private final RecepcionOCJpaRepository jpaRepository;
    private final RecepcionOCMapper mapper;

    @Override
    public RecepcionOC save(RecepcionOC recepcionOC) {
        if (recepcionOC == null) {
            throw new ValidationException("La Recepción de OC no puede ser nula");
        }
        RecepcionOCJpaEntity entity = mapper.toJpaEntity(recepcionOC);
        RecepcionOCJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RecepcionOC> findById(Long idRecepcion) {
        if (idRecepcion == null) return Optional.empty();
        return jpaRepository.findById(idRecepcion).map(mapper::toDomain);
    }

    @Override
    public List<RecepcionOC> findAllByOcId(Long ocId) {
        if (ocId == null) return List.of();
        return jpaRepository.findAllByOrdenCompra_IdOC(ocId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, BigDecimal> sumarRecibidoPorOCItem(Long ocId) {
        if (ocId == null) return Map.of();
        Map<Long, BigDecimal> resultado = new HashMap<>();
        for (Object[] fila : jpaRepository.sumarRecibidoPorOCItem(ocId)) {
            Long ocItemId = (Long) fila[0];
            BigDecimal total = fila[1] != null ? (BigDecimal) fila[1] : BigDecimal.ZERO;
            resultado.put(ocItemId, total);
        }
        return resultado;
    }
}
