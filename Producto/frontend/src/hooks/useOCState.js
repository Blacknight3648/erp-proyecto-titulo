import { useCallback, useEffect, useState } from 'react';
import { OrdenCompraService } from '../remote/service/OrdenCompraService';
import { HojaCompraService } from '../remote/service/HojaCompraService';
import { RecepcionOCService } from '../remote/service/RecepcionOCService';
import { ProveedorService } from '../remote/service/ProveedorService';
import { getApiErrorMessage } from '../utils/apiError';

/**
 * Hook para gestionar el estado de Órdenes de Compra (lista + acciones + flujo
 * de consolidación desde HCs aprobadas + recepciones).
 */
export function useOCState() {
    const [view, setView] = useState('list'); // 'list' | 'create' | 'detail'
    const [activeTab, setActiveTab] = useState('all');
    const [selectedOC, setSelectedOC] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    const [ocs, setOcs] = useState([]);
    const [hcsAprobadas, setHcsAprobadas] = useState([]);
    const [recepciones, setRecepciones] = useState([]);
    const [proveedores, setProveedores] = useState([]);
    const [loading, setLoading] = useState(false);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        ProveedorService.getAll()
            .then(data => setProveedores(data.map(p => ({ id: p.proveedorId, nombre: p.nombreProveedor }))))
            .catch(e => console.error('Error cargando proveedores:', e));
    }, []);

    // Identidad del actor (firma). El proyecto no persiste el usuario actual de forma
    // fiable; se intenta leer de localStorage y, si no hay, se usa un actor por defecto
    // con un rol autorizado (entorno sin RBAC real). Mismo patrón que useCosteosOPState.js.
    const getActor = () => {
        try {
            const raw = localStorage.getItem('user');
            if (raw) {
                const u = JSON.parse(raw);
                const rol = u.rol || u.roles?.[0]?.nombre || u.roles?.[0];
                const aprobador = u.nombre || u.email || 'JEFE_PRODUCCION';
                if (rol) return { aprobador, rol };
            }
        } catch (_) { /* noop */ }
        return { aprobador: 'JEFE_PRODUCCION', rol: 'JEFE_PRODUCCION' };
    };

    const refresh = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const estadoFiltro = activeTab !== 'all' ? activeTab.toUpperCase() : null;
            const data = await OrdenCompraService.getAll(
                estadoFiltro ? { estado: estadoFiltro } : {}
            );
            setOcs(data);
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error cargando OCs'));
            setOcs([]);
        } finally {
            setLoading(false);
        }
    }, [activeTab]);

    const loadHCsAprobadas = useCallback(async () => {
        try {
            const data = await HojaCompraService.getAll('APROBADA');
            setHcsAprobadas(data);
            return data;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error cargando HCs'));
            return [];
        }
    }, []);

    const loadRecepciones = useCallback(async (ocId) => {
        try {
            const data = await RecepcionOCService.listarPorOC(ocId);
            setRecepciones(data);
            return data;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error cargando recepciones'));
            return [];
        }
    }, []);

    const refreshSelectedOC = useCallback(async () => {
        if (!selectedOC?.idOC) return;
        const fresh = await OrdenCompraService.getById(selectedOC.idOC);
        if (fresh) setSelectedOC(fresh);
    }, [selectedOC]);

    useEffect(() => { refresh(); }, [refresh]);

    const openCreate = useCallback(async () => {
        await loadHCsAprobadas();
        setView('create');
    }, [loadHCsAprobadas]);

    const openDetail = useCallback(async (oc) => {
        setSelectedOC(oc);
        await loadRecepciones(oc.idOC);
        setView('detail');
    }, [loadRecepciones]);

    const back = useCallback(() => {
        setSelectedOC(null);
        setRecepciones([]);
        setView('list');
    }, []);

    const generarConsolidada = useCallback(async (payload) => {
        setSubmitting(true);
        setError(null);
        try {
            const nueva = await OrdenCompraService.generarConsolidada(payload);
            await refresh();
            back();
            return nueva;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error generando OC consolidada'));
            return null;
        } finally {
            setSubmitting(false);
        }
    }, [refresh, back]);

    const marcarEnviada = useCallback(async (idOC) => {
        try {
            await OrdenCompraService.marcarEnviada(idOC);
            await refresh();
            if (selectedOC?.idOC === idOC) await refreshSelectedOC();
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error en transición'));
        }
    }, [refresh, refreshSelectedOC, selectedOC]);

    const marcarRecepcionada = useCallback(async (idOC) => {
        try {
            await OrdenCompraService.marcarRecepcionada(idOC);
            await refresh();
            if (selectedOC?.idOC === idOC) await refreshSelectedOC();
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error en transición'));
        }
    }, [refresh, refreshSelectedOC, selectedOC]);

    const cerrar = useCallback(async (idOC) => {
        try {
            await OrdenCompraService.cerrar(idOC);
            await refresh();
            if (selectedOC?.idOC === idOC) await refreshSelectedOC();
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error cerrando OC'));
        }
    }, [refresh, refreshSelectedOC, selectedOC]);

    const actualizarPrecioItem = useCallback(async (idOC, idOCItem, precio) => {
        try {
            const oc = await OrdenCompraService.actualizarPrecioItem(idOC, idOCItem, precio);
            await refresh();
            if (oc && selectedOC?.idOC === idOC) setSelectedOC(oc);
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error actualizando precio'));
        }
    }, [refresh, selectedOC]);

    const rechazar = useCallback(async (idOC, motivo) => {
        setSubmitting(true);
        setError(null);
        try {
            const oc = await OrdenCompraService.rechazar(idOC, { ...getActor(), motivo });
            await refresh();
            if (oc && selectedOC?.idOC === idOC) setSelectedOC(oc);
            return oc;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error rechazando OC'));
            return null;
        } finally {
            setSubmitting(false);
        }
    }, [refresh, selectedOC]);

    const reingresar = useCallback(async (idOC, proveedorId, itemsCambiados) => {
        setSubmitting(true);
        setError(null);
        try {
            const oc = await OrdenCompraService.reingresar(idOC, { ...getActor(), proveedorId, itemsCambiados });
            await refresh();
            if (oc && selectedOC?.idOC === idOC) setSelectedOC(oc);
            return oc;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error reingresando OC'));
            return null;
        } finally {
            setSubmitting(false);
        }
    }, [refresh, selectedOC]);

    const agregarItem = useCallback(async (idOC, itemPayload) => {
        try {
            const oc = await OrdenCompraService.agregarItem(idOC, itemPayload);
            await refresh();
            if (oc && selectedOC?.idOC === idOC) setSelectedOC(oc);
            return oc;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error agregando ítem'));
            return null;
        }
    }, [refresh, selectedOC]);

    const actualizarItem = useCallback(async (idOC, idOCItem, itemPayload) => {
        try {
            const oc = await OrdenCompraService.actualizarItem(idOC, idOCItem, itemPayload);
            await refresh();
            if (oc && selectedOC?.idOC === idOC) setSelectedOC(oc);
            return oc;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error actualizando ítem'));
            return null;
        }
    }, [refresh, selectedOC]);

    const eliminarItem = useCallback(async (idOC, idOCItem) => {
        try {
            const oc = await OrdenCompraService.eliminarItem(idOC, idOCItem);
            await refresh();
            if (oc && selectedOC?.idOC === idOC) setSelectedOC(oc);
            return oc;
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error eliminando ítem'));
            return null;
        }
    }, [refresh, selectedOC]);

    /**
     * Registra una recepción para la OC seleccionada y recarga su estado.
     */
    const registrarRecepcion = useCallback(async (ocId, payload) => {
        setSubmitting(true);
        setError(null);
        try {
            await RecepcionOCService.registrar(ocId, payload);
            await loadRecepciones(ocId);
            await refresh();
            if (selectedOC?.idOC === ocId) await refreshSelectedOC();
        } catch (e) {
            setError(getApiErrorMessage(e, 'Error registrando recepción'));
        } finally {
            setSubmitting(false);
        }
    }, [loadRecepciones, refresh, refreshSelectedOC, selectedOC]);

    const formatCLP = useCallback((value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0,
        }).format(Number(value) || 0);
    }, []);

    const filteredOCs = ocs.filter(oc => {
        const term = (searchTerm || '').toLowerCase();
        return (
            String(oc.idOC ?? '').includes(term) ||
            (oc.numeroOC || '').toLowerCase().includes(term) ||
            String(oc.proveedorId ?? '').includes(term)
        );
    });

    return {
        view, setView,
        activeTab, setActiveTab,
        selectedOC, setSelectedOC,
        searchTerm, setSearchTerm,
        ocs: filteredOCs,
        hcsAprobadas,
        recepciones,
        proveedores,
        loading,
        submitting,
        error,
        refresh,
        loadHCsAprobadas,
        loadRecepciones,
        openCreate,
        openDetail,
        back,
        generarConsolidada,
        marcarEnviada,
        marcarRecepcionada,
        cerrar,
        rechazar,
        reingresar,
        agregarItem,
        actualizarItem,
        eliminarItem,
        actualizarPrecioItem,
        registrarRecepcion,
        formatCLP,
    };
}
