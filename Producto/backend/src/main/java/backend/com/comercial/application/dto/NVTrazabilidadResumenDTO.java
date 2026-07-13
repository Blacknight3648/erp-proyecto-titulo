package backend.com.comercial.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class NVTrazabilidadResumenDTO {
    private Long idNV;
    private String numeroNV;
    private Long clienteId;
    private String clienteNombre;
    private String estado;
    private LocalDate fechaEmision;
    private String motivoRechazo;
    private LocalDateTime fechaRechazo;

    private Long evaluacionNegocioId;
    private String numeroEVN;
    private String estadoEVN;
    private String motivoRechazoEVN;
    private LocalDateTime fechaRechazoEVN;

    private List<OPResumenDTO> ordenesProduccion;

    @Data
    @Builder
    public static class OPResumenDTO {
        private Long idOP;
        private String numeroOP;
        private String estado;
        private LocalDate fechaEntregaProgramada;
        private HCResumenDTO hojaCompra;
        private List<OCResumenDTO> ordenesCompra;
    }

    @Data
    @Builder
    public static class HCResumenDTO {
        private Long idHC;
        private String numeroHC;
        private String estado;
    }

    @Data
    @Builder
    public static class OCResumenDTO {
        private Long idOC;
        private String numeroOC;
        private String estado;
        private String motivoRechazo;
        private LocalDateTime fechaRechazo;
    }
}
