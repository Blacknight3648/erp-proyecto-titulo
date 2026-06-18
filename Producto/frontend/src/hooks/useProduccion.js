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
            let res;
            if (data.idCosteo) {
                // Actualizar costeo existente
                res = await api.put(`/produccion/costeos/${data.idCosteo}`, data);
            } else {
                // Crear nuevo costeo
                res = await api.post('/produccion/costeos', data);
            }
            return res.data;
        } catch (error) {
            console.error("Error saving costeo:", error);
            if (error.response) {
                console.error("Costeo error detail:", JSON.stringify(error.response.data, null, 2));
                console.error("Costeo payload:", JSON.stringify(data, null, 2));
            }
            throw error;
        } finally {
            setLoading(false);
        }
    };


    // Decisión sobre el costeo (Épica 3). Requieren firma del actor + rol autorizado (403 si no).
    const aprobarCosteo = async (id, { aprobador, rol, observacion } = {}) => {
        const res = await api.patch(`/produccion/costeos/${id}/aprobar`, { aprobador, rol, observacion });
        return res.data;
    };

    const rechazarCosteo = async (id, { aprobador, rol, motivo } = {}) => {
        const res = await api.patch(`/produccion/costeos/${id}/rechazar`, { aprobador, rol, motivo });
        return res.data;
    };

    return {
        loading,
        getCosteoBySCOS,
        getAllCosteosBySCOS,
        getAllCosteos,
        getCosteosDisponiblesEVN,
        getCosteoResumenEVN,
        saveCosteo,
        aprobarCosteo,
        rechazarCosteo
    };
};
