import { Plus, Trash2, ExternalLink } from 'lucide-react';

export default function ProductosCotizacionPanel({ data, onAdd, onUpdate, onRemove, readOnly = false }) {
  return (
    <div className={`space-y-4 ${readOnly ? 'opacity-95' : ''}`}>
      <div className="flex justify-between items-center mb-6">
        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
          <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
          Productos / Artículos
        </h4>

        {!readOnly && (
          <button
            type="button"
            onClick={onAdd}
            className="flex items-center px-4 py-2 bg-gray-900 text-white rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-blue-600 transition-all"
          >
            <Plus className="w-3.5 h-3.5 mr-2" />
            Agregar Artículo
          </button>
        )}
      </div>

      <div className="overflow-x-auto overflow-y-visible pb-10">
        <table className="w-full text-left border-separate border-spacing-y-2">
          <thead>
            <tr>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Descripción del Producto</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Proveedor ref.</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest">Link ref.</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Tallas</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cant. Tallas</th>
              <th className="px-4 py-2 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cant. Total</th>
              {!readOnly && <th className="w-10"></th>}
            </tr>
          </thead>
          <tbody>
            {(data || []).map((item, index) => {
              // Cálculo de resúmenes para la tabla
              const tallasStr = item.tipoTalla === 'POR_DEFINIR' 
                ? 'POR DEFINIR' 
                : (item.tallas || []).filter(t => t.cantidad > 0).map(t => t.talla).join(", ") || "SIN TALLAS";
              
              const cantTallasStr = item.tipoTalla === 'POR_DEFINIR'
                ? '-'
                : `${(item.tallas || []).filter(t => t.cantidad > 0).length} tallas`;

              return (
                <tr key={item.id || index} className="bg-white group hover:bg-gray-50/50 transition-all">
                  <td className="px-4 py-3 first:rounded-l-2xl border-y border-l border-transparent group-hover:border-gray-100 min-w-[200px]">
                    <div className="flex flex-col">
                      <input
                        type="text"
                        value={item.descripcion || item.nombre || ""}
                        readOnly={readOnly}
                        onChange={(e) => !readOnly && onUpdate(item.id, 'descripcion', e.target.value)}
                        placeholder="Ej: Polera deportiva..."
                        className={`w-full bg-transparent font-black text-xs text-blue-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                      />
                      <span className="text-[8px] font-bold text-gray-400 uppercase tracking-tighter">Descripción</span>
                    </div>
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100">
                    <div className="flex flex-col">
                      <input
                        type="text"
                        value={item.proveedor || item.proveedorReferencia || ""}
                        readOnly={readOnly}
                        onChange={(e) => !readOnly && onUpdate(item.id, 'proveedor', e.target.value)}
                        placeholder="Shein / Fabricante X"
                        className={`w-full bg-transparent font-bold text-xs text-gray-600 outline-none ${readOnly ? 'cursor-default' : ''}`}
                      />
                      <span className="text-[8px] font-bold text-gray-400 uppercase tracking-tighter">Proveedor</span>
                    </div>
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 relative group/link min-w-[150px]">
                    <div className="flex flex-col">
                      <div className="flex items-center gap-2">
                        <input
                          type="url"
                          value={item.linkReferencia || ""}
                          readOnly={readOnly}
                          onChange={(e) => !readOnly && onUpdate(item.id, 'linkReferencia', e.target.value)}
                          placeholder="https://..."
                          className={`w-full bg-transparent font-bold text-xs text-gray-400 focus:text-blue-500 outline-none truncate ${readOnly ? 'cursor-default' : ''}`}
                        />
                        {item.linkReferencia && (
                          <a href={item.linkReferencia} target="_blank" rel="noopener noreferrer" className="text-gray-300 hover:text-blue-500 transition-colors" title="Abrir enlace">
                            <ExternalLink className="w-4 h-4" />
                          </a>
                        )}
                      </div>
                      <span className="text-[8px] font-bold text-gray-400 uppercase tracking-tighter">Enlace</span>
                    </div>
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center min-w-[120px]">
                    <div className="bg-gray-50 px-3 py-1.5 rounded-lg inline-block min-w-[80px]">
                      <span className="block text-[9px] font-black text-gray-700 uppercase truncate max-w-[100px]" title={tallasStr}>
                        {tallasStr}
                      </span>
                      <span className="block text-[8px] font-bold text-gray-400 uppercase tracking-tighter">Tallas</span>
                    </div>
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                    <div className="flex flex-col items-center">
                      <span className="text-[10px] font-black text-gray-800 uppercase">{cantTallasStr}</span>
                      <span className="text-[8px] font-bold text-gray-400 uppercase tracking-tighter">Variedad</span>
                    </div>
                  </td>
                  <td className="px-4 py-3 border-y border-transparent group-hover:border-gray-100 text-center">
                    <div className="flex flex-col items-center">
                      <div className="bg-indigo-50 text-indigo-600 px-3 py-1 rounded-lg font-black text-xs">
                        {item.cantidadTotal || item.cantidad || 0}
                      </div>
                      <span className="text-[8px] font-bold text-gray-400 uppercase tracking-tighter mt-0.5">Total</span>
                    </div>
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
              );
            })}

            {(!data || data.length === 0) && (
              <tr key="empty-row">
                <td colSpan={readOnly ? 6 : 7} className="py-12 text-center text-[10px] font-black text-gray-200 uppercase tracking-widest italic">
                  No hay productos registrados
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
