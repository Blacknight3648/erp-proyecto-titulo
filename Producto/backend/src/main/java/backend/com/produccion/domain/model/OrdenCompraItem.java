package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class OrdenCompraItem {

    private Long idOCItem;
    private Long ocId;
    private String tipoInsumo;
    private Integer articuloId; // referencia blanda al Articulo; el registro conserva nombreInsumo congelado
    private String nombreInsumo;
    private BigDecimal cantidadRequerida;   // demanda original (suma de HCItems asignados)
    private BigDecimal cantidadStock;        // disponible en bodega (hoy = 0)
    private BigDecimal cantidadComprada;     // = cantidadRequerida - cantidadStock
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;             // cantidadComprada * precioUnitario
    private List<HCItemOCItemLink> hcLinks = new ArrayList<>();

    public OrdenCompraItem(Long idOCItem, Long ocId, String tipoInsumo, Integer articuloId, String nombreInsumo,
            BigDecimal cantidadRequerida, BigDecimal cantidadStock, BigDecimal cantidadComprada,
            BigDecimal precioUnitario, BigDecimal subtotal, List<HCItemOCItemLink> hcLinks) {
        this.idOCItem = idOCItem;
        this.ocId = ocId;
        this.tipoInsumo = tipoInsumo;
        this.articuloId = articuloId;
        this.nombreInsumo = nombreInsumo;
        this.cantidadRequerida = cantidadRequerida != null ? cantidadRequerida : BigDecimal.ZERO;
        this.cantidadStock = cantidadStock != null ? cantidadStock : BigDecimal.ZERO;
        this.cantidadComprada = cantidadComprada != null ? cantidadComprada : this.cantidadRequerida;
        this.precioUnitario = precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
        this.subtotal = subtotal != null ? subtotal : this.cantidadComprada.multiply(this.precioUnitario);
        if (hcLinks != null) {
            this.hcLinks.addAll(hcLinks);
        }
    }

    public void addLink(HCItemOCItemLink link) {
        this.hcLinks.add(link);
    }

    public List<HCItemOCItemLink> getHcLinks() {
        return Collections.unmodifiableList(hcLinks);
    }
}
