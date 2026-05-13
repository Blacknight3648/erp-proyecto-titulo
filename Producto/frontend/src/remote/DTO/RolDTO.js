/**
 * DTO para representar un Rol.
 * Basado en Rol.java
 */
export class RolDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombre = data.nombre || '';
        this.areaId = data.areaId || null;
        this.activo = data.hasOwnProperty('activo') ? data.activo : true;
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new RolDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new RolDTO(item));
    }
}
