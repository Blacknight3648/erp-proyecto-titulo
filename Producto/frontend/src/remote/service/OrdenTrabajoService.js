import { api } from './api';

/**
 * Servicio para la gestión de Órdenes de Trabajo (Fases de Producción).
 * Endpoints backend: /api/v1/produccion/ordenes-trabajo
 */
export const OrdenTrabajoService = {
    /**
     * Obtiene el listado de OTs de una Nota de Venta.
     */
    getByNotaVenta: async (nvId) => {
        try {
            const response = await api.get(`/produccion/ordenes-trabajo/nota-venta/${nvId}`);
            return response.data || [];
        } catch (error) {
            console.error(`Error fetching OTs for NV ${nvId}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene el listado de OTs de una Orden de Producción.
     */
    getByOrdenProduccion: async (opId) => {
        try {
            const response = await api.get(`/produccion/ordenes-trabajo/orden-produccion/${opId}`);
            return response.data || [];
        } catch (error) {
            console.error(`Error fetching OTs for OP ${opId}:`, error);
            throw error;
        }
    },

    /**
     * Inicia una OT (cambia de estado PENDIENTE a EN_PROCESO).
     */
    iniciar: async (id) => {
        try {
            const response = await api.post(`/produccion/ordenes-trabajo/${id}/iniciar`);
            return response.data;
        } catch (error) {
            console.error(`Error al iniciar OT ${id}:`, error);
            throw error;
        }
    },

    /**
     * Finaliza una OT manualmente.
     */
    finalizar: async (id) => {
        try {
            const response = await api.post(`/produccion/ordenes-trabajo/${id}/finalizar`);
            return response.data;
        } catch (error) {
            console.error(`Error al finalizar OT ${id}:`, error);
            throw error;
        }
    },

    /**
     * Registra un avance (unidades producidas y/o mermas) para una OT.
     * @param {number} id - ID de la OT
     * @param {Object} command - { cantidadProducida, cantidadMerma, motivoMerma, usuario, observacion }
     */
    registrarAvance: async (id, command) => {
        try {
            const response = await api.post(`/produccion/ordenes-trabajo/${id}/avance`, command);
            return response.data;
        } catch (error) {
            console.error(`Error al registrar avance en OT ${id}:`, error);
            throw error;
        }
    },

    /**
     * Obtiene el historial de avances de una OT.
     */
    getAvances: async (id) => {
        try {
            const response = await api.get(`/produccion/ordenes-trabajo/${id}/avances`);
            return response.data || [];
        } catch (error) {
            console.error(`Error fetching avances for OT ${id}:`, error);
            throw error;
        }
    }
};
