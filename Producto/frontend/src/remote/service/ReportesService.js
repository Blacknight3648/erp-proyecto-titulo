import { api } from './api';
import { HojaCompraDTO } from '../DTO/HojaCompraDTO';
import { OrdenCompraDTO } from '../DTO/OrdenCompraDTO';
import { OrdenServicioDTO } from '../DTO/OrdenServicioDTO';

/**
 * Servicio para reportes operativos.
 * Endpoints backend: /api/v1/reportes
 */
export const ReportesService = {

    hcsPendientesAprobacion: async () => {
        try {
            const response = await api.get('/reportes/hcs-pendientes-aprobacion');
            return HojaCompraDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error fetching HCs pendientes:', error);
            throw error;
        }
    },

    ocsPendientesRecepcion: async () => {
        try {
            const response = await api.get('/reportes/ocs-pendientes-recepcion');
            return OrdenCompraDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error fetching OCs pendientes:', error);
            throw error;
        }
    },

    ossEnTaller: async () => {
        try {
            const response = await api.get('/reportes/oss-en-taller');
            return OrdenServicioDTO.listFromResponse(response);
        } catch (error) {
            console.error('Error fetching OSs en taller:', error);
            throw error;
        }
    },

    dashboardOPKpis: async () => {
        try {
            const response = await api.get('/reportes/dashboard-op/kpis');
            return response.data;
        } catch (error) {
            console.error('Error fetching KPIs de dashboard OP:', error);
            throw error;
        }
    },

    historialPrecios: async ({ articuloId, proveedorId } = {}) => {
        try {
            const params = {};
            if (articuloId) params.articuloId = articuloId;
            if (proveedorId) params.proveedorId = proveedorId;
            const response = await api.get('/reportes/historial-precios', { params });
            return response.data || [];
        } catch (error) {
            console.error('Error fetching historial de precios:', error);
            throw error;
        }
    },
};
