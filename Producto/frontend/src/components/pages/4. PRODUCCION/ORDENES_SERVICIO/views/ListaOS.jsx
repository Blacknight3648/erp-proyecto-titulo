import React from 'react';
import { Loader2, Plus, Eye, Lock, Factory } from 'lucide-react';

const STATUS_BADGE = {
    EMITIDA:      'bg-amber-50 text-amber-600 border-amber-100',
    EN_PROCESO:   'bg-blue-50 text-blue-600 border-blue-100',
    RECEPCIONADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:      'bg-slate-100 text-slate-500 border-slate-200',
};

const TABS = [
    { key: 'all',          label: 'Global' },
    { key: 'EMITIDA',      label: 'Emitidas' },
    { key: 'EN_PROCESO',   label: 'En Proceso' },
    { key: 'RECEPCIONADA', label: 'Recepcionadas' },
    { key: 'CERRADA',      label: 'Cerradas' },
];

export default function ListaOS({
    oss, activeTab, setActiveTab, searchTerm, setSearchTerm,
    openCreate, openDetail, cerrar, formatCLP, loading, error,
}) {
    if (loading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[400px]">
                <Loader2 className="w-12 h-12 text-indigo-600 animate-spin mb-4" />
                <p className="text-gray-500 font-bold animate-pulse tracking-widest uppercase text-xs">Cargando OSs...</p>
            </div>
        );
    }

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight italic uppercase">Órdenes de Servicio</h1>
                    <p className="text-sm text-gray-500 font-medium">Procesos externos: bordado, estampado, lavado…</p>
                </div>
                <button
                    onClick={openCreate}
                    className="flex items-center gap-2 px-6 py-3 bg-indigo-600 text-white rounded-2xl font-black uppercase text-[10px] tracking-widest hover:bg-indigo-700 transition-all shadow-lg shadow-indigo-200 active:scale-95"
                >
                    <Plus className="w-4 h-4" /> Nueva OS
                </button>
            </div>

            {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {error}
                </div>
            )}

            <div className="flex items-center justify-between flex-wrap gap-3">
                <div className="flex gap-2 bg-slate-900/5 p-1 rounded-2xl border border-slate-200">
                    {TABS.map(tab => (
                        <button key={tab.key} onClick={() => setActiveTab(tab.key)}
                            className={`rounded-xl font-black text-[10px] uppercase tracking-widest px-4 py-2.5 transition-all ${
                                activeTab === tab.key ? 'bg-slate-900 text-white' : 'text-slate-500 hover:text-slate-900'
                            }`}>
                            {tab.label}
                        </button>
                    ))}
                </div>
                <input
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    placeholder="Buscar por número, OP, proveedor, tipo..."
                    className="px-5 h-12 bg-white border border-slate-100 rounded-2xl text-xs font-bold focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
            </div>

            <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                <table className="w-full text-left">
                    <thead className="bg-gray-50 border-b border-gray-100">
                        <tr>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Nro OS</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">OP / Proveedor</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Tipo</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Cant. Pactada</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Total</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Estado</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acciones</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-50">
                        {oss.map(os => (
                            <tr key={os.idOS} className="hover:bg-indigo-50/30 transition-all">
                                <td className="px-8 py-6">
                                    <div className="flex flex-col">
                                        <span className="font-black text-gray-900 text-sm tracking-tight italic uppercase">OS #{os.idOS}</span>
                                        <span className="text-[9px] text-slate-400 font-bold uppercase tracking-widest mt-1 truncate max-w-[200px]">{os.numeroOS}</span>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <div className="flex flex-col gap-1">
                                        <span className="text-[10px] font-black text-indigo-600 bg-indigo-50 px-3 py-1 rounded-lg uppercase tracking-widest">OP #{os.opId}</span>
                                        <span className="text-[10px] font-black text-slate-500 bg-slate-50 px-3 py-1 rounded-lg uppercase tracking-widest">Prov #{os.proveedorId}</span>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <span className="inline-flex items-center gap-1 text-[10px] font-black text-violet-600 bg-violet-50 px-3 py-1.5 rounded-lg uppercase tracking-widest border border-violet-100">
                                        <Factory className="w-3 h-3" /> {os.tipoServicio}
                                    </span>
                                </td>
                                <td className="px-8 py-6 text-center text-xs font-black text-gray-700 tabular-nums">{os.cantidadPactada}</td>
                                <td className="px-8 py-6 text-right text-xs font-black text-gray-700 tabular-nums">{formatCLP(os.totalNeto)}</td>
                                <td className="px-8 py-6 text-center">
                                    <span className={`px-3 py-1.5 rounded-full text-[9px] font-black uppercase tracking-widest border ${STATUS_BADGE[os.estado] || ''}`}>
                                        {os.estado}
                                    </span>
                                </td>
                                <td className="px-8 py-6 text-right">
                                    <div className="flex items-center justify-end gap-1">
                                        {os.estado === 'RECEPCIONADA' && (
                                            <button onClick={() => cerrar(os.idOS)} title="Cerrar OS"
                                                className="p-2 rounded-xl hover:bg-slate-900 hover:text-white transition-all">
                                                <Lock className="w-4 h-4" />
                                            </button>
                                        )}
                                        <button onClick={() => openDetail(os)} title="Ver detalle"
                                            className="p-2 rounded-xl hover:bg-indigo-600 hover:text-white transition-all">
                                            <Eye className="w-4 h-4" />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {oss.length === 0 && !loading && (
                    <div className="py-20 text-center text-slate-400 font-bold uppercase tracking-widest text-xs">
                        Sin órdenes de servicio en este filtro
                    </div>
                )}
            </div>
        </div>
    );
}
