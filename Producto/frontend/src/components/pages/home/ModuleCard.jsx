import { ArrowRight } from 'lucide-react';

export default function ModuleCard({ module, onClick }) {
    const Icon = module.icon;
    
    // Extraemos la base del color del módulo (ej: de "from-blue-500 to-indigo-600" detectamos "blue" o "indigo")
    // Esto nos sirve para aplicar bordes y textos dinámicos que combinen perfectamente.
    const colorMatch = module.color?.match(/from-(\w+)-/);
    const baseColor = colorMatch ? colorMatch[1] : 'blue';

    // Mapeo dinámico de estilos de borde y texto basados en el color del módulo
    const hoverBorderStyles = {
        blue: 'hover:border-blue-200/80 focus-visible:border-blue-300',
        emerald: 'hover:border-emerald-200/80 focus-visible:border-emerald-300',
        purple: 'hover:border-purple-200/80 focus-visible:border-purple-300',
        indigo: 'hover:border-indigo-200/80 focus-visible:border-indigo-300',
        amber: 'hover:border-amber-200/80 focus-visible:border-amber-300',
        rose: 'hover:border-rose-200/80 focus-visible:border-rose-300',
    }[baseColor] || 'hover:border-blue-200/80 focus-visible:border-blue-300';

    const hoverTextStyles = {
        blue: 'group-hover:text-blue-600',
        emerald: 'group-hover:text-emerald-600',
        purple: 'group-hover:text-purple-600',
        indigo: 'group-hover:text-indigo-600',
        amber: 'group-hover:text-amber-600',
        rose: 'group-hover:text-rose-600',
    }[baseColor] || 'group-hover:text-blue-600';

    return (
        <button
            onClick={onClick}
            className={`group relative flex h-full w-full flex-col items-start overflow-hidden rounded-2xl border border-slate-200 bg-white p-6 text-left outline-none transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)] hover:-translate-y-1 hover:shadow-[0_12px_30px_rgba(0,0,0,0.04)] sm:p-8 ${hoverBorderStyles} focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-slate-400`}
        >
            {/* Línea de acento superior */}
            <div className={`absolute left-0 right-0 top-0 h-[3px] bg-gradient-to-r ${module.color} opacity-0 transition-opacity duration-300 group-hover:opacity-100`} />

            {/* Icono con contenedor estilizado */}
            <div className={`relative mb-6 flex h-14 w-14 shrink-0 items-center justify-center rounded-xl bg-gradient-to-br ${module.color} text-white shadow-sm transition-all duration-300 group-hover:scale-105 group-hover:shadow-[0_8px_20px_rgba(0,0,0,0.12)] group-hover:shadow-${baseColor}-500/10`}>
                <Icon className="h-7 w-7 transition-transform duration-300 group-hover:rotate-3" strokeWidth={1.5} />
            </div>

            {/* Contenido (Título y Descripción) */}
            <div className="flex-1 space-y-2">
                <h3 className={`text-lg font-bold tracking-tight text-slate-800 transition-colors duration-200 ${hoverTextStyles}`}>
                    {module.title}
                </h3>
                <p className="text-sm font-medium leading-relaxed text-slate-500/90 line-clamp-3">
                    {module.description}
                </p>
            </div>

            {/* Llamada a la acción (CTA) */}
            <div className="mt-6 flex w-full items-center gap-2 border-t border-slate-100 pt-4 text-sm font-bold text-slate-400/80 transition-colors duration-200">
                <span className={`text-[10px] uppercase tracking-wider transition-colors duration-200 ${hoverTextStyles}`}>
                    Acceder al panel
                </span>
                <ArrowRight className={`h-4 w-4 transition-all duration-300 group-hover:translate-x-1.5 ${hoverTextStyles}`} />
            </div>

            {/* Resplandor ambiental de fondo en Hover (0.03 de opacidad para máxima sutileza) */}
            <div className={`absolute inset-0 bg-gradient-to-br ${module.color} pointer-events-none opacity-0 transition-opacity duration-300 group-hover:opacity-[0.03]`} />
        </button>
    );
}