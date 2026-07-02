import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, CheckCircle2, Download, FileText, FileSpreadsheet, Eye, Edit3, Plus, ShoppingCart, Lock } from 'lucide-react';
import { exportToPDF, exportToExcel } from '../../../utils/exportUtils';

const MODE_CONFIG = {
    create: {
        badgeText: 'Nueva EVN',
        badgeClass: 'bg-brand-indigo/10 text-brand-indigo border border-brand-indigo/20',
        icon: Plus,
        actionLabel: 'Crear Propuesta',
        actionClass: 'bg-brand-indigo hover:bg-brand-indigo/90 shadow-brand-indigo/20',
    },
    edit: {
        badgeText: 'Editando',
        badgeClass: 'bg-warning/10 text-warning border border-warning/20',
        icon: Edit3,
        actionLabel: 'Actualizar EVN',
        actionClass: 'bg-warning hover:bg-warning/90 shadow-warning/20',
    },
    view: {
        badgeText: 'Solo Lectura',
        badgeClass: 'bg-muted text-muted-foreground border border-border',
        icon: Eye,
        actionLabel: null, // no save button in view mode
        actionClass: '',
    },
};

export default function EVNActionBar({
    initialEval,
    totals,
    items,
    otrosCostos,
    solicitud,
    evalData,
    mode = 'create',
    isSaving,
    onBack,
    onGenerarPropuesta,
    onCerrarEVN
}) {
    const [showExportMenu, setShowExportMenu] = useState(false);
    const navigate = useNavigate();
    const cfg = MODE_CONFIG[mode] || MODE_CONFIG.create;
    const ModeIcon = cfg.icon;

    // Una EVN adjudicada puede generar su Nota de Venta manualmente (plantilla pre-cargada).
    const puedeGenerarNV = mode === 'view' && initialEval?.estado === 'ADJUDICADA';
    // Una EVN adjudicada puede cerrarse para bloquear nuevas Notas de Venta.
    const puedeCerrar = mode === 'view' && initialEval?.estado === 'ADJUDICADA' && typeof onCerrarEVN === 'function';

    const handleGenerarNV = () => {
        navigate('/registros-nv', { state: { initialData: initialEval } });
    };

    const handleExportPDF = () => {
        exportToPDF({
            items,
            otrosCostos,
            totals,
            cliente: solicitud.clienteNombre || initialEval?.cliente,
            id: initialEval?.evaluacionNegocioId || 'NUEVO',
            fecha: initialEval?.fecha,
            condiciones: evalData.condiciones
        });
        setShowExportMenu(false);
    };

    const handleExportExcel = () => {
        exportToExcel({
            items,
            otrosCostos,
            totals,
            cliente: solicitud.clienteNombre || initialEval?.cliente,
            id: initialEval?.id || 'NUEVO'
        });
        setShowExportMenu(false);
    };

    const margenNum = parseFloat(totals.margenPorc || 0);
    const margenColor = margenNum >= 25 ? 'text-success' : margenNum >= 15 ? 'text-warning' : 'text-destructive';

    const evnNumero = initialEval?.numeroEvn || initialEval?.numero || initialEval?.evaluacionNegocioId;

    return (
        <div className="sticky top-0 z-50 bg-card/90 backdrop-blur-md border-b border-border px-8 py-4 mb-8">
            <div className="max-w-[1700px] mx-auto flex items-center justify-between">
                {/* Left: back + title */}
                <div className="flex items-center space-x-4">
                    <button
                        onClick={() => onBack(mode === 'edit')}
                        className="w-10 h-10 bg-card rounded-xl shadow-sm border border-border flex items-center justify-center hover:bg-muted transition-all group"
                    >
                        <ChevronLeft className="w-5 h-5 text-muted-foreground group-hover:text-brand-indigo group-hover:-translate-x-0.5 transition-all" />
                    </button>

                    <div className="flex items-center gap-3">
                        <div className="flex items-center space-x-2">
                            <h1 className="text-xl font-black text-foreground tracking-tight uppercase italic">
                                Evaluación de Negocio
                            </h1>
                            {evnNumero && (
                                <span className="px-2.5 py-1 bg-muted text-muted-foreground text-[10px] font-black rounded-full uppercase tracking-widest">
                                    {String(evnNumero).replace(/^EVN-?/i, '') ? `EVN-${String(evnNumero).replace(/^EVN-?/i, '')}` : evnNumero}
                                </span>
                            )}
                        </div>
                        <span className={`flex items-center gap-1.5 px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest ${cfg.badgeClass}`}>
                            <ModeIcon className="w-3 h-3" />
                            {cfg.badgeText}
                        </span>
                    </div>
                </div>

                {/* Right: metrics + actions */}
                <div className="flex items-center space-x-4">
                    {/* Key metrics */}
                    <div className="flex items-center space-x-2 mr-4 text-right">
                        <div>
                            <p className="text-[9px] font-black text-muted-foreground uppercase leading-none mb-1">Margen</p>
                            <p className={`text-xl font-black ${margenColor}`}>
                                {totals.margenPorc}%
                            </p>
                        </div>
                        <div className="w-px h-10 bg-border rounded-full mx-3" />
                        <div>
                            <p className="text-[9px] font-black text-muted-foreground uppercase leading-none mb-1">Total Venta</p>
                            <p className="text-xl font-black text-foreground tracking-tight tabular-nums">
                                ${(totals.totalNeto || 0).toLocaleString('es-CL')}
                            </p>
                        </div>
                        {mode !== 'view' && (
                            <>
                                <div className="w-px h-10 bg-border rounded-full mx-3" />
                                <div>
                                    <p className="text-[9px] font-black text-muted-foreground uppercase leading-none mb-1">Costo Total</p>
                                    <p className="text-xl font-black text-foreground tracking-tight tabular-nums">
                                        ${(totals.totalCostoGeneral || 0).toLocaleString('es-CL')}
                                    </p>
                                </div>
                            </>
                        )}
                    </div>

                    {/* Export */}
                    <div className="relative">
                        <button
                            onClick={() => setShowExportMenu(!showExportMenu)}
                            className="px-5 py-2.5 bg-card border border-border text-foreground rounded-xl font-black text-[10px] uppercase tracking-widest hover:bg-muted transition-all flex items-center gap-2"
                        >
                            <Download className="w-3.5 h-3.5 text-brand-indigo" />
                            Exportar
                        </button>

                        {showExportMenu && (
                            <div className="absolute top-full mt-2 right-0 w-48 bg-card rounded-2xl shadow-2xl border border-border py-2 z-[60] animate-in fade-in slide-in-from-top-2 duration-200">
                                <button
                                    onClick={handleExportPDF}
                                    className="w-full text-left px-4 py-3 text-[10px] font-black uppercase tracking-widest text-muted-foreground hover:bg-brand-indigo/10 hover:text-brand-indigo flex items-center transition-colors"
                                >
                                    <FileText className="w-4 h-4 mr-3" />
                                    Exportar a PDF
                                </button>
                                <button
                                    onClick={handleExportExcel}
                                    className="w-full text-left px-4 py-3 text-[10px] font-black uppercase tracking-widest text-muted-foreground hover:bg-success/10 hover:text-success flex items-center transition-colors border-t border-border"
                                >
                                    <FileSpreadsheet className="w-4 h-4 mr-3" />
                                    Exportar a Excel
                                </button>
                            </div>
                        )}
                    </div>

                    {/* Generar Nota de Venta — solo al visualizar una EVN ADJUDICADA */}
                    {puedeGenerarNV && (
                        <button
                            onClick={handleGenerarNV}
                            className="px-7 py-2.5 bg-success hover:bg-success/90 text-white rounded-xl font-black text-[10px] uppercase tracking-widest shadow-lg shadow-success/20 hover:-translate-y-0.5 transition-all flex items-center gap-2 group"
                        >
                            <ShoppingCart className="w-3.5 h-3.5 group-hover:rotate-12 transition-transform" />
                            Generar Nota de Venta
                        </button>
                    )}

                    {/* Cerrar EVN — bloquea nuevas Notas de Venta (solo EVN ADJUDICADA) */}
                    {puedeCerrar && (
                        <button
                            onClick={() => onCerrarEVN(initialEval)}
                            className="px-7 py-2.5 bg-foreground hover:bg-foreground/90 text-background rounded-xl font-black text-[10px] uppercase tracking-widest shadow-lg shadow-foreground/10 hover:-translate-y-0.5 transition-all flex items-center gap-2 group"
                        >
                            <Lock className="w-3.5 h-3.5 group-hover:rotate-12 transition-transform" />
                            Cerrar EVN
                        </button>
                    )}

                    {/* Save button — only in create/edit modes */}
                    {cfg.actionLabel && (
                        <button
                            onClick={onGenerarPropuesta}
                            disabled={isSaving}
                            className={`px-7 py-2.5 text-white rounded-xl font-black text-[10px] uppercase tracking-widest shadow-lg hover:-translate-y-0.5 transition-all flex items-center gap-2 group disabled:opacity-60 disabled:cursor-not-allowed disabled:transform-none ${cfg.actionClass}`}
                        >
                            {isSaving ? (
                                <>
                                    <span className="w-3.5 h-3.5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                                    Guardando...
                                </>
                            ) : (
                                <>
                                    {cfg.actionLabel}
                                    <CheckCircle2 className="w-3.5 h-3.5 group-hover:rotate-12 transition-transform" />
                                </>
                            )}
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}
