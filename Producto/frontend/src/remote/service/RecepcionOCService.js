import { api } from './api';
import { RecepcionOCDTO } from '../DTO/RecepcionOCDTO';

/**
 * Servicio para Recepciones de OC.
 * Endpoints backend: /api/v1/recepciones-oc
 */
export const RecepcionOCService = {

    /**
     * Registra una nueva recepción para una OC.
     * Payload: { fechaRecepcion?, numeroGuia?, responsable?, observaciones?,
     *            items: [{ ocItemId, cantidadRecibida, cantidadConforme?, cantidadRechazada?, motivoRechazo? }] }
     */
    registrar: async (ocId, payload) => {
        try {
            const response = await api.post(`/recepciones-oc/oc/${ocId}`, payload);
            return RecepcionOCDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error registrando recepción para OC ${ocId}:`, error);
            throw error;
        }
    },

    getById: async (idRecepcion) => {
        try {
            const response = await api.get(`/recepciones-oc/${idRecepcion}`);
            return RecepcionOCDTO.fromResponse(response);
        } catch (error) {
            if (error.response?.status === 404) return null;
            console.error(`Error fetching recepción ${idRecepcion}:`, error);
            throw error;
        }
    },

    listarPorOC: async (ocId) => {
        try {
            const response = await api.get(`/recepciones-oc/oc/${ocId}`);
            return RecepcionOCDTO.listFromResponse(response);
        } catch (error) {
            console.error(`Error fetching recepciones de OC ${ocId}:`, error);
            throw error;
        }
    },
};
