import { AlertCircle, FileText, Save } from 'lucide-react';

export default function SCSummaryCard({ totalUnits, isManual, generatePDF, disabledPdf, onCreate }) {
    return (
        <div className="bg-sidebar text-sidebar-foreground p-6 rounded-[2rem] flex flex-col justify-between">
            <div>
                <h3 className="text-xs font-black uppercase tracking-widest text-brand-indigo mb-6">Resumen Operativo</h3>
                <div className="space-y-6">
                    <div className="p-4 bg-white/5 rounded-2xl border border-sidebar-border text-center">
                        <span className="text-[10px] font-bold text-sidebar-muted uppercase tracking-widest block mb-2">Total Unidades</span>
                        <span className="text-3xl font-black text-white">{totalUnits.toLocaleString()}</span>
                    </div>
                    <div className="p-4 bg-brand-indigo/20 rounded-2xl border border-brand-indigo/30">
                        <div className="flex items-start space-x-3">
                            <AlertCircle className="w-5 h-5 text-brand-indigo shrink-0 mt-0.5" />
                            <p className="text-[10px] font-bold text-brand-indigo uppercase tracking-widest leading-relaxed">
                                Esta es una solicitud <span className="text-white font-black">puramente técnica</span>. Los valores comerciales se asignarán en la <span className="text-white font-black">OC</span>.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div className="flex flex-col gap-3 mt-8">
                <button
                    onClick={generatePDF}
                    disabled={disabledPdf}
                    className="w-full py-4 bg-sidebar-popup hover:bg-sidebar-border text-white font-black rounded-xl shadow-[var(--shadow-lg)] transition-all uppercase tracking-widest text-[10px] flex items-center justify-center"
                >
                    <FileText className="w-4 h-4 mr-2" /> Descargar PDF
                </button>
                <button
                    onClick={onCreate}
                    className="w-full py-4 bg-brand-indigo hover:opacity-90 text-white font-black rounded-xl shadow-[var(--shadow-lg)] transition-all uppercase tracking-widest text-[10px] flex items-center justify-center"
                >
                    <Save className="w-4 h-4 mr-2" /> Crear Solicitud
                </button>
            </div>
        </div>
    );
}
