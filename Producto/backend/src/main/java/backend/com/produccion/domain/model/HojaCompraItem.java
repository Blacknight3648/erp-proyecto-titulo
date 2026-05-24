package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class HojaCompraItem {

    private Long idHCItem;
    private Long hcId;
    private String tipoInsumo;
    private Long insumoId;
    private String nombreInsumo;
    private BigDecimal consumoUnitario;
    private Integer cantidadOP;
    private BigDecimal cantidadRequerida;
    private BigDecimal precioUnitarioRef;

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Long insumoId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef) {
        this.idHCItem = idHCItem;
        this.hcId = hcId;
        this.tipoInsumo = tipoInsumo;
        this.insumoId = insumoId;
        this.nombreInsumo = nombreInsumo;
        this.consumoUnitario = consumoUnitario;
        this.cantidadOP = cantidadOP;
        this.cantidadRequerida = cantidadRequerida;
        this.precioUnitarioRef = precioUnitarioRef;
    }

    /**
     * Genera un HCItem a partir de un CosteoItemVersion (snapshot del costeo)
     * multiplicado por la cantidad total a producir en la OP.
     */
    public static HojaCompraItem desdeCosteoVersionItem(CosteoItemVersion origen, Integer cantidadOP) {
        BigDecimal cantidadOPbd = BigDecimal.valueOf(cantidadOP != null ? cantidadOP : 0);
        BigDecimal cantidadRequerida = origen.getConsumo() != null
                ? origen.getConsumo().multiply(cantidadOPbd)
                : BigDecimal.ZERO;
        return new HojaCompraItem(
                null,
                null,
                origen.getTipoInsumo(),
                origen.getInsumoId(),
                origen.getNombreInsumo(),
                origen.getConsumo(),
                cantidadOP,
                cantidadRequerida,
                origen.getPrecioUnitario());
    }
}
