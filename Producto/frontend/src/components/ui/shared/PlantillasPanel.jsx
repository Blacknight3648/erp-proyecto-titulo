import { ChevronDown, ChevronUp, Settings2, X, Plus, Search } from 'lucide-react';
import { useState, useEffect } from 'react';
import { usePlantillas, FIELD_LABELS, FIELD_DESCRIPTIONS, FIELD_CATALOGS, ALL_FIELDS } from '../../../hooks/usePlantillas';
import { toast } from 'sonner';

export default function PlantillasPanel({ title = "Especificaciones Técnicas", data, onUpdate, articuloDescripcion = "", readOnly = false, telas = [], accesorios = [] }) {
    const [isExpanded, setIsExpanded] = useState(true);
    const [showFieldSelector, setShowFieldSelector] = useState(false);
    const [activeFields, setActiveFields] = useState(new Set(ALL_FIELDS));
    const [activeLinkingField, setActiveLinkingField] = useState(null);
    const [customLabels, setCustomLabels] = useState({});
    const [linkQuantities, setLinkQuantities] = useState({});
    const [showNuevoCampoModal, setShowNuevoCampoModal] = useState(false);
    const [nuevoCampoLabel, setNuevoCampoLabel] = useState('');
    const [openCombo, setOpenCombo] = useState(null);
    const [comboSearch, setComboSearch] = useState('');

    const { getCamposForArticulo, loading } = usePlantillas();

    const spec = (data && data.length > 0) ? data[0] : {};
    const specId = spec.id || 'current-spec';
    const vinculos = spec.vinculos || [];

    useEffect(() => {
        if (spec.camposActivos && spec.camposActivos.length > 0) {
            setActiveFields(new Set(spec.camposActivos));
        } else {
            const fetchFields = async () => {
                const fields = await getCamposForArticulo(articuloDescripcion);
                setActiveFields(fields);
            };
            fetchFields();
        }
    }, [articuloDescripcion, getCamposForArticulo, spec.camposActivos]);

    const TOP_LEVEL_FIELDS = new Set(['vinculos', 'camposActivos', 'camposPersonalizados', 'nombre', 'descripcion', 'nombrePrenda', 'genero']);

    const handleChange = (field, value) => {
        if (readOnly) return;
        if (TOP_LEVEL_FIELDS.has(field)) {
            onUpdate(specId, field, value);
        } else {
            const next = { ...(spec.detallesPrenda || {}), [field]: value };
            onUpdate(specId, 'detallesPrenda', next);
        }
    };

    const handleAddVinculo = (fieldKey, type, materialId, qty = 1) => {
        if (readOnly) return;
        const amount = parseInt(qty);
        if (isNaN(amount) || amount <= 0) { toast.error("Ingrese una cantidad válida"); return; }

        if (type === 'ACCESORIO') {
            const material = accesorios.find(a => a.id === materialId);
            const alreadyUsed = vinculos.filter(v => v.materialId === materialId).reduce((sum, v) => sum + (v.cantidad || 0), 0);
            if (material && (alreadyUsed + amount) > material.cantidad) {
                toast.error(`Disponibilidad insuficiente. Quedan: ${material.cantidad - alreadyUsed}`);
                return;
            }
        }

        const newVinculo = { id: crypto.randomUUID(), fieldName: fieldKey, materialType: type, materialId, cantidad: amount };
        handleChange('vinculos', [...vinculos, newVinculo]);
        setLinkQuantities(prev => { const n = { ...prev }; delete n[materialId]; return n; });
        setActiveLinkingField(null);
        toast.success("Material vinculado");
    };

    const handleRemoveVinculo = (vinculoId) => {
        if (readOnly) return;
        handleChange('vinculos', vinculos.filter(v => v.id !== vinculoId));
    };

    useEffect(() => {
        if (spec.camposPersonalizados && typeof spec.camposPersonalizados === 'object') {
            setCustomLabels({ ...spec.camposPersonalizados });
        } else {
            setCustomLabels({});
        }
    }, [spec.camposPersonalizados]);

    const handleAddField = () => { setNuevoCampoLabel(''); setShowNuevoCampoModal(true); };

    const handleConfirmNuevoCampo = () => {
        if (!nuevoCampoLabel.trim()) return;
        const key = `cf_${Date.now()}`;
        const trimmedLabel = nuevoCampoLabel.trim();
        const newLabels = { ...customLabels, [key]: trimmedLabel };
        setCustomLabels(newLabels);
        const newActive = new Set(activeFields);
        newActive.add(key);
        setActiveFields(newActive);
        handleChange('camposPersonalizados', newLabels);
        handleChange('camposActivos', Array.from(newActive));
        toast.success(`Campo "${trimmedLabel}" agregado`);
        setShowNuevoCampoModal(false);
        setNuevoCampoLabel('');
    };

    const toggleField = (key) => {
        if (readOnly) return;
        const newFields = new Set(activeFields);
        if (newFields.has(key)) { newFields.delete(key); } else { newFields.add(key); }
        setActiveFields(newFields);
        handleChange('camposActivos', Array.from(newFields));
    };

    const openComboFor = (fieldKey, currentValue) => {
        setOpenCombo(fieldKey);
        setComboSearch(currentValue || '');
    };

    const selectComboOption = (fieldKey, option) => {
        handleChange(fieldKey, option.toUpperCase());
        setOpenCombo(null);
        setComboSearch('');
    };

    const createComboOption = (fieldKey) => {
        if (!comboSearch.trim()) return;
        handleChange(fieldKey, comboSearch.trim().toUpperCase());
        setOpenCombo(null);
        setComboSearch('');
    };

    const renderField = (fieldKey, label, isCustom = false) => {
        if (!activeFields || !activeFields.has(fieldKey)) return null;

        const description = !isCustom ? (FIELD_DESCRIPTIONS[fieldKey] || '') : '';
        const options = !isCustom ? (FIELD_CATALOGS[fieldKey] || []) : [];
        const isComboField = options.length > 0;
        const isLongText = fieldKey === 'obsModelo';
        const isOpen = openCombo === fieldKey;
        const value = spec.detallesPrenda?.[fieldKey] ?? '';
        const fieldVinculos = vinculos.filter(v => v.fieldName === fieldKey);

        const filteredOptions = isOpen
            ? options.filter(opt => opt.toLowerCase().includes(comboSearch.toLowerCase()))
            : [];

        const showCreateOption = isOpen
            && comboSearch.trim().length > 0
            && !options.some(opt => opt.toLowerCase() === comboSearch.trim().toLowerCase());

        return (
            <div key={fieldKey} className="flex flex-col gap-2">
                {/* Label + description */}
                <div className="flex items-start justify-between gap-2">
                    <div className="flex-1 min-w-0">
                        <span className="block text-[9px] font-black text-gray-500 uppercase tracking-widest">
                            {isCustom ? `[Personalizado] ${label}` : label}
                        </span>
                        {description && (
                            <span className="block text-[10px] text-gray-400 leading-snug mt-0.5">{description}</span>
                        )}
                    </div>

                    <div className="flex items-center gap-1.5 flex-shrink-0 mt-0.5">
                        {fieldVinculos.length > 0 && (
                            <span className="text-[8px] font-black text-blue-500 bg-blue-50 px-2 py-0.5 rounded border border-blue-100">
                                {fieldVinculos.length} VÍN.
                            </span>
                        )}
                        {!readOnly && (
                            <button
                                type="button"
                                onClick={() => setActiveLinkingField(activeLinkingField === fieldKey ? null : fieldKey)}
                                className={`p-1 rounded border transition-all text-[8px] font-black uppercase tracking-wider flex items-center gap-1 ${
                                    activeLinkingField === fieldKey
                                        ? 'bg-blue-600 text-white border-blue-700'
                                        : 'bg-white text-blue-500 border-blue-200 hover:bg-blue-50'
                                }`}
                                title="Vincular material"
                            >
                                <Plus className="w-3 h-3" />
                            </button>
                        )}
                    </div>
                </div>

                {/* Input / Combobox */}
                <div className="relative">
                    {isComboField && !readOnly ? (
                        <>
                            {isOpen && (
                                <div
                                    className="fixed inset-0 z-[60]"
                                    onClick={() => { setOpenCombo(null); setComboSearch(''); }}
                                />
                            )}

                            {/* Trigger */}
                            <button
                                type="button"
                                onClick={() => isOpen ? (setOpenCombo(null), setComboSearch('')) : openComboFor(fieldKey, value)}
                                className={`w-full flex items-center justify-between gap-2 px-3 py-2.5 rounded-lg border text-left transition-all text-xs font-semibold uppercase ${
                                    value
                                        ? 'bg-white border-gray-200 text-gray-800 hover:border-blue-300'
                                        : 'bg-gray-50 border-gray-200 text-gray-400 hover:border-blue-300'
                                } ${isOpen ? 'border-blue-400 ring-2 ring-blue-50' : ''}`}
                            >
                                <span className="truncate">{value || 'Seleccionar...'}</span>
                                <ChevronDown className={`w-3.5 h-3.5 flex-shrink-0 text-gray-400 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
                            </button>

                            {/* Dropdown */}
                            {isOpen && (
                                <div className="absolute top-full left-0 right-0 mt-1 bg-white border border-gray-200 rounded-lg shadow-lg z-[61] overflow-hidden">
                                    {/* Search */}
                                    <div className="p-2 border-b border-gray-100">
                                        <div className="flex items-center gap-2 px-2 py-1.5 bg-gray-50 rounded border border-gray-200">
                                            <Search className="w-3 h-3 text-gray-400 flex-shrink-0" />
                                            <input
                                                autoFocus
                                                type="text"
                                                value={comboSearch}
                                                onChange={(e) => setComboSearch(e.target.value)}
                                                onKeyDown={(e) => {
                                                    if (e.key === 'Escape') { setOpenCombo(null); setComboSearch(''); }
                                                    if (e.key === 'Enter' && showCreateOption) createComboOption(fieldKey);
                                                    if (e.key === 'Enter' && filteredOptions.length === 1) selectComboOption(fieldKey, filteredOptions[0]);
                                                }}
                                                placeholder="Buscar o escribir..."
                                                className="flex-1 bg-transparent text-xs text-gray-700 outline-none"
                                            />
                                            {comboSearch && (
                                                <button onClick={() => setComboSearch('')} className="text-gray-400 hover:text-gray-600">
                                                    <X className="w-3 h-3" />
                                                </button>
                                            )}
                                        </div>
                                    </div>

                                    {/* Options list */}
                                    <div className="max-h-48 overflow-y-auto">
                                        {filteredOptions.length > 0 ? (
                                            filteredOptions.map((opt) => (
                                                <button
                                                    key={opt}
                                                    type="button"
                                                    onClick={() => selectComboOption(fieldKey, opt)}
                                                    className={`w-full text-left px-3 py-2 text-[11px] font-medium text-gray-700 hover:bg-blue-50 hover:text-blue-700 transition-colors ${
                                                        value.toLowerCase() === opt.toLowerCase() ? 'bg-blue-50 text-blue-700 font-bold' : ''
                                                    }`}
                                                >
                                                    {opt}
                                                </button>
                                            ))
                                        ) : (
                                            !showCreateOption && (
                                                <p className="px-3 py-4 text-[10px] text-gray-400 text-center">Sin resultados</p>
                                            )
                                        )}

                                        {showCreateOption && (
                                            <button
                                                type="button"
                                                onClick={() => createComboOption(fieldKey)}
                                                className="w-full text-left px-3 py-2 text-[11px] font-bold text-gray-900 bg-gray-50 border-t border-gray-100 hover:bg-gray-100 transition-colors flex items-center gap-2"
                                            >
                                                <Plus className="w-3 h-3 text-gray-500" />
                                                Crear: <span className="text-blue-600 uppercase">{comboSearch.trim()}</span>
                                            </button>
                                        )}
                                    </div>

                                    {value && (
                                        <div className="border-t border-gray-100 p-1.5">
                                            <button
                                                type="button"
                                                onClick={() => { handleChange(fieldKey, ''); setOpenCombo(null); setComboSearch(''); }}
                                                className="w-full text-center text-[10px] text-gray-400 hover:text-red-500 py-1 rounded transition-colors"
                                            >
                                                Limpiar selección
                                            </button>
                                        </div>
                                    )}
                                </div>
                            )}
                        </>
                    ) : isLongText ? (
                        <textarea
                            rows={2}
                            readOnly={readOnly}
                            className={`w-full bg-white border border-gray-200 rounded-lg px-3 py-2.5 text-xs font-medium text-gray-700 uppercase outline-none focus:border-blue-400 focus:ring-2 focus:ring-blue-50 transition-all resize-none ${readOnly ? 'cursor-default bg-gray-50/50' : ''}`}
                            value={value}
                            onChange={(e) => handleChange(fieldKey, e.target.value.toUpperCase())}
                            placeholder={readOnly ? '' : `Observaciones del modelo...`}
                        />
                    ) : (
                        <input
                            type="text"
                            readOnly={readOnly}
                            className={`w-full bg-white border border-gray-200 rounded-lg px-3 py-2.5 text-xs font-medium text-gray-700 uppercase outline-none focus:border-blue-400 focus:ring-2 focus:ring-blue-50 transition-all ${readOnly ? 'cursor-default bg-gray-50/50' : ''}`}
                            value={value}
                            onChange={(e) => handleChange(fieldKey, e.target.value.toUpperCase())}
                            placeholder={readOnly ? '' : `Definir ${label.toLowerCase()}...`}
                        />
                    )}

                    {/* Link selector */}
                    {activeLinkingField === fieldKey && (
                        <div className="absolute top-full left-0 right-0 mt-1 p-4 bg-white rounded-lg border border-blue-300 shadow-xl z-50 animate-in fade-in zoom-in-95 duration-150">
                            <div className="flex justify-between items-center mb-3">
                                <span className="text-[9px] font-black uppercase text-gray-700 tracking-wider">Vincular material: {label}</span>
                                <button onClick={() => setActiveLinkingField(null)}><X className="w-3.5 h-3.5 text-gray-400 hover:text-gray-600" /></button>
                            </div>
                            <div className="space-y-3">
                                <div>
                                    <p className="text-[8px] font-black text-gray-400 uppercase mb-1.5">Telas</p>
                                    <div className="max-h-28 overflow-y-auto space-y-1 pr-1">
                                        {telas.map(t => (
                                            <button key={t.id} onClick={() => handleAddVinculo(fieldKey, 'TELA', t.id)}
                                                className="w-full text-left px-2 py-1.5 rounded border border-transparent hover:border-blue-100 hover:bg-blue-50 transition-all flex justify-between items-center group">
                                                <span className="text-[10px] font-medium text-gray-600 group-hover:text-blue-700 truncate max-w-[200px]">{t.nombre}</span>
                                                <Plus className="w-2.5 h-2.5 opacity-0 group-hover:opacity-100 text-blue-500" />
                                            </button>
                                        ))}
                                        {telas.length === 0 && <p className="text-[9px] text-gray-300 px-2">Sin telas registradas</p>}
                                    </div>
                                </div>
                                <div className="border-t border-gray-100 pt-3">
                                    <p className="text-[8px] font-black text-gray-400 uppercase mb-1.5">Accesorios y Avíos</p>
                                    <div className="max-h-48 overflow-y-auto space-y-2 pr-1">
                                        {accesorios.map(a => {
                                            const totalInInventory = a.cantidad || 0;
                                            const alreadyLinked = vinculos.filter(v => v.materialId === a.id).reduce((sum, v) => sum + (v.cantidad || 0), 0);
                                            const currentInput = linkQuantities[a.id] || 0;
                                            const remaining = totalInInventory - alreadyLinked - currentInput;
                                            return (
                                                <div key={a.id} className="bg-gray-50 p-2.5 rounded border border-gray-100 hover:border-gray-200 transition-all">
                                                    <div className="flex justify-between items-start mb-2">
                                                        <div>
                                                            <span className="text-[10px] font-bold text-gray-700 block">{a.nombreAccesorio}</span>
                                                            <span className="text-[8px] font-medium text-gray-400 uppercase">{a.tipo} · {totalInInventory} total</span>
                                                        </div>
                                                        <span className={`text-[8px] font-bold px-1.5 py-0.5 rounded ${remaining < 0 ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-600'}`}>
                                                            Quedan: {remaining}
                                                        </span>
                                                    </div>
                                                    <div className="flex items-center gap-2">
                                                        <input
                                                            type="number" min="1" placeholder="Cant."
                                                            value={linkQuantities[a.id] || ""}
                                                            onChange={(e) => setLinkQuantities(prev => ({ ...prev, [a.id]: parseInt(e.target.value) || 0 }))}
                                                            className="flex-1 bg-white border border-gray-200 rounded px-2 py-1 text-[10px] font-bold text-blue-600 outline-none focus:border-blue-300"
                                                        />
                                                        <button
                                                            onClick={() => handleAddVinculo(fieldKey, 'ACCESORIO', a.id, linkQuantities[a.id] || 0)}
                                                            disabled={currentInput <= 0}
                                                            className={`p-1.5 rounded transition-all ${currentInput > 0 ? 'bg-blue-600 text-white hover:bg-blue-700' : 'bg-gray-100 text-gray-300 cursor-not-allowed'}`}
                                                        >
                                                            <Plus className="w-3.5 h-3.5" />
                                                        </button>
                                                    </div>
                                                </div>
                                            );
                                        })}
                                        {accesorios.length === 0 && <p className="text-[9px] text-gray-300 px-2">Sin accesorios registrados</p>}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}
                </div>

                {/* Vínculos */}
                {fieldVinculos.length > 0 && (
                    <div className="flex flex-wrap gap-1 mt-0.5">
                        {fieldVinculos.map(v => {
                            const material = v.materialType === 'TELA'
                                ? telas.find(t => t.id === v.materialId)
                                : accesorios.find(a => a.id === v.materialId);
                            const name = v.materialType === 'TELA' ? (material?.nombre || 'Tela') : (material?.nombreAccesorio || 'Acc.');
                            return (
                                <div key={v.id} className={`flex items-center gap-1 px-2 py-0.5 rounded text-[8px] font-bold border ${
                                    v.materialType === 'TELA' ? 'bg-blue-50 border-blue-100 text-blue-600' : 'bg-green-50 border-green-100 text-green-600'
                                }`}>
                                    <span className="truncate max-w-[80px]">{name}</span>
                                    {v.cantidad > 1 && <span className="opacity-60">x{v.cantidad}</span>}
                                    {!readOnly && (
                                        <button onClick={() => handleRemoveVinculo(v.id)} className="hover:text-red-500 transition-colors ml-0.5">
                                            <X className="w-2 h-2" />
                                        </button>
                                    )}
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className="space-y-5">
            {/* Header */}
            <div className="flex justify-between items-center pb-4 border-b border-gray-100">
                <div>
                    <div className="flex items-center gap-2.5">
                        <div className="w-1 h-5 bg-gray-800 rounded-sm" />
                        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest">{title}</h4>
                        {loading && <span className="text-[9px] font-bold text-blue-500 animate-pulse bg-blue-50 px-2 py-0.5 rounded border border-blue-100">Cargando...</span>}
                    </div>
                    <p className="text-[10px] text-gray-400 mt-1 ml-3.5">
                        Defina las características constructivas de la prenda. Los campos marcados son obligatorios para la aprobación técnica.
                    </p>
                </div>

                <div className="flex items-center gap-2">
                    {!readOnly && (
                        <button
                            type="button"
                            onClick={() => setShowFieldSelector(!showFieldSelector)}
                            className={`flex items-center gap-1.5 px-3 py-2 rounded-lg text-[9px] font-black uppercase tracking-widest transition-all border ${
                                showFieldSelector
                                    ? 'bg-gray-900 text-white border-gray-900'
                                    : 'bg-white text-gray-500 border-gray-200 hover:border-gray-400 hover:text-gray-700'
                            }`}
                        >
                            <Settings2 className="w-3.5 h-3.5" />
                            {showFieldSelector ? 'Finalizar' : 'Gestionar Campos'}
                        </button>
                    )}
                    <button
                        type="button"
                        onClick={() => setIsExpanded(!isExpanded)}
                        className="p-2 text-gray-400 rounded-lg hover:text-gray-700 hover:bg-gray-50 transition-all border border-gray-200"
                    >
                        {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                    </button>
                </div>
            </div>

            {/* Field selector panel */}
            {showFieldSelector && !readOnly && (
                <div className="bg-gray-50 p-5 rounded-lg border border-gray-200 animate-in fade-in duration-150">
                    <div className="flex justify-between items-center mb-4">
                        <span className="text-[9px] font-black text-gray-500 uppercase tracking-widest">Activar / Desactivar campos</span>
                        <div className="flex gap-2">
                            <button
                                type="button"
                                onClick={handleAddField}
                                className="px-4 py-1.5 bg-gray-900 text-white rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-blue-700 transition-all flex items-center gap-1.5"
                            >
                                <Plus className="w-3 h-3" /> Nuevo Campo
                            </button>
                            <button
                                type="button"
                                onClick={() => { const all = new Set(ALL_FIELDS); setActiveFields(all); handleChange('camposActivos', Array.from(all)); }}
                                className="px-4 py-1.5 bg-white text-gray-500 border border-gray-200 rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-gray-100 transition-all"
                            >
                                Seleccionar todo
                            </button>
                            <button
                                type="button"
                                onClick={() => { setActiveFields(new Set()); handleChange('camposActivos', []); }}
                                className="px-4 py-1.5 bg-white text-gray-500 border border-gray-200 rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-gray-100 transition-all"
                            >
                                Limpiar
                            </button>
                        </div>
                    </div>

                    <div className="flex flex-wrap gap-1.5 max-h-48 overflow-y-auto">
                        {Array.from(ALL_FIELDS).map(f => (
                            <button
                                key={f} type="button" onClick={() => toggleField(f)}
                                className={`px-3 py-1.5 rounded-lg text-[9px] font-black uppercase tracking-widest transition-all border ${
                                    activeFields.has(f)
                                        ? 'bg-gray-900 border-gray-900 text-white'
                                        : 'bg-white border-gray-200 text-gray-400 hover:border-gray-400'
                                }`}
                            >
                                {FIELD_LABELS[f] || f}
                            </button>
                        ))}
                        {Object.keys(customLabels).map(key => (
                            <div key={key} className="flex gap-1">
                                <button
                                    type="button" onClick={() => toggleField(key)}
                                    className={`px-3 py-1.5 rounded-lg text-[9px] font-black uppercase tracking-widest transition-all border ${
                                        activeFields.has(key)
                                            ? 'bg-orange-600 border-orange-600 text-white'
                                            : 'bg-white border-gray-200 text-gray-400 hover:border-orange-300'
                                    }`}
                                >
                                    {customLabels[key]}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => {
                                        const nextActive = new Set(activeFields); nextActive.delete(key);
                                        setActiveFields(nextActive);
                                        handleChange('camposActivos', Array.from(nextActive));
                                        const nextLabels = { ...customLabels }; delete nextLabels[key];
                                        setCustomLabels(nextLabels);
                                        handleChange('camposPersonalizados', nextLabels);
                                    }}
                                    className="p-1 text-gray-300 hover:text-red-500 transition-colors"
                                >
                                    <X className="w-3.5 h-3.5" />
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {/* Fields grid */}
            {isExpanded && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6 animate-in fade-in duration-200">
                    {Object.entries(FIELD_LABELS).map(([key, label]) => {
                        if (key === 'obsModelo') return null;
                        return (
                            <div key={key} className="border-b border-gray-100 pb-6 last:border-0">
                                {renderField(key, label)}
                            </div>
                        );
                    })}
                    {Object.entries(customLabels).map(([key, label]) => (
                        <div key={key} className="border-b border-gray-100 pb-6">
                            {renderField(key, label, true)}
                        </div>
                    ))}
                    {/* obsModelo spans full width */}
                    {activeFields.has('obsModelo') && (
                        <div className="md:col-span-2 border-b border-gray-100 pb-6 last:border-0">
                            {renderField('obsModelo', FIELD_LABELS.obsModelo)}
                        </div>
                    )}
                </div>
            )}

            {!isExpanded && (
                <div className="bg-gray-50 p-3 rounded-lg border border-dashed border-gray-200 text-center text-[9px] font-bold text-gray-400 uppercase tracking-widest">
                    Especificaciones contraídas · {articuloDescripcion || 'Sin descripción de artículo'}
                </div>
            )}

            {/* Modal nuevo campo */}
            {showNuevoCampoModal && (
                <div className="fixed inset-0 z-[200] flex items-center justify-center">
                    <div
                        className="absolute inset-0 bg-black/50 backdrop-blur-sm"
                        onClick={() => { setShowNuevoCampoModal(false); setNuevoCampoLabel(''); }}
                    />
                    <div className="relative bg-white rounded-xl shadow-2xl border border-gray-200 w-full max-w-sm mx-4 p-7 animate-in fade-in zoom-in-95 duration-200">
                        <div className="mb-5">
                            <div className="flex items-center gap-2 mb-2">
                                <div className="w-1 h-4 bg-gray-900 rounded-sm" />
                                <h3 className="text-[11px] font-black text-gray-900 uppercase tracking-widest">Nuevo campo personalizado</h3>
                            </div>
                            <p className="text-[11px] text-gray-400 leading-relaxed pl-3">
                                Define el nombre del campo que se agregará a las especificaciones técnicas. Este valor no existe en el catálogo estándar.
                            </p>
                        </div>

                        <div className="mb-5">
                            <label className="block text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1.5">Nombre del campo</label>
                            <input
                                type="text"
                                autoFocus
                                value={nuevoCampoLabel}
                                onChange={(e) => setNuevoCampoLabel(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === 'Enter') handleConfirmNuevoCampo();
                                    if (e.key === 'Escape') { setShowNuevoCampoModal(false); setNuevoCampoLabel(''); }
                                }}
                                placeholder="Ej: Doble costura, Ribete, Bolsillo interior..."
                                className="w-full border border-gray-200 rounded-lg px-4 py-2.5 text-xs font-semibold text-gray-800 uppercase outline-none focus:border-blue-400 focus:ring-2 focus:ring-blue-50 transition-all placeholder:normal-case placeholder:font-normal placeholder:text-gray-300"
                            />
                        </div>

                        <div className="flex gap-2 justify-end">
                            <button
                                type="button"
                                onClick={() => { setShowNuevoCampoModal(false); setNuevoCampoLabel(''); }}
                                className="px-5 py-2 rounded-lg text-[10px] font-black uppercase tracking-widest text-gray-500 bg-gray-50 hover:bg-gray-100 border border-gray-200 transition-all"
                            >
                                Cancelar
                            </button>
                            <button
                                type="button"
                                onClick={handleConfirmNuevoCampo}
                                disabled={!nuevoCampoLabel.trim()}
                                className="px-5 py-2 rounded-lg text-[10px] font-black uppercase tracking-widest text-white bg-gray-900 hover:bg-blue-700 disabled:opacity-40 disabled:cursor-not-allowed transition-all"
                            >
                                Agregar campo
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
