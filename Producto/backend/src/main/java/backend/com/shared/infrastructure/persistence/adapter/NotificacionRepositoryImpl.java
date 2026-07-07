package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Notificacion;
import backend.com.shared.infrastructure.mapper.NotificacionMapper;
import backend.com.shared.infrastructure.persistence.entity.NotificacionJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.NotificacionRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.NotificacionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificacionRepositoryImpl implements NotificacionRepository {

    private final NotificacionJpaRepository jpaRepository;
    private final NotificacionMapper mapper;

    @Override
    public Notificacion save(Notificacion notificacion) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpaEntity(notificacion)));
    }

    @Override
    public List<Notificacion> listarTodas() {
        return jpaRepository.findAllByOrderByFechaDesc().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long contarNoLeidas() {
        return jpaRepository.countByLeidaFalse();
    }

    @Override
    public Optional<Notificacion> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void marcarTodasLeidas() {
        List<NotificacionJpaEntity> pendientes = jpaRepository.findAllByOrderByFechaDesc().stream()
                .filter(e -> !e.isLeida())
                .peek(e -> e.setLeida(true))
                .collect(Collectors.toList());
        jpaRepository.saveAll(pendientes);
    }
}
