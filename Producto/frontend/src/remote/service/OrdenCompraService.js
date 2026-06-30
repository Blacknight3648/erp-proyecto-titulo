import { api } from './api';
import { OrdenCompraDTO } from '../DTO/OrdenCompraDTO';

/**
 * Servicio para la gestión de Órdenes de Compra (modelo Fase 3 con consolidación N:M).
 * Endpoints backend: /api/v1/ordenes-compra
 */
export const OrdenCompraService = {

    /**
     * Lista todas las OCs. Filtros opcionales: estado, proveedorId, hcItemId.
     */
    getAll: async ({ estado, proveedorId, hcItemId } = {}) => {
        try {
            const params = {};
            if (estado) params.estado = estado;
            if (proveedorId) params.proveedorId = proveedorId;
            if (hcItemId) params.hcItemId = hcItemId;
            const response = await api.get('/ordenes-compra', { params });
            return OrdenCompraDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error fetching Órdenes de Compra:', error);
            throw error;
        }
    },

    getById: async (idOC) => {
        try {
            const response = await api.get(`/ordenes-compra/${idOC}`);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            if (error.response?.status === 404) return null;
            console.error(`Error fetching OC ${idOC}:`, error);
            throw error;
        }
    },

    /**
     * Genera una OC consolidando items de HCs aprobadas.
     * Payload: { proveedorId, hcItemIds: number[], fechaEntregaEstimada?, observaciones? }
     */
    generarConsolidada: async (payload) => {
        try {
            const response = await api.post('/ordenes-compra/consolidar', payload);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error('Error generando OC consolidada:', error);
            throw error;
        }
    },

    /**
     * Genera varias OC en una sola tanda atómica.
     * Payload: { grupos: [{ proveedorId, hcItemIds: number[], fechaEntregaEstimada?, observaciones? }] }
     */
    generarLote: async (grupos) => {
        try {
            const response = await api.post('/ordenes-compra/consolidar-lote', { grupos });
            return OrdenCompraDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error generando lote de OC:', error);
            throw error;
        }
    },

    marcarEnviada: async (idOC) => {
        try {
            const response = await api.patch(`/ordenes-compra/${idOC}/enviar`);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error marcando OC ${idOC} como enviada:`, error);
            throw error;
        }
    },

    marcarRecepcionada: async (idOC) => {
        try {
            const response = await api.patch(`/ordenes-compra/${idOC}/recepcionar`);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error marcando OC ${idOC} como recepcionada:`, error);
            throw error;
        }
    },

    cerrar: async (idOC) => {
        try {
            const response = await api.patch(`/ordenes-compra/${idOC}/cerrar`);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error cerrando OC ${idOC}:`, error);
            throw error;
        }
    },

    /**
     * Actualiza el precio unitario de un OCItem específico.
     * Solo permitido cuando la OC está EMITIDA.
     */
    actualizarPrecioItem: async (idOC, idOCItem, precio) => {
        try {
            const response = await api.patch(
                `/ordenes-compra/${idOC}/items/${idOCItem}/precio`,
                null,
                { params: { precio } }
            );
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error actualizando precio OCItem ${idOCItem}:`, error);
            throw error;
        }
    },
};
