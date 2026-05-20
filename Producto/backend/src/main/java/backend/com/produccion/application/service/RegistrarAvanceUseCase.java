package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.RegistrarAvanceCommand;
import backend.com.produccion.application.dto.RegistroAvanceDTO;
import backend.com.produccion.domain.model.EstadoOT;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.model.RegistroAvance;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.produccion.domain.repository.RegistroAvanceRepository;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrarAvanceUseCase {

    private final OrdenTrabajoRepository otRepository;
    private final RegistroAvanceRepository avanceRepository;
    private final HistorialEstadoService historialService;

    @Transactional
    public RegistroAvanceDTO registrar(Long otId, RegistrarAvanceCommand cmd) {
        if (cmd == null) {
            throw new ValidationException("Comando de avance requerido");
        }
        int producida = cmd.getCantidadProducida() != null ? cmd.getCantidadProducida() : 0;
        int merma = cmd.getCantidadMerma() != null ? cmd.getCantidadMerma() : 0;
        if (producida == 0 && merma == 0) {
            throw new ValidationException("Debe registrar al menos una unidad producida o de merma");
        }
        if (merma > 0 && (cmd.getMotivoMerma() == null || cmd.getMotivoMerma().isBlank())) {
            throw new ValidationException("Las mermas requieren un motivo");
        }
        if (cmd.getUsuario() == null || cmd.getUsuario().isBlank()) {
            throw new ValidationException("Se requiere identificar al usuario que registra el avance");
        }

        OrdenTrabajo ot = otRepository.findById(otId)
                .orElseThrow(() -> new EntityNotFoundException("OT no encontrada: " + otId));

        if (ot.getCantidadTotal() != null && ot.getCantidadTotal() > 0) {
            int futuro = ot.getCantidadProducida() + ot.getCantidadMerma() + producida + merma;
            if (futuro > ot.getCantidadTotal()) {
                throw new ValidationException(
                        "El avance excede la cantidad total de la OT (" + ot.getCantidadTotal() + ")");
            }
        }

        EstadoOT estadoAnterior = ot.getEstadoOT();
        ot.registrarAvance(producida, merma);
        OrdenTrabajo otGuardada = otRepository.save(ot);

        RegistroAvance registro = avanceRepository.save(RegistroAvance.crear(
                otId, producida, merma, cmd.getMotivoMerma(), cmd.getUsuario(), cmd.getObservacion()));

        if (estadoAnterior != otGuardada.getEstadoOT()) {
            historialService.registrar("OT", otGuardada.getIdOT(),
                    estadoAnterior.name(), otGuardada.getEstadoOT().name(),
                    cmd.getUsuario(),
                    "Cambio de estado por registro de avance");
        }

        return RegistroAvanceDTO.fromDomain(registro);
    }

    public List<RegistroAvanceDTO> listarPorOT(Long otId) {
        return avanceRepository.findByOrdenTrabajoId(otId).stream()
                .map(RegistroAvanceDTO::fromDomain)
                .collect(Collectors.toList());
    }
}
