import { useMemo, useState } from 'react';
import { ChevronLeft, Save, AlertCircle, Package, Layers, Truck } from 'lucide-react';

/**
 * Pantalla para generar una OC consolidada desde HCs aprobadas.
 * Flujo:
 *   1. Elegir proveedor (numérico por ahora)
 *   2. Marcar items de las HCs aprobadas que quieres consolidar
 *   3. Opcional: fecha de entrega + observaciones
 *   4. Generar → POST /api/v1/ordenes-compra/consolidar
 */
export default function EmisorCompraProduccion({
    onBack,
    hcsAprobadas,
    onGenerar,
    submitting,
    error,
    formatCLP,
}) {
    const [proveedorId, setProveedorId] = useState('');
    const [fechaEntregaEstimada, setFechaEntregaEstimada] = useState('');
    const [observaciones, setObservaciones] = useState('');
    const [selectedItemIds, setSelectedItemIds] = useState(new Set());

    const toggleItem = (itemId) => {
        const next = new Set(selectedItemIds);
        if (next.has(itemId)) next.delete(itemId);
        else next.add(itemId);
        setSelectedItemIds(next);
    };

    const selectedItems = useMemo(() => {
        const result = [];
        for (const hc of (hcsAprobadas || [])) {
            for (const item of (hc.items || [])) {
                if (selectedItemIds.has(item.idHCItem)) {
                    result.push({ ...item, hcId: hc.idHC, hcNumero: hc.numeroHC });
                }
            }
        }
        return result;
    }, [hcsAprobadas, selectedItemIds]);

    const totalEstimado = useMemo(() => {
        return selectedItems.reduce(
            (acc, i) => acc + Number(i.cantidadRequerida || 0) * Number(i.precioUnitarioRef || 0),
            0
        );
    }, [selectedItems]);

    const proveedorIdNum = Number(proveedorId);
    const canSubmit =
        Number.isFinite(proveedorIdNum) &&
        proveedorIdNum > 0 &&
        selectedItemIds.size > 0 &&
        !submitting;

    const handleGenerar = async () => {
        if (!canSubmit) return;
        await onGenerar({
            proveedorId: proveedorIdNum,
            hcItemIds: Array.from(selectedItemIds),
            fechaEntregaEstimada: fechaEntregaEstimada || null,
            observaciones: observaciones || null,
        });
    };

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-32">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-6">
                    <button
                        onClick={onBack}
                        className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-indigo-600 active:scale-95"
                    >
                        <ChevronLeft className="w-5 h-5" />
                    </button>
                    <div>
                        <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">Generar OC Consolidada</h2>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-1">Selecciona items de HCs aprobadas para un proveedor</p>
                    </div>
                </div>
                <button
                    onClick={handleGenerar}
                    disabled={!canSubmit}
                    className="px-10 h-12 bg-indigo-600 hover:bg-slate-900 text-white rounded-2xl font-black text-xs uppercase tracking-widest shadow-2xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    <Save className="w-4 h-4" />
                    {submitting ? 'Generando...' : 'Generar OC'}
                </button>
            </div>

            {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {error}
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main: Items de HCs */}
                <div className="lg:col-span-2 space-y-6">
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-slate-50 flex items-center gap-4">
                            <div className="bg-emerald-50 p-2 rounded-xl">
                                <Layers className="w-5 h-5 text-emerald-600" />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-slate-800 uppercase tracking-widest">Items disponibles (HCs APROBADAS)</h3>
                                <p className="text-[9px] text-slate-400 font-bold uppercase tracking-widest mt-1">Marca los que quieres incluir en esta OC</p>
                            </div>
                        </div>

                        {(!hcsAprobadas || hcsAprobadas.length === 0) ? (
                            <div className="py-16 text-center text-slate-400 font-bold uppercase tracking-widest text-xs">
                                No hay HCs aprobadas disponibles
                            </div>
                        ) : (
                            <div className="divide-y divide-slate-50">
                                {hcsAprobadas.map(hc => (
                                    <div key={hc.idHC} className="p-6">
                                        <div className="flex items-center gap-3 mb-4">
                                            <span className="text-[10px] font-black text-indigo-600 bg-indigo-50 px-3 py-1 rounded-lg uppercase tracking-widest">HC #{hc.idHC}</span>
                                            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">{hc.numeroHC}</span>
                                            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">· OP #{hc.opId}</span>
                                        </div>
                                        <table className="w-full text-left">
                                            <thead>
                                                <tr className="text-[9px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-100">
                                                    <th className="py-2 w-8"></th>
                                                    <th className="py-2">Insumo</th>
                                                    <th className="py-2 text-center">Tipo</th>
                                                    <th className="py-2 text-center">Cant. Requerida</th>
                                                    <th className="py-2 text-right">Precio Ref.</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {(hc.items || []).map(item => (
                                                    <tr key={item.idHCItem} className="hover:bg-indigo-50/30 transition-colors">
                                                        <td className="py-2">
                                                            <input
                                                                type="checkbox"
                                                                checked={selectedItemIds.has(item.idHCItem)}
                                                                onChange={() => toggleItem(item.idHCItem)}
                                                                className="w-4 h-4 accent-indigo-600 cursor-pointer"
                                                            />
                                                        </td>
                                                        <td className="py-2 text-xs font-bold text-slate-700 uppercase">{item.nombreInsumo || '—'}</td>
                                                        <td className="py-2 text-[10px] font-bold text-slate-500 uppercase text-center">{item.tipoInsumo || '—'}</td>
                                                        <td className="py-2 text-xs font-black text-slate-700 text-center tabular-nums">{item.cantidadRequerida}</td>
                                                        <td className="py-2 text-xs font-black text-indigo-600 text-right tabular-nums">{formatCLP(item.precioUnitarioRef)}</td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>

                {/* Sidebar: Proveedor + resumen */}
                <div className="space-y-6">
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 p-8 space-y-6">
                        <div className="flex items-center gap-3">
                            <Truck className="w-5 h-5 text-indigo-600" />
                            <h3 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Proveedor</h3>
                        </div>
                        <div className="space-y-2">
                            <label className="text-[9px] font-black text-slate-400 uppercase tracking-widest">ID Proveedor</label>
                            <input
                                type="number"
                                min={1}
                                value={proveedorId}
                                onChange={(e) => setProveedorId(e.target.value)}
                                placeholder="Ej: 5"
                                className="w-full h-12 px-4 bg-slate-50 border border-slate-100 rounded-2xl text-sm font-black text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            />
                        </div>
                        <div className="space-y-2">
                            <label className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Fecha Entrega Estimada</label>
                            <input
                                type="date"
                                value={fechaEntregaEstimada}
                                onChange={(e) => setFechaEntregaEstimada(e.target.value)}
                                className="w-full h-12 px-4 bg-slate-50 border border-slate-100 rounded-2xl text-xs font-bold text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            />
                        </div>
                        <div className="space-y-2">
                            <label className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Observaciones</label>
                            <textarea
                                rows={3}
                                value={observaciones}
                                onChange={(e) => setObservaciones(e.target.value)}
                                placeholder="Notas para el proveedor..."
                                className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl text-[11px] font-medium text-slate-600 italic resize-none focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            />
                        </div>
                    </div>

                    <div className="bg-slate-900 rounded-[2.5rem] p-8 space-y-6 text-white">
                        <h3 className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.25em]">Resumen</h3>
                        <div className="space-y-2">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Items seleccionados</p>
                            <p className="text-3xl font-black tracking-tighter tabular-nums">{selectedItemIds.size}</p>
                        </div>
                        <div className="space-y-2">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Total Estimado</p>
                            <p className="text-2xl font-black tracking-tighter tabular-nums text-emerald-400">{formatCLP(totalEstimado)}</p>
                        </div>
                        <div className="pt-4 border-t border-slate-800 flex items-start gap-3">
                            <AlertCircle className="w-4 h-4 text-amber-500 shrink-0 mt-0.5" />
                            <p className="text-[9px] font-bold text-slate-400 uppercase tracking-tighter italic leading-relaxed">
                                El precio final se puede ajustar item por item después de generar la OC, mientras esté en estado EMITIDA.
                            </p>
                        </div>
                    </div>

                    {selectedItems.length > 0 && (
                        <div className="bg-white rounded-[2rem] border border-slate-100 p-6 space-y-3">
                            <div className="flex items-center gap-2">
                                <Package className="w-4 h-4 text-indigo-500" />
                                <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Items por incluir</h4>
                            </div>
                            <ul className="space-y-1.5 max-h-64 overflow-y-auto pr-2">
                                {selectedItems.map(i => (
                                    <li key={i.idHCItem} className="text-[10px] font-bold text-slate-600 uppercase tracking-tight flex justify-between gap-2">
                                        <span className="truncate">{i.nombreInsumo}</span>
                                        <span className="tabular-nums text-slate-400 shrink-0">{i.cantidadRequerida}</span>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
