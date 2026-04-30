import React from 'react';
import { Loader2 } from 'lucide-react';

export default function ConfirmModal({ show, onConfirm, onCancel }) {
    if (!show) return null;

    return (
        <div className="fixed inset-0 z-[100] bg-slate-900/40 backdrop-blur-md flex items-center justify-center p-6 text-center animate-in fade-in duration-300">
            <div className="bg-white p-10 rounded-[3rem] shadow-2xl max-w-sm w-full animate-in zoom-in-95 duration-300">
                <div className="bg-amber-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-6">
                    <Loader2 className="w-8 h-8 text-amber-600" />
                </div>
                <h3 className="text-xl font-black text-gray-800">¿Desea realizar este cambio?</h3>
                <p className="text-gray-500 text-sm mt-2 mb-8">Ya existe información registrada. El nuevo valor reemplazará al anterior.</p>
                <div className="space-y-3">
                    <button
                        onClick={onConfirm}
                        className="w-full py-4 bg-blue-600 text-white font-black rounded-2xl shadow-lg shadow-blue-100 uppercase tracking-widest text-xs"
                    >
                        Sí, Guardar Cambio
                    </button>
                    <button
                        onClick={onCancel}
                        className="w-full py-4 bg-gray-100 text-gray-500 font-black rounded-2xl uppercase tracking-widest text-xs"
                    >
                        Cancelar
                    </button>
                </div>
            </div>
        </div>
    );
}
