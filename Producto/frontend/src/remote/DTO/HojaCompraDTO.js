import { HojaCompraItemDTO } from './HojaCompraItemDTO';

/**
 * DTO para representar una Hoja de Compra.
 * Espejo de HojaCompraDTO.java del backend.
 */
export class HojaCompraDTO {
    constructor(data = {}) {
        this.idHC = data.idHC ?? null;
        this.numeroHC = data.numeroHC ?? '';
        this.opId = data.opId ?? null;
        this.costeoVersionId = data.costeoVersionId ?? null;
        this.estado = data.estado ?? 'BORRADOR'; // BORRADOR | APROBADA | CERRADA
        this.fechaGeneracion = data.fechaGeneracion ?? null;
        this.observaciones = data.observaciones ?? '';
        this.items = Array.isArray(data.items)
            ? data.items.map(item => new HojaCompraItemDTO(item))
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new HojaCompraDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new HojaCompraDTO(item));
    }

    /** Calculados para la UI */
    get totalItems() {
        return this.items.length;
    }

    get totalUnidades() {
        return this.items.reduce((acc, i) => acc + Number(i.cantidadRequerida || 0), 0);
    }

    get totalEstimado() {
        return this.items.reduce(
            (acc, i) => acc + Number(i.cantidadRequerida || 0) * Number(i.precioUnitarioRef || 0),
            0
        );
    }
}
