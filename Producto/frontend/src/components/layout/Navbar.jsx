import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Bell, User, LogOut, ChevronDown, Search, Box, Settings, Menu, FileText, Target } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { useNotifications } from '../../contexts/NotificationContext';
import NotificationDropdown from './NotificationDropdown';

export default function Navbar({ isSidebarOpen = true, setIsSidebarOpen }) {
    const { user, logout } = useAuth();
    const { unreadCount } = useNotifications();
    const [isNotifOpen, setIsNotifOpen] = useState(false);
    const [isFocusMode, setIsFocusMode] = useState(false); // Estado para el modo concentración
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
            className={`bg-surface-1/90 backdrop-blur-md border-b border-border h-20 px-4 md:px-8 grid grid-cols-3 items-center fixed top-0 right-0 z-40 transition-all duration-300 left-0 ${
                isSidebarOpen ? 'md:left-64' : 'md:left-20'
            }`}
        >

            {/* 1. SECCIÓN IZQUIERDA: Buscador y Menú */}
            <div className="flex items-center gap-2 md:gap-4 justify-self-start w-full z-20">
                <button
                    onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                    className="p-2 text-foreground/80 hover:text-foreground hover:bg-surface-2 rounded-lg transition-colors md:hidden"
                    aria-label="Menú"
                >
                    <Menu className="w-5 h-5" />
                </button>

                {/* Buscador inteligente adaptable */}
                <div className="flex items-center relative w-full max-w-[40px] md:max-w-xs group">
                    <Search className="w-4 h-4 text-muted-foreground absolute left-3 pointer-events-none group-focus-within:text-primary transition-colors z-10" />
                    <input
                        type="text"
                        placeholder="Buscar..."
                        className="bg-surface-2/60 border border-border/80 text-foreground text-xs rounded-lg pl-9 pr-4 py-2 w-full transition-all placeholder:text-muted-foreground/70
                                   absolute md:relative opacity-0 md:opacity-100 cursor-pointer md:cursor-text focus:w-48 focus:md:w-full focus:opacity-100 focus:bg-surface-1 focus:cursor-text focus:outline-none focus:ring-1 focus:ring-primary/40 focus:border-primary"
                    />
                </div>
            </div>

            {/* 2. SECCIÓN CENTRAL: GESTIÓN DE DATOS MAESTROS Y REPORTES (Forzado al centro en pantallas pequeñas/minimizadas) */}
            <nav className="absolute left-1/2 -translate-x-1/2 md:relative md:left-0 md:translate-x-0 flex items-center justify-center gap-1.5 sm:gap-3 justify-self-center w-auto md:w-full z-10">
                <button
                    onClick={() => navigate('/admin/datos-maestros')}
                    className="flex items-center gap-2 text-xs font-medium text-muted-foreground hover:text-primary bg-transparent hover:bg-primary/5 border border-transparent hover:border-primary/20 px-2 py-2 md:px-3 rounded-lg transition-all whitespace-nowrap"
                    title="Gestión de Datos Maestros"
                >
                    <Box className="w-4 h-4 text-muted-foreground group-hover:text-primary" />
                    <span className="hidden sm:inline">Datos Maestros</span>
                </button>

                <button
                    onClick={() => navigate('/admin/reportes')}
                    className="flex items-center gap-2 text-xs font-medium text-muted-foreground hover:text-primary bg-transparent hover:bg-primary/5 border border-transparent hover:border-primary/20 px-2 py-2 md:px-3 rounded-lg transition-all whitespace-nowrap"
                    title="Reportes"
                >
                    <FileText className="w-4 h-4 text-muted-foreground group-hover:text-primary" />
                    <span className="hidden sm:inline">Reportes</span>
                </button>
            </nav>

            {/* 3. SECCIÓN DERECHA: Acciones de Usuario */}
            <div className="flex items-center gap-1 md:gap-3 justify-self-end z-20">
                
                {/* Botón Modo Concentración (Estilo iOS Premium) */}
                <div className="flex items-center gap-2 sm:gap-3 px-1.5 py-1 select-none">
                    <span className={`text-xs font-semibold tracking-wide transition-colors duration-200 hidden lg:inline ${
                        isFocusMode ? 'text-primary' : 'text-foreground/70'
                    }`}>
                        Modo Concentración
                    </span>
                    
                    <button
                        onClick={() => setIsFocusMode(!isFocusMode)}
                        className={`relative inline-flex h-[22px] w-[42px] flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-250 ease-in-out outline-none focus-visible:ring-2 focus-visible:ring-primary/40 ${
                            isFocusMode 
                                ? 'bg-emerald-500' 
                                : 'bg-zinc-300 dark:bg-zinc-650 hover:bg-zinc-400/80 dark:hover:bg-zinc-500'
                        }`}
                        role="switch"
                        aria-checked={isFocusMode}
                        title={isFocusMode ? "Desactivar modo concentración" : "Activar modo concentración"}
                    >
                        <span
                            className={`pointer-events-none inline-block h-[18px] w-[18px] transform rounded-full bg-white shadow-md transition duration-250 ease-in-out ${
                                isFocusMode ? 'translate-x-[20px]' : 'translate-x-0'
                            }`}
                        />
                    </button>
                </div>

                {/* Configuración */}
                <button
                    className="p-2 text-muted-foreground hover:text-foreground hover:bg-surface-2 rounded-lg transition-colors group"
                    title="Configuración"
                >
                    <Settings className="w-4 h-4 group-hover:rotate-45 transition-transform duration-300" />
                </button>

                {/* Notificaciones */}
                <div className="relative" ref={notifRef}>
                    <button
                        className="relative p-2 text-muted-foreground hover:text-foreground hover:bg-surface-2 rounded-lg transition-colors"
                        onClick={() => setIsNotifOpen(!isNotifOpen)}
                        aria-label="Notificaciones"
                    >
                        <Bell className="w-4 h-4" />
                        {unreadCount > 0 && (
                            <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-destructive rounded-full ring-2 ring-surface-1" />
                        )}
                    </button>
                    <NotificationDropdown isOpen={isNotifOpen} onClose={() => setIsNotifOpen(false)} />
                </div>

                <div className="h-4 w-px bg-border/60 mx-0.5 md:mx-1" />

                {/* Perfil de Usuario */}
                <div className="flex items-center gap-1 md:gap-2">
                    <div className="flex items-center gap-2 p-1 rounded-lg transition-all cursor-pointer border border-transparent hover:border-border/60 group">
                        <div className="w-7 h-7 bg-primary/10 border border-primary/20 text-primary rounded-md flex items-center justify-center font-semibold text-xs">
                            <User className="w-4 h-4" />
                        </div>
                        <span className="hidden xl:block text-xs font-medium text-foreground/90">
                            Perfil
                        </span>
                        <ChevronDown className="hidden md:block w-3.5 h-3.5 text-muted-foreground group-hover:text-foreground transition-colors" />
                    </div>

                    <button
                        onClick={logout}
                        title="Cerrar Sesión"
                        className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/5 rounded-lg transition-colors"
                    >
                        <LogOut className="w-4 h-4" />
                    </button>
                </div>
            </div>
        </header>
    );
}