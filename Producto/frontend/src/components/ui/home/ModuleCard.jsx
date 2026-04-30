import { ArrowRight } from 'lucide-react';

export default function ModuleCard({ module, onClick }) {
    const Icon = module.icon;

    return (
        <div
            onClick={onClick}
            className="group relative overflow-hidden bg-white rounded-[2rem] border border-gray-100 p-8 hover:shadow-xl transition-all duration-300 hover:-translate-y-1 cursor-pointer"
        >
            {/* Icon */}
            <div className={`w-16 h-16 bg-gradient-to-br ${module.color} rounded-2xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform shadow-lg`}>
                <Icon className="w-8 h-8 text-white" />
            </div>

            {/* Content */}
            <h3 className="text-xl font-black text-gray-900 mb-3 italic">{module.title}</h3>
            <p className="text-sm text-gray-600 leading-relaxed mb-6">
                {module.description}
            </p>

            {/* CTA */}
            <div className="flex items-center gap-2 text-sm font-bold text-gray-400 group-hover:text-indigo-600 transition-colors">
                <span className="tracking-wider uppercase text-[10px]">Ingresar al módulo</span>
                <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
            </div>

            {/* Hover Effect */}
            <div className={`absolute inset-0 bg-gradient-to-br ${module.color} opacity-0 group-hover:opacity-5 transition-opacity rounded-[2rem]`}></div>
        </div>
    );
}
