import { Plus, Trash2, ChevronDown, ChevronUp } from 'lucide-react';
import { useState } from 'react';
import ComboSearchField from './ComboSearchField';
import InlineComboField from './InlineComboField';

const APLICACIONES = [
    'CUERPO', 'FORRO', 'MANGAS', 'CUELLO', 'CAPUCHA', 'BOLSILLOS',
    'PRETINA', 'ESPALDA', 'PECHERA', 'PUÑOS', 'ENTRETELADO',
    'PANEL TRANSPIRABLE', 'LATERAL', 'RIBETE',
];

const COMPOSICIONES = [
    '100% ALGODÓN', '100% POLIÉSTER', '100% NYLON', '100% LANA', '100% VISCOSA',
    '65% POLIÉSTER 35% ALGODÓN', '50% ALGODÓN 50% POLIÉSTER',
    '95% POLIÉSTER 5% ELASTANO', '80% NYLON 20% ELASTANO',
    '60% ALGODÓN 40% POLIÉSTER', '100% ACRÍLICO',
];

const COLORES = [
    'BLANCO', 'NEGRO', 'GRIS', 'GRIS CLARO', 'GRIS OSCURO',
    'AZUL MARINO', 'AZUL ROYAL', 'CELESTE', 'ROJO', 'VERDE',
    'VERDE LIMÓN', 'AMARILLO', 'NARANJA', 'BEIGE', 'CAFÉ',
    'MORADO', 'BURDEO', 'INSTITUCIONAL', 'A DEFINIR',
];

export default function TelasSCOSPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
    const [isExpanded, setIsExpanded] = useState(true);

    return (
        <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h4 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
                        <div className="w-1 h-5 bg-primary rounded-sm" />
                        Telas
                        <span className="ml-1 px-2 py-0.5 bg-accent text-accent-foreground text-[9px] rounded border border-accent font-black">
                            {(data || []).length} ITEMS
                        </span>
                    </h4>
                    <p className="text-[10px] text-muted-foreground mt-1 ml-3">
                        Especifique cada tejido que compone la prenda. Cada fila corresponde a un corte o pieza de confección: cuerpo, forro, panel transpirable, mangas, etc. La composición y el gramaje son necesarios para el cálculo de consumo y costo de material.
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
                            Agregar Tela
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
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Aplicación</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Nombre Tela</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Composición</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Color</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Peso</th>
                                <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">U. Medida</th>
                                {!readOnly && <th className="w-10" />}
                            </tr>
                        </thead>
                        <tbody>
                            {(data || []).map((item, index) => (
                                <tr key={item.id || index} className="group hover:bg-accent/30 transition-all duration-200">
                                    <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-accent/50">
                                        <InlineComboField
                                            value={item.aplicacion || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'aplicacion', val)}
                                            options={APLICACIONES}
                                            placeholder="Cuerpo..."
                                            readOnly={readOnly}
                                            className="min-w-[130px]"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-accent/50">
                                        <ComboSearchField
                                            tipo="TELA"
                                            value={item.nombre || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'nombre', val)}
                                            placeholder="Buscar tela..."
                                            readOnly={readOnly}
                                            className="min-w-[160px]"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-accent/50">
                                        <InlineComboField
                                            value={item.composicion || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'composicion', val)}
                                            options={COMPOSICIONES}
                                            placeholder="100%..."
                                            readOnly={readOnly}
                                            className="min-w-[160px]"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-accent/50">
                                        <InlineComboField
                                            value={item.color || ''}
                                            onChange={(val) => !readOnly && onUpdate(item.id, 'color', val)}
                                            options={COLORES}
                                            placeholder="Color..."
                                            readOnly={readOnly}
                                            className="min-w-[130px]"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-accent/50 text-center">
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="1"
                                            value={item.peso || ""}
                                            readOnly={readOnly}
                                            onChange={(e) => {
                                                if (readOnly) return;
                                                const val = parseFloat(e.target.value);
                                                onUpdate(item.id, 'peso', isNaN(val) || val < 1 ? "" : val);
                                            }}
                                            className="w-20 bg-muted p-2 rounded-lg text-center font-black text-xs text-foreground outline-none border border-border focus:bg-card focus:border-primary transition-all"
                                        />
                                    </td>
                                    <td className="px-4 py-3 border-y border-transparent group-hover:border-accent/50 text-center">
                                        <select
                                            value={item.unidadMedida || 'MTRS'}
                                            disabled={readOnly}
                                            onChange={(e) => !readOnly && onUpdate(item.id, 'unidadMedida', e.target.value)}
                                            className={`w-18 bg-accent/30 border border-accent/50 p-2 rounded-lg text-center font-black text-[10px] text-accent-foreground outline-none focus:bg-card focus:border-primary transition-all ${readOnly ? 'cursor-default opacity-70' : 'cursor-pointer'}`}
                                        >
                                            <option value="KG">KG</option>
                                            <option value="MTRS">MTRS</option>
                                            <option value="Unidades">Unidades</option>
                                        </select>
                                    </td>
                                    {!readOnly && (
                                        <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-accent/50 text-right">
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
                                    <td colSpan={7} className="py-12 text-center text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
                                        No hay telas registradas
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="bg-muted/50 p-4 rounded-xl border border-dashed border-border text-center text-[10px] font-bold text-muted-foreground uppercase tracking-widest">
                    Sección de telas contraída · {(data || []).length} items registrados
                </div>
            )}
        </div>
    );
}
