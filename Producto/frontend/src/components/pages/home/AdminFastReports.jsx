import {
    BarChart3,
    Briefcase,
    Factory,
    Users,
    ChevronRight
} from 'lucide-react';

const QUICK_LINKS = [
    {
        id: 'reportes',
        label: 'Reportes',
        description: 'KPIs y métricas clave',
        icon: BarChart3,
    },
    {
        id: 'comercial',
        label: 'Comercial',
        description: 'Ventas y cotizaciones',
        icon: Briefcase,
    },
    {
        id: 'produccion',
        label: 'Producción',
        description: 'Tablero operativo',
        icon: Factory,
    },
    {
        id: 'usuarios',
        label: 'Gestión de Usuarios',
        description: 'Roles y permisos',
        icon: Users,
    },
];

export function AdminFastReports({ onNavigate }) {
    return (
        <div className="flex flex-col gap-3">
            <div className="mb-1">
                <h3 className="text-[11px] font-semibold uppercase tracking-[0.2em] text-slate-400">
                    Acceso Rápido
                </h3>
            </div>

            {QUICK_LINKS.map((link) => {
                const Icon = link.icon;

                return (
                    <button
                        key={link.id}
                        onClick={() => onNavigate?.(link.id)}
                        className="
                            group
                            flex
                            items-center
                            gap-4
                            rounded-2xl
                            border
                            border-slate-200/70
                            bg-white/80
                            px-4
                            py-3.5
                            text-left
                            backdrop-blur-md
                            shadow-sm
                            transition-all
                            duration-300
                            hover:-translate-y-0.5
                            hover:border-[#6610f2]/20
                            hover:bg-white
                            hover:shadow-lg
                            hover:shadow-slate-200/50
                        "
                    >
                        {/* Ícono */}
                        <div
                            className="
                                flex
                                h-10
                                w-10
                                shrink-0
                                items-center
                                justify-center
                                rounded-xl
                                bg-slate-100
                                transition-all
                                duration-300
                                group-hover:bg-[#6610f2]/10
                            "
                        >
                            <Icon
                                className="
                                    h-5
                                    w-5
                                    text-slate-600
                                    transition-colors
                                    duration-300
                                    group-hover:text-[#6610f2]
                                "
                                strokeWidth={1.75}
                            />
                        </div>

                        {/* Texto */}
                        <div className="min-w-0 flex-1">
                            <p className="text-sm font-semibold text-slate-800">
                                {link.label}
                            </p>

                            <p className="mt-0.5 text-xs text-slate-500">
                                {link.description}
                            </p>
                        </div>

                        {/* Flecha */}
                        <ChevronRight
                            className="
                                h-4
                                w-4
                                shrink-0
                                text-slate-400
                                transition-all
                                duration-300
                                group-hover:translate-x-1
                                group-hover:text-[#6610f2]
                            "
                        />
                    </button>
                );
            })}
        </div>
    );
}