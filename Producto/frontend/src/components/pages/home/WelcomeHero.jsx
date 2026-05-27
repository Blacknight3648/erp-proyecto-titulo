import { Activity, ShieldCheck, LayoutDashboard, UserCircle, Settings } from 'lucide-react';

export function WelcomeHero({ userName, role = "Administrador del Sistema" }) {
    return (
        <div className="relative overflow-hidden rounded-2xl border border-slate-200/80 bg-gradient-to-b from-white to-slate-50/50 p-6 shadow-sm transition-all duration-300 hover:shadow-md md:p-8">
            {/* Elementos decorativos de fondo con gradientes suaves */}
            <div className="absolute -right-10 -top-10 h-72 w-72 rounded-full bg-gradient-to-br from-blue-400/10 to-indigo-400/10 blur-3xl" />
            <div className="absolute -bottom-10 right-1/3 h-40 w-40 rounded-full bg-gradient-to-tr from-emerald-400/5 to-teal-400/5 blur-2xl" />
            
            <div className="relative z-10 flex flex-col gap-6 md:flex-row md:items-center md:justify-between">
                <div className="flex flex-col items-start gap-4 sm:flex-row sm:items-center sm:gap-5">
                    {/* Contenedor del Avatar */}
                    <div className="relative flex h-16 w-16 shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br from-blue-50 to-indigo-100 text-blue-600 shadow-inner border border-blue-100/50">
                        <UserCircle className="h-10 w-10" strokeWidth={1.5} />
                        <span className="absolute -bottom-0.5 -right-0.5 flex h-3.5 w-3.5 items-center justify-center rounded-full bg-white p-0.5 shadow-sm">
                            <span className="h-full w-full rounded-full bg-emerald-500 animate-pulse" />
                        </span>
                    </div>
                    
                    {/* Textos de Bienvenida */}
                    <div className="space-y-1">
                        <h1 className="text-2xl font-bold tracking-tight text-slate-900 md:text-3xl">
                            ¡Bienvenido de vuelta, <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">{userName}</span>!
                        </h1>
                        <div className="flex flex-wrap items-center gap-2.5">
                            <span className="inline-flex items-center gap-1.5 rounded-lg border border-blue-100 bg-blue-50/60 px-2.5 py-1 text-xs font-semibold uppercase tracking-wider text-blue-700 backdrop-blur-sm">
                                <ShieldCheck className="h-3.5 w-3.5" />
                                {role}
                            </span>
                            <span className="inline-flex items-center gap-1.5 text-xs font-medium text-slate-500 bg-slate-100/80 px-2.5 py-1 rounded-lg border border-slate-200/40">
                                Sesión Activa
                            </span>
                        </div>
                    </div>
                </div>

                {/* Acciones de la derecha */}
                <div className="flex items-center gap-3 border-t border-slate-100 pt-4 md:border-none md:pt-0">
                    <button className="group flex h-10 w-10 items-center justify-center rounded-xl border border-slate-200 bg-white text-slate-500 shadow-sm transition-all duration-200 hover:border-blue-200 hover:bg-blue-50/50 hover:text-blue-600 hover:shadow-md active:scale-95">
                        <Settings className="h-5 w-5 transition-transform duration-300 group-hover:rotate-45" />
                    </button>
                </div>
            </div>
        </div>
    );
}

export function WelcomeFooter() {
    const items = [
        {
            icon: Activity,
            text: "Sincronización Real-Time",
            colorClass: "hover:text-blue-600 hover:bg-blue-50/40 hover:border-blue-100",
            iconBg: "bg-blue-50 text-blue-600",
            justify: "md:justify-start"
        },
        {
            icon: ShieldCheck,
            text: "Seguridad Enterprise",
            colorClass: "hover:text-emerald-600 hover:bg-emerald-50/40 hover:border-emerald-100",
            iconBg: "bg-emerald-50 text-emerald-600",
            justify: "md:justify-center"
        },
        {
            icon: LayoutDashboard,
            text: "Dashboard Modular",
            colorClass: "hover:text-purple-600 hover:bg-purple-50/40 hover:border-purple-100",
            iconBg: "bg-purple-50 text-purple-600",
            justify: "md:justify-end"
        }
    ];

    return (
        <div className="grid grid-cols-1 gap-4 pt-6 border-t border-slate-200/60 mt-6 sm:grid-cols-2 md:grid-cols-3">
            {items.map((item, index) => {
                const Icon = item.icon;
                return (
                    <div 
                        key={index} 
                        className={`flex items-center space-x-3 rounded-xl border border-transparent p-2.5 text-slate-500 transition-all duration-200 cursor-default transform hover:-translate-y-0.5 ${item.colorClass} ${item.justify}`}
                    >
                        <div className={`p-2 rounded-lg shrink-0 ${item.iconBg} shadow-sm`}>
                            <Icon className="w-4 h-4" />
                        </div>
                        <span className="text-xs font-bold uppercase tracking-wider text-slate-600/90 group-hover:text-inherit">
                            {item.text}
                        </span>
                    </div>
                );
            })}
        </div>
    );
}