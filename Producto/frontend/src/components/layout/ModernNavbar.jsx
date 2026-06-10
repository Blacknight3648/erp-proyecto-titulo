import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Bell, User, LogOut, ChevronDown, Search, Box, History, Settings } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { useNotifications } from '../../contexts/NotificationContext';
import NotificationDropdown from './NotificationDropdown';

export default function ModernNavbar({ isSidebarOpen = true }) {
    const { user, logout } = useAuth();
    const { unreadCount } = useNotifications();
    const [isNotifOpen, setIsNotifOpen] = useState(false);
    const notifRef = useRef(null);
    const navigate = useNavigate();

    // Close dropdown when clicking outside
    useEffect(() => {
        function handleClickOutside(event) {
            if (notifRef.current && !notifRef.current.contains(event.target)) {
                setIsNotifOpen(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <header className={`bg-surface-1/90 backdrop-blur-md border-b border-border h-20 px-8 flex items-center justify-between fixed top-0 right-0 z-40 transition-all duration-300 left-0 ${isSidebarOpen ? 'md:left-64' : 'md:left-20'}`}>
            
            {/* Left Side - Quick Actions & Status */}
            <div className="flex items-center gap-6">
                {/* Search Bar */}
                <div className="hidden lg:flex items-center relative group">
                    <Search className="w-5 h-5 text-muted-foreground absolute left-4 group-focus-within:text-primary transition-colors" />
                    <input 
                        type="text" 
                        placeholder="Buscar en el sistema..." 
                        className="bg-surface-2 border border-border text-foreground text-sm rounded-xl pl-12 pr-4 py-2.5 w-72 focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all placeholder:text-muted-foreground"
                    />
                </div>

                <div className="hidden md:block h-6 w-px bg-border"></div>

                {/* Quick Links */}
                <nav className="hidden md:flex items-center gap-3">
                    <button 
                        onClick={() => navigate('/admin/datos-maestros')}
                        className="flex items-center gap-2 text-sm font-semibold text-foreground/80 hover:text-primary bg-surface-2 hover:bg-primary/10 border border-border hover:border-primary/20 px-4 py-2 rounded-xl transition-all shadow-sm hover:shadow"
                    >
                        <Box className="w-4 h-4 text-muted-foreground" />
                        <span>Gestión de Datos Maestros</span>
                    </button>
                    <button className="flex items-center gap-2 text-sm font-semibold text-foreground/80 hover:text-primary bg-surface-2 hover:bg-primary/10 border border-border hover:border-primary/20 px-4 py-2 rounded-xl transition-all shadow-sm hover:shadow">
                        <History className="w-4 h-4 text-muted-foreground" />
                        <span>Versiones del Sistema</span>
                    </button>
                </nav>
            </div>

            {/* Right Side - User & Notifications */}
            <div className="flex items-center gap-5">
                
                {/* System Status Indicator */}
                <div className="hidden xl:flex items-center gap-2 bg-success-bg border border-success/20 px-3 py-1.5 rounded-lg mr-2">
                    <span className="relative flex h-2.5 w-2.5">
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-success opacity-75"></span>
                      <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-success"></span>
                    </span>
                    <span className="text-[11px] font-bold text-success uppercase tracking-wider">ERP Operativo</span>
                </div>

                {/* Settings Button */}
                <button className="hidden sm:flex items-center gap-2 px-3 py-2 text-foreground/80 hover:text-primary hover:bg-primary/10 rounded-xl transition-all border border-transparent hover:border-primary/10 group">
                    <Settings className="w-5 h-5 text-muted-foreground group-hover:text-primary group-hover:rotate-90 transition-all duration-300" />
                    <span className="text-sm font-semibold">Configuración</span>
                </button>

                <div className="h-6 w-px bg-border hidden sm:block"></div>

                {/* Notification Bell */}
                <div className="relative" ref={notifRef}>
                    <button
                        className="relative p-2.5 text-foreground/80 hover:text-primary hover:bg-primary/10 rounded-xl transition-all"
                        onClick={() => setIsNotifOpen(!isNotifOpen)}
                    >
                        <Bell className="w-5 h-5" />
                        {unreadCount > 0 && (
                            <span className="absolute top-2 right-2 w-2 h-2 bg-destructive rounded-full ring-2 ring-white animate-pulse"></span>
                        )}
                        {unreadCount > 0 && (
                            <span className="absolute -top-1 -right-1 bg-destructive text-white text-[10px] font-bold w-4.5 h-4.5 flex items-center justify-center rounded-full shadow-sm">
                                {unreadCount}
                            </span>
                        )}
                    </button>

                    <NotificationDropdown
                        isOpen={isNotifOpen}
                        onClose={() => setIsNotifOpen(false)}
                    />
                </div>

                <div className="h-6 w-px bg-border"></div>

                {/* User Profile */}
                <div className="flex items-center gap-3">
                    <div className="flex items-center gap-3 p-1.5 pr-2.5 hover:bg-surface-2 rounded-2xl transition-all cursor-pointer border border-transparent hover:border-border group">
                        <div className="w-9 h-9 bg-primary/10 border border-primary/20 text-primary rounded-xl flex items-center justify-center group-hover:bg-primary group-hover:text-primary-foreground transition-colors">
                            <User className="w-5 h-5" strokeWidth={2} />
                        </div>
                        <div className="hidden sm:block text-left">
                            <p className="text-sm font-bold text-foreground leading-tight">{user?.name || 'Administrador'}</p>
                            <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest mt-0.5">{user?.role || 'Admin'}</p>
                        </div>
                        <ChevronDown className="w-4 h-4 text-muted-foreground ml-1 group-hover:text-foreground transition-colors" />
                    </div>

                    <button
                        onClick={logout}
                        title="Cerrar Sesión"
                        className="p-2.5 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-xl transition-all"
                    >
                        <LogOut className="w-5 h-5" />
                    </button>
                </div>
            </div>
        </header>
    );
}
