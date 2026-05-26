import { useState } from 'react';
import {
    Search,
    Filter,
    Link as LinkIcon,
    ArrowRight,
    AlertCircle,
    CheckCircle2,
    Clock,
    MoreHorizontal
} from 'lucide-react';
import { useDataLookup } from '../../../hooks/useDataLookup';
import { mockNVs } from '../../../data/mockData';

export default function TrazabilidadNV() {
    const { getTrazabilidad } = useDataLookup();
    const [searchQuery, setSearchQuery] = useState('');
    const [statusFilter, setStatusFilter] = useState('Todos');

    // Generar datos de trazabilidad para todas las NVs
    const traceabilityData = mockNVs.map(nv => getTrazabilidad(nv.id));

    const filteredData = traceabilityData.filter(item => {
        const matchesSearch =
            (item.nv.numeroNV || item.nv.idNV || item.nv.id || '').toString().toLowerCase().includes(searchQuery.toLowerCase()) ||
            (item.nv.cliente || item.nv.nombreCliente || '').toString().toLowerCase().includes(searchQuery.toLowerCase());

        const matchesFilter = statusFilter === 'Todos' || item.nv.estado === statusFilter; // Simplificado por ahora

        return matchesSearch && matchesFilter;
    });

    return (
        <div className="max-w-7xl mx-auto space-y-8 animate-in fade-in duration-700 pb-12">

            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight flex items-center">
                        <LinkIcon className="w-8 h-8 mr-3 text-blue-600" />
                        Trazabilidad NV
                    </h1>
                    <p className="text-gray-500 font-medium mt-2">
                        Vista unificada del ciclo de vida de cada orden (NV → OP)
                    </p>
                </div>

                {/* Search & Filter */}
                <div className="flex flex-col sm:flex-row gap-4">
                    <div className="relative group">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 group-focus-within:text-blue-600 transition-colors" />
                        <input
                            type="text"
                            placeholder="Buscar por NV, Cliente..."
                            className="pl-12 pr-4 py-3 bg-white border border-gray-200 rounded-xl text-sm font-medium focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none shadow-sm w-full sm:w-64"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <select
                        className="px-4 py-3 bg-white border border-gray-200 rounded-xl text-sm font-medium focus:ring-2 focus:ring-blue-500 outline-none shadow-sm"
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="Todos">Todos los Estados</option>
                        <option value="Pendiente SC">Pendiente SC</option>
                        <option value="En Proceso">En Proceso</option>
                        <option value="Completado">Completado</option>
                    </select>
                </div>
            </div>

            {/* Traceability Table */}
            <div className="bg-white rounded-[2rem] shadow-sm border border-gray-100 overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-gray-50 border-b border-gray-100 text-xs font-black text-gray-400 uppercase tracking-widest">
                                <th className="p-6">Nota Venta (NV)</th>
                                <th className="p-6">Producción (OP)</th>
                                <th className="p-6 text-center">Estado General</th>
                                <th className="p-6"></th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-50 text-sm">
                            {filteredData.map((row, idx) => (
                                <tr key={idx} className="group hover:bg-blue-50/30 transition-colors">
                                    {/* NV Column */}
                                    <td className="p-6 align-top">
                                        <div className="font-bold text-gray-800 flex items-center space-x-2">
                                            <span className="text-blue-600">{row.nv.idNV || row.nv.id}</span>
                                            {row.nv.cliente.includes('VIP') && (
                                                <span className="bg-amber-100 text-amber-700 text-[9px] px-2 py-0.5 rounded-full font-black">VIP</span>
                                            )}
                                        </div>
                                        <div className="text-xs text-gray-500 mt-1 font-medium">{row.nv.cliente}</div>
                                        <div className="text-[10px] text-gray-400 mt-2 flex items-center">
                                            <Clock className="w-3 h-3 mr-1" /> {row.nv.fecha}
                                        </div>
                                    </td>


                                    {/* OP Column */}
                                    <td className="p-6 align-top">
                                        {row.relatedOP && row.relatedOP.length > 0 ? (
                                            <div className="space-y-2">
                                                {row.relatedOP.map(op => (
                                                    <div key={op.id} className="flex items-center space-x-2 bg-amber-50 border border-amber-100 px-3 py-1.5 rounded-lg shadow-sm">
                                                        <div className={`w-2 h-2 rounded-full ${op.estado === 'OP Terminada' ? 'bg-green-500' : op.estado === 'OP En Progreso' ? 'bg-blue-500' : 'bg-amber-500'}`}></div>
                                                        <span className="text-xs font-bold text-amber-700">{op.id}</span>
                                                    </div>
                                                ))}
                                            </div>
                                        ) : (
                                            <span className="text-gray-400 text-xs">-</span>
                                        )}
                                    </td>


                                    {/* Status Column */}
                                    <td className="p-6 align-top text-center">
                                        <div className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-bold border ${
                                            // Priority 1: OP Status
                                            row.relatedOP && row.relatedOP.length > 0
                                                ? row.relatedOP[0].estado === 'OP Terminada'
                                                    ? 'bg-green-50 text-green-700 border-green-100' // OP Terminada
                                                    : row.relatedOP[0].estado === 'OP En Progreso'
                                                        ? 'bg-blue-50 text-blue-700 border-blue-100' // OP En Progreso
                                                        : 'bg-amber-50 text-amber-700 border-amber-100' // OP Pendiente
                                                : row.nv.estado === 'Completado'
                                                    ? 'bg-green-50 text-green-700 border-green-100'
                                                    : row.nv.estado === 'Pendiente SC'
                                                        ? 'bg-red-50 text-red-700 border-red-100'
                                                        : 'bg-gray-50 text-gray-700 border-gray-100'
                                            }`}>
                                            {/* Display Logic */}
                                            {row.relatedOP && row.relatedOP.length > 0
                                                ? row.relatedOP[0].estado
                                                : row.nv.estado
                                            }
                                        </div>
                                    </td>

                                    <td className="p-6 align-top text-right">
                                        <button className="text-gray-400 hover:text-blue-600 transition-colors">
                                            <MoreHorizontal className="w-5 h-5" />
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    {filteredData.length === 0 && (
                        <div className="p-12 text-center">
                            <AlertCircle className="w-12 h-12 text-gray-300 mx-auto mb-4" />
                            <h3 className="text-gray-400 font-bold uppercase tracking-widest text-sm">No se encontraron registros</h3>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
