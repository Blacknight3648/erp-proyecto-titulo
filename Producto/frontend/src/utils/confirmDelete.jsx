import React from 'react';
import { createRoot } from 'react-dom/client';
import { AlertTriangle, Trash2 } from 'lucide-react';

export function confirmDelete(message, onConfirm) {
  const container = document.createElement('div');
  document.body.appendChild(container);

  const root = createRoot(container);

  const cleanup = () => {
    root.unmount();
    if (document.body.contains(container)) {
      document.body.removeChild(container);
    }
  };

  const handleConfirm = () => {
    onConfirm();
    cleanup();
  };

  const Modal = () => (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center bg-slate-900/40 backdrop-blur-md animate-in fade-in duration-300">
      {/* Contenedor del Modal */}
      <div className="bg-white rounded-3xl p-8 max-w-md w-full mx-4 shadow-[0_24px_70px_rgba(0,0,0,0.15)] border border-slate-100 animate-in zoom-in-95 duration-200 relative overflow-hidden">
        
        {/* Detalle decorativo de peligro en el fondo */}
        <div className="absolute -top-16 -right-16 w-40 h-40 bg-gradient-to-br from-rose-500/10 to-orange-500/10 rounded-full blur-2xl pointer-events-none" />

        <div className="flex flex-col items-center text-center relative z-10">
          
          {/* Icono de Alerta Estilizado con micro-animación */}
          <div className="w-16 h-16 bg-rose-50 text-rose-600 rounded-2xl flex items-center justify-center mb-5 border border-rose-100 shadow-sm animate-bounce [animation-iteration-count:2] [animation-duration:1s]">
            <AlertTriangle size={32} strokeWidth={2} />
          </div>

          {/* Textos Informativos */}
          <h2 className="text-2xl font-extrabold text-slate-900 mb-2 tracking-tight">
            ¿Confirmas la eliminación?
          </h2>
          
          <p className="text-slate-600 font-medium mb-4 text-base leading-relaxed px-2">
            {message}
          </p>

          {/* Badge de Advertencia Irreversible */}
          <div className="inline-flex items-center gap-1.5 bg-amber-50 border border-amber-200/60 rounded-xl px-3 py-2 text-xs font-semibold text-amber-800 mb-8 max-w-full">
            <Trash2 size={14} className="shrink-0 text-amber-600" />
            <span className="truncate">Atención: Esta acción no se puede deshacer.</span>
          </div>

          {/* Acciones */}
          <div className="flex flex-col-reverse sm:flex-row gap-3 w-full">
            <button
              onClick={cleanup}
              className="flex-1 py-3.5 px-5 rounded-xl bg-slate-100 text-slate-700 font-bold hover:bg-slate-200/80 hover:text-slate-900 transition-all duration-200 border border-transparent active:scale-[0.98]"
            >
              Cancelar
            </button>
            <button
              onClick={handleConfirm}
              className="flex-1 py-3.5 px-5 rounded-xl bg-gradient-to-r from-rose-500 to-rose-600 text-white font-bold hover:from-rose-600 hover:to-rose-700 hover:shadow-lg hover:shadow-rose-500/20 transition-all duration-200 active:scale-[0.98]"
            >
              Sí, eliminar
            </button>
          </div>

        </div>
      </div>
    </div>
  );

  root.render(<Modal />);
}