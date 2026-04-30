import { useState, useCallback } from 'react';
import { toast } from 'sonner';
import { mockOperaciones, mockOpDetails, mockNVs } from '../data/mockData';
import { validateNumericInput } from '../utils/validations';

export const useOpRegistroState = () => {
    const [selectedOP, setSelectedOP] = useState(null);
    const [view, setView] = useState('list'); // 'list', 'loading', 'detail', 'bulk_edit'
    const [isSelectionMode, setIsSelectionMode] = useState(false);
    const [selectedOPIds, setSelectedOPIds] = useState([]);
    const [showSelectionModal, setShowSelectionModal] = useState(false);
    const [editingFieldIdx, setEditingFieldIdx] = useState(null);
    const [tempValue, setTempValue] = useState('');
    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [isReadOnly, setIsReadOnly] = useState(false);
    const [isManualCutting, setIsManualCutting] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const opFields = [
        { key: 'recepcionOP', title: 'Recepción OP', type: 'date', hasData: true },
        { key: 'finTizado', title: 'Fin de Tizado', type: 'date' },
        { key: 'estadoOcMP', title: 'Estado emisión OC MP', type: 'select', options: ['sin tela en mercado', 'OC emitida', 'tela en stock', 'en stock y OC emitida', 'N/A'] },
        { key: 'recepcionCompra', title: 'Recepc. de Compra', type: 'date' },
        { key: 'inicioCorte', title: 'Inicio Corte', type: 'date' },
        { key: 'cantidadCortes', title: 'Cantidad de cortes', type: 'calculated_number' },
        { key: 'finCorte', title: 'Fin Corte', type: 'date' },
        { key: 'inicioLogo', title: 'Inicio Logo', type: 'date' },
        { key: 'regresoLogo', title: 'Regreso Logo', type: 'date' },
        { key: 'estadoRecLogo', title: 'Estado Rec. Logo', type: 'select', options: ['Recep. completa', 'Recep. parcial', 'N/A'] },
        { key: 'inicioTaller', title: 'Inicio Taller Ext.', type: 'date' },
        { key: 'finTaller', title: 'Fin Taller Ext.', type: 'date' },
        { key: 'calidadTaller', title: 'Calidad Taller', type: 'select', options: ['Aprobado', 'Rechazado', 'con Obs.'] },
        { key: 'obsTaller', title: 'Obs. Taller', type: 'textarea' },
        { key: 'finPersonalizado', title: 'Fin Personalizado', type: 'date' },
        { key: 'finOP', title: 'Fin Term./Fin OP', type: 'date' },
        { key: 'entregaBodega', title: 'Entrega a Bodega', type: 'date' },
    ];

    const formatDateToBackend = (dateStr) => {
        if (!dateStr) return null;
        const [year, month, day] = dateStr.split('-');
        return `${day}-${month}-${year}`;
    };

    const calculateTotalQty = useCallback((opId) => {
        const op = mockOperaciones.find(o => (o.idOP || o.id) === opId);
        if (!op || !(op.notaVentaId || op.nv_id)) return 0;
        const nv = mockNVs.find(n => (n.idNV || n.id) === (op.notaVentaId || op.nv_id));
        if (!nv || !nv.items) return 0;
        return nv.items.reduce((acc, item) => acc + (item.qty || item.quantity || item.cantidad || 0), 0);
    }, []);

    const handleModificarRegistro = () => {
        setIsReadOnly(false);
        setView('loading');
        setTimeout(() => {
            setView('detail');
        }, 1500);
    };

    const handleVerDetalles = () => {
        setIsReadOnly(true);
        setView('loading');
        setTimeout(() => {
            setView('detail');
        }, 1000);
    };

    const toggleSelection = (id) => {
        setSelectedOPIds(prev =>
            prev.includes(id) ? prev.filter(item => item !== id) : [...prev, id]
        );
    };

    const handleBulkEdit = () => {
        if (selectedOPIds.length > 0) {
            setShowSelectionModal(true);
        }
    };

    const handleSelectFieldInline = (idx) => {
        if (isReadOnly) return;
        setEditingFieldIdx(idx);
        setTempValue('');
    };

    const handleSaveInline = (field) => {
        const hasExistingData = field.hasData;
        if (hasExistingData) {
            setShowConfirmModal(true);
        } else {
            finalizeSave();
        }
    };

    const finalizeSave = () => {
        setIsSubmitting(true);
        const field = opFields[editingFieldIdx];
        const formattedValue = (field.type === 'date')
            ? formatDateToBackend(tempValue)
            : tempValue;

        console.log(`Guardando campo ${field.key} con valor: ${formattedValue}`);

        setShowConfirmModal(false);
        setEditingFieldIdx(null);

        if (view === 'bulk_edit' || showSelectionModal) {
            setIsSelectionMode(false);
            setSelectedOPIds([]);
            setShowSelectionModal(false);
            setView('loading');
            setTimeout(() => {
                setView('list');
                setIsSubmitting(false);
            }, 1200);
        } else {
            if (selectedOP) {
                if (!mockOpDetails[selectedOP.id]) mockOpDetails[selectedOP.id] = {};
                mockOpDetails[selectedOP.id][field.key] = formattedValue;
            }
            setIsSubmitting(false);
        }
    };

    return {
        selectedOP, setSelectedOP,
        view, setView,
        isSelectionMode, setIsSelectionMode,
        selectedOPIds, setSelectedOPIds,
        showSelectionModal, setShowSelectionModal,
        editingFieldIdx, setEditingFieldIdx,
        tempValue, setTempValue,
        showConfirmModal, setShowConfirmModal,
        isReadOnly, setIsReadOnly,
        isManualCutting, setIsManualCutting,
        isSubmitting,
        opFields,
        handleModificarRegistro,
        handleVerDetalles,
        toggleSelection,
        handleBulkEdit,
        handleSelectFieldInline,
        handleSaveInline,
        finalizeSave,
        calculateTotalQty,
        mockOperaciones,
        mockOpDetails
    };
};
