import { ArrowRight } from 'lucide-react';

export default function ModuleCard({ module, onClick }) {
    const Icon = module.icon;
    
    // Extraemos la base del color del módulo (ej: de "from-blue-500 to-indigo-600" detectamos "blue" o "indigo")
    // Esto nos sirve para aplicar bordes y textos dinámicos que combinen perfectamente.
    const colorMatch = module.color?.match(/from-(\w+)-/);
    const baseColor = colorMatch ? colorMatch[1] : 'blue';

    // Mapeo dinámico de estilos de borde y texto basados en el color del módulo (tokens de marca)
    const hoverBorderStyles = {
        blue: 'hover:border-primary/50 focus-visible:border-primary',
        sky: 'hover:border-primary/50 focus-visible:border-primary',
        cyan: 'hover:border-brand-teal/50 focus-visible:border-brand-teal',
        emerald: 'hover:border-success/50 focus-visible:border-success',
        teal: 'hover:border-brand-teal/50 focus-visible:border-brand-teal',
        purple: 'hover:border-brand-violet/50 focus-visible:border-brand-violet',
        violet: 'hover:border-brand-violet/50 focus-visible:border-brand-violet',
        indigo: 'hover:border-brand-indigo/50 focus-visible:border-brand-indigo',
        amber: 'hover:border-warning/50 focus-visible:border-warning',
        orange: 'hover:border-warning/50 focus-visible:border-warning',
        rose: 'hover:border-destructive/50 focus-visible:border-destructive',
        slate: 'hover:border-border-strong focus-visible:border-border-strong',
        gray: 'hover:border-border-strong focus-visible:border-border-strong',
    }[baseColor] || 'hover:border-primary/50 focus-visible:border-primary';

    const hoverTextStyles = {
        blue: 'group-hover:text-primary',
        sky: 'group-hover:text-primary',
        cyan: 'group-hover:text-brand-teal',
        emerald: 'group-hover:text-success',
        teal: 'group-hover:text-brand-teal',
        purple: 'group-hover:text-brand-violet',
        violet: 'group-hover:text-brand-violet',
        indigo: 'group-hover:text-brand-indigo',
        amber: 'group-hover:text-warning',
        orange: 'group-hover:text-warning',
        rose: 'group-hover:text-destructive',
        slate: 'group-hover:text-foreground',
        gray: 'group-hover:text-foreground',
    }[baseColor] || 'group-hover:text-primary';

    return (
        <button
            onClick={onClick}
            className={`group relative flex h-full w-full flex-col items-start overflow-hidden rounded-[2rem] border border-border bg-card p-6 text-left outline-none transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)] hover:-translate-y-1 hover:shadow-md sm:p-8 ${hoverBorderStyles} focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-ring`}
        >
            {/* Línea de acento superior */}
            <div className={`absolute left-0 right-0 top-0 h-[3px] bg-gradient-to-r ${module.color} opacity-0 transition-opacity duration-300 group-hover:opacity-100`} />

            {/* Icono con contenedor estilizado */}
            <div className={`relative mb-6 flex h-14 w-14 shrink-0 items-center justify-center rounded-xl bg-gradient-to-br ${module.color} text-white shadow-sm transition-all duration-300 group-hover:scale-105 group-hover:shadow-[0_8px_20px_rgba(0,0,0,0.12)]`}>
                <Icon className="h-7 w-7 transition-transform duration-300 group-hover:rotate-3" strokeWidth={1.5} />
            </div>

            {/* Contenido (Título y Descripción) */}
            <div className="flex-1 space-y-2">
                <h3 className={`text-lg font-bold tracking-tight text-foreground transition-colors duration-200 ${hoverTextStyles}`}>
                    {module.title}
                </h3>
                <p className="text-sm font-medium leading-relaxed text-muted-foreground line-clamp-3">
                    {module.description}
                </p>
            </div>

            {/* Llamada a la acción (CTA) */}
            <div className="mt-6 flex w-full items-center gap-2 border-t border-border pt-4 text-sm font-bold text-muted-foreground/80 transition-colors duration-200">
                <span className={`text-[10px] uppercase tracking-wider transition-colors duration-200 ${hoverTextStyles}`}>
                    Acceder al panel
                </span>
                <ArrowRight className={`h-4 w-4 transition-all duration-300 group-hover:translate-x-1.5 ${hoverTextStyles}`} />
            </div>

            {/* Resplandor ambiental de fondo en Hover */}
            <div className={`absolute inset-0 bg-gradient-to-br ${module.color} pointer-events-none opacity-0 transition-opacity duration-300 group-hover:opacity-[0.03]`} />
        </button>
    );
}