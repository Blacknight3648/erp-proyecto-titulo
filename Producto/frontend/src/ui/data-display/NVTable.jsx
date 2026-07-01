import { FileText, ChevronRight } from 'lucide-react';

export default function NVTable({ nvs, onSelectNV }) {
    return (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-x-auto animate-in fade-in duration-500">
            <table className="w-full text-left min-w-[800px]">
                <thead className="bg-gray-50 border-b border-gray-100">
                    <tr>
                        <th className="px-6 py-4 text-xs font-bold text-gray-400 uppercase tracking-wider">ID NV / Cliente</th>
                        <th className="px-6 py-4 text-xs font-bold text-gray-400 uppercase tracking-wider">Fecha</th>
                        <th className="px-6 py-4 text-xs font-bold text-gray-400 uppercase tracking-wider">Items Req.</th>
                        <th className="px-6 py-4 text-xs font-bold text-gray-400 uppercase tracking-wider">Estado</th>
                        <th className="px-6 py-4 text-xs font-bold text-gray-400 uppercase tracking-wider text-right">Acción</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                    {nvs.map((nv) => (
                        <tr
                            key={nv.id}
                            className="hover:bg-indigo-50/30 transition-colors group cursor-pointer"
                            onClick={() => onSelectNV(nv)}
                        >
                            <td className="px-6 py-4">
                                <div className="flex items-center">
                                    <div className="bg-indigo-100 p-2 rounded-lg mr-3">
                                        <FileText className="text-indigo-600 w-4 h-4" />
                                    </div>
                                    <div>
                                        <div className="font-bold text-gray-900">{nv.id}</div>
                                        <div className="text-xs text-gray-500">{nv.cliente}</div>
                                    </div>
                                </div>
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-600">{nv.fecha}</td>
                            <td className="px-6 py-4 text-sm text-gray-600">{nv.items.length} Items</td>
                            <td className="px-6 py-4">
                                <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase ${nv.estado === 'Pendiente SC' ? 'bg-amber-100 text-amber-700' : 'bg-blue-100 text-blue-700'
                                    }`}>
                                    {nv.estado}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-right">
                                <button className="text-gray-400 hover:text-indigo-600 transition-colors">
                                    <ChevronRight className="w-5 h-5" />
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
