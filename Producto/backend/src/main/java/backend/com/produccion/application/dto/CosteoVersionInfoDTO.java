package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Vista compacta de una versión de Costeo para mostrar en trazabilidad.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CosteoVersionInfoDTO {

    private Long idCosteoVersion;
    private Long costeoId;
    private Integer numeroVersion;
    private LocalDateTime fechaCreacion;
    private String motivoCambio;
    private String usuarioCreador;
    private String motivoRechazoCosteo;
    private LocalDateTime fechaRechazoCosteo;
}
