import { Plus, Trash2, ChevronDown, ChevronUp } from 'lucide-react';
import { useState } from 'react';
import ComboSearchField from './ComboSearchField';
import InlineComboField from './InlineComboField';

const TIPOS_ACCESORIO = [
    'CIERRE', 'CREMALLERA', 'BOTÓN', 'HEBILLA', 'HILO', 'HILO DE CONTRASTE',
    'PARCHE', 'ETIQUETA', 'VELCRO', 'RIBETE', 'CORCHETE', 'BROCHE',
    'REMACHE', 'ARGOLLA', 'CINTA', 'ELÁSTICO', 'TIRA', 'CORDÓN',
];

export default function AccesoriosSCOSPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
    const [isExpanded, setIsExpanded] = useState(true);

    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h4 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
                        <div className="w-1 h-5 bg-success rounded-sm" />
                        Accesorios
                        <span className="ml-1 px-2 py-0.5 bg-success-bg text-success text-[9px] rounded border border-success-bg font-black">
                            {(data || []).length} ITEMS
                        </span>
                    </h4>
                    <p className="text-[10px] text-muted-foreground mt-1 ml-3">
                        Registre todos los avíos requeridos: cierres, botones, hebillas, hilos de contraste, parches, etiquetas, etc. La cantidad ingresada se validará contra el inventario disponible al momento de aprobar la solicitud.
                    </p>
                </div>
                <div className="flex items-center gap-2">
                    {!readOnly && (
                        <button
                            type="button"
                            onClick={onAdd}
                            className="flex items-center px-4 py-2 bg-foreground text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-primary transition-all"
                        >
                            <Plus className="w-3.5 h-3.5 mr-2" />
                            Agregar Accesorio
                        </button>
                    )}
                    <button
                        type="button"
                        onClick={() => setIsExpanded(!isExpanded)}
                        className="p-2 bg-muted text-muted-foreground rounded-xl hover:text-accent-foreground hover:bg-accent transition-all"
                    >
                        {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                    </button>
                </div>
            </div>

            {isExpanded ? (
                <div className="bg-card p-6 rounded-2xl border border-border shadow-sm animate-in slide-in-from-top-2 duration-300">
                    <table className="w-full text-left border-separate border-spacing-y-2">
                        <thead>
                            <tr>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Nombre Accesorio</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Tipo</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Cantidad</th>
                                {!readOnly && <th className="w-10" />}
                            </tr>
                        </thead>
                        <tbody>
                            {(data || []).map((item, index) => (
                                <tr key={item.id || index} className="group hover:bg-success-bg/30 transition-all duration-200">
                                    <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-success-bg/50">
                                        <ComboSearchField
                                            tipo="ACCESORIO"
                                            value={item.nombreAccesorio || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'nombreAccesorio', val)}
                                            placeholder="Buscar accesorio..."
                                            readOnly={readOnly}
                                            className="min-w-[180px]"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-success-bg/50">
                                        <InlineComboField
                                            value={item.tipo || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'tipo', val)}
                                            options={TIPOS_ACCESORIO}
                                            placeholder="Tipo..."
                                            readOnly={readOnly}
                                            className="min-w-[140px]"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-success-bg/50 text-center">
                                        <input
                                            type="number"
                                            min="1"
                                            value={item.cantidad ?? ""}
                                            readOnly={readOnly}
                                            onChange={(e) => {
                                                if (readOnly) return;
                                                const val = parseInt(e.target.value);
                                                onUpdate(item.id, 'cantidad', isNaN(val) || val < 1 ? "" : val);
                                            }}
                                            className="w-20 bg-success-bg/30 border border-success-bg/50 p-2 rounded-lg text-center font-black text-xs text-success outline-none focus:bg-card focus:border-success transition-all"
                                        />
                                    </td>
                                    {!readOnly && (
                                        <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-success-bg/50 text-right">
                                            <button
                                                onClick={() => onRemove(item.id)}
                                                className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-all"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </button>
                                        </td>
                                    )}
                                </tr>
                            ))}

                            {(!data || data.length === 0) && (
                                <tr>
                                    <td colSpan={4} className="py-12 text-center text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
                                        No hay accesorios registrados
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="bg-muted/50 p-4 rounded-xl border border-dashed border-border text-center text-[10px] font-bold text-muted-foreground uppercase tracking-widest">
                    Sección de accesorios contraída · {(data || []).length} items registrados
                </div>
            )}
        </div>
    );
}
