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
import { EvaluacionNegocioService } from '../../../remote/service/EvaluacionNegocioService';
import { useAuth } from '../../../contexts/AuthContext';
import { toast } from 'sonner';
import FirmaAprobacionModal from './FirmaAprobacionModal';
import {
    Card,
    CardHeader,
    CardTitle,
    CardDescription,
    CardContent,
    CardFooter
} from '../../../ui/card';

// El backend emite estos estados (EstadoEVN.java):
// BORRADOR | EVALUACION | APROBADA | ADJUDICADA | RECHAZADA | CANCELADA | CERRADA
// ADJUDICADA va en Activos: el deal fue ganado y aún se puede generar NV desde allí
const ESTADOS_ACTIVOS = new Set(['BORRADOR', 'EVALUACION', 'APROBADA', 'ADJUDICADA']);
const ESTADOS_CERRADOS = new Set(['RECHAZADA', 'CANCELADA', 'CERRADA']);
// Solo estos estados pueden adjudicarse o editarse (separado del filtro de tabs)
const ESTADOS_ADJUDICABLES = new Set(['BORRADOR', 'EVALUACION', 'APROBADA']);

const ESTADO_STYLE = {
    BORRADOR:   { badge: 'bg-warning/10 text-warning border border-warning/20',       dot: 'bg-warning' },
    EVALUACION: { badge: 'bg-primary/10 text-primary border border-primary/20',       dot: 'bg-primary' },
    APROBADA:   { badge: 'bg-brand-indigo/10 text-brand-indigo border border-brand-indigo/20', dot: 'bg-brand-indigo' },
    ADJUDICADA: { badge: 'bg-success/10 text-success border border-success/20',       dot: 'bg-success' },
    RECHAZADA:  { badge: 'bg-destructive/10 text-destructive border border-destructive/20', dot: 'bg-destructive' },
    CANCELADA:  { badge: 'bg-muted text-muted-foreground border border-border',       dot: 'bg-muted-foreground' },
    CERRADA:    { badge: 'bg-foreground text-background border border-border',       dot: 'bg-muted-foreground' }
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
        <div className="min-h-screen bg-muted/50 px-4 py-6 md:p-8 animate-in fade-in duration-700">
            <div className="max-w-[1600px] mx-auto">
                {/* Header */}
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8 md:mb-10">
                    <div>
                        <div className="flex items-center space-x-3 mb-2">
                            <div className="w-10 h-10 bg-brand-indigo rounded-2xl flex items-center justify-center shadow-lg shadow-brand-indigo/30">
                                <Target className="w-5 h-5 text-white" />
                            </div>
                            <h1 className="text-3xl font-black text-foreground tracking-tight italic">Administración de Negocios</h1>
                        </div>
                        <p className="text-sm text-muted-foreground font-bold uppercase tracking-widest mt-1 ml-1">
                            Acuerdo de condiciones y formalización de ventas
                        </p>
                    </div>

                    <div className="flex items-center gap-3">
                        <button
                            onClick={onNueva}
                            className="flex items-center gap-2 px-6 py-3 bg-brand-indigo hover:bg-brand-indigo/90 text-white rounded-2xl text-[11px] font-black uppercase tracking-widest shadow-lg shadow-brand-indigo/30 transition-all hover:scale-105 active:scale-95"
                        >
                            <Plus className="w-4 h-4" />
                            Nueva EVN
                        </button>
                        <div className="flex bg-card p-1.5 rounded-2xl border border-border shadow-sm">
                            <button
                                onClick={() => setActiveTab('Activo')}
                                className={`flex items-center px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === 'Activo' ? 'bg-brand-indigo text-white shadow-md' : 'text-muted-foreground hover:bg-muted'}`}
                            >
                                <Clock className="w-3.5 h-3.5 mr-2" />
                                Activos
                            </button>
                            <button
                                onClick={() => setActiveTab('Cerrado')}
                                className={`flex items-center px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === 'Cerrado' ? 'bg-brand-indigo text-white shadow-md' : 'text-muted-foreground hover:bg-muted'}`}
                            >
                                <CheckCircle className="w-3.5 h-3.5 mr-2" />
                                Cerrados
                            </button>
                        </div>
                    </div>
                </div>

                {/* Buscador */}
                <div className="mb-6 md:mb-8 bg-card p-4 rounded-[2.5rem] shadow-sm border border-border relative">
                    <Search className="absolute left-7 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                    <input
                        type="text"
                        placeholder="Buscar evaluación por ID, cliente, número o referencia..."
                        className="w-full pl-11 pr-4 py-3 bg-muted border-none rounded-2xl text-xs font-bold focus:ring-2 focus:ring-brand-indigo outline-none transition-all"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                {/* Grid de Evaluaciones */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    {filteredEvaluations.map((ev) => {
                        const estado = ev.estado || 'BORRADOR';
                        const puedeAdjudicar = ESTADOS_ADJUDICABLES.has(estado);
                        const estadoStyle = ESTADO_STYLE[estado] || ESTADO_STYLE.CANCELADA;

                        // Normalizar monto y margen desde los campos que el backend realmente devuelve
                        const montoTotal = ev.montoTotal ?? ev.totalNeto ?? ev.totalVenta ?? 0;
                        // rentabilidadEsperada viene del backend como porcentaje real (0-100)
                        // margenGanancia es el valor absoluto en CLP, no un porcentaje
                        const margenPorc = ev.rentabilidadEsperada ?? null;
                        const margenNum  = margenPorc !== null ? parseFloat(margenPorc) : null;
                        const margenColor = margenNum === null ? 'text-muted-foreground'
                            : margenNum >= 25 ? 'text-success'
                            : margenNum >= 15 ? 'text-warning'
                            : 'text-destructive';
                        const margenBg = margenNum === null ? 'bg-muted'
                            : margenNum >= 25 ? 'bg-success/10'
                            : margenNum >= 15 ? 'bg-warning/10'
                            : 'bg-destructive/10';

                        return (
                            <Card
                                key={ev.evaluacionNegocioId}
                                className="bg-card p-7 rounded-[2.5rem] border border-border hover:shadow-xl hover:shadow-foreground/5 transition-all flex flex-col gap-6"
                            >
                                <CardHeader className="p-0 flex flex-col gap-4">
                                    <div className="flex justify-between items-center w-full">
                                        <span className="text-[11px] font-black text-brand-indigo bg-brand-indigo/10 px-2.5 py-1 rounded-xl shadow-sm border border-brand-indigo/20 uppercase tracking-widest">
                                            EVN-{formatNumeroEVN(ev.numeroEvn ?? ev.numero)}
                                        </span>
                                        <span className={`flex items-center gap-1.5 px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest ${estadoStyle.badge}`}>
                                            <span className={`w-1.5 h-1.5 rounded-full ${estadoStyle.dot}`} />
                                            {estado}
                                        </span>
                                    </div>
                                    <div>
                                        <CardTitle className="text-xl font-black text-foreground leading-tight uppercase mb-1">
                                            {ev.clienteNombre || 'Sin cliente'}
                                        </CardTitle>
                                        <CardDescription className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest flex items-center gap-1 italic">
                                            GESTIONADO POR: <span className="text-brand-indigo not-italic">{ev.vendedorNombre || 'SISTEMA'}</span>
                                        </CardDescription>
                                    </div>
                                </CardHeader>

                                <CardContent className="p-0 flex-1 flex flex-col justify-between gap-5">
                                    <div className="grid grid-cols-2 gap-4">
                                        <div className="bg-sidebar px-4 py-3.5 rounded-2xl shadow-inner flex flex-col justify-between">
                                            <p className="text-[9px] font-black text-sidebar-muted uppercase mb-1">Monto Total Venta</p>
                                            <p className="text-lg font-black text-white tabular-nums">
                                                {montoTotal > 0 ? `$${montoTotal.toLocaleString('es-CL')}` : <span className="text-sidebar-muted text-xs">Sin cálculo</span>}
                                            </p>
                                        </div>
                                        <div className={`${margenBg} px-4 py-3.5 rounded-2xl flex flex-col justify-between transition-colors`}>
                                            <p className={`text-[9px] font-black uppercase mb-1 ${margenColor}`}>Margen s/Venta</p>
                                            <p className={`text-lg font-black tabular-nums ${margenColor}`}>
                                                {margenNum !== null ? `${margenNum.toFixed(1)}%` : <span className="text-muted-foreground text-xs">—</span>}
                                            </p>
                                        </div>
                                    </div>

                                    {(ev.costeoId || ev.solicitudCotizacionId) && (
                                        <div className="flex flex-wrap gap-1.5">
                                            {ev.costeoId && (
                                                <span className="flex items-center gap-1.5 px-3 py-1 bg-warning/10 border border-warning/20 text-warning rounded-full text-[9px] font-black uppercase tracking-widest">
                                                    <Calculator className="w-2.5 h-2.5" />
                                                    SCOS #{ev.costeoId}
                                                </span>
                                            )}
                                            {ev.solicitudCotizacionId && (
                                                <span className="flex items-center gap-1.5 px-3 py-1 bg-primary/10 border border-primary/20 text-primary rounded-full text-[9px] font-black uppercase tracking-widest">
                                                    <FileText className="w-2.5 h-2.5" />
                                                    SCOT #{ev.solicitudCotizacionId}
                                                </span>
                                            )}
                                        </div>
                                    )}
                                </CardContent>

                                <CardFooter className="p-0 flex flex-col gap-2 pt-4 border-t border-border w-full">
                                    <div className="flex gap-2 w-full">
                                        <button
                                            onClick={() => onVer(ev)}
                                            className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2.5 bg-muted hover:bg-muted/70 text-foreground rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <ExternalLink className="w-3.5 h-3.5" />
                                            Ver Detalle
                                        </button>
                                        {puedeAdjudicar && (
                                            <button
                                                onClick={() => onEditar(ev)}
                                                className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2.5 bg-brand-indigo/10 hover:bg-brand-indigo/15 text-brand-indigo rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                            >
                                                Ajustar
                                            </button>
                                        )}
                                    </div>
                                    {puedeAdjudicar && (
                                        <button
                                            onClick={(e) => openFirma(ev, e)}
                                            className="w-full flex items-center justify-center gap-2 px-3 py-3 bg-success hover:bg-success/90 text-white rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <CheckCircle2 className="w-3.5 h-3.5" />
                                            Adjudicar (requiere firma)
                                        </button>
                                    )}
                                </CardFooter>
                            </Card>
                        );
                    })}

                    {filteredEvaluations.length === 0 && (
                        <div className="col-span-full py-32 flex flex-col items-center justify-center bg-card border-2 border-dashed border-border rounded-[4rem]">
                            <Target className="w-16 h-16 text-muted-foreground/30 mb-6" />
                            <p className="text-sm font-black text-muted-foreground uppercase tracking-widest">
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
