package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KPIs de tiempos de ciclo por etapa y distribución de OPs por tamaño de lote,
 * para los gráficos operacionales del dashboard de producción.
 */
@Data
@Builder
public class DashboardOPKpisResponse {
    private List<EtapaPromedio> tiemposPorEtapa;
    private List<LotePromedio> promedioPorLote;
    private List<LoteDistribucion> distribucionLote;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EtapaPromedio {
        private String etapa;
        private Double dias;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LotePromedio {
        private String rango;
        private long ops;
        private Double corte;
        private Double logo;
        private Double taller;
        private Double term;
        private Double total;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoteDistribucion {
        private String rango;
        private long valor;
    }
}
