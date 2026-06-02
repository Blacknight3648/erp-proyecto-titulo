package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
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
