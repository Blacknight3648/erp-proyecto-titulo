import { useState } from 'react';
import {
    History,
    Search,
    Filter,
    ChevronRight,
    Calendar,
    User,
    CheckCircle,
    FileText,
    ArrowLeft
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { mockNVs } from '../../data/mockData';

export default function HistorialNV() {
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');

    // Filter only NVs with status 'Entregado'
    const historicalNVs = mockNVs.filter(nv => nv.estado === 'Entregado');

    const filteredNVs = historicalNVs.filter(nv => {
        const nvId = (nv.numeroNV || nv.idNV || nv.id || '').toString().toLowerCase();
        const clientName = (nv.cliente || nv.nombreCliente || '').toString().toLowerCase();
        const search = searchQuery.toLowerCase();
        return nvId.includes(search) || clientName.includes(search);
    });

    return (
        <div className="max-w-7xl mx-auto space-y-8 animate-in fade-in duration-700 pb-12">
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div className="flex items-center space-x-4">
                    <button
                        onClick={() => navigate(-1)}
                        className="p-2 hover:bg-gray-100 rounded-full transition-colors"
                    >
                        <ArrowLeft className="w-5 h-5 text-gray-500" />
                    </button>
                    <div>
                        <h1 className="text-3xl font-black text-gray-800 tracking-tight flex items-center">
                            <History className="w-8 h-8 mr-3 text-blue-600" />
                            Historial de Notas de Venta
                        </h1>
                        <p className="text-gray-500 font-medium mt-1">
                            Listado de pedidos cerrados y entregados satisfactoriamente
                        </p>
                    </div>
                </div>

                <div className="relative group min-w-[300px]">
                    <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 group-focus-within:text-blue-600 transition-colors" />
                    <input
                        type="text"
                        placeholder="Buscar por NV, Cliente..."
                        className="w-full pl-12 pr-4 py-3 bg-white border border-gray-200 rounded-2xl text-sm font-medium focus:ring-4 focus:ring-blue-50 focus:border-blue-500 outline-none shadow-sm transition-all"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>
            </div>

            {/* Quick Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded-[2rem] border border-gray-100 shadow-sm flex items-center space-x-4">
                    <div className="w-12 h-12 bg-green-50 rounded-2xl flex items-center justify-center">
                        <CheckCircle className="w-6 h-6 text-green-600" />
                    </div>
                    <div>
                        <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Total Entregados</p>
                        <p className="text-2xl font-black text-gray-800 tracking-tight">{historicalNVs.length} <span className="text-xs font-bold text-gray-400 font-mono">NVs</span></p>
                    </div>
                </div>
                {/* More placeholder stats could go here */}
            </div>

            {/* Table */}
            <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-gray-50/50 border-b border-gray-100 text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] italic">
                                <th className="p-8">Identificación Nota de Venta</th>
                                <th className="p-8">Cliente / Vendedor</th>
                                <th className="p-8">Fecha Entrega</th>
                                <th className="p-8 text-right">Total Neto</th>
                                <th className="p-8"></th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-50">
                            {filteredNVs.map((nv) => {
                                const currentId = nv.numeroNV || nv.idNV || nv.id;
                                return (
                                    <tr
                                        key={currentId}
                                        onClick={() => navigate('/detalle-nv', { state: { selectedNV: currentId } })}
                                        className="group hover:bg-blue-50/20 transition-all cursor-pointer"
                                    >
                                        <td className="p-8">
                                            <div className="flex items-center space-x-4">
                                                <div className="w-10 h-10 bg-gray-100 rounded-xl flex items-center justify-center text-gray-500 group-hover:bg-blue-600 group-hover:text-white transition-colors">
                                                    <FileText className="w-5 h-5" />
                                                </div>
                                                <div>
                                                    <div className="text-sm font-black text-gray-800 tracking-tight">{nv.numeroNV || nv.idNV || nv.id}</div>
                                                    <div className="text-[9px] font-black text-green-600 bg-green-50 px-2 py-0.5 rounded-full inline-block mt-1 uppercase tracking-widest">Entregado</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="p-8">
                                            <div className="flex items-center space-x-3">
                                                <User className="w-4 h-4 text-gray-300" />
                                                <div>
                                                    <div className="text-sm font-bold text-gray-700">{nv.cliente}</div>
                                                    <div className="text-[10px] text-gray-400 font-medium">{nv.vendedor}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="p-8">
                                            <div className="flex items-center space-x-2 text-gray-500">
                                                <Calendar className="w-4 h-4 text-gray-300" />
                                                <span className="text-xs font-bold">{nv.fecha}</span>
                                            </div>
                                        </td>
                                        <td className="p-8 text-right">
                                            <div className="text-sm font-black text-gray-900 font-mono">
                                                ${(nv.total ?? 0).toLocaleString('es-CL')}
                                            </div>
                                        </td>
                                        <td className="p-8 text-right">
                                            <button className="p-2 hover:bg-gray-100 rounded-full transition-colors text-gray-300 group-hover:text-blue-600">
                                                <ChevronRight className="w-5 h-5" />
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })}
                            {filteredNVs.length === 0 && (
                                <tr>
                                    <td colSpan="5" className="p-20 text-center">
                                        <History className="w-12 h-12 text-gray-200 mx-auto mb-4" />
                                        <p className="text-xs font-black text-gray-300 uppercase tracking-widest">No se encontraron registros en el historial</p>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
