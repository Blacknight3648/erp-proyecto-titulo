import React from 'react';
import { Loader2 } from 'lucide-react';

export default function ConfirmModal({ show, onConfirm, onCancel }) {
    if (!show) return null;

    return (
        <div className="fixed inset-0 z-[100] bg-foreground/60 backdrop-blur-md flex items-center justify-center p-6 text-center animate-in fade-in duration-300">
            <div className="bg-card p-10 rounded-[3rem] shadow-2xl max-w-sm w-full animate-in zoom-in-95 duration-300">
                <div className="bg-warning/10 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-6">
                    <Loader2 className="w-8 h-8 text-warning" />
                </div>
                <h3 className="text-xl font-black text-foreground">¿Desea realizar este cambio?</h3>
                <p className="text-muted-foreground text-sm mt-2 mb-8">Ya existe información registrada. El nuevo valor reemplazará al anterior.</p>
                <div className="space-y-3">
                    <button
                        onClick={onConfirm}
                        className="w-full py-4 bg-primary text-primary-foreground font-black rounded-2xl shadow-lg shadow-primary/20 uppercase tracking-widest text-xs"
                    >
                        Sí, Guardar Cambio
                    </button>
                    <button
                        onClick={onCancel}
                        className="w-full py-4 bg-muted text-muted-foreground font-black rounded-2xl uppercase tracking-widest text-xs"
                    >
                        Cancelar
                    </button>
                </div>
            </div>
        </div>
    );
}
