import { AlertCircle, FileText, Save } from 'lucide-react';

export default function SCSummaryCard({ totalUnits, isManual, generatePDF, disabledPdf, onCreate }) {
    return (
        <div className="bg-slate-900 text-white p-6 rounded-[2rem] flex flex-col justify-between">
            <div>
                <h3 className="text-xs font-black uppercase tracking-widest text-indigo-400 mb-6">Resumen Operativo</h3>
                <div className="space-y-6">
                    <div className="p-4 bg-white/5 rounded-2xl border border-white/10 text-center">
                        <span className="text-[10px] font-bold text-gray-400 uppercase tracking-widest block mb-2">Total Unidades</span>
                        <span className="text-3xl font-black text-white">{totalUnits.toLocaleString()}</span>
                    </div>
                    <div className="p-4 bg-indigo-600/20 rounded-2xl border border-indigo-500/30">
                        <div className="flex items-start space-x-3">
                            <AlertCircle className="w-5 h-5 text-indigo-400 shrink-0 mt-0.5" />
                            <p className="text-[10px] font-bold text-indigo-300 uppercase tracking-widest leading-relaxed">
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
                    className="w-full py-4 bg-gray-800 hover:bg-gray-700 text-white font-black rounded-xl shadow-lg transition-all uppercase tracking-widest text-[10px] flex items-center justify-center"
                >
                    <FileText className="w-4 h-4 mr-2" /> Descargar PDF
                </button>
                <button
                    onClick={onCreate}
                    className="w-full py-4 bg-indigo-600 hover:bg-indigo-500 text-white font-black rounded-xl shadow-lg transition-all uppercase tracking-widest text-[10px] flex items-center justify-center"
                >
                    <Save className="w-4 h-4 mr-2" /> Crear Solicitud
                </button>
            </div>
        </div>
    );
}
