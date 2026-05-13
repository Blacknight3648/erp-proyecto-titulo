/**
 * DTO para representar un Item de Orden de Compra.
 * Basado en ItemOC.java
 */
export class ItemOCDTO {
    constructor(data = {}) {
        this.productoId = data.productoId || null;
        this.cantidad = data.cantidad || 0;
        this.cantidadRecibida = data.cantidadRecibida || 0;
        this.precioUnitario = data.precioUnitario ? data.precioUnitario.amount : 0;
        this.currency = data.precioUnitario ? data.precioUnitario.currency : 'CLP';
    }
}
