import React from 'react';
import { CheckCircle2 } from 'lucide-react';

export default function SelectionModal({ 
    show, 
    onClose, 
    opFields, 
    selectedOPIds, 
    mockOpDetails, 
    onSelectField 
}) {
    if (!show) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in duration-300">
            <div className="bg-white w-full max-w-lg rounded-[2.5rem] p-8 shadow-2xl animate-in zoom-in-95 duration-300 scale-100 overflow-hidden flex flex-col max-h-[90vh]">
                <div className="text-center mb-6">
                    <h2 className="text-2xl font-black text-gray-800 tracking-tight">Seleccionar Campo Masivo</h2>
                    <p className="text-gray-400 text-sm font-medium mt-1">¿Qué información deseas modificar de las {selectedOPIds.length} OPs seleccionadas?</p>
                </div>

                <div className="grid grid-cols-2 gap-3 overflow-y-auto pr-2 custom-scrollbar flex-1 mb-8">
                    {opFields.map((field, idx) => {
                        // Check if all selected OPs have data for this field
                        const isCompleteForAll = selectedOPIds.length > 0 &&
                            selectedOPIds.every(id => mockOpDetails[id]?.[field.key]);

                        return (
                            <button
                                key={idx}
                                onClick={() => onSelectField(idx)}
                                className={`relative p-4 border-2 rounded-2xl text-[11px] font-black transition-all text-center leading-tight tracking-tight shadow-sm flex items-center justify-center min-h-[64px] ${isCompleteForAll
                                    ? 'bg-green-50 border-green-200 text-green-700 hover:border-green-500 hover:bg-green-100'
                                    : 'bg-gray-50 border-gray-100 text-gray-600 hover:border-blue-500 hover:bg-blue-50'
                                    }`}
                            >
                                <span className="relative z-10">{field.title}</span>
                                {isCompleteForAll && (
                                    <div className="absolute top-2 right-2">
                                        <CheckCircle2 className="w-3.5 h-3.5 text-green-600" />
                                    </div>
                                )}
                            </button>
                        );
                    })}
                </div>

                <button
                    onClick={onClose}
                    className="w-full py-4 bg-gray-100 text-gray-500 font-black rounded-[1.5rem] text-xs uppercase tracking-widest hover:bg-gray-200 transition-colors"
                >
                    Cancelar
                </button>
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
