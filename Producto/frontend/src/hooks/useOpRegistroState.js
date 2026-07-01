import { useState, useCallback, useEffect } from 'react';
import { toast } from 'sonner';
import { mockNVs } from '../data/mockData';
import { validateNumericInput } from '../utils/validations';
import { OrdenProduccionService } from '../remote/service/OrdenProduccionService';

// Mapeo de field.key (frontend) → nombre del campo en ActualizarSeguimientoCommand (backend)
const FIELD_KEY_TO_BACKEND = {
    recepcionOP:    'fechaRecepcionOp',
    finTizado:      'finTizado',
    recepcionCompra: 'recepcionCompras',
    inicioCorte:    'inicioCorte',
    finCorte:       'finCorte',
    inicioLogo:     'inicioLogo',
    regresoLogo:    'regresoLogo',
    estadoRecLogo:  'estadoRecLogo',
    finTaller:      'finTallerExterno',
    calidadTaller:  'calidadTaller',
    obsTaller:      'obsTaller',
    finPersonalizado: 'finPersonalizado',
    finOP:          'finTerminacion',
    // estadoOcMP, cantidadCortes, inicioTaller, entregaBodega — sin equivalente en backend, se omiten
};

// Construye el payload completo para ActualizarSeguimientoCommand a partir del DTO actual
// aplicando el nuevo valor para el campo editado.
const buildPayload = (seguimientoDTO, fieldKey, value) => {
    const backendKey = FIELD_KEY_TO_BACKEND[fieldKey];
    const base = {
        fechaRecepcionOp:  seguimientoDTO?.fechaRecepcionOp  ?? null,
        finTizado:         seguimientoDTO?.finTizado          ?? null,
        recepcionCompras:  seguimientoDTO?.recepcionCompras   ?? null,
        inicioCorte:       seguimientoDTO?.inicioCorte        ?? null,
        finCorte:          seguimientoDTO?.finCorte           ?? null,
        inicioLogo:        seguimientoDTO?.inicioLogo         ?? null,
        estadoIdaLogo:     seguimientoDTO?.estadoIdaLogo      ?? null,
        regresoLogo:       seguimientoDTO?.regresoLogo        ?? null,
        estadoRecLogo:     seguimientoDTO?.estadoRecLogo      ?? null,
        finTallerExterno:  seguimientoDTO?.finTallerExterno   ?? null,
        calidadTaller:     seguimientoDTO?.calidadTaller      ?? null,
        obsTaller:         seguimientoDTO?.obsTaller          ?? null,
        finTerminacion:    seguimientoDTO?.finTerminacion     ?? null,
        finPersonalizado:  seguimientoDTO?.finPersonalizado   ?? null,
    };
    if (backendKey) {
        base[backendKey] = value;
    }
    return base;
};

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
    const [ordenes, setOrdenes] = useState([]);
    const [isLoadingOrdenes, setIsLoadingOrdenes] = useState(true);
    // Map opId → SeguimientoOPDTO; permite construir payload completo al guardar
    const [seguimientos, setSeguimientos] = useState({});

    useEffect(() => {
        let active = true;
        setIsLoadingOrdenes(true);
        OrdenProduccionService.getAll()
            .then(async (ops) => {
                const conSeguimiento = await Promise.all(
                    ops.map(async (op) => {
                        try {
                            const dto = await OrdenProduccionService.getSeguimiento(op.idOP);
                            return { op: { ...op, id: op.idOP, progreso: dto?.porcentajeAvance ?? 0 }, dto };
                        } catch {
                            return { op: { ...op, id: op.idOP, progreso: 0 }, dto: null };
                        }
                    })
                );
                if (!active) return;
                const nuevasOrdenes = conSeguimiento.map(r => r.op);
                const nuevosSeguimientos = {};
                conSeguimiento.forEach(r => {
                    if (r.dto) nuevosSeguimientos[r.op.id] = r.dto;
                });
                setOrdenes(nuevasOrdenes);
                setSeguimientos(nuevosSeguimientos);
            })
            .catch((err) => {
                console.error('Error fetching Ordenes de Producción:', err);
            })
            .finally(() => {
                if (active) setIsLoadingOrdenes(false);
            });
        return () => { active = false; };
    }, []);

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
        const op = ordenes.find(o => o.id === opId);
        if (op?.items?.length) {
            return op.items.reduce((acc, item) => acc + (item.cantidad || 0), 0);
        }
        if (!op || !op.notaVentaId) return 0;
        const nv = mockNVs.find(n => (n.idNV || n.id) === op.notaVentaId);
        if (!nv || !nv.items) return 0;
        return nv.items.reduce((acc, item) => acc + (item.qty || item.quantity || item.cantidad || 0), 0);
    }, [ordenes]);

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

    const finalizeSave = async () => {
        setIsSubmitting(true);
        const field = opFields[editingFieldIdx];
        const formattedValue = (field.type === 'date')
            ? formatDateToBackend(tempValue)
            : tempValue;

        setShowConfirmModal(false);
        setEditingFieldIdx(null);

        if (view === 'bulk_edit' || showSelectionModal) {
            // Guardar el campo editado en cada OP seleccionada
            const opIdsCopy = [...selectedOPIds];
            setIsSelectionMode(false);
            setSelectedOPIds([]);
            setShowSelectionModal(false);
            setView('loading');

            const results = await Promise.allSettled(
                opIdsCopy.map(async (opId) => {
                    const payload = buildPayload(seguimientos[opId], field.key, formattedValue);
                    const dto = await OrdenProduccionService.actualizarSeguimiento(opId, payload);
                    return { opId, dto };
                })
            );

            const exitosas = results.filter(r => r.status === 'fulfilled').map(r => r.value);
            const fallidas = results.filter(r => r.status === 'rejected').length;

            if (exitosas.length > 0) {
                setOrdenes(prev => prev.map(op => {
                    const res = exitosas.find(e => e.opId === op.id);
                    return res ? { ...op, progreso: res.dto.porcentajeAvance } : op;
                }));
                setSeguimientos(prev => {
                    const next = { ...prev };
                    exitosas.forEach(({ opId, dto }) => { next[opId] = dto; });
                    return next;
                });
            }

            if (fallidas > 0) {
                toast.error(`${fallidas} OP(s) no pudieron guardarse.`);
            }
            if (exitosas.length > 0) {
                toast.success(`${exitosas.length} OP(s) actualizadas.`);
            }

            setTimeout(() => {
                setView('list');
                setIsSubmitting(false);
            }, 400);
        } else {
            // Guardado individual
            if (!selectedOP) {
                setIsSubmitting(false);
                return;
            }

            // Si el campo no tiene equivalente en backend, simplemente cerramos sin llamar al backend
            const backendKey = FIELD_KEY_TO_BACKEND[field.key];
            if (!backendKey) {
                setIsSubmitting(false);
                return;
            }

            try {
                const payload = buildPayload(seguimientos[selectedOP.id], field.key, formattedValue);
                const dto = await OrdenProduccionService.actualizarSeguimiento(selectedOP.id, payload);

                setSeguimientos(prev => ({ ...prev, [selectedOP.id]: dto }));
                setOrdenes(prev => prev.map(op =>
                    op.id === selectedOP.id ? { ...op, progreso: dto.porcentajeAvance } : op
                ));
                toast.success('Campo guardado correctamente.');
            } catch (err) {
                console.error('Error guardando seguimiento:', err);
                toast.error('No se pudo guardar el campo. Intente nuevamente.');
            } finally {
                setIsSubmitting(false);
            }
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
        ordenes,
        isLoadingOrdenes,
        seguimientos,
    };
};
