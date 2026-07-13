import { OrdenCompraItemDTO } from './OrdenCompraItemDTO';

/**
 * DTO para representar una Orden de Compra.
 * Espejo de OrdenCompraDTO.java del backend (modelo nuevo Fase 3).
 *
 * Estados: EMITIDA | ENVIADA | RECEPCIONADA_PARCIAL | RECEPCIONADA | CERRADA | RECHAZADA
 */
export class OrdenCompraDTO {
    constructor(data = {}) {
        this.idOC = data.idOC ?? null;
        this.numeroOC = data.numeroOC ?? '';
        this.proveedorId = data.proveedorId ?? null;
        this.estado = data.estado ?? 'EMITIDA';
        this.fechaEmision = data.fechaEmision ?? null;
        this.fechaEntregaEstimada = data.fechaEntregaEstimada ?? null;
        this.observaciones = data.observaciones ?? '';
        this.totalNeto = data.totalNeto ?? 0;
        this.items = Array.isArray(data.items)
            ? data.items.map(item => new OrdenCompraItemDTO(item))
            : [];
        this.motivoRechazo = data.motivoRechazo ?? null;
        this.fechaRechazo = data.fechaRechazo ?? null;
        this.version = data.version ?? 1;
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new OrdenCompraDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new OrdenCompraDTO(item));
    }

    get totalItems() {
        return this.items.length;
    }
}
