/**
 * DTO para representar un ítem de Hoja de Compra.
 * Espejo de HojaCompraItemDTO.java del backend.
 */
export class HojaCompraItemDTO {
    constructor(data = {}) {
        this.idHCItem = data.idHCItem ?? null;
        this.hcId = data.hcId ?? null;
        this.tipoInsumo = data.tipoInsumo ?? '';
        this.insumoId = data.insumoId ?? null;
        this.nombreInsumo = data.nombreInsumo ?? '';
        this.consumoUnitario = data.consumoUnitario ?? 0;
        this.cantidadOP = data.cantidadOP ?? 0;
        this.cantidadRequerida = data.cantidadRequerida ?? 0;
        this.precioUnitarioRef = data.precioUnitarioRef ?? 0;
    }
}
