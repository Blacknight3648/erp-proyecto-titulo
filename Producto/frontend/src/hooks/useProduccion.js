import { useState, useCallback } from "react";
import { api } from "../remote/service/api";

export const useProduccion = () => {
    const [loading, setLoading] = useState(false);

    const getCosteoBySCOS = async (scosId) => {
        setLoading(true);
        try {
            const res = await api.get(`/produccion/costeos/scos/${scosId}`);
            return res.data;
        } catch (error) {
            if (error.response?.status === 404) {
                return null;
            }
            console.error("Error fetching costeo by SCOS:", error);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const getAllCosteosBySCOS = async (scosId) => {
        setLoading(true);
        try {
            const res = await api.get(`/produccion/costeos/scos/${scosId}/all`);
            return res.data || [];
        } catch (error) {
            console.error("Error fetching all costeos by SCOS:", error);
            return [];
        } finally {
            setLoading(false);
        }
    };

    // Lista global de costeos (para buscador en ítems OP de la EVN)
    const getAllCosteos = useCallback(async () => {
        setLoading(true);
        try {
            const res = await api.get('/produccion/costeos/scos');
            return res.data || [];
        } catch (error) {
            console.error("Error fetching all costeos:", error);
            return [];
        } finally {
            setLoading(false);
        }
    }, []);

    // Costeos disponibles (no vinculados a ninguna EVN) para vincular a un ítem OP
    const getCosteosDisponiblesEVN = useCallback(async () => {
        try {
            const res = await api.get('/produccion/costeos/disponibles-evn');
            return res.data || [];
        } catch (error) {
            console.error("Error fetching costeos disponibles para EVN:", error);
            return [];
        }
    }, []);

    // Resumen de un costeo para auto-rellenar un ítem OP (modelo/tela/composición/género)
    const getCosteoResumenEVN = useCallback(async (idCosteo) => {
        try {
            const res = await api.get(`/produccion/costeos/${idCosteo}/resumen-evn`);
            return res.data;
        } catch (error) {
            if (error.response?.status === 404) return null;
            console.error("Error fetching costeo resumen-evn:", error);
            throw error;
        }
    }, []);

    const saveCosteo = async (data) => {
        setLoading(true);
        try {
            // El backend actual solo tiene un POST que parece manejar creación.
            // Si el ID de costeo existe, podríamos necesitar un PUT o que el POST lo maneje.
            // Viendo el controlador, solo hay POST /api/v1/produccion/costeos
            const res = await api.post('/produccion/costeos', data);
            return res.data;
        } catch (error) {
            console.error("Error saving costeo:", error);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    return {
        loading,
        getCosteoBySCOS,
        getAllCosteosBySCOS,
        getAllCosteos,
        getCosteosDisponiblesEVN,
        getCosteoResumenEVN,
        saveCosteo
    };
};
