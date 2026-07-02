import {
    AlertTriangle,
    Clock,
    ShieldAlert,
    ArrowRight,
    CheckCircle2
} from 'lucide-react';
import { useDataLookup } from '../../hooks/useDataLookup';

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
                <div className="bg-destructive/10 p-3 rounded-2xl">
                    <ShieldAlert className="w-8 h-8 text-destructive" />
                </div>
                <div>
                    <h1 className="text-3xl font-black text-foreground tracking-tight">Centro de Alertas</h1>
                    <p className="text-muted-foreground font-medium">Monitoreo de SLAs y Reglas de Negocio Críticas</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">

                {/* 1. REGLA DEL 3 (CRÍTICA) */}
                <div className="bg-card p-6 rounded-[2rem] shadow-sm border-t-4 border-destructive flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Regla de 3 días</h3>
                            <span className="bg-destructive/10 text-destructive text-[10px] font-black px-2 py-1 rounded-full">{alertasRegla3.length} ACTIVAS</span>
                        </div>
                        <div className="space-y-4">
                            {alertasRegla3.length > 0 ? (
                                alertasRegla3.map(alert => (
                                    <div key={alert.numeroOC || alert.idOC || Math.random()} className="bg-destructive/5 p-3 rounded-xl border border-destructive/20">
                                        <div className="flex justify-between items-center mb-1">
                                            <span className="text-xs font-black text-destructive">{alert.numeroOC || alert.idOC}</span>
                                            <Clock className="w-3 h-3 text-destructive/60" />
                                        </div>
                                        <p className="text-[10px] text-destructive/80 font-bold uppercase">{alert.proveedor}</p>
                                        <div className="mt-2 text-[9px] font-black text-destructive/70 bg-card/50 px-2 py-1 rounded-lg inline-block">
                                            {alert.estado} &gt; 3 Días
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="text-center py-8 opacity-50">
                                    <CheckCircle2 className="w-8 h-8 mx-auto text-success mb-2" />
                                    <p className="text-xs font-bold text-muted-foreground">Todo en orden</p>
                                </div>
                            )}
                        </div>
                    </div>
                    <button className="mt-6 w-full py-3 bg-destructive hover:bg-destructive/90 text-white rounded-xl text-[10px] font-black uppercase tracking-widest transition-colors">
                        Gestionar Prioridad
                    </button>
                </div>

                {/* 2. CLIENTE VIP (CRÍTICO) */}
                <div className="bg-card p-6 rounded-[2rem] shadow-sm border-t-4 border-destructive flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Clientes VIP</h3>
                            <span className="bg-destructive/10 text-destructive text-[10px] font-black px-2 py-1 rounded-full">{alertasVIP.length} CRÍTICOS</span>
                        </div>
                        <div className="space-y-4">
                            {alertasVIP.map((vip, idx) => (
                                <div key={idx} className="bg-gradient-to-br from-warning/10 to-destructive/10 p-3 rounded-xl border border-destructive/20 relative overflow-hidden">
                                    <div className="absolute top-0 right-0 w-8 h-8 bg-destructive/10 rounded-bl-xl"></div>
                                    <h4 className="text-xs font-black text-foreground mb-1">{vip.cliente}</h4>
                                    <div className="flex justify-between items-center">
                                        <span className="text-[10px] font-bold text-muted-foreground">{vip.id}</span>
                                        <span className="text-[10px] font-black text-destructive bg-card px-2 py-0.5 rounded shadow-sm">
                                            +{vip.atraso} Días
                                        </span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* 3. TALLER EXTERNO */}
                <div className="bg-card p-6 rounded-[2rem] shadow-sm border-t-4 border-warning flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Taller Externo</h3>
                            <span className="bg-warning/10 text-warning text-[10px] font-black px-2 py-1 rounded-full">ATRASO</span>
                        </div>
                        {talleresAtrasados.map((taller, idx) => (
                            <div key={idx} className="bg-warning/5 p-3 rounded-xl border border-warning/20 mb-3">
                                <h4 className="text-xs font-black text-foreground">{taller.taller}</h4>
                                <div className="flex items-center space-x-2 mt-2">
                                    <span className="text-[10px] font-bold text-muted-foreground">{taller.op}</span>
                                    <span className="w-1 h-1 bg-muted-foreground/40 rounded-full"></span>
                                    <span className="text-[10px] font-black text-warning">{taller.atraso}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* 4. LOGO EXTERNO */}
                <div className="bg-card p-6 rounded-[2rem] shadow-sm border-t-4 border-warning flex flex-col justify-between group hover:shadow-lg transition-all">
                    <div>
                        <div className="flex justify-between items-start mb-4">
                            <h3 className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Logo Externo</h3>
                            <span className="bg-warning/10 text-warning text-[10px] font-black px-2 py-1 rounded-full">RIESGO</span>
                        </div>
                        {logoAtrasados.map((item, idx) => (
                            <div key={idx} className="bg-warning/5 p-3 rounded-xl border border-warning/20 mb-3">
                                <h4 className="text-xs font-black text-foreground">{item.taller}</h4>
                                <div className="flex items-center space-x-2 mt-2">
                                    <span className="text-[10px] font-bold text-muted-foreground">{item.op}</span>
                                    <span className="w-1 h-1 bg-muted-foreground/40 rounded-full"></span>
                                    <span className="text-[10px] font-black text-warning">{item.atraso}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

            </div>
        </div>
    );
}
