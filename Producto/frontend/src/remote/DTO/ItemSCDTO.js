/**
 * DTO para representar un Item de Solicitud de Compra.
 * Basado en ItemSC.java
 */
export class ItemSCDTO {
    constructor(data = {}) {
        this.productoId = data.productoId || null;
        this.cantidad = data.cantidad || 0;
        this.descripcion = data.descripcionProducto || data.descripcion || '';
        this.precioEstimadoUnitario = data.precioEstimadoUnitario || 0;
        this.moneda = data.moneda || 'CLP';
        this.tipoItem = data.tipo || data.tipoItem || 'STOCK';
    }
}
