package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialVersionCosteoDTO {
    private String id; // Puede ser UUID o numérico
    private Integer version;
    private String accion; // CREACION, MODIFICACION, APROBACION, RECHAZO
    private String fechaFormateada;
    private BigDecimal costoTotal;
    private BigDecimal costoAnterior;
    private String usuario;
    private String motivo;
}
