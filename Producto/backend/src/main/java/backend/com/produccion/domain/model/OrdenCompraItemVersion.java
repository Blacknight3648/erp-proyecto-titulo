package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Snapshot congelado de un OrdenCompraItem al momento de rechazarse la OC.
 * Análogo a {@link CosteoItemVersion}.
 */
@Getter
public class OrdenCompraItemVersion {
    private Long idOCItemVersion;
    private Long ocVersionId;
    private Long ocItemId;
    private String tipoInsumo;
    private Integer articuloId; // referencia blanda al Articulo; el snapshot conserva nombreInsumo congelado
    private String nombreInsumo;
    private BigDecimal cantidadComprada;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public OrdenCompraItemVersion(Long idOCItemVersion, Long ocVersionId, Long ocItemId, String tipoInsumo,
            Integer articuloId, String nombreInsumo, BigDecimal cantidadComprada, BigDecimal precioUnitario,
            BigDecimal subtotal) {
        this.idOCItemVersion = idOCItemVersion;
        this.ocVersionId = ocVersionId;
        this.ocItemId = ocItemId;
        this.tipoInsumo = tipoInsumo;
        this.articuloId = articuloId;
        this.nombreInsumo = nombreInsumo;
        this.cantidadComprada = cantidadComprada;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
}
