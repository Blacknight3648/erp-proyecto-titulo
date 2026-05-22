import { Lock } from 'lucide-react';

export default function AuthLayout({ children }) {
    return (
        <div className="min-h-screen w-full flex items-center justify-center bg-[#070b14] relative overflow-hidden">
            {/* Grid Pattern */}
            <div className="absolute inset-0 bg-[linear-gradient(to_right,#80808012_1px,transparent_1px),linear-gradient(to_bottom,#80808012_1px,transparent_1px)] bg-[size:24px_24px]"></div>

            {/* Blur decorativos animados */}
            <div className="absolute top-[-10%] left-[-10%] w-[50%] h-[50%] bg-blue-600/20 blur-[120px] rounded-full animate-pulse"></div>
            <div className="absolute bottom-[-10%] right-[-10%] w-[50%] h-[50%] bg-indigo-600/20 blur-[120px] rounded-full animate-pulse" style={{ animationDelay: '2s' }}></div>

            <div className="bg-slate-900/40 backdrop-blur-2xl border border-white/5 p-8 sm:p-12 rounded-[2.5rem] shadow-2xl w-full max-w-md z-10 mx-4 relative">
                {/* Inner glow */}
                <div className="absolute inset-0 bg-gradient-to-b from-white/5 to-transparent rounded-[2.5rem] pointer-events-none"></div>

                <div className="text-center mb-10 relative z-10">
                    <div className="bg-gradient-to-br from-blue-500 to-indigo-600 w-20 h-20 rounded-[1.5rem] flex items-center justify-center mx-auto mb-6 shadow-[0_0_40px_-10px_rgba(59,130,246,0.6)] border border-blue-400/20">
                        <Lock className="text-white w-10 h-10 drop-shadow-md" strokeWidth={1.5} />
                    </div>
                    <h2 className="text-3xl font-black text-white mb-3 tracking-tight">Bienvenido</h2>
                    <p className="text-slate-400 font-medium">Ingresa a tu panel de control ERP</p>
                </div>

                <div className="relative z-10">
                    {children}
                </div>

                <p className="text-center mt-10 text-slate-500 text-sm font-medium relative z-10">
                    ¿No tienes acceso? <a href="#" className="text-blue-400 hover:text-blue-300 font-bold transition-colors hover:underline decoration-blue-400/50 underline-offset-4">Contacta al administrador</a>
                </p>
            </div>
        </div>
    );
}
