import { Plus, Trash2 } from 'lucide-react';
import ProveedorComboField from '../shared/ProveedorComboField';

export default function DetailTable({ title, data, onAdd, onUpdate, onRemove, readOnly = false }) {
    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <h4 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
                    <div className="w-1.5 h-6 bg-primary rounded-full"></div>
                    {title}
                </h4>

                {!readOnly && (
                    <button
                        type="button"
                        onClick={onAdd}
                        className="flex items-center px-4 py-2 bg-foreground text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-primary transition-all"
                    >
                        <Plus className="w-3.5 h-3.5 mr-2" />
                        Agregar Item
                    </button>
                )}
            </div>

            <div className="overflow-x-auto">
                <table className="w-full text-left">
                    <thead>
                        <tr className="border-b border-border">
                            <th className="px-4 py-3 text-[9px] font-black text-muted-foreground uppercase tracking-widest">
                                Descripción Insumo
                            </th>
                            <th className="px-4 py-3 text-[9px] font-black text-muted-foreground uppercase tracking-widest">
                                Proveedor ref.
                            </th>
                            <th className="px-4 py-3 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">
                                Consumo
                            </th>
                            <th className="px-4 py-3 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">
                                Unidad
                            </th>
                            {!readOnly && <th className="px-4 py-3"></th>}
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-border">
                        {(data || []).map((item, index) => (
                            <tr key={item.id || index} className="group hover:bg-muted/50 transition-colors">
                                <td className="px-4 py-4">
                                    <input
                                        type="text"
                                        readOnly={readOnly}
                                        className={`w-full bg-muted/50 p-2 rounded-lg font-bold text-xs text-foreground outline-none border border-transparent focus:border-accent placeholder:text-muted-foreground ${readOnly ? 'cursor-default' : ''}`}
                                        value={item.descripcion || ""}
                                        onChange={(e) => !readOnly && onUpdate(item.id, 'descripcion', e.target.value)}
                                        placeholder="Descripción Insumo..."
                                    />
                                </td>

                                <td className="px-4 py-4">
                                    <ProveedorComboField
                                        value={item.proveedorReferencia || ''}
                                        onChange={(val) => !readOnly && onUpdate(item.id, 'proveedorReferencia', val)}
                                        readOnly={readOnly}
                                        placeholder="Proveedor ref..."
                                        className="min-w-[180px]"
                                    />
                                </td>

                                <td className="px-4 py-4 text-center">
                                    <input
                                        type="number"
                                        readOnly={readOnly}
                                        className="w-16 bg-accent/50 p-2 rounded-lg text-center font-black text-xs text-accent-foreground outline-none border border-accent"
                                        value={item.consumo}
                                        onChange={(e) => !readOnly && onUpdate(item.id, 'consumo', parseFloat(e.target.value) || 0)}
                                    />
                                </td>

                                <td className="px-4 py-4 text-center">
                                    <select
                                        disabled={readOnly}
                                        className={`bg-muted/50 p-2 rounded-lg font-bold text-[10px] text-muted-foreground outline-none border border-transparent focus:border-accent ${readOnly ? 'cursor-not-allowed' : ''}`}
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
                                            className="p-2 text-muted-foreground hover:text-destructive transition-colors"
                                        >
                                            <Trash2 className="w-3.5 h-3.5" />
                                        </button>
                                    </td>
                                )}
                            </tr>
                        ))}

                        {(!data || data.length === 0) && (
                            <tr key="empty-row">
                                <td colSpan={5} className="py-12 text-center text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
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