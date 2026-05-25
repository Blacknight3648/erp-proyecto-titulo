import { api } from './api';
import { OrdenServicioDTO } from '../DTO/OrdenServicioDTO';

/**
 * Servicio para Órdenes de Servicio (talleres externos).
 * Endpoints backend: /api/v1/ordenes-servicio
 */
export const OrdenServicioService = {

    getAll: async ({ estado, opId, proveedorId } = {}) => {
        try {
            const params = {};
            if (estado) params.estado = estado;
            if (opId) params.opId = opId;
            if (proveedorId) params.proveedorId = proveedorId;
            const response = await api.get('/ordenes-servicio', { params });
            return OrdenServicioDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error fetching OSs:', error);
            throw error;
        }
    },

    getById: async (idOS) => {
        try {
            const response = await api.get(`/ordenes-servicio/${idOS}`);
            return OrdenServicioDTO.fromResponse(response);
        } catch (error) {
            if (error.response?.status === 404) return null;
            console.error(`Error fetching OS ${idOS}:`, error);
            throw error;
        }
    },

    /**
     * Crea una OS.
     * Payload: { opId, proveedorId, tipoServicio, cantidadPactada, precioUnitario?, fechaEntregaEstimada?, descripcionTrabajo?, observaciones? }
     */
    crear: async (payload) => {
        try {
            const response = await api.post('/ordenes-servicio', payload);
            return OrdenServicioDTO.fromResponse(response);
        } catch (error) {
            console.error('Error creando OS:', error);
            throw error;
        }
    },

    /**
     * Registra un despacho. Payload: { fechaDespacho?, cantidadDespachada, descripcion?, responsable?, observaciones? }
     */
    registrarDespacho: async (idOS, despacho) => {
        try {
            const response = await api.post(`/ordenes-servicio/${idOS}/despachos`, despacho);
            return OrdenServicioDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error registrando despacho en OS ${idOS}:`, error);
            throw error;
        }
    },

    /**
     * Registra una recepción. Payload: { fechaRecepcion?, cantidadRecibida, cantidadConforme?, cantidadDefectuosa?, responsable?, observaciones? }
     */
    registrarRecepcion: async (idOS, recepcion) => {
        try {
            const response = await api.post(`/ordenes-servicio/${idOS}/recepciones`, recepcion);
            return OrdenServicioDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error registrando recepción en OS ${idOS}:`, error);
            throw error;
        }
    },

    cerrar: async (idOS) => {
        try {
            const response = await api.patch(`/ordenes-servicio/${idOS}/cerrar`);
            return OrdenServicioDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error cerrando OS ${idOS}:`, error);
            throw error;
        }
    },
};
