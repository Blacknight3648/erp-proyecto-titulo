package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.HistorialEstado;
import backend.com.shared.infrastructure.persistence.repository.HistorialEstadoRepository;
import backend.com.shared.infrastructure.mapper.HistorialEstadoMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.HistorialEstadoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HistorialEstadoRepositoryImpl implements HistorialEstadoRepository {

    private final HistorialEstadoJpaRepository jpaRepository;
    private final HistorialEstadoMapper mapper;

    @Override
    public HistorialEstado save(HistorialEstado historial) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpaEntity(historial)));
    }

    @Override
    public List<HistorialEstado> findByEntidad(String tipoEntidad, Long entidadId) {
        return jpaRepository.findByTipoEntidadAndEntidadIdOrderByFechaAsc(tipoEntidad, entidadId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
