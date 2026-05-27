import { useState, useEffect } from "react";
import { api } from "../remote/service/api";
import { toast } from "sonner";

export function useColaboradores() {
    const [colaboradores, setColaboradores] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadColaboradores = async () => {
        try {
            const res = await api.get("/usuarios");
            setColaboradores(res.data);
            setLoading(false);
        } catch (error) {
            console.error("Error al cargar colaboradores:", error);
            toast.error("Error al cargar colaboradores");
        } finally {
            setLoading(false);
        }
    };

    const formatPayload = (c) => ({
        usuarioRun: c.usuarioRun,
        usuarioNombre: c.usuarioNombre,
        usuarioApellidos: c.usuarioApellidos,
        usuarioEmail: c.usuarioEmail,
        usuarioPassword: c.usuarioPassword || "",
        telefono: c.telefono || "",
        fechaNacimiento: c.fechaNacimiento || null,
        direccion: c.direccion || "",
        region: c.region || "",
        comuna: c.comuna || "",
        enabled: c.enabled ?? c.activo ?? true,
        roles: Array.isArray(c.roles) 
            ? (c.roles.length > 0 && typeof c.roles[0] === 'object' ? c.roles.map(r => r.nombre) : c.roles)
            : (c.rol ? [c.rol] : []),
        areas: Array.isArray(c.areas)
            ? (c.areas.length > 0 && typeof c.areas[0] === 'object' ? c.areas.map(a => a.nombre) : c.areas)
            : (c.area ? [c.area] : [])
    });

    const createColaborador = async (colaborador) => {
        try {
            const payload = formatPayload(colaborador);
            await api.post("/usuarios", payload);
            await loadColaboradores();
            toast.success("Colaborador registrado correctamente");
        } catch (error) {
            console.error("Error al registrar colaborador:", error);
            toast.error("Error al registrar colaborador");
        }
    };

    const updateColaborador = async (colaborador) => {
        try {
            const id = colaborador.id || colaborador.usuarioId;
            const payload = formatPayload(colaborador);
            await api.put(`/usuarios/${id}`, payload);
            await loadColaboradores();
            toast.success("Colaborador actualizado correctamente");
        } catch (error) {
            console.error("Error al actualizar colaborador:", error);
            toast.error("Error al actualizar colaborador");
        }
    };

    const deleteColaborador = async (id) => {
        try {
            await api.delete(`/usuarios/${id}`);
            setColaboradores(prev => prev.filter(c => (c.id || c.usuarioId) !== id));
            toast.success("Colaborador eliminado");
        } catch (error) {
            console.error("Error al eliminar colaborador:", error);
            toast.error("Error al eliminar colaborador");
        }
    };

    const toggleColaborador = async (colaborador) => {
        try {
            const id = colaborador.id || colaborador.usuarioId;
            
            // La API espera CreateUserDTO. Si hay roles/areas como objetos, pasarlos a string.
            const payload = formatPayload(colaborador);
            // Intentamos mantener el estado activo (aunque el backend parece forzar true)
            payload.enabled = !(colaborador.enabled ?? colaborador.activo); // Toggle el estado actual
            
            await api.put(`/usuarios/${id}`, payload);
            await loadColaboradores();
            toast.success(`Estado actualizado`);
        } catch (error) {
            console.error("Error al cambiar estado:", error);
            toast.error("Error al cambiar estado");
        }
    };

    useEffect(() => {
        loadColaboradores();
    }, []);

    return {
        colaboradores,
        loading,
        createColaborador,
        updateColaborador,
        deleteColaborador,
        toggleColaborador,
        refresh: loadColaboradores
    };
}
