import React from 'react';
import { ClipboardList, CheckCircle2 } from 'lucide-react';

export default function BulkEditOP({
    editingFieldIdx,
    opFields,
    selectedOPIds,
    setTempValue,
    finalizeSave,
    setView,
    setIsSelectionMode,
    setSelectedOPIds,
    setShowSelectionModal,
    validateNumericInput,
    toast,
    tempValue
}) {
    if (editingFieldIdx === null) return null;
    const field = opFields[editingFieldIdx];

    return (
        <div className="max-w-2xl mx-auto bg-muted/50 min-h-[calc(100vh-120px)] p-4 pb-24 animate-in slide-in-from-bottom-8 duration-700">
            <div className="bg-card p-8 rounded-[2.5rem] shadow-sm border border-border">
                <div className="text-center mb-10">
                    <div className="relative inline-block group">
                        <div className="inline-flex items-center px-4 py-2 bg-primary/10 text-primary rounded-full text-[10px] font-black uppercase tracking-widest border border-primary/20 mb-4 shadow-sm cursor-help transition-all">
                            <ClipboardList className="w-3 h-3 mr-2" />
                            Modificando {selectedOPIds.length} OPs simultáneamente
                        </div>

                        <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 hidden group-hover:block w-48 z-50 animate-in fade-in slide-in-from-bottom-2 duration-300">
                            <div className="bg-sidebar/95 backdrop-blur-md text-white p-4 rounded-2xl shadow-2xl border border-sidebar-border relative">
                                <div className="text-[9px] font-black text-sidebar-primary uppercase tracking-widest mb-2 border-b border-sidebar-border pb-1">Lista de OPs</div>
                                <div className="space-y-1.5 max-h-32 overflow-y-auto px-1 custom-scrollbar">
                                    {selectedOPIds.map((id, idx) => (
                                        <div key={idx} className="flex items-center justify-between text-[11px] font-bold tracking-tight">
                                            <span className="text-sidebar-foreground">#{idx + 1}</span>
                                            <span className="text-white">{String(id || '').replace('OP-2024-', 'OP ')}</span>
                                        </div>
                                    ))}
                                </div>
                                <div className="absolute top-full left-1/2 -translate-x-1/2 w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-sidebar"></div>
                            </div>
                        </div>
                    </div>
                    <h2 className="text-2xl font-black text-foreground tracking-tight uppercase italic">Editar: <span className="text-primary underline decoration-4 decoration-primary/20 underline-offset-4">{field.title}</span></h2>
                </div>

                <div className="bg-muted/50 p-8 rounded-[2rem] border-2 border-dashed border-border mb-10">
                    <div className="space-y-6">
                        {field.type === 'select' ? (
                            <select
                                className="w-full p-6 bg-card border-2 border-border rounded-3xl font-black text-sm text-foreground focus:ring-8 focus:ring-primary/10 outline-none appearance-none uppercase shadow-sm"
                                onChange={(e) => setTempValue(e.target.value)}
                            >
                                <option value="">Seleccione el nuevo valor...</option>
                                {field.options.map((opt, idx) => (
                                    <option key={idx} value={opt}>{opt.toUpperCase()}</option>
                                ))}
                            </select>
                        ) : field.type === 'textarea' ? (
                            <textarea
                                rows="6"
                                placeholder="Escriba la observación para todas las OPs..."
                                className="w-full p-6 bg-card border-2 border-border rounded-3xl font-bold text-sm text-foreground focus:ring-8 focus:ring-primary/10 outline-none uppercase resize-none shadow-sm"
                                onChange={(e) => setTempValue(e.target.value)}
                            />
                        ) : (
                            <input
                                type={field.type === 'calculated_number' ? 'number' : 'date'}
                                className="w-full p-6 bg-card border-2 border-border rounded-3xl font-black text-sm text-foreground focus:ring-8 focus:ring-primary/10 outline-none uppercase shadow-sm"
                                onChange={(e) => {
                                    if (field.type === 'calculated_number') {
                                        const error = validateNumericInput(e.target.value, field.title);
                                        if (error) { toast.error(error); return; }
                                    }
                                    setTempValue(e.target.value);
                                }}
                            />
                        )}
                    </div>
                </div>

                <div className="grid grid-cols-1 gap-4">
                    <button
                        onClick={finalizeSave}
                        className="w-full py-6 bg-primary text-primary-foreground font-black rounded-[1.5rem] shadow-xl shadow-primary/20 uppercase tracking-widest text-sm hover:translate-y-[-2px] active:translate-y-0 transition-all border-b-4 border-primary-hover flex items-center justify-center"
                    >
                        <CheckCircle2 className="w-5 h-5 mr-3" /> GUARDAR CAMBIOS MASIVOS
                    </button>

                    <div className="grid grid-cols-2 gap-4 mt-2">
                        <button
                            onClick={() => { setView('list'); setIsSelectionMode(false); setSelectedOPIds([]); }}
                            className="w-full py-5 bg-card border-2 border-border text-muted-foreground font-black rounded-[1.5rem] uppercase tracking-widest text-[10px] hover:bg-muted transition-colors"
                        >
                            CANCELAR TODO
                        </button>
                        <button
                            onClick={() => { setView('list'); setShowSelectionModal(true); }}
                            className="w-full py-5 bg-muted text-muted-foreground font-black rounded-[1.5rem] uppercase tracking-widest text-[10px] hover:bg-muted/70 transition-colors"
                        >
                            VOLVER ATRÁS
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
