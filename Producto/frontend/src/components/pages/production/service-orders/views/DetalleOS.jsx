import React, { useState } from 'react';
import { ChevronLeft, Lock, Truck, Inbox, Plus, X, Check } from 'lucide-react';

const STATUS_BADGE = {
    EMITIDA:      'bg-amber-50 text-amber-600 border-amber-100',
    EN_PROCESO:   'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:      'bg-slate-100 text-slate-500 border-slate-200',
};

export default function DetalleOS({
    selectedOS, back, registrarDespacho, registrarRecepcion, cerrar, formatCLP,
}) {
    const [tab, setTab] = useState('despachos');
    const [showDespachoForm, setShowDespachoForm] = useState(false);
    const [showRecepcionForm, setShowRecepcionForm] = useState(false);

    if (!selectedOS) return null;

    const os = selectedOS;
    const restantePorDespachar = (os.cantidadPactada || 0) - os.totalDespachado;
    const restantePorRecibir = os.totalDespachado - os.totalRecibido;
    const cerrable = os.estado === 'RECEPCIONADA';
    const editable = os.estado !== 'CERRADA';

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-32">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-6">
                    <button onClick={back}
                        className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-indigo-600 active:scale-95">
                        <ChevronLeft className="w-5 h-5" />
                    </button>
                    <div>
                        <div className="flex items-center gap-3">
                            <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">OS #{os.idOS}</h2>
                            <span className={`px-3 py-1.5 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_BADGE[os.estado] || ''}`}>
                                {os.estado}
                            </span>
                            <span className="text-[9px] font-black text-violet-600 bg-violet-50 px-3 py-1.5 rounded-lg uppercase tracking-widest border border-violet-100">
                                {os.tipoServicio}
                            </span>
                        </div>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-1">
                            {os.numeroOS} · OP #{os.opId} · Proveedor #{os.proveedorId}
                        </p>
                    </div>
                </div>
                {cerrable && (
                    <button onClick={() => cerrar(os.idOS)}
                        className="bg-slate-900 hover:bg-black text-white rounded-2xl px-6 h-12 font-black text-xs uppercase tracking-widest shadow-xl shadow-slate-200 transition-all active:scale-95 flex items-center gap-2">
                        <Lock className="w-4 h-4" /> Cerrar OS
                    </button>
                )}
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main area: tabs Despachos / Recepciones */}
                <div className="lg:col-span-2 space-y-6">
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="border-b border-slate-100 flex">
                            <TabBtn active={tab === 'despachos'} onClick={() => setTab('despachos')}>
                                <Truck className="w-4 h-4" /> Despachos ({os.despachos.length})
                            </TabBtn>
                            <TabBtn active={tab === 'recepciones'} onClick={() => setTab('recepciones')}>
                                <Inbox className="w-4 h-4" /> Recepciones ({os.recepciones.length})
                            </TabBtn>
                        </div>

                        {tab === 'despachos' && (
                            <div className="p-6 space-y-4">
                                <div className="flex justify-between items-center">
                                    <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">
                                        Despachado: <span className="text-slate-700">{os.totalDespachado}</span> / Pactado: <span className="text-slate-700">{os.cantidadPactada}</span>
                                    </p>
                                    {editable && restantePorDespachar > 0 && (
                                        <button onClick={() => setShowDespachoForm(s => !s)}
                                            className="text-[10px] font-black uppercase tracking-widest text-indigo-600 hover:text-indigo-800 flex items-center gap-1">
                                            <Plus className="w-3.5 h-3.5" /> Registrar Despacho
                                        </button>
                                    )}
                                </div>

                                {showDespachoForm && (
                                    <DespachoForm
                                        maxCantidad={restantePorDespachar}
                                        onCancel={() => setShowDespachoForm(false)}
                                        onSave={async (despacho) => {
                                            await registrarDespacho(os.idOS, despacho);
                                            setShowDespachoForm(false);
                                        }}
                                    />
                                )}

                                <ListaSimple
                                    items={os.despachos}
                                    columns={[
                                        { key: 'idDespacho',         label: '#' },
                                        { key: 'fechaDespacho',      label: 'Fecha' },
                                        { key: 'cantidadDespachada', label: 'Cantidad', align: 'right' },
                                        { key: 'responsable',         label: 'Responsable' },
                                        { key: 'descripcion',         label: 'Descripción' },
                                    ]}
                                    emptyText="Sin despachos registrados"
                                />
                            </div>
                        )}

                        {tab === 'recepciones' && (
                            <div className="p-6 space-y-4">
                                <div className="flex justify-between items-center">
                                    <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">
                                        Recibido: <span className="text-slate-700">{os.totalRecibido}</span> / Despachado: <span className="text-slate-700">{os.totalDespachado}</span>
                                    </p>
                                    {editable && os.totalDespachado > 0 && restantePorRecibir > 0 && (
                                        <button onClick={() => setShowRecepcionForm(s => !s)}
                                            className="text-[10px] font-black uppercase tracking-widest text-indigo-600 hover:text-indigo-800 flex items-center gap-1">
                                            <Plus className="w-3.5 h-3.5" /> Registrar Recepción
                                        </button>
                                    )}
                                </div>

                                {showRecepcionForm && (
                                    <RecepcionForm
                                        maxCantidad={restantePorRecibir}
                                        onCancel={() => setShowRecepcionForm(false)}
                                        onSave={async (recepcion) => {
                                            await registrarRecepcion(os.idOS, recepcion);
                                            setShowRecepcionForm(false);
                                        }}
                                    />
                                )}

                                <ListaSimple
                                    items={os.recepciones}
                                    columns={[
                                        { key: 'idRecepcion',         label: '#' },
                                        { key: 'fechaRecepcion',      label: 'Fecha' },
                                        { key: 'cantidadRecibida',    label: 'Recibida', align: 'right' },
                                        { key: 'cantidadConforme',    label: 'Conforme', align: 'right' },
                                        { key: 'cantidadDefectuosa',  label: 'Defectuosa', align: 'right' },
                                        { key: 'responsable',          label: 'Responsable' },
                                    ]}
                                    emptyText="Sin recepciones registradas"
                                />
                            </div>
                        )}
                    </div>

                    {os.descripcionTrabajo && (
                        <div className="bg-white rounded-[2rem] border border-slate-100 p-6 space-y-3">
                            <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Descripción del Trabajo</h4>
                            <p className="text-[11px] font-medium italic text-slate-500 leading-relaxed">{os.descripcionTrabajo}</p>
                        </div>
                    )}
                </div>

                {/* Sidebar */}
                <div className="space-y-6">
                    <div className="bg-slate-900 rounded-[2.5rem] p-8 space-y-6 text-white">
                        <h3 className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.25em]">Resumen</h3>
                        <div className="space-y-2">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Total Neto</p>
                            <p className="text-3xl font-black tracking-tighter tabular-nums text-emerald-400">{formatCLP(os.totalNeto)}</p>
                        </div>
                        <div className="pt-6 border-t border-slate-800 space-y-3">
                            <Row label="Cant. Pactada" value={os.cantidadPactada} />
                            <Row label="Despachado" value={os.totalDespachado} />
                            <Row label="Recibido" value={os.totalRecibido} />
                            <Row label="Fecha Emisión" value={os.fechaEmision || '—'} />
                            <Row label="Fecha Entrega" value={os.fechaEntregaEstimada || '—'} />
                        </div>
                    </div>

                    {os.observaciones && (
                        <div className="bg-white rounded-[2rem] border border-slate-100 p-6 space-y-3">
                            <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Observaciones</h4>
                            <p className="text-[11px] font-medium italic text-slate-500 leading-relaxed">{os.observaciones}</p>
                        </div>
                    )}
                </div>
            </div>
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

