import { api } from './api';

export const areasService = {
    getAreas: async () => {
        try {
            const response = await api.get('/areas');
            return response.data;
        } catch (error) {
            console.error("Error fetching areas:", error);
            throw error;
        }
    }
};
