
import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { mockNotifications } from '../data/mockData';

export interface Notification {
    id: number;
    type: string;
    message: string;
    timestamp: string;
    read: boolean;
    priority: string;
    category: string;
}

export interface NewNotification {
    type: string;
    message: string;
    priority?: string;
    category?: string;
}

export interface NotificationContextType {
    notifications: Notification[];
    unreadCount: number;
    markAsRead: (id: number) => void;
    markAllAsRead: () => void;
    addNotification: (notification: NewNotification) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export function useNotifications() {
    const context = useContext(NotificationContext);
    if (!context) {
        throw new Error('useNotifications must be used within a NotificationProvider');
    }
    return context;
}

interface NotificationProviderProps {
    children: ReactNode;
}

export function NotificationProvider({ children }: NotificationProviderProps) {
    const [notifications, setNotifications] = useState<Notification[]>([]);
    const [unreadCount, setUnreadCount] = useState<number>(0);

    // Initialize with mock data
    useEffect(() => {
        const initialNotifications = (mockNotifications || []) as Notification[];
        setNotifications(initialNotifications);
        setUnreadCount(initialNotifications.filter(n => !n.read).length);
    }, []);

    const markAsRead = (id: number) => {
        setNotifications(prev => prev.map(n =>
            n.id === id ? { ...n, read: true } : n
        ));
        setUnreadCount(prev => Math.max(0, prev - 1));
    };

    const markAllAsRead = () => {
        setNotifications(prev => prev.map(n => ({ ...n, read: true })));
        setUnreadCount(0);
    };

    const addNotification = (notification: NewNotification) => {
        const newNotif: Notification = {
            id: Date.now(),
            timestamp: 'Ahora',
            read: false,
            priority: notification.priority ?? 'normal',
            category: notification.category ?? 'GENERAL',
            ...notification
        };
        setNotifications(prev => [newNotif, ...prev]);
        setUnreadCount(prev => prev + 1);
    };

    const value = {
        notifications,
        unreadCount,
        markAsRead,
        markAllAsRead,
        addNotification
    };

    return (
        <NotificationContext.Provider value={value}>
            {children}
        </NotificationContext.Provider>
    );
}