function ListaSimple({ items, columns, emptyText }) {
    if (!items || items.length === 0) {
        return <p className="text-center text-slate-400 text-[10px] font-bold uppercase tracking-widest py-8">{emptyText}</p>;
    }
    return (
        <table className="w-full text-left">
            <thead className="bg-slate-50/50 border-b border-slate-100">
                <tr>
                    {columns.map(c => (
                        <th key={c.key} className={`px-3 py-3 text-[9px] font-black text-slate-400 uppercase tracking-widest text-${c.align || 'left'}`}>
                            {c.label}
                        </th>
                    ))}
                </tr>
            </thead>
            <tbody className="divide-y divide-slate-50">
                {items.map((item, idx) => (
                    <tr key={item.id ?? idx} className="hover:bg-indigo-50/30">
                        {columns.map(c => (
                            <td key={c.key} className={`px-3 py-3 text-xs text-slate-700 text-${c.align || 'left'}`}>
                                {item[c.key] ?? '—'}
                            </td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

function DespachoForm({ maxCantidad, onCancel, onSave }) {
    const [form, setForm] = useState({
        fechaDespacho: '',
        cantidadDespachada: '',
        descripcion: '',
        responsable: '',
        observaciones: '',
    });
    const set = (k, v) => setForm(p => ({ ...p, [k]: v }));
    const cantidad = Number(form.cantidadDespachada);
    const ok = cantidad > 0 && cantidad <= maxCantidad;

    return (
        <div className="bg-amber-50/40 border border-amber-200 rounded-2xl p-5 space-y-4">
            <p className="text-[10px] font-black text-amber-700 uppercase tracking-widest">Nuevo Despacho · Máx: {maxCantidad}</p>
            <div className="grid grid-cols-2 gap-3">
                <input type="date" value={form.fechaDespacho} onChange={(e) => set('fechaDespacho', e.target.value)}
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold" />
                <input type="number" min={1} max={maxCantidad} value={form.cantidadDespachada} onChange={(e) => set('cantidadDespachada', e.target.value)}
                    placeholder="Cantidad"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-black" />
                <input value={form.responsable} onChange={(e) => set('responsable', e.target.value)} placeholder="Responsable"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold col-span-2" />
                <input value={form.descripcion} onChange={(e) => set('descripcion', e.target.value)} placeholder="Descripción (opcional)"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold col-span-2" />
                <textarea rows={2} value={form.observaciones} onChange={(e) => set('observaciones', e.target.value)} placeholder="Observaciones"
                    className="px-3 py-2 bg-white border border-slate-100 rounded-xl text-xs font-medium italic col-span-2 resize-none" />
            </div>
            <div className="flex justify-end gap-2">
                <button onClick={onCancel} className="px-4 h-9 rounded-xl text-[10px] font-black text-slate-500 hover:text-slate-700 uppercase tracking-widest flex items-center gap-1">
                    <X className="w-3.5 h-3.5" /> Cancelar
                </button>
                <button onClick={() => onSave({
                    fechaDespacho: form.fechaDespacho || null,
                    cantidadDespachada: cantidad,
                    descripcion: form.descripcion || null,
                    responsable: form.responsable || null,
                    observaciones: form.observaciones || null,
                })} disabled={!ok}
                    className="px-4 h-9 rounded-xl text-[10px] font-black text-white bg-amber-600 hover:bg-amber-700 uppercase tracking-widest flex items-center gap-1 disabled:opacity-50">
                    <Check className="w-3.5 h-3.5" /> Registrar
                </button>
            </div>
        </div>
    );
}

function RecepcionForm({ maxCantidad, onCancel, onSave }) {
    const [form, setForm] = useState({
        fechaRecepcion: '',
        cantidadRecibida: '',
        cantidadConforme: '',
        cantidadDefectuosa: '',
        responsable: '',
        observaciones: '',
    });
    const set = (k, v) => setForm(p => ({ ...p, [k]: v }));
    const recibida = Number(form.cantidadRecibida);
    const ok = recibida > 0 && recibida <= maxCantidad;

    return (
        <div className="bg-emerald-50/40 border border-emerald-200 rounded-2xl p-5 space-y-4">
            <p className="text-[10px] font-black text-emerald-700 uppercase tracking-widest">Nueva Recepción · Máx: {maxCantidad}</p>
            <div className="grid grid-cols-3 gap-3">
                <input type="date" value={form.fechaRecepcion} onChange={(e) => set('fechaRecepcion', e.target.value)}
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold col-span-3" />
                <input type="number" min={1} max={maxCantidad} value={form.cantidadRecibida} onChange={(e) => set('cantidadRecibida', e.target.value)}
                    placeholder="Recibida"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-black" />
                <input type="number" min={0} value={form.cantidadConforme} onChange={(e) => set('cantidadConforme', e.target.value)}
                    placeholder="Conforme"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-black" />
                <input type="number" min={0} value={form.cantidadDefectuosa} onChange={(e) => set('cantidadDefectuosa', e.target.value)}
                    placeholder="Defectuosa"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-black" />
                <input value={form.responsable} onChange={(e) => set('responsable', e.target.value)} placeholder="Responsable"
                    className="h-10 px-3 bg-white border border-slate-100 rounded-xl text-xs font-bold col-span-3" />
                <textarea rows={2} value={form.observaciones} onChange={(e) => set('observaciones', e.target.value)} placeholder="Observaciones"
                    className="px-3 py-2 bg-white border border-slate-100 rounded-xl text-xs font-medium italic col-span-3 resize-none" />
            </div>
            <div className="flex justify-end gap-2">
                <button onClick={onCancel} className="px-4 h-9 rounded-xl text-[10px] font-black text-slate-500 hover:text-slate-700 uppercase tracking-widest flex items-center gap-1">
                    <X className="w-3.5 h-3.5" /> Cancelar
                </button>
                <button onClick={() => onSave({
                    fechaRecepcion: form.fechaRecepcion || null,
                    cantidadRecibida: recibida,
                    cantidadConforme: form.cantidadConforme ? Number(form.cantidadConforme) : null,
                    cantidadDefectuosa: form.cantidadDefectuosa ? Number(form.cantidadDefectuosa) : null,
                    responsable: form.responsable || null,
                    observaciones: form.observaciones || null,
                })} disabled={!ok}
                    className="px-4 h-9 rounded-xl text-[10px] font-black text-white bg-emerald-600 hover:bg-emerald-700 uppercase tracking-widest flex items-center gap-1 disabled:opacity-50">
                    <Check className="w-3.5 h-3.5" /> Registrar
                </button>
            </div>
        </div>
    );
}
