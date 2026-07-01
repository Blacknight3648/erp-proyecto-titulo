import React from "react";
import {
    Users,
    DollarSign,
    TrendingUp,
    Percent,
    ClipboardList,
    Scale,
    Factory,
} from "lucide-react";

import {
    ResponsiveContainer,
    LineChart,
    Line,
    BarChart,
    Bar,
    CartesianGrid,
    Tooltip,
    XAxis,
    YAxis,
} from "recharts";

import { Card, CardContent } from "../../ui/card";

const KPI_CONFIG = [
    {
        id: "ventas",
        label: "Ventas del Mes",
        icon: DollarSign,
        colorText: "text-blue-600 dark:text-blue-400",
        colorBg: "bg-blue-50 dark:bg-blue-950/40",
        ringColor: "hover:border-blue-500 hover:shadow-sm",
    },
    {
        id: "scos",
        label: "SCOS Pendientes",
        icon: ClipboardList,
        colorText: "text-amber-600 dark:text-amber-400",
        colorBg: "bg-amber-50 dark:bg-amber-950/40",
        ringColor: "hover:border-amber-500 hover:shadow-sm",
    },
    {
        id: "evn",
        label: "EVN en Evaluación",
        icon: Scale,
        colorText: "text-indigo-600 dark:text-indigo-400",
        colorBg: "bg-indigo-50 dark:bg-indigo-950/40",
        ringColor: "hover:border-indigo-500 hover:shadow-sm",
    },
    {
        id: "ops",
        label: "OPs en Planta",
        icon: Factory,
        colorText: "text-emerald-600 dark:text-emerald-400",
        colorBg: "bg-emerald-50 dark:bg-emerald-950/40",
        ringColor: "hover:border-emerald-500 hover:shadow-sm",
    },
];

// Tooltip Personalizado para mantener el estilo de la app
const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
        return (
            <div className="bg-card text-foreground p-4 rounded-2xl shadow-xl border border-border animate-in fade-in zoom-in-95 duration-150">
                <p className="text-[10px] font-black uppercase tracking-widest text-muted-foreground mb-1">{label}</p>
                <p className="text-sm font-black italic">
                    {payload[0].name === "ventas" ? "$" : ""}
                    {payload[0].value.toLocaleString("es-CL")}
                    {payload[0].name === "rentabilidad" ? "%" : ""}
                </p>
            </div>
        );
    }
    return null;
};

