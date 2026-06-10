import { useState } from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { 
  ShoppingCart, 
  Factory, 
  Package, 
  Users, 
  Activity, 
  ChevronLeft, 
  ChevronRight,
  BarChart3,
  Wallet,
  Shield,
  LayoutDashboard,
  ClipboardList,
  Target,
  FileText,
  Briefcase,
  Truck,
  DollarSign,
  Banknote,
  AlertCircle,
  History,
  Settings
} from 'lucide-react';

export default function ModernSidebar({ isOpen, setIsOpen }) {
  const location = useLocation();
  const [openSubmenu, setOpenSubmenu] = useState(null);

  const menuItems = [
    { path: '/', label: 'Dashboard', icon: BarChart3, color: 'text-blue-600', disabled: false },
    { 
      id: 'comercial',
      label: 'Comercial', 
      icon: Users, 
      color: 'text-blue-500', 
      submenu: [
        { path: '/comercial/tablero', label: 'Tablero Comercial', icon: LayoutDashboard },
        { path: '/comercial/solicitudes-costos', label: 'Solicitudes Costos', icon: DollarSign},
        { path: '/comercial/solicitudes-cotizaciones', label: 'Solicitudes Cotizaciones', icon: DollarSign },
        { path: '/comercial/administracion-negocios', label: 'Admin Negocios', icon: Target },
        { path: '/registros-nv', label: 'Notas de Venta', icon: FileText },
        { path: '/comercial/registros-sc', label: 'Solicitudes (SC)', icon: ClipboardList },
        { path: '/comercial/gestion-proyectos', label: 'Gestión Proyectos', icon: Briefcase },
        { path: '/detalle-nv', label: 'Seguimiento NV', icon: Activity },
        { path: '/comercial/gestion-plantillas', label: 'Biblioteca Plantillas', icon: Settings },
      ]
    },
    { 
      id: 'adquisiciones',
      label: 'Adquisiciones',
      icon: ShoppingCart,
      color: 'text-indigo-600',
      disabled: true,
      submenu: [
        { path: '/dashboard-sc', label: 'Dashboard SC', icon: LayoutDashboard },
        { path: '/adquisiciones/tablero-sc', label: 'Tablero SC', icon: LayoutDashboard },
        { path: '/adquisiciones/cotizaciones', label: 'Gestión Cotizaciones', icon: FileText },
        { path: '/adquisiciones/estado-sc', label: 'Estado SC', icon: Activity },
        { path: '/adquisiciones/emitir-oc', label: 'Emitir OC (SC)', icon: FileText },
        { path: '/recepcionar-oc', label: 'Recepción OC', icon: Truck },
      ]
    },
    { 
      id: 'produccion',
      label: 'Producción', 
      icon: Factory, 
      color: 'text-orange-600',
      submenu: [
        { path: '/dashboard-op', label: 'Control Planta', icon: LayoutDashboard },
        { path: '/produccion/tablero-op', label: 'Tablero OP', icon: LayoutDashboard },
        { path: '/produccion/ordenes', label: 'Ficha Técnica OP', icon: FileText },
        { path: '/op-registro', label: 'Registro OPs', icon: ClipboardList },
        { path: '/produccion/costeo-mp', label: 'Costeos OP', icon: DollarSign },
        { path: '/produccion/compras', label: 'Ordenes de Compra', icon: ShoppingCart },
      ]
    },
    { path: '/bodega', label: 'Bodega', icon: Package, color: 'text-green-600', disabled: true },
    { path: '/contabilidad', label: 'Contabilidad', icon: Wallet, color: 'text-indigo-600', disabled: true },
    { 
      id: 'usuarios',
      label: 'Gestión de Usuarios', 
      icon: Users, 
      color: 'text-violet-600',
      submenu: [
        { path: '/gestion-usuarios/colaboradores', label: 'Colaboradores', icon: Users },
        { path: '/admin/areas', label: 'Gestión Áreas', icon: Briefcase },
        { path: '/admin/roles', label: 'Gestión Roles', icon: Shield },
        { path: '/gestion-usuarios/clientes', label: 'Clientes', icon: Users },
        { path: '/gestion-usuarios/proveedores', label: 'Proveedores', icon: Truck },
        { path: '/gestion-usuarios/vendedores', label: 'Vendedores', icon: BarChart3 },
      ]
    },
    { 
      id: 'trazabilidad',
      label: 'Trazabilidad', 
      icon: Activity, 
      color: 'text-red-600',
      submenu: [
        { path: '/trazabilidad/completa', label: 'Trazabilidad NV', icon: Activity },
        { path: '/trazabilidad/global', label: 'Pipeline Global', icon: Truck },
        { path: '/trazabilidad/alertas', label: 'Monitor de Alertas', icon: AlertCircle },
        { path: '/trazabilidad/historial', label: 'Historial NV', icon: History },
      ]
    },
  ];

  const handleToggleSubmenu = (id) => {
    setOpenSubmenu(openSubmenu === id ? null : id);
  };

  return (
    <aside className={`fixed top-0 left-0 z-50 h-screen transition-all duration-300 bg-sidebar/95 backdrop-blur-md border-r border-sidebar-border flex flex-col ${isOpen ? 'w-64' : 'w-20'}`}>
      {/* Logo */}
      <div className="p-4 border-b border-sidebar-border flex items-center justify-between h-20">
        {isOpen && (
          <div className="flex items-center gap-2 animate-in fade-in duration-300">
            <div className="w-8 h-8 bg-gradient-to-br from-indigo-600 to-blue-500 rounded-lg flex items-center justify-center shadow-lg">
              <span className="font-black text-white text-sm">A</span>
            </div>
            <div>
              <h1 className="font-black text-white text-sm leading-tight">ANTUAN ERP</h1>
              <p className="text-[9px] text-sidebar-foreground/60 tracking-widest font-bold uppercase">V1.0.2 MODULAR</p>
            </div>
          </div>
        )}
        <button 
          onClick={() => setIsOpen(!isOpen)}
          className={`p-1 text-sidebar-foreground hover:bg-sidebar-hover-bg rounded-lg transition-colors ${!isOpen ? 'mx-auto' : ''}`}
        >
          {isOpen ? <ChevronLeft className="w-4 h-4" /> : <ChevronRight className="w-4 h-4" />}
        </button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 overflow-y-auto p-3 space-y-1 custom-scrollbar">
        {menuItems.map((item) => {
          const Icon = item.icon;
          const hasSubmenu = item.submenu && item.submenu.length > 0;
          const isSubmenuOpen = openSubmenu === item.id;
          const isActive = location.pathname === item.path || (hasSubmenu && item.submenu.some(sub => location.pathname === sub.path));

          return (
            <div key={item.id || item.path}>
              {hasSubmenu ? (
                <button
                  onClick={() => {
                    if (item.disabled) return;
                    if (!isOpen) {
                      setIsOpen(true);
                      setOpenSubmenu(item.id);
                    } else {
                      handleToggleSubmenu(item.id);
                    }
                  }}
                  className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all ${
                    item.disabled
                      ? 'opacity-40 cursor-not-allowed text-sidebar-foreground/40'
                      : isSubmenuOpen || isActive
                        ? 'bg-sidebar-active-bg text-sidebar-primary'
                        : 'text-sidebar-foreground hover:bg-sidebar-hover-bg'
                  }`}
                >
                  <Icon className={`w-5 h-5 flex-shrink-0 ${
                    item.disabled ? 'text-sidebar-foreground/30' : isActive || isSubmenuOpen ? 'text-sidebar-primary' : 'text-sidebar-foreground/50'
                  }`} />
                  {isOpen && (
                    <>
                      <span className="text-sm font-semibold flex-1 text-left">{item.label}</span>
                      {item.disabled
                        ? <span className="text-[9px] font-bold uppercase tracking-wider text-sidebar-foreground/40 bg-sidebar-hover-bg px-1.5 py-0.5 rounded">Pronto</span>
                        : <ChevronRight className={`w-4 h-4 transition-transform ${isSubmenuOpen ? 'rotate-90' : ''}`} />
                      }
                    </>
                  )}
                </button>
              ) : item.disabled ? (
                <div className="flex items-center gap-3 px-3 py-2.5 rounded-xl opacity-40 cursor-not-allowed text-sidebar-foreground/40">
                  <Icon className="w-5 h-5 flex-shrink-0 text-sidebar-foreground/30" />
                  {isOpen && (
                    <>
                      <span className="text-sm font-semibold flex-1">{item.label}</span>
                      <span className="text-[9px] font-bold uppercase tracking-wider text-sidebar-foreground/40 bg-sidebar-hover-bg px-1.5 py-0.5 rounded">Pronto</span>
                    </>
                  )}
                </div>
              ) : (
                <NavLink
                  to={item.path}
                  className={({ isActive }) => `
                    flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all
                    ${isActive
                      ? 'bg-sidebar-active-bg text-sidebar-primary'
                      : 'text-sidebar-foreground hover:bg-sidebar-hover-bg'}
                  `}
                >
                  <Icon className={`w-5 h-5 flex-shrink-0 ${isActive ? 'text-sidebar-primary' : 'text-sidebar-foreground/50'}`} />
                  {isOpen && <span className="text-sm font-semibold">{item.label}</span>}
                </NavLink>
              )}

              {/* Submenu */}
              {isOpen && hasSubmenu && isSubmenuOpen && (
                <div className="ml-8 mt-1 space-y-1 border-l-2 border-sidebar-border pl-2 animate-in slide-in-from-top-2 duration-200">
                  {item.submenu.map((subitem) => (
                    <NavLink
                      key={subitem.path}
                      to={subitem.path}
                      className={({ isActive }) => `
                        block px-3 py-2 rounded-lg text-xs font-medium transition-colors
                        ${isActive
                          ? 'bg-sidebar-active-bg text-sidebar-primary'
                          : 'text-sidebar-foreground/80 hover:bg-sidebar-hover-bg'}
                      `}
                    >
                      {subitem.label}
                    </NavLink>
                  ))}
                </div>
              )}
            </div>
          );
        })}
      </nav>

      {/* User Info (Minimalistized version for sidebar) */}
      {isOpen && (
        <div className="p-3 border-t border-sidebar-border">
          <div className="flex items-center gap-3 p-3 bg-sidebar-hover-bg/40 rounded-xl">
            <div className="w-9 h-9 bg-gradient-to-br from-indigo-600 to-blue-500 rounded-full flex items-center justify-center shadow-md">
              <span className="font-bold text-white text-xs">AJ</span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-xs font-bold text-white truncate">Antuan Andrés Jury</p>
              <p className="text-[10px] text-sidebar-foreground/50 font-bold uppercase tracking-tight">Jefe de Planta</p>
            </div>
          </div>
        </div>
      )}
    </aside>
  );
}
