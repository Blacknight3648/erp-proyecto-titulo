package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class HojaCompraItem {

    private Long idHCItem;
    private Long hcId;
    private String tipoInsumo;
    private Integer articuloId; // referencia blanda al Articulo; el registro conserva nombreInsumo congelado
    private String nombreInsumo;
    private BigDecimal consumoUnitario;
    private Integer cantidadOP;
    private BigDecimal cantidadRequerida;
    private BigDecimal precioUnitarioRef;
    
    private Long proveedorId;
    private String proveedorNombre;
    private Long ocId;
    private String numeroOC;

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef) {
        this(idHCItem, hcId, tipoInsumo, articuloId, nombreInsumo, consumoUnitario, cantidadOP, cantidadRequerida,
                precioUnitarioRef, null, null, null, null);
    }

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef, Long proveedorId, String proveedorNombre, Long ocId, String numeroOC) {
        this.idHCItem = idHCItem;
        this.hcId = hcId;
        this.tipoInsumo = tipoInsumo;
        this.articuloId = articuloId;
        this.nombreInsumo = nombreInsumo;
        this.consumoUnitario = consumoUnitario;
        this.cantidadOP = cantidadOP;
        this.cantidadRequerida = cantidadRequerida;
        this.precioUnitarioRef = precioUnitarioRef;
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
        this.ocId = ocId;
        this.numeroOC = numeroOC;
    }

    public void asignarProveedor(Long proveedorId, String proveedorNombre) {
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
    }

    public void vincularOC(Long ocId, String numeroOC) {
        this.ocId = ocId;
        this.numeroOC = numeroOC;
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
                origen.getArticuloId(),
                origen.getNombreInsumo(),
                origen.getConsumo(),
                cantidadOP,
                cantidadRequerida,
                origen.getPrecioUnitario(),
                null,
                null,
                null,
                null);
    }
}
