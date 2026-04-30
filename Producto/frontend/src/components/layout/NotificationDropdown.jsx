
import React from 'react';
import { useNotifications } from '../../contexts/NotificationContext';
import { Check, Info, Box, Truck, AlertCircle, ShoppingCart } from 'lucide-react';

const NotificationItem = ({ notification, onRead }) => {
    const getIcon = () => {
        switch (notification.type) {
            case 'OC': return <ShoppingCart className="w-5 h-5 text-blue-500" />;
            case 'LOGO': return <Info className="w-5 h-5 text-orange-500" />;
            case 'MP': return <Box className="w-5 h-5 text-green-500" />;
            default: return <AlertCircle className="w-5 h-5 text-gray-500" />;
        }
    };

    return (
        <div
            className={`p-4 border-b hover:bg-gray-50 cursor-pointer transition-colors ${!notification.read ? 'bg-blue-50/50' : ''}`}
            onClick={() => onRead(notification.id)}
        >
            <div className="flex gap-3">
                <div className="mt-1">
                    {getIcon()}
                </div>
                <div className="flex-1">
                    <p className={`text-sm ${!notification.read ? 'font-semibold text-gray-800' : 'text-gray-600'}`}>
                        {notification.message}
                    </p>
                    <div className="flex justify-between items-center mt-1">
                        <span className="text-xs text-gray-400">{notification.timestamp}</span>
                        {notification.category && (
                            <span className="text-[10px] font-bold px-2 py-0.5 bg-gray-100 text-gray-500 rounded-full">
                                {notification.category}
                            </span>
                        )}
                    </div>
                </div>
                {!notification.read && (
                    <div className="w-2 h-2 rounded-full bg-blue-500 mt-2"></div>
                )}
            </div>
        </div>
    );
};

export default function NotificationDropdown({ isOpen, onClose }) {
    const { notifications, markAsRead, markAllAsRead, unreadCount } = useNotifications();

    if (!isOpen) return null;

    return (
        <div className="absolute top-12 right-0 w-80 bg-white rounded-xl shadow-2xl border border-gray-100 z-50 overflow-hidden transform transition-all">
            <div className="p-3 border-b bg-white flex justify-between items-center sticky top-0">
                <h3 className="font-bold text-gray-700">Notificaciones</h3>
                {unreadCount > 0 && (
                    <button
                        onClick={markAllAsRead}
                        className="text-xs font-bold text-blue-600 hover:text-blue-700 flex items-center"
                    >
                        <Check className="w-3 h-3 mr-1" /> Marcar leídas
                    </button>
                )}
            </div>

            <div className="max-h-96 overflow-y-auto">
                {notifications.length === 0 ? (
                    <div className="p-8 text-center text-gray-400">
                        <p className="text-sm">No tienes notificaciones</p>
                    </div>
                ) : (
                    notifications.map(notif => (
                        <NotificationItem
                            key={notif.id}
                            notification={notif}
                            onRead={markAsRead}
                        />
                    ))
                )}
            </div>

            <div className="p-2 bg-gray-50 text-center border-t">
                <button className="text-xs font-bold text-gray-500 hover:text-gray-700">
                    Ver todo
                </button>
            </div>
        </div>
    );
}
