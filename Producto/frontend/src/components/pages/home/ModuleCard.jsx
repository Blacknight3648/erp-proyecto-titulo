import { ArrowRight } from 'lucide-react';

export default function ModuleCard({ module, onClick }) {
    const Icon = module.icon;

    return (
        <div
            onClick={onClick}
            className="group relative overflow-hidden bg-white rounded-2xl border border-slate-200 p-6 sm:p-8 hover:shadow-[0_8px_30px_rgb(0,0,0,0.04)] hover:border-blue-200 transition-all duration-300 hover:-translate-y-1 cursor-pointer flex flex-col h-full"
        >
            {/* Top accent line */}
            <div className={`absolute top-0 left-0 right-0 h-1 bg-gradient-to-r ${module.color} opacity-0 group-hover:opacity-100 transition-opacity duration-300`}></div>

            {/* Icon */}
            <div className={`w-14 h-14 bg-gradient-to-br ${module.color} rounded-xl flex items-center justify-center mb-6 group-hover:scale-105 group-hover:shadow-lg transition-all duration-300 shrink-0`}>
                <Icon className="w-7 h-7 text-white" strokeWidth={1.5} />
            </div>

            {/* Content */}
            <div className="flex-1">
                <h3 className="text-lg font-bold text-slate-800 mb-2 tracking-tight group-hover:text-blue-700 transition-colors">{module.title}</h3>
                <p className="text-sm text-slate-500 leading-relaxed mb-6 font-medium">
                    {module.description}
                </p>
            </div>

            {/* CTA */}
            <div className="flex items-center gap-2 text-sm font-bold text-slate-400 group-hover:text-blue-600 transition-colors mt-auto pt-4 border-t border-slate-50">
                <span className="tracking-wider uppercase text-[10px]">Acceder al panel</span>
                <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
            </div>

            {/* Subtle Hover Background */}
            <div className={`absolute inset-0 bg-gradient-to-br ${module.color} opacity-0 group-hover:opacity-[0.02] transition-opacity pointer-events-none`}></div>
        </div>
    );
}
