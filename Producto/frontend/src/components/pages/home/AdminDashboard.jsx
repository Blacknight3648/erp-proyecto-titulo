import { Users, Database, Cpu, ClipboardList } from 'lucide-react';

const KPI_CONFIG = [
    {
        id: 'usuarios',
        label: 'Usuarios Activos',
        sublabel: 'Usuarios del sistema',
        icon: Users,
        color: 'from-blue-500 to-blue-700',
        shadow: 'shadow-blue-500/30',
    },
    {
        id: 'registros',
        label: 'Datos Registrados',
        sublabel: 'Registros en BD',
        icon: Database,
        color: 'from-cyan-500 to-teal-600',
        shadow: 'shadow-cyan-500/30',
    },
    {
        id: 'sistema',
        label: 'Sistema',
        sublabel: 'Funcionando',
        icon: Cpu,
        color: 'from-emerald-500 to-green-600',
        shadow: 'shadow-emerald-500/30',
    },
    {
        id: 'auditoria',
        label: 'Auditoría',
        sublabel: 'Logs del sistema',
        icon: ClipboardList,
        color: 'from-amber-500 to-orange-600',
        shadow: 'shadow-amber-500/30',
    },
];

export function AdminDashboard({ stats = {} }) {
    const values = {
        usuarios: stats.usuarios ?? '—',
        registros: stats.registros ?? '—',
        sistema: stats.sistema ?? 'v1.0.0',
        auditoria: stats.auditoria ?? '●',
    };

    return (
        <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
            {KPI_CONFIG.map((kpi) => {
                const Icon = kpi.icon;
                return (
                    <div
                        key={kpi.id}
                        className={`relative overflow-hidden rounded-2xl bg-gradient-to-br ${kpi.color} p-5 text-white shadow-lg ${kpi.shadow}`}
                    >
                        <div className="absolute -right-4 -top-4 h-24 w-24 rounded-full bg-white/10" />
                        <div className="absolute -bottom-6 -right-2 h-20 w-20 rounded-full bg-white/5" />

                        <div className="relative z-10 flex flex-col gap-3">
                            <div className="flex items-start justify-between">
                                <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-white/20 backdrop-blur-sm">
                                    <Icon className="h-5 w-5 text-white" strokeWidth={1.75} />
                                </div>
                            </div>

                            <div>
                                <p className="text-[11px] font-semibold uppercase tracking-widest text-white/70">
                                    {kpi.label}
                                </p>
                                <p className="mt-0.5 text-3xl font-bold tracking-tight text-white">
                                    {values[kpi.id]}
                                </p>
                                <p className="mt-1 text-[11px] font-medium text-white/60">
                                    {kpi.sublabel}
                                </p>
                            </div>
                        </div>
                    </div>
                );
            })}
        </div>
    );
}
