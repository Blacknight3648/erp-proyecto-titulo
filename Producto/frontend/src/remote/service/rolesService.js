import { api } from './api';

export const rolesService = {
    getRoles: async () => {
        try {
            const response = await api.get('/shared/roles');
            return response.data;
        } catch (error) {
            console.error("Error fetching roles:", error);
            throw error;
        }
    }
};
