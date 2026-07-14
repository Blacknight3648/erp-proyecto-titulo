package backend.com.produccion.application.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ActualizarSeguimientoCommand {
    private LocalDate fechaRecepcionOp;
    private LocalDate finTizado;
    private String estadoOcMp;
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
}
