import { Plus, Trash2 } from 'lucide-react';

export default function PrendasSCOTPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                    <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
                    Prendas a Cotizar
                </h4>
                {!readOnly && (
                    <button
                        type="button"
                        onClick={onAdd}
                        className="flex items-center px-4 py-2 bg-gray-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all"
                    >
                        <Plus className="w-3.5 h-3.5 mr-2" />
                        Agregar Prenda
                    </button>
                )}
            </div>

            <div className="overflow-x-auto">
                <table className="w-full text-left border-separate border-spacing-y-2">
                    <thead>
                    <tr>
                        <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Nombre Prenda</th>
                        <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Proveedor Referencia</th>
                        <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cantidad</th>
                        {!readOnly && <th className="w-10"></th>}
                    </tr>
                    </thead>
                    <tbody>
                    {(data || []).map((item, index) => (
                        <tr key={item.id || index} className="bg-white group hover:bg-gray-50/50 transition-all">
                            <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-gray-100">
                                <input
                                    type="text"
                                    value={item.nombre || ""}
                                    readOnly={readOnly}
                                    onChange={(e) => !readOnly && onUpdate(item.id, 'nombre', e.target.value)}
                                    placeholder="Ej: Chaqueta impermeable..."
                                    className={`w-full bg-transparent font-bold text-xs text-blue-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                                />
                            </td>
                            <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100">
                                <input
                                    type="text"
                                    value={item.proveedorReferencia || ""}
                                    readOnly={readOnly}
                                    onChange={(e) => !readOnly && onUpdate(item.id, 'proveedorReferencia', e.target.value)}
                                    placeholder="Ej: Proveedor XYZ..."
                                    className={`w-full bg-transparent font-bold text-xs text-gray-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                                />
                            </td>
                            <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                                <input
                                    type="number"
                                    value={item.cantidad || 0}
                                    readOnly={readOnly}
                                    onChange={(e) => !readOnly && onUpdate(item.id, 'cantidad', parseInt(e.target.value) || 0)}
                                    className="w-16 bg-blue-50/50 p-2 rounded-lg text-center font-black text-xs text-blue-600 outline-none border border-blue-100"
                                />
                            </td>
                            {!readOnly && (
                                <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-gray-100 text-right">
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
                        <tr>
                            <td colSpan={4} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
                                No hay prendas registradas
                            </td>
                        </tr>
                    )}
                    </tbody>

                    {/* Total prendas */}
                    {data && data.length > 0 && (
                        <tfoot>
                        <tr>
                            <td colSpan={2} className="px-4 pt-4 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">
                                Total unidades
                            </td>
                            <td className="px-4 pt-4 text-center font-black text-sm text-blue-600">
                                {(data || []).reduce((acc, p) => acc + (parseInt(p.cantidad) || 0), 0)}
                            </td>
                            {!readOnly && <td></td>}
                        </tr>
                        </tfoot>
                    )}
                </table>
            </div>
        </div>
    );
}
