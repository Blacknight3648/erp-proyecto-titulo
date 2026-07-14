import { useState, useMemo } from 'react';
import { X, Calculator, ArrowRight, Search, Link2Off } from 'lucide-react';

/**
 * Modal para vincular un costeo (disponible, no vinculado a ninguna EVN) a un ítem tipo OP.
 */
export default function CosteoSelectionModal({
    open,
    costeos = [],
    loading = false,
    currentCosteoId = null,
    onSelect,
    onClose
}) {
    const [query, setQuery] = useState('');

    const filtered = useMemo(() => {
        const q = query.trim().toLowerCase();
        if (!q) return costeos;
        return costeos.filter(c =>
            String(c.numeroCosteo || '').toLowerCase().includes(q) ||
            String(c.idCosteo || '').includes(q)
        );
    }, [costeos, query]);

    if (!open) return null;

    return (
        <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-foreground/60 backdrop-blur-sm">
            <div className="bg-card w-full max-w-lg rounded-[2.5rem] shadow-2xl overflow-hidden border border-border flex flex-col animate-in fade-in zoom-in duration-300">
                <div className="p-8 border-b border-border flex justify-between items-center bg-muted/50">
                    <div>
                        <h3 className="text-xl font-black text-foreground tracking-tight">Vincular Costeo</h3>
                        <p className="text-xs font-bold text-muted-foreground uppercase tracking-widest mt-1">
                            Disponibles (no vinculados a otra EVN)
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-3 hover:bg-card rounded-2xl transition-all shadow-sm group"
                    >
                        <X className="w-5 h-5 text-muted-foreground group-hover:text-destructive" />
                    </button>
                </div>

                <div className="px-8 pt-6">
                    <div className="relative">
                        <Search className="w-4 h-4 text-muted-foreground absolute left-4 top-1/2 -translate-y-1/2" />
                        <input
                            type="text"
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                            placeholder="Buscar por número de costeo…"
                            className="w-full pl-11 pr-4 py-3 bg-muted border border-border rounded-2xl text-sm font-bold outline-none focus:ring-2 focus:ring-brand-indigo/20 transition-all"
                        />
                    </div>
                </div>

                <div className="p-8 pt-4 space-y-3 max-h-[55vh] overflow-y-auto">
                    {loading ? (
                        <p className="text-sm text-muted-foreground font-bold text-center py-8">Cargando costeos…</p>
                    ) : filtered.length === 0 ? (
                        <p className="text-sm text-muted-foreground font-bold text-center py-8">
                            No hay costeos disponibles para vincular.
                        </p>
                    ) : (
                        <div className="grid gap-3">
                            {filtered.map((c) => (
                                <div
                                    key={c.idCosteo}
                                    onClick={() => onSelect(c.idCosteo, c.numeroCosteo)}
                                    className="group p-5 rounded-3xl border-2 border-border hover:border-brand-indigo hover:bg-brand-indigo/5 transition-all cursor-pointer flex items-center justify-between"
                                >
                                    <div className="flex items-center space-x-4">
                                        <div className="w-12 h-12 bg-brand-indigo/10 rounded-2xl flex items-center justify-center group-hover:scale-110 transition-transform">
                                            <Calculator className="w-6 h-6 text-brand-indigo" />
                                        </div>
                                        <div>
                                            <p className="font-black text-foreground">{c.numeroCosteo || `Costeo #${c.idCosteo}`}</p>
                                            <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">
                                                P. Venta sugerido: ${((c.precioVentaSugerido || 0)).toLocaleString('es-CL')}
                                            </p>
                                        </div>
                                    </div>
                                    <div className="w-10 h-10 bg-muted rounded-xl flex items-center justify-center group-hover:bg-brand-indigo group-hover:text-white transition-colors">
                                        <ArrowRight className="w-4 h-4" />
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {currentCosteoId && (
                    <div className="p-8 bg-muted/50 border-t border-border">
                        <button
                            onClick={() => onSelect(null, null)}
                            className="w-full py-4 text-xs font-black text-muted-foreground uppercase tracking-[0.2em] hover:text-destructive transition-colors flex items-center justify-center gap-2"
                        >
                            <Link2Off className="w-4 h-4" />
                            Desvincular costeo actual (#{currentCosteoId})
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}
