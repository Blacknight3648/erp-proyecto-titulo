import React from 'react';
import { Layers, Plus, Trash2, X } from 'lucide-react';
import { ALL_FIELDS, FIELD_LABELS } from "../../../../../hooks/usePlantillas";

export default function ConfiguracionTecnica({
    isNew,
    configId,
    camposActivos,
    customFields,
    telas,
    accesorios,
    availableFields,
    onToggleField,
    onRemoveCustomField,
    onOpenFieldModal,
    onAddTableItem,
    onUpdateTableItem,
    onRemoveTableItem
}) {
    // Usa campos de la API si están disponibles; si no, usa la lista hardcodeada como fallback
    const camposList = (availableFields && availableFields.length > 0)
        ? availableFields
        : Array.from(ALL_FIELDS).map(nombre => ({ nombreCampo: nombre }));

    return (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Sección de Campos */}
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

                <div className="grid grid-cols-2 sm:grid-cols-3 gap-2">
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

            {/* Sección de Tablas Técnicas */}
            <div className="space-y-8">
                {/* Tabla de Telas */}
                <div className="space-y-3">
                    <div className="flex justify-between items-center">
                        <h4 className="text-[10px] font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                            <div className="w-1 h-3 bg-blue-600 rounded-full" /> Detalle Tela Base
                        </h4>
                        <button 
                            onClick={() => onAddTableItem(isNew, configId, 'tela')} 
                            className="p-1.5 bg-gray-900 text-white rounded-lg hover:bg-blue-600 transition-all"
                        >
                            <Plus className="w-3.5 h-3.5" />
                        </button>
                    </div>
                    <div className="bg-gray-50 p-4 rounded-2xl border border-gray-100">
                        <table className="w-full text-left text-[10px]">
                            <thead>
                                <tr className="text-gray-400">
                                    <th className="pb-2 font-black uppercase tracking-widest">Aplicación</th>
                                    <th className="pb-2 font-black uppercase tracking-widest">Nombre</th>
                                    <th className="pb-2 font-black uppercase tracking-widest text-center">Peso</th>
                                    <th className="pb-2 w-8"></th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {telas.map((item, idx) => (
                                    <tr key={idx} className="group">
                                        <td className="py-2 pr-2">
                                            <input type="text" value={item.aplicacion} onChange={e => onUpdateTableItem(isNew, configId, 'tela', idx, 'aplicacion', e.target.value)} placeholder="Ej: Cuerpo" className="w-full bg-transparent font-bold outline-none text-gray-700 uppercase" />
                                        </td>
                                        <td className="py-2 pr-2">
                                            <input type="text" value={item.nombre} onChange={e => onUpdateTableItem(isNew, configId, 'tela', idx, 'nombre', e.target.value)} placeholder="Ej: Algodón" className="w-full bg-transparent font-bold outline-none text-blue-600 uppercase" />
                                        </td>
                                        <td className="py-2 text-center">
                                            <input type="number" value={item.peso} onChange={e => onUpdateTableItem(isNew, configId, 'tela', idx, 'peso', parseFloat(e.target.value))} className="w-12 bg-white rounded border border-gray-100 p-1 text-center font-bold" />
                                        </td>
                                        <td className="py-2 text-right">
                                            <button onClick={() => onRemoveTableItem(isNew, configId, 'tela', idx)} className="text-gray-200 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-all">
                                                <Trash2 className="w-3.5 h-3.5" />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        {telas.length === 0 && <p className="text-[9px] text-gray-400 italic text-center py-2">Sin telas definidas</p>}
                    </div>
                </div>

                {/* Tabla de Accesorios */}
                <div className="space-y-3">
                    <div className="flex justify-between items-center">
                        <h4 className="text-[10px] font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                            <div className="w-1 h-3 bg-green-600 rounded-full" /> Detalle Accesorios Base
                        </h4>
                        <button 
                            onClick={() => onAddTableItem(isNew, configId, 'accesorio')} 
                            className="p-1.5 bg-gray-900 text-white rounded-lg hover:bg-green-600 transition-all"
                        >
                            <Plus className="w-3.5 h-3.5" />
                        </button>
                    </div>
                    <div className="bg-gray-50 p-4 rounded-2xl border border-gray-100">
                        <table className="w-full text-left text-[10px]">
                            <thead>
                                <tr className="text-gray-400">
                                    <th className="pb-2 font-black uppercase tracking-widest">Tipo</th>
                                    <th className="pb-2 font-black uppercase tracking-widest">Nombre</th>
                                    <th className="pb-2 font-black uppercase tracking-widest text-center">Cant</th>
                                    <th className="pb-2 w-8"></th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {accesorios.map((item, idx) => (
                                    <tr key={idx} className="group">
                                        <td className="py-2 pr-2">
                                            <input type="text" value={item.tipo} onChange={e => onUpdateTableItem(isNew, configId, 'accesorio', idx, 'tipo', e.target.value)} placeholder="Ej: Cierre" className="w-full bg-transparent font-bold outline-none text-gray-700 uppercase" />
                                        </td>
                                        <td className="py-2 pr-2">
                                            <input type="text" value={item.nombreAccesorio} onChange={e => onUpdateTableItem(isNew, configId, 'accesorio', idx, 'nombreAccesorio', e.target.value)} placeholder="Ej: Nylon" className="w-full bg-transparent font-bold outline-none text-green-600 uppercase" />
                                        </td>
                                        <td className="py-2 text-center">
                                            <input type="number" value={item.cantidad} onChange={e => onUpdateTableItem(isNew, configId, 'accesorio', idx, 'cantidad', parseInt(e.target.value))} className="w-12 bg-white rounded border border-gray-100 p-1 text-center font-bold" />
                                        </td>
                                        <td className="py-2 text-right">
                                            <button onClick={() => onRemoveTableItem(isNew, configId, 'accesorio', idx)} className="text-gray-200 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-all">
                                                <Trash2 className="w-3.5 h-3.5" />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        {accesorios.length === 0 && <p className="text-[9px] text-gray-400 italic text-center py-2">Sin accesorios definidos</p>}
                    </div>
                </div>
            </div>
        </div>
    );
}
