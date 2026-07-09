import { useMemo, useState } from 'react';
import {
    ChevronLeft, Truck, CheckCircle2, Lock, Pencil, Check, X, GitMerge,
    Package, Inbox, Plus, Trash2, XCircle, RotateCcw, AlertTriangle,
} from 'lucide-react';
import { clampNonNegative } from '../../../utils/validations';
import { useProveedores } from '../../../hooks/useProveedores';
import ReingresoOCModal from '../../../components/shared/ReingresoOCModal';

const STATUS_BADGE = {
    EMITIDA:               'bg-amber-50 text-amber-600 border-amber-100',
    ENVIADA:               'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA_PARCIAL:  'bg-violet-50 text-violet-600 border-violet-100',
    RECEPCIONADA:          'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:               'bg-slate-100 text-slate-500 border-slate-200',
    RECHAZADA:              'bg-rose-50 text-rose-600 border-rose-100',
};

export default function DetalleOC({
    oc,
    recepciones,
    onBack,
    onMarcarEnviada,
    onMarcarRecepcionada,
    onCerrar,
    onActualizarPrecio,
    onRechazar,
    onReingresar,
    onAgregarItem,
    onActualizarItem,
    onEliminarItem,
    onRegistrarRecepcion,
    submitting,
    error,
    formatCLP,
}) {
    const [tab, setTab] = useState('items');
    const [editingItemId, setEditingItemId] = useState(null);
    const [precioEdit, setPrecioEdit] = useState('');
    const [cantidadEdit, setCantidadEdit] = useState('');
    const [showRecepcionForm, setShowRecepcionForm] = useState(false);
    const [showRechazoModal, setShowRechazoModal] = useState(false);
    const [motivoRechazo, setMotivoRechazo] = useState('');
    const [showReingresoModal, setShowReingresoModal] = useState(false);
    const { proveedores } = useProveedores();

    if (!oc) return null;

    const startEdit = (item) => {
        setEditingItemId(item.idOCItem);
        setPrecioEdit(String(item.precioUnitario ?? ''));
        setCantidadEdit(String(item.cantidadComprada ?? ''));
    };
    const cancelEdit = () => { setEditingItemId(null); setPrecioEdit(''); setCantidadEdit(''); };
    const commitEdit = async (idOCItem) => {
        const precio = Number(precioEdit);
        const cantidad = Number(cantidadEdit);
        if (!Number.isFinite(precio) || precio < 0) return;
        if (!Number.isFinite(cantidad) || cantidad <= 0) return;
        const item = (oc.items || []).find(i => i.idOCItem === idOCItem);
        if (precio === item?.precioUnitario && cantidad === item?.cantidadComprada) {
            cancelEdit();
            return;
        }
        await onActualizarItem(oc.idOC, idOCItem, { cantidadComprada: cantidad, precioUnitario: precio });
        cancelEdit();
    };

    const confirmarRechazo = async () => {
        if (!motivoRechazo.trim()) return;
        await onRechazar(oc.idOC, motivoRechazo.trim());
        setShowRechazoModal(false);
        setMotivoRechazo('');
    };

    const confirmarReingreso = async (payload) => {
        const resultado = await onReingresar(oc.idOC, payload.proveedorId, payload.itemsCambiados);
        if (resultado) {
            setShowReingresoModal(false);
        }
    };

    const puedeEditarPrecio = oc.estado === 'EMITIDA';
    const puedeEditarItems = oc.estado === 'EMITIDA';
    const puedeRecepcionar = ['ENVIADA', 'RECEPCIONADA_PARCIAL'].includes(oc.estado);

    /** Calcula lo que falta por recibir por cada OCItem */
    const faltantesPorItem = useMemo(() => {
        const recibidoAcum = {};
        for (const rec of recepciones || []) {
            for (const item of rec.items || []) {
                recibidoAcum[item.ocItemId] = (recibidoAcum[item.ocItemId] || 0) + Number(item.cantidadRecibida || 0);
            }
        }
        const map = {};
        for (const item of oc.items || []) {
            const comprado = Number(item.cantidadComprada || 0);
            map[item.idOCItem] = Math.max(0, comprado - (recibidoAcum[item.idOCItem] || 0));
        }
        return map;
    }, [oc.items, recepciones]);

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-32">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-6">
                    <button onClick={onBack}
                        className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-indigo-600 active:scale-95">
                        <ChevronLeft className="w-5 h-5" />
                    </button>
                    <div>
                        <div className="flex items-center gap-3">
                            <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">OC #{oc.idOC}</h2>
                            <span className={`px-3 py-1.5 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_BADGE[oc.estado] || ''}`}>
                                {oc.estado}
                            </span>
                            {oc.version > 1 && (
                                <span className="px-2.5 py-1 rounded-full text-[9px] font-black uppercase tracking-widest bg-indigo-50 text-indigo-600 border border-indigo-100">
                                    v{oc.version}
                                </span>
                            )}
                        </div>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-1">
                            {oc.numeroOC} · Proveedor #{oc.proveedorId} · Emitida {oc.fechaEmision || '—'}
                        </p>
                    </div>
                </div>

                <div className="flex gap-3">
                    {oc.estado === 'EMITIDA' && (
                        <button onClick={() => onMarcarEnviada(oc.idOC)}
                            className="bg-blue-600 hover:bg-blue-700 text-white rounded-2xl px-6 h-12 font-black text-xs uppercase tracking-widest shadow-xl shadow-blue-100 transition-all active:scale-95 flex items-center gap-2">
                            <Truck className="w-4 h-4" /> Marcar Enviada
                        </button>
                    )}
                    {oc.estado === 'EMITIDA' && (
                        <button onClick={() => setShowRechazoModal(true)}
                            className="bg-rose-600 hover:bg-rose-700 text-white rounded-2xl px-6 h-12 font-black text-xs uppercase tracking-widest shadow-xl shadow-rose-100 transition-all active:scale-95 flex items-center gap-2">
                            <XCircle className="w-4 h-4" /> Rechazar OC
                        </button>
                    )}
                    {oc.estado === 'RECHAZADA' && (
                        <button onClick={() => setShowReingresoModal(true)} disabled={submitting}
                            className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl px-6 h-12 font-black text-xs uppercase tracking-widest shadow-xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2 disabled:opacity-50">
                            <RotateCcw className="w-4 h-4" /> Reingresar OC
                        </button>
                    )}
                    {puedeRecepcionar && (
                        <button onClick={() => onMarcarRecepcionada(oc.idOC)}
                            className="bg-emerald-600 hover:bg-emerald-700 text-white rounded-2xl px-6 h-12 font-black text-xs uppercase tracking-widest shadow-xl shadow-emerald-100 transition-all active:scale-95 flex items-center gap-2">
                            <CheckCircle2 className="w-4 h-4" /> Marcar Recepcionada
                        </button>
                    )}
                    {oc.estado === 'RECEPCIONADA' && (
                        <button onClick={() => onCerrar(oc.idOC)}
                            className="bg-slate-900 hover:bg-black text-white rounded-2xl px-6 h-12 font-black text-xs uppercase tracking-widest shadow-xl shadow-slate-200 transition-all active:scale-95 flex items-center gap-2">
                            <Lock className="w-4 h-4" /> Cerrar OC
                        </button>
                    )}
                </div>
            </div>

            {oc.estado === 'RECHAZADA' && oc.motivoRechazo && (
                <div className="flex items-start gap-3 px-6 py-4 bg-rose-50 border border-rose-200 rounded-2xl">
                    <AlertTriangle className="w-4 h-4 text-rose-600 shrink-0 mt-0.5" />
                    <div>
                        <p className="text-[10px] font-black text-rose-700 uppercase tracking-widest mb-1">Motivo del rechazo</p>
                        <p className="text-xs font-medium text-rose-700 italic">{oc.motivoRechazo}</p>
                    </div>
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main: tabs */}
                <div className="lg:col-span-2 space-y-6">
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="border-b border-slate-100 flex">
                            <TabBtn active={tab === 'items'} onClick={() => setTab('items')}>
                                <Package className="w-4 h-4" /> Items ({oc.items?.length ?? 0})
                            </TabBtn>
                            <TabBtn active={tab === 'recepciones'} onClick={() => setTab('recepciones')}>
                                <Inbox className="w-4 h-4" /> Recepciones ({recepciones?.length ?? 0})
                            </TabBtn>
                            <TabBtn active={tab === 'trazabilidad'} onClick={() => setTab('trazabilidad')}>
                                <GitMerge className="w-4 h-4" /> Trazabilidad
                            </TabBtn>
                        </div>

                        {tab === 'items' && (
                            <TablaItems
                                oc={oc}
                                editingItemId={editingItemId}
                                precioEdit={precioEdit}
                                setPrecioEdit={setPrecioEdit}
                                cantidadEdit={cantidadEdit}
                                setCantidadEdit={setCantidadEdit}
                                startEdit={startEdit}
                                cancelEdit={cancelEdit}
                                commitEdit={commitEdit}
                                puedeEditarPrecio={puedeEditarPrecio}
                                puedeEditarItems={puedeEditarItems}
                                onAgregarItem={onAgregarItem}
                                onEliminarItem={onEliminarItem}
                                formatCLP={formatCLP}
                            />
                        )}

                        {tab === 'recepciones' && (
                            <div className="p-6 space-y-4">
                                <div className="flex justify-between items-center">
                                    <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">
                                        {recepciones?.length || 0} recepción(es) registrada(s)
                                    </p>
                                    {puedeRecepcionar && (
                                        <button onClick={() => setShowRecepcionForm(s => !s)}
                                            className="text-[10px] font-black uppercase tracking-widest text-indigo-600 hover:text-indigo-800 flex items-center gap-1">
                                            <Plus className="w-3.5 h-3.5" /> Registrar Recepción
                                        </button>
                                    )}
                                </div>

                                {showRecepcionForm && (
                                    <RecepcionForm
                                        oc={oc}
                                        faltantesPorItem={faltantesPorItem}
                                        submitting={submitting}
                                        onCancel={() => setShowRecepcionForm(false)}
                                        onSave={async (payload) => {
                                            await onRegistrarRecepcion(oc.idOC, payload);
                                            setShowRecepcionForm(false);
                                        }}
                                    />
                                )}

                                <ListaRecepciones recepciones={recepciones || []} oc={oc} />
                            </div>
                        )}

                        {tab === 'trazabilidad' && (
                            <div className="p-6 space-y-4">
                                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-widest">
                                    Cada item de esta OC consume cantidad de HCs aprobadas:
                                </p>
                                <div className="divide-y divide-slate-50">
                                    {(oc.items || []).map(item => (
                                        <div key={item.idOCItem} className="py-3">
                                            <p className="text-xs font-bold text-slate-700 uppercase mb-1">{item.nombreInsumo}</p>
                                            <div className="flex flex-wrap gap-2">
                                                {(item.hcLinks || []).length === 0 && (
                                                    <span className="text-[10px] text-slate-400 italic">Sin links registrados</span>
                                                )}
                                                {(item.hcLinks || []).map((link, idx) => (
                                                    <span key={idx} className="text-[10px] font-black text-violet-600 bg-violet-50 px-3 py-1 rounded-lg border border-violet-100">
                                                        HCItem #{link.hcItemId} → {link.cantidadAsignada}
                                                    </span>
                                                ))}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                {/* Sidebar */}
                <div className="space-y-6">
                    <div className="bg-slate-900 rounded-[2.5rem] p-8 space-y-6 text-white">
                        <h3 className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.25em]">Resumen</h3>
                        <div className="space-y-2">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Total Neto</p>
                            <p className="text-3xl font-black tracking-tighter tabular-nums text-emerald-400">{formatCLP(oc.totalNeto)}</p>
                        </div>
                        <div className="pt-6 border-t border-slate-800 space-y-3">
                            <Row label="Items" value={oc.items?.length ?? 0} />
                            <Row label="Recepciones" value={recepciones?.length ?? 0} />
                            <Row label="Fecha Emisión" value={oc.fechaEmision || '—'} />
                            <Row label="Fecha Entrega" value={oc.fechaEntregaEstimada || '—'} />
                        </div>
                    </div>

                    {oc.observaciones && (
                        <div className="bg-white rounded-[2rem] border border-slate-100 p-6 space-y-3">
                            <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Observaciones</h4>
                            <p className="text-[11px] font-medium italic text-slate-500 leading-relaxed">{oc.observaciones}</p>
                        </div>
                    )}
                </div>
            </div>

            {/* Modal de Rechazo Obligatorio */}
            {showRechazoModal && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-sm p-4"
                    onClick={() => setShowRechazoModal(false)}>
                    <div className="bg-white w-full max-w-md rounded-[2rem] p-6 shadow-2xl animate-in zoom-in-95 duration-200"
                        onClick={(e) => e.stopPropagation()}>
                        <div className="flex items-center gap-3 mb-4">
                            <div className="w-10 h-10 rounded-2xl bg-rose-50 flex items-center justify-center">
                                <XCircle className="w-5 h-5 text-rose-600" />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-slate-800 uppercase tracking-tight">Rechazar Orden de Compra</h3>
                                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">
                                    {oc.numeroOC}
                                </p>
                            </div>
                        </div>
                        <textarea
                            rows={4}
                            value={motivoRechazo}
                            onChange={(e) => setMotivoRechazo(e.target.value)}
                            placeholder="Motivo del rechazo (obligatorio)..."
                            className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-2xl text-xs font-medium text-slate-700 outline-none focus:ring-2 focus:ring-rose-400 resize-none mb-4"
                        />
                        <div className="flex justify-end gap-2">
                            <button onClick={() => { setShowRechazoModal(false); setMotivoRechazo(''); }}
                                className="px-4 h-10 rounded-xl text-[10px] font-black text-slate-500 hover:text-slate-700 uppercase tracking-widest">
                                Cancelar
                            </button>
                            <button onClick={confirmarRechazo} disabled={!motivoRechazo.trim() || submitting}
                                className="px-5 h-10 rounded-xl text-[10px] font-black text-white bg-rose-600 hover:bg-rose-700 uppercase tracking-widest flex items-center gap-1.5 disabled:opacity-50 disabled:cursor-not-allowed">
                                <XCircle className="w-3.5 h-3.5" /> {submitting ? 'Rechazando...' : 'Confirmar Rechazo'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showReingresoModal && (
                <ReingresoOCModal
                    oc={oc}
                    proveedores={proveedores}
                    submitting={submitting}
                    error={error}
                    onClose={() => setShowReingresoModal(false)}
                    onConfirm={confirmarReingreso}
                />
            )}
        </div>
    );
}

function TabBtn({ active, onClick, children }) {
    return (
        <button onClick={onClick}
            className={`flex items-center gap-2 px-6 py-4 text-[10px] font-black uppercase tracking-widest transition-all border-b-2 ${
                active ? 'text-indigo-600 border-indigo-600 bg-indigo-50/30' : 'text-slate-400 border-transparent hover:text-slate-700'
            }`}>
            {children}
        </button>
    );
}

function Row({ label, value }) {
    return (
        <div className="flex items-center justify-between text-xs">
            <span className="text-[9px] font-black text-slate-500 uppercase tracking-widest">{label}</span>
            <span className="text-[10px] font-black text-slate-300 uppercase">{value}</span>
        </div>
    );
}

function TablaItems({
    oc, editingItemId, precioEdit, setPrecioEdit, cantidadEdit, setCantidadEdit,
    startEdit, cancelEdit, commitEdit, puedeEditarPrecio, puedeEditarItems,
    onAgregarItem, onEliminarItem, formatCLP,
}) {
    const [showAddForm, setShowAddForm] = useState(false);
    const [nuevoItem, setNuevoItem] = useState({ tipoInsumo: 'ACCESORIO', nombreInsumo: '', cantidadComprada: '', precioUnitario: '' });
    const [deletingId, setDeletingId] = useState(null);

    const puedeAgregar = Boolean(nuevoItem.nombreInsumo.trim()) && Number(nuevoItem.cantidadComprada) > 0 && Number(nuevoItem.precioUnitario) >= 0;

    const handleAgregar = async () => {
        if (!puedeAgregar) return;
        await onAgregarItem(oc.idOC, {
            tipoInsumo: nuevoItem.tipoInsumo,
            nombreInsumo: nuevoItem.nombreInsumo.trim().toUpperCase(),
            cantidadRequerida: Number(nuevoItem.cantidadComprada),
            cantidadComprada: Number(nuevoItem.cantidadComprada),
            precioUnitario: Number(nuevoItem.precioUnitario),
        });
        setNuevoItem({ tipoInsumo: 'ACCESORIO', nombreInsumo: '', cantidadComprada: '', precioUnitario: '' });
        setShowAddForm(false);
    };

    const handleEliminar = async (idOCItem) => {
        setDeletingId(idOCItem);
        try {
            await onEliminarItem(oc.idOC, idOCItem);
        } finally {
            setDeletingId(null);
        }
    };

    return (
        <div>
            <div className="p-6 border-b border-slate-50 flex items-center justify-between">
                <p className="text-[9px] text-slate-400 font-bold uppercase tracking-widest">
                    {puedeEditarItems
                        ? 'Puedes ajustar cantidad/precio o agregar/quitar ítems mientras la OC esté EMITIDA'
                        : 'Ítems congelados (OC ya enviada)'}
                </p>
                {puedeEditarItems && (
                    <button onClick={() => setShowAddForm(s => !s)}
                        className="text-[10px] font-black uppercase tracking-widest text-indigo-600 hover:text-indigo-800 flex items-center gap-1">
                        <Plus className="w-3.5 h-3.5" /> Agregar Ítem
                    </button>
                )}
            </div>

            {showAddForm && (
                <div className="p-6 bg-indigo-50/40 border-b border-indigo-100 grid grid-cols-12 gap-3 items-end">
                    <div className="col-span-2">
                        <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest block mb-1">Tipo</label>
                        <select value={nuevoItem.tipoInsumo} onChange={(e) => setNuevoItem(p => ({ ...p, tipoInsumo: e.target.value }))}
                            className="w-full h-9 px-2 bg-white border border-slate-200 rounded-lg text-[10px] font-black uppercase">
                            <option value="ACCESORIO">Accesorio</option>
                            <option value="TELA">Tela</option>
                        </select>
                    </div>
                    <div className="col-span-4">
                        <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest block mb-1">Insumo</label>
                        <input value={nuevoItem.nombreInsumo} onChange={(e) => setNuevoItem(p => ({ ...p, nombreInsumo: e.target.value }))}
                            placeholder="Nombre del insumo"
                            className="w-full h-9 px-2 bg-white border border-slate-200 rounded-lg text-[11px] font-bold uppercase" />
                    </div>
                    <div className="col-span-2">
                        <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest block mb-1">Cantidad</label>
                        <input type="number" min={0} value={nuevoItem.cantidadComprada}
                            onChange={(e) => setNuevoItem(p => ({ ...p, cantidadComprada: String(clampNonNegative(e.target.value)) }))}
                            className="w-full h-9 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums" />
                    </div>
                    <div className="col-span-2">
                        <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest block mb-1">Precio Unit.</label>
                        <input type="number" min={0} value={nuevoItem.precioUnitario}
                            onChange={(e) => setNuevoItem(p => ({ ...p, precioUnitario: String(clampNonNegative(e.target.value)) }))}
                            className="w-full h-9 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums" />
                    </div>
                    <div className="col-span-2 flex gap-1">
                        <button onClick={handleAgregar} disabled={!puedeAgregar}
                            className="flex-1 h-9 rounded-lg text-[10px] font-black text-white bg-indigo-600 hover:bg-indigo-700 uppercase tracking-widest disabled:opacity-40 disabled:cursor-not-allowed flex items-center justify-center gap-1">
                            <Check className="w-3.5 h-3.5" /> Agregar
                        </button>
                        <button onClick={() => setShowAddForm(false)} className="h-9 w-9 rounded-lg text-slate-400 hover:text-rose-600 flex items-center justify-center">
                            <X className="w-4 h-4" />
                        </button>
                    </div>
                </div>
            )}

            <table className="w-full text-left">
                <thead className="bg-slate-50/50 border-b border-slate-100">
                    <tr className="text-[9px] font-black text-slate-400 uppercase tracking-widest">
                        <th className="pl-8 py-4">Insumo</th>
                        <th className="py-4 text-center">Tipo</th>
                        <th className="py-4 text-center">Cant.</th>
                        <th className="py-4 text-right">Precio Unit.</th>
                        <th className="py-4 text-right">Subtotal</th>
                        {puedeEditarItems && <th className="py-4 text-right pr-8">Acciones</th>}
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                    {(oc.items || []).map(item => (
                        <tr key={item.idOCItem} className="hover:bg-indigo-50/30 transition-colors">
                            <td className="pl-8 py-5 text-xs font-bold text-slate-700 uppercase">{item.nombreInsumo}</td>
                            <td className="py-5 text-[10px] font-bold text-slate-500 uppercase text-center">{item.tipoInsumo}</td>
                            <td className="py-5 text-xs font-black text-slate-700 text-center tabular-nums">
                                {editingItemId === item.idOCItem ? (
                                    <input type="number" min={0} value={cantidadEdit} onChange={(e) => setCantidadEdit(clampNonNegative(e.target.value))}
                                        className="w-20 h-8 px-2 bg-white border border-indigo-200 rounded-lg text-xs font-black text-center tabular-nums focus:outline-none focus:ring-2 focus:ring-indigo-500" />
                                ) : item.cantidadComprada}
                            </td>
                            <td className="py-5 text-right">
                                {editingItemId === item.idOCItem ? (
                                    <div className="flex items-center justify-end gap-1">
                                        <input type="number" min={0} value={precioEdit} onChange={(e) => setPrecioEdit(clampNonNegative(e.target.value))}
                                            className="w-28 h-8 px-2 bg-white border border-indigo-200 rounded-lg text-xs font-black text-right tabular-nums focus:outline-none focus:ring-2 focus:ring-indigo-500" autoFocus />
                                        <button onClick={() => commitEdit(item.idOCItem)} className="p-1 text-emerald-600 hover:text-emerald-700">
                                            <Check className="w-4 h-4" />
                                        </button>
                                        <button onClick={cancelEdit} className="p-1 text-rose-500 hover:text-rose-700">
                                            <X className="w-4 h-4" />
                                        </button>
                                    </div>
                                ) : (
                                    <div className="flex items-center justify-end gap-2">
                                        <span className="text-xs font-black text-indigo-600 tabular-nums">{formatCLP(item.precioUnitario)}</span>
                                        {puedeEditarPrecio && (
                                            <button onClick={() => startEdit(item)} className="p-1 text-slate-300 hover:text-indigo-600">
                                                <Pencil className="w-3.5 h-3.5" />
                                            </button>
                                        )}
                                    </div>
                                )}
                            </td>
                            <td className="py-5 text-right text-xs font-black text-slate-800 tabular-nums">{formatCLP(item.subtotal)}</td>
                            {puedeEditarItems && (
                                <td className="pr-8 py-5 text-right">
                                    <button onClick={() => handleEliminar(item.idOCItem)} disabled={deletingId === item.idOCItem}
                                        className="p-1.5 text-slate-300 hover:text-rose-600 disabled:opacity-40">
                                        <Trash2 className="w-3.5 h-3.5" />
                                    </button>
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

function ListaRecepciones({ recepciones, oc }) {
    if (!recepciones || recepciones.length === 0) {
        return <p className="text-center text-slate-400 text-[10px] font-bold uppercase tracking-widest py-8">Sin recepciones registradas</p>;
    }

    const insumoPorOcItemId = {};
    for (const item of oc.items || []) insumoPorOcItemId[item.idOCItem] = item.nombreInsumo;

    return (
        <div className="space-y-3">
            {recepciones.map(rec => (
                <div key={rec.idRecepcion} className="border border-slate-100 rounded-2xl p-4 bg-slate-50/30">
                    <div className="flex justify-between items-center mb-3">
                        <div className="flex items-center gap-3">
                            <span className="text-[10px] font-black text-indigo-600 bg-indigo-50 px-3 py-1 rounded-lg uppercase tracking-widest">Recepción #{rec.idRecepcion}</span>
                            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">{rec.fechaRecepcion}</span>
                            {rec.numeroGuia && <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Guía: {rec.numeroGuia}</span>}
                        </div>
                        {rec.responsable && <span className="text-[10px] font-bold text-slate-500 italic">{rec.responsable}</span>}
                    </div>
                    <table className="w-full text-left text-xs">
                        <thead>
                            <tr className="text-[9px] font-black text-slate-400 uppercase tracking-widest">
                                <th className="py-1">Insumo</th>
                                <th className="py-1 text-right">Recibida</th>
                                <th className="py-1 text-right">Conforme</th>
                                <th className="py-1 text-right">Rechazada</th>
                                <th className="py-1">Motivo</th>
                            </tr>
                        </thead>
                        <tbody>
                            {rec.items.map((it, idx) => (
                                <tr key={idx} className="border-t border-slate-100">
                                    <td className="py-2 font-bold uppercase">{insumoPorOcItemId[it.ocItemId] || `Item #${it.ocItemId}`}</td>
                                    <td className="py-2 text-right tabular-nums">{it.cantidadRecibida}</td>
                                    <td className="py-2 text-right tabular-nums text-emerald-600">{it.cantidadConforme}</td>
                                    <td className="py-2 text-right tabular-nums text-rose-600">{it.cantidadRechazada}</td>
                                    <td className="py-2 italic text-slate-500">{it.motivoRechazo || '—'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ))}
        </div>
    );
}

function RecepcionForm({ oc, faltantesPorItem, submitting, onCancel, onSave }) {
    const [header, setHeader] = useState({
        fechaRecepcion: '',
        numeroGuia: '',
        responsable: '',
        observaciones: '',
    });
    const [itemsForm, setItemsForm] = useState(
        () => (oc.items || []).map(i => ({
            ocItemId: i.idOCItem,
            nombreInsumo: i.nombreInsumo,
            max: faltantesPorItem[i.idOCItem] || 0,
            cantidadRecibida: '',
            cantidadConforme: '',
            cantidadRechazada: '',
            motivoRechazo: '',
        }))
    );

    const updateItem = (idx, field, value) => {
        setItemsForm(prev => prev.map((it, i) => i === idx ? { ...it, [field]: value } : it));
    };

    const itemsAEnviar = itemsForm.filter(it => Number(it.cantidadRecibida) > 0);
    const algunExcede = itemsAEnviar.some(it => Number(it.cantidadRecibida) > it.max);
    const canSubmit = itemsAEnviar.length > 0 && !algunExcede && !submitting;

    const handleSave = () => {
        if (!canSubmit) return;
        onSave({
            fechaRecepcion: header.fechaRecepcion || null,
            numeroGuia: header.numeroGuia || null,
            responsable: header.responsable || null,
            observaciones: header.observaciones || null,
            items: itemsAEnviar.map(it => ({
                ocItemId: it.ocItemId,
                cantidadRecibida: Number(it.cantidadRecibida),
                cantidadConforme: it.cantidadConforme ? Number(it.cantidadConforme) : null,
                cantidadRechazada: it.cantidadRechazada ? Number(it.cantidadRechazada) : null,
                motivoRechazo: it.motivoRechazo || null,
            })),
        });
    };

    return (
        <div className="bg-emerald-50/40 border border-emerald-200 rounded-2xl p-5 space-y-4">
            <p className="text-[10px] font-black text-emerald-700 uppercase tracking-widest">Nueva Recepción</p>

            <div className="grid grid-cols-2 gap-3">
                <input type="date" value={header.fechaRecepcion} onChange={(e) => setHeader({...header, fechaRecepcion: e.target.value})}
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold" />
                <input value={header.numeroGuia} onChange={(e) => setHeader({...header, numeroGuia: e.target.value.toUpperCase()})}
                    placeholder="N° Guía"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold uppercase" />
                <input value={header.responsable} onChange={(e) => setHeader({...header, responsable: e.target.value.toUpperCase()})}
                    placeholder="Responsable"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold uppercase col-span-2" />
                <textarea rows={2} value={header.observaciones} onChange={(e) => setHeader({...header, observaciones: e.target.value.toUpperCase()})}
                    placeholder="Observaciones"
                    className="px-3 py-2 bg-white border border-slate-100 rounded-xl text-xs font-medium uppercase italic col-span-2 resize-none" />
            </div>

            <div className="bg-white rounded-xl border border-slate-100 overflow-hidden">
                <table className="w-full text-left text-xs">
                    <thead className="bg-slate-50">
                        <tr className="text-[9px] font-black text-slate-400 uppercase tracking-widest">
                            <th className="px-3 py-2">Insumo</th>
                            <th className="px-3 py-2 text-right">Falta</th>
                            <th className="px-3 py-2 text-right">Recibida</th>
                            <th className="px-3 py-2 text-right">Conforme</th>
                            <th className="px-3 py-2 text-right">Rechazada</th>
                            <th className="px-3 py-2">Motivo</th>
                        </tr>
                    </thead>
                    <tbody>
                        {itemsForm.map((it, idx) => {
                            const recibida = Number(it.cantidadRecibida);
                            const excede = recibida > it.max;
                            return (
                                <tr key={it.ocItemId} className={`border-t border-slate-100 ${excede ? 'bg-rose-50' : ''}`}>
                                    <td className="px-3 py-2 font-bold uppercase">{it.nombreInsumo}</td>
                                    <td className="px-3 py-2 text-right tabular-nums text-slate-400">{it.max}</td>
                                    <td className="px-3 py-2 text-right">
                                        <input type="number" min={0} max={it.max} value={it.cantidadRecibida}
                                            onChange={(e) => updateItem(idx, 'cantidadRecibida', clampNonNegative(e.target.value))}
                                            disabled={it.max === 0}
                                            className="w-20 h-8 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums disabled:opacity-30 disabled:cursor-not-allowed" />
                                    </td>
                                    <td className="px-3 py-2 text-right">
                                        <input type="number" min={0} value={it.cantidadConforme}
                                            onChange={(e) => updateItem(idx, 'cantidadConforme', clampNonNegative(e.target.value))}
                                            disabled={!recibida}
                                            className="w-20 h-8 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums text-emerald-600 disabled:opacity-30" />
                                    </td>
                                    <td className="px-3 py-2 text-right">
                                        <input type="number" min={0} value={it.cantidadRechazada}
                                            onChange={(e) => updateItem(idx, 'cantidadRechazada', clampNonNegative(e.target.value))}
                                            disabled={!recibida}
                                            className="w-20 h-8 px-2 bg-white border border-slate-200 rounded-lg text-xs font-black text-right tabular-nums text-rose-600 disabled:opacity-30" />
                                    </td>
                                    <td className="px-3 py-2">
                                        <input value={it.motivoRechazo} onChange={(e) => updateItem(idx, 'motivoRechazo', e.target.value)}
                                            placeholder="—" disabled={!Number(it.cantidadRechazada)}
                                            className="w-full h-8 px-2 bg-white border border-slate-200 rounded-lg text-[11px] italic disabled:opacity-30" />
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>

            {algunExcede && (
                <p className="text-[10px] text-rose-600 font-black uppercase tracking-widest">Hay items que exceden el faltante</p>
            )}

            <div className="flex justify-end gap-2">
                <button onClick={onCancel} className="px-4 h-9 rounded-xl text-[10px] font-black text-slate-500 hover:text-slate-700 uppercase tracking-widest flex items-center gap-1">
                    <X className="w-3.5 h-3.5" /> Cancelar
                </button>
                <button onClick={handleSave} disabled={!canSubmit}
                    className="px-4 h-9 rounded-xl text-[10px] font-black text-white bg-emerald-600 hover:bg-emerald-700 uppercase tracking-widest flex items-center gap-1 disabled:opacity-50 disabled:cursor-not-allowed">
                    <Check className="w-3.5 h-3.5" /> {submitting ? 'Guardando...' : 'Registrar Recepción'}
                </button>
            </div>
        </div>
    );
}
