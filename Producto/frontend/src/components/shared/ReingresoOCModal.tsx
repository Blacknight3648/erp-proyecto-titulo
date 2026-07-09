import React, { useMemo, useState } from 'react';
import { RotateCcw, Loader2, AlertTriangle } from 'lucide-react';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../ui/select';
import { clampNonNegative } from '../../utils/validations';

/**
 * Modal de "reingreso con cambios" de una OC RECHAZADA: permite corregir el
 * proveedor y la cantidad/precio de cada ítem en el mismo paso, ya que el
 * motivo típico de rechazo es justamente que alguno de esos datos estaba mal.
 * Reutilizado desde DetalleOC.tsx y HCModificacion.tsx.
 */
export default function ReingresoOCModal({ oc, proveedores, submitting, error, onClose, onConfirm }) {
    const [proveedorId, setProveedorId] = useState(() => String(oc.proveedorId ?? ''));
    const [itemsEdit, setItemsEdit] = useState(() => (oc.items || []).map(i => ({
        idOCItem: i.idOCItem,
        nombreInsumo: i.nombreInsumo,
        cantidadComprada: String(i.cantidadComprada ?? ''),
        precioUnitario: String(i.precioUnitario ?? ''),
    })));

    const updateItem = (idOCItem, field, value) => {
        setItemsEdit(prev => prev.map(it => it.idOCItem === idOCItem ? { ...it, [field]: value } : it));
    };

    const puedeConfirmar = useMemo(() => (
        itemsEdit.every(it => {
            const cant = Number(it.cantidadComprada);
            const precio = Number(it.precioUnitario);
            return Number.isFinite(cant) && cant > 0 && Number.isFinite(precio) && precio >= 0;
        })
    ), [itemsEdit]);

    const handleConfirmar = () => {
        if (!puedeConfirmar || submitting) return;
        const proveedorSeleccionado = Number(proveedorId);
        const itemsCambiados = (oc.items || [])
            .map((original, idx) => {
                const edited = itemsEdit[idx];
                const cantidadComprada = Number(edited.cantidadComprada);
                const precioUnitario = Number(edited.precioUnitario);
                const cambio = cantidadComprada !== Number(original.cantidadComprada)
                    || precioUnitario !== Number(original.precioUnitario);
                return cambio ? { idOCItem: original.idOCItem, cantidadComprada, precioUnitario } : null;
            })
            .filter(Boolean);

        onConfirm({
            proveedorId: proveedorSeleccionado !== Number(oc.proveedorId) ? proveedorSeleccionado : null,
            itemsCambiados,
        });
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-sm p-4"
            onClick={onClose}>
            <div className="bg-white w-full max-w-2xl rounded-[2rem] p-6 shadow-2xl animate-in zoom-in-95 duration-200 max-h-[85vh] overflow-y-auto"
                onClick={(e) => e.stopPropagation()}>
                <div className="flex items-center gap-3 mb-6">
                    <div className="bg-indigo-100 p-2 rounded-xl">
                        <RotateCcw className="w-5 h-5 text-indigo-600" />
                    </div>
                    <div>
                        <h3 className="text-sm font-black text-slate-800 uppercase tracking-tight">Reingresar Orden de Compra</h3>
                        <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">
                            {oc.numeroOC || `OC #${oc.idOC}`}
                        </p>
                    </div>
                </div>

                {error && (
                    <div className="flex items-start gap-2.5 px-4 py-3 mb-6 bg-rose-50 border border-rose-200 rounded-2xl">
                        <AlertTriangle className="w-4 h-4 text-rose-600 shrink-0 mt-0.5" />
                        <p className="text-xs font-medium text-rose-700">{error}</p>
                    </div>
                )}

                <div className="space-y-2 mb-6">
                    <label className="text-[9px] font-black text-slate-500 uppercase tracking-widest">Proveedor</label>
                    <Select
                        value={proveedorId}
                        onValueChange={setProveedorId}
                    >
                        <SelectTrigger className="w-full h-11 px-4 bg-slate-50 border border-slate-200 rounded-2xl text-xs font-bold text-slate-700 outline-none focus:ring-2 focus:ring-indigo-400">
                            <SelectValue placeholder="Seleccione un proveedor..." />
                        </SelectTrigger>
                        <SelectContent>
                            {proveedores.map((p) => (
                                <SelectItem key={p.proveedorId} value={String(p.proveedorId)}>{p.nombreProveedor}</SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className="space-y-2 mb-6">
                    <label className="text-[9px] font-black text-slate-500 uppercase tracking-widest">Ítems</label>
                    <div className="border border-slate-200 rounded-2xl overflow-hidden">
                        <table className="w-full text-xs">
                            <thead className="bg-slate-50">
                                <tr>
                                    <th className="text-left px-4 py-2 font-black text-[9px] text-slate-500 uppercase tracking-widest">Insumo</th>
                                    <th className="text-right px-4 py-2 font-black text-[9px] text-slate-500 uppercase tracking-widest">Cantidad</th>
                                    <th className="text-right px-4 py-2 font-black text-[9px] text-slate-500 uppercase tracking-widest">Precio Unit.</th>
                                </tr>
                            </thead>
                            <tbody>
                                {itemsEdit.map((it) => (
                                    <tr key={it.idOCItem} className="border-t border-slate-100">
                                        <td className="px-4 py-2 font-bold text-slate-700">{it.nombreInsumo}</td>
                                        <td className="px-4 py-2">
                                            <input type="number" min={0} value={it.cantidadComprada}
                                                onChange={(e) => updateItem(it.idOCItem, 'cantidadComprada', String(clampNonNegative(e.target.value)))}
                                                className="w-24 h-8 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums ml-auto block" />
                                        </td>
                                        <td className="px-4 py-2">
                                            <input type="number" min={0} value={it.precioUnitario}
                                                onChange={(e) => updateItem(it.idOCItem, 'precioUnitario', String(clampNonNegative(e.target.value)))}
                                                className="w-24 h-8 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums ml-auto block" />
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="flex justify-end gap-2">
                    <button onClick={onClose}
                        className="px-4 h-10 rounded-xl text-[10px] font-black text-slate-500 hover:text-slate-700 uppercase tracking-widest">
                        Cancelar
                    </button>
                    <button onClick={handleConfirmar} disabled={!puedeConfirmar || submitting}
                        className="px-5 h-10 rounded-xl text-[10px] font-black text-white bg-indigo-600 hover:bg-indigo-700 uppercase tracking-widest flex items-center gap-1.5 disabled:opacity-50 disabled:cursor-not-allowed">
                        {submitting ? <Loader2 className="w-3.5 h-3.5 animate-spin" /> : <RotateCcw className="w-3.5 h-3.5" />}
                        {submitting ? 'Reingresando...' : 'Confirmar Reingreso'}
                    </button>
                </div>
            </div>
        </div>
    );
}
