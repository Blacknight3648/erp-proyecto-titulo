import { ItemSCDTO } from './ItemSCDTO';

/**
 * DTO para representar una Solicitud de Compra.
 * Basado en SolicitudCompra.java
 */
export class SolicitudCompraDTO {
    constructor(data = {}) {
        this.idSC = data.id || data.idSC || null;
        this.numeroSC = data.numero || (data.numeroSC ? data.numeroSC.value : '');
        this.tenantId = data.tenantId || null;
        this.notaVentaId = data.notaVentaId || null;
        this.estado = data.estado || 'PENDIENTE';
        this.fechaEmision = data.fechaEmision || null;

        this.items = Array.isArray(data.items)
            ? data.items.map(item => new ItemSCDTO(item))
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new SolicitudCompraDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new SolicitudCompraDTO(item));
    }
}
