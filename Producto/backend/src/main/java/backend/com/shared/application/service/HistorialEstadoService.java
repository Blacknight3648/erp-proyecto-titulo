package backend.com.shared.application.service;

import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.domain.model.HistorialEstado;
import backend.com.shared.infrastructure.persistence.repository.HistorialEstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistorialEstadoService {

    private final HistorialEstadoRepository repository;

    @Transactional
    public void registrar(String tipoEntidad, Long entidadId, String estadoAnterior,
            String estadoNuevo, String usuario, String observacion) {
        repository.save(HistorialEstado.registrar(tipoEntidad, entidadId, estadoAnterior,
                estadoNuevo, usuario, observacion));
    }

    public List<HistorialEstadoDTO> consultar(String tipoEntidad, Long entidadId) {
        return repository.findByEntidad(tipoEntidad, entidadId).stream()
                .map(HistorialEstadoDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
