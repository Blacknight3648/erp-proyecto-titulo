import { BarChart3, Briefcase, Factory, Users, ChevronRight } from 'lucide-react';

const QUICK_LINKS = [
    {
        id: 'reportes',
        label: 'Reportes',
        description: 'KPIs y métricas clave',
        icon: BarChart3,
        accent: 'border-l-blue-500 group-hover:bg-blue-50/60 group-hover:border-blue-200',
        iconColor: 'text-blue-600 bg-blue-50',
        chevronColor: 'text-blue-400',
    },
    {
        id: 'comercial',
        label: 'Comercial',
        description: 'Ventas y cotizaciones',
        icon: Briefcase,
        accent: 'border-l-indigo-500 group-hover:bg-indigo-50/60 group-hover:border-indigo-200',
        iconColor: 'text-indigo-600 bg-indigo-50',
        chevronColor: 'text-indigo-400',
    },
    {
        id: 'produccion',
        label: 'Producción',
        description: 'Tablero operativo',
        icon: Factory,
        accent: 'border-l-emerald-500 group-hover:bg-emerald-50/60 group-hover:border-emerald-200',
        iconColor: 'text-emerald-600 bg-emerald-50',
        chevronColor: 'text-emerald-400',
    },
    {
        id: 'usuarios',
        label: 'Gestión de Usuarios',
        description: 'Roles y permisos',
        icon: Users,
        accent: 'border-l-violet-500 group-hover:bg-violet-50/60 group-hover:border-violet-200',
        iconColor: 'text-violet-600 bg-violet-50',
        chevronColor: 'text-violet-400',
    },
];

export function AdminFastReports({ onNavigate }) {
    return (
        <div className="flex flex-col gap-3">
            <div className="mb-1">
                <h3 className="text-[11px] font-bold uppercase tracking-widest text-slate-400">
                    Acceso Rápido
                </h3>
            </div>

            {QUICK_LINKS.map((link) => {
                const Icon = link.icon;
                return (
                    <button
                        key={link.id}
                        onClick={() => onNavigate?.(link.id)}
                        className={`group flex items-center gap-4 rounded-xl border border-slate-200/80 bg-white px-4 py-3.5 text-left shadow-sm transition-all duration-200 hover:-translate-y-px hover:shadow-md border-l-4 ${link.accent}`}
                    >
                        <div className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-lg ${link.iconColor}`}>
                            <Icon className="h-4.5 w-4.5 h-[18px] w-[18px]" strokeWidth={1.75} />
                        </div>

                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-bold text-slate-800 leading-tight">
                                {link.label}
                            </p>
                            <p className="text-[11px] text-slate-400 font-medium mt-0.5 truncate">
                                {link.description}
                            </p>
                        </div>

                        <ChevronRight
                            className={`h-4 w-4 shrink-0 transition-transform duration-200 group-hover:translate-x-0.5 ${link.chevronColor}`}
                        />
                    </button>
                );
            })}
        </div>
    );
}
