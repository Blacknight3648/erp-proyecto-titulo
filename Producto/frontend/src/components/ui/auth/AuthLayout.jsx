import { Lock } from 'lucide-react';

export default function AuthLayout({ children }) {
    return (
        <div className="min-h-screen w-full flex items-center justify-center bg-[#0f172a] relative overflow-hidden">
            {/* Elementos decorativos de fondo */}
            <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-blue-600/20 blur-[120px] rounded-full"></div>
            <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-purple-600/20 blur-[120px] rounded-full"></div>

            <div className="bg-white/10 backdrop-blur-xl border border-white/10 p-10 rounded-3xl shadow-2xl w-full max-w-md z-10">
                <div className="text-center mb-10">
                    <div className="bg-blue-500 w-16 h-16 rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg shadow-blue-500/30">
                        <Lock className="text-white w-8 h-8" />
                    </div>
                    <h2 className="text-3xl font-bold text-white mb-2">Bienvenido</h2>
                    <p className="text-slate-400">Ingresa a tu panel de control ERP</p>
                </div>

                {children}

                <p className="text-center mt-8 text-slate-500 text-sm">
                    ¿No tienes acceso? <a href="#" className="text-blue-400 font-medium hover:underline">Contacta al administrador</a>
                </p>
            </div>
        </div>
    );
}
