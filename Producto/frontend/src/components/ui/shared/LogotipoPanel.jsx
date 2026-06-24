import { Plus, Trash2, ChevronDown, ChevronUp } from 'lucide-react';
import { useState } from 'react';

export default function LogotipoPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
  const [isExpanded, setIsExpanded] = useState(true);

  return (
    <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
      <div className="flex justify-between items-center mb-6">
        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
          <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
          Logotipos
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
              Agregar Logotipo
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
                <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Tipo</th>
                <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Nombre</th>
                <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Ubicación</th>
                <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Color</th>
                <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Tamaño</th>
                 <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cant.</th>
                 <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-right">Precio</th>
                 <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-right">Subtotal</th>
                 {!readOnly && <th className="w-10"></th>}
              </tr>
            </thead>
            <tbody>
              {(data || []).map((item, index) => (
                <tr key={item.id || index} className="bg-white group hover:bg-gray-50/50 transition-all">
                  <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-gray-100">
                    <input
                      type="text"
                      value={item.tipo || ""}
                      readOnly={readOnly}
                      onChange={(e) => !readOnly && onUpdate(item.id, 'tipo', e.target.value)}
                      placeholder="Bordado..."
                      className={`w-full bg-transparent font-bold text-xs text-gray-700 outline-none ${readOnly ? 'cursor-default' : ''}`}
                    />
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100">
                    <input
                      type="text"
                      value={item.nombre || ""}
                      readOnly={readOnly}
                      onChange={(e) => !readOnly && onUpdate(item.id, 'nombre', e.target.value)}
                      placeholder="Logo Pecho..."
                      className={`w-full bg-transparent font-bold text-xs text-blue-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                    />
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100">
                    <input
                      type="text"
                      value={item.ubicacion || ""}
                      readOnly={readOnly}
                      onChange={(e) => !readOnly && onUpdate(item.id, 'ubicacion', e.target.value)}
                      placeholder="Manga Izq..."
                      className={`w-full bg-transparent font-bold text-xs text-gray-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                    />
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100">
                    <input
                      type="text"
                      value={item.color || ""}
                      readOnly={readOnly}
                      onChange={(e) => !readOnly && onUpdate(item.id, 'color', e.target.value)}
                      placeholder="Blanco..."
                      className={`w-full bg-transparent font-bold text-xs text-gray-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                    />
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                    <input
                      type="number"
                      step="0.1"
                      min="1"
                      value={item.tamanio || ""}
                      readOnly={readOnly}
                      onChange={(e) => {
                          if (readOnly) return;
                          const val = parseFloat(e.target.value);
                          onUpdate(item.id, 'tamanio', isNaN(val) || val < 1 ? "" : val);
                      }}
                      className="w-16 bg-gray-50 p-2 rounded-lg text-center font-black text-xs text-gray-700 outline-none border border-transparent focus:border-blue-100"
                    />
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                    <input
                      type="number"
                      min="1"
                      value={item.cantidad || ""}
                      readOnly={readOnly}
                      onChange={(e) => {
                          if (readOnly) return;
                          const val = parseInt(e.target.value);
                          onUpdate(item.id, 'cantidad', isNaN(val) || val < 1 ? "" : val);
                      }}
                      className="w-12 bg-blue-50/50 p-2 rounded-lg text-center font-black text-xs text-blue-600 outline-none border border-blue-100"
                    />
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-right">
                    <div className="flex items-center justify-end">
                      <span className="text-[10px] font-black text-gray-300 mr-1">$</span>
                      <input
                        type="number"
                        min="1"
                        value={item.precio || ""}
                        readOnly={readOnly}
                        onChange={(e) => {
                            if (readOnly) return;
                            const val = parseFloat(e.target.value);
                            onUpdate(item.id, 'precio', isNaN(val) || val < 1 ? "" : val);
                        }}
                        className="w-20 bg-transparent text-right font-black text-xs text-gray-700 outline-none"
                      />
                    </div>
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-right">
                    <span className="text-[11px] font-black text-blue-600">
                      ${((item.cantidad || 0) * (item.precio || 0)).toLocaleString('es-CL')}
                    </span>
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
                <tr key="empty-row">
                   <td colSpan={9} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
                     No hay logotipos registrados
                   </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="bg-gray-50/50 p-4 rounded-xl border border-dashed border-gray-200 text-center text-[10px] font-bold text-gray-400 uppercase tracking-widest">
            Sección de logotipos contraída • {(data || []).length} items registrados
        </div>
      )}
    </div>
  );
}
