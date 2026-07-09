import { Plus, Trash2 } from 'lucide-react';
import { clampNonNegative } from '../../utils/validations';

export default function CintasPanel({ data, onAdd, onUpdate, onRemove }) {
  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center mb-6">
        <h4 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
          <div className="w-1.5 h-6 bg-primary rounded-full"></div>
          Cintas
        </h4>

        <button
          type="button"
          onClick={onAdd}
          className="flex items-center px-4 py-2 bg-foreground text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-primary transition-all"
        >
          <Plus className="w-3.5 h-3.5 mr-2" />
          Agregar Cinta
        </button>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-left border-separate border-spacing-y-2">
          <thead>
            <tr>
              <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Tipo</th>
              <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Marca</th>
              <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Medida (cm)</th>
              <th className="px-4 py-2 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Cantidad</th>
              <th className="w-10"></th>
            </tr>
          </thead>
          <tbody>
            {(data || []).map((item, index) => (
              <tr key={item.id || index} className="bg-card group hover:bg-muted/50 transition-all">
                <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-border">
                  <input
                    type="text"
                    value={item.tipo || ""}
                    onChange={(e) => onUpdate(item.id, 'tipo', e.target.value)}
                    placeholder="Reflectante..."
                    className="w-full bg-transparent font-bold text-xs text-foreground outline-none"
                  />
                </td>
                <td className="px-4 py-3 border-y border-transparent group-hover:border-border">
                  <input
                    type="text"
                    value={item.marca || ""}
                    onChange={(e) => onUpdate(item.id, 'marca', e.target.value)}
                    placeholder="3M..."
                    className="w-full bg-transparent font-bold text-xs text-accent-foreground outline-none"
                  />
                </td>
                <td className="px-4 py-3 border-y border-transparent group-hover:border-border text-center">
                  <input
                    type="number"
                    step="0.1"
                    min="0"
                    value={item.medida || 0}
                    onChange={(e) => onUpdate(item.id, 'medida', Math.max(0, parseFloat(e.target.value) || 0))}
                    className="w-20 bg-muted p-2 rounded-lg text-center font-black text-xs text-foreground outline-none border border-transparent focus:border-accent"
                  />
                </td>
                <td className="px-4 py-3 border-y border-transparent group-hover:border-border text-center">
                  <input
                    type="number"
                    min="0"
                    value={item.consumo || 0}
                    onChange={(e) => onUpdate(item.id, 'consumo', Math.max(0, parseInt(e.target.value) || 0))}
                    className="w-16 bg-accent/50 p-2 rounded-lg text-center font-black text-xs text-accent-foreground outline-none border border-accent"
                  />
                </td>
                <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-border text-right">
                  <button
                    onClick={() => onRemove(item.id)}
                    className="p-2 text-muted-foreground hover:text-destructive transition-colors"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </td>
              </tr>
            ))}

            {(!data || data.length === 0) && (
              <tr key="empty-row">
                <td colSpan={5} className="py-12 text-center text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
                  No hay cintas registradas
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
