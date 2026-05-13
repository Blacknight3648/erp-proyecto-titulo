import { api } from './api';
import { UserDTO } from '../DTO/UserDTO';
import { RolDTO } from '../DTO/RolDTO';

/**
 * Servicio para la gestión de Usuarios y Roles.
 */
export const UsuarioService = {
    getAllUsers: async () => {
        try {
            const response = await api.get('/shared/usuarios');
            return UserDTO.listFromResponse(response);
        } catch (error) {
            console.error("Error fetching users:", error);
            throw error;
        }
    },

    getUserById: async (id) => {
        try {
            const response = await api.get(`/shared/usuarios/${id}`);
            return UserDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error fetching user ${id}:`, error);
            throw error;
        }
    },

    saveUser: async (user) => {
        try {
            const response = await api.post('/shared/usuarios', user);
            return UserDTO.fromResponse(response);
        } catch (error) {
            console.error("Error saving user:", error);
            throw error;
        }
    },

    deleteUser: async (id) => {
        try {
            await api.delete(`/shared/usuarios/${id}`);
        } catch (error) {
            console.error(`Error deleting user ${id}:`, error);
            throw error;
        }
    },

    getAllRoles: async () => {
        try {
            const response = await api.get('/shared/roles');
            return RolDTO.listFromResponse(response);
        } catch (error) {
            console.error("Error fetching roles:", error);
            throw error;
        }
    }
};
