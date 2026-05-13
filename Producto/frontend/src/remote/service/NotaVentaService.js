import { api } from './api';
import { NotaVentaDTO } from '../DTO/NotaVentaDTO';

/**
 * Servicio para la gestión de Notas de Venta.
 */
export const NotaVentaService = {
    getAll: async () => {
        try {
            const response = await api.get('/comercial/notas-venta');
            return NotaVentaDTO.listFromResponse(response);
        } catch (error) {
            console.error("Error fetching Notas de Venta:", error);
            throw error;
        }
    },

    getById: async (id) => {
        try {
            const response = await api.get(`/comercial/notas-venta/${id}`);
            return NotaVentaDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error fetching Nota de Venta ${id}:`, error);
            throw error;
        }
    },

    save: async (notaVenta) => {
        try {
            const response = await api.post('/comercial/notas-venta', notaVenta);
            return NotaVentaDTO.fromResponse(response);
        } catch (error) {
            console.error("Error saving Nota de Venta:", error);
            throw error;
        }
    },

    getNextNumber: async () => {
        try {
            const response = await api.get('/comercial/notas-venta/next-number');
            return response.data;
        } catch (error) {
            console.error("Error fetching next number for NV:", error);
            throw error;
        }
    }
};
