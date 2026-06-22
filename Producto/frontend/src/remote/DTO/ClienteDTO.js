/**
 * DTO para representar un Cliente.
 * Basado en Cliente.java
 */
export class ClienteDTO {
    constructor(data = {}) {
        this.clienteId = data.clienteId || null;
        this.nombreCliente = data.nombreCliente || data.razonSocial || '';
        this.apellidoCliente = data.apellidoCliente || '';
        this.razonSocial = data.razonSocial || '';
        this.runCliente = data.runCliente || '';
        this.correoCliente = data.correoCliente || '';
        this.telefonoCliente = data.telefonoCliente || '';
        this.direccionCliente = data.direccionCliente || '';
        this.segmento = data.segmento || '';
        this.contacto = data.contacto || '';
        this.activo = data.hasOwnProperty('activo') ? data.activo : true;
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new ClienteDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new ClienteDTO(item));
    }
}
