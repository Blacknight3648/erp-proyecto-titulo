import React from 'react';
import { Trash2, Plus } from 'lucide-react';

export default function CosteoTable({ title, insumos, onUpdateItem, onRemoveItem, onAddItem, disabled }) {
    const isLogoTable = insumos.length > 0 && insumos[0].categoryId === 'logotipo';

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
            <div className="p-6 border-b border-slate-100 bg-slate-50/50 flex justify-between items-center">
                <div className="flex items-center gap-3">
                    <div className="w-1.5 h-6 bg-blue-600 rounded-full"></div>
                    <h3 className="text-sm font-black text-slate-800 uppercase tracking-widest">{title || 'Desglose de Materias Primas'}</h3>
                </div>
                {!disabled && onAddItem && (
                    <button
                        onClick={onAddItem}
                        className="px-4 py-2 bg-slate-900 text-white text-[10px] font-black rounded-xl uppercase tracking-widest hover:bg-green-600 transition-all flex items-center"
                    >
                        <Plus className="w-3 h-3 mr-2" />
                        Agregar Item
                    </button>
                )}
            </div>

            <div className="overflow-x-auto">
                <table className="w-full text-left min-w-[800px]">
                    <thead className="bg-white border-b border-slate-100">
                        {!isLogoTable ? (
                            <tr>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Cat.</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Producto / Insumo</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Prov. Ref.</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Consumo</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">UM</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Costo</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Subtotal</th>
                                <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider text-center"></th>
                            </tr>
                        ) : (
                            <tr>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Ubicación</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Nombre</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Descripción</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Técnica</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Cantidad</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Costo</th>
                                <th className="px-6 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Subtotal</th>
                                <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider text-center"></th>
                            </tr>
                        )}
                    </thead>
                    <tbody className="divide-y divide-slate-50">
                        {insumos.map((item, index) => {
                            const isNew = item.id?.toString().startsWith('NEW-');
                            const isLogo = item.categoryId === 'logotipo';

                            if (!isLogo) {
                                return (
                                    <tr key={item.id || index} className="hover:bg-slate-50/50 transition-all">
                                        <td className="px-6 py-4">
                                            <span className="text-[9px] font-black px-2 py-1 bg-slate-100 text-slate-400 rounded-md uppercase tracking-tighter">
                                                {item.category || 'Gral'}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4">
                                            <input
                                                type="text"
                                                disabled={disabled}
                                                className={`w-full bg-transparent border-none text-sm font-bold ${disabled || !isNew ? 'text-slate-400' : 'text-slate-700'} focus:ring-0`}
                                                value={item.producto}
                                                onChange={(e) => onUpdateItem(item.id, 'producto', e.target.value)}
                                                placeholder="Producto..."
                                            />
                                        </td>
                                        <td className="px-6 py-4">
                                            <input
                                                type="text"
                                                disabled={disabled}
                                                className={`w-full bg-transparent border-none text-xs font-bold ${disabled ? 'text-slate-400' : 'text-slate-600'} focus:ring-0`}
                                                value={item.proveedorReferencia || ''}
                                                onChange={(e) => onUpdateItem(item.id, 'proveedorReferencia', e.target.value)}
                                                placeholder="Prov. Ref..."
                                            />
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            <input
                                                type="number"
                                                disabled={disabled}
                                                className={`w-20 bg-transparent border-none text-sm font-black ${disabled || !isNew ? 'text-slate-400' : 'text-slate-800'} text-center focus:ring-0`}
                                                value={item.cantidad}
                                                onChange={(e) => {
                                                    const val = e.target.value;
                                                    onUpdateItem(item.id, 'cantidad', val === '' ? '' : Math.max(0, parseFloat(val) || 0));
                                                }}
                                            />
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            <select
                                                disabled={disabled}
                                                className={`w-full bg-transparent border-none text-xs font-bold ${disabled ? 'text-slate-400' : 'text-slate-500'} text-center focus:ring-0 appearance-none cursor-pointer`}
                                                value={item.unidad || 'und'}
                                                onChange={(e) => onUpdateItem(item.id, 'unidad', e.target.value)}
                                            >
                                                <option value="m">m</option>
                                                <option value="kg">kg</option>
                                                <option value="und">und</option>
                                                <option value="paq">paq</option>
                                                <option value="caja">caja</option>
                                                <option value="lt">lt</option>
                                            </select>
                                        </td>
                                        <td className="px-6 py-4">
                                            <div className="flex items-center justify-end">
                                                <span className="text-slate-400 text-xs mr-1">$</span>
                                                <input
                                                    type="number"
                                                    disabled={disabled}
                                                    className={`w-24 bg-transparent border-none text-sm font-black ${disabled ? 'text-slate-400' : 'text-slate-800'} text-right focus:ring-0`}
                                                    value={item.costo}
                                                    onChange={(e) => {
                                                        const val = e.target.value;
                                                        onUpdateItem(item.id, 'costo', val === '' ? '' : Math.max(0, parseFloat(val) || 0));
                                                    }}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <span className="text-sm font-black text-indigo-600">
                                                ${((parseFloat(item.costo) || 0) * (parseFloat(item.cantidad) || 0)).toLocaleString('es-CL')}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            {!disabled && onRemoveItem && (
                                                <button
                                                    onClick={() => onRemoveItem(item.id)}
                                                    className="p-2 text-slate-300 hover:text-red-500 transition-colors"
                                                >
                                                    <Trash2 className="w-4 h-4" />
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                );
                            } else {
                                // Render para Logotipos
                                return (
                                    <tr key={item.id || index} className="hover:bg-slate-50/50 transition-all">
                                        <td className="px-6 py-4">
                                            <input
                                                type="text"
                                                disabled={disabled}
                                                className={`w-full bg-transparent border-none text-xs font-bold ${disabled ? 'text-slate-400' : 'text-slate-700'} focus:ring-0`}
                                                value={item.ubicacion || ''}
                                                onChange={(e) => onUpdateItem(item.id, 'ubicacion', e.target.value)}
                                            />
                                        </td>
                                        <td className="px-6 py-4">
                                            <input
                                                type="text"
                                                disabled={disabled}
                                                className={`w-full bg-transparent border-none text-sm font-bold ${disabled || !isNew ? 'text-slate-400' : 'text-slate-700'} focus:ring-0`}
                                                value={item.producto}
                                                onChange={(e) => onUpdateItem(item.id, 'producto', e.target.value)}
                                            />
                                        </td>
                                        <td className="px-6 py-4 text-xs text-slate-400 font-bold uppercase truncate max-w-[100px]">
                                            {item.descripcion || 'S/D'}
                                        </td>
                                        <td className="px-6 py-4 text-xs text-slate-400 font-bold">
                                            {item.tipo || 'S/T'}
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            <input
                                                type="number"
                                                disabled={disabled}
                                                className={`w-20 bg-transparent border-none text-sm font-black ${disabled || !isNew ? 'text-slate-400' : 'text-slate-800'} text-center focus:ring-0`}
                                                value={item.cantidad}
                                                onChange={(e) => {
                                                    const val = e.target.value;
                                                    onUpdateItem(item.id, 'cantidad', val === '' ? '' : Math.max(0, parseFloat(val) || 0));
                                                }}
                                            />
                                        </td>
                                        <td className="px-6 py-4">
                                            <div className="flex items-center justify-end">
                                                <span className="text-slate-400 text-xs mr-1">$</span>
                                                <input
                                                    type="number"
                                                    disabled={disabled}
                                                    className={`w-24 bg-transparent border-none text-sm font-black ${disabled ? 'text-slate-400' : 'text-slate-800'} text-right focus:ring-0`}
                                                    value={item.costo}
                                                    onChange={(e) => {
                                                        const val = e.target.value;
                                                        onUpdateItem(item.id, 'costo', val === '' ? '' : Math.max(0, parseFloat(val) || 0));
                                                    }}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <span className="text-sm font-black text-indigo-600">
                                                ${((parseFloat(item.costo) || 0) * (parseFloat(item.cantidad) || 0)).toLocaleString('es-CL')}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            {!disabled && onRemoveItem && (
                                                <button
                                                    onClick={() => onRemoveItem(item.id)}
                                                    className="p-2 text-slate-300 hover:text-red-500 transition-colors"
                                                >
                                                    <Trash2 className="w-4 h-4" />
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                );
                            }
                        })}
                        {insumos.length === 0 && (
                            <tr>
                                <td colSpan="9" className="px-6 py-12 text-center text-slate-400 font-bold text-sm uppercase">
                                    No hay insumos registrados
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
