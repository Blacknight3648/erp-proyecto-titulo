package backend.com.shared.application.dto;

import backend.com.shared.domain.model.Notificacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionDTO {
    private Long id;
    private String tipo;
    private String mensaje;
    private String categoria;
    private String prioridad;
    private boolean leida;
    private LocalDateTime fecha;

    public static NotificacionDTO fromDomain(Notificacion n) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.id = n.getId();
        dto.tipo = n.getTipo();
        dto.mensaje = n.getMensaje();
        dto.categoria = n.getCategoria();
        dto.prioridad = n.getPrioridad();
        dto.leida = n.isLeida();
        dto.fecha = n.getFecha();
        return dto;
    }
}
