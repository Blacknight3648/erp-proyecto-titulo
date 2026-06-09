import { useState, useMemo, useEffect } from 'react';
import {
    Target,
    Plus,
    Clock,
    CheckCircle,
    Search,
    CheckCircle2,
    ExternalLink,
    Calculator,
    FileText
} from 'lucide-react';
import { EvaluacionNegocioService } from '../../../../../remote/service/EvaluacionNegocioService';
import { useAuth } from '../../../../../contexts/AuthContext';
import { toast } from 'sonner';
import FirmaAprobacionModal from './Modals/FirmaAprobacionModal';

// El backend emite estos estados (EstadoEVN.java):
// BORRADOR | EVALUACION | APROBADA | ADJUDICADA | RECHAZADA | CANCELADA
const ESTADOS_ACTIVOS = new Set(['BORRADOR', 'EVALUACION', 'APROBADA']);
const ESTADOS_CERRADOS = new Set(['ADJUDICADA', 'RECHAZADA', 'CANCELADA']);

const ESTADO_STYLE = {
    BORRADOR:   { badge: 'bg-amber-100 text-amber-700 border border-amber-200',   dot: 'bg-amber-500' },
    EVALUACION: { badge: 'bg-sky-100 text-sky-700 border border-sky-200',           dot: 'bg-sky-500' },
    APROBADA:   { badge: 'bg-indigo-100 text-indigo-700 border border-indigo-200', dot: 'bg-indigo-500' },
    ADJUDICADA: { badge: 'bg-emerald-100 text-emerald-700 border border-emerald-200', dot: 'bg-emerald-500' },
    RECHAZADA:  { badge: 'bg-red-100 text-red-700 border border-red-200',           dot: 'bg-red-500' },
    CANCELADA:  { badge: 'bg-gray-100 text-gray-500 border border-gray-200',        dot: 'bg-gray-400' }
};

/** Normaliza el número de EVN eliminando prefijos duplicados y devuelve solo el número entero */
const formatNumeroEVN = (raw) => {
    if (!raw && raw !== 0) return '—';
    const s = String(raw).trim().replace(/^EVN-?/i, '');
    return s || '—';
};

