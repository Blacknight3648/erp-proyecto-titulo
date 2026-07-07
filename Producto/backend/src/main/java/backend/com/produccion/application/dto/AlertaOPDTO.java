package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Una alerta de atraso vigente para una OP puntual (una OP puede tener varias
 * a la vez — ver {@code DashboardOPService.evaluarAlertas}). Refleja las mismas
 * reglas SLA que {@code DashboardOPService.calcular()} usa para los conteos
 * agregados del dashboard, pero evaluadas por OP individual.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertaOPDTO {
    private String etapaAtrasada;
    private Long diasAtraso;
    private String motivo;
}
