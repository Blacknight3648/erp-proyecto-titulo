package backend.com.produccion.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Asignación N:M entre un HCItem y un OCItem.
 * Permite que un HCItem se compre en varias OCs (split por proveedor)
 * o que una OC consolide ítems de múltiples HCs.
 */
@Getter
public class HCItemOCItemLink {

    private Long hcItemId;
    private Long ocItemId;
    private BigDecimal cantidadAsignada;

    public HCItemOCItemLink(Long hcItemId, Long ocItemId, BigDecimal cantidadAsignada) {
        this.hcItemId = hcItemId;
        this.ocItemId = ocItemId;
        this.cantidadAsignada = cantidadAsignada;
    }
}
