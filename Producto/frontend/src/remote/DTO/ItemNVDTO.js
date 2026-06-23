/**
 * DTO para representar un Item de Nota de Venta.
 * Basado en ItemNV.java
 */
export class ItemNVDTO {
    constructor(data = {}) {
        this.idItemNV = data.id || data.idItemNV || null;
        this.nroItem = data.nroItem || null;
        this.productoId = data.productoId || null;
        this.proveedorId = data.proveedorId || null;
        this.cantidad = data.cantidad || 0;
        this.precioUnitario = data.precioUnitario || 0;
        this.modelo = data.modelo || '';
        this.tela = data.tela || '';
        this.composicion = data.composicion || '';
        this.color = data.color || '';
        this.talla = data.talla || '';
        this.genero = data.genero || '';
        this.llevaLogo = data.llevaLogo || 'N/A';
        this.logoDetalle = data.logoDetalle || '';
        this.tallas = data.tallas || [];
        this.itemType = data.itemType || data.tipoItem || '';
        this.requiereOt = data.requiereOt || data.generaOt || false;
        this.detalleOt = data.detalleOt || '';
        this.currency = 'CLP';
        this.totalItem = data.total || data.totalItem || 0;
    }
}
