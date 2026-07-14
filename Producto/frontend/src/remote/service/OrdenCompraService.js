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

    /**
     * Historial de versiones de la OC (snapshots congelados en cada rechazo),
     * más reciente primero.
     */
    obtenerHistorialVersiones: async (idOC) => {
        try {
            const response = await api.get(`/ordenes-compra/${idOC}/versiones`);
            return response.data || [];
        } catch (error) {
            console.error(`Error obteniendo historial de versiones de OC ${idOC}:`, error);
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

    /**
     * Rechaza la OC (solo desde EMITIDA). Requiere firma del actor + rol
     * autorizado (403 si no) y motivo obligatorio.
     */
    rechazar: async (idOC, { aprobador, rol, motivo } = {}) => {
        try {
            const response = await api.patch(`/ordenes-compra/${idOC}/rechazar`, { aprobador, rol, motivo });
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error rechazando OC ${idOC}:`, error);
            throw error;
        }
    },

    /**
     * Reingresa una OC RECHAZADA: vuelve a EMITIDA con el mismo número de
     * documento e incrementa la versión. Requiere firma del actor + rol autorizado.
     * `proveedorId` e `itemsCambiados` son opcionales: permiten corregir el
     * proveedor y editar cantidad/precio de ítems en el mismo paso, todo en
     * una sola transacción atómica en el backend (si algo falla, no se
     * persiste nada y la OC queda intacta en RECHAZADA).
     */
    reingresar: async (idOC, { aprobador, rol, proveedorId, itemsCambiados } = {}) => {
        try {
            const response = await api.patch(`/ordenes-compra/${idOC}/reingresar`, { aprobador, rol, proveedorId, itemsCambiados });
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error reingresando OC ${idOC}:`, error);
            throw error;
        }
    },

    /**
     * Agrega un ítem nuevo a una OC EMITIDA.
     * Payload: { tipoInsumo, articuloId?, nombreInsumo, cantidadRequerida, cantidadStock?, cantidadComprada?, precioUnitario, hcLinks? }
     */
    agregarItem: async (idOC, itemPayload) => {
        try {
            const response = await api.post(`/ordenes-compra/${idOC}/items`, itemPayload);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error agregando ítem a OC ${idOC}:`, error);
            throw error;
        }
    },

    /**
     * Actualiza un ítem existente de una OC EMITIDA (cantidad, precio, insumo, etc.).
     */
    actualizarItem: async (idOC, idOCItem, itemPayload) => {
        try {
            const response = await api.put(`/ordenes-compra/${idOC}/items/${idOCItem}`, itemPayload);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error actualizando OCItem ${idOCItem}:`, error);
            throw error;
        }
    },

    /**
     * Elimina un ítem de una OC EMITIDA.
     */
    eliminarItem: async (idOC, idOCItem) => {
        try {
            const response = await api.delete(`/ordenes-compra/${idOC}/items/${idOCItem}`);
            return OrdenCompraDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error eliminando OCItem ${idOCItem}:`, error);
            throw error;
        }
    },
};
