import { api } from './api';
import { UserDTO } from '../DTO/UserDTO';

/**
 * Servicio para manejar la autenticación.
 * Basado en el patrón observado en la carpeta remote/service.
 */
export const AuthService = {
    /**
     * Realiza el inicio de sesión.
     * @param {string} email 
     * @param {string} password 
     * @returns {Promise<{user: UserDTO, token: string}>}
     */
    login: async (email, password) => {
        try {
            const response = await api.post('/auth/login', { email, password });
            const { token, user } = response.data;
            
            // Guardar token para futuras peticiones
            if (token) {
                localStorage.setItem('token', token);
            }
            
            return {
                user: new UserDTO(user),
                token
            };
        } catch (error) {
            console.error("Error en login:", error);
            throw error;
        }
    },

    /**
     * Cierra la sesión limpiando el almacenamiento local.
     */
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }
};
