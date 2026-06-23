/**
 * DTO para representar un Proveedor.
 * Basado en Proveedor.java
 */
export class ProveedorDTO {
    constructor(data = {}) {
        this.proveedorId = data.proveedorId || data.id || null;
        this.id = this.proveedorId;
        this.nombreProveedor = data.razonSocialProveedor || data.nombreProveedor || data.nombre || '';
        this.nombre = this.nombreProveedor;
        this.razonSocialProveedor = this.nombreProveedor;
        this.rutProveedor = data.rutProveedor || data.runProveedor || '';
        this.direccionProveedor = data.direccionProveedor || '';
        this.telefonoProveedor = data.telefonoProveedor || '';
        this.emailProveedor = data.emailProveedor || '';
        this.contactoProveedor = data.contactoProveedor || '';
        this.categoria = data.categoria || '';
        this.activo = data.hasOwnProperty('activo') ? data.activo : true;
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new ProveedorDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new ProveedorDTO(item));
    }
}
