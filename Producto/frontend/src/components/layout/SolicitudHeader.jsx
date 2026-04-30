import { ChevronRight, Calculator, DollarSign, Save, Edit } from 'lucide-react';

export default function SolicitudHeader({ 
    formData, 
    onBack, 
    onSave, 
    readOnly = false,
    isEditing = false,
    setIsEditing
}) {
    return (
        <div className="max-w-5xl mx-auto mb-8 flex items-center justify-between">
            <div className="flex items-center gap-6">
                <button
                    onClick={onBack}
                    className="flex items-center text-gray-400 hover:text-gray-800 font-black text-[10px] uppercase tracking-widest transition-all"
                >
                    <ChevronRight className="w-4 h-4 mr-2 rotate-180" />
                    Volver
                </button>

                <div className="flex items-center gap-4">
                    <div className="p-3 bg-blue-100 rounded-xl">
                        {formData.tipo === 'cotizacion'
                            ? <Calculator className="w-5 h-5 text-blue-600" />
                            : <DollarSign className="w-5 h-5 text-blue-600" />}
                    </div>

                    <div>
                        <h2 className="text-2xl font-black text-gray-800 tracking-tight italic uppercase">
                            {formData.tipo === 'cotizacion'
                                ? 'Solicitud de Cotización'
                                : 'Costeo de Prenda'}: {formData.idSolicitud || 'NUEVO'}
                        </h2>
                    </div>
                </div>
            </div>

            <div className="flex items-center gap-6">
                {/* Botón Dinámico: Modificar vs Guardar */}
                {!isEditing && formData.idSolicitud ? (
                    <button
                        onClick={() => setIsEditing(true)}
                        className="flex items-center px-8 py-4 bg-gray-900 text-white rounded-[2rem] font-black text-[10px] uppercase tracking-widest shadow-xl shadow-gray-100 hover:bg-black hover:-translate-y-0.5 transition-all"
                    >
                        <Edit className="w-4 h-4 mr-2" />
                        Modificar SCOS
                    </button>
                ) : isEditing && (
                    <button
                        onClick={onSave}
                        className="flex items-center px-8 py-4 bg-blue-600 text-white rounded-[2rem] font-black text-[10px] uppercase tracking-widest shadow-xl shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                    >
                        <Save className="w-4 h-4 mr-2" />
                        Guardar Solicitud
                    </button>
                )}

                {formData.tipo !== 'SCOS' && (
                    <div className="bg-gray-900 px-8 py-4 rounded-[2rem] border-l-8 border-blue-600 shadow-xl flex flex-col items-end">
                        <span className="text-[10px] font-black text-blue-400 uppercase tracking-widest mb-1">Costo Total</span>
                        <div className="flex items-baseline gap-2">
                            <span className="text-3xl font-black text-white">
                                ${(formData.costoTotalCalculado || 0).toLocaleString('es-CL')}
                            </span>
                            <span className="text-blue-500 font-bold text-xs uppercase italic">CLP</span>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}