import { useState } from 'react';
import { Mail, Lock, ChevronRight, AlertCircle } from 'lucide-react';

export default function LoginForm({ email, setEmail, password, setPassword, onSubmit }) {
    const [errors, setErrors] = useState({ email: '', password: '' });

    const handleSubmit = (e) => {
        e.preventDefault();
        const newErrors = {};
        if (!email.trim()) {
            newErrors.email = 'Introduce un usuario o correo electrónico';
        }
        if (!password) {
            newErrors.password = 'Introduce una contraseña';
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
        } else {
            setErrors({ email: '', password: '' });
            onSubmit(e);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="flex flex-col gap-6" noValidate>
            <div>
                <label className={`block text-sm font-semibold mb-2 ml-1 tracking-wide transition-colors ${errors.email ? 'text-rose-400' : 'text-slate-200'}`}>
                    Usuario o Email
                </label>
                <div className="relative group">
                    <div className={`absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none transition-colors ${
                        errors.email 
                        ? 'text-rose-400 group-focus-within:text-rose-400' 
                        : 'text-slate-400 group-focus-within:text-blue-400'
                    }`}>
                        <Mail className="w-5 h-5" />
                    </div>
                    <input
                        type="text"
                        value={email}
                        onChange={(e) => {
                            setEmail(e.target.value);
                            if (errors.email) {
                                setErrors(prev => ({ ...prev, email: '' }));
                            }
                        }}
                        className={`w-full h-14 bg-slate-900/60 border text-white pl-12 pr-4 rounded-2xl focus:ring-4 outline-none transition-all placeholder:text-slate-500 shadow-inner backdrop-blur-md ${
                            errors.email 
                            ? 'border-rose-500/80 focus:ring-rose-500/30 focus:border-rose-500' 
                            : 'border-slate-700/50 focus:ring-blue-500/30 focus:border-blue-500'
                        }`}
                        placeholder="admin"
                        required
                    />
                </div>
                {errors.email && (
                    <div className="flex items-center gap-2 mt-2 ml-1 text-rose-400 text-sm animate-in fade-in slide-in-from-top-1 duration-200">
                        <AlertCircle className="w-4 h-4 flex-shrink-0" />
                        <span>{errors.email}</span>
                    </div>
                )}
            </div>

            <div>
                <label className={`block text-sm font-semibold mb-2 ml-1 tracking-wide transition-colors ${errors.password ? 'text-rose-400' : 'text-slate-200'}`}>
                    Contraseña
                </label>
                <div className="relative group">
                    <div className={`absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none transition-colors ${
                        errors.password 
                        ? 'text-rose-400 group-focus-within:text-rose-400' 
                        : 'text-slate-400 group-focus-within:text-blue-400'
                    }`}>
                        <Lock className="w-5 h-5" />
                    </div>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                            if (errors.password) {
                                setErrors(prev => ({ ...prev, password: '' }));
                            }
                        }}
                        className={`w-full h-14 bg-slate-900/60 border text-white pl-12 pr-4 rounded-2xl focus:ring-4 outline-none transition-all placeholder:text-slate-500 shadow-inner backdrop-blur-md ${
                            errors.password 
                            ? 'border-rose-500/80 focus:ring-rose-500/30 focus:border-rose-500' 
                            : 'border-slate-700/50 focus:ring-blue-500/30 focus:border-blue-500'
                        }`}
                        placeholder="••••••••"
                        required
                    />
                </div>
                {errors.password && (
                    <div className="flex items-center gap-2 mt-2 ml-1 text-rose-400 text-sm animate-in fade-in slide-in-from-top-1 duration-200">
                        <AlertCircle className="w-4 h-4 flex-shrink-0" />
                        <span>{errors.password}</span>
                    </div>
                )}
            </div>

            <div className="flex items-center justify-between text-sm px-1 mt-2">
                <label className="flex items-center text-slate-300 hover:text-white cursor-pointer transition-colors group">
                    <div className="relative flex items-center justify-center mr-3">
                        <input type="checkbox" className="peer w-5 h-5 rounded-lg border-2 border-slate-600 bg-slate-800/80 text-blue-500 focus:ring-4 focus:ring-blue-500/30 focus:outline-none transition-all appearance-none checked:bg-blue-500 checked:border-blue-500 cursor-pointer" />
                        <svg className="absolute w-3.5 h-3.5 text-white opacity-0 peer-checked:opacity-100 pointer-events-none transition-opacity duration-200" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="3"><path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7"></path></svg>
                    </div>
                    <span className="font-medium">Recordarme</span>
                </label>
                <button
                    type="button"
                    onClick={() => alert('Por favor, contacta al administrador para restablecer tu contraseña.')}
                    className="text-blue-400 hover:text-blue-300 font-medium transition-colors hover:underline decoration-blue-400/50 underline-offset-4 focus:outline-none"
                >
                    Restablecer contraseña
                </button>
            </div>

            <button
                type="submit"
                className="w-full h-14 mt-4 bg-gradient-to-r from-blue-500 to-purple-500 hover:from-blue-600 hover:to-purple-600 text-white font-bold text-lg rounded-2xl shadow-[0_0_40px_-10px_rgba(92,142,242,0.4)] hover:shadow-[0_0_60px_-15px_rgba(92,142,242,0.6)] hover:-translate-y-1 transition-all duration-300 flex items-center justify-center group overflow-hidden relative"
            >
                <div className="absolute inset-0 bg-white/20 translate-y-full group-hover:translate-y-0 transition-transform duration-300 ease-out"></div>
                <span className="relative z-10 flex items-center">
                    Iniciar Sesión
                    <ChevronRight className="ml-2 w-5 h-5 group-hover:translate-x-1.5 transition-transform" />
                </span>
            </button>
        </form>
    );
}
