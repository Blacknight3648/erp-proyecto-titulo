import React from "react";
import {
    Users,
    DollarSign,
    TrendingUp,
    Percent,
    ClipboardList,
    Scale,
    Factory,
    ArrowUpRight,
    ArrowDownRight,
} from "lucide-react";

import {
    ResponsiveContainer,
    LineChart,
    Line,
    BarChart,
    Bar,
    AreaChart,
    Area,
} from "recharts";

import { Card, CardContent } from "../../ui/card";

// Componente para el círculo de progreso (Gauge circular) estilo azia
const CircularGauge = ({ percentage, colorClass, size = 70, strokeWidth = 6 }) => {
    const radius = (size - strokeWidth) / 2;
    const circumference = 2 * Math.PI * radius;
    const strokeDashoffset = circumference - (Math.min(100, Math.max(0, percentage)) / 100) * circumference;

    return (
        <div className="relative flex items-center justify-center" style={{ width: size, height: size }}>
            <svg className="absolute transform -rotate-90" width={size} height={size}>
                <circle
                  cx={size / 2}
                  cy={size / 2}
                  r={radius}
                  className="stroke-slate-200 dark:stroke-slate-800"
                  strokeWidth={strokeWidth}
                  fill="transparent"
                />
                <circle
                  cx={size / 2}
                  cy={size / 2}
                  r={radius}
                  className={`${colorClass} transition-all duration-700 ease-out`}
                  strokeWidth={strokeWidth}
                  strokeDasharray={circumference}
                  strokeDashoffset={strokeDashoffset}
                  strokeLinecap="round"
                  fill="transparent"
                />
            </svg>
            <span className="text-sm font-extrabold text-foreground">{Math.round(percentage)}%</span>
        </div>
    );
};

interface DashboardStats {
    ventas?: string;
    scos?: number;
    evn?: number;
    ops?: number;
    totalScos?: number;
    totalEvn?: number;
    totalOps?: number;
}

interface DashboardUser {
    name?: string;
    role?: string;
}

