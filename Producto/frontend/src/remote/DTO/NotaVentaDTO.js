import { ItemNVDTO } from './ItemNVDTO';

/**
 * DTO para representar una Nota de Venta.
 * Basado en NotaVenta.java
 */
export class NotaVentaDTO {
    constructor(data = {}) {
        this.idNV = data.idNV || data.id || null;
        this.numeroNV = data.numeroNV || data.numero || '';
        this.evaluacionNegocioId = data.evaluacionNegocioId || null;
        this.clienteId = data.clienteId || null;
        this.vendedorId = data.vendedorId || null;
        this.estado = data.estado || 'BORRADOR';
        this.esKit = data.esKit || false;
        this.detalleKit = data.detalleKit || '';
        this.fechaEmision = data.fechaEmision || null;
        this.fechaEntregaEstimada = data.fechaEntregaEstimada || null;
        this.montoSubtotal = data.montoSubtotal || 0;
        this.montoIva = data.montoIva || 0;
        this.montoTotal = data.montoTotal || 0;
        this.currency = 'CLP';

        this.items = Array.isArray(data.items)
            ? data.items.map(item => new ItemNVDTO(item))
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new NotaVentaDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new NotaVentaDTO(item));
    }
}
