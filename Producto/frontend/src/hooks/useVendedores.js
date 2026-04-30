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

    useEffect(() => {
        fetchVendedores();
    }, [fetchVendedores]);

    return {
        vendedores,
        loading,
        fetchVendedores,
    };
};
