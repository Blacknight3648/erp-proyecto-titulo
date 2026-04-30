import { useState } from 'react';
import { usePlantillas } from './usePlantillas';

export const usePlantillasState = () => {
    const { configuraciones, loading, save, remove } = usePlantillas();
    
    // UI Local States
    const [expanded, setExpanded] = useState(null);
    const [adding, setAdding] = useState(false);
    const [newName, setNewName] = useState('');
    
    // Edit States per config (using separate states for performance/simplicity in hooks)
    const [editCampos, setEditCampos] = useState({});
    const [editTelas, setEditTelas] = useState({});
    const [editAccesorios, setEditAccesorios] = useState({});
    const [editCustomFields, setEditCustomFields] = useState({});

    // New Prenda States
    const [newCampos, setNewCampos] = useState(new Set(['nombrePrenda', 'tela', 'composicion', 'color', 'obsModelo']));
    const [newTelas, setNewTelas] = useState([]);
    const [newAccesorios, setNewAccesorios] = useState([]);
    const [newCustomFields, setNewCustomFields] = useState({});

    // Modal State
    const [fieldModal, setFieldModal] = useState({ open: false, isNew: true, configId: null, fieldName: "" });

    const toggleExpand = (config) => {
        if (expanded === config.id) {
            setExpanded(null);
        } else {
            setExpanded(config.id);
            setEditCampos(prev => ({ ...prev, [config.id]: new Set(config.camposActivos) }));
            setEditTelas(prev => ({ ...prev, [config.id]: config.plantillaTelas || [] }));
            setEditAccesorios(prev => ({ ...prev, [config.id]: config.plantillaAccesorios || [] }));
            
            if (config.customFields) {
                try {
                    const parsed = JSON.parse(config.customFields);
                    setEditCustomFields(prev => ({ ...prev, [config.id]: parsed }));
                } catch (e) {
                    console.error("Error parsing custom fields:", e);
                    setEditCustomFields(prev => ({ ...prev, [config.id]: {} }));
                }
            } else {
                setEditCustomFields(prev => ({ ...prev, [config.id]: {} }));
            }
        }
    };

    const toggleFieldInEdit = (configId, field) => {
        setEditCampos(prev => {
            const s = new Set(prev[configId] || []);
            if (s.has(field)) s.delete(field);
            else s.add(field);
            return { ...prev, [configId]: s };
        });
    };

    const toggleFieldInNew = (field) => {
        setNewCampos(prev => {
            const s = new Set(prev);
            if (s.has(field)) s.delete(field);
            else s.add(field);
            return s;
        });
    };

    const handleSaveEdit = async (config) => {
        const customString = JSON.stringify(editCustomFields[config.id] || {});
        const success = await save({ 
            id: config.id, 
            nombrePrenda: config.nombrePrenda, 
            camposActivos: Array.from(editCampos[config.id]), 
            customFields: customString !== '{}' ? customString : null,
            plantillaTelas: editTelas[config.id],
            plantillaAccesorios: editAccesorios[config.id]
        });
        if (success) setExpanded(null);
    };

    const handleCreate = async () => {
        if (!newName.trim()) return;
        const customString = JSON.stringify(newCustomFields);
        const success = await save({ 
            nombrePrenda: newName.trim(), 
            camposActivos: Array.from(newCampos),
            customFields: customString !== '{}' ? customString : null,
            plantillaTelas: newTelas,
            plantillaAccesorios: newAccesorios
        });
        if (success) {
            setNewName('');
            setNewCampos(new Set(['nombrePrenda', 'tela', 'composicion', 'color', 'obsModelo']));
            setNewTelas([]);
            setNewAccesorios([]);
            setNewCustomFields({});
            setAdding(false);
        }
    };

    // Generic Handlers for Tables
    const updateTableItem = (isNew, configId, type, index, field, value) => {
        const setter = type === 'tela' ? (isNew ? setNewTelas : setEditTelas) : (isNew ? setNewAccesorios : setEditAccesorios);
        
        if (isNew) {
            setter(prev => prev.map((item, i) => i === index ? { ...item, [field]: value } : item));
        } else {
            setter(prev => ({
                ...prev,
                [configId]: prev[configId].map((item, i) => i === index ? { ...item, [field]: value } : item)
            }));
        }
    };

    const addTableItem = (isNew, configId, type) => {
        const item = type === 'tela' 
            ? { aplicacion: '', nombre: '', composicion: '', color: '', peso: 0 }
            : { tipo: '', nombreAccesorio: '', cantidad: 0 };
            
        const setter = type === 'tela' ? (isNew ? setNewTelas : setEditTelas) : (isNew ? setNewAccesorios : setEditAccesorios);
        
        if (isNew) {
            setter(prev => [...prev, item]);
        } else {
            setter(prev => ({ ...prev, [configId]: [...(prev[configId] || []), item] }));
        }
    };

    const removeTableItem = (isNew, configId, type, index) => {
        const setter = type === 'tela' ? (isNew ? setNewTelas : setEditTelas) : (isNew ? setNewAccesorios : setEditAccesorios);
        if (isNew) {
            setter(prev => prev.filter((_, i) => i !== index));
        } else {
            setter(prev => ({ ...prev, [configId]: prev[configId].filter((_, i) => i !== index) }));
        }
    };

    // Custom Fields Logic
    const handleAddField = () => {
        const label = fieldModal.fieldName.trim();
        if (!label) return;
        const key = `cf_${Date.now()}`;
        
        if (fieldModal.isNew) {
            setNewCustomFields(prev => ({ ...prev, [key]: { label } }));
            toggleFieldInNew(key);
        } else {
            setEditCustomFields(prev => ({
                ...prev,
                [fieldModal.configId]: { ...prev[fieldModal.configId], [key]: { label } }
            }));
            toggleFieldInEdit(fieldModal.configId, key);
        }
        setFieldModal({ ...fieldModal, open: false, fieldName: "" });
    };

    const removeCustomField = (isNew, configId, key) => {
        if (isNew) {
            setNewCustomFields(prev => { const n = { ...prev }; delete n[key]; return n; });
            setNewCampos(prev => { const s = new Set(prev); s.delete(key); return s; });
        } else {
            setEditCustomFields(prev => { 
                const n = { ...prev[configId] }; 
                delete n[key]; 
                return { ...prev, [configId]: n }; 
            });
            setEditCampos(prev => { 
                const s = new Set(prev[configId]); 
                s.delete(key); 
                return { ...prev, [configId]: s }; 
            });
        }
    };

    return {
        configuraciones, loading, remove,
        expanded, toggleExpand,
        adding, setAdding,
        newName, setNewName,
        editCampos, editTelas, editAccesorios, editCustomFields,
        newCampos, newTelas, newAccesorios, newCustomFields,
        fieldModal, setFieldModal,
        toggleFieldInEdit, toggleFieldInNew,
        handleSaveEdit, handleCreate,
        updateTableItem, addTableItem, removeTableItem,
        handleAddField, removeCustomField
    };
};
