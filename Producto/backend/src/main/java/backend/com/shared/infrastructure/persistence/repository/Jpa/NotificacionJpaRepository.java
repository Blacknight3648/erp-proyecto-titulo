package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.NotificacionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionJpaRepository extends JpaRepository<NotificacionJpaEntity, Long> {
    List<NotificacionJpaEntity> findAllByOrderByFechaDesc();

    long countByLeidaFalse();
}
