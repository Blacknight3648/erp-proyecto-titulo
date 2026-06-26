import {
    BarChart3,
    Briefcase,
    Factory,
    Users,
    ChevronRight
} from 'lucide-react';
import { Card } from '../../ui/card';

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
        <Card className="flex flex-col gap-2 w-full p-4 bg-card border border-border">
            <div className="mb-1 pl-1">
                <h3 className="text-[10px] font-bold uppercase tracking-widest text-muted-foreground/80">
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
                            gap-3.5
                            rounded-xl
                            border
                            border-border/60
                            bg-muted/20
                            px-4
                            py-3
                            text-left
                            transition-all
                            duration-200
                            hover:border-primary/30
                            hover:bg-muted/60
                        "
                    >
                        {/* Contenedor del Ícono */}
                        <div
                            className="
                                flex
                                h-9
                                w-9
                                shrink-0
                                items-center
                                justify-center
                                rounded-lg
                                bg-muted
                                border
                                border-border/40
                                transition-all
                                duration-200
                                group-hover:bg-primary/10
                                group-hover:border-primary/20
                            "
                        >
                            <Icon
                                className="
                                    h-4
                                    w-4
                                    text-muted-foreground
                                    transition-colors
                                    duration-200
                                    group-hover:text-primary
                                "
                                strokeWidth={2}
                            />
                        </div>

                        {/* Textos Informativos */}
                        <div className="min-w-0 flex-1">
                            <p className="text-sm font-bold text-foreground tracking-tight transition-colors group-hover:text-primary">
                                {link.label}
                            </p>

                            <p className="mt-0.5 text-xs font-medium leading-relaxed text-muted-foreground/90 truncate">
                                {link.description}
                            </p>
                        </div>

                        {/* Flecha Indicadora de Acción */}
                        <ChevronRight
                            className="
                                h-4
                                w-4
                                shrink-0
                                text-muted-foreground/50
                                transition-all
                                duration-200
                                group-hover:translate-x-0.5
                                group-hover:text-primary
                            "
                            strokeWidth={2}
                        />
                    </button>
                );
            })}
        </Card>
    );
}