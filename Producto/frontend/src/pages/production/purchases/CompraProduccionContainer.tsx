import { Loader2, ShoppingCart, Plus, History, CheckCircle2, Truck, Lock, Eye } from 'lucide-react';
import EmisorCompraProduccion from './EmisorCompraProduccion';
import DetalleOC from './DetalleOC';
import { useOCState } from '../../../hooks/useOCState';

const STATUS_BADGE = {
    EMITIDA:               'bg-amber-50 text-amber-600 border-amber-100',
    ENVIADA:               'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA_PARCIAL:  'bg-violet-50 text-violet-600 border-violet-100',
    RECEPCIONADA:          'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:               'bg-slate-100 text-slate-500 border-slate-200',
};

const TABS = [
    { key: 'all',                  label: 'Global' },
    { key: 'EMITIDA',              label: 'Emitidas' },
    { key: 'ENVIADA',              label: 'Enviadas' },
    { key: 'RECEPCIONADA_PARCIAL', label: 'Parcial' },
    { key: 'RECEPCIONADA',         label: 'Recibidas' },
    { key: 'CERRADA',              label: 'Cerradas' },
];

export default function CompraProduccionContainer() {
    const {
        view, ocs, hcsAprobadas, recepciones, proveedores, loading, submitting, error,
        activeTab, setActiveTab, searchTerm, setSearchTerm,
        selectedOC, openCreate, openDetail, back,
        generarConsolidada, marcarEnviada, marcarRecepcionada, cerrar, actualizarPrecioItem,
        registrarRecepcion,
        formatCLP,
    } = useOCState();

    if (loading && view === 'list') {
        return (
            <div className="flex flex-col items-center justify-center min-h-[400px]">
                <Loader2 className="w-12 h-12 text-indigo-600 animate-spin mb-4" />
                <p className="text-gray-500 font-bold animate-pulse tracking-widest uppercase text-xs">Cargando OCs...</p>
            </div>
        );
    }

    if (view === 'create') {
        return (
            <EmisorCompraProduccion
                onBack={back}
                hcsAprobadas={hcsAprobadas}
                proveedores={proveedores}
                onGenerar={generarConsolidada}
                submitting={submitting}
                error={error}
                formatCLP={formatCLP}
            />
        );
    }

    if (view === 'detail' && selectedOC) {
        return (
            <DetalleOC
                oc={selectedOC}
                recepciones={recepciones}
                onBack={back}
                onMarcarEnviada={marcarEnviada}
                onMarcarRecepcionada={marcarRecepcionada}
                onCerrar={cerrar}
                onActualizarPrecio={actualizarPrecioItem}
                onRegistrarRecepcion={registrarRecepcion}
                submitting={submitting}
                formatCLP={formatCLP}
            />
        );
    }

    // List view
    const activas = ocs.filter(o => o.estado !== 'CERRADA').length;
    const pendientesRecepcion = ocs.filter(o => ['ENVIADA', 'RECEPCIONADA_PARCIAL'].includes(o.estado)).length;
    const cerradas = ocs.filter(o => o.estado === 'CERRADA').length;

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight italic uppercase">Compras de Producción</h1>
                    <p className="text-sm text-gray-500 font-medium">Consolidación N:M desde HCs aprobadas</p>
                </div>
                <button
                    onClick={openCreate}
                    className="flex items-center gap-2 px-6 py-3 bg-indigo-600 text-white rounded-2xl font-black uppercase text-[10px] tracking-widest hover:bg-indigo-700 transition-all shadow-lg shadow-indigo-200 active:scale-95"
                >
                    <Plus className="w-4 h-4" /> Generar OC Consolidada
                </button>
            </div>

            {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {error}
                </div>
            )}

            {/* Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <StatusCard label="OCs Activas"            value={activas}              icon={ShoppingCart} color="bg-blue-500" />
                <StatusCard label="Pendientes Recepción"   value={pendientesRecepcion}  icon={Truck}        color="bg-amber-500" />
                <StatusCard label="Cerradas"                value={cerradas}             icon={History}      color="bg-emerald-500" />
            </div>

            {/* Filters */}
            <div className="flex items-center justify-between flex-wrap gap-3">
                <div className="flex gap-2 bg-slate-900/5 p-1 rounded-2xl border border-slate-200">
                    {TABS.map(tab => (
                        <button
                            key={tab.key}
                            onClick={() => setActiveTab(tab.key)}
                            className={`rounded-xl font-black text-[10px] uppercase tracking-widest px-4 py-2.5 transition-all ${
                                activeTab === tab.key ? 'bg-slate-900 text-white' : 'text-slate-500 hover:text-slate-900'
                            }`}
                        >
                            {tab.label}
                        </button>
                    ))}
                </div>
                <input
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    placeholder="Buscar por número, ID, proveedor..."
                    className="px-5 h-12 bg-white border border-slate-100 rounded-2xl text-xs font-bold focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
            </div>

            {/* Tabla */}
            <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                <table className="w-full text-left">
                    <thead className="bg-gray-50 border-b border-gray-100">
                        <tr>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Nro OC</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Proveedor</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Items</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Total Neto</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Estado</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acciones</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-50">
                        {ocs.map((oc) => (
                            <tr key={oc.idOC} className="hover:bg-indigo-50/30 transition-all">
                                <td className="px-8 py-6">
                                    <div className="flex flex-col">
                                        <span className="font-black text-gray-900 text-sm tracking-tight italic uppercase">OC #{oc.idOC}</span>
                                        <span className="text-[9px] text-slate-400 font-bold uppercase tracking-widest mt-1">{oc.numeroOC}</span>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <span className="text-[10px] font-black text-indigo-600 bg-indigo-50 px-3 py-1.5 rounded-lg border border-indigo-100 uppercase tracking-widest">
                                        Prov #{oc.proveedorId}
                                    </span>
                                </td>
                                <td className="px-8 py-6 text-center text-xs font-black text-gray-700">{oc.items?.length ?? 0}</td>
                                <td className="px-8 py-6 text-right text-xs font-black text-gray-700 tabular-nums">{formatCLP(oc.totalNeto)}</td>
                                <td className="px-8 py-6 text-center">
                                    <span className={`px-3 py-1.5 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_BADGE[oc.estado] || ''}`}>
                                        {oc.estado}
                                    </span>
                                </td>
                                <td className="px-8 py-6 text-right">
                                    <div className="flex items-center justify-end gap-1">
                                        {oc.estado === 'EMITIDA' && (
                                            <button onClick={() => marcarEnviada(oc.idOC)} title="Marcar Enviada"
                                                className="p-2 rounded-xl hover:bg-blue-600 hover:text-white transition-all">
                                                <Truck className="w-4 h-4" />
                                            </button>
                                        )}
                                        {['ENVIADA', 'RECEPCIONADA_PARCIAL'].includes(oc.estado) && (
                                            <button onClick={() => marcarRecepcionada(oc.idOC)} title="Marcar Recepcionada"
                                                className="p-2 rounded-xl hover:bg-emerald-600 hover:text-white transition-all">
                                                <CheckCircle2 className="w-4 h-4" />
                                            </button>
                                        )}
                                        {oc.estado === 'RECEPCIONADA' && (
                                            <button onClick={() => cerrar(oc.idOC)} title="Cerrar"
                                                className="p-2 rounded-xl hover:bg-slate-900 hover:text-white transition-all">
                                                <Lock className="w-4 h-4" />
                                            </button>
                                        )}
                                        <button onClick={() => openDetail(oc)} title="Ver detalle"
                                            className="p-2 rounded-xl hover:bg-indigo-600 hover:text-white transition-all">
                                            <Eye className="w-4 h-4" />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {ocs.length === 0 && !loading && (
                    <div className="py-20 text-center text-slate-400 font-bold uppercase tracking-widest text-xs">
                        Sin órdenes de compra en este filtro
                    </div>
                )}
            </div>
        </div>
    );
}

function StatusCard({ label, value, icon: Icon, color }) {
    return (
        <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-gray-100 flex items-center gap-6">
            <div className={`${color} p-4 rounded-2xl text-white shadow-lg`}>
                <Icon className="w-6 h-6" />
            </div>
            <div>
                <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest">{label}</p>
                <p className="text-2xl font-black text-gray-900 tracking-tight">{value}</p>
            </div>
        </div>
    );
}
