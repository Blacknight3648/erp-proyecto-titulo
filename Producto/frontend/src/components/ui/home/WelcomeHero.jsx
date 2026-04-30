import { Activity, ShieldCheck, LayoutDashboard } from 'lucide-react';

export function WelcomeHero({ userName }) {
    return (
        <div className="relative overflow-hidden rounded-[2rem] bg-gradient-to-br from-indigo-600 via-blue-500 to-purple-600 p-12 text-white shadow-xl">
            <div className="relative z-10">
                <div className="inline-block px-4 py-1.5 bg-white/20 backdrop-blur-sm rounded-full mb-4">
                    <p className="text-[10px] font-black tracking-widest uppercase italic">Sistema Operativo - v1.0.2</p>
                </div>
                <h1 className="text-5xl md:text-6xl font-black mb-4 leading-tight">
                    Hola, <span className="text-blue-200">{userName}</span> 👋
                </h1>
                <p className="text-xl text-blue-100 max-w-2xl font-medium">
                    Bienvenido al ecosistema digital de <span className="font-black">Antuan SA</span>. Tu centro de mando para
                    la excelencia operativa y comercial.
                </p>
            </div>
            {/* Decorative elements */}
            <div className="absolute -top-24 -right-24 w-96 h-96 bg-white/10 rounded-full blur-3xl"></div>
            <div className="absolute -bottom-24 -left-24 w-96 h-96 bg-blue-400/10 rounded-full blur-3xl"></div>
        </div>
    );
}

export function WelcomeFooter() {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 pt-12 border-t border-gray-100">
            <div className="flex items-center space-x-4 grayscale hover:grayscale-0 opacity-50 hover:opacity-100 transition-all cursor-default">
                <Activity className="text-blue-600 w-5 h-5" />
                <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Sincronización Real-Time Activa</span>
            </div>
            <div className="flex items-center space-x-4 grayscale hover:grayscale-0 opacity-50 hover:opacity-100 transition-all cursor-default lg:justify-center">
                <ShieldCheck className="text-emerald-600 w-5 h-5" />
                <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Seguridad Nivel Enterprise</span>
            </div>
            <div className="flex items-center space-x-4 grayscale hover:grayscale-0 opacity-50 hover:opacity-100 transition-all cursor-default lg:justify-end">
                <LayoutDashboard className="text-purple-600 w-5 h-5" />
                <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Dashboard v1.0.2 Modular</span>
            </div>
        </div>
    );
}
