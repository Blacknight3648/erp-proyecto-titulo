import { api } from './api';

/**
 * Servicio de notificaciones de sistema (feed global disparado por eventos reales
 * en cualquier módulo: OC, HC, OS, seguimiento de OP).
 * Endpoints backend: /api/v1/notificaciones
 */
export const NotificacionService = {
    getAll: async () => {
        try {
            const response = await api.get('/notificaciones');
            return response.data || [];
        } catch (error) {
            console.error('Error fetching notificaciones:', error);
            throw error;
        }
    },

    contarNoLeidas: async () => {
        try {
            const response = await api.get('/notificaciones/no-leidas');
            return response.data || 0;
        } catch (error) {
            console.error('Error contando notificaciones no leídas:', error);
            throw error;
        }
    },

    marcarLeida: async (id) => {
        try {
            await api.patch(`/notificaciones/${id}/leida`);
        } catch (error) {
            console.error(`Error marcando notificación ${id} como leída:`, error);
            throw error;
        }
    },

    marcarTodasLeidas: async () => {
        try {
            await api.patch('/notificaciones/marcar-todas-leidas');
        } catch (error) {
            console.error('Error marcando todas las notificaciones como leídas:', error);
            throw error;
        }
    },
};
