import { api } from './api';
import { TrazabilidadOPDTO } from '../DTO/TrazabilidadOPDTO';

/**
 * Servicio de trazabilidad: vista consolidada NV → OP → ... → recepciones.
 * Endpoints backend: /api/v1/trazabilidad
 */
export const TrazabilidadService = {

    obtenerPorOP: async (opId) => {
        try {
            const response = await api.get(`/trazabilidad/op/${opId}`);
            return TrazabilidadOPDTO.fromResponse(response);
        } catch (error) {
            if (error.response?.status === 404) return null;
            console.error(`Error fetching trazabilidad OP ${opId}:`, error);
            throw error;
        }
    },
};
