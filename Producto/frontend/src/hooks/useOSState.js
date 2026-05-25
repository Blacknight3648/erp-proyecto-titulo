import { useCallback, useEffect, useState } from 'react';
import { OrdenServicioService } from '../remote/service/OrdenServicioService';

export function useOSState() {
    const [view, setView] = useState('list'); // 'list' | 'create' | 'detail'
    const [activeTab, setActiveTab] = useState('all');
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedOS, setSelectedOS] = useState(null);

    const [oss, setOss] = useState([]);
    const [loading, setLoading] = useState(false);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);

    const refresh = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const estadoFiltro = activeTab !== 'all' ? activeTab.toUpperCase() : null;
            const data = await OrdenServicioService.getAll(
                estadoFiltro ? { estado: estadoFiltro } : {}
            );
            setOss(data);
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error cargando OSs');
            setOss([]);
        } finally {
            setLoading(false);
        }
    }, [activeTab]);

    const refreshSelected = useCallback(async () => {
        if (!selectedOS?.idOS) return;
        const fresh = await OrdenServicioService.getById(selectedOS.idOS);
        if (fresh) setSelectedOS(fresh);
    }, [selectedOS]);

    useEffect(() => { refresh(); }, [refresh]);

    const openCreate = useCallback(() => setView('create'), []);
    const openDetail = useCallback((os) => { setSelectedOS(os); setView('detail'); }, []);
    const back = useCallback(() => { setSelectedOS(null); setView('list'); }, []);

    const crear = useCallback(async (payload) => {
        setSubmitting(true);
        setError(null);
        try {
            const nueva = await OrdenServicioService.crear(payload);
            await refresh();
            back();
            return nueva;
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error creando OS');
            return null;
        } finally {
            setSubmitting(false);
        }
    }, [refresh, back]);

    const registrarDespacho = useCallback(async (idOS, despacho) => {
        try {
            const actualizada = await OrdenServicioService.registrarDespacho(idOS, despacho);
            await refresh();
            if (actualizada && selectedOS?.idOS === idOS) setSelectedOS(actualizada);
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error en despacho');
        }
    }, [refresh, selectedOS]);

    const registrarRecepcion = useCallback(async (idOS, recepcion) => {
        try {
            const actualizada = await OrdenServicioService.registrarRecepcion(idOS, recepcion);
            await refresh();
            if (actualizada && selectedOS?.idOS === idOS) setSelectedOS(actualizada);
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error en recepción');
        }
    }, [refresh, selectedOS]);

    const cerrar = useCallback(async (idOS) => {
        try {
            await OrdenServicioService.cerrar(idOS);
            await refresh();
            if (selectedOS?.idOS === idOS) await refreshSelected();
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error cerrando OS');
        }
    }, [refresh, refreshSelected, selectedOS]);

    const formatCLP = useCallback((value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0,
        }).format(Number(value) || 0);
    }, []);

    const filteredOSs = oss.filter(os => {
        const term = (searchTerm || '').toLowerCase();
        return (
            String(os.idOS ?? '').includes(term) ||
            (os.numeroOS || '').toLowerCase().includes(term) ||
            String(os.opId ?? '').includes(term) ||
            String(os.proveedorId ?? '').includes(term) ||
            (os.tipoServicio || '').toLowerCase().includes(term)
        );
    });

    return {
        view, setView,
        activeTab, setActiveTab,
        searchTerm, setSearchTerm,
        oss: filteredOSs,
        selectedOS,
        loading, submitting, error,
        openCreate, openDetail, back,
        crear, registrarDespacho, registrarRecepcion, cerrar,
        formatCLP,
    };
}
