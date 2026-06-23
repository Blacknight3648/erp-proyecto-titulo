import React from "react";
import {
    Users,
    DollarSign,
    TrendingUp,
    Percent,
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

const KPI_CONFIG = [
    {
        id: "ventas",
        label: "Ventas del Mes",
        icon: DollarSign,
        colorText: "text-blue-600",
        colorBg: "bg-blue-50",
        ringColor: "hover:border-blue-500 hover:shadow-blue-50",
    },
    {
        id: "utilidad",
        label: "Utilidad Neta",
        icon: TrendingUp,
        colorText: "text-emerald-600",
        colorBg: "bg-emerald-50",
        ringColor: "hover:border-emerald-500 hover:shadow-emerald-50",
    },
    {
        id: "rentabilidad",
        label: "Margen",
        icon: Percent,
        colorText: "text-indigo-600",
        colorBg: "bg-indigo-50",
        ringColor: "hover:border-indigo-500 hover:shadow-indigo-50",
    },
    {
        id: "clientes",
        label: "Clientes Activos",
        icon: Users,
        colorText: "text-amber-600",
        colorBg: "bg-amber-50",
        ringColor: "hover:border-amber-500 hover:shadow-amber-50",
    },
];

// Tooltip Personalizado para mantener el estilo de la app
const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
        return (
            <div className="bg-gray-900 text-white p-4 rounded-2xl shadow-xl border border-gray-800 animate-in fade-in zoom-in-95 duration-150">
                <p className="text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1">{label}</p>
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
        utilidad: stats.utilidad ?? "$0",
        rentabilidad: stats.rentabilidad ?? "0%",
        clientes: stats.clientes ?? 0,
    };

    return (
        <div className="max-w-full p-2 space-y-8 animate-in fade-in duration-700">

            {/* KPIs Cards */}
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-4">
                {KPI_CONFIG.map((kpi) => {
                    const Icon = kpi.icon;

                    return (
                        <div
                            key={kpi.id}
                            className={`group bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 transition-all relative overflow-hidden flex flex-col shadow-sm ${kpi.ringColor} hover:shadow-2xl`}
                        >
                            <div className="flex items-center justify-between mb-4">
                                <div className="flex flex-col">
                                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">
                                        {kpi.label}
                                    </p>
                                    <h2 className="text-2xl font-black text-gray-800 tracking-tight">
                                        {values[kpi.id]}
                                    </h2>
                                </div>
                                <div className={`w-12 h-12 rounded-2xl ${kpi.colorBg} flex items-center justify-center shrink-0 transition-transform group-hover:scale-110 duration-300`}>
                                    <Icon className={`h-5 w-5 ${kpi.colorText}`} />
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>

            {/* Gráficos */}
            <div className="grid gap-6 lg:grid-cols-2">

                {/* Grafico Ventas */}
                <div className="bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 shadow-sm">
                    <div className="mb-6">
                        <h3 className="text-sm font-black text-gray-800 uppercase tracking-tight">
                            Ventas Mensuales
                        </h3>
                        <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mt-0.5">
                            Evolución de ventas por período
                        </p>
                    </div>

                    <ResponsiveContainer width="100%" height={320}>
                        <LineChart data={salesData} margin={{ top: 10, right: 10, left: -10, bottom: 0 }}>
                            <defs>
                                <linearGradient id="colorVentas" x1="0" y1="0" x2="0" y2="1">
                                    <stop offset="5%" stopColor="#2563eb" stopOpacity={0.1}/>
                                    <stop offset="95%" stopColor="#2563eb" stopOpacity={0}/>
                                </linearGradient>
                            </defs>
                            <CartesianGrid strokeDasharray="0" stroke="#f3f4f6" vertical={false} />
                            <XAxis 
                                dataKey="mes" 
                                axisLine={false} 
                                tickLine={false} 
                                tick={{ fill: '#9ca3af', fontSize: 10, fontWeight: 700 }}
                            />
                            <YAxis 
                                axisLine={false} 
                                tickLine={false} 
                                tick={{ fill: '#9ca3af', fontSize: 10, fontWeight: 700 }}
                            />
                            <Tooltip content={<CustomTooltip />} cursor={{ stroke: '#e5e7eb', strokeWidth: 1 }} />
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
                </div>

                {/* Grafico Rentabilidad */}
                <div className="bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 shadow-sm">
                    <div className="mb-6">
                        <h3 className="text-sm font-black text-gray-800 uppercase tracking-tight">
                            Rentabilidad %
                        </h3>
                        <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mt-0.5">
                            Margen obtenido por período
                        </p>
                    </div>

                    <ResponsiveContainer width="100%" height={320}>
                        <BarChart data={profitabilityData} margin={{ top: 10, right: 10, left: -10, bottom: 0 }}>
                            <CartesianGrid strokeDasharray="0" stroke="#f3f4f6" vertical={false} />
                            <XAxis 
                                dataKey="mes" 
                                axisLine={false} 
                                tickLine={false} 
                                tick={{ fill: '#9ca3af', fontSize: 10, fontWeight: 700 }}
                            />
                            <YAxis 
                                axisLine={false} 
                                tickLine={false} 
                                tick={{ fill: '#9ca3af', fontSize: 10, fontWeight: 700 }}
                            />
                            <Tooltip content={<CustomTooltip />} cursor={{ fill: '#f9fafb' }} />
                            <Bar
                                dataKey="rentabilidad"
                                name="rentabilidad"
                                fill="#10b981"
                                maxBarSize={45}
                                radius={[8, 8, 0, 0]}
                            />
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>

            {/* Resumen inferior */}
            <div className="grid gap-6 grid-cols-1 lg:grid-cols-3">
                <div className="bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 shadow-sm flex flex-col justify-center">
                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">Ventas Totales</p>
                    <p className="text-3xl font-black text-blue-600 tracking-tight italic">
                        {values.ventas}
                    </p>
                </div>

                <div className="bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 shadow-sm flex flex-col justify-center">
                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">Utilidad Neta</p>
                    <p className="text-3xl font-black text-emerald-600 tracking-tight italic">
                        {values.utilidad}
                    </p>
                </div>

                <div className="bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 shadow-sm flex flex-col justify-center">
                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">Margen Actual</p>
                    <p className="text-3xl font-black text-indigo-600 tracking-tight italic">
                        {values.rentabilidad}
                    </p>
                </div>
            </div>

        </div>
    );
}