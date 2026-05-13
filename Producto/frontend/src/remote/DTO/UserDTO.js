import { api } from "../remote/service/api";
import { UserDTO } from "../remote/DTO/UserDTO";

const buildPayload = (usuario) => ({
    nombre: usuario.usuarioNombre,
    apellidos: usuario.usuarioApellidos,
    email: usuario.usuarioEmail,
    run: usuario.usuarioRun,
    telefono: usuario.telefono,
    region: usuario.region,
    comuna: usuario.comuna,
    roles: usuario.rol ? [usuario.rol] : [],
    areas: usuario.area ? [usuario.area] : [],
    activo: usuario.activo
});

export const colaboradoresService = {

    getAll: async () => {

        const response = await api.get("/usuarios");
        return UserDTO.listFromResponse(response);

    },

    create: async (usuario) => {

        const payload = buildPayload(usuario);

        const response = await api.post("/usuarios", payload);

        return UserDTO.fromResponse(response);

    },

    update: async (usuario) => {

        const payload = buildPayload(usuario);

        const response = await api.put(`/usuarios/${usuario.id}`, payload);

        return UserDTO.fromResponse(response);

    },

    delete: async (id) => {

        await api.delete(`/usuarios/${id}`);

    },

    toggleStatus: async (usuario) => {

        const payload = buildPayload({
            ...usuario,
            activo: !usuario.activo
        });

        const response = await api.put(`/usuarios/${usuario.id}`, payload);

        return UserDTO.fromResponse(response);

    }

};