import { Plus, Trash2, ChevronDown, ChevronUp } from 'lucide-react';
import { useState } from 'react';

export default function TelasSCOSPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
    const [isExpanded, setIsExpanded] = useState(true);

    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                    <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
                    Telas
                    <span className="ml-2 px-2 py-0.5 bg-blue-50 text-blue-600 text-[9px] rounded-full border border-blue-100">
                        {(data || []).length} ITEMS
                    </span>
                </h4>
                <div className="flex items-center gap-2">
                    {!readOnly && (
                        <button
                            type="button"
                            onClick={onAdd}
                            className="flex items-center px-4 py-2 bg-gray-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all"
                        >
                            <Plus className="w-3.5 h-3.5 mr-2" />
                            Agregar Tela
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
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Aplicación</th>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Nombre Tela</th>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Composición</th>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Color</th>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Peso</th>
                            <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">U. Medida</th>
                            {!readOnly && <th className="w-10"></th>}
                        </tr>
                        </thead>
                        <tbody>
                        {(data || []).map((item, index) => (
                            <tr key={item.id || index} className="group hover:bg-blue-50/30 transition-all duration-200">
                                <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-blue-100/50">
                                    <div className="flex flex-col gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Aplicación</label>
                                        <input
                                            type="text"
                                            value={item.aplicacion || ""}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'aplicacion', e.target.value)}
                                            placeholder="Cuerpo..."
                                            className={`w-full bg-gray-50/50 border border-gray-100/50 rounded-lg p-2 text-xs font-bold text-gray-700 outline-none focus:bg-white focus:border-blue-200 transition-all ${readOnly ? 'cursor-default' : ''}`}
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-blue-100/50">
                                    <div className="flex flex-col gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Nombre Tela</label>
                                        <input
                                            type="text"
                                            value={item.nombre || ""}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'nombre', e.target.value)}
                                            placeholder="Nombre..."
                                            className={`w-full bg-gray-50/50 border border-gray-100/50 rounded-lg p-2 text-xs font-black text-blue-600 outline-none focus:bg-white focus:border-blue-200 transition-all ${readOnly ? 'cursor-default' : ''}`}
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-blue-100/50">
                                    <div className="flex flex-col gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Composición</label>
                                        <input
                                            type="text"
                                            value={item.composicion || ""}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'composicion', e.target.value)}
                                            placeholder="100%..."
                                            className={`w-full bg-gray-50/50 border border-gray-100/50 rounded-lg p-2 text-xs font-bold text-gray-600 outline-none focus:bg-white focus:border-blue-200 transition-all ${readOnly ? 'cursor-default' : ''}`}
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-blue-100/50">
                                    <div className="flex flex-col gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Color</label>
                                        <input
                                            type="text"
                                            value={item.color || ""}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'color', e.target.value)}
                                            placeholder="Color..."
                                            className={`w-full bg-gray-50/50 border border-gray-100/50 rounded-lg p-2 text-xs font-bold text-gray-600 outline-none focus:bg-white focus:border-blue-200 transition-all ${readOnly ? 'cursor-default' : ''}`}
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-blue-100/50 text-center">
                                    <div className="flex flex-col items-center gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">Peso</label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            value={item.peso || 0}
                                            readOnly={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'peso', parseFloat(e.target.value) || 0)}
                                            className="w-20 bg-gray-50 p-2 rounded-lg text-center font-black text-xs text-gray-800 outline-none border border-gray-100 focus:bg-white focus:border-blue-200 transition-all"
                                        />
                                    </div>
                                </td>
                                <td className="px-4 py-3 border-y border-transparent group-hover:border-blue-100/50 text-center">
                                    <div className="flex flex-col items-center gap-1">
                                        <label className="lg:hidden text-[7px] font-black text-gray-400 uppercase">U.M.</label>
                                        <select
                                            value={item.unidadMedida || "MTRS"}
                                            disabled={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'unidadMedida', e.target.value)}
                                            className={`w-18 bg-blue-50/30 border border-blue-100/50 p-2 rounded-lg text-center font-black text-[10px] text-blue-600 outline-none focus:bg-white focus:border-blue-200 transition-all ${readOnly ? 'cursor-default opacity-70' : 'cursor-pointer'}`}
                                        >
                                            <option value="KG">KG</option>
                                            <option value="MTRS">MTRS</option>
                                            <option value="Unidades">Unidades</option>
                                        </select>
                                    </div>
                                </td>
                                {!readOnly && (
                                    <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-blue-100/50 text-right">
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
                                <td colSpan={7} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
                                    No hay telas registradas
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="bg-gray-50/50 p-4 rounded-xl border border-dashed border-gray-200 text-center text-[10px] font-bold text-gray-400 uppercase tracking-widest">
                    Sección de telas contraída • {(data || []).length} items registrados
                </div>
            )}
        </div>
    );
}
