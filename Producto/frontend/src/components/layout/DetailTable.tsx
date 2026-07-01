import { Plus, Trash2 } from 'lucide-react';

export default function DetailTable({ title, data, onAdd, onUpdate, onRemove, readOnly = false }) {
    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                    <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
                    {title}
                </h4>

                {!readOnly && (
                    <button
                        type="button"
                        onClick={onAdd}
                        className="flex items-center px-4 py-2 bg-gray-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all"
                    >
                        <Plus className="w-3.5 h-3.5 mr-2" />
                        Agregar Item
                    </button>
                )}
            </div>

            <div className="overflow-x-auto">
                <table className="w-full text-left">
                    <thead>
                        <tr className="border-b border-gray-100">
                            <th className="px-4 py-3 text-[9px] font-black text-gray-400 uppercase tracking-widest">
                                Descripción Insumo
                            </th>
                            <th className="px-4 py-3 text-[9px] font-black text-gray-400 uppercase tracking-widest">
                                Proveedor ref.
                            </th>
                            <th className="px-4 py-3 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">
                                Consumo
                            </th>
                            <th className="px-4 py-3 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">
                                Unidad
                            </th>
                            {!readOnly && <th className="px-4 py-3"></th>}
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-gray-50">
                        {(data || []).map((item, index) => (
                            <tr key={item.id || index} className="group hover:bg-gray-50/50 transition-colors">
                                <td className="px-4 py-4">
                                    <input
                                        type="text"
                                        readOnly={readOnly}
                                        className={`w-full bg-gray-50/50 p-2 rounded-lg font-bold text-xs text-gray-700 outline-none border border-transparent focus:border-blue-100 placeholder:text-gray-300 ${readOnly ? 'cursor-default' : ''}`}
                                        value={item.descripcion || ""}
                                        onChange={(e) => !readOnly && onUpdate(item.id, 'descripcion', e.target.value)}
                                        placeholder="Descripción Insumo..."
                                    />
                                </td>

                                <td className="px-4 py-4">
                                    <input
                                        type="text"
                                        readOnly={readOnly}
                                        className={`w-full bg-blue-50/30 p-2 rounded-lg font-bold text-[10px] text-blue-600 outline-none border border-blue-50 focus:border-blue-100 placeholder:text-blue-300 ${readOnly ? 'cursor-default' : ''}`}
                                        value={item.proveedorReferencia || ""}
                                        onChange={(e) => !readOnly && onUpdate(item.id, 'proveedorReferencia', e.target.value)}
                                        placeholder="Proveedor ref..."
                                    />
                                </td>

                                <td className="px-4 py-4 text-center">
                                    <input
                                        type="number"
                                        readOnly={readOnly}
                                        className="w-16 bg-blue-50/50 p-2 rounded-lg text-center font-black text-xs text-blue-600 outline-none border border-blue-100"
                                        value={item.consumo}
                                        onChange={(e) => !readOnly && onUpdate(item.id, 'consumo', parseFloat(e.target.value) || 0)}
                                    />
                                </td>

                                <td className="px-4 py-4 text-center">
                                    <select
                                        disabled={readOnly}
                                        className={`bg-gray-50/50 p-2 rounded-lg font-bold text-[10px] text-gray-600 outline-none border border-transparent focus:border-blue-100 ${readOnly ? 'cursor-not-allowed' : ''}`}
                                        value={item.unidadMedida || "un"}
                                        onChange={(e) => !readOnly && onUpdate(item.id, 'unidadMedida', e.target.value)}
                                    >
                                        <option value="un">UN</option>
                                        <option value="metros">MTS</option>
                                        <option value="kilos">KG</option>
                                        <option value="set">SET</option>
                                        <option value="par">PAR</option>
                                    </select>
                                </td>

                                {!readOnly && (
                                    <td className="px-4 py-4 text-right">
                                        <button
                                            onClick={() => onRemove(item.id)}
                                            className="p-2 text-gray-200 hover:text-red-500 transition-colors"
                                        >
                                            <Trash2 className="w-3.5 h-3.5" />
                                        </button>
                                    </td>
                                )}
                            </tr>
                        ))}

                        {(!data || data.length === 0) && (
                            <tr key="empty-row">
                                <td colSpan={5} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
                                    No hay ítems registrados
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}