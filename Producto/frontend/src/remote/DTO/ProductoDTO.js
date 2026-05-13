/**
 * DTO para representar un Producto.
 * Basado en Producto.java
 */
export class ProductoDTO {
    constructor(data = {}) {
        this.productoId = data.productoId || null;
        this.codigoProducto = data.codigoProducto || '';
        this.nombreProducto = data.nombreProducto || '';
        this.descripcionProducto = data.descripcionProducto || '';
        this.generoProducto = data.generoProducto || '';
        this.colorProducto = data.colorProducto || '';
        this.modeloProducto = data.modeloProducto || '';
        this.telaProducto = data.telaProducto || '';
        this.composicionProducto = data.composicionProducto || '';
        this.gramajeProducto = data.gramajeProducto || '';
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new ProductoDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new ProductoDTO(item));
    }
}
