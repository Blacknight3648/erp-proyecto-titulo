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
    <div
      className="fixed inset-0 z-[9999] flex items-center justify-center bg-slate-900/50 backdrop-blur-sm"
      style={{ fontFamily: "'Outfit', 'Inter', ui-sans-serif, sans-serif" }}
    >
      <div className="bg-white rounded-3xl p-8 max-w-md w-full mx-4 shadow-[0_32px_80px_rgba(0,0,0,0.18)] border border-slate-100 relative overflow-hidden">

        {/* Acento decorativo */}
        <div className="absolute -top-20 -right-20 w-52 h-52 bg-rose-500/8 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute -bottom-10 -left-10 w-36 h-36 bg-orange-400/6 rounded-full blur-2xl pointer-events-none" />

        <div className="flex flex-col items-center text-center relative z-10">

          {/* Icono */}
          <div className="w-16 h-16 bg-rose-50 rounded-2xl flex items-center justify-center mb-6 border border-rose-100 shadow-sm">
            <AlertTriangle size={30} strokeWidth={2} className="text-rose-500" />
          </div>

          {/* Heading estilo Defontana — peso ultra-bold, tracking ajustado */}
          <h2
            className="text-slate-900 mb-2"
            style={{
              fontSize: '1.65rem',
              fontWeight: 800,
              letterSpacing: '-0.03em',
              lineHeight: 1.15,
              fontFamily: "'Outfit', sans-serif",
            }}
          >
            ¿Confirmas la eliminación?
          </h2>

          {/* Subtítulo — peso medio, color suave */}
          <p
            className="text-slate-500 mb-5 px-2"
            style={{
              fontSize: '1rem',
              fontWeight: 500,
              lineHeight: 1.6,
              fontFamily: "'Outfit', sans-serif",
            }}
          >
            {message}
          </p>

          {/* Badge de advertencia */}
          <div
            className="inline-flex items-center gap-2 bg-amber-50 border border-amber-200/70 rounded-xl px-3.5 py-2 mb-8"
            style={{
              fontSize: '0.78rem',
              fontWeight: 700,
              color: '#92400e',
              letterSpacing: '0.01em',
              fontFamily: "'Outfit', sans-serif",
            }}
          >
            <Trash2 size={13} className="shrink-0 text-amber-600" />
            <span>Atención: esta acción no se puede deshacer.</span>
          </div>

          {/* Botones */}
          <div className="flex flex-col-reverse sm:flex-row gap-3 w-full">
            <button
              onClick={cleanup}
              className="flex-1 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200 transition-all duration-200 active:scale-[0.98] border border-transparent"
              style={{
                padding: '0.875rem 1.25rem',
                fontSize: '0.95rem',
                fontWeight: 700,
                letterSpacing: '-0.01em',
                fontFamily: "'Outfit', sans-serif",
              }}
            >
              Cancelar
            </button>
            <button
              onClick={handleConfirm}
              className="flex-1 rounded-xl bg-rose-500 text-white hover:bg-rose-600 hover:shadow-lg hover:shadow-rose-500/25 transition-all duration-200 active:scale-[0.98]"
              style={{
                padding: '0.875rem 1.25rem',
                fontSize: '0.95rem',
                fontWeight: 700,
                letterSpacing: '-0.01em',
                fontFamily: "'Outfit', sans-serif",
              }}
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
