import {
    AlertTriangle,
    Clock,
    ShieldAlert,
    ArrowRight,
    CheckCircle2
} from 'lucide-react';
import { useDataLookup } from '../../../hooks/useDataLookup';

export default function AlertasAtrasos() {
    const { getAlertasReglaDelTres } = useDataLookup();

    // Alertas Reales (Calculadas)
    const alertasRegla3 = getAlertasReglaDelTres();

    // Datos Mock (Placeholder hasta ticket 2.4)
    const alertasVIP = [
        { id: 'OP-001', cliente: 'Orden de Malta', atraso: 5, sla: '2d', estado: 'Crítico' }
    ];

    const talleresAtrasados = [
        { taller: 'Maquila Centro', op: 'OP-002', atraso: '5/7 días', impacto: 'Alto' }
    ];

    const logoAtrasados = [
        { taller: 'Taller Logo Premium', op: 'OP-003', atraso: '2/3 días', impacto: 'Medio' }
    ];

    return (
        <div className="max-w-7xl mx-auto space-y-8 animate-in fade-in duration-700 pb-12">

            <div className="flex items-center space-x-4 mb-8">
                <div className="bg-red-100 p-3 rounded-2xl">
                    <ShieldAlert className="w-8 h-8 text-red-600" />
                </div>
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight">Centro de Alertas</h1>
                    <p className="text-gray-500 font-medium">Monitoreo de SLAs y Reglas de Negocio Críticas</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">

                {/* 1. REGLA DEL 3 (CRÍTICA) */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-red-500 flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Regla de 3 días</h3>
                            <span className="bg-red-100 text-red-600 text-[10px] font-black px-2 py-1 rounded-full">{alertasRegla3.length} ACTIVAS</span>
                        </div>
                        <div className="space-y-4">
                            {alertasRegla3.length > 0 ? (
                                alertasRegla3.map(alert => (
                                    <div key={alert.numeroOC || alert.idOC || Math.random()} className="bg-red-50 p-3 rounded-xl border border-red-100">
                                        <div className="flex justify-between items-center mb-1">
                                            <span className="text-xs font-black text-red-700">{alert.numeroOC || alert.idOC}</span>
                                            <Clock className="w-3 h-3 text-red-400" />
                                        </div>
                                        <p className="text-[10px] text-red-500 font-bold uppercase">{alert.proveedor}</p>
                                        <div className="mt-2 text-[9px] font-black text-red-400 bg-white/50 px-2 py-1 rounded-lg inline-block">
                                            {alert.estado} &gt; 3 Días
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="text-center py-8 opacity-50">
                                    <CheckCircle2 className="w-8 h-8 mx-auto text-green-500 mb-2" />
                                    <p className="text-xs font-bold text-gray-400">Todo en orden</p>
                                </div>
                            )}
                        </div>
                    </div>
                    <button className="mt-6 w-full py-3 bg-red-600 hover:bg-red-700 text-white rounded-xl text-[10px] font-black uppercase tracking-widest transition-colors">
                        Gestionar Prioridad
                    </button>
                </div>

                {/* 2. CLIENTE VIP (CRÍTICO) */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-red-500 flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Clientes VIP</h3>
                            <span className="bg-red-100 text-red-600 text-[10px] font-black px-2 py-1 rounded-full">{alertasVIP.length} CRÍTICOS</span>
                        </div>
                        <div className="space-y-4">
                            {alertasVIP.map((vip, idx) => (
                                <div key={idx} className="bg-gradient-to-br from-amber-50 to-red-50 p-3 rounded-xl border border-red-100 relative overflow-hidden">
                                    <div className="absolute top-0 right-0 w-8 h-8 bg-red-500/10 rounded-bl-xl"></div>
                                    <h4 className="text-xs font-black text-gray-800 mb-1">{vip.cliente}</h4>
                                    <div className="flex justify-between items-center">
                                        <span className="text-[10px] font-bold text-gray-500">{vip.id}</span>
                                        <span className="text-[10px] font-black text-red-600 bg-white px-2 py-0.5 rounded shadow-sm">
                                            +{vip.atraso} Días
                                        </span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* 3. TALLER EXTERNO */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-orange-500 flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Taller Externo</h3>
                            <span className="bg-orange-100 text-orange-600 text-[10px] font-black px-2 py-1 rounded-full">ATRASO</span>
                        </div>
                        {talleresAtrasados.map((taller, idx) => (
                            <div key={idx} className="bg-orange-50 p-3 rounded-xl border border-orange-100 mb-3">
                                <h4 className="text-xs font-black text-gray-700">{taller.taller}</h4>
                                <div className="flex items-center space-x-2 mt-2">
                                    <span className="text-[10px] font-bold text-gray-500">{taller.op}</span>
                                    <span className="w-1 h-1 bg-gray-300 rounded-full"></span>
                                    <span className="text-[10px] font-black text-orange-500">{taller.atraso}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* 4. LOGO EXTERNO */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-orange-500 flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Logo Externo</h3>
                            <span className="bg-orange-100 text-orange-600 text-[10px] font-black px-2 py-1 rounded-full">RIESGO</span>
                        </div>
                        {logoAtrasados.map((item, idx) => (
                            <div key={idx} className="bg-orange-50 p-3 rounded-xl border border-orange-100 mb-3">
                                <h4 className="text-xs font-black text-gray-700">{item.taller}</h4>
                                <div className="flex items-center space-x-2 mt-2">
                                    <span className="text-[10px] font-bold text-gray-500">{item.op}</span>
                                    <span className="w-1 h-1 bg-gray-300 rounded-full"></span>
                                    <span className="text-[10px] font-black text-orange-500">{item.atraso}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

            </div>
        </div>
    );
}
