import React from 'react';
import { useReportes } from '../../../../hooks/useReportes';
import { Loader2, RefreshCw, ShoppingBag, ShoppingCart, Factory, AlertCircle } from 'lucide-react';

const STATUS_HC = {
    BORRADOR: 'bg-amber-50 text-amber-600 border-amber-100',
    APROBADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:  'bg-slate-100 text-slate-500 border-slate-200',
};
const STATUS_OC = {
    EMITIDA:               'bg-amber-50 text-amber-600 border-amber-100',
    ENVIADA:               'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA_PARCIAL:  'bg-violet-50 text-violet-600 border-violet-100',
    RECEPCIONADA:          'bg-emerald-50 text-emerald-600 border-emerald-100',
};
const STATUS_OS = {
    EMITIDA:      'bg-amber-50 text-amber-600 border-amber-100',
    EN_PROCESO:   'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
};

export default function ReportesDashboard() {
    const { hcsPendientes, ocsPendientes, ossEnTaller, loading, error, refresh, formatCLP } = useReportes();

    return (
        <div className="space-y-8 pb-32">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight italic uppercase">Reportes Operativos</h1>
                    <p className="text-sm text-gray-500 font-medium">Indicadores en tiempo real del flujo producción → compras</p>
                </div>
                <button onClick={refresh} disabled={loading}
                    className="flex items-center gap-2 px-6 py-3 bg-slate-900 text-white rounded-2xl font-black uppercase text-[10px] tracking-widest hover:bg-slate-700 transition-all shadow-lg shadow-slate-200 active:scale-95 disabled:opacity-50">
                    <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
                    Refrescar
                </button>
            </div>

            {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl flex items-center gap-2">
                    <AlertCircle className="w-4 h-4" /> {error}
                </div>
            )}

            {/* KPIs */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <KPICard label="HCs pendientes" sublabel="por aprobar" value={hcsPendientes.length} icon={ShoppingBag} color="bg-amber-500" />
                <KPICard label="OCs pendientes" sublabel="por recepcionar" value={ocsPendientes.length} icon={ShoppingCart} color="bg-blue-500" />
                <KPICard label="OSs en taller" sublabel="EN_PROCESO o RECEPCIONADA" value={ossEnTaller.length} icon={Factory} color="bg-violet-500" />
            </div>

            {loading && (
                <div className="flex justify-center py-12">
                    <Loader2 className="w-10 h-10 text-indigo-600 animate-spin" />
                </div>
            )}

            {!loading && (
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    {/* HCs pendientes */}
                    <Panel
                        title="Hojas de Compra Pendientes"
                        subtitle="En estado BORRADOR"
                        icon={ShoppingBag}
                        color="amber"
                        empty="No hay HCs pendientes de aprobación"
                        data={hcsPendientes}
                        renderItem={(hc) => (
                            <Row key={hc.idHC}
                                left={`HC #${hc.idHC}`}
                                center={`OP #${hc.opId}`}
                                right={<Badge cls={STATUS_HC[hc.estado]}>{hc.estado}</Badge>}
                                meta={`${hc.items.length} items · ${hc.fechaGeneracion || '—'}`}
                            />
                        )}
                    />

                    {/* OCs pendientes */}
                    <Panel
                        title="OCs Pendientes Recepción"
                        subtitle="EMITIDA / ENVIADA / PARCIAL"
                        icon={ShoppingCart}
                        color="blue"
                        empty="No hay OCs pendientes"
                        data={ocsPendientes}
                        renderItem={(oc) => (
                            <Row key={oc.idOC}
                                left={`OC #${oc.idOC}`}
                                center={`Prov #${oc.proveedorId}`}
                                right={<Badge cls={STATUS_OC[oc.estado]}>{oc.estado}</Badge>}
                                meta={`${oc.items.length} items · ${formatCLP(oc.totalNeto)}`}
                            />
                        )}
                    />

                    {/* OSs en taller */}
                    <Panel
                        title="OSs en Taller Externo"
                        subtitle="EN_PROCESO o RECEPCIONADA (no cerrada)"
                        icon={Factory}
                        color="violet"
                        empty="No hay OSs activas en talleres"
                        data={ossEnTaller}
                        renderItem={(os) => (
                            <Row key={os.idOS}
                                left={`OS #${os.idOS}`}
                                center={`${os.tipoServicio} · Prov #${os.proveedorId}`}
                                right={<Badge cls={STATUS_OS[os.estado]}>{os.estado}</Badge>}
                                meta={`Pact: ${os.cantidadPactada} · Desp: ${os.totalDespachado} · Rec: ${os.totalRecibido}`}
                            />
                        )}
                    />
                </div>
            )}
        </div>
    );
}

function KPICard({ label, sublabel, value, icon: Icon, color }) {
    return (
        <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-gray-100 flex items-center gap-6">
            <div className={`${color} p-4 rounded-2xl text-white shadow-lg`}>
                <Icon className="w-6 h-6" />
            </div>
            <div>
                <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest">{label}</p>
                <p className="text-3xl font-black text-gray-900 tracking-tight">{value}</p>
                <p className="text-[9px] font-bold text-gray-300 uppercase tracking-widest mt-1 italic">{sublabel}</p>
            </div>
        </div>
    );
}

function Panel({ title, subtitle, icon: Icon, color, data, renderItem, empty }) {
    const colorCls = {
        amber:  'bg-amber-50 text-amber-600',
        blue:   'bg-blue-50 text-blue-600',
        violet: 'bg-violet-50 text-violet-600',
    }[color];
    return (
        <div className="bg-white rounded-[2rem] border border-gray-100 shadow-sm overflow-hidden flex flex-col">
            <div className="p-6 border-b border-slate-50 flex items-center gap-3">
                <div className={`${colorCls} p-2 rounded-xl`}>
                    <Icon className="w-5 h-5" />
                </div>
                <div>
                    <h3 className="text-[11px] font-black text-slate-800 uppercase tracking-widest">{title}</h3>
                    <p className="text-[9px] font-bold text-slate-400 uppercase tracking-widest italic">{subtitle}</p>
                </div>
            </div>
            <div className="flex-1 max-h-[480px] overflow-y-auto divide-y divide-slate-50">
                {data.length === 0 ? (
                    <div className="py-16 text-center text-[10px] font-bold text-slate-400 uppercase tracking-widest">{empty}</div>
                ) : (
                    data.map(renderItem)
                )}
            </div>
        </div>
    );
}

function Row({ left, center, right, meta }) {
    return (
        <div className="p-4 hover:bg-slate-50/40 transition-colors">
            <div className="flex items-center justify-between mb-1">
                <div className="flex items-center gap-3">
                    <span className="text-[11px] font-black text-slate-800 uppercase tracking-tight">{left}</span>
                    <span className="text-[10px] font-bold text-slate-500 uppercase">{center}</span>
                </div>
                {right}
            </div>
            {meta && <p className="text-[9px] font-bold text-slate-400 uppercase tracking-widest">{meta}</p>}
        </div>
    );
}

function Badge({ cls, children }) {
    return (
        <span className={`px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest border ${cls || ''}`}>
            {children}
        </span>
    );
}
