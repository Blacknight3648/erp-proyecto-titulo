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
  AlertCircle,
  History,
  Settings,
  Scissors
} from 'lucide-react';

export default function Sidebar({ isOpen, setIsOpen }) {
  const location = useLocation();
  const [openSubmenu, setOpenSubmenu] = useState(null);
  const [hoveredItem, setHoveredItem] = useState(null);

  const menuItems = [
    { path: '/', label: 'Dashboard General', icon: BarChart3 },
    {
      id: 'comercial',
      label: 'Área Comercial',
      icon: Wallet,
      submenu: [
        { path: '/comercial/tablero',                  label: 'Tablero Comercial',      icon: LayoutDashboard },
        { path: '/comercial/solicitudes-costos',        label: 'Análisis de Costos',     icon: DollarSign },
        { path: '/comercial/solicitudes-cotizaciones',  label: 'Cotizaciones',           icon: FileText },
        { path: '/comercial/administracion-negocios',   label: 'Gestión de Negocios',    icon: Target },
        { path: '/registros-nv',                        label: 'Notas de Venta (NV)',    icon: FileText },
        { path: '/comercial/registros-sc',              label: 'Solicitudes Comerciales',icon: ClipboardList },
        { path: '/comercial/gestion-proyectos',         label: 'Proyectos Textiles',     icon: Briefcase },
        { path: '/detalle-nv',                          label: 'Seguimiento Operativo',  icon: Activity },
        { path: '/comercial/gestion-plantillas',        label: 'Modelos y Plantillas',   icon: Settings },
      ]
    },
    {
      id: 'adquisiciones',
      label: 'Adquisiciones',
      icon: ShoppingCart,
      disabled: true,
      submenu: [
        { path: '/dashboard-sc',                label: 'Métricas de Compra',         icon: LayoutDashboard },
        { path: '/adquisiciones/tablero-sc',    label: 'Tablero SC',                 icon: LayoutDashboard },
        { path: '/adquisiciones/cotizaciones',  label: 'Comparativa Proveedores',    icon: FileText },
        { path: '/adquisiciones/estado-sc',     label: 'Monitoreo de Solicitudes',   icon: Activity },
        { path: '/adquisiciones/emitir-oc',     label: 'Órdenes de Compra',          icon: FileText },
        { path: '/recepcionar-oc',              label: 'Control de Recepción',       icon: Truck },
      ]
    },
    {
      id: 'produccion',
      label: 'Producción e Hilados',
      icon: Factory,
      submenu: [
        { path: '/dashboard-op',            label: 'Estado de Planta',           icon: LayoutDashboard },
        { path: '/produccion/tablero-op',   label: 'Planificación Preventiva',   icon: LayoutDashboard },
        { path: '/produccion/ordenes',      label: 'Ficha Técnica Textil',       icon: Scissors },
        { path: '/op-registro',             label: 'Órdenes de Producción',      icon: ClipboardList },
        { path: '/produccion/costeo-mp',    label: 'Costeo de Materia Prima',    icon: DollarSign },
        { path: '/produccion/hoja-compra',  label: 'Hojas de Requerimiento',     icon: ClipboardList },
        { path: '/produccion/compras',      label: 'Suministros de Fábrica',     icon: ShoppingCart },
      ]
    },
    { path: '/bodega',       label: 'Inventario y Bodega',      icon: Package, disabled: true },
    { path: '/contabilidad', label: 'Finanzas y Contabilidad',  icon: Wallet,  disabled: true },
    {
      id: 'usuarios',
      label: 'Gestión de Usuarios',
      icon: Users,
      submenu: [
        { path: '/gestion-usuarios/colaboradores', label: 'Personal Técnico',      icon: Users },
        { path: '/admin/areas',                    label: 'Secciones de Planta',   icon: Briefcase },
        { path: '/admin/roles',                    label: 'Permisos de Sistema',   icon: Shield },
        { path: '/gestion-usuarios/clientes',      label: 'Cartera Clientes',      icon: Users },
        { path: '/gestion-usuarios/proveedores',   label: 'Proveedores Hilados',   icon: Truck },
        { path: '/gestion-usuarios/vendedores',    label: 'Gestores Comerciales',  icon: BarChart3 },
      ]
    },
    {
      id: 'trazabilidad',
      label: 'Trazabilidad Crítica',
      icon: Activity,
      submenu: [
        { path: '/trazabilidad/completa',  label: 'Trazabilidad de Lote',   icon: Activity },
        { path: '/trazabilidad/global',    label: 'Despachos y Logística',  icon: Truck },
        { path: '/trazabilidad/alertas',   label: 'Alertas de Calidad',     icon: AlertCircle },
        { path: '/trazabilidad/historial', label: 'Auditoría Histórica',    icon: History },
      ]
    },
  ];

  const handleToggleSubmenu = (id) =>
    setOpenSubmenu(openSubmenu === id ? null : id);

  /* ── Variantes de clase ── */
  const base     = 'w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all duration-150 text-left';
  const inactive = 'text-[#B7C7DB] hover:text-white hover:bg-[#112645]';
  const opened   = 'bg-[#112645] text-white';
  const active   = 'bg-gradient-to-r from-[#163A63] to-[#204B7A] text-white font-semibold border border-[#2D5F96] shadow-lg shadow-blue-900/30';
  const disabled = 'opacity-30 cursor-not-allowed text-[#4A5B73]';

  const subBase     = 'flex items-center gap-2.5 px-3 py-2 rounded-lg text-xs tracking-wide transition-all duration-150';
  const subInactive = 'text-[#AFC3DD] hover:text-white hover:bg-[#112645]';
  const subActive   = 'bg-[#163A63] text-white font-medium';

  return (
    <>
      {/* Backdrop móvil */}
      {isOpen && (
        <div
          onClick={() => setIsOpen(false)}
          className="fixed inset-0 bg-white/50 backdrop-blur-sm z-40 md:hidden animate-in fade-in duration-200"
        />
      )}

      <aside className={`
        fixed top-0 left-0 z-50 h-screen flex flex-col
        bg-[#081428] border-r border-[#1E3A5F]
        transition-all duration-300
        ${isOpen ? 'translate-x-0 w-64' : '-translate-x-full md:translate-x-0 md:w-20 w-0'}
      `}>

        {/* ── Cabecera / Logo ── */}
        <div className="p-4 border-b border-[#2D5F96]/30 flex items-center justify-between h-20 flex-shrink-0">
          {isOpen ? (
            <div className="flex items-center gap-3 animate-in fade-in duration-200">
              <div className="w-9 h-9 bg-[#102341] border border-[#2D5F96] rounded-xl flex items-center justify-center shadow-inner flex-shrink-0">
                <span className="font-extrabold text-white text-sm tracking-wider">A</span>
              </div>
              <div className="flex flex-col">
                <p className="font-bold text-white text-sm tracking-wide leading-tight">ANTUAN ERP</p>
                <p className="text-[10px] text-[#AFC3DD] tracking-wider font-semibold uppercase">División Textil</p>
              </div>
            </div>
          ) : (
            <div className="mx-auto w-9 h-9 bg-[#102341] border border-[#2D5F96] rounded-xl flex items-center justify-center shadow-inner">
              <span className="font-extrabold text-white text-sm">A</span>
            </div>
          )}

          {isOpen && (
            <button
              onClick={() => setIsOpen(false)}
              className="p-1.5 text-[#B7C7DB] hover:text-white hover:bg-[#112645] rounded-lg transition-all"
              aria-label="Cerrar menú"
            >
              <ChevronLeft className="w-4 h-4" />
            </button>
          )}
        </div>

        {/* ── Navegación ── */}
        <nav className="flex-1 overflow-y-auto p-3 space-y-0.5 scrollbar-thin scrollbar-thumb-[#2D5F96]/20 scrollbar-track-transparent">

          {!isOpen && (
            <button
              onClick={() => setIsOpen(true)}
              className="w-full flex items-center justify-center py-3 mb-2 text-[#B7C7DB] hover:text-white bg-[#0D1D35] hover:bg-[#112645] border border-[#2D5F96]/40 rounded-xl transition-all"
              aria-label="Expandir menú"
            >
              <ChevronRight className="w-4 h-4" />
            </button>
          )}

          {menuItems.map((item) => {
            const Icon = item.icon;
            const hasSubmenu = item.submenu?.length > 0;
            const isSubmenuOpen = openSubmenu === item.id;
            
            // Comprobación estricta de ruta activa (tanto para ruta directa como para hijos)
            const isActive = location.pathname === item.path 
              || (hasSubmenu && item.submenu.some(s => location.pathname === s.path));

            return (
              <div
                key={item.id || item.path}
                className="relative"
                onMouseEnter={() => !isOpen && setHoveredItem(item.id)}
                onMouseLeave={() => !isOpen && setHoveredItem(null)}
              >
                {/* ── Item con submenú ── */}
                {hasSubmenu ? (
                  <button
                    onClick={() => {
                      if (item.disabled) return;
                      if (!isOpen) { setIsOpen(true); setOpenSubmenu(item.id); }
                      else handleToggleSubmenu(item.id);
                    }}
                    className={`${base} ${
                      item.disabled   ? disabled
                      : isActive      ? active
                      : isSubmenuOpen ? opened
                      : inactive
                    }`}
                  >
                    <Icon className={`w-5 h-5 flex-shrink-0 transition-colors ${
                      item.disabled ? 'text-[#4A5B73]' : isActive ? 'text-[#60A5FA]' : 'text-[#B7C7DB]'
                    }`} />
                    {isOpen && (
                      <>
                        <span className="text-sm font-medium flex-1 text-left tracking-wide">{item.label}</span>
                        {item.disabled
                          ? <span className="text-[9px] font-semibold uppercase tracking-wider text-[#4A5B73] bg-[#050D1A]/40 px-1.5 py-0.5 rounded-md">Pronto</span>
                          : <ChevronRight className={`w-3.5 h-3.5 text-[#B7C7DB] transition-transform duration-200 ${isSubmenuOpen ? 'rotate-90 text-white' : ''}`} />
                        }
                      </>
                    )}
                  </button>

                ) : item.disabled ? (
                  /* ── Item deshabilitado sin ruta ── */
                  <div className={`${base} ${disabled}`}>
                    <Icon className="w-5 h-5 flex-shrink-0 text-[#4A5B73]" />
                    {isOpen && (
                      <>
                        <span className="text-sm font-medium flex-1 tracking-wide">{item.label}</span>
                        <span className="text-[9px] font-semibold uppercase tracking-wider text-[#4A5B73] bg-[#050D1A]/40 px-1.5 py-0.5 rounded-md">Pronto</span>
                      </>
                    )}
                  </div>

                ) : (
                  /* ── NavLink directo (Dashboard General, etc) ── */
                  <NavLink
                    to={item.path}
                    className={`${base} border border-transparent ${isActive ? active : inactive}`}
                  >
                    <Icon className={`w-5 h-5 flex-shrink-0 ${isActive ? 'text-[#60A5FA]' : 'text-[#B7C7DB]'}`} />
                    {isOpen && <span className="text-sm font-medium tracking-wide">{item.label}</span>}
                  </NavLink>
                )}

                {/* ── Submenú desplegado (sidebar abierto) ── */}
                {isOpen && hasSubmenu && isSubmenuOpen && (
                  <div className="ml-5 mt-1 space-y-0.5 border-l border-[#2D5F96]/40 pl-3 animate-in slide-in-from-top-1 duration-150">
                    {item.submenu.map((sub) => {
                      const SubIcon = sub.icon;
                      // El hijo está activo si coincide exactamente con la URL
                      const isSubActive = location.pathname === sub.path;
                      return (
                        <NavLink
                          key={sub.path}
                          to={sub.path}
                          className={`${subBase} ${isSubActive ? subActive : subInactive}`}
                        >
                          <SubIcon className={`w-3.5 h-3.5 flex-shrink-0 ${isSubActive ? 'opacity-100' : 'opacity-60'}`} />
                          <span>{sub.label}</span>
                        </NavLink>
                      );
                    })}
                  </div>
                )}

                {/* ── Submenú flotante (sidebar colapsado) ── */}
                {!isOpen && hasSubmenu && hoveredItem === item.id && (
                  <div className="absolute left-[calc(100%-4px)] top-0 ml-3 bg-[#0D1D35] border border-[#2D5F96] rounded-2xl p-2 w-60 shadow-2xl z-50 animate-in fade-in slide-in-from-left-1 duration-100">
                    <div className="px-3 py-2 border-b border-[#2D5F96]/30 mb-1">
                      <span className="text-xs font-bold text-[#B7C7DB] tracking-wide uppercase">{item.label}</span>
                    </div>
                    <div className="space-y-0.5">
                      {item.submenu.map((sub) => {
                        const SubIcon = sub.icon;
                        const isSubActive = location.pathname === sub.path;
                        return (
                          <NavLink
                            key={sub.path}
                            to={sub.path}
                            className={`${subBase} ${isSubActive ? subActive : subInactive}`}
                          >
                            <SubIcon className={`w-3.5 h-3.5 flex-shrink-0 ${isSubActive ? 'opacity-100' : 'opacity-60'}`} />
                            <span>{sub.label}</span>
                          </NavLink>
                        );
                      })}
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </nav>

        {/* ── Footer / Sesión ── */}
        <div className="p-3 border-t border-[#2D5F96]/20 bg-black/10 flex-shrink-0">
          <div className={`flex items-center gap-3 p-2 rounded-xl transition-all ${
            isOpen ? 'bg-[#0D1D35] border border-[#2D5F96]/40' : 'justify-center'
          }`}>
            <div className="w-9 h-9 bg-[#163A63] border border-[#2D5F96] rounded-full flex items-center justify-center shadow-sm flex-shrink-0">
              <span className="font-semibold text-white text-xs">AJ</span>
            </div>
            {isOpen && (
              <div className="flex-1 min-w-0">
                <p className="text-xs font-semibold text-white truncate">Antuan Andrés Jury</p>
                <p className="text-[10px] text-[#AFC3DD] font-medium tracking-wide uppercase">Jefe de Planta</p>
              </div>
            )}
          </div>
        </div>
      </aside>
    </>
  );
}