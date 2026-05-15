import { api } from './api';
import { EvaluacionNegocioDTO } from '../DTO/EvaluacionNegocioDTO';

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

    adjudicar: async (id, aprobador, observacion) => {
        const response = await api.patch(
            `/comercial/evaluaciones-negocio/${id}/adjudicar`,
            { aprobador, observacion }
        );
        return response.data;
    },

    aprobar: async (id, aprobador, observacion) => {
        const response = await api.patch(
            `/comercial/evaluaciones-negocio/${id}/aprobar`,
            { aprobador, observacion }
        );
        return response.data;
    },

    rechazar: async (id, aprobador, motivo) => {
        const response = await api.patch(
            `/comercial/evaluaciones-negocio/${id}/rechazar`,
            { aprobador, motivo }
        );
        return response.data;
    },

    getHistorial: async (id) => {
        const response = await api.get(`/comercial/evaluaciones-negocio/${id}/historial`);
        return response.data;
    }
};
