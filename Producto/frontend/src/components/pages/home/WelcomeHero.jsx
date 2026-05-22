import { Activity, ShieldCheck, LayoutDashboard, UserCircle, Settings } from 'lucide-react';

export function WelcomeHero({ userName, role = "Administrador del Sistema" }) {
    return (
        <div className="flex flex-col md:flex-row md:items-center justify-between bg-white border border-slate-200 rounded-2xl p-6 shadow-sm mb-2 relative overflow-hidden">
            {/* Subtle background element */}
            <div className="absolute right-0 top-0 w-64 h-64 bg-gradient-to-br from-blue-50 to-indigo-50 rounded-full blur-3xl -translate-y-1/2 translate-x-1/3"></div>
            
            <div className="flex items-center gap-5 relative z-10">
                <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-100 to-indigo-100 border border-blue-200 flex items-center justify-center text-blue-700 shadow-sm">
                    <UserCircle className="w-9 h-9" strokeWidth={1.5} />
                </div>
                <div>
                    <h1 className="text-2xl font-bold text-slate-800 tracking-tight">
                        Bienvenido, {userName}
                    </h1>
                    <div className="flex flex-wrap items-center gap-3 mt-1.5">
                        <span className="flex items-center gap-1.5 text-xs font-bold uppercase tracking-wider text-blue-700 bg-blue-50/80 px-2.5 py-1 rounded-md border border-blue-100">
                            <ShieldCheck className="w-3.5 h-3.5" />
                            {role}
                        </span>
                        <span className="text-sm text-slate-500 font-medium flex items-center gap-1.5">
                            <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></div>
                            Sesión Activa
                        </span>
                    </div>
                </div>
            </div>
            <div className="hidden md:flex items-center gap-3 relative z-10">
                <button className="p-2.5 text-slate-500 hover:text-blue-700 hover:bg-blue-50 rounded-xl transition-all border border-transparent hover:border-blue-200 shadow-sm hover:shadow">
                    <Settings className="w-5 h-5" />
                </button>
            </div>
        </div>
    );
}

export function WelcomeFooter() {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-6 border-t border-slate-200 mt-8">
            <div className="flex items-center space-x-3 text-slate-500 hover:text-blue-600 transition-colors cursor-default">
                <div className="p-2 bg-slate-100 rounded-lg">
                    <Activity className="w-4 h-4" />
                </div>
                <span className="text-[10px] font-bold uppercase tracking-widest">Sincronización Real-Time</span>
            </div>
            <div className="flex items-center space-x-3 text-slate-500 hover:text-emerald-600 transition-colors cursor-default lg:justify-center">
                <div className="p-2 bg-slate-100 rounded-lg">
                    <ShieldCheck className="w-4 h-4" />
                </div>
                <span className="text-[10px] font-bold uppercase tracking-widest">Seguridad Enterprise</span>
            </div>
            <div className="flex items-center space-x-3 text-slate-500 hover:text-purple-600 transition-colors cursor-default lg:justify-end">
                <div className="p-2 bg-slate-100 rounded-lg">
                    <LayoutDashboard className="w-4 h-4" />
                </div>
                <span className="text-[10px] font-bold uppercase tracking-widest">Dashboard Modular</span>
            </div>
        </div>
    );
}
