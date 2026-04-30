import { useState } from 'react';

export function useHCState(initialView = 'list') {
    const [view, setView] = useState(initialView);
    const [activeTab, setActiveTab] = useState('all');
    const [selectedHC, setSelectedHC] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [formData, setFormData] = useState({});
    const [isReadOnly, setIsReadOnly] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Mocks iniciales de Hojas de Compra
    const [registros, setRegistros] = useState([
        { 
            id: 'HC-1020', 
            folioNV: 'NV-550', 
            idOP: 'OP-990', 
            cliente: 'Confecciones del Sur', 
            proveedorPrincipal: 'Telas Santiago S.A.',
            status: 'PENDIENTE', 
            fechaCreacion: '12/04/2026',
            items: [
                { id: 1, insumo: 'Tela Mezclilla 12oz', unidad: 'MTS', cantidadRequerida: 500, precioEstimado: 4500, proveedor: 'Telas Santiago' },
                { id: 2, insumo: 'Hilo Poliester 40/2', unidad: 'CONOS', cantidadRequerida: 24, precioEstimado: 1200, proveedor: 'Hilos & Cía' }
            ]
        },
        { 
            id: 'HC-1021', 
            folioNV: 'NV-551', 
            idOP: 'OP-991', 
            cliente: 'Modas Antuan', 
            proveedorPrincipal: 'Botones Pro',
            status: 'COMPRADO', 
            fechaCreacion: '13/04/2026',
            items: [
                { id: 3, insumo: 'Botón Zamak 18mm', unidad: 'GRUESA', cantidadRequerida: 10, precioEstimado: 8500, proveedor: 'Botones Pro' }
            ]
        }
    ]);

    const handleOpenForm = (data, mode) => {
        setFormData(data || { items: [] });
        setIsReadOnly(mode === 'view');
        if (mode === 'view') {
            setSelectedHC(data.id);
            setView('detail');
        } else {
            setView('form');
        }
    };

    const handleAddItem = () => {
        const newItem = { id: Date.now(), insumo: '', unidad: 'MTS', cantidadRequerida: 0, precioEstimado: 0, proveedor: '' };
        setFormData({ ...formData, items: [...(formData.items || []), newItem] });
    };

    const handleRemoveItem = (id) => {
        setFormData({ ...formData, items: formData.items.filter(item => item.id !== id) });
    };

    const handleUpdateItem = (id, field, value) => {
        const updatedItems = formData.items.map(item => 
            item.id === id ? { ...item, [field]: value } : item
        );
        setFormData({ ...formData, items: updatedItems });
    };

    const handleSave = () => {
        setIsSubmitting(true);
        setTimeout(() => {
            const isNew = !formData.id;
            if (isNew) {
                const newHC = {
                    ...formData,
                    id: `HC-${Math.floor(Math.random() * 9000) + 1000}`,
                    status: 'PENDIENTE',
                    fechaCreacion: new Date().toLocaleDateString('es-CL')
                };
                setRegistros([newHC, ...registros]);
            } else {
                setRegistros(registros.map(r => r.id === formData.id ? formData : r));
            }
            setIsSubmitting(false);
            setView('list');
            setFormData({});
        }, 800);
    };

    const formatCLP = (value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0
        }).format(value);
    };

    const calculations = {
        totalItems: (formData.items || []).length,
        totalBudget: (formData.items || []).reduce((acc, curr) => acc + (parseFloat(curr.cantidadRequerida || 0) * parseFloat(curr.precioEstimado || 0)), 0),
        totalUnits: (formData.items || []).reduce((acc, curr) => acc + parseFloat(curr.cantidadRequerida || 0), 0)
    };

    return {
        view, setView,
        activeTab, setActiveTab,
        selectedHC, setSelectedHC,
        searchTerm, setSearchTerm,
        registros,
        formData, setFormData,
        isReadOnly,
        isSubmitting,
        handleOpenForm,
        addItem: handleAddItem,
        removeItem: handleRemoveItem,
        updateItem: handleUpdateItem,
        handleSave,
        formatCLP,
        ...calculations
    };
}
