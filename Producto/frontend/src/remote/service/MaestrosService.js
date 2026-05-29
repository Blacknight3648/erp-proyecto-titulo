import { api } from './api';
import {
    BancoDTO,
    PaisDTO,
    RegionDTO,
    ComunaDTO,
    TipoDireccionDTO,
    DireccionDTO,
    TipoContactoDTO,
    ContactoDTO,
    TipoCuentaBancariaDTO,
    DatoBancarioDTO,
    GiroDTO,
    RubroDTO,
    PermisoDTO,
    HistorialEstadoDTO,
} from '../DTO/MaestrosDTO';

// --- Bancos ---
export const BancoService = {
    getAll: async () => {
        const response = await api.get('/bancos');
        return BancoDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/bancos/${id}`);
        return BancoDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/bancos', dto);
        return BancoDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/bancos/${id}`, dto);
        return BancoDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/bancos/${id}`);
    },
};

// --- Países ---
export const PaisService = {
    getAll: async () => {
        const response = await api.get('/paises');
        return PaisDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/paises/${id}`);
        return PaisDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/paises', dto);
        return PaisDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/paises/${id}`, dto);
        return PaisDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/paises/${id}`);
    },
};

// --- Regiones ---
export const RegionService = {
    getAll: async () => {
        const response = await api.get('/regiones');
        return RegionDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/regiones/${id}`);
        return RegionDTO.fromResponse(response);
    },
    getByPais: async (paisId) => {
        const response = await api.get(`/regiones/pais/${paisId}`);
        return RegionDTO.listFromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/regiones', dto);
        return RegionDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/regiones/${id}`, dto);
        return RegionDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/regiones/${id}`);
    },
};

// --- Comunas ---
export const ComunaService = {
    getAll: async () => {
        const response = await api.get('/comunas');
        return ComunaDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/comunas/${id}`);
        return ComunaDTO.fromResponse(response);
    },
    getByRegion: async (regionId) => {
        const response = await api.get(`/comunas/region/${regionId}`);
        return ComunaDTO.listFromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/comunas', dto);
        return ComunaDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/comunas/${id}`, dto);
        return ComunaDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/comunas/${id}`);
    },
};

// --- Tipos de Dirección ---
export const TipoDireccionService = {
    getAll: async () => {
        const response = await api.get('/tipos-direccion');
        return TipoDireccionDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/tipos-direccion/${id}`);
        return TipoDireccionDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/tipos-direccion', dto);
        return TipoDireccionDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/tipos-direccion/${id}`, dto);
        return TipoDireccionDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/tipos-direccion/${id}`);
    },
};

// --- Direcciones ---
export const DireccionService = {
    getAll: async () => {
        const response = await api.get('/direcciones');
        return DireccionDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/direcciones/${id}`);
        return DireccionDTO.fromResponse(response);
    },
    getByComuna: async (comunaId) => {
        const response = await api.get(`/direcciones/comuna/${comunaId}`);
        return DireccionDTO.listFromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/direcciones', dto);
        return DireccionDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/direcciones/${id}`, dto);
        return DireccionDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/direcciones/${id}`);
    },
};

// --- Tipos de Contacto ---
export const TipoContactoService = {
    getAll: async () => {
        const response = await api.get('/tipo-contacto');
        return TipoContactoDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/tipo-contacto/${id}`);
        return TipoContactoDTO.fromResponse(response);
    },
    getByDescripcion: async (descripcion) => {
        const response = await api.get(`/tipo-contacto/descripcion/${descripcion}`);
        return TipoContactoDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/tipo-contacto', dto);
        return TipoContactoDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/tipo-contacto/${id}`, dto);
        return TipoContactoDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/tipo-contacto/${id}`);
    },
};

// --- Contactos ---
export const ContactoService = {
    getAll: async () => {
        const response = await api.get('/contacto');
        return ContactoDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/contacto/${id}`);
        return ContactoDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/contacto', dto);
        return ContactoDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/contacto/${id}`, dto);
        return ContactoDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/contacto/${id}`);
    },
};

// --- Tipos de Cuenta Bancaria ---
export const TipoCuentaBancariaService = {
    getAll: async () => {
        const response = await api.get('/tipos-cuenta-bancaria');
        return TipoCuentaBancariaDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/tipos-cuenta-bancaria/${id}`);
        return TipoCuentaBancariaDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/tipos-cuenta-bancaria', dto);
        return TipoCuentaBancariaDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/tipos-cuenta-bancaria/${id}`, dto);
        return TipoCuentaBancariaDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/tipos-cuenta-bancaria/${id}`);
    },
};

// --- Datos Bancarios ---
export const DatoBancarioService = {
    getAll: async () => {
        const response = await api.get('/datos-bancarios');
        return DatoBancarioDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/datos-bancarios/${id}`);
        return DatoBancarioDTO.fromResponse(response);
    },
    getByBanco: async (bancoId) => {
        const response = await api.get(`/datos-bancarios/banco/${bancoId}`);
        return DatoBancarioDTO.listFromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/datos-bancarios', dto);
        return DatoBancarioDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/datos-bancarios/${id}`, dto);
        return DatoBancarioDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/datos-bancarios/${id}`);
    },
};

// --- Giros ---
export const GiroService = {
    getAll: async () => {
        const response = await api.get('/giros');
        return GiroDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/giros/${id}`);
        return GiroDTO.fromResponse(response);
    },
    getByCodigoSii: async (codigoSii) => {
        const response = await api.get(`/giros/codigo/${codigoSii}`);
        return GiroDTO.fromResponse(response);
    },
    searchByNombre: async (nombreGiro) => {
        const response = await api.get(`/giros/nombre/${nombreGiro}`);
        return GiroDTO.listFromResponse(response);
    },
    searchByDescripcion: async (descripcionGiro) => {
        const response = await api.get(`/giros/descripcion/${descripcionGiro}`);
        return GiroDTO.listFromResponse(response);
    },
    getOrCreate: async (nombreGiro) => {
        const response = await api.get(`/giros/obtenerocrear/${nombreGiro}`);
        return GiroDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/giros', dto);
        return GiroDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/giros/${id}`, dto);
        return GiroDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/giros/${id}`);
    },
};

// --- Rubros ---
export const RubroService = {
    getAll: async () => {
        const response = await api.get('/rubros');
        return RubroDTO.listFromResponse(response);
    },
    getById: async (id) => {
        const response = await api.get(`/rubros/${id}`);
        return RubroDTO.fromResponse(response);
    },
    getByNombre: async (nombreRubro) => {
        const response = await api.get(`/rubros/nombre/${nombreRubro}`);
        return RubroDTO.fromResponse(response);
    },
    create: async (dto) => {
        const response = await api.post('/rubros', dto);
        return RubroDTO.fromResponse(response);
    },
    update: async (id, dto) => {
        const response = await api.put(`/rubros/${id}`, dto);
        return RubroDTO.fromResponse(response);
    },
    delete: async (id) => {
        await api.delete(`/rubros/${id}`);
    },
};

// --- Permisos ---
export const PermisoService = {
    getAll: async () => {
        const response = await api.get('/permisos');
        return PermisoDTO.listFromResponse(response);
    },
};

// --- Historial de Estado ---
export const HistorialEstadoService = {
    getByEntidad: async (tipoEntidad, entidadId) => {
        const response = await api.get(`/historial-estado/${tipoEntidad}/${entidadId}`);
        return HistorialEstadoDTO.listFromResponse(response);
    },
};
