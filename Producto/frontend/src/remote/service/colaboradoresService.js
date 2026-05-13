import { api } from "../remote/service/api";
import { UserDTO } from "../remote/DTO/UserDTO";

export const colaboradoresService = {

    getAll: async () => {

        try {

            const response = await api.get("/usuarios");

            return UserDTO.listFromResponse(response);

        } catch (error) {

            console.error("Error fetching usuarios:", error);
            throw error;

        }

    },


    create: async (usuario) => {

        try {

            const payload = {

                ...usuario,
                roles: usuario.rol ? [usuario.rol] : [],
                areas: usuario.area ? [usuario.area] : []

            };

            const response = await api.post("/usuarios", payload);

            return UserDTO.fromResponse(response);

        } catch (error) {

            console.error("Error creating usuario:", error);
            throw error;

        }

    },


    update: async (usuario) => {

        try {

            const payload = {

                ...usuario,
                roles: usuario.rol ? [usuario.rol] : [],
                areas: usuario.area ? [usuario.area] : []

            };

            const response = await api.put(`/usuarios/${usuario.id}`, payload);

            return UserDTO.fromResponse(response);

        } catch (error) {

            console.error(`Error updating usuario ${usuario.id}:`, error);
            throw error;

        }

    },


    delete: async (id) => {

        try {

            await api.delete(`/usuarios/${id}`);

        } catch (error) {

            console.error(`Error deleting usuario ${id}:`, error);
            throw error;

        }

    },


    toggleStatus: async (usuario) => {

        try {

            const payload = {

                ...usuario,
                activo: !usuario.activo,
                roles: usuario.rol ? [usuario.rol] : [],
                areas: usuario.area ? [usuario.area] : []

            };

            const response = await api.put(`/usuarios/${usuario.id}`, payload);

            return UserDTO.fromResponse(response);

        } catch (error) {

            console.error(`Error toggling usuario ${usuario.id}:`, error);
            throw error;

        }

    }

};