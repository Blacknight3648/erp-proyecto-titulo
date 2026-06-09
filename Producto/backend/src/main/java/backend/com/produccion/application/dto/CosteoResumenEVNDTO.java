package backend.com.produccion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resumen de un costeo pensado para auto-rellenar un ítem tipo OP de una EVN.
 * El frontend llama a este endpoint al vincular un costeo y completa los campos
 * descriptivos del ítem (modelo, tela, composición, género).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosteoResumenEVNDTO {
    private Long idCosteo;
    private String numeroCosteo;
    private Long solicitudCostosId;
    private String genero;       // desde la SolicitudCostos
    private String modelo;       // nombrePrenda de la SolicitudCostos
    private String tela;         // nombreArticulo del ítem tipo TELA del costeo
    private String composicion;  // composición del ArticuloTela vinculado
}
