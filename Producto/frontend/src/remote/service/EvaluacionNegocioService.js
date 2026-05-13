import { api } from './api';
import { EvaluacionNegocioDTO } from '../DTO/EvaluacionNegocioDTO';

/**
 * Servicio para la gestión de Evaluaciones de Negocio.
 */
export const EvaluacionNegocioService = {
    getAll: async () => {
        try {
            const response = await api.get('/comercial/evaluaciones-negocio');
            return EvaluacionNegocioDTO.listFromResponse(response);
        } catch (error) {
            console.error("Error fetching Evaluaciones de Negocio:", error);
            throw error;
        }
    },

    getById: async (id) => {
        try {
            const response = await api.get(`/comercial/evaluaciones-negocio/${id}`);
            return EvaluacionNegocioDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error fetching Evaluacion de Negocio ${id}:`, error);
            throw error;
        }
    },

    save: async (evaluacion) => {
        try {
            const response = await api.post('/comercial/evaluaciones-negocio', evaluacion);
            return EvaluacionNegocioDTO.fromResponse(response);
        } catch (error) {
            console.error("Error saving Evaluacion de Negocio:", error);
            throw error;
        }
    },

    adjudicar: async (id) => {
        try {
            const response = await api.patch(`/comercial/evaluaciones-negocio/${id}/adjudicar`);
            return response.data;
        } catch (error) {
            console.error(`Error adjudicando EVN ${id}:`, error);
            throw error;
        }
    }
};
