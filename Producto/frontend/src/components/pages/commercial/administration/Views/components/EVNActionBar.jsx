import { useState } from 'react';
import { ChevronLeft, CheckCircle2, Download, FileText, FileSpreadsheet } from 'lucide-react';
import { exportToPDF, exportToExcel } from '../../../../../../utils/exportUtils';

export default function EVNActionBar({
    initialEval,
    totals,
    items,
    otrosCostos,
    solicitud,
    evalData,
    isReadOnly,
    isSaving,
    onBack,
    onGenerarPropuesta
}) {
    const [showExportMenu, setShowExportMenu] = useState(false);

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

    return (
        <div className="sticky top-0 z-50 bg-white/80 backdrop-blur-md border-b border-gray-100 px-8 py-4 mb-8">
            <div className="max-w-[1700px] mx-auto flex items-center justify-between">
                <div className="flex items-center space-x-6">
                    <button
                        onClick={onBack}
                        className="w-10 h-10 bg-white rounded-xl shadow-sm border border-gray-100 flex items-center justify-center hover:bg-gray-50 transition-all group"
                    >
                        <ChevronLeft className="w-5 h-5 text-gray-400 group-hover:text-indigo-600 group-hover:-translate-x-0.5 transition-all" />
                    </button>
                    <div className="flex items-center space-x-3">
                        <h1 className="text-xl font-black text-gray-800 tracking-tight uppercase italic">
                            Evaluación de Negocio
                        </h1>
                        <span className="px-3 py-1 bg-indigo-50 text-indigo-600 text-[10px] font-black rounded-full uppercase tracking-widest">
                            {initialEval?.numeroEvn || initialEval?.numero || initialEval?.evaluacionNegocioId || 'Nuevo'}
                        </span>
                    </div>
                </div>

                <div className="flex items-center space-x-4">
                    <div className="flex items-center space-x-2 mr-6 text-right">
                        <div>
                            <p className="text-[10px] font-black text-gray-400 uppercase leading-none mb-1">Margen Final</p>
                            <p className={`text-xl font-black ${parseFloat(totals.margenPorc) < 20 ? 'text-orange-500' : 'text-green-600'}`}>
                                {totals.margenPorc}%
                            </p>
                        </div>
                        <div className="w-1 h-10 bg-gray-100 rounded-full mx-4" />
                        <div>
                            <p className="text-[10px] font-black text-gray-400 uppercase leading-none mb-1">Monto Total</p>
                            <p className="text-xl font-black text-gray-800 tracking-tight">
                                ${(totals.totalNeto || 0).toLocaleString('es-CL')}
                            </p>
                        </div>
                    </div>

                    <div className="relative">
                        <button
                            onClick={() => setShowExportMenu(!showExportMenu)}
                            className="px-6 py-3 bg-white border border-gray-200 text-gray-700 rounded-xl font-black text-xs uppercase tracking-widest hover:bg-gray-50 transition-all flex items-center"
                        >
                            <Download className="w-4 h-4 mr-2 text-indigo-600" />
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

                    {!isReadOnly && (
                        <button
                            onClick={onGenerarPropuesta}
                            disabled={isSaving}
                            className="px-8 py-3 bg-indigo-600 text-white rounded-xl font-black text-xs uppercase tracking-widest shadow-xl shadow-indigo-100 hover:bg-indigo-700 hover:-translate-y-0.5 transition-all flex items-center group disabled:opacity-60 disabled:cursor-not-allowed"
                        >
                            {isSaving ? 'Guardando...' : 'Generar Propuesta'}
                            <CheckCircle2 className="w-4 h-4 ml-3 group-hover:rotate-12 transition-transform" />
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}
