import { FileText, ChevronRight } from 'lucide-react';

export default function NVTable({ nvs, onSelectNV }) {
    return (
        <div className="bg-card rounded-2xl shadow-sm border border-border overflow-x-auto animate-in fade-in duration-500">
            <table className="w-full text-left min-w-[800px]">
                <thead className="bg-muted border-b border-border">
                    <tr>
                        <th className="px-6 py-4 text-xs font-bold text-muted-foreground uppercase tracking-wider">ID NV / Cliente</th>
                        <th className="px-6 py-4 text-xs font-bold text-muted-foreground uppercase tracking-wider">Fecha</th>
                        <th className="px-6 py-4 text-xs font-bold text-muted-foreground uppercase tracking-wider">Items Req.</th>
                        <th className="px-6 py-4 text-xs font-bold text-muted-foreground uppercase tracking-wider">Estado</th>
                        <th className="px-6 py-4 text-xs font-bold text-muted-foreground uppercase tracking-wider text-right">Acción</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-border">
                    {nvs.map((nv) => (
                        <tr
                            key={nv.id}
                            className="hover:bg-accent/50 transition-colors group cursor-pointer"
                            onClick={() => onSelectNV(nv)}
                        >
                            <td className="px-6 py-4">
                                <div className="flex items-center">
                                    <div className="bg-brand-indigo/10 p-2 rounded-lg mr-3">
                                        <FileText className="text-brand-indigo w-4 h-4" />
                                    </div>
                                    <div>
                                        <div className="font-bold text-foreground">{nv.id}</div>
                                        <div className="text-xs text-muted-foreground">{nv.cliente}</div>
                                    </div>
                                </div>
                            </td>
                            <td className="px-6 py-4 text-sm text-muted-foreground">{nv.fecha}</td>
                            <td className="px-6 py-4 text-sm text-muted-foreground">{nv.items.length} Items</td>
                            <td className="px-6 py-4">
                                <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase ${nv.estado === 'Pendiente SC' ? 'bg-warning-bg text-warning' : 'bg-accent text-accent-foreground'
                                    }`}>
                                    {nv.estado}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-right">
                                <button className="text-muted-foreground hover:text-brand-indigo transition-colors">
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
