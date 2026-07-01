import { ChevronLeft, Save, Edit } from 'lucide-react';

const SUBTITULOS = {
    cotizacion: 'Defina materiales y especificaciones para generar una cotización al cliente.',
    SCOS: 'Registre los materiales, especificaciones y estructura de costos de la prenda.',
};

export default function SolicitudHeader({
    formData,
    onBack,
    onSave,
    isEditing = false,
    setIsEditing
}) {
    const tipo = formData.tipo;
    const isNew = !formData.idSolicitud;
    const titulo = tipo === 'cotizacion' ? 'Solicitud de Cotización' : 'Costeo de Prenda';
    const subtitulo = SUBTITULOS[tipo] ?? SUBTITULOS['SCOS'];
    const etiqueta = isNew ? 'Nueva solicitud' : `ID ${formData.idSolicitud}`;

    return (
        <div className="flex items-start justify-between gap-6">
            <div className="flex items-start gap-5">
                <button
                    onClick={onBack}
                    className="mt-1.5 flex items-center gap-1 text-slate-400 hover:text-slate-700 font-bold text-[10px] uppercase tracking-widest transition-colors"
                >
                    <ChevronLeft className="w-3.5 h-3.5" />
                    Volver
                </button>

                <div className="w-px self-stretch bg-slate-200" />

                <div>
                    <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-0.5">
                        {etiqueta}
                    </p>
                    <h1 className="text-2xl font-black text-slate-900 tracking-tight leading-tight">
                        {titulo}
                    </h1>
                    <p className="text-xs text-slate-500 mt-1 max-w-md leading-relaxed">
                        {subtitulo}
                    </p>
                </div>
            </div>

            <div className="flex items-center gap-4 shrink-0">
                {!isEditing && formData.idSolicitud ? (
                    <button
                        onClick={() => setIsEditing(true)}
                        className="flex items-center gap-2 px-6 py-3 bg-slate-900 text-white rounded-2xl font-bold text-xs uppercase tracking-widest shadow-lg hover:bg-black hover:-translate-y-0.5 transition-all"
                    >
                        <Edit className="w-3.5 h-3.5" />
                        Modificar
                    </button>
                ) : isEditing && (
                    <button
                        onClick={onSave}
                        className="flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-2xl font-bold text-xs uppercase tracking-widest shadow-lg shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                    >
                        <Save className="w-3.5 h-3.5" />
                        Guardar Solicitud
                    </button>
                )}

                {tipo !== 'SCOS' && (
                    <div className="bg-slate-900 px-6 py-3 rounded-2xl border-l-4 border-blue-600 shadow-lg flex flex-col items-end">
                        <span className="text-[9px] font-bold text-blue-400 uppercase tracking-widest mb-0.5">
                            Costo Total
                        </span>
                        <div className="flex items-baseline gap-1.5">
                            <span className="text-2xl font-black text-white">
                                ${(formData.costoTotalCalculado || 0).toLocaleString('es-CL')}
                            </span>
                            <span className="text-blue-500 font-bold text-[10px] uppercase italic">CLP</span>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
