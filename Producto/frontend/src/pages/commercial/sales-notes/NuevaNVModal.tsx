import { useState, useEffect } from 'react';
import { X, FileText, Copy, ChevronRight, ChevronLeft, Search, Target, Plus } from 'lucide-react';

/** Normaliza el número de EVN a "EVN-####" */
const formatNumeroEVN = (raw) => {
    if (!raw && raw !== 0) return '—';
    const s = String(raw).trim().replace(/^EVN-?/i, '');
    return s ? `EVN-${s}` : '—';
};

/**
 * Modal inicial de creación de Nota de Venta.
 * Obliga a elegir entre crear desde cero o desde una plantilla de Evaluación de
 * Negocio (EVN) antes de entrar al formulario.
 *
 * Props:
 *  - open: boolean
 *  - onClose(): cerrar/cancelar
 *  - onDesdeCero(): iniciar NV en blanco
 *  - evaluaciones: EVN aplicables (ya filtradas a ADJUDICADA)
 *  - onSelectEVN(evn): EVN elegida como plantilla
 */
export default function NuevaNVModal({ open, onClose, onDesdeCero, evaluaciones = [], onSelectEVN }) {
    const [step, setStep] = useState('choice');
    const [search, setSearch] = useState('');

    // Cada apertura arranca en el paso de elección y sin búsqueda previa.
    useEffect(() => {
        if (open) {
            setStep('choice');
            setSearch('');
        }
    }, [open]);

    if (!open) return null;

    const hayPlantillas = evaluaciones.length > 0;

    const q = search.trim().toLowerCase();
    const filtradas = !q
        ? evaluaciones
        : evaluaciones.filter(ev =>
            String(ev.numeroEvn ?? ev.numero ?? '').toLowerCase().includes(q) ||
            String(ev.clienteNombre ?? '').toLowerCase().includes(q) ||
            String(ev.referencia ?? '').toLowerCase().includes(q) ||
            String(ev.evaluacionNegocioId ?? ev.id ?? '').includes(search.trim())
        );

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <div
                className="absolute inset-0 bg-black/40 backdrop-blur-sm"
                onClick={onClose}
            />

            <div className="relative bg-white rounded-[2.5rem] shadow-2xl w-full max-w-2xl max-h-[85vh] flex flex-col overflow-hidden animate-in zoom-in-95 fade-in duration-200">

                {/* Header */}
                <div className="flex items-center justify-between p-6 border-b border-gray-100">
                    <div className="flex items-center gap-3">
                        {step === 'template' && (
                            <button
                                onClick={() => setStep('choice')}
                                className="w-9 h-9 bg-gray-100 rounded-xl flex items-center justify-center hover:bg-gray-200 transition-all"
                                aria-label="Volver"
                            >
                                <ChevronLeft className="w-4 h-4 text-gray-500" />
                            </button>
                        )}
                        <div>
                            <h2 className="text-lg font-black text-gray-800 uppercase tracking-tight">
                                {step === 'choice' ? 'Nueva Nota de Venta' : 'Elegir Evaluación de Negocio'}
                            </h2>
                            <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">
                                {step === 'choice'
                                    ? '¿Cómo quieres iniciar la NV?'
                                    : 'Selecciona la EVN adjudicada que usarás como plantilla'}
                            </p>
                        </div>
                    </div>
                    <button
                        onClick={onClose}
                        className="w-10 h-10 bg-gray-100 rounded-xl flex items-center justify-center hover:bg-gray-200 transition-all"
                        aria-label="Cerrar"
                    >
                        <X className="w-4 h-4 text-gray-500" />
                    </button>
                </div>

                {/* Paso 1: elección */}
                {step === 'choice' && (
                    <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-5">
                        {/* Desde cero */}
                        <button
                            onClick={onDesdeCero}
                            className="group text-left p-6 rounded-[2rem] border-2 border-gray-100 hover:border-blue-500 hover:shadow-xl transition-all flex flex-col gap-4"
                        >
                            <div className="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center group-hover:bg-blue-600 transition-colors">
                                <Plus className="w-6 h-6 text-blue-600 group-hover:text-white transition-colors" />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-wide mb-1">Crear desde cero</h3>
                                <p className="text-xs font-bold text-gray-400">Formulario en blanco para capturar manualmente cliente e ítems.</p>
                            </div>
                            <span className="mt-auto flex items-center gap-1 text-[10px] font-black text-blue-600 uppercase tracking-widest opacity-0 group-hover:opacity-100 transition-all">
                                Comenzar <ChevronRight className="w-3.5 h-3.5" />
                            </span>
                        </button>

                        {/* Desde plantilla EVN */}
                        <button
                            onClick={() => hayPlantillas && setStep('template')}
                            disabled={!hayPlantillas}
                            className={`group text-left p-6 rounded-[2rem] border-2 transition-all flex flex-col gap-4 ${
                                hayPlantillas
                                    ? 'border-gray-100 hover:border-emerald-500 hover:shadow-xl cursor-pointer'
                                    : 'border-gray-100 opacity-60 cursor-not-allowed'
                            }`}
                        >
                            <div className={`w-12 h-12 rounded-2xl flex items-center justify-center transition-colors ${
                                hayPlantillas ? 'bg-emerald-50 group-hover:bg-emerald-600' : 'bg-gray-100'
                            }`}>
                                <Copy className={`w-6 h-6 transition-colors ${hayPlantillas ? 'text-emerald-600 group-hover:text-white' : 'text-gray-400'}`} />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-wide mb-1">Desde plantilla EVN</h3>
                                <p className="text-xs font-bold text-gray-400">
                                    {hayPlantillas
                                        ? 'Precarga cliente e ítems desde una Evaluación de Negocio adjudicada.'
                                        : 'No hay evaluaciones adjudicadas disponibles.'}
                                </p>
                            </div>
                            {hayPlantillas && (
                                <span className="mt-auto flex items-center gap-1 text-[10px] font-black text-emerald-600 uppercase tracking-widest opacity-0 group-hover:opacity-100 transition-all">
                                    Elegir EVN <ChevronRight className="w-3.5 h-3.5" />
                                </span>
                            )}
                        </button>
                    </div>
                )}

                {/* Paso 2: selector de EVN */}
                {step === 'template' && (
                    <>
                        <div className="px-6 pt-5 pb-3">
                            <div className="relative">
                                <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                                <input
                                    autoFocus
                                    type="text"
                                    value={search}
                                    onChange={(e) => setSearch(e.target.value)}
                                    placeholder="Buscar por número, cliente o referencia..."
                                    className="w-full pl-11 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-sm font-bold focus:ring-2 focus:ring-emerald-500 outline-none transition-all"
                                />
                            </div>
                        </div>

                        <div className="px-6 pb-6 overflow-y-auto flex-1 space-y-3">
                            {filtradas.map((ev) => (
                                <button
                                    key={ev.evaluacionNegocioId ?? ev.id}
                                    onClick={() => onSelectEVN(ev)}
                                    className="group w-full text-left p-4 rounded-2xl border-2 border-gray-50 hover:border-emerald-500 hover:bg-emerald-50/30 transition-all flex items-center justify-between gap-4"
                                >
                                    <div className="min-w-0">
                                        <div className="flex items-center gap-2 mb-1">
                                            <span className="text-[10px] font-black text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-full uppercase tracking-widest">
                                                {formatNumeroEVN(ev.numeroEvn ?? ev.numero)}
                                            </span>
                                            <span className="text-[9px] font-black text-gray-400 uppercase tracking-widest">
                                                {(ev.items?.length || 0)} ítems
                                            </span>
                                        </div>
                                        <p className="text-sm font-black text-gray-800 uppercase truncate">{ev.clienteNombre || 'Sin cliente'}</p>
                                        <p className="text-[11px] font-bold text-gray-400 truncate">{ev.referencia || 'Sin referencia'}</p>
                                    </div>
                                    <ChevronRight className="w-5 h-5 text-gray-300 group-hover:text-emerald-600 group-hover:translate-x-1 transition-all flex-shrink-0" />
                                </button>
                            ))}

                            {filtradas.length === 0 && (
                                <div className="py-16 flex flex-col items-center justify-center text-center">
                                    <Target className="w-12 h-12 text-gray-100 mb-4" />
                                    <p className="text-sm font-black text-gray-300 uppercase tracking-widest">
                                        {evaluaciones.length === 0
                                            ? 'No hay evaluaciones adjudicadas disponibles'
                                            : 'Sin resultados para la búsqueda'}
                                    </p>
                                    {evaluaciones.length === 0 && (
                                        <p className="text-xs font-bold text-gray-300 mt-2">
                                            Adjudica una Evaluación de Negocio para usarla como plantilla.
                                        </p>
                                    )}
                                </div>
                            )}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}
