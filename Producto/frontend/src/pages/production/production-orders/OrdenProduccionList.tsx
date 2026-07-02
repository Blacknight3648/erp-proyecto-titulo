import { useState, useMemo, useEffect } from 'react';
import { Search, Filter, ChevronRight, Factory, Activity, Loader2 } from 'lucide-react';
import { OrdenProduccionService } from '../../../remote/service/OrdenProduccionService';

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
            <div className="bg-card p-4 rounded-2xl shadow-sm border border-border flex flex-wrap gap-4 items-center">
                <div className="relative flex-1 min-w-[250px]">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar por OP, NV o Modelo..."
                        className="w-full pl-10 pr-4 py-2 bg-muted border-none rounded-xl text-sm focus:ring-2 focus:ring-primary outline-none"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
                <div className="flex space-x-2">
                    <button className="flex items-center px-4 py-2 bg-muted text-muted-foreground rounded-lg text-[10px] font-black uppercase border border-transparent hover:border-border">
                        <Filter className="w-3 h-3 mr-2" /> Estado
                    </button>
                    <button className="flex items-center px-4 py-2 bg-muted text-muted-foreground rounded-lg text-[10px] font-black uppercase border border-transparent hover:border-border">
                        <Activity className="w-3 h-3 mr-2" /> Prioridad
                    </button>
                </div>
            </div>

            {/* Lista de OPs */}
            <div className="bg-card rounded-[2.5rem] shadow-sm border border-border overflow-hidden">
                <table className="w-full text-left">
                    <thead className="bg-muted border-b border-border">
                        <tr>
                            <th className="px-8 py-5 text-[10px] font-black text-muted-foreground uppercase tracking-widest">ID OP</th>
                            <th className="px-8 py-5 text-[10px] font-black text-muted-foreground uppercase tracking-widest text-center">NV Asociada</th>
                            <th className="px-8 py-5 text-[10px] font-black text-muted-foreground uppercase tracking-widest text-center">Estado</th>
                            <th className="px-8 py-5 text-[10px] font-black text-muted-foreground uppercase tracking-widest text-center">Progreso</th>
                            <th className="px-8 py-5 text-[10px] font-black text-muted-foreground uppercase tracking-widest text-right">Acción</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-border">
                        {loading && (
                            <tr>
                                <td colSpan="5" className="py-20 text-center text-muted-foreground font-bold uppercase tracking-widest text-xs">
                                    <Loader2 className="w-6 h-6 mx-auto mb-2 animate-spin text-primary" />
                                    Cargando Ordenes de Producción...
                                </td>
                            </tr>
                        )}
                        {!loading && error && (
                            <tr>
                                <td colSpan="5" className="py-20 text-center text-destructive font-bold italic uppercase tracking-widest text-xs">
                                    Error al cargar las Ordenes de Producción
                                </td>
                            </tr>
                        )}
                        {!loading && !error && filteredOPs.map((op) => (
                            <tr
                                key={op.idOP}
                                className="hover:bg-primary/5 transition-all group cursor-pointer border-l-4 border-transparent hover:border-primary"
                                onClick={() => onSelect(op.idOP)}
                            >
                                <td className="px-8 py-6">
                                    <div className="flex items-center">
                                        <div className="bg-primary/10 p-2.5 rounded-xl mr-4 group-hover:bg-primary group-hover:text-primary-foreground transition-all">
                                            <Factory className="w-5 h-5 text-primary group-hover:text-primary-foreground" />
                                        </div>
                                        <div>
                                            <div className="font-black text-foreground text-sm tracking-tight italic uppercase">{op.numeroOP || `OP-${op.idOP}`}</div>
                                            <div className="text-[10px] text-muted-foreground font-bold uppercase tracking-wider">{(op.items?.length || 0)} ítem(s)</div>
                                        </div>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <span className="text-[10px] font-black text-brand-indigo bg-brand-indigo/10 px-3 py-1.5 rounded-lg border border-brand-indigo/20 uppercase tracking-widest italic">
                                        {op.notaVentaId ?? '-'}
                                    </span>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <span className="text-xs font-black text-foreground uppercase tracking-tight">{op.estado || '-'}</span>
                                </td>
                                <td className="px-8 py-6 text-center">
                                    <div className="flex items-center justify-center gap-3">
                                        <div className="w-24 h-1.5 bg-muted rounded-full overflow-hidden">
                                            <div
                                                className="h-full bg-primary rounded-full transition-all duration-1000"
                                                style={{ width: `${op.progreso}%` }}
                                            ></div>
                                        </div>
                                        <span className="text-[10px] font-black text-primary">{op.progreso}%</span>
                                    </div>
                                </td>
                                <td className="px-8 py-6 text-right">
                                    <button className="p-2 bg-muted text-muted-foreground rounded-xl group-hover:bg-primary group-hover:text-primary-foreground transition-all scale-90 group-hover:scale-110">
                                        <ChevronRight className="w-5 h-5" />
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {!loading && !error && filteredOPs.length === 0 && (
                            <tr>
                                <td colSpan="5" className="py-20 text-center text-muted-foreground font-bold italic uppercase tracking-widest text-xs">
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
