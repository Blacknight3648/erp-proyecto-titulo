import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, CheckCircle2, Download, FileText, FileSpreadsheet, Eye, Edit3, Plus, ShoppingCart } from 'lucide-react';
import { exportToPDF, exportToExcel } from '../../../../../../utils/exportUtils';

const MODE_CONFIG = {
    create: {
        badgeText: 'Nueva EVN',
        badgeClass: 'bg-indigo-100 text-indigo-700 border border-indigo-200',
        icon: Plus,
        actionLabel: 'Crear Propuesta',
        actionClass: 'bg-indigo-600 hover:bg-indigo-700 shadow-indigo-200',
    },
    edit: {
        badgeText: 'Editando',
        badgeClass: 'bg-amber-100 text-amber-700 border border-amber-200',
        icon: Edit3,
        actionLabel: 'Actualizar EVN',
        actionClass: 'bg-amber-600 hover:bg-amber-700 shadow-amber-200',
    },
    view: {
        badgeText: 'Solo Lectura',
        badgeClass: 'bg-slate-100 text-slate-600 border border-slate-200',
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
    onGenerarPropuesta
}) {
    const [showExportMenu, setShowExportMenu] = useState(false);
    const navigate = useNavigate();
    const cfg = MODE_CONFIG[mode] || MODE_CONFIG.create;
    const ModeIcon = cfg.icon;

    // Una EVN adjudicada puede generar su Nota de Venta manualmente (plantilla pre-cargada).
    const puedeGenerarNV = mode === 'view' && initialEval?.estado === 'ADJUDICADA';

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
    const margenColor = margenNum >= 25 ? 'text-emerald-600' : margenNum >= 15 ? 'text-amber-500' : 'text-red-500';

    const evnNumero = initialEval?.numeroEvn || initialEval?.numero || initialEval?.evaluacionNegocioId;

    return (
        <div className="sticky top-0 z-50 bg-white/90 backdrop-blur-md border-b border-gray-100 px-8 py-4 mb-8">
            <div className="max-w-[1700px] mx-auto flex items-center justify-between">
                {/* Left: back + title */}
                <div className="flex items-center space-x-4">
                    <button
                        onClick={() => onBack(mode === 'edit')}
                        className="w-10 h-10 bg-white rounded-xl shadow-sm border border-gray-100 flex items-center justify-center hover:bg-gray-50 transition-all group"
                    >
                        <ChevronLeft className="w-5 h-5 text-gray-400 group-hover:text-indigo-600 group-hover:-translate-x-0.5 transition-all" />
                    </button>

                    <div className="flex items-center gap-3">
                        <div className="flex items-center space-x-2">
                            <h1 className="text-xl font-black text-gray-800 tracking-tight uppercase italic">
                                Evaluación de Negocio
                            </h1>
                            {evnNumero && (
                                <span className="px-2.5 py-1 bg-gray-100 text-gray-600 text-[10px] font-black rounded-full uppercase tracking-widest">
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
                            <p className="text-[9px] font-black text-gray-400 uppercase leading-none mb-1">Margen</p>
                            <p className={`text-xl font-black ${margenColor}`}>
                                {totals.margenPorc}%
                            </p>
                        </div>
                        <div className="w-px h-10 bg-gray-100 rounded-full mx-3" />
                        <div>
                            <p className="text-[9px] font-black text-gray-400 uppercase leading-none mb-1">Total Venta</p>
                            <p className="text-xl font-black text-gray-800 tracking-tight tabular-nums">
                                ${(totals.totalNeto || 0).toLocaleString('es-CL')}
                            </p>
                        </div>
                        {mode !== 'view' && (
                            <>
                                <div className="w-px h-10 bg-gray-100 rounded-full mx-3" />
                                <div>
                                    <p className="text-[9px] font-black text-gray-400 uppercase leading-none mb-1">Costo Total</p>
                                    <p className="text-xl font-black text-slate-700 tracking-tight tabular-nums">
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
                            className="px-5 py-2.5 bg-white border border-gray-200 text-gray-700 rounded-xl font-black text-[10px] uppercase tracking-widest hover:bg-gray-50 transition-all flex items-center gap-2"
                        >
                            <Download className="w-3.5 h-3.5 text-indigo-600" />
                            Exportar
                        </button>

                        {showExportMenu && (
                            <div className="absolute top-full mt-2 right-0 w-48 bg-white rounded-2xl shadow-2xl border border-gray-100 py-2 z-[60] animate-in fade-in slide-in-from-top-2 duration-200">
                                <button
                                    onClick={handleExportPDF}
                                    className="w-full text-left px-4 py-3 text-[10px] font-black uppercase tracking-widest text-gray-600 hover:bg-indigo-50 hover:text-indigo-600 flex items-center transition-colors"
                                >
                                    <FileText className="w-4 h-4 mr-3" />
                                    Exportar a PDF
                                </button>
                                <button
                                    onClick={handleExportExcel}
                                    className="w-full text-left px-4 py-3 text-[10px] font-black uppercase tracking-widest text-gray-600 hover:bg-green-50 hover:text-green-600 flex items-center transition-colors border-t border-gray-50"
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
                            className="px-7 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl font-black text-[10px] uppercase tracking-widest shadow-lg shadow-emerald-200 hover:-translate-y-0.5 transition-all flex items-center gap-2 group"
                        >
                            <ShoppingCart className="w-3.5 h-3.5 group-hover:rotate-12 transition-transform" />
                            Generar Nota de Venta
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
