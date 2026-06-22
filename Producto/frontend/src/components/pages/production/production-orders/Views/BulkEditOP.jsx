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
        <div className="max-w-2xl mx-auto bg-gray-50/50 min-h-[calc(100vh-120px)] p-4 pb-24 animate-in slide-in-from-bottom-8 duration-700">
            <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100">
                <div className="text-center mb-10">
                    <div className="relative inline-block group">
                        <div className="inline-flex items-center px-4 py-2 bg-blue-50 text-blue-700 rounded-full text-[10px] font-black uppercase tracking-widest border border-blue-100 mb-4 shadow-sm cursor-help transition-all">
                            <ClipboardList className="w-3 h-3 mr-2" />
                            Modificando {selectedOPIds.length} OPs simultáneamente
                        </div>

                        <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 hidden group-hover:block w-48 z-50 animate-in fade-in slide-in-from-bottom-2 duration-300">
                            <div className="bg-slate-900/95 backdrop-blur-md text-white p-4 rounded-2xl shadow-2xl border border-slate-700/50 relative">
                                <div className="text-[9px] font-black text-blue-400 uppercase tracking-widest mb-2 border-b border-white/10 pb-1">Lista de OPs</div>
                                <div className="space-y-1.5 max-h-32 overflow-y-auto px-1 custom-scrollbar">
                                    {selectedOPIds.map((id, idx) => (
                                        <div key={idx} className="flex items-center justify-between text-[11px] font-bold tracking-tight">
                                            <span className="text-gray-300">#{idx + 1}</span>
                                            <span className="text-white">{String(id || '').replace('OP-2024-', 'OP ')}</span>
                                        </div>
                                    ))}
                                </div>
                                <div className="absolute top-full left-1/2 -translate-x-1/2 w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-slate-900/95"></div>
                            </div>
                        </div>
                    </div>
                    <h2 className="text-2xl font-black text-gray-800 tracking-tight uppercase italic">Editar: <span className="text-blue-600 underline decoration-4 decoration-blue-100 underline-offset-4">{field.title}</span></h2>
                </div>

                <div className="bg-gray-50/50 p-8 rounded-[2rem] border-2 border-dashed border-gray-100 mb-10">
                    <div className="space-y-6">
                        {field.type === 'select' ? (
                            <select
                                className="w-full p-6 bg-white border-2 border-gray-100 rounded-3xl font-black text-sm text-gray-700 focus:ring-8 focus:ring-blue-50 outline-none appearance-none uppercase shadow-sm"
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
                                className="w-full p-6 bg-white border-2 border-gray-100 rounded-3xl font-bold text-sm text-gray-700 focus:ring-8 focus:ring-blue-50 outline-none uppercase resize-none shadow-sm"
                                onChange={(e) => setTempValue(e.target.value)}
                            />
                        ) : (
                            <input
                                type={field.type === 'calculated_number' ? 'number' : 'date'}
                                className="w-full p-6 bg-white border-2 border-gray-100 rounded-3xl font-black text-sm text-gray-700 focus:ring-8 focus:ring-blue-50 outline-none uppercase shadow-sm"
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
                        className="w-full py-6 bg-blue-600 text-white font-black rounded-[1.5rem] shadow-xl shadow-blue-100 uppercase tracking-widest text-sm hover:translate-y-[-2px] active:translate-y-0 transition-all border-b-4 border-blue-700 flex items-center justify-center"
                    >
                        <CheckCircle2 className="w-5 h-5 mr-3" /> GUARDAR CAMBIOS MASIVOS
                    </button>

                    <div className="grid grid-cols-2 gap-4 mt-2">
                        <button
                            onClick={() => { setView('list'); setIsSelectionMode(false); setSelectedOPIds([]); }}
                            className="w-full py-5 bg-white border-2 border-gray-100 text-gray-400 font-black rounded-[1.5rem] uppercase tracking-widest text-[10px] hover:bg-gray-50 transition-colors"
                        >
                            CANCELAR TODO
                        </button>
                        <button
                            onClick={() => { setView('list'); setShowSelectionModal(true); }}
                            className="w-full py-5 bg-gray-100 text-gray-600 font-black rounded-[1.5rem] uppercase tracking-widest text-[10px] hover:bg-gray-200 transition-colors"
                        >
                            VOLVER ATRÁS
                        </button>
                    </div>
                </div>
            </div>
            <style>{`
                .custom-scrollbar::-webkit-scrollbar {
                    width: 4px;
                }
                .custom-scrollbar::-webkit-scrollbar-track {
                    background: transparent;
                }
                .custom-scrollbar::-webkit-scrollbar-thumb {
                    background: #e2e8f0;
                    border-radius: 10px;
                }
            `}</style>
        </div>
    );
}