export default function AdminDashboard({
    user = {} as DashboardUser,
    stats = {} as DashboardStats,
    salesData = [],
    profitabilityData = [],
}: {
    user?: DashboardUser;
    stats?: DashboardStats;
    salesData?: any[];
    profitabilityData?: any[];
}) {
    const values = {
        ventas: stats.ventas ?? "$0",
        scos: stats.scos ?? 0,
        evn: stats.evn ?? 0,
        ops: stats.ops ?? 0,
        totalScos: stats.totalScos ?? 0,
        totalEvn: stats.totalEvn ?? 0,
        totalOps: stats.totalOps ?? 0,
    };

    // Calcular el promedio de rentabilidad real (del último mes disponible con ventas)
    const latestMonthWithVentas = [...profitabilityData].reverse().find(d => d.ventas > 0);
    const rentabilidadPromedio = latestMonthWithVentas ? latestMonthWithVentas.rentabilidad : 25;

    // Calcular eficiencia operacional (OPs en planta vs total de OPs)
    const totalOps = values.totalOps || 1;
    const activeOps = values.ops;
    const opsFinalizadas = Math.max(0, totalOps - activeOps);
    const eficienciaOperacional = Math.round((opsFinalizadas / totalOps) * 100) || 85;

    // Datos simulados estructurados para los mini sparklines de los KPIs apilados
    const scosSparkline = [
        { v: 1 }, { v: 3 }, { v: 2 }, { v: values.scos || 3 }
    ];
    const evnSparkline = [
        { v: 2 }, { v: 1 }, { v: 3 }, { v: values.evn || 2 }
    ];
    const opsSparkline = [
        { v: 3 }, { v: 2 }, { v: 4 }, { v: values.ops || 2 }
    ];
    const totalVentasSparkline = salesData.length > 0
        ? salesData.slice(-6).map(d => ({ v: d.ventas }))
        : [{ v: 10 }, { v: 30 }, { v: 20 }, { v: 45 }, { v: 60 }];

    return (
        <div className="space-y-6 animate-in fade-in duration-500">

            {/* ── FILA 1: TARJETAS DE RENDIMIENTO ANALÍTICO (ESTILO AZIA) ── */}
            <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
                
                {/* Tarjeta de Margen de Utilidad */}
                <Card className="p-6 bg-card border border-border flex flex-col justify-between hover:shadow-md transition-all duration-300">
                    <div className="space-y-2">
                        <div className="flex items-center justify-between">
                            <h3 className="text-[11px] font-black text-slate-400 uppercase tracking-widest">
                                Margen de Utilidad
                            </h3>
                            <span className="text-[10px] font-bold text-emerald-500 bg-emerald-500/10 border border-emerald-500/20 px-2 py-0.5 rounded-full flex items-center gap-0.5">
                                <ArrowUpRight className="w-3 h-3" /> Promedio
                            </span>
                        </div>
                        <p className="text-[12px] text-muted-foreground leading-snug">
                            Porcentaje neto de rentabilidad obtenido en los cierres de negocios más recientes.
                        </p>
                    </div>
                    <div className="flex items-center justify-center py-4">
                        <CircularGauge percentage={rentabilidadPromedio} colorClass="stroke-brand-indigo" size={110} strokeWidth={8} />
                    </div>
                </Card>

                {/* Tarjeta de Eficiencia Operacional */}
                <Card className="p-6 bg-card border border-border flex flex-col justify-between hover:shadow-md transition-all duration-300">
                    <div className="space-y-2">
                        <div className="flex items-center justify-between">
                            <h3 className="text-[11px] font-black text-slate-400 uppercase tracking-widest">
                                Eficiencia Operativa
                            </h3>
                            <span className="text-[10px] font-bold text-blue-500 bg-blue-500/10 border border-blue-500/20 px-2 py-0.5 rounded-full flex items-center gap-0.5">
                                <ArrowUpRight className="w-3 h-3" /> Planta
                            </span>
                        </div>
                        <p className="text-[12px] text-muted-foreground leading-snug">
                            Tasa de órdenes de producción completadas con éxito frente al total emitido.
                        </p>
                    </div>
                    <div className="flex items-center justify-center py-4">
                        <CircularGauge percentage={eficienciaOperacional} colorClass="stroke-emerald-500" size={110} strokeWidth={8} />
                    </div>
                </Card>

                {/* Tarjeta de Balance del Mes (Diseño Tarjeta de Crédito Premium / VISA) */}
                <Card className="relative overflow-hidden bg-gradient-to-br from-[#0c1322] via-[#1a2333] to-[#0c1322] border border-slate-800 text-white p-6 flex flex-col justify-between shadow-xl min-h-[220px]">
                    {/* Efecto de brillo de fondo */}
                    <div className="absolute top-0 right-0 w-32 h-32 bg-blue-600/10 rounded-full blur-3xl pointer-events-none" />
                    
                    {/* Contenido Superior */}
                    <div className="z-10 flex items-start justify-between">
                        <div className="space-y-1">
                            <p className="text-[10px] font-black text-slate-400 uppercase tracking-wider">
                                Facturación del Mes
                            </p>
                            <h2 className="text-3xl font-extrabold tracking-tight text-white drop-shadow-md">
                                {values.ventas}
                            </h2>
                        </div>
                        <div className="flex flex-col items-end">
                            <span className="text-[11px] font-black italic tracking-widest text-slate-500">ANTUAN S.A.</span>
                            <span className="w-8 h-5 bg-white/5 border border-white/10 rounded-md mt-1 flex items-center justify-center">
                                <span className="w-4 h-3 bg-amber-500/70 rounded-sm" />
                            </span>
                        </div>
                    </div>

                    {/* Sparkline de Ventas de Fondo */}
                    <div className="absolute inset-x-0 bottom-16 h-12 z-0 opacity-40">
                        <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
                            <AreaChart data={salesData.slice(-6)}>
                                <defs>
                                    <linearGradient id="visaChartGrad" x1="0" y1="0" x2="0" y2="1">
                                        <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.2}/>
                                        <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
                                    </linearGradient>
                                </defs>
                                <Area
                                    type="monotone"
                                    dataKey="ventas"
                                    stroke="#3b82f6"
                                    strokeWidth={2}
                                    fillOpacity={1}
                                    fill="url(#visaChartGrad)"
                                />
                            </AreaChart>
                        </ResponsiveContainer>
                    </div>

                    {/* Contenido Inferior */}
                    <div className="z-10 flex items-end justify-between pt-4 border-t border-slate-800">
                        <div>
                            <p className="text-[8px] font-bold uppercase tracking-widest text-slate-400">Propietario de Cuenta</p>
                            <p className="text-xs font-semibold tracking-wide text-white mt-0.5">
                                {user.name || "Administrador del Sistema"}
                            </p>
                        </div>
                        <div className="text-right">
                            <p className="text-[8px] font-bold uppercase tracking-widest text-slate-400">Rol Operativo</p>
                            <p className="text-[10px] font-semibold text-blue-400 bg-blue-500/10 border border-blue-500/20 px-2 py-0.5 rounded mt-0.5">
                                {user.role || "ADMIN"}
                            </p>
                        </div>
                    </div>
                </Card>
            </div>

            {/* ── FILA 2: RATIOS DE CONVERSIÓN Y KPIS APILADOS (ESTILO AZIA) ── */}
            <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
                
                {/* Ratios e Indicadores del Flujo (Columna Izquierda de Ratios) */}
                <Card className="lg:col-span-2 p-6 bg-card border border-border flex flex-col justify-between hover:shadow-md transition-all duration-300">
                    <div>
                        <h3 className="text-sm font-black text-foreground uppercase tracking-tight">
                            Ratios de Conversión Operacional
                        </h3>
                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest mt-0.5">
                            Eficiencia en el flujo del embudo comercial e industrial
                        </p>
                    </div>

                    <div className="space-y-6 py-4 my-auto">
                        {/* Conversión SCOS a EVN */}
                        <div className="space-y-2">
                            <div className="flex items-center justify-between text-sm">
                                <span className="font-semibold text-foreground">Conversión de Solicitudes (SCOS a EVN)</span>
                                <span className="font-extrabold text-brand-indigo">72%</span>
                            </div>
                            <div className="w-full bg-slate-100 dark:bg-slate-800 h-2.5 rounded-full overflow-hidden">
                                <div className="bg-gradient-to-r from-blue-500 to-indigo-600 h-full rounded-full" style={{ width: '72%' }} />
                            </div>
                            <div className="flex justify-between text-[10px] text-muted-foreground">
                                <span>Total Solicitudes: {values.totalScos}</span>
                                <span>Meta de Conversión: &gt; 70%</span>
                            </div>
                        </div>

                        {/* Conversión EVN a OP */}
                        <div className="space-y-2">
                            <div className="flex items-center justify-between text-sm">
                                <span className="font-semibold text-foreground">Aprobación de Órdenes (EVN a OP)</span>
                                <span className="font-extrabold text-emerald-500">65%</span>
                            </div>
                            <div className="w-full bg-slate-100 dark:bg-slate-800 h-2.5 rounded-full overflow-hidden">
                                <div className="bg-gradient-to-r from-emerald-400 to-teal-500 h-full rounded-full" style={{ width: '65%' }} />
                            </div>
                            <div className="flex justify-between text-[10px] text-muted-foreground">
                                <span>Total Evaluaciones: {values.totalEvn}</span>
                                <span>Meta de Cierre: &gt; 60%</span>
                            </div>
                        </div>
                    </div>
                </Card>

                {/* KPIs Apilados Verticales con Mini Sparklines (Columna Derecha) */}
                <div className="flex flex-col gap-4">
                    
                    {/* SCOS Card */}
                    <Card className="p-4 bg-card border border-border flex items-center justify-between hover:shadow-md transition-all duration-300">
                        <div className="space-y-1">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">SCOS Pendientes</p>
                            <div className="flex items-baseline gap-2">
                                <h3 className="text-2xl font-black text-foreground">{values.scos}</h3>
                                <span className="text-[10px] text-amber-500 font-bold flex items-center gap-0.5">
                                    <ArrowUpRight className="w-2.5 h-2.5" /> Requiere revisión
                                </span>
                            </div>
                        </div>
                        <div className="w-20 h-10 opacity-70">
                            <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
                                <BarChart data={scosSparkline}>
                                    <Bar dataKey="v" fill="var(--warning)" radius={[2, 2, 0, 0]} />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    </Card>

                    {/* EVN Card */}
                    <Card className="p-4 bg-card border border-border flex items-center justify-between hover:shadow-md transition-all duration-300">
                        <div className="space-y-1">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">EVN en Evaluación</p>
                            <div className="flex items-baseline gap-2">
                                <h3 className="text-2xl font-black text-foreground">{values.evn}</h3>
                                <span className="text-[10px] text-brand-indigo font-bold flex items-center gap-0.5">
                                    <ArrowUpRight className="w-2.5 h-2.5" /> En evaluación
                                </span>
                            </div>
                        </div>
                        <div className="w-20 h-10 opacity-70">
                            <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
                                <BarChart data={evnSparkline}>
                                    <Bar dataKey="v" fill="var(--secondary-foreground)" radius={[2, 2, 0, 0]} />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    </Card>

                    {/* OPs Card */}
                    <Card className="p-4 bg-card border border-border flex items-center justify-between hover:shadow-md transition-all duration-300">
                        <div className="space-y-1">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">OPs en Planta</p>
                            <div className="flex items-baseline gap-2">
                                <h3 className="text-2xl font-black text-foreground">{values.ops}</h3>
                                <span className="text-[10px] text-emerald-500 font-bold flex items-center gap-0.5">
                                    <ArrowUpRight className="w-2.5 h-2.5" /> En confección
                                </span>
                            </div>
                        </div>
                        <div className="w-20 h-10 opacity-70">
                            <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
                                <BarChart data={opsSparkline}>
                                    <Bar dataKey="v" fill="var(--success)" radius={[2, 2, 0, 0]} />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    </Card>

                    {/* Ventas Sparkline Card */}
                    <Card className="p-4 bg-card border border-border flex items-center justify-between hover:shadow-md transition-all duration-300">
                        <div className="space-y-1">
                            <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Histórico Ventas</p>
                            <div className="flex items-baseline gap-2">
                                <h3 className="text-2xl font-black text-foreground">12m</h3>
                                <span className="text-[10px] text-blue-500 font-bold flex items-center gap-0.5">
                                    <ArrowUpRight className="w-2.5 h-2.5" /> Tendencia estable
                                </span>
                            </div>
                        </div>
                        <div className="w-20 h-10 opacity-75">
                            <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
                                <LineChart data={totalVentasSparkline}>
                                    <Line type="monotone" dataKey="v" stroke="var(--info)" strokeWidth={2} dot={false} />
                                </LineChart>
                            </ResponsiveContainer>
                        </div>
                    </Card>

                </div>

            </div>

        </div>
    );
}