import { useState, useEffect } from 'react';
import { X, CheckCircle2, ShieldAlert } from 'lucide-react';

/**
 * Modal de firma para Adjudicar / Aprobar / Rechazar una EVN.
 * Captura el usuario aprobador y una observación, que viajan al backend
 * y quedan registrados en el historial de estado.
 */
export default function FirmaAprobacionModal({
    open,
    title = 'Firma del Aprobador',
    description,
    accion = 'Confirmar',
    accentColor = 'emerald',
    requireObservacion = false,
    defaultAprobador = '',
    onConfirm,
    onClose,
    isSubmitting = false
}) {
    const [aprobador, setAprobador] = useState(defaultAprobador);
    const [observacion, setObservacion] = useState('');

    useEffect(() => {
        if (open) {
            setAprobador(defaultAprobador);
            setObservacion('');
        }
    }, [open, defaultAprobador]);

    if (!open) return null;

    const canSubmit =
        aprobador.trim().length > 0 &&
        (!requireObservacion || observacion.trim().length > 0) &&
        !isSubmitting;

    const colorMap = {
        emerald: { bg: 'bg-success', hover: 'hover:bg-success/90', ring: 'focus:ring-success', text: 'text-success' },
        red: { bg: 'bg-destructive', hover: 'hover:bg-destructive/90', ring: 'focus:ring-destructive', text: 'text-destructive' },
        indigo: { bg: 'bg-brand-indigo', hover: 'hover:bg-brand-indigo/90', ring: 'focus:ring-brand-indigo', text: 'text-brand-indigo' },
        slate: { bg: 'bg-foreground', hover: 'hover:bg-foreground/90', ring: 'focus:ring-border-strong', text: 'text-foreground' }
    };
    const c = colorMap[accentColor] || colorMap.emerald;

    return (
        <div className="fixed inset-0 z-[120] flex items-center justify-center p-4 bg-foreground/60 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-card w-full max-w-md rounded-3xl shadow-2xl border border-border overflow-hidden animate-in zoom-in-95 slide-in-from-bottom-4 duration-300">
                <div className="p-7 border-b border-border flex items-start justify-between">
                    <div className="flex items-start gap-3">
                        <div className={`w-10 h-10 ${c.bg} rounded-2xl flex items-center justify-center shadow-md`}>
                            <ShieldAlert className="w-5 h-5 text-white" />
                        </div>
                        <div>
                            <h3 className="text-lg font-black text-foreground uppercase tracking-tight">{title}</h3>
                            {description && (
                                <p className="text-[11px] font-bold text-muted-foreground uppercase tracking-widest mt-1">{description}</p>
                            )}
                        </div>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 hover:bg-muted rounded-full transition-colors"
                        disabled={isSubmitting}
                    >
                        <X className="w-5 h-5 text-muted-foreground" />
                    </button>
                </div>

                <div className="p-7 space-y-5">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">
                            Usuario que firma <span className={c.text}>*</span>
                        </label>
                        <input
                            autoFocus
                            type="text"
                            value={aprobador}
                            onChange={(e) => setAprobador(e.target.value)}
                            placeholder="Nombre / código del aprobador"
                            className={`w-full bg-muted border border-border rounded-2xl p-3.5 text-xs font-bold text-foreground outline-none focus:ring-2 ${c.ring} focus:border-border-strong transition-all`}
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">
                            {requireObservacion ? 'Motivo' : 'Observación'} {requireObservacion && <span className={c.text}>*</span>}
                        </label>
                        <textarea
                            rows={3}
                            value={observacion}
                            onChange={(e) => setObservacion(e.target.value)}
                            placeholder={requireObservacion ? 'Indique el motivo...' : 'Opcional: notas para el historial'}
                            className={`w-full bg-muted border border-border rounded-2xl p-3.5 text-xs font-bold text-foreground outline-none focus:ring-2 ${c.ring} focus:border-border-strong transition-all resize-none`}
                        />
                    </div>
                </div>

                <div className="px-7 pb-7 flex gap-3">
                    <button
                        onClick={onClose}
                        disabled={isSubmitting}
                        className="flex-1 px-5 py-3.5 rounded-2xl text-[10px] font-black uppercase tracking-widest text-muted-foreground border border-border hover:bg-muted transition-all disabled:opacity-50"
                    >
                        Cancelar
                    </button>
                    <button
                        onClick={() => onConfirm({ aprobador: aprobador.trim(), observacion: observacion.trim() })}
                        disabled={!canSubmit}
                        className={`flex-1 px-5 py-3.5 ${c.bg} ${c.hover} text-white rounded-2xl text-[10px] font-black uppercase tracking-widest shadow-lg transition-all disabled:opacity-40 disabled:cursor-not-allowed flex items-center justify-center gap-2`}
                    >
                        <CheckCircle2 className="w-4 h-4" />
                        {isSubmitting ? 'Procesando...' : accion}
                    </button>
                </div>
            </div>
        </div>
    );
}
