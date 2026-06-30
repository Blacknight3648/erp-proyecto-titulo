package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.CalidadTaller;
import backend.com.produccion.domain.enums.EstadoIdaLogo;
import backend.com.produccion.domain.enums.EstadoRecLogo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SeguimientoOP {
    private Long idSeguimiento;
    private Long ordenProduccionId;
    
    private LocalDate fechaRecepcionOp;
    private LocalDate finTizado;
    private LocalDate fechaEstadoOcMp;
    private LocalDate recepcionCompras;
    private LocalDate inicioCorte;
    private LocalDate finCorte;
    private LocalDate inicioLogo;
    private EstadoIdaLogo estadoIdaLogo;
    private LocalDate regresoLogo;
    private EstadoRecLogo estadoRecLogo;
    private LocalDate inicioTallerExterno;
    private LocalDate finTallerExterno;
    private CalidadTaller calidadTaller;
    private String obsTaller;
    private LocalDate finTerminacion;
    private LocalDate finPersonalizado;

    public SeguimientoOP() {
    }

    public SeguimientoOP(Long ordenProduccionId) {
        this.ordenProduccionId = ordenProduccionId;
    }
}
