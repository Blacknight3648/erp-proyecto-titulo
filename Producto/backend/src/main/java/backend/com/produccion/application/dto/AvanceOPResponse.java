package backend.com.produccion.application.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AvanceOPResponse {
    private Long ordenProduccionId;
    private BigDecimal porcentajeGlobal;
    private String semaforo;
    private Integer totalUnidades;
    private Integer totalProducidas;
    private Integer totalMermas;
    private List<AvanceOT> ordenesTrabajo;

    @Data
    public static class AvanceOT {
        private Long idOT;
        private String fase;
        private String estado;
        private Integer cantidadTotal;
        private Integer cantidadProducida;
        private Integer cantidadMerma;
        private BigDecimal porcentajeAvance;
    }
}