export default function AdminDashboard({
    stats = {},
    salesData = [],
    profitabilityData = [],
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

    return (
        <div className="max-w-full p-2 space-y-8 animate-in fade-in duration-700">

            {/* KPIs Cards */}
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-4">
                {KPI_CONFIG.map((kpi) => {
                    const Icon = kpi.icon;

                    return (
                        <Card
                            key={kpi.id}
                            className={`group p-6 border border-border bg-card transition-all relative overflow-hidden flex flex-col hover:shadow-lg ${kpi.ringColor}`}
                        >
                            <CardContent className="p-0 flex items-center justify-between">
                                <div className="flex flex-col">
                                    <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-1">
                                        {kpi.label}
                                    </p>
                                    <h2 className="text-2xl font-black text-foreground tracking-tight">
                                        {values[kpi.id]}
                                    </h2>
                                </div>
                                <div className={`w-12 h-12 rounded-2xl ${kpi.colorBg} flex items-center justify-center shrink-0 transition-transform group-hover:scale-110 duration-300`}>
                                    <Icon className={`h-5 w-5 ${kpi.colorText}`} />
                                </div>
                            </CardContent>
                        </Card>
                    );
                })}
            </div>

            {/* Gráficos */}
            <div className="grid gap-6 lg:grid-cols-2">

                {/* Grafico Ventas */}
                <Card className="p-6 bg-card border border-border">
                    <div className="mb-6">
                        <h3 className="text-sm font-black text-foreground uppercase tracking-tight">
                            Ventas Mensuales
                        </h3>
                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest mt-0.5">
                            Evolución de ventas por período
                        </p>
                    </div>

                    <CardContent className="p-0">
                        <ResponsiveContainer width="100%" height={320}>
                            <LineChart data={salesData} margin={{ top: 10, right: 10, left: -10, bottom: 0 }}>
                                <defs>
                                    <linearGradient id="colorVentas" x1="0" y1="0" x2="0" y2="1">
                                        <stop offset="5%" stopColor="#2563eb" stopOpacity={0.1}/>
                                        <stop offset="95%" stopColor="#2563eb" stopOpacity={0}/>
                                    </linearGradient>
                                </defs>
                                <CartesianGrid strokeDasharray="0" stroke="var(--border)" vertical={false} />
                                <XAxis 
                                    dataKey="mes" 
                                    axisLine={false} 
                                    tickLine={false} 
                                    tick={{ fill: 'var(--muted-foreground)', fontSize: 10, fontWeight: 700 }}
                                />
                                <YAxis 
                                    axisLine={false} 
                                    tickLine={false} 
                                    tick={{ fill: 'var(--muted-foreground)', fontSize: 10, fontWeight: 700 }}
                                />
                                <Tooltip content={<CustomTooltip />} cursor={{ stroke: 'var(--border)', strokeWidth: 1 }} />
                                <Line
                                    type="monotone"
                                    dataKey="ventas"
                                    name="ventas"
                                    stroke="#2563eb"
                                    strokeWidth={4}
                                    dot={{ r: 4, strokeWidth: 2, fill: "#fff" }}
                                    activeDot={{ r: 6, strokeWidth: 0 }}
                                />
                            </LineChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>

                {/* Grafico Rentabilidad */}
                <Card className="p-6 bg-card border border-border">
                    <div className="mb-6">
                        <h3 className="text-sm font-black text-foreground uppercase tracking-tight">
                            Rentabilidad %
                        </h3>
                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest mt-0.5">
                            Margen obtenido por período
                        </p>
                    </div>

                    <CardContent className="p-0">
                        <ResponsiveContainer width="100%" height={320}>
                            <BarChart data={profitabilityData} margin={{ top: 10, right: 10, left: -10, bottom: 0 }}>
                                <CartesianGrid strokeDasharray="0" stroke="var(--border)" vertical={false} />
                                <XAxis 
                                    dataKey="mes" 
                                    axisLine={false} 
                                    tickLine={false} 
                                    tick={{ fill: 'var(--muted-foreground)', fontSize: 10, fontWeight: 700 }}
                                />
                                <YAxis 
                                    axisLine={false} 
                                    tickLine={false} 
                                    tick={{ fill: 'var(--muted-foreground)', fontSize: 10, fontWeight: 700 }}
                                />
                                <Tooltip content={<CustomTooltip />} cursor={{ fill: 'var(--muted)' }} />
                                <Bar
                                    dataKey="rentabilidad"
                                    name="rentabilidad"
                                    fill="#10b981"
                                    maxBarSize={45}
                                    radius={[8, 8, 0, 0]}
                                />
                            </BarChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>
            </div>

            {/* Resumen inferior */}
            <div className="grid gap-6 grid-cols-1 lg:grid-cols-3">
                <Card className="p-6 bg-card border border-border flex flex-col justify-center">
                    <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-1">Total SCOS Registradas</p>
                    <p className="text-3xl font-black text-blue-600 dark:text-blue-400 tracking-tight italic">
                        {values.totalScos}
                    </p>
                </Card>

                <Card className="p-6 bg-card border border-border flex flex-col justify-center">
                    <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-1">Total EVN Creadas</p>
                    <p className="text-3xl font-black text-emerald-600 dark:text-emerald-400 tracking-tight italic">
                        {values.totalEvn}
                    </p>
                </Card>

                <Card className="p-6 bg-card border border-border flex flex-col justify-center">
                    <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-1">Total OPs Emitidas</p>
                    <p className="text-3xl font-black text-indigo-600 dark:text-indigo-400 tracking-tight italic">
                        {values.totalOps}
                    </p>
                </Card>
            </div>

        </div>
    );
}