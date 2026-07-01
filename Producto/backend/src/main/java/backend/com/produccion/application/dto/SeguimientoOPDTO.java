package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.SeguimientoOP;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SeguimientoOPDTO {
    private Long ordenProduccionId;
    private LocalDate fechaRecepcionOp;
    private LocalDate finTizado;
    private LocalDate fechaEstadoOcMp;
    private LocalDate recepcionCompras;
    private LocalDate inicioCorte;
    private LocalDate finCorte;
    private LocalDate inicioLogo;
    private String estadoIdaLogo;
    private LocalDate regresoLogo;
    private String estadoRecLogo;
    private LocalDate inicioTallerExterno;
    private LocalDate finTallerExterno;
    private String calidadTaller;
    private String obsTaller;
    private LocalDate finTerminacion;
    private LocalDate finPersonalizado;
    private int porcentajeAvance;

    public static SeguimientoOPDTO from(SeguimientoOP seg) {
        SeguimientoOPDTO dto = new SeguimientoOPDTO();
        dto.setOrdenProduccionId(seg.getOrdenProduccionId());
        dto.setFechaRecepcionOp(seg.getFechaRecepcionOp());
        dto.setFinTizado(seg.getFinTizado());
        dto.setFechaEstadoOcMp(seg.getFechaEstadoOcMp());
        dto.setRecepcionCompras(seg.getRecepcionCompras());
        dto.setInicioCorte(seg.getInicioCorte());
        dto.setFinCorte(seg.getFinCorte());
        dto.setInicioLogo(seg.getInicioLogo());
        dto.setEstadoIdaLogo(seg.getEstadoIdaLogo() != null ? seg.getEstadoIdaLogo().name() : null);
        dto.setRegresoLogo(seg.getRegresoLogo());
        dto.setEstadoRecLogo(seg.getEstadoRecLogo() != null ? seg.getEstadoRecLogo().name() : null);
        dto.setInicioTallerExterno(seg.getInicioTallerExterno());
        dto.setFinTallerExterno(seg.getFinTallerExterno());
        dto.setCalidadTaller(seg.getCalidadTaller() != null ? seg.getCalidadTaller().name() : null);
        dto.setObsTaller(seg.getObsTaller());
        dto.setFinTerminacion(seg.getFinTerminacion());
        dto.setFinPersonalizado(seg.getFinPersonalizado());
        dto.setPorcentajeAvance(seg.getPorcentajeAvance());
        return dto;
    }
}
