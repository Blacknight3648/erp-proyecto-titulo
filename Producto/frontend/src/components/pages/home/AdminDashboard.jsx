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
        color: "from-blue-500 to-blue-700",
        shadow: "shadow-blue-500/30",
    },
    {
        id: "utilidad",
        label: "Utilidad Neta",
        icon: TrendingUp,
        color: "from-emerald-500 to-green-600",
        shadow: "shadow-emerald-500/30",
    },
    {
        id: "rentabilidad",
        label: "Margen",
        icon: Percent,
        color: "from-violet-500 to-purple-600",
        shadow: "shadow-violet-500/30",
    },
    {
        id: "clientes",
        label: "Clientes Activos",
        icon: Users,
        color: "from-amber-500 to-orange-600",
        shadow: "shadow-amber-500/30",
    },
];

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
        <div className="space-y-6">

            {/* KPIs */}
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
                {KPI_CONFIG.map((kpi) => {
                    const Icon = kpi.icon;

                    return (
                        <div
                            key={kpi.id}
                            className={`
                                relative overflow-hidden rounded-2xl
                                bg-gradient-to-br ${kpi.color}
                                p-5 text-white shadow-lg ${kpi.shadow}
                            `}
                        >
                            <div className="absolute -right-5 -top-5 h-28 w-28 rounded-full bg-white/10" />
                            <div className="absolute -bottom-8 -right-3 h-24 w-24 rounded-full bg-white/5" />

                            <div className="relative z-10">
                                <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-white/20">
                                    <Icon className="h-6 w-6" />
                                </div>

                                <p className="text-xs uppercase tracking-widest text-white/70">
                                    {kpi.label}
                                </p>

                                <h2 className="mt-2 text-3xl font-bold">
                                    {values[kpi.id]}
                                </h2>
                            </div>
                        </div>
                    );
                })}
            </div>

            {/* Gráficos */}
            <div className="grid gap-6 lg:grid-cols-2">

                {/* Ventas */}
                <div className="rounded-2xl bg-white p-6 shadow-sm border">
                    <div className="mb-4">
                        <h3 className="text-lg font-semibold text-gray-800">
                            Ventas Mensuales
                        </h3>

                        <p className="text-sm text-gray-500">
                            Evolución de ventas por período
                        </p>
                    </div>

                    <ResponsiveContainer width="100%" height={320}>
                        <LineChart data={salesData}>
                            <CartesianGrid strokeDasharray="3 3" />

                            <XAxis dataKey="mes" />

                            <YAxis />

                            <Tooltip />

                            <Line
                                type="monotone"
                                dataKey="ventas"
                                stroke="#2563eb"
                                strokeWidth={4}
                            />
                        </LineChart>
                    </ResponsiveContainer>
                </div>

                {/* Rentabilidad */}
                <div className="rounded-2xl bg-white p-6 shadow-sm border">
                    <div className="mb-4">
                        <h3 className="text-lg font-semibold text-gray-800">
                            Rentabilidad %
                        </h3>

                        <p className="text-sm text-gray-500">
                            Margen obtenido por período
                        </p>
                    </div>

                    <ResponsiveContainer width="100%" height={320}>
                        <BarChart data={profitabilityData}>
                            <CartesianGrid strokeDasharray="3 3" />

                            <XAxis dataKey="mes" />

                            <YAxis />

                            <Tooltip />

                            <Bar
                                dataKey="rentabilidad"
                                fill="#10b981"
                                radius={[8, 8, 0, 0]}
                            />
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>

            {/* Resumen inferior */}
            <div className="grid gap-6 lg:grid-cols-3">

                <div className="rounded-2xl border bg-white p-6 shadow-sm">
                    <h3 className="mb-4 text-lg font-semibold">
                        Ventas Totales
                    </h3>

                    <p className="text-4xl font-bold text-blue-600">
                        {values.ventas}
                    </p>
                </div>

                <div className="rounded-2xl border bg-white p-6 shadow-sm">
                    <h3 className="mb-4 text-lg font-semibold">
                        Utilidad Neta
                    </h3>

                    <p className="text-4xl font-bold text-green-600">
                        {values.utilidad}
                    </p>
                </div>

                <div className="rounded-2xl border bg-white p-6 shadow-sm">
                    <h3 className="mb-4 text-lg font-semibold">
                        Margen Actual
                    </h3>

                    <p className="text-4xl font-bold text-violet-600">
                        {values.rentabilidad}
                    </p>
                </div>

            </div>

        </div>
    );
}
