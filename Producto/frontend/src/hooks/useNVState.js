import { useState, useEffect, useMemo } from 'react';
import { api } from '../remote/service/api';
import { useVendedores } from './useVendedores';
import { useProveedores } from './useProveedores';
import { useClientes } from './useClientes';
import { useComercial } from './useComercial';
import { toast } from 'sonner';
import { validateNumericInput } from '../utils/validations';

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

    const handleOpenForm = (data = null, mode = 'new') => {
        setSubmitStatus(null);
        setSourceEVN(null);

        if (mode === 'view') {
            setFormData({
                ...data,
                vendedorId: data.vendedorId || '',
                fechaEntregaEstimada: data.fechaEntregaEstimada || ''
            });
            setIsReadOnly(true);
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
                    detalleOt: item.detalleOt || ''
                }))
            });
            setSourceEVN(data.evaluacionNegocioId || data.id);
            setIsReadOnly(false);
            setView('form');
        } else {
            setFormData({
                clienteId: '',
                clienteNombre: '',
                vendedorId: '',
                fechaEntregaEstimada: '',
                esKit: false,
                detalleKit: '',
                items: []
            });
            setIsReadOnly(false);
            setView('form');
        }
    };

    const addItem = () => {
        if (isReadOnly) return;
        const newItem = {
            id: Date.now(),
            productoId: '',
            nombreProducto: '',
            modelo: '',
            tela: '',
            composicion: '',
            color: '',
            genero: 'Unisex',
            talla: '',
            sizes: { XS: 0, S: 0, M: 0, L: 0, XL: 0 },
            quantity: 0,
            unitPrice: 0,
            tipoItem: 'OP',
            logo: 'N/A',
            proveedorId: '',
            requiereOt: false,
            detalleOt: ''
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

    const handleConfirmNV = async () => {
        if (!formData.clienteId) {
            toast.error('Debe seleccionar un cliente');
            return;
        }
        if (formData.items.length === 0) {
            toast.error('Debe añadir al menos un ítem');
            return;
        }

        setIsSubmitting(true);
        setSubmitStatus(null);
        try {
            const payload = {
                clienteId: parseInt(formData.clienteId),
                vendedorId: parseInt(formData.vendedorId) || 1,
                esKit: formData.esKit,
                detalleKit: formData.detalleKit,
                fechaEntregaEstimada: formData.fechaEntregaEstimada,
                evaluacionNegocioId: sourceEVN || null,
                items: formData.items.map(item => ({
                    productoId: item.productoId ? parseInt(item.productoId) : null,
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
                    tipoItem: item.tipoItem,
                    requiereOt: item.requiereOt,
                    detalleOt: item.detalleOt,
                    logoDetalle: item.logo || 'N/A'
                }))
            };
            if (isReadOnly && formData.idNV) {
                await api.put(`/comercial/notas-venta/${formData.idNV}`, payload);
            } else {
                await api.post('/comercial/notas-venta', payload);
            }
            setSubmitStatus({ type: 'success', message: 'Operación exitosa' });
            setTimeout(() => { setView('list'); loadComercial(); }, 2000);
        } catch (error) {
            const msg = error.response?.data?.message || error.message || 'Error desconocido';
            setSubmitStatus({ type: 'error', message: msg });
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
        handleOpenForm,
        addItem,
        removeItem,
        updateItem,
        updateSize,
        handleConfirmNV,
        totalItems,
        totalAmount
    };
};
