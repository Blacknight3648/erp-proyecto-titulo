
import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { NotificacionService } from '../remote/service/NotificacionService';

export interface Notification {
    id: number;
    type: string;
    message: string;
    timestamp: string;
    read: boolean;
    priority: string;
    category: string;
}

export interface NotificationContextType {
    notifications: Notification[];
    unreadCount: number;
    markAsRead: (id: number) => void;
    markAllAsRead: () => void;
    refresh: () => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export function useNotifications() {
    const context = useContext(NotificationContext);
    if (!context) {
        throw new Error('useNotifications must be used within a NotificationProvider');
    }
    return context;
}

// Los mismos umbrales que usaba el mock ("Hace 10 min"), pero calculados desde la
// fecha real que entrega el backend (NotificacionDTO.fecha).
function formatRelativeTime(fechaISO: string): string {
    if (!fechaISO) return '';
    const fecha = new Date(fechaISO);
    const diffMin = Math.floor((Date.now() - fecha.getTime()) / 60000);
    if (diffMin < 1) return 'Ahora';
    if (diffMin < 60) return `Hace ${diffMin} min`;
    const diffH = Math.floor(diffMin / 60);
    if (diffH < 24) return `Hace ${diffH} hora${diffH === 1 ? '' : 's'}`;
    const diffD = Math.floor(diffH / 24);
    return `Hace ${diffD} día${diffD === 1 ? '' : 's'}`;
}

function toNotification(dto): Notification {
    return {
        id: dto.id,
        type: dto.tipo,
        message: dto.mensaje,
        timestamp: formatRelativeTime(dto.fecha),
        read: !!dto.leida,
        priority: dto.prioridad,
        category: dto.categoria,
    };
}

const POLL_INTERVAL_MS = 60000;

interface NotificationProviderProps {
    children: ReactNode;
}

export function NotificationProvider({ children }: NotificationProviderProps) {
    const [notifications, setNotifications] = useState<Notification[]>([]);
    const [unreadCount, setUnreadCount] = useState<number>(0);

    const refresh = useCallback(() => {
        NotificacionService.getAll()
            .then((data) => {
                const mapped = (data || []).map(toNotification);
                setNotifications(mapped);
                setUnreadCount(mapped.filter(n => !n.read).length);
            })
            .catch(() => {
                setNotifications([]);
                setUnreadCount(0);
            });
    }, []);

    useEffect(() => {
        refresh();
        const interval = setInterval(refresh, POLL_INTERVAL_MS);
        return () => clearInterval(interval);
    }, [refresh]);

    const markAsRead = (id: number) => {
        setNotifications(prev => prev.map(n => (n.id === id ? { ...n, read: true } : n)));
        setUnreadCount(prev => Math.max(0, prev - 1));
        NotificacionService.marcarLeida(id).catch(() => refresh());
    };

    const markAllAsRead = () => {
        setNotifications(prev => prev.map(n => ({ ...n, read: true })));
        setUnreadCount(0);
        NotificacionService.marcarTodasLeidas().catch(() => refresh());
    };

    const value = {
        notifications,
        unreadCount,
        markAsRead,
        markAllAsRead,
        refresh,
    };

    return (
        <NotificationContext.Provider value={value}>
            {children}
        </NotificationContext.Provider>
    );
}
