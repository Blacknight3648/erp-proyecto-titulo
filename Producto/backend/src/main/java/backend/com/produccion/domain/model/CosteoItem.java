package backend.com.produccion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import backend.com.shared.domain.model.Articulo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CosteoItem {
    private Long idCosteoItem;
    private String tipoInsumo;
    private Long insumoId;
    private String nombreInsumo;
    private BigDecimal consumo;
    private BigDecimal precioUnitario;
    private BigDecimal costoTotal;
    private Articulo articulo;
}
