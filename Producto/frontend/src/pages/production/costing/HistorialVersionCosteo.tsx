import React from 'react';
import { 
    Clock, 
    CheckCircle2, 
    XCircle, 
    FileText, 
    User, 
    ArrowRight,
    CornerDownRight
} from 'lucide-react';

// Mapeo de estilos específicos para las acciones del historial
const CONFIG_ACCION = {
    CREACION: {
        label: 'Creado',
        colorText: 'text-gray-600',
        colorBg: 'bg-gray-100',
        icon: FileText
    },
    MODIFICACION: {
        label: 'Modificado',
        colorText: 'text-indigo-600',
        colorBg: 'bg-indigo-50',
        icon: Clock
    },
    APROBACION: {
        label: 'Aprobado',
        colorText: 'text-emerald-700',
        colorBg: 'bg-emerald-50',
        icon: CheckCircle2
    },
    RECHAZO: {
        label: 'Rechazado',
        colorText: 'text-red-600',
        colorBg: 'bg-red-50',
        icon: XCircle
    }
};

export default function HistorialVersionCosteo({ 
    historial = [], 
    onClose,
    numeroCosteo 
}) {
    return (
        <div className="bg-white p-6 rounded-[2.5rem] border-2 border-gray-100 shadow-xl max-w-md w-full animate-in slide-in-from-right duration-300">
            {/* Header del Historial */}
            <div className="flex items-center justify-between border-b border-gray-100 pb-4 mb-6">
                <div>
                    <h3 className="text-md font-black text-gray-800 uppercase tracking-tight flex items-center gap-2">
                        <Clock className="w-5 h-5 text-indigo-500" />
                        Historial de Versiones
                    </h3>
                    <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mt-0.5">
                        Registro OP: {numeroCosteo || 'SCOS-XXXXXX'}
                    </p>
                </div>
                {onClose && (
                    <button 
                        onClick={onClose}
                        className="p-2 hover:bg-gray-50 rounded-xl text-gray-400 hover:text-gray-600 transition-colors"
                    >
                        ✕
                    </button>
                )}
            </div>

            {/* Lista / Timeline */}
            <div className="relative border-l-2 border-gray-100 ml-4 pl-6 space-y-6">
                {historial.map((item, index) => {
                    const config = CONFIG_ACCION[item.accion] || CONFIG_ACCION.MODIFICACION;
                    const IconoAccion = config.icon;

                    return (
                        <div key={item.id || index} className="relative group">
                            {/* Nodo de la línea de tiempo */}
                            <div className={`absolute -left-[35px] top-0 w-6 h-6 rounded-lg ${config.colorBg} flex items-center justify-center border-2 border-white shadow-sm transition-transform group-hover:scale-110`}>
                                <IconoAccion className={`w-3 h-3 ${config.colorText}`} />
                            </div>

                            {/* Contenido del hito */}
                            <div className="space-y-1">
                                <div className="flex items-center justify-between gap-2">
                                    <div className="flex items-center gap-2">
                                        <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest ${config.colorBg} ${config.colorText}`}>
                                            {config.label}
                                        </span>
                                        {item.version && (
                                            <span className="px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest bg-indigo-50 text-indigo-500">
                                                v{item.version}
                                            </span>
                                        )}
                                    </div>
                                    <span className="text-[9px] font-bold text-gray-400 italic">
                                        {item.fechaFormateada || 'Hace un momento'}
                                    </span>
                                </div>

                                {/* Cambio de valor principal si aplica (ej: de $0 a $150.000) */}
                                {item.costoTotal != null && (
                                    <div className="text-sm font-black text-gray-800 tracking-tight flex items-center gap-1.5 py-0.5">
                                        {item.costoAnterior != null && (
                                            <>
                                                <span className="text-gray-300 line-through font-normal text-xs">
                                                    ${item.costoAnterior.toLocaleString('es-CL')}
                                                </span>
                                                <ArrowRight className="w-3 h-3 text-gray-400" />
                                            </>
                                        )}
                                        <span>${item.costoTotal.toLocaleString('es-CL')}</span>
                                    </div>
                                )}

                                {/* Usuario responsable */}
                                <div className="flex items-center gap-1 text-[10px] font-bold text-gray-500 uppercase tracking-wide">
                                    <User className="w-3 h-3 text-gray-400" />
                                    <span>{item.usuario || 'Sistema'}</span>
                                </div>

                                {/* Motivo de rechazo o comentarios si existen */}
                                {item.motivo && (
                                    <div className="mt-2 p-2.5 bg-red-50/50 border border-red-100/50 rounded-xl flex items-start gap-1.5">
                                        <CornerDownRight className="w-3 h-3 text-red-400 shrink-0 mt-0.5" />
                                        <p className="text-[11px] font-bold text-red-700 normal-case leading-relaxed">
                                            <span className="uppercase text-[9px] font-black block tracking-wider mb-0.5 text-red-500">Motivo:</span>
                                            {item.motivo}
                                        </p>
                                    </div>
                                )}
                            </div>
                        </div>
                    );
                })}

                {/* Estado vacío por si no hay historial todavía */}
                {historial.length === 0 && (
                    <div className="py-4 text-center -ml-6">
                        <p className="text-[10px] font-black text-gray-300 uppercase tracking-widest italic">
                            No hay modificaciones registradas
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
}