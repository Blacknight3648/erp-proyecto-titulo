import { HCItemOCItemLinkDTO } from './HCItemOCItemLinkDTO';

/**
 * DTO para representar un ítem de Orden de Compra.
 * Espejo de OrdenCompraItemDTO.java del backend.
 */
export class OrdenCompraItemDTO {
    constructor(data = {}) {
        this.idOCItem = data.idOCItem ?? null;
        this.ocId = data.ocId ?? null;
        this.tipoInsumo = data.tipoInsumo ?? '';
        this.insumoId = data.insumoId ?? null;
        this.nombreInsumo = data.nombreInsumo ?? '';
        this.cantidadRequerida = data.cantidadRequerida ?? 0;
        this.cantidadStock = data.cantidadStock ?? 0;
        this.cantidadComprada = data.cantidadComprada ?? 0;
        this.precioUnitario = data.precioUnitario ?? 0;
        this.subtotal = data.subtotal ?? 0;
        this.hcLinks = Array.isArray(data.hcLinks)
            ? data.hcLinks.map(l => new HCItemOCItemLinkDTO(l))
            : [];
    }
}
