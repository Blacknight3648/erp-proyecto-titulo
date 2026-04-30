import { useState } from 'react';

export function useOPState(initialView = 'list') {
    const [view, setView] = useState(initialView);
    const [activeTab, setActiveTab] = useState('all');
    const [selectedOP, setSelectedOP] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [formData, setFormData] = useState({});
    const [isReadOnly, setIsReadOnly] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitStatus, setSubmitStatus] = useState(null);

    const [registros, setRegistros] = useState([
        { id: 'OP-001', cliente: 'Cliente de Prueba A', status: 'Pendiente', idOP: 'OP-001' },
        { id: 'OP-002', cliente: 'Cliente de Prueba B', status: 'En Proceso', idOP: 'OP-002' }
    ]);

    const clientes = [
        { id: 1, nombre: 'Empresa Textil Alfa' },
        { id: 2, nombre: 'Modas Beta S.A.' }
    ];

    const [evnModal, setEvnModal] = useState(false);

    const handleOpenForm = (data, mode) => {
        setFormData(data || {});
        setIsReadOnly(mode === 'view');
        if (mode === 'view') {
            setSelectedOP(data.id || data.idOP);
            setView('detail');
        } else {
            setView('form');
        }
    };

    const handleAddItem = () => {
        const newItem = { nombreProducto: '', talla: '', color: '', cantidad: 0 };
        const currentItems = formData.items || [];
        setFormData({ ...formData, items: [...currentItems, newItem] });
    };

    const handleRemoveItem = (index) => {
        const currentItems = formData.items || [];
        setFormData({ ...formData, items: currentItems.filter((_, i) => i !== index) });
    };

    const handleUpdateItem = (index, field, value) => {
        const currentItems = [...(formData.items || [])];
        currentItems[index] = { ...currentItems[index], [field]: value };
        setFormData({ ...formData, items: currentItems });
    };

    const formatCLP = (value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0
        }).format(value);
    };

    const handleSave = () => {
        setIsSubmitting(true);
        setTimeout(() => {
            const isNew = !formData.id && !formData.idOP;
            if (isNew) {
                const newRecord = { 
                    ...formData, 
                    id: 'OP-' + Math.floor(Math.random() * 1000), 
                    idOP: 'OP-MOCK',
                    status: 'PENDIENTE',
                    fechaInicio: new Date().toLocaleDateString('es-CL')
                };
                setRegistros([newRecord, ...registros]);
            } else {
                setRegistros(registros.map(r => (r.id === formData.id || r.idOP === formData.idOP) ? formData : r));
            }
            setIsSubmitting(false);
            setView('list');
            setFormData({});
        }, 1000);
    };

    // Motor de Simulación: Cálculo de totales derivado del estado
    const calculations = {
        totalUnits: (formData.items || []).reduce((acc, curr) => acc + (parseInt(curr.cantidad) || 0), 0),
        totalAmount: (formData.items || []).reduce((acc, curr) => acc + ((parseInt(curr.cantidad) || 0) * (parseInt(curr.precioEstimado) || 0)), 0),
    };

    return {
        view: view,
        setView: setView,
        activeTab: activeTab,
        setActiveTab: setActiveTab,
        selectedOP: selectedOP,
        setSelectedOP: setSelectedOP,
        searchTerm: searchTerm,
        setSearchTerm: setSearchTerm,
        loadingClientes: false,
        clientes: clientes,
        vendedores: [],
        proveedores: [],
        registros: registros,
        evaluaciones: [],
        loadingComercial: false,
        formData: formData,
        setFormData: setFormData,
        isReadOnly: isReadOnly,
        isSubmitting: isSubmitting,
        submitStatus: submitStatus,
        sourceEVN: null,
        nextNumbers: { op: 'OP-003' },
        evnModal: evnModal,
        setEvnModal: setEvnModal,
        handleOpenForm: handleOpenForm,
        addItem: handleAddItem,
        removeItem: handleRemoveItem,
        updateItem: handleUpdateItem,
        handleSave: handleSave,
        formatCLP: formatCLP,
        updateSize: () => { },
        handleConfirmNV: () => { },
        totalItems: calculations.totalUnits,
        totalAmount: calculations.totalAmount
    };
}