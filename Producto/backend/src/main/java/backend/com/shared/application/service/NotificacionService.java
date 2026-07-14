package backend.com.shared.application.service;

import backend.com.shared.application.dto.NotificacionDTO;
import backend.com.shared.domain.model.Notificacion;
import backend.com.shared.infrastructure.persistence.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificacionService {

    private final NotificacionRepository repository;

    /** Crea una notificación de sistema. Se llama desde los servicios de dominio
     *  cuando ocurre un evento real relevante (OC generada, HC aprobada, etc.). */
    @Transactional
    public void crear(String tipo, String mensaje, String categoria, String prioridad) {
        repository.save(Notificacion.crear(tipo, mensaje, categoria, prioridad));
    }

    public List<NotificacionDTO> listar() {
        return repository.listarTodas().stream()
                .map(NotificacionDTO::fromDomain)
                .collect(Collectors.toList());
    }

    public long contarNoLeidas() {
        return repository.contarNoLeidas();
    }

    @Transactional
    public void marcarLeida(Long id) {
        repository.findById(id).ifPresent(n -> {
            n.marcarLeida();
            repository.save(n);
        });
    }

    @Transactional
    public void marcarTodasLeidas() {
        repository.marcarTodasLeidas();
    }
}
