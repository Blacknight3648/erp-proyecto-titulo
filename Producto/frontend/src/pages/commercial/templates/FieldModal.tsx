import React from 'react';
import { X, Check } from 'lucide-react';

export default function FieldModal({ fieldModal, setFieldModal, onConfirm }) {
    if (!fieldModal.open) return null;

    return (
        <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-[100] animate-in fade-in duration-200 p-4">
            <div className="bg-white w-full max-w-md rounded-3xl shadow-2xl border border-gray-100 overflow-hidden animate-in zoom-in-95 slide-in-from-bottom-4 duration-300">
                <div className="p-8">
                    <div className="flex justify-between items-start mb-6">
                        <div>
                            <h3 className="text-xl font-black text-gray-800 uppercase tracking-widest italic">Registrar Campo</h3>
                            <p className="text-[10px] text-gray-400 font-bold uppercase tracking-widest mt-1">Nombre para la biblioteca de plantillas</p>
                        </div>
                        <button 
                            onClick={() => setFieldModal({ ...fieldModal, open: false })} 
                            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
                        >
                            <X className="w-5 h-5 text-gray-400" />
                        </button>
                    </div>

                    <div className="space-y-4">
                        <div className="space-y-2">
                            <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Nombre del Campo</label>
                            <input
                                autoFocus
                                type="text"
                                value={fieldModal.fieldName}
                                onChange={(e) => setFieldModal({ ...fieldModal, fieldName: e.target.value.toUpperCase() })}
                                onKeyDown={(e) => e.key === 'Enter' && onConfirm()}
                                placeholder="Ej: DETALLE DE BORDADO"
                                className="w-full bg-gray-50 border border-gray-100 rounded-2xl p-4 text-xs font-black text-blue-600 outline-none focus:ring-4 focus:ring-blue-50/50 focus:border-blue-400 transition-all uppercase placeholder:text-gray-300"
                            />
                        </div>
                    </div>

                    <div className="flex gap-3 mt-8">
                        <button
                            onClick={() => setFieldModal({ ...fieldModal, open: false })}
                            className="flex-1 px-6 py-4 rounded-2xl text-[10px] font-black uppercase tracking-widest text-gray-400 border border-gray-100 hover:bg-gray-50 transition-all"
                        >
                            Cancelar
                        </button>
                        <button
                            onClick={onConfirm}
                            className="flex-1 px-6 py-4 bg-blue-600 text-white rounded-2xl text-[10px] font-black uppercase tracking-widest shadow-lg shadow-blue-100 hover:bg-blue-700 transition-all flex items-center justify-center gap-2"
                        >
                            <Check className="w-4 h-4" />
                            Confirmar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
