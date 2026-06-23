import { useState, useMemo, useEffect } from 'react';
import { Search, Filter, ChevronRight, Factory, Activity, Loader2 } from 'lucide-react';
import { OrdenProduccionService } from '../../../../remote/service/OrdenProduccionService';

export default function OrdenProduccionList({ onSelect }) {
    const [searchTerm, setSearchTerm] = useState('');
    const [ordenes, setOrdenes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        let active = true;
        setLoading(true);
        OrdenProduccionService.getAll()
            .then(async (ops) => {
                const conAvance = await Promise.all(
                    ops.map(async (op) => {
                        try {
                            const avance = await OrdenProduccionService.getAvance(op.idOP);
                            return { ...op, progreso: Number(avance?.porcentajeGlobal) || 0 };
                        } catch {
                            return { ...op, progreso: 0 };
                        }
                    })
                );
                if (active) setOrdenes(conAvance);
            })
            .catch((err) => {
                if (active) setError(err);
            })
            .finally(() => {
                if (active) setLoading(false);
            });
        return () => { active = false; };
    }, []);

    const filteredOPs = useMemo(() => {
        const term = searchTerm.toLowerCase();
        return ordenes.filter(op =>
            (op.numeroOP || '').toLowerCase().includes(term) ||
            String(op.notaVentaId || '').toLowerCase().includes(term) ||
            (op.items || []).some(item => (item.modelo || '').toLowerCase().includes(term))
        );
    }, [ordenes, searchTerm]);

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            {/* Buscador y Filtros */}
            <div className="bg-white p-4 rounded-2xl shadow-sm border border-gray-100 flex flex-wrap gap-4 items-center">
                <div className="relative flex-1 min-w-[250px]">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar por OP, NV o Modelo..."
                        className="w-full pl-10 pr-4 py-2 bg-gray-50 border-none rounded-xl text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
                <div className="flex space-x-2">
                    <button className="flex items-center px-4 py-2 bg-gray-50 text-gray-500 rounded-lg text-[10px] font-black uppercase border border-transparent hover:border-gray-200">
                        <Filter className="w-3 h-3 mr-2" /> Estado
                    </button>
                    <button className="flex items-center px-4 py-2 bg-gray-50 text-gray-500 rounded-lg text-[10px] font-black uppercase border border-transparent hover:border-gray-200">
                        <Activity className="w-3 h-3 mr-2" /> Prioridad
                    </button>
                </div>
            </div>

            {/* Lista de OPs */}
            <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                <table className="w-full text-left">
                    <thead className="bg-gray-50 border-b border-gray-100">
                        <tr>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">ID OP</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">NV Asociada</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Estado</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Progreso</th>
                            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acción</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-50">
                        {loading && (
                            <tr>
                                <td colSpan="5" className="py-20 text-center text-gray-400 font-bold uppercase tracking-widest text-xs">
                                    <Loader2 className="w-6 h-6 mx-auto mb-2 animate-spin text-blue-500" />
                                    Cargando Ordenes de Producción...
                                </td>
                            </tr>
                        )}
                        {!loading && error && (
                            <tr>
                                <td colSpan="5" className="py-20 text-center text-red-400 font-bold italic uppercase tracking-widest text-xs">
                                    Error al cargar las Ordenes de Producción
                                </td>
                            </tr>
                        )}
                        {!loading && !error && filteredOPs.map((op) => (
                            <tr
                                key={op.idOP}
                                className="hover:bg-blue-50/40 transition-all group cursor-pointer border-l-4 border-transparent hover:border-blue-500"
                                onClick={() => onSelect(op.idOP)}
                            >
                                <td className="px-8 py-6">
                                    <div className="flex items-center">
                                        <div className="bg-blue-100 p-2.5 rounded-xl mr-4 group-hover:bg-blue-600 group-hover:text-white transition-all">
                                            <Factory className="w-5 h-5 text-blue-600 group-hover:text-white" />
                                        </div>
                                        <div>
                                            <div className="font-black text-gray-900 text-sm tracking-tight italic uppercase">{op.numeroOP || `OP-${op.idOP}`}</div>
                                            <div className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">{(op.items?.length || 0)} ítem(s)</div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <span className="text-[10px] font-black text-indigo-600 bg-indigo-50 px-3 py-1.5 rounded-lg border border-indigo-100 uppercase tracking-widest italic">
                                        {op.notaVentaId ?? '-'}
                                    </span>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <span className="text-xs font-black text-gray-700 uppercase tracking-tight">{op.estado || '-'}</span>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <div className="flex items-center justify-center gap-3">
                                        <div className="w-24 h-1.5 bg-gray-100 rounded-full overflow-hidden">
                                            <div
                                                className="h-full bg-blue-500 rounded-full transition-all duration-1000"
                                                style={{ width: `${op.progreso}%` }}
                                            ></div>
                                        </div>
                                        <span className="text-[10px] font-black text-blue-600">{op.progreso}%</span>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-right">
                                    <button className="p-2 bg-gray-50 text-gray-300 rounded-xl group-hover:bg-blue-600 group-hover:text-white transition-all scale-90 group-hover:scale-110">
                                        <ChevronRight className="w-5 h-5" />
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {!loading && !error && filteredOPs.length === 0 && (
                            <tr>
                                <td colSpan="5" className="py-20 text-center text-gray-400 font-bold italic uppercase tracking-widest text-xs">
                                    No se encontraron Ordenes de Producción
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
