import { useCallback, useEffect, useMemo, useState } from 'react';
import { HojaCompraService } from '../remote/service/HojaCompraService';
import { OrdenCompraService } from '../remote/service/OrdenCompraService';

/**
 * Mapea una HojaCompraDTO (shape del backend) al shape esperado por la lista de la UI.
 * Mantiene también el DTO completo en `raw` para acciones (aprobar, cerrar, detalle).
 */
function toRegistro(hc) {
    if (!hc) return null;
    return {
        id: hc.idHC,
        numero: hc.numeroHC,
        idOP: hc.opId,
        status: hc.estado, // BORRADOR | APROBADA | CERRADA
        fechaCreacion: hc.fechaGeneracion,
        items: hc.items.map(i => ({
            id: i.idHCItem,
            insumo: i.nombreInsumo,
            tipoInsumo: i.tipoInsumo,
            insumoId: i.insumoId,
            cantidadRequerida: i.cantidadRequerida,
            cantidadOP: i.cantidadOP,
            consumoUnitario: i.consumoUnitario,
            precioEstimado: i.precioUnitarioRef,
            proveedorId: i.proveedorId,
            proveedorNombre: i.proveedorNombre,
            ocId: i.ocId,
            numeroOC: i.numeroOC,
        })),
        // Shape backend completo para acciones que lo necesiten
        raw: hc,
    };
}

export function useHCState(initialView = 'list') {
    const [view, setView] = useState(initialView);
    const [activeTab, setActiveTab] = useState('all');
    const [selectedHC, setSelectedHC] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [formData, setFormData] = useState({});
    const [isReadOnly, setIsReadOnly] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [registros, setRegistros] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    /** Carga todas las HCs (o filtra por estado si activeTab !== 'all'). */
    const refresh = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const estadoFiltro = activeTab !== 'all' ? activeTab.toUpperCase() : null;
            const data = await HojaCompraService.getAll(estadoFiltro);
            setRegistros(data.map(toRegistro));
        } catch (e) {
            setError(e?.message || 'Error cargando Hojas de Compra');
            setRegistros([]);
        } finally {
            setLoading(false);
        }
    }, [activeTab]);

    useEffect(() => {
        refresh();
    }, [refresh]);

    /** Abre el formulario (crear) o el detalle (ver) */
    const handleOpenForm = useCallback((data, mode) => {
        setFormData(data || { items: [] });
        setIsReadOnly(mode === 'view');
        if (mode === 'view') {
            setSelectedHC(data?.id ?? null);
            setView('detail');
        } else {
            setView('form');
        }
    }, []);

    /**
     * "Crear HC" desde la UI = generar desde una OP (el backend exige opId).
     * formData debe traer `opId` para que esto funcione.
     */
    const handleSave = useCallback(async () => {
        if (!formData?.opId) {
            setError('Para generar una HC, debe indicar la OP de origen (opId)');
            return;
        }
        setIsSubmitting(true);
        setError(null);
        try {
            await HojaCompraService.generarDesdeOP(formData.opId);
            await refresh();
            setView('list');
            setFormData({});
        } catch (e) {
            const msg = e?.response?.data?.message || e?.message || 'Error generando HC';
            setError(msg);
        } finally {
            setIsSubmitting(false);
        }
    }, [formData, refresh]);

    /** Acciones de transición de estado */
    const aprobar = useCallback(async (idHC) => {
        try {
            await HojaCompraService.aprobar(idHC);
            await refresh();
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error aprobando HC');
        }
    }, [refresh]);

    const cerrar = useCallback(async (idHC) => {
        try {
            await HojaCompraService.cerrar(idHC);
            await refresh();
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error cerrando HC');
        }
    }, [refresh]);

    /** Abre la vista de gestión de compras (consolidación en OC) para una HC aprobada */
    const handleOpenModificacion = useCallback((idHC) => {
        setSelectedHC(idHC);
        setView('modificacion');
    }, []);

    /** Consolida varios grupos (proveedor + items) en una sola tanda atómica de OCs */
    const consolidarOCLote = useCallback(async (grupos) => {
        const ocs = await OrdenCompraService.generarLote(grupos);
        await refresh();
        return ocs;
    }, [refresh]);

    /** Helpers para items (solo afectan el formData local; las HC no se editan línea por línea por API) */
    const handleAddItem = useCallback(() => {
        const newItem = { id: Date.now(), insumo: '', cantidadRequerida: 0, precioEstimado: 0 };
        setFormData(prev => ({ ...prev, items: [...(prev?.items || []), newItem] }));
    }, []);

    const handleRemoveItem = useCallback((id) => {
        setFormData(prev => ({ ...prev, items: (prev?.items || []).filter(item => item.id !== id) }));
    }, []);

    const handleUpdateItem = useCallback((id, field, value) => {
        setFormData(prev => ({
            ...prev,
            items: (prev?.items || []).map(item => item.id === id ? { ...item, [field]: value } : item),
        }));
    }, []);

    const formatCLP = useCallback((value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0,
        }).format(Number(value) || 0);
    }, []);

    const calculations = useMemo(() => {
        const items = formData?.items || [];
        return {
            totalItems: items.length,
            totalBudget: items.reduce(
                (acc, curr) => acc + (parseFloat(curr.cantidadRequerida || 0) * parseFloat(curr.precioEstimado || 0)),
                0
            ),
            totalUnits: items.reduce((acc, curr) => acc + parseFloat(curr.cantidadRequerida || 0), 0),
        };
    }, [formData]);

    return {
        view, setView,
        activeTab, setActiveTab,
        selectedHC, setSelectedHC,
        searchTerm, setSearchTerm,
        registros,
        loading,
        error,
        formData, setFormData,
        isReadOnly,
        isSubmitting,
        refresh,
        handleOpenForm,
        addItem: handleAddItem,
        removeItem: handleRemoveItem,
        updateItem: handleUpdateItem,
        handleSave,
        aprobar,
        cerrar,
        handleOpenModificacion,
        consolidarOCLote,
        formatCLP,
        ...calculations,
    };
}
