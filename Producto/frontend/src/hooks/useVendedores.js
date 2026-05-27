import { useState, useCallback, useEffect } from "react";
import { api } from "../remote/service/api";
import { toast } from "sonner";

export const useVendedores = () => {
    const [vendedores, setVendedores] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchVendedores = useCallback(async () => {
        setLoading(true);
        try {
            const res = await api.get("/vendedores");
            setVendedores(res.data);
        } catch (error) {
            console.error(error);
            toast.error("No se pudieron cargar los vendedores");
        } finally {
            setLoading(false);
        }
    }, []);

    const createVendedor = async (data) => {
        try {
            await api.post("/vendedores", {
                usuarioId: data.usuarioId,
                codigoVendedor: data.codigoVendedor,
            });
            await fetchVendedores();
            toast.success("Vendedor registrado correctamente");
        } catch (error) {
            console.error("Error al crear vendedor:", error);
            const msg = error.response?.data?.message || "Error al registrar el vendedor";
            toast.error(msg);
        }
    };

    const updateVendedor = async (id, data) => {
        if (!id) {
            toast.error("No se puede actualizar: ID de vendedor no encontrado");
            return;
        }
        try {
            await api.put(`/vendedores/${id}`, {
                usuarioId: data.usuarioId,
                codigoVendedor: data.codigoVendedor,
            });
            await fetchVendedores();
            toast.success("Vendedor actualizado correctamente");
        } catch (error) {
            console.error("Error al actualizar vendedor:", error);
            const msg = error.response?.data?.message || "Error al actualizar el vendedor";
            toast.error(msg);
        }
    };

    const deleteVendedor = async (id) => {
        if (!id) {
            toast.error("No se puede eliminar: ID de vendedor no encontrado");
            return;
        }
        try {
            await api.delete(`/vendedores/${id}`);
            setVendedores(prev => prev.filter(v => v.id !== id));
            toast.success("Vendedor eliminado correctamente");
        } catch (error) {
            console.error("Error al eliminar vendedor:", error);
            toast.error("No se pudo eliminar el vendedor");
        }
    };

    useEffect(() => {
        fetchVendedores();
    }, [fetchVendedores]);

    return {
        vendedores,
        loading,
        fetchVendedores,
        createVendedor,
        updateVendedor,
        deleteVendedor,
    };
};
