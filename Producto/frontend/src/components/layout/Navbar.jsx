import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Bell, User, LogOut, ChevronDown, Search, Box, Settings, Menu } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { useNotifications } from '../../contexts/NotificationContext';
import NotificationDropdown from './NotificationDropdown';

export default function Navbar({ isSidebarOpen = true, setIsSidebarOpen }) {
    const { user, logout } = useAuth();
    const { unreadCount } = useNotifications();
    const [isNotifOpen, setIsNotifOpen] = useState(false);
    const notifRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        function handleClickOutside(event) {
            if (notifRef.current && !notifRef.current.contains(event.target)) {
                setIsNotifOpen(false);
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    return (
        <header 
            className={`bg-[#0b1220]/95 backdrop-blur-xl border-b border-slate-800 h-[76px] px-5 md:px-7 flex items-center justify-between fixed top-0 right-0 z-40 transition-all duration-300 ease-in-out left-0 ${
                isSidebarOpen ? 'md:left-[260px]' : 'md:left-[72px]'
            }`}
        >
            {/* ── SECCIÓN IZQUIERDA: Buscador Profesional de Alto Contraste ── */}
            <div className="flex items-center gap-3 w-full max-w-md">
                {/* Botón menú móvil */}
                <button
                    onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                    className="p-2 text-slate-400 hover:text-white hover:bg-white/[0.04] rounded-lg transition-colors lg:hidden"
                    aria-label="Alternar menú"
                >
                    <Menu className="w-5 h-5" />
                </button>

                {/* Input iOS Estilo Profesional Maduro */}
                <div className="relative flex items-center w-full group">
                    <Search 
                        className="absolute left-3.5 z-10 w-4 h-4 text-slate-400 pointer-events-none transition-colors group-focus-within:text-sky-400" 
                        strokeWidth={2} 
                    />
                    <input
                        type="text"
                        placeholder="Buscar en el sistema..."
                        className="
                            w-full
                            rounded-xl
                            border border-slate-700
                            bg-[#111827]
                            py-2.5
                            pl-10
                            pr-4
                            text-sm
                            text-slate-100
                            placeholder:text-slate-400
                            transition-all
                            focus:outline-none
                            focus:border-sky-500
                            focus:ring-2
                            focus:ring-sky-500/10
                        "
                    />
                </div>
            </div>

            {/* ── SECCIÓN DERECHA: Acciones y Estados de Hover Consistentes ── */}
            <div className="flex items-center gap-1 sm:gap-2">
                
                {/* Datos Maestros (Corrección de clase group y hover activo) */}
                <button
                    onClick={() => navigate('/admin/datos-maestros')}
                    className="group flex items-center gap-2 text-[13px] font-medium text-slate-200 hover:text-white hover:bg-white/[0.03] border border-transparent hover:border-slate-700 px-3 py-2 rounded-lg transition-all whitespace-nowrap"
                    title="Gestión de Datos Maestros"
                >
                    <Box 
                        className="w-4 h-4 text-slate-400 group-hover:text-sky-400 transition-colors" 
                        strokeWidth={1.8} 
                    />
                    <span className="hidden sm:inline">Datos Maestros</span>
                </button>

                {/* Separador Optimizado */}
                <div className="h-5 w-px bg-slate-700/60 mx-2 hidden sm:block" />

                {/* Configuración */}
                <button
                    className="p-2 text-slate-400 hover:text-white hover:bg-white/[0.03] rounded-lg transition-colors group"
                    title="Configuración general"
                >
                    <Settings className="w-4 h-4 group-hover:rotate-45 transition-transform duration-300" strokeWidth={1.8} />
                </button>

                {/* Notificaciones */}
                <div className="relative" ref={notifRef}>
                    <button
                        className="relative p-2 text-slate-400 hover:text-white hover:bg-white/[0.03] rounded-lg transition-colors"
                        onClick={() => setIsNotifOpen(!isNotifOpen)}
                        aria-label="Ver alertas"
                    >
                        <Bell className="w-4 h-4" strokeWidth={1.8} />
                        {unreadCount > 0 && (
                            <span className="absolute top-2 right-2 w-1.5 h-1.5 bg-sky-400 rounded-full shadow-[0_0_6px_rgba(56,189,248,0.6)]" />
                        )}
                    </button>
                    <NotificationDropdown isOpen={isNotifOpen} onClose={() => setIsNotifOpen(false)} />
                </div>

                {/* Separador Optimizado */}
                <div className="h-5 w-px bg-slate-700/60 mx-2" />

                {/* Perfil de Usuario con mejor Contraste Legible */}
                <div className="flex items-center gap-1.5">
                    <div className="flex items-center gap-2.5 p-1.5 rounded-lg transition-all cursor-pointer border border-transparent hover:border-slate-700 hover:bg-white/[0.02] group">
                        <div className="w-[26px] h-[26px] bg-sky-500/10 border border-sky-500/15 text-sky-400 rounded-md flex items-center justify-center font-semibold text-[11px]">
                            <User className="w-3.5 h-3.5" strokeWidth={2} />
                        </div>
                        <span className="hidden md:block text-[13px] font-medium text-slate-200 group-hover:text-white transition-colors">
                            {user?.name || 'Administrador'}
                        </span>
                        <ChevronDown 
                            className="hidden sm:block w-3.5 h-3.5 text-slate-400 group-hover:text-slate-200 transition-colors" 
                            strokeWidth={1.5} 
                        />
                    </div>

                    {/* Botón de Salir con consistencia visual */}
                    <button
                        onClick={logout}
                        title="Cerrar Sesión"
                        className="p-2 text-slate-400 hover:text-rose-400 hover:bg-rose-500/[0.04] rounded-lg transition-colors"
                    >
                        <LogOut className="w-4 h-4" strokeWidth={1.8} />
                    </button>
                </div>
            </div>
        </header>
    );
}