import { useState, useEffect } from 'react';
import { X, Search, Target, ChevronRight } from 'lucide-react';

/** Normaliza el número de EVN a "EVN-####" */
const formatNumeroEVN = (raw) => {
    if (!raw && raw !== 0) return '—';
    const s = String(raw).trim().replace(/^EVN-?/i, '');
    return s ? `EVN-${s}` : '—';
};

/**
 * Modal de creación de Nota de Venta.
 * Toda NV debe originarse desde una Evaluación de Negocio (EVN) ADJUDICADA:
 * el dominio NotaVenta exige un evaluacionNegocioId no nulo, por lo que ya
 * no existe la opción de crear una NV "en blanco".
 *
 * Props:
 *  - open: boolean
 *  - onClose(): cerrar/cancelar
 *  - evaluaciones: EVN aplicables (ya filtradas a ADJUDICADA)
 *  - onSelectEVN(evn): EVN elegida como plantilla
 */
export default function NuevaNVModal({ open, onClose, evaluaciones = [], onSelectEVN }) {
    const [search, setSearch] = useState('');

    // Cada apertura arranca sin búsqueda previa.
    useEffect(() => {
        if (open) {
            setSearch('');
        }
    }, [open]);

    if (!open) return null;

    const q = search.trim().toLowerCase();
    const filtradas = !q
        ? evaluaciones
        : evaluaciones.filter(ev =>
            String(ev.numeroEvn ?? ev.numero ?? '').toLowerCase().includes(q) ||
            String(ev.clienteNombre ?? '').toLowerCase().includes(q) ||
            String(ev.referencia ?? '').toLowerCase().includes(q) ||
            String(ev.evaluacionNegocioId ?? ev.id ?? '').includes(search.trim())
        );

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <div
                className="absolute inset-0 bg-foreground/40 backdrop-blur-sm"
                onClick={onClose}
            />

            <div className="relative bg-card rounded-[2.5rem] shadow-2xl w-full max-w-2xl max-h-[85vh] flex flex-col overflow-hidden animate-in zoom-in-95 fade-in duration-200">

                {/* Header */}
                <div className="flex items-center justify-between p-6 border-b border-border">
                    <div>
                        <h2 className="text-lg font-black text-foreground uppercase tracking-tight">
                            Elegir Evaluación de Negocio
                        </h2>
                        <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest mt-0.5">
                            Selecciona la EVN adjudicada que usarás para crear la NV
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="w-10 h-10 bg-muted rounded-xl flex items-center justify-center hover:bg-muted/70 transition-all"
                        aria-label="Cerrar"
                    >
                        <X className="w-4 h-4 text-muted-foreground" />
                    </button>
                </div>

                <div className="px-6 pt-5 pb-3">
                    <div className="relative">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                        <input
                            autoFocus
                            type="text"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                            placeholder="Buscar por número, cliente o referencia..."
                            className="w-full pl-11 pr-4 py-3 bg-muted border-none rounded-2xl text-sm font-bold focus:ring-2 focus:ring-success outline-none transition-all"
                        />
                    </div>
                </div>

                <div className="px-6 pb-6 overflow-y-auto flex-1 space-y-3">
                    {filtradas.map((ev) => (
                        <button
                            key={ev.evaluacionNegocioId ?? ev.id}
                            onClick={() => onSelectEVN(ev)}
                            className="group w-full text-left p-4 rounded-2xl border-2 border-border hover:border-success hover:bg-success/5 transition-all flex items-center justify-between gap-4"
                        >
                            <div className="min-w-0">
                                <div className="flex items-center gap-2 mb-1">
                                    <span className="text-[10px] font-black text-success bg-success/10 px-2 py-0.5 rounded-full uppercase tracking-widest">
                                        {formatNumeroEVN(ev.numeroEvn ?? ev.numero)}
                                    </span>
                                    <span className="text-[9px] font-black text-muted-foreground uppercase tracking-widest">
                                        {(ev.items?.length || 0)} ítems
                                    </span>
                                </div>
                                <p className="text-sm font-black text-foreground uppercase truncate">{ev.clienteNombre || 'Sin cliente'}</p>
                                <p className="text-[11px] font-bold text-muted-foreground truncate">{ev.referencia || 'Sin referencia'}</p>
                            </div>
                            <ChevronRight className="w-5 h-5 text-muted-foreground/50 group-hover:text-success group-hover:translate-x-1 transition-all flex-shrink-0" />
                        </button>
                    ))}

                    {filtradas.length === 0 && (
                        <div className="py-16 flex flex-col items-center justify-center text-center">
                            <Target className="w-12 h-12 text-muted-foreground/30 mb-4" />
                            <p className="text-sm font-black text-muted-foreground uppercase tracking-widest">
                                {evaluaciones.length === 0
                                    ? 'No hay evaluaciones adjudicadas disponibles'
                                    : 'Sin resultados para la búsqueda'}
                            </p>
                            {evaluaciones.length === 0 && (
                                <p className="text-xs font-bold text-muted-foreground mt-2">
                                    Toda Nota de Venta debe originarse desde una Evaluación de Negocio adjudicada.
                                    Adjudica una EVN para poder crear la NV.
                                </p>
                            )}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
