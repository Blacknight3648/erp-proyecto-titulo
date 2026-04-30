import React from 'react';
import { Box, Trash2, ChevronUp, ChevronDown, Check } from 'lucide-react';
import ConfiguracionTecnica from './ConfiguracionTecnica';

export default function ListaPlantillas({
    configuraciones,
    expanded,
    toggleExpand,
    remove,
    editCampos,
    editTelas,
    editAccesorios,
    editCustomFields,
    toggleFieldInEdit,
    removeCustomField,
    setFieldModal,
    addTableItem,
    updateTableItem,
    removeTableItem,
    handleSaveEdit
}) {
    return (
        <div className="space-y-4">
            {configuraciones.map(config => {
                const isOpen = expanded === config.id;
                const localCampos = editCampos[config.id] || new Set(config.camposActivos);
                const localTelas = editTelas[config.id] || config.plantillaTelas || [];
                const localAccesorios = editAccesorios[config.id] || config.plantillaAccesorios || [];
                const localCustomFields = editCustomFields[config.id] || (config.customFields ? JSON.parse(config.customFields) : {});

                return (
                    <div key={config.id} className={`bg-white rounded-[2.5rem] border transition-all duration-300 ${isOpen ? 'border-blue-200 shadow-xl' : 'border-gray-100 shadow-sm'}`}>
                        {/* Header Item */}
                        <div className="flex items-center justify-between p-6 cursor-pointer" onClick={() => toggleExpand(config)}>
                            <div className="flex items-center gap-4">
                                <div className="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center border border-blue-100">
                                    <Box className="w-6 h-6 text-blue-600" />
                                </div>
                                <div>
                                    <h2 className="text-sm font-black text-gray-800 uppercase tracking-widest">{config.nombrePrenda}</h2>
                                    <div className="flex gap-3 mt-1">
                                        <span className="text-[9px] font-bold text-gray-400 uppercase tracking-widest">{config.camposActivos?.length || 0} CAMPOS</span>
                                        <span className="text-[9px] font-bold text-blue-400 uppercase tracking-widest">{config.plantillaTelas?.length || 0} TELAS</span>
                                        <span className="text-[9px] font-bold text-green-400 uppercase tracking-widest">{config.plantillaAccesorios?.length || 0} ACCESORIOS</span>
                                    </div>
                                </div>
                            </div>
                            <div className="flex items-center gap-3">
                                <button
                                    type="button"
                                    onClick={(e) => { e.stopPropagation(); remove(config.id); }}
                                    className="p-3 text-gray-200 hover:text-red-500 hover:bg-red-50 rounded-xl transition-all"
                                >
                                    <Trash2 className="w-4 h-4" />
                                </button>
                                <div className={`p-2 rounded-xl transition-all ${isOpen ? 'bg-blue-600 text-white' : 'bg-gray-50 text-gray-400'}`}>
                                    {isOpen ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                                </div>
                            </div>
                        </div>

                        {/* Expanded Content */}
                        {isOpen && (
                            <div className="p-8 pt-0 border-t border-gray-50 space-y-8 animate-in slide-in-from-top-2 duration-300">
                                <div className="pt-6">
                                    <ConfiguracionTecnica 
                                        isNew={false}
                                        configId={config.id}
                                        camposActivos={localCampos}
                                        customFields={localCustomFields}
                                        telas={localTelas}
                                        accesorios={localAccesorios}
                                        onToggleField={toggleFieldInEdit}
                                        onRemoveCustomField={removeCustomField}
                                        onOpenFieldModal={(isNew, id) => setFieldModal({ open: true, isNew, configId: id, fieldName: "" })}
                                        onAddTableItem={addTableItem}
                                        onUpdateTableItem={updateTableItem}
                                        onRemoveTableItem={removeTableItem}
                                    />
                                </div>

                                <div className="flex gap-4 p-4 bg-gray-50 rounded-3xl">
                                    <button onClick={() => handleSaveEdit(config)}
                                        className="flex items-center gap-2 px-8 py-4 bg-blue-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest hover:bg-blue-700 transition-all shadow-lg shadow-blue-100">
                                        <Check className="w-5 h-5" /> Guardar Cambios Integrales
                                    </button>
                                    <button onClick={() => toggleExpand(config)}
                                        className="px-8 py-4 bg-white text-gray-500 border border-gray-200 rounded-2xl font-black text-[10px] uppercase tracking-widest hover:bg-gray-50 transition-all">
                                        Cancelar
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                );
            })}
        </div>
    );
}
