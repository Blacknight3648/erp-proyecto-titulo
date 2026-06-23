import { useState, useCallback } from "react";
import { api } from "../remote/service/api";
import { toast } from "sonner";

export const useComercial = () => {
    const [notasVenta, setNotasVenta] = useState([]);
    const [solicitudesCostos, setSolicitudesCostos] = useState([]);
    const [solicitudesCotizaciones, setSolicitudesCotizaciones] = useState([]);
    const [evaluacionesNegocio, setEvaluacionesNegocio] = useState([]);
    const [loading, setLoading] = useState(false);

    // Helper para validación de datos
    const validateSolicitud = (data) => {
        if (!data) return "Los datos de la solicitud son requeridos.";
        // Puedes agregar más reglas según tu modelo de datos
        if (!data.clienteId && !data.cliente_id && !data.id_cliente) {
            return "El cliente es obligatorio.";
        }
        return null;
    };

    const load = useCallback(async () => {
        setLoading(true);
        try {
            const [nvRes, scRes, scotRes, evnRes] = await Promise.allSettled([
                api.get('/comercial/notas-venta'),
                api.get('/solicitudes-costos'),
                api.get('/solicitudes-cotizaciones'),
                api.get('/comercial/evaluaciones-negocio')
            ]);
            
            if (nvRes.status === 'fulfilled') {
                setNotasVenta(nvRes.value.data || []);
            } else {
                console.error("Error loading Notas de Venta:", nvRes.reason);
            }

            if (scRes.status === 'fulfilled') {
                setSolicitudesCostos(scRes.value.data || []);
            } else {
                console.error("Error loading Solicitudes de Costos:", scRes.reason);
            }

            if (scotRes.status === 'fulfilled') {
                setSolicitudesCotizaciones(scotRes.value.data || []);
            } else {
                console.error("Error loading Solicitudes de Cotizaciones:", scotRes.reason);
            }

            if (evnRes.status === 'fulfilled') {
                setEvaluacionesNegocio(evnRes.value.data || []);
            } else {
                console.error("Error loading Evaluaciones de Negocio:", evnRes.reason);
            }
        } catch (error) {
            toast.error("Error crítico al cargar datos comerciales");
            console.error("Critical error in commercial data loader:", error);
        } finally {
            setLoading(false);
        }
    }, []);

    const createSolicitudCostos = async (data) => {
        const errorMsg = validateSolicitud(data);
        if (errorMsg) {
            toast.error(errorMsg);
            throw new Error(errorMsg);
        }

        setLoading(true);
        try {
            const res = await api.post('/solicitudes-costos', data);
            const newRequest = res.data;
            
            // Actualización optimista del estado
            setSolicitudesCostos(prev => [newRequest, ...prev]);
            toast.success("Solicitud creada con éxito");
            return newRequest;
        } catch (error) {
            const message = error.response?.data?.message || "Error al crear la solicitud";
            toast.error(message);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const updateSolicitudCostos = async (id, data) => {
        if (!id) {
            toast.error("ID de solicitud no proporcionado");
            return;
        }

        const errorMsg = validateSolicitud(data);
        if (errorMsg) {
            toast.error(errorMsg);
            throw new Error(errorMsg);
        }

        setLoading(true);
        try {
            const res = await api.put(`/solicitudes-costos/${id}`, data);
            const updatedRequest = res.data;

            // Actualizamos la lista local
            setSolicitudesCostos(prev => 
                prev.map(item => (item.id === id || item.idSolicitud === id) ? updatedRequest : item)
            );
            
            return updatedRequest;
        } catch (error) {
            console.error("Error updating solicitud-costos:", error);
            // Log detallado para diagnóstico
            if (error.response) {
                console.error("Status:", error.response.status);
                console.error("Backend error detail:", JSON.stringify(error.response.data, null, 2));
                console.error("Payload enviado:", JSON.stringify(data, null, 2));
            }
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const deleteSolicitudCostos = async (id) => {
        if (!id) return;

        setLoading(true);
        try {
            await api.delete(`/solicitudes-costos/${id}`);
            
            // Filtramos la lista localmente para reflejar el cambio
            setSolicitudesCostos(prev => 
                prev.filter(item => (item.id !== id && item.idSolicitud !== id))
            );
            
            toast.success("Solicitud eliminada correctamente");
        } catch (error) {
            toast.error("No se pudo eliminar la solicitud");
            console.error("Error deleting solicitud-costos:", error);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const createSolicitudCotizaciones = async (data) => {
        setLoading(true);
        try {
            const res = await api.post('/solicitudes-cotizaciones', data);
            const newRecord = res.data;
            setSolicitudesCotizaciones(prev => [newRecord, ...prev]);
            toast.success("Cotización creada con éxito");
            return newRecord;
        } catch (error) {
            const message = error.response?.data?.message || "Error al crear la cotización";
            toast.error(message);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const updateSolicitudCotizaciones = async (id, data) => {
        if (!id) {
            toast.error("ID de cotización no proporcionado");
            return;
        }
        setLoading(true);
        try {
            const res = await api.put(`/solicitudes-cotizaciones/${id}`, data);
            const updated = res.data;
            setSolicitudesCotizaciones(prev =>
                prev.map(item => (item.id === id || item.idSolicitud === id) ? updated : item)
            );
            return updated;
        } catch (error) {
            const message = error.response?.data?.message || "Error al actualizar la cotización";
            toast.error(message);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const deleteSolicitudCotizaciones = async (id) => {
        if (!id) return;
        setLoading(true);
        try {
            await api.delete(`/solicitudes-cotizaciones/${id}`);
            setSolicitudesCotizaciones(prev =>
                prev.filter(item => item.id !== id && item.idSolicitud !== id)
            );
            toast.success("Cotización eliminada correctamente");
        } catch (error) {
            toast.error("No se pudo eliminar la cotización");
            throw error;
        } finally {
            setLoading(false);
        }
    };


    return { 
        notasVenta, 
        solicitudesCostos, 
        solicitudesCotizaciones,
        evaluacionesNegocio,
        loadSolicitudesCostos: load,
        load,
        loading,
        createSolicitudCostos,
        updateSolicitudCostos,
        deleteSolicitudCostos,
        createSolicitudCotizaciones,
        updateSolicitudCotizaciones,
        deleteSolicitudCotizaciones
    };
};