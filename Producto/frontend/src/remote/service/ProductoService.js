import { api } from './api';
import { ProductoDTO } from '../DTO/ProductoDTO';

/**
 * Servicio para la gestión de Productos.
 */
export const ProductoService = {
    getAll: async () => {
        try {
            const response = await api.get('/shared/productos');
            return ProductoDTO.listFromResponse(response);
        } catch (error) {
            console.error("Error fetching products:", error);
            throw error;
        }
    },

    getById: async (id) => {
        try {
            const response = await api.get(`/shared/productos/${id}`);
            return ProductoDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error fetching product ${id}:`, error);
            throw error;
        }
    }
};
