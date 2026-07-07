import React from 'react';
import { Check, Info, Box, Truck, AlertCircle, ShoppingCart, ClipboardList, Wrench } from 'lucide-react';
import { useNotifications } from '@contexts/NotificationContext';

const ICON_MAP = {
  OC:   <ShoppingCart   className="w-5 h-5 text-accent-foreground"  />,
  HC:   <ClipboardList  className="w-5 h-5 text-accent-foreground" />,
  OS:   <Wrench         className="w-5 h-5 text-accent-foreground" />,
  LOGO: <Info           className="w-5 h-5 text-warning"/>,
  MP:   <Box            className="w-5 h-5 text-success" />,
};

function NotificationItem({ notification, onRead }) {
  const icon = ICON_MAP[notification.type] ?? <AlertCircle className="w-5 h-5 text-muted-foreground" />;

  return (
    <div
      onClick={() => onRead(notification.id)}
      className={`p-4 border-b hover:bg-muted cursor-pointer transition-colors ${!notification.read ? 'bg-accent/50' : ''}`}
    >
      <div className="flex gap-3">
        <div className="mt-1">{icon}</div>
        <div className="flex-1">
          <p className={`text-sm ${!notification.read ? 'font-semibold text-foreground' : 'text-muted-foreground'}`}>
            {notification.message}
          </p>
          <div className="flex justify-between items-center mt-1">
            <span className="text-xs text-muted-foreground">{notification.timestamp}</span>
            {notification.category && (
              <span className="text-[10px] font-bold px-2 py-0.5 bg-muted text-muted-foreground rounded-full">
                {notification.category}
              </span>
            )}
          </div>
        </div>
        {!notification.read && <div className="w-2 h-2 rounded-full bg-primary mt-2" />}
      </div>
    </div>
  );
}

export default function NotificationDropdown({ isOpen, onClose }) {
  const { notifications, markAsRead, markAllAsRead, unreadCount } = useNotifications();

  if (!isOpen) return null;

  return (
    <div className="absolute top-12 right-0 w-80 bg-card rounded-xl shadow-2xl border border-border z-50 overflow-hidden">
      <div className="p-3 border-b bg-card flex justify-between items-center sticky top-0">
        <h3 className="font-bold text-foreground">Notificaciones</h3>
        {unreadCount > 0 && (
          <button
            onClick={markAllAsRead}
            className="text-xs font-bold text-accent-foreground hover:text-accent-foreground flex items-center gap-1"
          >
            <Check className="w-3 h-3" /> Marcar leídas
          </button>
        )}
      </div>

      <div className="max-h-96 overflow-y-auto">
        {notifications.length === 0 ? (
          <div className="p-8 text-center text-muted-foreground text-sm">No tienes notificaciones</div>
        ) : (
          notifications.map(notif => (
            <NotificationItem key={notif.id} notification={notif} onRead={markAsRead} />
          ))
        )}
      </div>

      <div className="p-2 bg-muted text-center border-t">
        <button className="text-xs font-bold text-muted-foreground hover:text-foreground">Ver todo</button>
      </div>
    </div>
  );
}
