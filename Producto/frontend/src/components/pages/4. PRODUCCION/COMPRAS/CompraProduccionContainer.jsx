import { useState } from 'react';
import { Loader2, ShoppingCart, Plus, History } from 'lucide-react';
import EmisorCompraProduccion from './EmisorCompraProduccion';
import { mockAllOCs } from '../../../../data/mockData';

export default function CompraProduccionContainer() {
    const [view, setView] = useState('list'); // 'list', 'create'
    const [loading, setLoading] = useState(false);

    // Filtrar solo OCs que tengan vinculación con una OP
    const productionOCs = mockAllOCs.filter(oc => oc.ordenProduccionId);

    const handleCreateNew = () => {
        setView('create');
    };

    const handleBack = () => {
        setView('list');
    };

    if (loading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[400px]">
                <Loader2 className="w-12 h-12 text-indigo-600 animate-spin mb-4" />
                <p className="text-gray-500 font-bold animate-pulse tracking-widest uppercase text-xs">Procesando Adquisición de Planta...</p>
            </div>
        );
    }

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight italic uppercase">COMPRAS DE PRODUCCIÓN</h1>
                    <p className="text-sm text-gray-500 font-medium">Gestión de insumos y servicios vinculados a Ordenes de Producción</p>
                </div>
                {view === 'list' && (
                    <button 
                        onClick={handleCreateNew}
                        className="flex items-center gap-2 px-6 py-3 bg-indigo-600 text-white rounded-2xl font-black uppercase text-[10px] tracking-widest hover:bg-indigo-700 transition-all shadow-lg shadow-indigo-200 active:scale-95"
                    >
                        <Plus className="w-4 h-4" /> Nueva Orden de Compra (OP)
                    </button>
                )}
            </div>

            {view === 'list' ? (
                <div className="space-y-6">
                    {/* Resumen en tarjetas */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <StatusCard label="OCs Activas" value={productionOCs.length} icon={ShoppingCart} color="bg-blue-500" />
                        <StatusCard label="Pendientes Recepción" value={productionOCs.filter(o => o.estado === 'Pendiente').length} icon={Loader2} color="bg-amber-500" />
                        <StatusCard label="Historial (30 días)" value={productionOCs.length} icon={History} color="bg-emerald-500" />
                    </div>

                    {/* Tabla de Registros */}
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Nro OC</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">OP Vinculada</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Proveedor / Artículos</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Monto</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Estado</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {productionOCs.map((oc) => (
                                    <tr key={oc.idOC} className="hover:bg-indigo-50/30 transition-all">
                                        <td className="px-8 py-6 text-center">
                                            <span className="font-black text-gray-900 text-sm tracking-tight italic uppercase">{oc.numeroOC}</span>
                                        </td>
                                        <td className="px-8 py-6 text-center">
                                            <span className="text-[10px] font-black text-indigo-600 bg-indigo-50 px-3 py-1.5 rounded-lg border border-indigo-100 uppercase tracking-widest italic">
                                                {oc.ordenProduccionId}
                                            </span>
                                        </td>
                                        <td className="px-8 py-6">
                                            <div className="font-bold text-gray-800 text-xs uppercase">{oc.proveedor}</div>
                                            <div className="text-[10px] text-gray-400 font-medium truncate max-w-xs">{oc.items}</div>
                                        </td>
                                        <td className="px-8 py-6 text-center">
                                            <span className="text-xs font-black text-gray-700">${oc.montoTotal?.toLocaleString()}</span>
                                        </td>
                                        <td className="px-8 py-6 text-right">
                                            <span className={`px-3 py-1.5 rounded-full text-[9px] font-black uppercase tracking-widest border ${
                                                oc.estado === 'Recibida' ? 'bg-emerald-50 text-emerald-600 border-emerald-100' : 'bg-amber-50 text-amber-600 border-amber-100'
                                            }`}>
                                                {oc.estado}
                                            </span>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            ) : (
                <EmisorCompraProduccion onBack={handleBack} />
            )}
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
