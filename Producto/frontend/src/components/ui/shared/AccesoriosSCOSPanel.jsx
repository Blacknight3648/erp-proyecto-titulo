import { Plus, Trash2, ChevronDown, ChevronUp } from 'lucide-react';
import { useState } from 'react';
import ComboSearchField from './ComboSearchField';

export default function AccesoriosSCOSPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
    const [isExpanded, setIsExpanded] = useState(true);

    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                        <div className="w-1 h-5 bg-emerald-500 rounded-sm"></div>
                        Accesorios y Avíos
                        <span className="ml-1 px-2 py-0.5 bg-emerald-50 text-emerald-600 text-[9px] rounded border border-emerald-100 font-black">
                            {(data || []).length} ITEMS
                        </span>
                    </h4>
                    <p className="text-[10px] text-gray-400 mt-1 ml-3">
                        Registre todos los avíos requeridos: cierres, botones, hebillas, hilos de contraste, parches, etiquetas, etc. La cantidad ingresada se validará contra el inventario disponible al momento de aprobar la solicitud.
                    </p>
                </div>
                <div className="flex items-center gap-2">
                    {!readOnly && (
                        <button
                            type="button"
                            onClick={onAdd}
                            className="flex items-center px-4 py-2 bg-gray-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all"
                        >
                            <Plus className="w-3.5 h-3.5 mr-2" />
                            Agregar Accesorio
                        </button>
                    )}
                    <button
                        type="button"
                        onClick={() => setIsExpanded(!isExpanded)}
                        className="p-2 bg-gray-50 text-gray-400 rounded-xl hover:text-blue-600 hover:bg-blue-50 transition-all"
                    >
                        {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                    </button>
                </div>
            </div>

            {isExpanded ? (
                <div className="overflow-x-auto bg-white p-6 rounded-2xl border border-gray-50 shadow-sm animate-in slide-in-from-top-2 duration-300">
                    <table className="w-full text-left border-separate border-spacing-y-2">
                        <thead>
                        <tr>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Nombre Accesorio</th>
                            <th className="px-4 py-2 text-[9px) font-black text-gray-400 uppercase tracking-widest">Tipo</th>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cantidad</th>
                            {!readOnly && <th className="w-10"></th>}
                        </tr>
                        </thead>
                        <tbody>
                        {(data || []).map((item, index) => (
                            <tr key={item.id || index} className="group hover:bg-emerald-50/30 transition-all duration-200">
                                <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-emerald-100/50">
                                    <div className="flex flex-col gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Nombre</label>
                                        <ComboSearchField
                                            tipo="ACCESORIO"
                                            value={item.nombreAccesorio || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'nombreAccesorio', val)}
                                            placeholder="Buscar avío..."
                                            readOnly={readOnly}
                                            className="min-w-[180px]"
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-emerald-100/50">
                                    <div className="flex flex-col gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Tipo</label>
                                        <input
                                            type="text"
                                            value={item.tipo || ""}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'tipo', e.target.value.toUpperCase())}
                                            placeholder="Tipo..."
                                            className={`w-full bg-gray-50/50 border border-gray-100/50 rounded-lg p-2 text-xs font-bold text-gray-600 uppercase outline-none focus:bg-white focus:border-emerald-200 transition-all ${readOnly ? 'cursor-default' : ''}`}
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-emerald-100/50 text-center">
                                    <div className="flex flex-col items-center gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Cantidad</label>
                                        <input
                                            type="number"
                                            value={item.cantidad || 0}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'cantidad', parseInt(e.target.value) || 0)}
                                            className="w-20 bg-emerald-50/30 border border-emerald-100/50 p-2 rounded-lg text-center font-black text-xs text-emerald-600 outline-none focus:bg-white focus:border-emerald-200 transition-all"
                                        />
                                    </div>
                                </td>
                                {!readOnly && (
                                    <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-emerald-100/50 text-right">
                                        <button
                                            onClick={() => onRemove(item.id)}
                                            className="p-2 text-gray-300 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all"
                                        >
                                            <Trash2 className="w-4 h-4" />
                                        </button>
                                    </td>
                                )}
                            </tr>
                        ))}

                        {(!data || data.length === 0) && (
                            <tr>
                                <td colSpan={4} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
                                    No hay accesorios registrados
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="bg-gray-50/50 p-4 rounded-xl border border-dashed border-gray-200 text-center text-[10px] font-bold text-gray-400 uppercase tracking-widest">
                    Sección de accesorios contraída • {(data || []).length} items registrados
                </div>
            )}
        </div>
    );
}
