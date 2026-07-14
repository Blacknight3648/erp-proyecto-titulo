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
    
    private BigDecimal cantidadStock;
    private BigDecimal cantidadAComprar;
    private Boolean modificado;
    private String justificacionModificacion;

    private Long proveedorId;
    private String proveedorNombre;
    private Long ocId;
    private String numeroOC;
    /**
     * Si es false, este ítem fue agregado manualmente durante la consolidación
     * de OC (insumo "no presupuestado", no vino del cálculo automático desde
     * el Costeo/OP). Por defecto true para todos los ítems generados desde OP.
     */
    private Boolean presupuestado;

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef) {
        this(idHCItem, hcId, tipoInsumo, articuloId, nombreInsumo, consumoUnitario, cantidadOP, cantidadRequerida,
                precioUnitarioRef, BigDecimal.ZERO, null, false, null, null, null, null, null, true);
    }

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef, Long proveedorId, String proveedorNombre, Long ocId, String numeroOC) {
        this(idHCItem, hcId, tipoInsumo, articuloId, nombreInsumo, consumoUnitario, cantidadOP, cantidadRequerida,
                precioUnitarioRef, BigDecimal.ZERO, null, false, null, proveedorId, proveedorNombre, ocId, numeroOC, true);
    }

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef, BigDecimal cantidadStock, BigDecimal cantidadAComprar,
            Boolean modificado, String justificacionModificacion,
            Long proveedorId, String proveedorNombre, Long ocId, String numeroOC) {
        this(idHCItem, hcId, tipoInsumo, articuloId, nombreInsumo, consumoUnitario, cantidadOP, cantidadRequerida,
                precioUnitarioRef, cantidadStock, cantidadAComprar, modificado, justificacionModificacion,
                proveedorId, proveedorNombre, ocId, numeroOC, true);
    }

    public HojaCompraItem(Long idHCItem, Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal consumoUnitario, Integer cantidadOP, BigDecimal cantidadRequerida,
            BigDecimal precioUnitarioRef, BigDecimal cantidadStock, BigDecimal cantidadAComprar,
            Boolean modificado, String justificacionModificacion,
            Long proveedorId, String proveedorNombre, Long ocId, String numeroOC, Boolean presupuestado) {
        this.idHCItem = idHCItem;
        this.hcId = hcId;
        this.tipoInsumo = tipoInsumo;
        this.articuloId = articuloId;
        this.nombreInsumo = nombreInsumo;
        this.consumoUnitario = consumoUnitario;
        this.cantidadOP = cantidadOP;
        this.cantidadRequerida = cantidadRequerida;
        this.precioUnitarioRef = precioUnitarioRef;
        this.cantidadStock = cantidadStock != null ? cantidadStock : BigDecimal.ZERO;
        this.cantidadAComprar = cantidadAComprar;
        this.modificado = modificado != null ? modificado : false;
        this.justificacionModificacion = justificacionModificacion;
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
        this.ocId = ocId;
        this.numeroOC = numeroOC;
        this.presupuestado = presupuestado != null ? presupuestado : true;
    }

    /**
     * Crea un ítem manual ("no presupuestado"): agregado por Jefatura de
     * Producción durante la consolidación de OC, sin venir del cálculo
     * automático del Costeo/OP.
     */
    public static HojaCompraItem manual(Long hcId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal cantidadRequerida, BigDecimal precioUnitarioRef) {
        return new HojaCompraItem(
                null, hcId, tipoInsumo, articuloId, nombreInsumo,
                null, null, cantidadRequerida, precioUnitarioRef,
                BigDecimal.ZERO, cantidadRequerida, false, null,
                null, null, null, null, false);
    }

    public void asignarProveedor(Long proveedorId, String proveedorNombre) {
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
    }

    public void vincularOC(Long ocId, String numeroOC) {
        this.ocId = ocId;
        this.numeroOC = numeroOC;
    }

    public void asignarStock(BigDecimal stock) {
        this.cantidadStock = stock != null ? stock : BigDecimal.ZERO;
    }

    public void modificar(BigDecimal cantidadAComprar, BigDecimal precioUnitarioRef, String justificacion, BigDecimal cantidadStock) {
        if (cantidadStock != null) {
            this.cantidadStock = cantidadStock;
        }
        boolean difiere = (cantidadAComprar != null && (this.cantidadRequerida == null || cantidadAComprar.compareTo(this.cantidadRequerida) != 0)) ||
                          (precioUnitarioRef != null && (this.precioUnitarioRef == null || precioUnitarioRef.compareTo(this.precioUnitarioRef) != 0)) ||
                          (this.cantidadStock != null && this.cantidadStock.compareTo(BigDecimal.ZERO) > 0);
        
        String justifFinal = justificacion;
        if ((justifFinal == null || justifFinal.trim().isEmpty()) && difiere) {
            justifFinal = "Modificación o ajuste de stock registrado por Jefatura de Producción";
        }
        
        this.cantidadAComprar = cantidadAComprar != null ? cantidadAComprar : this.cantidadRequerida;
        if (precioUnitarioRef != null) {
            this.precioUnitarioRef = precioUnitarioRef;
        }
        if (justifFinal != null && !justifFinal.trim().isEmpty()) {
            this.justificacionModificacion = justifFinal.trim();
        }
        this.modificado = difiere || (this.modificado != null && this.modificado);
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
                BigDecimal.ZERO,
                null,
                false,
                null,
                null,
                null,
                null,
                null);
    }
}
