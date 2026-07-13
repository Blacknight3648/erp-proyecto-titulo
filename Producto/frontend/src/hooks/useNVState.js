import { useState, useEffect, useMemo, useCallback } from 'react';
import { api } from '../remote/service/api';
import { useVendedores } from './useVendedores';
import { useProveedores } from './useProveedores';
import { useClientes } from './useClientes';
import { useComercial } from './useComercial';
import { useProduccion } from './useProduccion';
import { toast } from 'sonner';
import { validateNumericInput } from '../utils/validations';
import { getApiErrorMessage } from '../utils/apiError';

export const useNVState = (initialView = 'list') => {
    const [view, setView] = useState(initialView);
    const [activeTab, setActiveTab] = useState('registros');
    const [selectedNV, setSelectedNV] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    // Data States
    const { clientes, loading: loadingClientes } = useClientes();
    const { vendedores } = useVendedores();
    const { proveedores } = useProveedores();
    const {
        notasVenta,
        evaluacionesNegocio,
        load: loadComercial,
        loading: loadingComercial
    } = useComercial();
    const { getCosteosDisponiblesOP, getOrdenesProduccionPorNotaVenta } = useProduccion();

    const registros = useMemo(() => {
        return (notasVenta || []).map(r => {
            const foundClient = clientes?.find(c => (c.clienteId || c.id) === r.clienteId);
            const resolvedClienteNombre = (r.clienteNombre && !r.clienteNombre.startsWith("Cliente #"))
                ? r.clienteNombre
                : (foundClient?.razonSocial || foundClient?.nombreCliente || r.clienteNombre || ("Cliente #" + r.clienteId));
            return {
                ...r,
                clienteNombre: resolvedClienteNombre,
                cliente: resolvedClienteNombre
            };
        });
    }, [notasVenta, clientes]);

    const evaluaciones = useMemo(() => {
        return (evaluacionesNegocio || []).map(e => {
            const foundClient = clientes?.find(c => (c.clienteId || c.id) === e.clienteId);
            const resolvedClienteNombre = (e.clienteNombre && !e.clienteNombre.startsWith("Cliente #"))
                ? e.clienteNombre
                : (foundClient?.razonSocial || foundClient?.nombreCliente || e.clienteNombre || ("Cliente #" + e.clienteId));
            return {
                ...e,
                clienteNombre: resolvedClienteNombre,
                cliente: resolvedClienteNombre
            };
        });
    }, [evaluacionesNegocio, clientes]);

    const [formData, setFormData] = useState({
        clienteId: '',
        clienteNombre: '',
        vendedorId: '',
        fechaEntregaEstimada: '',
        esKit: false,
        detalleKit: '',
        items: []
    });

    const [isReadOnly, setIsReadOnly] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitStatus, setSubmitStatus] = useState(null);
    const [sourceEVN, setSourceEVN] = useState(null);
    const [nextNumbers, setNextNumbers] = useState({ nv: '...', sc: '...' });
    const [evnModal, setEvnModal] = useState({ open: false, evn: null, selectedIds: new Set() });

    // Info de solo lectura de las OP ya creadas para esta NV (una por cada ítem tipo OP
    // que ya tenga OP asignada), para mostrar el costeo real de cada ítem al editar/ver
    // una NV guardada. Cada ítem tipo OP tiene su propia OP y su propio costeo.
    const [opsCosteoInfo, setOpsCosteoInfo] = useState([]);

    // Vínculo manual de Costeo existente a un ítem tipo OP nuevo (que aún no
    // hereda un costeo desde la EVN plantilla). Solo aplica al crear la NV.
    const [showCosteoModal, setShowCosteoModal] = useState(false);
    const [costeoModalItemId, setCosteoModalItemId] = useState(null);
    const [costeosDisponibles, setCosteosDisponibles] = useState([]);
    const [loadingCosteos, setLoadingCosteos] = useState(false);

    const openCosteoSelector = useCallback(async (itemId) => {
        setCosteoModalItemId(itemId);
        setShowCosteoModal(true);
        setLoadingCosteos(true);
        try {
            const data = await getCosteosDisponiblesOP();
            setCosteosDisponibles(data || []);
        } finally {
            setLoadingCosteos(false);
        }
    }, [getCosteosDisponiblesOP]);

    const closeCosteoSelector = useCallback(() => {
        setShowCosteoModal(false);
        setCosteoModalItemId(null);
    }, []);

    const handleSelectCosteo = useCallback((idCosteo, numeroCosteo) => {
        if (costeoModalItemId != null) {
            setFormData(prev => ({
                ...prev,
                items: prev.items.map(item =>
                    item.id === costeoModalItemId ? { ...item, costeoId: idCosteo || null, numeroCosteo: numeroCosteo || null } : item)
            }));
        }
        setShowCosteoModal(false);
        setCosteoModalItemId(null);
    }, [costeoModalItemId]);

    useEffect(() => {
        loadComercial();
    }, []);

    useEffect(() => {
        if (view === 'form') {
            fetchNextNumbers();
        }
    }, [view]);

    const fetchNextNumbers = async () => {
        try {
            const nvRes = await api.get('/comercial/notas-venta/next-number');
            setNextNumbers({ nv: nvRes.data, sc: '...' });
        } catch (e) {
            console.error("Error fetching next numbers", e);
        }
    };

    const formatDateToBackend = (dateStr) => {
        if (!dateStr) return null;
        const [year, month, day] = dateStr.split('-');
        return `${day}-${month}-${year}`;
    };

    const transformSizesToList = (sizesObj) => {
        return Object.entries(sizesObj)
            .filter(([_, qty]) => qty > 0)
            .map(([talla, cantidad]) => ({ talla, cantidad }));
    };

    const loadOpCosteoInfo = useCallback(async (notaVentaId) => {
        if (!notaVentaId) {
            setOpsCosteoInfo([]);
            return;
        }
        const ops = await getOrdenesProduccionPorNotaVenta(notaVentaId);
        setOpsCosteoInfo((ops || []).map(op => ({
            opId: op.idOP,
            numeroOP: op.numeroOP,
            numeroCosteo: op.numeroCosteo,
            estadoCosteo: op.estadoCosteo
        })));
    }, [getOrdenesProduccionPorNotaVenta]);

    const handleOpenForm = (data = null, mode = 'new') => {
        setSubmitStatus(null);
        setSourceEVN(null);
        setOpsCosteoInfo([]);

        if (mode === 'view' || mode === 'edit') {
            loadOpCosteoInfo(data?.idNV);
        }

        if (mode === 'view') {
            setFormData({
                ...data,
                vendedorId: data.vendedorId || '',
                fechaEntregaEstimada: data.fechaEntregaEstimada || ''
            });
            setIsReadOnly(true);
            setView('form');
        } else if (mode === 'edit') {
            const tallasToSizes = (tallas) => {
                const base = { XS: 0, S: 0, M: 0, L: 0, XL: 0 };
                (tallas || []).forEach(t => { if (t.talla in base) base[t.talla] = t.cantidad; });
                return base;
            };
            setFormData({
                idNV: data.idNV,
                numeroNV: data.numeroNV || '',
                clienteId: data.clienteId || '',
                clienteNombre: data.clienteNombre || '',
                vendedorId: data.vendedorId || '',
                fechaEntregaEstimada: data.fechaEntregaEstimada || '',
                esKit: data.esKit || false,
                detalleKit: data.detalleKit || '',
                items: (data.items || []).map((item, i) => ({
                    id: Date.now() + i,
                    idItemNV: item.idItemNV || null,
                    opId: item.opId || null,
                    numeroOPReservado: item.numeroOPReservado || null,
                    productoId: item.articuloId || '',
                    nombreProducto: item.nombreProducto || item.modelo || '',
                    modelo: item.modelo || '',
                    tela: item.tela || '',
                    composicion: item.composicion || '',
                    color: item.color || '',
                    coloresDisponibles: [],
                    genero: item.genero || 'Unisex',
                    talla: item.talla || '',
                    sizes: tallasToSizes(item.tallas),
                    quantity: item.cantidad || 0,
                    unitPrice: item.precioUnitario || 0,
                    tipoItem: item.tipoItem || 'OP',
                    logo: item.llevaLogo || 'N/A',
                    proveedorId: item.proveedorId || '',
                    requiereOt: item.requiereOt || false,
                    detalleOt: item.detalleOt || '',
                    costeoId: item.costeoIdManual || item.costeoId || null,
                }))
            });
            setSourceEVN(data.evaluacionNegocioId || null);
            setIsReadOnly(false);
            setView('form');
        } else if (mode === 'template') {
            setFormData({
                clienteId: data.clienteId,
                clienteNombre: data.clienteNombre,
                vendedorId: data.vendedorId || '',
                referencia: data.referencia || '',
                fechaEntregaEstimada: '',
                esKit: false,
                detalleKit: '',
                items: (data.items || []).map(item => ({
                    id: item.id || Date.now() + Math.random(),
                    productoId: item.productoId || '',
                    nombreProducto: item.descripcion || item.nombreProducto || item.producto || '',
                    modelo: item.modelo || item.descripcion || '',
                    tela: item.tela || '',
                    composicion: item.composicion || '',
                    color: item.color || '',
                    coloresDisponibles: [],
                    genero: item.genero || 'Unisex',
                    talla: item.talla || '',
                    sizes: item.sizes || { XS: 0, S: 0, M: 0, L: 0, XL: 0 },
                    quantity: item.cantidad || item.cant || 0,
                    unitPrice: item.precioNetoUnit || item.unitPrice || item.precioUnitario || item.precioVentaNeto || 0,
                    tipoItem: item.tipoItem || item.tipo || 'OP',
                    codigoInterno: item.codigoInterno || '',
                    logo: item.logo || 'N/A',
                    proveedorId: item.proveedorId || '',
                    requiereOt: item.requiereOt || false,
                    detalleOt: item.detalleOt || '',
                    // Costeo ya vinculado en la EVN (si el ítem OP lo trae); si no viene,
                    // el usuario podrá vincular uno existente manualmente en el formulario.
                    costeoId: item.costeoId || null,
                }))
            });
            setSourceEVN(data.evaluacionNegocioId || data.id);
            setIsReadOnly(false);
            setView('form');
        }
        // Toda NV nueva se crea desde una EVN adjudicada (modo 'template');
        // el dominio NotaVenta exige evaluacionNegocioId no nulo.
    };

    const addItem = () => {
        if (isReadOnly) return;
        const newItem = {
            id: Date.now(),
            idItemNV: null,
            opId: null,
            numeroOPReservado: null,
            productoId: '',
            nombreProducto: '',
            modelo: '',
            tela: '',
            composicion: '',
            color: '',
            coloresDisponibles: [],
            genero: 'Unisex',
            talla: '',
            sizes: { XS: 0, S: 0, M: 0, L: 0, XL: 0 },
            quantity: 0,
            unitPrice: 0,
            tipoItem: 'OP',
            logo: 'N/A',
            proveedorId: '',
            requiereOt: false,
            detalleOt: '',
            costeoId: null,
        };
        setFormData({ ...formData, items: [...formData.items, newItem] });
    };

    const removeItem = (id) => {
        if (isReadOnly) return;
        setFormData({ ...formData, items: formData.items.filter(item => item.id !== id) });
    };

    const updateItem = (id, field, value) => {
        if (isReadOnly) return;

        if (field === 'unitPrice' || field === 'quantity') {
            const label = field === 'unitPrice' ? 'Precio Unitario' : 'Cantidad';
            const error = validateNumericInput(value, label);
            if (error) {
                toast.error(error);
                return;
            }
        }

        setFormData(prev => {
            const newItems = prev.items.map(item => {
                if (item.id === id) {
                    const updatedItem = { ...item, [field]: value };
                    
                    if (field === 'nombreProducto' || field === 'proveedorId') {
                        const targetProduct = field === 'nombreProducto' ? value : item.nombreProducto;
                        const currentProveedorId = field === 'proveedorId' ? value : item.proveedorId;

                        if (targetProduct && currentProveedorId) {
                            const p = proveedores.find(prov =>
                                (prov.nombreProveedor === currentProveedorId || prov.nombre === currentProveedorId) ||
                                (String(prov.proveedorId) === String(currentProveedorId) || String(prov.id) === String(currentProveedorId))
                            );
                            if (p && p.precios) {
                                const priceEntry = p.precios.find(pr =>
                                    pr.garment.toLowerCase().includes(targetProduct.toLowerCase()) ||
                                    targetProduct.toLowerCase().includes(pr.garment.toLowerCase())
                                );
                                if (priceEntry) {
                                    updatedItem.unitPrice = priceEntry.price;
                                }
                            }
                        }
                    }
                    return updatedItem;
                }
                return item;
            });
            return { ...prev, items: newItems };
        });
    };

    const updateSize = (itemId, size, val) => {
        if (isReadOnly) return;

        const error = validateNumericInput(val, `Talla ${size}`);
        if (error) {
            toast.error(error);
            return;
        }

        setFormData({
            ...formData,
            items: formData.items.map(item => {
                if (item.id === itemId) {
                    const newSizes = { ...item.sizes, [size]: parseInt(val) || 0 };
                    const newTotal = Object.values(newSizes).reduce((a, b) => a + b, 0);
                    return { ...item, sizes: newSizes, quantity: newTotal };
                }
                return item;
            })
        });
    };

    const handleConfirmNV = async (isDraft = false) => {
        if (!formData.clienteId) {
            toast.error('Debe seleccionar un cliente');
            return;
        }
        if (formData.items.length === 0) {
            toast.error('Debe añadir al menos un ítem');
            return;
        }
        if (!formData.fechaEntregaEstimada) {
            toast.error('Debe indicar la fecha de entrega estimada');
            return;
        }

        setIsSubmitting(true);
        setSubmitStatus(null);
        try {
            const payload = {
                emitir: !isDraft,
                clienteId: parseInt(formData.clienteId),
                vendedorId: parseInt(formData.vendedorId) || 1,
                esKit: formData.esKit,
                detalleKit: formData.detalleKit,
                fechaEntregaEstimada: formData.fechaEntregaEstimada,
                evaluacionNegocioId: sourceEVN || null,
                items: formData.items.map(item => ({
                    idItemNV: item.idItemNV || null,
                    articuloId: item.productoId ? parseInt(item.productoId) : null,
                    cantidad: item.quantity,
                    precioUnitario: item.unitPrice,
                    modelo: item.modelo,
                    tela: item.tela,
                    composicion: item.composicion,
                    color: item.color,
                    tallas: transformSizesToList(item.sizes),
                    genero: item.genero,
                    proveedorId: item.proveedorId ? parseInt(item.proveedorId) : null,
                    llevaLogo: item.logo || 'N/A',
                    itemType: item.tipoItem,
                    costeoId: item.tipoItem === 'OP' ? (item.costeoId || null) : null,
                    requiereOt: item.requiereOt,
                    detalleOt: item.detalleOt,
                    logoDetalle: item.logo || 'N/A'
                }))
            };
            let savedNV;
            if (formData.idNV) {
                const res = await api.put(`/comercial/notas-venta/${formData.idNV}`, payload);
                savedNV = res.data;
            } else {
                const res = await api.post('/comercial/notas-venta', payload);
                savedNV = res.data;
            }

            // Actualizar el estado local con los IDs asignados por el backend
            // (idItemNV, opId, numeroOPReservado) para que un segundo guardado no pierda datos.
            if (savedNV && savedNV.items && isDraft) {
                setFormData(prev => ({
                    ...prev,
                    idNV: savedNV.idNV || prev.idNV,
                    numeroNV: savedNV.numeroNV || prev.numeroNV,
                    items: prev.items.map((localItem, idx) => {
                        const serverItem = savedNV.items[idx];
                        if (!serverItem) return localItem;
                        return {
                            ...localItem,
                            idItemNV: serverItem.idItemNV || localItem.idItemNV,
                            opId: serverItem.opId || localItem.opId,
                            numeroOPReservado: serverItem.numeroOPReservado || localItem.numeroOPReservado,
                            costeoId: serverItem.costeoIdManual || localItem.costeoId,
                        };
                    })
                }));
            }

            setSubmitStatus({ type: 'success', message: 'Operación exitosa' });
            setTimeout(() => { setView('list'); loadComercial(); }, 2000);
        } catch (error) {
            const msg = getApiErrorMessage(error, 'Error desconocido');
            setSubmitStatus({ type: 'error', message: msg });
        } finally {
            setIsSubmitting(false);
        }
    };

    const deleteDraftNV = async (id) => {
        setIsSubmitting(true);
        try {
            await api.delete(`/comercial/notas-venta/${id}`);
            return true;
        } catch (error) {
            console.error("Error al eliminar borrador:", error);
            setSubmitStatus({ type: 'error', message: getApiErrorMessage(error, 'Error al eliminar el borrador') });
            return false;
        } finally {
            setIsSubmitting(false);
        }
    };

    const totalItems = (formData.items || []).reduce((sum, item) => sum + (parseInt(item.quantity) || 0), 0);
    const totalAmount = (formData.items || []).reduce((sum, item) => sum + ((parseInt(item.quantity) || 0) * (parseFloat(item.unitPrice) || 0)), 0);

    return {
        view, setView,
        activeTab, setActiveTab,
        selectedNV, setSelectedNV,
        searchTerm, setSearchTerm,
        loadingClientes,
        clientes,
        vendedores,
        proveedores,
        registros,
        evaluaciones,
        loadingComercial,
        formData, setFormData,
        isReadOnly, setIsReadOnly,
        isSubmitting,
        submitStatus,
        sourceEVN,
        nextNumbers,
        evnModal, setEvnModal,
        opsCosteoInfo,
        showCosteoModal,
        costeoModalItemId,
        costeosDisponibles,
        loadingCosteos,
        openCosteoSelector,
        closeCosteoSelector,
        handleSelectCosteo,
        handleOpenForm,
        addItem,
        removeItem,
        updateItem,
        updateSize,
        handleConfirmNV,
        deleteDraftNV,
        resetForm: () => handleOpenForm(),
        totalItems,
        totalAmount
    };
};
