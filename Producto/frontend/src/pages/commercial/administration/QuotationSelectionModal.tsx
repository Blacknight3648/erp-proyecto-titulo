import { X, Calculator, ArrowRight } from 'lucide-react';

export default function QuotationSelectionModal({
    open,
    pendingSCOS,
    availableQuotations,
    onApply,
    onClose
}) {
    if (!open) return null;

    return (
        <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-foreground/60 backdrop-blur-sm">
            <div className="bg-card w-full max-w-lg rounded-[2.5rem] shadow-2xl overflow-hidden border border-border flex flex-col animate-in fade-in zoom-in duration-300">
                <div className="p-8 border-b border-border flex justify-between items-center bg-muted/50">
                    <div>
                        <h3 className="text-xl font-black text-foreground tracking-tight">Seleccionar Cotización</h3>
                        <p className="text-xs font-bold text-muted-foreground uppercase tracking-widest mt-1">
                            SCOS: {pendingSCOS?.numero}
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-3 hover:bg-card rounded-2xl transition-all shadow-sm group"
                    >
                        <X className="w-5 h-5 text-muted-foreground group-hover:text-destructive" />
                    </button>
                </div>

                <div className="p-8 space-y-4 max-h-[60vh] overflow-y-auto">
                    <p className="text-sm text-muted-foreground font-medium leading-relaxed">
                        Se han encontrado múltiples valoraciones para esta receta técnica. Seleccione
                        la cotización que desea aplicar.
                    </p>

                    <div className="grid gap-3">
                        {availableQuotations.map((quote) => (
                            <div
                                key={quote.idCosteo}
                                onClick={() => onApply(pendingSCOS, quote)}
                                className="group p-5 rounded-3xl border-2 border-border hover:border-brand-indigo hover:bg-brand-indigo/5 transition-all cursor-pointer flex items-center justify-between"
                            >
                                <div className="flex items-center space-x-4">
                                    <div className="w-12 h-12 bg-brand-indigo/10 rounded-2xl flex items-center justify-center group-hover:scale-110 transition-transform">
                                        <Calculator className="w-6 h-6 text-brand-indigo" />
                                    </div>
                                    <div>
                                        <p className="font-black text-foreground">{quote.numeroCosteo}</p>
                                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">
                                            Total: ${((quote.precioVentaSugerido || 0)).toLocaleString('es-CL')}
                                        </p>
                                    </div>
                                </div>
                                <div className="w-10 h-10 bg-muted rounded-xl flex items-center justify-center group-hover:bg-brand-indigo group-hover:text-white transition-colors">
                                    <ArrowRight className="w-4 h-4" />
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="p-8 bg-muted/50 border-t border-border">
                    <button
                        onClick={() => onApply(pendingSCOS, null)}
                        className="w-full py-4 text-xs font-black text-muted-foreground uppercase tracking-[0.2em] hover:text-brand-indigo transition-colors"
                    >
                        Usar datos base (Sin cotización específica)
                    </button>
                </div>
            </div>
        </div>
    );
}
