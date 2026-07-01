import React from 'react';
import { useTrazabilidad } from '../../hooks/useTrazabilidad';
import {
    Search, Loader2, Package, GitBranch, ShoppingBag, ShoppingCart,
    Inbox, Factory, X, Hash, Calendar
} from 'lucide-react';

const STATUS_OC = {
    EMITIDA:               'bg-amber-50 text-amber-600 border-amber-100',
    ENVIADA:               'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA_PARCIAL:  'bg-violet-50 text-violet-600 border-violet-100',
    RECEPCIONADA:          'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:               'bg-slate-100 text-slate-500 border-slate-200',
};
const STATUS_OS = {
    EMITIDA:      'bg-amber-50 text-amber-600 border-amber-100',
    EN_PROCESO:   'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:      'bg-slate-100 text-slate-500 border-slate-200',
};
const STATUS_HC = {
    BORRADOR: 'bg-amber-50 text-amber-600 border-amber-100',
    APROBADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:  'bg-slate-100 text-slate-500 border-slate-200',
};

export default function TrazabilidadOP() {
    const { opIdInput, setOpIdInput, data, loading, error, buscar, limpiar, formatCLP } = useTrazabilidad();

    return (
        <div className="space-y-8 pb-32">
            {/* Header + buscador */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 bg-white/40 backdrop-blur-xl p-8 rounded-[2.5rem] border border-white/20 shadow-2xl shadow-indigo-100/20">
                <div className="flex items-center gap-5">
                    <div className="bg-violet-600 p-4 rounded-3xl shadow-xl shadow-violet-200">
                        <GitBranch className="w-8 h-8 text-white" />
                    </div>
                    <div>
                        <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">Trazabilidad de OP</h2>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-1">
                            NV → OP → Costeo → HC → OCs → Recepciones · OSs
                        </p>
                    </div>
                </div>
                <div className="flex items-center gap-3">
                    <div className="relative">
                        <Hash className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                        <input
                            type="number"
                            min={1}
                            value={opIdInput}
                            onChange={(e) => setOpIdInput(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && buscar()}
                            placeholder="ID Orden de Producción"
                            className="pl-12 h-12 w-[280px] bg-white border border-slate-100 rounded-2xl text-xs font-bold focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        />
                    </div>
                    <button onClick={() => buscar()}
                        className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl h-12 px-6 font-black text-xs uppercase tracking-widest shadow-xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2">
                        <Search className="w-4 h-4" />
                        Rastrear
                    </button>
                    {data && (
                        <button onClick={limpiar}
                            className="bg-white border border-slate-100 hover:bg-slate-50 text-slate-500 rounded-2xl h-12 px-4 font-black text-[10px] uppercase tracking-widest flex items-center gap-1">
                            <X className="w-3.5 h-3.5" /> Limpiar
                        </button>
                    )}
                </div>
            </div>

            {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">{error}</div>
            )}

            {loading && (
                <div className="flex justify-center py-16">
                    <Loader2 className="w-10 h-10 text-indigo-600 animate-spin" />
                </div>
            )}

            {!loading && !data && !error && (
                <div className="text-center py-32 text-slate-400 font-bold uppercase tracking-widest text-xs">
                    Ingresa un opId y presiona Rastrear para ver la cadena completa
                </div>
            )}

            {data && (
                <div className="space-y-6">
                    {/* Bloque OP */}
                    <Bloque icon={Package} title="Orden de Producción" color="indigo">
                        <Grid>
                            <Field label="ID OP">#{data.opId}</Field>
                            <Field label="Número">{data.numeroOP || '—'}</Field>
                            <Field label="Estado">{data.estadoOP || '—'}</Field>
                            <Field label="NV Origen">#{data.notaVentaId ?? '—'}</Field>
                            <Field label="Fecha Entrega">{data.fechaEntregaProgramada || '—'}</Field>
                        </Grid>
                    </Bloque>

                    {/* Bloque Costeo Version */}
                    {data.costeoVersion ? (
                        <Bloque icon={GitBranch} title="Versión de Costeo (snapshot)" color="indigo">
                            <Grid>
                                <Field label="ID Versión">#{data.costeoVersion.idCosteoVersion}</Field>
                                <Field label="Costeo Origen">#{data.costeoVersion.costeoId}</Field>
                                <Field label="N° Versión">v{data.costeoVersion.numeroVersion}</Field>
                                <Field label="Fecha">{data.costeoVersion.fechaCreacion?.split('T')[0] || '—'}</Field>
                                <Field label="Usuario">{data.costeoVersion.usuarioCreador || '—'}</Field>
                                <Field label="Motivo" wide>{data.costeoVersion.motivoCambio || '—'}</Field>
                            </Grid>
                        </Bloque>
                    ) : (
                        <Vacio mensaje="La OP no tiene versión de Costeo vinculada" />
                    )}

                    {/* Bloque HC */}
                    {data.hojaCompra ? (
                        <Bloque icon={ShoppingBag} title="Hoja de Compra" color="emerald">
                            <div className="flex items-center gap-3 mb-4">
                                <span className="text-[10px] font-black text-emerald-700 bg-emerald-50 px-3 py-1 rounded-lg uppercase tracking-widest">HC #{data.hojaCompra.idHC}</span>
                                <span className="text-[10px] font-bold text-slate-400 uppercase">{data.hojaCompra.numeroHC}</span>
                                <span className={`px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_HC[data.hojaCompra.estado] || ''}`}>
                                    {data.hojaCompra.estado}
                                </span>
                                <span className="text-[10px] text-slate-400 font-bold uppercase ml-auto">{data.hojaCompra.items.length} items</span>
                            </div>
                            <table className="w-full text-left text-xs">
                                <thead>
                                    <tr className="text-[9px] font-black text-slate-400 uppercase tracking-widest border-b border-slate-100">
                                        <th className="py-2">Insumo</th>
                                        <th className="py-2 text-center">Tipo</th>
                                        <th className="py-2 text-center">Cant. Req.</th>
                                        <th className="py-2 text-right">Precio Ref.</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {data.hojaCompra.items.map(item => (
                                        <tr key={item.idHCItem} className="border-b border-slate-50">
                                            <td className="py-2 font-bold uppercase">{item.nombreInsumo}</td>
                                            <td className="py-2 text-[10px] text-slate-500 uppercase text-center">{item.tipoInsumo}</td>
                                            <td className="py-2 text-center font-black tabular-nums">{item.cantidadRequerida}</td>
                                            <td className="py-2 text-right font-black text-indigo-600 tabular-nums">{formatCLP(item.precioUnitarioRef)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </Bloque>
                    ) : (
                        <Vacio mensaje="No se ha generado HC para esta OP" />
                    )}

                    {/* Bloque OCs */}
                    {data.ordenesCompra.length > 0 ? (
                        <Bloque icon={ShoppingCart} title={`Órdenes de Compra (${data.ordenesCompra.length})`} color="blue">
                            <div className="space-y-3">
                                {data.ordenesCompra.map(oc => (
                                    <div key={oc.idOC} className="border border-slate-100 rounded-2xl p-4 bg-slate-50/30 flex items-center justify-between">
                                        <div className="flex items-center gap-3">
                                            <span className="text-[10px] font-black text-blue-700 bg-blue-50 px-3 py-1 rounded-lg uppercase tracking-widest">OC #{oc.idOC}</span>
                                            <span className="text-[10px] font-bold text-slate-400 uppercase">{oc.numeroOC}</span>
                                            <span className="text-[10px] font-bold text-slate-500 uppercase">Prov #{oc.proveedorId}</span>
                                            <span className={`px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_OC[oc.estado] || ''}`}>{oc.estado}</span>
                                        </div>
                                        <span className="text-xs font-black text-slate-700 tabular-nums">{formatCLP(oc.totalNeto)}</span>
                                    </div>
                                ))}
                            </div>
                        </Bloque>
                    ) : (
                        <Vacio mensaje="No hay OCs generadas para esta OP" />
                    )}

                    {/* Bloque Recepciones OC */}
                    {data.recepcionesOC.length > 0 && (
                        <Bloque icon={Inbox} title={`Recepciones de OC (${data.recepcionesOC.length})`} color="emerald">
                            <div className="space-y-3">
                                {data.recepcionesOC.map(rec => (
                                    <div key={rec.idRecepcion} className="border border-slate-100 rounded-2xl p-4 bg-slate-50/30">
                                        <div className="flex items-center gap-3 mb-2">
                                            <span className="text-[10px] font-black text-emerald-700 bg-emerald-50 px-3 py-1 rounded-lg uppercase tracking-widest">Recepción #{rec.idRecepcion}</span>
                                            <span className="text-[10px] font-bold text-slate-400 uppercase">
                                                <Calendar className="w-3 h-3 inline mr-1" />{rec.fechaRecepcion}
                                            </span>
                                            {rec.numeroGuia && <span className="text-[10px] font-bold text-slate-500 italic">Guía: {rec.numeroGuia}</span>}
                                            {rec.responsable && <span className="text-[10px] font-bold text-slate-500 italic">Por: {rec.responsable}</span>}
                                        </div>
                                        <p className="text-[10px] font-bold text-slate-400 uppercase">
                                            {rec.items.length} item(s) · Total recibido: <span className="text-slate-700">{rec.totalRecibido}</span>
                                        </p>
                                    </div>
                                ))}
                            </div>
                        </Bloque>
                    )}

                    {/* Bloque OSs */}
                    {data.ordenesServicio.length > 0 ? (
                        <Bloque icon={Factory} title={`Órdenes de Servicio (${data.ordenesServicio.length})`} color="violet">
                            <div className="space-y-3">
                                {data.ordenesServicio.map(os => (
                                    <div key={os.idOS} className="border border-slate-100 rounded-2xl p-4 bg-slate-50/30 flex items-center justify-between">
                                        <div className="flex items-center gap-3">
                                            <span className="text-[10px] font-black text-violet-700 bg-violet-50 px-3 py-1 rounded-lg uppercase tracking-widest">OS #{os.idOS}</span>
                                            <span className="text-[10px] font-bold text-slate-400 uppercase">{os.tipoServicio}</span>
                                            <span className="text-[10px] font-bold text-slate-500 uppercase">Prov #{os.proveedorId}</span>
                                            <span className={`px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_OS[os.estado] || ''}`}>{os.estado}</span>
                                        </div>
                                        <div className="flex items-center gap-3 text-[10px] font-bold text-slate-500 uppercase">
                                            <span>Pact: <span className="text-slate-800 font-black">{os.cantidadPactada}</span></span>
                                            <span>Desp: <span className="text-blue-600 font-black">{os.totalDespachado}</span></span>
                                            <span>Recib: <span className="text-emerald-600 font-black">{os.totalRecibido}</span></span>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </Bloque>
                    ) : (
                        <Vacio mensaje="No hay OSs vinculadas a esta OP" />
                    )}
                </div>
            )}
        </div>
    );
}

function Bloque({ icon: Icon, title, color, children }) {
    const colorClass = {
        indigo: 'bg-indigo-50 text-indigo-600',
        emerald: 'bg-emerald-50 text-emerald-600',
        blue: 'bg-blue-50 text-blue-600',
        violet: 'bg-violet-50 text-violet-600',
    }[color] || 'bg-slate-50 text-slate-600';
    return (
        <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 p-8 space-y-4">
            <div className="flex items-center gap-3">
                <div className={`${colorClass} p-2 rounded-xl`}>
                    <Icon className="w-5 h-5" />
                </div>
                <h3 className="text-sm font-black text-slate-800 uppercase tracking-widest">{title}</h3>
            </div>
            {children}
        </div>
    );
}

function Grid({ children }) {
    return <div className="grid grid-cols-2 md:grid-cols-3 gap-6">{children}</div>;
}

function Field({ label, children, wide }) {
    return (
        <div className={`space-y-1 ${wide ? 'col-span-2 md:col-span-3' : ''}`}>
            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">{label}</p>
            <p className="text-xs font-black text-slate-700 uppercase tracking-tight">{children}</p>
        </div>
    );
}

function Vacio({ mensaje }) {
    return (
        <div className="bg-slate-50 border border-dashed border-slate-200 rounded-2xl p-6 text-center text-[10px] font-bold text-slate-400 uppercase tracking-widest">
            {mensaje}
        </div>
    );
}
