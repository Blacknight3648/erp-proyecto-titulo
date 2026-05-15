package backend.com.shared.application.dto;

import backend.com.shared.domain.model.HistorialEstado;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialEstadoDTO {
    private Long id;
    private String tipoEntidad;
    private Long entidadId;
    private String estadoAnterior;
    private String estadoNuevo;
    private String usuario;
    private String observacion;
    private LocalDateTime fecha;

    public static HistorialEstadoDTO fromDomain(HistorialEstado h) {
        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.id = h.getId();
        dto.tipoEntidad = h.getTipoEntidad();
        dto.entidadId = h.getEntidadId();
        dto.estadoAnterior = h.getEstadoAnterior();
        dto.estadoNuevo = h.getEstadoNuevo();
        dto.usuario = h.getUsuario();
        dto.observacion = h.getObservacion();
        dto.fecha = h.getFecha();
        return dto;
    }
}
