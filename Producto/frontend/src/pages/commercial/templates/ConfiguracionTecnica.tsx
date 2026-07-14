import React from 'react';
import { Layers, Plus, X } from 'lucide-react';
import { ALL_FIELDS, FIELD_LABELS } from "../../../hooks/usePlantillas";

export default function ConfiguracionTecnica({
    isNew,
    configId,
    camposActivos,
    customFields,
    availableFields,
    onToggleField,
    onRemoveCustomField,
    onOpenFieldModal,
}) {
    // Usa campos de la API si están disponibles; si no, usa la lista hardcodeada como fallback
    const camposList = (availableFields && availableFields.length > 0)
        ? availableFields
        : Array.from(ALL_FIELDS).map(nombre => ({ nombreCampo: nombre }));

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center mb-4">
                <h3 className="text-[10px] font-black text-gray-400 uppercase tracking-widest flex items-center gap-2">
                    <Layers className="w-3 h-3" /> {isNew ? 'Campos de Entrada Activos' : 'Configuración de Visibilidad'}
                </h3>
                <button
                    type="button"
                    onClick={() => onOpenFieldModal(isNew, configId)}
                    className={`px-3 py-1 rounded-lg text-[9px] font-black uppercase tracking-widest transition-all flex items-center gap-1 ${isNew ? 'bg-blue-50 text-blue-600 hover:bg-blue-100' : 'bg-orange-50 text-orange-600 hover:bg-orange-100'}`}
                >
                    <Plus className="w-2.5 h-2.5" />
                    Agregar Nuevo Campo
                </button>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-2">
                {camposList.map(campo => {
                    const field = campo.nombreCampo;
                    const label = FIELD_LABELS[field] || field;
                    return (
                    <button
                        key={field}
                        type="button"
                        onClick={() => onToggleField(configId, field)}
                        className={`flex items-center gap-2 px-3 py-2 rounded-xl text-[9px] font-black uppercase tracking-wide transition-all border ${
                            camposActivos.has(field)
                                ? 'bg-blue-50 border-blue-300 text-blue-700'
                                : 'bg-gray-50 border-gray-50 text-gray-400'
                        }`}
                    >
                        <div className={`w-2.5 h-2.5 rounded-sm flex-shrink-0 transition-all ${camposActivos.has(field) ? 'bg-blue-500' : 'bg-gray-200'}`} />
                        {label}
                    </button>
                    );
                })}

                {/* Custom Fields */}
                {Object.entries(customFields || {}).map(([key, item]) => (
                    <div key={key} className="relative group/custom">
                        <button
                            type="button"
                            onClick={() => onToggleField(configId, key)}
                            className={`w-full flex items-center gap-2 px-3 py-2 rounded-xl text-[9px] font-black uppercase tracking-wide transition-all border ${
                                camposActivos.has(key)
                                    ? 'bg-orange-50 border-orange-300 text-orange-700'
                                    : 'bg-gray-50 border-gray-50 text-gray-400'
                            }`}
                        >
                            <div className={`w-2.5 h-2.5 rounded-sm flex-shrink-0 transition-all ${camposActivos.has(key) ? 'bg-orange-500' : 'bg-gray-200'}`} />
                            {item.label}
                        </button>
                        <button
                            onClick={() => onRemoveCustomField(isNew, configId, key)}
                            className="absolute -top-1 -right-1 bg-red-500 text-white rounded-full p-0.5 opacity-0 group-hover/custom:opacity-100 transition-opacity z-10"
                        >
                            <X className="w-2 h-2" />
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}
