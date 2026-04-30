import { ChevronDown, ChevronUp, Settings2, Check, X, Plus, Trash2, Link as LinkIcon } from 'lucide-react';
import { useState, useEffect } from 'react';
import { usePlantillas, FIELD_LABELS, ALL_FIELDS } from '../../hooks/usePlantillas';
import { toast } from 'sonner';

export default function PlantillasPanel({ title = "Especificaciones Técnicas", data, onUpdate, articuloDescripcion = "", readOnly = false, telas = [], accesorios = [] }) {
    const [isExpanded, setIsExpanded] = useState(true);
    const [showFieldSelector, setShowFieldSelector] = useState(false);
    const [activeFields, setActiveFields] = useState(new Set(ALL_FIELDS));
    const [activeLinkingField, setActiveLinkingField] = useState(null); // Qué campo se está vinculando
    const [customLabels, setCustomLabels] = useState({});
    const [linkQuantities, setLinkQuantities] = useState({});

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

    const handleChange = (field, value) => {
        if (!readOnly) onUpdate(specId, field, value);
    };

    const handleAddVinculo = (fieldKey, type, materialId, qty = 1) => {
        if (readOnly) return;

        const amount = parseInt(qty);
        if (isNaN(amount) || amount <= 0) {
            toast.error("Ingrese una cantidad válida");
            return;
        }

        // Validación de cantidad para accesorios
        if (type === 'ACCESORIO') {
            const material = accesorios.find(a => a.id === materialId);
            const alreadyUsed = vinculos
                .filter(v => v.materialId === materialId)
                .reduce((sum, v) => sum + (v.cantidad || 0), 0);

            const totalProposed = alreadyUsed + amount;

            if (material && totalProposed > material.cantidad) {
                toast.error(`No hay suficiente disponibilidad. Quedan: ${material.cantidad - alreadyUsed}`);
                return;
            }
        }

        const newVinculo = {
            id: crypto.randomUUID(),
            fieldName: fieldKey,
            materialType: type,
            materialId: materialId,
            cantidad: amount
        };

        const newVinculos = [...vinculos, newVinculo];
        handleChange('vinculos', newVinculos);

        // Limpiar la cantidad de este material específico
        setLinkQuantities(prev => {
            const next = { ...prev };
            delete next[materialId];
            return next;
        });

        setActiveLinkingField(null);
        toast.success("Material vinculado correctamente");
    };

    const handleRemoveVinculo = (vinculoId) => {
        if (readOnly) return;
        const newVinculos = vinculos.filter(v => v.id !== vinculoId);
        handleChange('vinculos', newVinculos);
    };

    useEffect(() => {
        if (spec.customFields) {
            try {
                const parsed = JSON.parse(spec.customFields);
                const labels = {};
                Object.keys(parsed).forEach(key => {
                    labels[key] = parsed[key].label;
                });
                setCustomLabels(labels);
            } catch (e) {
                console.error("Error parsing custom fields:", e);
            }
        }
    }, [spec.customFields]);

    const handleAddField = () => {
        const label = window.prompt("Nombre del nuevo campo:");
        if (!label || !label.trim()) return;

        const key = `cf_${Date.now()}`;
        const trimmedLabel = label.trim();
        const newLabels = { ...customLabels, [key]: trimmedLabel };
        setCustomLabels(newLabels);

        // Activar el nuevo campo automáticamente
        const newActive = new Set(activeFields);
        newActive.add(key);
        setActiveFields(newActive);

        // Guardar la estructura en customFields
        const currentCustom = spec.customFields ? JSON.parse(spec.customFields) : {};
        currentCustom[key] = { label: trimmedLabel, value: "" };
        handleChange('customFields', JSON.stringify(currentCustom));
        handleChange('camposActivos', Array.from(newActive));

        toast.success(`Campo "${trimmedLabel}" agregado correctamente`);
    };

    const handleCustomValueChange = (key, value) => {
        const currentCustom = spec.customFields ? JSON.parse(spec.customFields) : {};
        if (!currentCustom[key]) currentCustom[key] = { label: customLabels[key], value: "" };
        currentCustom[key].value = value;
        handleChange('customFields', JSON.stringify(currentCustom));
    };

    const toggleField = (key) => {
        if (readOnly) return;
        const newFields = new Set(activeFields);
        if (newFields.has(key)) {
            newFields.delete(key);
        } else {
            newFields.add(key);
        }
        setActiveFields(newFields);
        handleChange('camposActivos', Array.from(newFields));
    };

    const renderField = (fieldKey, label, isCustom = false) => {
        if (!activeFields || !activeFields.has(fieldKey)) return null;

        const isLongText = fieldKey === 'obsModelo' || fieldKey === 'cortesAplicacion';
        const fieldVinculos = vinculos.filter(v => v.fieldName === fieldKey);

        const value = isCustom
            ? (spec.customFields ? (JSON.parse(spec.customFields)[fieldKey]?.value || "") : "")
            : (spec[fieldKey] || "");

        return (
            <div key={fieldKey} className="group flex flex-col gap-1.5 p-4 rounded-2xl hover:bg-gray-50/50 transition-all border border-transparent hover:border-gray-100 bg-white shadow-sm mb-2">
                <div className="flex justify-between items-center px-1">
                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">
                        {isCustom ? `[Personalizado] ${label}` : label}
                    </label>
                    <div className="flex items-center gap-2">
                        {fieldVinculos.length > 0 && (
                            <span className="text-[9px] font-black text-blue-500 bg-blue-50 px-2 py-0.5 rounded-full border border-blue-100 flex items-center gap-1">
                                {fieldVinculos.length} VINCULO(S)
                            </span>
                        )}
                        {!readOnly && (
                            <button
                                type="button"
                                onClick={() => setActiveLinkingField(activeLinkingField === fieldKey ? null : fieldKey)}
                                className={`p-1.5 rounded-lg border transition-all shadow-sm flex items-center justify-center hover:scale-110 active:scale-95 ${activeLinkingField === fieldKey
                                    ? 'bg-blue-600 text-white border-blue-700 shadow-md ring-2 ring-blue-100'
                                    : 'bg-blue-50 text-blue-600 border-blue-200 hover:bg-blue-100 hover:text-blue-700'
                                    }`}
                                title="Vincular Tela o Accesorio"
                            >
                                <Plus className="w-4 h-4 stroke-[3px]" />
                            </button>
                        )}
                    </div>
                </div>

                <div className="relative">
                    {isLongText ? (
                        <textarea
                            rows={2}
                            readOnly={readOnly}
                            className={`w-full bg-white border border-gray-100 rounded-xl p-3 text-xs font-bold text-gray-700 outline-none focus:border-blue-200 focus:ring-4 focus:ring-blue-50/50 transition-all resize-none ${readOnly ? 'cursor-default' : ''}`}
                            value={value}
                            onChange={(e) => isCustom ? handleCustomValueChange(fieldKey, e.target.value) : handleChange(fieldKey, e.target.value)}
                            placeholder={readOnly ? "" : `Ingrese detalles de ${label.toLowerCase()}...`}
                        />
                    ) : (
                        <input
                            type="text"
                            readOnly={readOnly}
                            className={`w-full bg-white border border-gray-100 rounded-xl p-3 text-xs font-bold text-gray-700 outline-none focus:border-blue-200 focus:ring-4 focus:ring-blue-50/50 transition-all ${readOnly ? 'cursor-default' : ''}`}
                            value={value}
                            onChange={(e) => isCustom ? handleCustomValueChange(fieldKey, e.target.value) : handleChange(fieldKey, e.target.value)}
                            placeholder={readOnly ? "" : `Definir ${label.toLowerCase()}...`}
                        />
                    )}

                    {/* Selector de Vínculos Simplificado */}
                    {activeLinkingField === fieldKey && (
                        <div className="absolute top-full left-0 right-0 mt-1 p-4 bg-white rounded-2xl border border-blue-500 shadow-xl z-50 animate-in fade-in zoom-in-95 duration-200 min-w-[280px]">
                            <div className="flex justify-between items-center mb-3 px-1">
                                <span className="text-[9px] font-black uppercase text-gray-800 tracking-wider flex items-center gap-2">
                                    <div className="w-1 h-3 bg-blue-600 rounded-full"></div>
                                    Vincular: {label}
                                </span>
                                <button onClick={() => setActiveLinkingField(null)}><X className="w-3.5 h-3.5 text-gray-400 hover:text-gray-600" /></button>
                            </div>

                            <div className="space-y-4">
                                <div>
                                    <p className="text-[8px] font-black text-gray-400 uppercase mb-2 ml-1">Telas</p>
                                    <div className="max-h-32 overflow-y-auto space-y-1 custom-scrollbar pr-1">
                                        {telas.map(t => (
                                            <button key={t.id} onClick={() => handleAddVinculo(fieldKey, 'TELA', t.id)}
                                                className="w-full text-left p-1.5 rounded-lg border border-transparent hover:border-blue-100 hover:bg-blue-50 transition-all flex justify-between items-center group">
                                                <span className="text-[9px] font-bold text-gray-600 group-hover:text-blue-700 italic truncate max-w-[200px]">{t.nombre}</span>
                                                <Plus className="w-2.5 h-2.5 opacity-0 group-hover:opacity-100 text-blue-500" />
                                            </button>
                                        ))}
                                    </div>
                                </div>
                                <div className="border-t border-gray-50 pt-3">
                                    <p className="text-[8px] font-black text-gray-400 uppercase mb-2 ml-1 italic">Accesorios y Avíos en Inventario</p>
                                    <div className="max-h-56 overflow-y-auto space-y-2 custom-scrollbar pr-1">
                                        {accesorios.map(a => {
                                            const totalInInventory = a.cantidad || 0;
                                            const alreadyLinked = vinculos
                                                .filter(v => v.materialId === a.id)
                                                .reduce((sum, v) => sum + (v.cantidad || 0), 0);
                                            const currentInput = linkQuantities[a.id] || 0;
                                            const remaining = totalInInventory - alreadyLinked - currentInput;

                                            return (
                                                <div key={a.id} className="bg-gray-50/30 p-2.5 rounded-xl border border-gray-100/50 hover:bg-green-50/30 hover:border-green-100 transition-all group">
                                                    <div className="flex justify-between items-start mb-2">
                                                        <div className="flex flex-col">
                                                            <span className="text-[9px] font-black text-gray-700">{a.nombreAccesorio}</span>
                                                            <span className="text-[7px] font-bold text-gray-400 uppercase tracking-widest">{a.tipo} • {totalInInventory} TOTAL</span>
                                                        </div>
                                                        <div className={`text-[8px] font-black px-1.5 py-0.5 rounded-full ${remaining < 0 ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-600'}`}>
                                                            QUEDAN: {remaining}
                                                        </div>
                                                    </div>

                                                    <div className="flex items-center gap-2 mt-1">
                                                        <div className="flex-1 flex flex-col gap-0.5">
                                                            <label className="text-[7px] font-black text-gray-400 uppercase ml-1">Cant. a usar:</label>
                                                            <input
                                                                type="number"
                                                                min="1"
                                                                placeholder="0"
                                                                value={linkQuantities[a.id] || ""}
                                                                onChange={(e) => setLinkQuantities(prev => ({ ...prev, [a.id]: parseInt(e.target.value) || 0 }))}
                                                                className="w-full bg-white border border-gray-200 rounded-lg p-1.5 text-[10px] font-black text-blue-600 outline-none focus:border-blue-400"
                                                            />
                                                        </div>
                                                        <button
                                                            onClick={() => handleAddVinculo(fieldKey, 'ACCESORIO', a.id, linkQuantities[a.id] || 0)}
                                                            disabled={currentInput <= 0}
                                                            className={`mt-3 self-end p-2 rounded-lg transition-all shadow-sm ${currentInput > 0
                                                                    ? 'bg-blue-600 text-white hover:bg-blue-700'
                                                                    : 'bg-gray-100 text-gray-300 cursor-not-allowed'
                                                                }`}
                                                            title="Confirmar Vínculo"
                                                        >
                                                            <Plus className="w-4 h-4" />
                                                        </button>
                                                    </div>
                                                </div>
                                            );
                                        })}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}
                </div>

                {/* Vínculos debajo del input más compactos */}
                {fieldVinculos.length > 0 && (
                    <div className="flex flex-wrap gap-1.5 mt-1 border-t border-gray-50 pt-1.5">
                        {fieldVinculos.map(v => {
                            const material = v.materialType === 'TELA'
                                ? telas.find(t => t.id === v.materialId)
                                : accesorios.find(a => a.id === v.materialId);
                            const name = v.materialType === 'TELA' ? (material?.nombre || "Tela") : (material?.nombreAccesorio || "Acc.");

                            return (
                                <div key={v.id} className={`flex items-center gap-1 px-1.5 py-0.5 rounded-md text-[8px] font-bold border shadow-xs ${v.materialType === 'TELA' ? 'bg-blue-50/50 border-blue-100 text-blue-600' : 'bg-green-50/50 border-green-100 text-green-600'
                                    }`}>
                                    <span className="truncate max-w-[80px]">{name}</span>
                                    {v.cantidad > 1 && <span className="opacity-60 text-[7px]">x{v.cantidad}</span>}
                                    {!readOnly && (
                                        <button onClick={() => handleRemoveVinculo(v.id)} className="hover:text-red-500 transition-colors"><X className="w-2 h-2" /></button>
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
        <div className="space-y-6">
            <div className="flex justify-between items-center mb-6 px-2">
                <h4 className="text-xl font-black text-gray-800 uppercase tracking-widest italic flex items-center gap-3">
                    <div className="w-1.5 h-8 bg-blue-600 rounded-full"></div>
                    {title}
                    {loading && <span className="text-[10px] font-bold text-blue-500 animate-pulse bg-blue-50 px-3 py-1 rounded-full border border-blue-100 ml-4 tracking-normal not-italic">CARGANDO...</span>}
                </h4>

                <div className="flex items-center gap-3">
                    {!readOnly && (
                        <button
                            type="button"
                            onClick={() => setShowFieldSelector(!showFieldSelector)}
                            className={`flex items-center gap-2 px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${showFieldSelector
                                ? "bg-blue-600 text-white shadow-lg shadow-blue-200"
                                : "bg-white text-gray-500 border border-gray-100 hover:border-blue-200 hover:text-blue-600"
                                }`}
                        >
                            <Settings2 className="w-4 h-4" />
                            {showFieldSelector ? "Finalizar" : "Gestionar Campos"}
                        </button>
                    )}

                    <button
                        type="button"
                        onClick={() => setIsExpanded(!isExpanded)}
                        className="p-2.5 bg-gray-50 text-gray-400 rounded-xl hover:text-blue-600 hover:bg-blue-50 transition-all border border-transparent hover:border-gray-200"
                    >
                        {isExpanded ? <ChevronUp className="w-5 h-5" /> : <ChevronDown className="w-5 h-5" />}
                    </button>
                </div>
            </div>

            {showFieldSelector && !readOnly && (
                <div className="bg-white p-8 rounded-3xl border-2 border-blue-500/20 shadow-xl animate-in fade-in zoom-in-95 duration-200">
                    <div className="flex justify-between items-center mb-8">
                        <div>
                            <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Activar/Desactivar Campos</span>
                        </div>
                        <div className="flex gap-2">
                                <button
                                    type="button"
                                    onClick={handleAddField}
                                    className="px-5 py-2 bg-gray-900 text-white rounded-xl text-[10px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all flex items-center gap-2 shadow-md shadow-blue-100"
                                >
                                    <Plus className="w-4 h-4" />
                                    Nuevo Campo
                                </button>
                            <button
                                type="button"
                                onClick={() => {
                                    const all = new Set(ALL_FIELDS);
                                    setActiveFields(all);
                                    handleChange('camposActivos', Array.from(all));
                                }}
                                className="px-5 py-2 bg-gray-50 text-gray-500 rounded-xl text-[10px] font-black uppercase tracking-widest hover:bg-gray-100 transition-all"
                            >
                                Seleccionar Todo
                            </button>
                            <button
                                type="button"
                                onClick={() => {
                                    setActiveFields(new Set());
                                    handleChange('camposActivos', []);
                                }}
                                className="px-5 py-2 bg-gray-50 text-gray-500 rounded-xl text-[10px] font-black uppercase tracking-widest hover:bg-gray-100 transition-all"
                            >
                                Limpiar
                            </button>
                        </div>
                    </div>

                    <div className="flex flex-wrap gap-2 max-h-60 overflow-y-auto pr-2 custom-scrollbar p-1">
                        {Array.from(ALL_FIELDS).map(f => (
                            <button
                                key={f}
                                type="button"
                                onClick={() => toggleField(f)}
                                className={`px-4 py-2.5 rounded-2xl text-[10px] font-black uppercase tracking-widest transition-all border-2 ${activeFields.has(f)
                                    ? "bg-blue-50 border-blue-200 text-blue-700 shadow-sm"
                                    : "bg-white border-gray-100 text-gray-400 hover:border-blue-100"
                                    }`}
                            >
                                {FIELD_LABELS[f] || f}
                            </button>
                        ))}
                        {Object.keys(customLabels).map(key => (
                            <div key={key} className="flex gap-1 group/item">
                                <button
                                    type="button"
                                    onClick={() => toggleField(key)}
                                    className={`px-4 py-2.5 rounded-2xl text-[10px] font-black uppercase tracking-widest transition-all border-2 ${activeFields.has(key)
                                        ? "bg-orange-50 border-orange-200 text-orange-700 shadow-sm"
                                        : "bg-white border-gray-100 text-gray-400 hover:border-orange-100"
                                        }`}
                                >
                                    {customLabels[key]}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => {
                                        const current = spec.customFields ? JSON.parse(spec.customFields) : {};
                                        delete current[key];
                                        handleChange('customFields', JSON.stringify(current));
                                        const nextActive = new Set(activeFields);
                                        nextActive.delete(key);
                                        setActiveFields(nextActive);
                                        handleChange('camposActivos', Array.from(nextActive));
                                        const nextLabels = { ...customLabels };
                                        delete nextLabels[key];
                                        setCustomLabels(nextLabels);
                                    }}
                                    className="p-1 text-gray-200 hover:text-red-500 transition-colors"
                                >
                                    <X className="w-4 h-4 stroke-[3px]" />
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {isExpanded && (
                <div className="animate-in fade-in duration-300 space-y-2">
                    {Object.entries(FIELD_LABELS).map(([key, label]) => renderField(key, label))}
                    {Object.entries(customLabels).map(([key, label]) => renderField(key, label, true))}
                </div>
            )}

            {!isExpanded && (
                <div className="bg-gray-50/50 p-4 rounded-xl border border-dashed border-gray-200 text-center text-[10px] font-bold text-gray-400 uppercase tracking-widest">
                    Especificaciones contraídas • {articuloDescripcion || "Sin descripción de artículo"}
                </div>
            )}
            {/* Modal para nuevo campo eliminado y reemplazado por window.prompt */}
        </div>
    );
}