import { api } from './api';
import { HojaCompraDTO } from '../DTO/HojaCompraDTO';

/**
 * Servicio para la gestión de Hojas de Compra.
 * Endpoints backend: /api/v1/hojas-compra
 */
export const HojaCompraService = {

    /**
     * Lista todas las HCs (opcionalmente filtradas por estado: BORRADOR|APROBADA|CERRADA).
     */
    getAll: async (estado = null) => {
        try {
            const config = estado ? { params: { estado } } : {};
            const response = await api.get('/hojas-compra', config);
            return HojaCompraDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error fetching Hojas de Compra:', error);
            throw error;
        }
    },

    /**
     * Obtiene una HC específica por su id.
     */
    getById: async (idHC) => {
        try {
            const response = await api.get(`/hojas-compra/${idHC}`);
            return HojaCompraDTO.fromResponse(response);
        } catch (error) {
            if (error.response && error.response.status === 404) return null;
            console.error(`Error fetching Hoja de Compra ${idHC}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene la HC asociada a una OP (1:1), o null si aún no se generó.
     */
    getByOpId: async (opId) => {
        try {
            const response = await api.get(`/hojas-compra/op/${opId}`);
            return HojaCompraDTO.fromResponse(response);
        } catch (error) {
            if (error.response && error.response.status === 404) return null;
            console.error(`Error fetching HC para OP ${opId}:`, error);
            throw error;
        }
    },

    /**
     * Genera una HC nueva desde una OP. Falla si la OP no tiene costeoVersionId
     * o si ya existe una HC para esa OP.
     */
    generarDesdeOP: async (opId) => {
        try {
            const response = await api.post(`/hojas-compra/generar/${opId}`);
            return HojaCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error generando HC desde OP ${opId}:`, error);
            throw error;
        }
    },

    /**
     * Transiciona BORRADOR → APROBADA.
     */
    aprobar: async (idHC) => {
        try {
            const response = await api.patch(`/hojas-compra/${idHC}/aprobar`);
            return HojaCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error aprobando HC ${idHC}:`, error);
            throw error;
        }
    },

    /**
     * Transiciona APROBADA → CERRADA.
     */
    cerrar: async (idHC) => {
        try {
            const response = await api.patch(`/hojas-compra/${idHC}/cerrar`);
            return HojaCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error cerrando HC ${idHC}:`, error);
            throw error;
        }
    },

    /**
     * Modifica cantidad, precio o justificación de un ítem de la Hoja de Compra.
     */
    modificarItem: async (idHC, idHCItem, payload) => {
        try {
            const response = await api.put(`/hojas-compra/${idHC}/items/${idHCItem}/modificar`, payload);
            return HojaCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error modificando ítem ${idHCItem} de HC ${idHC}:`, error?.response?.data || error);
            throw error;
        }
    },
};
