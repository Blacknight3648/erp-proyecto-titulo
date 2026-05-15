package backend.com.produccion.infrastructure.persistence.adapter;

import backend.com.produccion.domain.model.RegistroAvance;
import backend.com.produccion.domain.repository.RegistroAvanceRepository;
import backend.com.produccion.infrastructure.mapper.RegistroAvanceMapper;
import backend.com.produccion.infrastructure.persistence.repository.RegistroAvanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegistroAvanceRepositoryImpl implements RegistroAvanceRepository {

    private final RegistroAvanceJpaRepository jpaRepository;
    private final RegistroAvanceMapper mapper;

    @Override
    public RegistroAvance save(RegistroAvance registro) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpaEntity(registro)));
    }

    @Override
    public List<RegistroAvance> findByOrdenTrabajoId(Long ordenTrabajoId) {
        if (ordenTrabajoId == null) return List.of();
        return jpaRepository.findByOrdenTrabajoIdOrderByFechaAsc(ordenTrabajoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
