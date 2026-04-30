import { useState, useRef, useEffect } from 'react';
import { Bell, User, LogOut, ChevronDown } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { useNotifications } from '../../contexts/NotificationContext';
import NotificationDropdown from './NotificationDropdown';

export default function ModernNavbar({ isSidebarOpen = true }) {
    const { user, logout } = useAuth();
    const { unreadCount } = useNotifications();
    const [isNotifOpen, setIsNotifOpen] = useState(false);
    const notifRef = useRef(null);

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
        <header className={`bg-white/80 backdrop-blur-md border-b border-gray-100 h-20 px-8 flex items-center justify-between fixed top-0 right-0 z-40 transition-all duration-300 left-0 ${isSidebarOpen ? 'md:left-64' : 'md:left-20'}`}>
            <div className="flex flex-col">
                <p className="text-[10px] font-black text-gray-400 tracking-widest uppercase mb-0.5">Sistema Operativo </p>
                <h2 className="font-black text-gray-900 leading-tight">
                    Bienvenido, <span className="text-indigo-600 font-black">{user?.name?.split(' ')[0] || 'Administrador'}</span>
                </h2>
            </div>

            <div className="flex items-center gap-6">
                {/* Notification Bell */}
                <div className="relative" ref={notifRef}>
                    <button
                        className="relative p-2.5 hover:bg-gray-50 rounded-xl transition-all group"
                        onClick={() => setIsNotifOpen(!isNotifOpen)}
                    >
                        <Bell className="w-5 h-5 text-gray-500 group-hover:text-indigo-600 transition-colors" />
                        {unreadCount > 0 && (
                            <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full ring-2 ring-white animate-pulse"></span>
                        )}
                        {unreadCount > 0 && (
                            <span className="absolute -top-1 -right-1 bg-red-500 text-white text-[9px] font-black w-4 h-4 flex items-center justify-center rounded-full">
                                {unreadCount}
                            </span>
                        )}
                    </button>

                    <NotificationDropdown
                        isOpen={isNotifOpen}
                        onClose={() => setIsNotifOpen(false)}
                    />
                </div>

                <div className="h-8 w-px bg-gray-100 mx-2"></div>

                <div className="flex items-center gap-4">
                    <div className="flex items-center gap-3 p-1.5 hover:bg-gray-50 rounded-2xl transition-all cursor-pointer group">
                        <div className="w-9 h-9 bg-gradient-to-br from-indigo-600 to-blue-500 rounded-xl flex items-center justify-center shadow-lg group-hover:scale-105 transition-transform">
                            <User className="w-5 h-5 text-white" />
                        </div>
                        <div className="hidden sm:block">
                            <p className="text-xs font-black text-gray-900 leading-none">{user?.name || 'Usuario'}</p>
                            <p className="text-[10px] text-gray-400 font-bold uppercase tracking-tight mt-1">{user?.role || 'Personal'}</p>
                        </div>
                        <ChevronDown className="w-4 h-4 text-gray-300" />
                    </div>

                    <button
                        onClick={logout}
                        className="flex items-center gap-2 px-4 py-2.5 bg-gray-50 hover:bg-red-50 text-gray-600 hover:text-red-600 rounded-xl transition-all text-xs font-black uppercase tracking-widest group"
                    >
                        <LogOut className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
                        <span className="hidden sm:inline">Salir</span>
                    </button>
                </div>
            </div>
        </header>
    );
}
