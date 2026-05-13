import { ItemOCDTO } from './ItemOCDTO';

/**
 * DTO para representar una Orden de Compra.
 * Basado en OrdenCompra.java
 */
export class OrdenCompraDTO {
    constructor(data = {}) {
        this.idOC = data.idOC || null;
        this.numeroOC = data.numeroOC ? data.numeroOC.value : '';
        this.solicitudCompraId = data.solicitudCompraId || null;
        this.ordenProduccionId = data.ordenProduccionId || null;
        this.proveedorId = data.proveedorId || null;
        this.tipo = data.tipo || 'NACIONAL';
        this.estado = data.estado || 'EMITIDA';
        this.montoTotal = data.montoTotal ? data.montoTotal.amount : 0;
        this.currency = data.montoTotal ? data.montoTotal.currency : 'CLP';
        this.fechaEmision = data.fechaEmision || null;
        this.fechaRecepcion = data.fechaRecepcion || null;
        
        this.items = Array.isArray(data.items) 
            ? data.items.map(item => new ItemOCDTO(item)) 
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new OrdenCompraDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new OrdenCompraDTO(item));
    }
}
