export class BancoDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombre = data.nombre || '';
        this.codigo = data.codigo || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new BancoDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new BancoDTO(item));
    }
}

export class PaisDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombre = data.nombre || '';
        this.codigo = data.codigo || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new PaisDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new PaisDTO(item));
    }
}

export class RegionDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombre = data.nombre || '';
        this.paisId = data.paisId || null;
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new RegionDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new RegionDTO(item));
    }
}

export class ComunaDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombre = data.nombre || '';
        this.regionId = data.regionId || null;
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new ComunaDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new ComunaDTO(item));
    }
}

export class TipoDireccionDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.descripcion = data.descripcion || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new TipoDireccionDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new TipoDireccionDTO(item));
    }
}

export class DireccionDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.calle = data.calle || '';
        this.numero = data.numero || '';
        this.tipoDireccionId = data.tipoDireccionId || null;
        this.comunaId = data.comunaId || null;
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new DireccionDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new DireccionDTO(item));
    }
}

export class TipoContactoDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.descripcionTipoContacto = data.descripcionTipoContacto || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new TipoContactoDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new TipoContactoDTO(item));
    }
}

export class ContactoDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.valor = data.valor || '';
        this.tipoContactoId = data.tipoContactoId || null;
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new ContactoDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new ContactoDTO(item));
    }
}

export class TipoCuentaBancariaDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.descripcion = data.descripcion || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new TipoCuentaBancariaDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new TipoCuentaBancariaDTO(item));
    }
}

export class DatoBancarioDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.numeroCuenta = data.numeroCuenta || '';
        this.bancoId = data.bancoId || null;
        this.tipoCuentaBancariaId = data.tipoCuentaBancariaId || null;
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new DatoBancarioDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new DatoBancarioDTO(item));
    }
}

export class GiroDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.codigoSii = data.codigoSii || '';
        this.nombreGiro = data.nombreGiro || '';
        this.descripcionGiro = data.descripcionGiro || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new GiroDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new GiroDTO(item));
    }
}

export class RubroDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombreRubro = data.nombreRubro || '';
        this.descripcion = data.descripcion || '';
        this.siglaRubro = data.siglaRubro || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new RubroDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new RubroDTO(item));
    }
}

export class PermisoDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.nombre = data.nombre || '';
        this.descripcion = data.descripcion || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new PermisoDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new PermisoDTO(item));
    }
}

export class HistorialEstadoDTO {
    constructor(data = {}) {
        this.id = data.id || null;
        this.tipoEntidad = data.tipoEntidad || '';
        this.entidadId = data.entidadId || null;
        this.estadoAnterior = data.estadoAnterior || null;
        this.estadoNuevo = data.estadoNuevo || '';
        this.fechaCambio = data.fechaCambio || null;
        this.usuario = data.usuario || '';
        this.observacion = data.observacion || '';
    }
    static fromResponse(response) {
        if (!response?.data) return null;
        return new HistorialEstadoDTO(response.data);
    }
    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new HistorialEstadoDTO(item));
    }
}
