import { Plus, Trash2 } from 'lucide-react';

export default function CintasPanel({ data, onAdd, onUpdate, onRemove }) {
  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center mb-6">
        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
          <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
          Cintas
        </h4>

        <button
          type="button"
          onClick={onAdd}
          className="flex items-center px-4 py-2 bg-gray-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all"
        >
          <Plus className="w-3.5 h-3.5 mr-2" />
          Agregar Cinta
        </button>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-left border-separate border-spacing-y-2">
          <thead>
            <tr>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Tipo</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Marca</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Medida (cm)</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cantidad</th>
              <th className="w-10"></th>
            </tr>
          </thead>
          <tbody>
            {(data || []).map((item, index) => (
              <tr key={item.id || index} className="bg-white group hover:bg-gray-50/50 transition-all">
                <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-gray-100">
                  <input
                    type="text"
                    value={item.tipo || ""}
                    onChange={(e) => onUpdate(item.id, 'tipo', e.target.value)}
                    placeholder="Reflectante..."
                    className="w-full bg-transparent font-bold text-xs text-gray-700 outline-none"
                  />
                </td>
                <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100">
                  <input
                    type="text"
                    value={item.marca || ""}
                    onChange={(e) => onUpdate(item.id, 'marca', e.target.value)}
                    placeholder="3M..."
                    className="w-full bg-transparent font-bold text-xs text-blue-600 outline-none"
                  />
                </td>
                <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                  <input
                    type="number"
                    step="0.1"
                    value={item.medida || 0}
                    onChange={(e) => onUpdate(item.id, 'medida', parseFloat(e.target.value) || 0)}
                    className="w-20 bg-gray-50 p-2 rounded-lg text-center font-black text-xs text-gray-700 outline-none border border-transparent focus:border-blue-100"
                  />
                </td>
                <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                  <input
                    type="number"
                    value={item.consumo || 0}
                    onChange={(e) => onUpdate(item.id, 'consumo', parseInt(e.target.value) || 0)}
                    className="w-16 bg-blue-50/50 p-2 rounded-lg text-center font-black text-xs text-blue-600 outline-none border border-blue-100"
                  />
                </td>
                <td className="px-4 py-3 last:rounded-r-2xl border-y border-r border-transparent group-hover:border-gray-100 text-right">
                  <button
                    onClick={() => onRemove(item.id)}
                    className="p-2 text-gray-200 hover:text-red-500 transition-colors"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </td>
              </tr>
            ))}

            {(!data || data.length === 0) && (
              <tr key="empty-row">
                <td colSpan={5} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
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
