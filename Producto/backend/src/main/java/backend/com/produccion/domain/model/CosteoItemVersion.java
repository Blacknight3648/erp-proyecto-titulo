package backend.com.produccion.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import backend.com.shared.domain.model.Articulo;

@Getter
@Setter
public class CosteoItemVersion {
    private Long idCosteoItemVersion;
    private Long costeoVersionId;
    private Long costeoItemId;
    private String tipoInsumo;
    private Long insumoId;
    private String nombreInsumo;
    private BigDecimal consumo;
    private BigDecimal precioUnitario;
    private BigDecimal costoTotal;
    private Articulo articulo;

    public CosteoItemVersion(Long idCosteoItemVersion, Long costeoVersionId, Long costeoItemId, String tipoInsumo,
            Long insumoId, String nombreInsumo, BigDecimal consumo, BigDecimal precioUnitario, BigDecimal costoTotal) {
        this.idCosteoItemVersion = idCosteoItemVersion;
        this.costeoVersionId = costeoVersionId;
        this.costeoItemId = costeoItemId;
        this.tipoInsumo = tipoInsumo;
        this.insumoId = insumoId;
        this.nombreInsumo = nombreInsumo;
        this.consumo = consumo;
        this.precioUnitario = precioUnitario;
        this.costoTotal = costoTotal;
    }

}
