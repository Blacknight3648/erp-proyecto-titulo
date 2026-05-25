/**
 * DTO de la tabla puente HCItem ↔ OCItem (asignación N:M).
 * Espejo de HCItemOCItemLinkDTO.java del backend.
 */
export class HCItemOCItemLinkDTO {
    constructor(data = {}) {
        this.hcItemId = data.hcItemId ?? null;
        this.ocItemId = data.ocItemId ?? null;
        this.cantidadAsignada = data.cantidadAsignada ?? 0;
    }
}
