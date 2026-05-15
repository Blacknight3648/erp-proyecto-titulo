package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.RegistroAvance;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistroAvanceDTO {
    private Long id;
    private Long ordenTrabajoId;
    private Integer cantidadProducida;
    private Integer cantidadMerma;
    private String motivoMerma;
    private String usuario;
    private String observacion;
    private LocalDateTime fecha;

    public static RegistroAvanceDTO fromDomain(RegistroAvance r) {
        RegistroAvanceDTO dto = new RegistroAvanceDTO();
        dto.id = r.getId();
        dto.ordenTrabajoId = r.getOrdenTrabajoId();
        dto.cantidadProducida = r.getCantidadProducida();
        dto.cantidadMerma = r.getCantidadMerma();
        dto.motivoMerma = r.getMotivoMerma();
        dto.usuario = r.getUsuario();
        dto.observacion = r.getObservacion();
        dto.fecha = r.getFecha();
        return dto;
    }
}
