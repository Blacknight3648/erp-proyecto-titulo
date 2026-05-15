import { useState } from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import {
  Home,
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
    { path: '/', label: 'Inicio', icon: Home, color: 'text-blue-600' },
    {
      id: 'comercial',
      label: 'Comercial',
      icon: Users,
      color: 'text-blue-500',
      submenu: [
        { path: '/comercial/tablero', label: 'Tablero Comercial', icon: LayoutDashboard },
        { path: '/comercial/solicitudes-costos', label: 'Solicitudes Costos', icon: DollarSign },
        { path: '/comercial/administracion-negocios', label: 'Admin Negocios', icon: Target },
        { path: '/registros-nv', label: 'Notas de Venta', icon: FileText },
        { path: '/comercial/gestion-proyectos', label: 'Gestión Proyectos', icon: Briefcase },
        { path: '/detalle-nv', label: 'Seguimiento NV', icon: Activity },
        { path: '/comercial/gestion-plantillas', label: 'Biblioteca Plantillas', icon: Settings },
        { path: '/comercial/ordenes-produccion', label: 'Ordenes de Produccion', icon: DollarSign },
      ]
    },
    { id: 'adquisiciones', path: '#', label: 'Adquisiciones', icon: ShoppingCart, color: 'text-indigo-600' },
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
        { path: '/produccion/hoja-compra', label: 'Hoja de Compra', icon: ClipboardList },
        { path: '/produccion/compras', label: 'Ordenes de Compra', icon: ShoppingCart },
      ]
    },
    { id: 'bodega', path: '#', label: 'Bodega', icon: Package, color: 'text-green-600' },
    { id: 'contabilidad', path: '#', label: 'Contabilidad', icon: Wallet, color: 'text-indigo-600' },
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
      ]
    },
    {
      id: 'trazabilidad',
      label: 'Trazabilidad',
      icon: Activity,
      color: 'text-red-600',
      submenu: [
        { path: '/trazabilidad/completa', label: 'Trazabilidad Comercial-Producción', icon: Activity },
        { path: '/trazabilidad/global', label: 'Timeline de Pedidos', icon: Truck },
      ]
    },
  ];

  const handleToggleSubmenu = (id) => {
    setOpenSubmenu(openSubmenu === id ? null : id);
  };

  return (
    <aside className={`fixed top-0 left-0 z-50 h-screen transition-all duration-300 bg-white border-r border-gray-200 flex flex-col ${isOpen ? 'w-64' : 'w-20'}`}>
      {/* Logo */}
      <div className="p-4 border-b border-gray-200 flex items-center justify-between h-20">
        {isOpen && (
          <div className="flex items-center gap-2 animate-in fade-in duration-300">
            <div className="w-8 h-8 bg-gradient-to-br from-indigo-600 to-blue-500 rounded-lg flex items-center justify-center shadow-lg">
              <span className="font-black text-white text-sm">A</span>
            </div>
            <div>
              <h1 className="font-black text-gray-900 text-sm leading-tight">ANTUAN ERP</h1>
              <p className="text-[9px] text-gray-500 tracking-widest font-bold uppercase">V1.0.2 MODULAR</p>
            </div>
          </div>
        )}
        <button
          onClick={() => setIsOpen(!isOpen)}
          className={`p-1 hover:bg-gray-100 rounded-lg transition-colors ${!isOpen ? 'mx-auto' : ''}`}
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
                    if (!isOpen) {
                      setIsOpen(true);
                      setOpenSubmenu(item.id);
                    } else {
                      handleToggleSubmenu(item.id);
                    }
                  }}
                  className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all ${isSubmenuOpen || isActive
                    ? 'bg-indigo-50 text-indigo-600'
                    : 'text-gray-700 hover:bg-gray-50'
                    }`}
                >
                  <Icon className={`w-5 h-5 flex-shrink-0 ${isActive || isSubmenuOpen ? item.color : 'text-gray-400'}`} />
                  {isOpen && (
                    <>
                      <span className="text-sm font-semibold flex-1 text-left">{item.label}</span>
                      <ChevronRight className={`w-4 h-4 transition-transform ${isSubmenuOpen ? 'rotate-90' : ''}`} />
                    </>
                  )}
                </button>
              ) : (
                <NavLink
                  to={item.path}
                  className={({ isActive }) => `
                    flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all
                    ${isActive
                      ? 'bg-indigo-50 text-indigo-600'
                      : 'text-gray-700 hover:bg-gray-50'}
                  `}
                >
                  <Icon className={`w-5 h-5 flex-shrink-0 ${isActive ? item.color : 'text-gray-400'}`} />
                  {isOpen && <span className="text-sm font-semibold">{item.label}</span>}
                </NavLink>
              )}

              {/* Submenu */}
              {isOpen && hasSubmenu && isSubmenuOpen && (
                <div className="ml-8 mt-1 space-y-1 border-l-2 border-indigo-100 pl-2 animate-in slide-in-from-top-2 duration-200">
                  {item.submenu.map((subitem) => (
                    <NavLink
                      key={subitem.path}
                      to={subitem.path}
                      className={({ isActive }) => `
                        block px-3 py-2 rounded-lg text-xs font-medium transition-colors
                        ${isActive
                          ? 'bg-indigo-50 text-indigo-600'
                          : 'text-gray-600 hover:bg-gray-50'}
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
        <div className="p-3 border-t border-gray-100">
          <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl">
            <div className="w-9 h-9 bg-gradient-to-br from-indigo-600 to-blue-500 rounded-full flex items-center justify-center shadow-md">
              <span className="font-bold text-white text-xs">JS</span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-xs font-bold text-gray-900 truncate">Juan Soto</p>
              <p className="text-[10px] text-gray-400 font-bold uppercase tracking-tight">Jefe de Planta</p>
            </div>
          </div>
        </div>
      )}
    </aside>
  );
}
