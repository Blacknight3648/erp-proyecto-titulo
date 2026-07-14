package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Notificacion;

import java.util.List;
import java.util.Optional;

public interface NotificacionRepository {
    Notificacion save(Notificacion notificacion);

    List<Notificacion> listarTodas();

    long contarNoLeidas();

    Optional<Notificacion> findById(Long id);

    void marcarTodasLeidas();
}
