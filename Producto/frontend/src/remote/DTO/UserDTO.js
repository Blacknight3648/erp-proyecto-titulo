/**
 * DTO para representar un Usuario/Colaborador.
 * Basado en ColaboradorDTO del backend.
 */
export class UserDTO {
    constructor(data = {}) {
        this.id = data.usuarioId || data.id || null;
        this.run = data.usuarioRun || '';
        this.nombre = data.usuarioNombre || '';
        this.apellidos = data.usuarioApellidos || '';
        this.email = data.usuarioEmail || '';
        this.fechaNacimiento = data.fechaNacimiento || null;
        this.direccion = data.direccion || '';
        this.region = data.region || '';
        this.comuna = data.comuna || '';
        this.activo = data.hasOwnProperty('enabled') ? data.enabled : (data.hasOwnProperty('activo') ? data.activo : true);
        this.roles = data.roles || [];
        this.areas = data.areas || [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new UserDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new UserDTO(item));
    }
}