export default function ListaEVN({ onNueva, onEditar, onVer }) {
    const { user } = useAuth();
    const [evaluaciones, setEvaluaciones] = useState([]);
    const [, setLoading] = useState(false);
    const [activeTab, setActiveTab] = useState('Activo');
    const [searchTerm, setSearchTerm] = useState('');

    const [firmaModal, setFirmaModal] = useState({ open: false, evn: null });
    const [submitting, setSubmitting] = useState(false);

    const loadEvaluations = async () => {
        setLoading(true);
        try {
            const data = await EvaluacionNegocioService.getAll();
            setEvaluaciones(data || []);
        } catch (error) {
            console.error("Error loading evaluations:", error);
            toast.error("Error al cargar evaluaciones");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadEvaluations();
    }, []);

    const filteredEvaluations = useMemo(() => {
        const targetSet = activeTab === 'Activo' ? ESTADOS_ACTIVOS : ESTADOS_CERRADOS;
        const q = searchTerm.toLowerCase();
        return evaluaciones.filter(ev => {
            if (!targetSet.has(ev.estado)) return false;
            if (!q) return true;
            return (
                String(ev.clienteNombre || '').toLowerCase().includes(q) ||
                String(ev.evaluacionNegocioId || '').includes(searchTerm) ||
                String(ev.numeroEvn || ev.numero || '').toLowerCase().includes(q) ||
                String(ev.referencia || '').toLowerCase().includes(q)
            );
        });
    }, [evaluaciones, activeTab, searchTerm]);

    const openFirma = (ev, e) => {
        e.stopPropagation();
        setFirmaModal({ open: true, evn: ev });
    };

    const handleAdjudicar = async ({ aprobador, observacion }) => {
        const ev = firmaModal.evn;
        if (!ev) return;
        setSubmitting(true);
        try {
            await EvaluacionNegocioService.adjudicar(
                ev.evaluacionNegocioId || ev.id,
                aprobador,
                observacion
            );
            toast.success(`EVN adjudicada y firmada por ${aprobador}`);
            setFirmaModal({ open: false, evn: null });
            loadEvaluations();
        } catch (error) {
            toast.error('Error al adjudicar: ' + (error.response?.data?.message || error.message));
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50/50 px-4 py-6 md:p-8 animate-in fade-in duration-700">
            <div className="max-w-[1600px] mx-auto">
                {/* Header */}
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8 md:mb-10">
                    <div>
                        <div className="flex items-center space-x-3 mb-2">
                            <div className="w-10 h-10 bg-indigo-600 rounded-2xl flex items-center justify-center shadow-lg shadow-indigo-200">
                                <Target className="w-5 h-5 text-white" />
                            </div>
                            <h1 className="text-3xl font-black text-gray-800 tracking-tight italic">Administración de Negocios</h1>
                        </div>
                        <p className="text-sm text-gray-500 font-bold uppercase tracking-widest mt-1 ml-1">
                            Acuerdo de condiciones y formalización de ventas
                        </p>
                    </div>

                    <div className="flex items-center gap-3">
                        <button
                            onClick={onNueva}
                            className="flex items-center gap-2 px-6 py-3 bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl text-[11px] font-black uppercase tracking-widest shadow-lg shadow-indigo-200 transition-all hover:scale-105 active:scale-95"
                        >
                            <Plus className="w-4 h-4" />
                            Nueva EVN
                        </button>
                        <div className="flex bg-white p-1.5 rounded-2xl border border-gray-100 shadow-sm">
                            <button
                                onClick={() => setActiveTab('Activo')}
                                className={`flex items-center px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === 'Activo' ? 'bg-indigo-600 text-white shadow-md' : 'text-gray-400 hover:bg-gray-50'}`}
                            >
                                <Clock className="w-3.5 h-3.5 mr-2" />
                                Activos
                            </button>
                            <button
                                onClick={() => setActiveTab('Cerrado')}
                                className={`flex items-center px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === 'Cerrado' ? 'bg-indigo-600 text-white shadow-md' : 'text-gray-400 hover:bg-gray-50'}`}
                            >
                                <CheckCircle className="w-3.5 h-3.5 mr-2" />
                                Cerrados
                            </button>
                        </div>
                    </div>
                </div>

                {/* Buscador */}
                <div className="mb-6 md:mb-8 bg-white p-4 rounded-[2.5rem] shadow-sm border border-gray-100 relative">
                    <Search className="absolute left-7 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                    <input
                        type="text"
                        placeholder="Buscar evaluación por ID, cliente, número o referencia..."
                        className="w-full pl-11 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-xs font-bold focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                {/* Grid de Evaluaciones */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    {filteredEvaluations.map((ev) => {
                        const estado = ev.estado || 'BORRADOR';
                        const puedeAdjudicar = ESTADOS_ACTIVOS.has(estado);
                        const estadoStyle = ESTADO_STYLE[estado] || ESTADO_STYLE.CANCELADA;

                        // Normalizar monto y margen desde los campos que el backend realmente devuelve
                        const montoTotal = ev.montoTotal ?? ev.totalNeto ?? ev.totalVenta ?? 0;
                        const margenPorc = ev.margenPorc ?? ev.margenGanancia ?? null;
                        const margenNum  = margenPorc !== null ? parseFloat(margenPorc) : null;
                        const margenColor = margenNum === null ? 'text-gray-400'
                            : margenNum >= 25 ? 'text-emerald-600'
                            : margenNum >= 15 ? 'text-amber-600'
                            : 'text-red-600';
                        const margenBg = margenNum === null ? 'bg-gray-50'
                            : margenNum >= 25 ? 'bg-emerald-50'
                            : margenNum >= 15 ? 'bg-amber-50'
                            : 'bg-red-50';

                        return (
                            <div
                                key={ev.evaluacionNegocioId}
                                className="bg-white p-7 rounded-[2.5rem] border border-gray-100 hover:shadow-xl hover:shadow-gray-100 transition-all flex flex-col"
                            >
                                <div className="flex justify-between items-start mb-5">
                                    <span className="text-[11px] font-black text-indigo-600 bg-indigo-50 px-2.5 py-1 rounded-xl shadow-sm border border-indigo-100 uppercase tracking-widest">
                                        EVN-{formatNumeroEVN(ev.numeroEvn ?? ev.numero)}
                                    </span>
                                    <span className={`flex items-center gap-1.5 px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest ${estadoStyle.badge}`}>
                                        <span className={`w-1.5 h-1.5 rounded-full ${estadoStyle.dot}`} />
                                        {estado}
                                    </span>
                                </div>

                                <div className="mb-6 flex-1">
                                    <h4 className="text-xl font-black text-gray-800 leading-tight uppercase mb-1">
                                        {ev.clienteNombre || 'Sin cliente'}
                                    </h4>
                                    <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest flex items-center gap-1 italic">
                                        GESTIONADO POR: <span className="text-indigo-500 not-italic">{ev.vendedorNombre || 'SISTEMA'}</span>
                                    </p>
                                </div>

                                <div className="grid grid-cols-2 gap-3 mb-5">
                                    <div className="bg-slate-800 px-3 py-3 rounded-2xl">
                                        <p className="text-[9px] font-black text-slate-400 uppercase mb-1">Monto Total Venta</p>
                                        <p className="text-base font-black text-white tabular-nums">
                                            {montoTotal > 0 ? `$${montoTotal.toLocaleString('es-CL')}` : <span className="text-slate-500 text-xs">Sin cálculo</span>}
                                        </p>
                                    </div>
                                    <div className={`${margenBg} px-3 py-3 rounded-2xl`}>
                                        <p className={`text-[9px] font-black uppercase mb-1 ${margenColor}`}>Margen s/Venta</p>
                                        <p className={`text-base font-black tabular-nums ${margenColor}`}>
                                            {margenNum !== null ? `${margenNum.toFixed(1)}%` : <span className="text-gray-400 text-xs">—</span>}
                                        </p>
                                    </div>
                                </div>

                                {(ev.costeoId || ev.solicitudCotizacionId) && (
                                    <div className="flex flex-wrap gap-1.5 mb-4">
                                        {ev.costeoId && (
                                            <span className="flex items-center gap-1 px-2.5 py-1 bg-amber-50 border border-amber-200 text-amber-700 rounded-full text-[9px] font-black uppercase tracking-widest">
                                                <Calculator className="w-2.5 h-2.5" />
                                                SCOS #{ev.costeoId}
                                            </span>
                                        )}
                                        {ev.solicitudCotizacionId && (
                                            <span className="flex items-center gap-1 px-2.5 py-1 bg-blue-50 border border-blue-200 text-blue-700 rounded-full text-[9px] font-black uppercase tracking-widest">
                                                <FileText className="w-2.5 h-2.5" />
                                                SCOT #{ev.solicitudCotizacionId}
                                            </span>
                                        )}
                                    </div>
                                )}

                                <div className="flex flex-col gap-2 pt-4 border-t border-gray-50">
                                    <div className="flex gap-2">
                                        <button
                                            onClick={() => onVer(ev)}
                                            className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <ExternalLink className="w-3 h-3" />
                                            Ver Detalle
                                        </button>
                                        {puedeAdjudicar && (
                                            <button
                                                onClick={() => onEditar(ev)}
                                                className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                            >
                                                Ajustar
                                            </button>
                                        )}
                                    </div>
                                    {puedeAdjudicar && (
                                        <button
                                            onClick={(e) => openFirma(ev, e)}
                                            className="w-full flex items-center justify-center gap-2 px-3 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <CheckCircle2 className="w-3.5 h-3.5" />
                                            Adjudicar (requiere firma)
                                        </button>
                                    )}
                                </div>
                            </div>
                        );
                    })}

                    {filteredEvaluations.length === 0 && (
                        <div className="col-span-full py-32 flex flex-col items-center justify-center bg-white border-2 border-dashed border-gray-100 rounded-[4rem]">
                            <Target className="w-16 h-16 text-gray-100 mb-6" />
                            <p className="text-sm font-black text-gray-300 uppercase tracking-widest">
                                No se encontraron evaluaciones {activeTab === 'Activo' ? 'activas' : 'cerradas'}
                            </p>
                        </div>
                    )}
                </div>
            </div>

            <FirmaAprobacionModal
                open={firmaModal.open}
                title="Adjudicar Evaluación"
                description={firmaModal.evn ? `EVN-${formatNumeroEVN(firmaModal.evn.numeroEvn ?? firmaModal.evn.numero)} — ${firmaModal.evn.clienteNombre || ''}` : ''}
                accion="Adjudicar y firmar"
                accentColor="emerald"
                defaultAprobador={user?.name || ''}
                onClose={() => setFirmaModal({ open: false, evn: null })}
                onConfirm={handleAdjudicar}
                isSubmitting={submitting}
            />
        </div>
    );
}
