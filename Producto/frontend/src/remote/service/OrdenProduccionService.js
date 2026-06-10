import { api } from './api';

/**
 * Servicio para la gestión de Ordenes de Producción (OP).
 * Endpoints backend: /api/v1/produccion/ordenes-produccion
 */
export const OrdenProduccionService = {
    /**
     * Obtiene el listado de todas las OPs existentes.
     */
    getAll: async () => {
        try {
            const response = await api.get('/produccion/ordenes-produccion');
            return response.data || [];
        } catch (error) {
            console.error('Error fetching Ordenes de Producción:', error);
            throw error;
        }
    }
};
