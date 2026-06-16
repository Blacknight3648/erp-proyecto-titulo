import { useState } from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import {
  ShoppingCart, Factory, Package, Users, Activity,
  ChevronLeft, ChevronRight, BarChart3, Wallet, Shield,
  LayoutDashboard, ClipboardList, Target, FileText,
  Briefcase, Truck, DollarSign, AlertCircle, History,
  Settings, Scissors
} from 'lucide-react';

/* ─────────────────────────────────────────
   TOKENS DE COLOR
───────────────────────────────────────── */
const C = {
  bg:           '#081428',
  bgPanel:      '#0D1D35',
  bgHover:      '#112645',
  bgActive:     '#163A63',
  border:       '#1E3A5F',
  borderAccent: '#2D5F96',
  textPrimary:  '#FFFFFF',
  textSub:      '#D4E4F7',   // FIX: contraste legible (~5.2:1) vs fondo oscuro
  textMuted:    '#8BAAC8',
  textDisabled: '#4A5B73',
  iconActive:   '#60A5FA',
  iconOpen:     '#93C5FD',
  shadow:       'rgba(30,58,95,0.4)',
};

/* ─────────────────────────────────────────
   DATOS DEL MENÚ
───────────────────────────────────────── */
const menuItems = [
  { path: '/', label: 'Dashboard General', icon: BarChart3 },
  {
    id: 'comercial', label: 'Área Comercial', icon: Wallet,
    submenu: [
      { path: '/comercial/tablero',                  label: 'Tablero Comercial',       icon: LayoutDashboard },
      { path: '/comercial/solicitudes-costos',        label: 'Análisis de Costos',      icon: DollarSign },
      { path: '/comercial/solicitudes-cotizaciones',  label: 'Cotizaciones',            icon: FileText },
      { path: '/comercial/administracion-negocios',   label: 'Gestión de Negocios',     icon: Target },
      { path: '/registros-nv',                        label: 'Notas de Venta (NV)',     icon: FileText },
      { path: '/comercial/registros-sc',              label: 'Solicitudes Comerciales', icon: ClipboardList },
      { path: '/comercial/gestion-proyectos',         label: 'Proyectos Textiles',      icon: Briefcase },
      { path: '/detalle-nv',                          label: 'Seguimiento Operativo',   icon: Activity },
      { path: '/comercial/gestion-plantillas',        label: 'Modelos y Plantillas',    icon: Settings },
    ],
  },
  {
    id: 'adquisiciones', label: 'Adquisiciones', icon: ShoppingCart, disabled: true,
    submenu: [
      { path: '/dashboard-sc',               label: 'Métricas de Compra',       icon: LayoutDashboard },
      { path: '/adquisiciones/tablero-sc',   label: 'Tablero SC',               icon: LayoutDashboard },
      { path: '/adquisiciones/cotizaciones', label: 'Comparativa Proveedores',  icon: FileText },
      { path: '/adquisiciones/estado-sc',    label: 'Monitoreo de Solicitudes', icon: Activity },
      { path: '/adquisiciones/emitir-oc',    label: 'Órdenes de Compra',        icon: FileText },
      { path: '/recepcionar-oc',             label: 'Control de Recepción',     icon: Truck },
    ],
  },
  {
    id: 'produccion', label: 'Producción e Hilados', icon: Factory,
    submenu: [
      { path: '/dashboard-op',           label: 'Estado de Planta',         icon: LayoutDashboard },
      { path: '/produccion/tablero-op',  label: 'Planificación Preventiva', icon: LayoutDashboard },
      { path: '/produccion/ordenes',     label: 'Ficha Técnica Textil',     icon: Scissors },
      { path: '/op-registro',            label: 'Órdenes de Producción',    icon: ClipboardList },
      { path: '/produccion/costeo-mp',   label: 'Costeo de Materia Prima',  icon: DollarSign },
      { path: '/produccion/hoja-compra', label: 'Hojas de Requerimiento',   icon: ClipboardList },
      { path: '/produccion/compras',     label: 'Suministros de Fábrica',   icon: ShoppingCart },
    ],
  },
  { path: '/bodega',       label: 'Inventario y Bodega',     icon: Package, disabled: true },
  { path: '/contabilidad', label: 'Finanzas y Contabilidad', icon: Wallet,  disabled: true },
  {
    id: 'usuarios', label: 'Gestión de Usuarios', icon: Users,
    submenu: [
      { path: '/gestion-usuarios/colaboradores', label: 'Personal Técnico',     icon: Users },
      { path: '/admin/areas',                    label: 'Secciones de Planta',  icon: Briefcase },
      { path: '/admin/roles',                    label: 'Permisos de Sistema',  icon: Shield },
      { path: '/gestion-usuarios/clientes',      label: 'Cartera Clientes',     icon: Users },
      { path: '/gestion-usuarios/proveedores',   label: 'Proveedores Hilados',  icon: Truck },
      { path: '/gestion-usuarios/vendedores',    label: 'Gestores Comerciales', icon: BarChart3 },
    ],
  },
  {
    id: 'trazabilidad', label: 'Trazabilidad Crítica', icon: Activity,
    submenu: [
      { path: '/trazabilidad/completa',  label: 'Trazabilidad de Lote',  icon: Activity },
      { path: '/trazabilidad/global',    label: 'Despachos y Logística', icon: Truck },
      { path: '/trazabilidad/alertas',   label: 'Alertas de Calidad',    icon: AlertCircle },
      { path: '/trazabilidad/historial', label: 'Auditoría Histórica',   icon: History },
    ],
  },
];

/* ─────────────────────────────────────────
   COMPONENTE
───────────────────────────────────────── */
export default function Sidebar({ isOpen, setIsOpen }) {
  const location = useLocation();
  const [openSubmenu, setOpenSubmenu]   = useState(null);
  const [hoveredItem, setHoveredItem]   = useState(null);
  const [hoveredSub,  setHoveredSub]    = useState(null);
  const [hoveredChev, setHoveredChev]   = useState(false);
  const [hoveredExp,  setHoveredExp]    = useState(false);

  const toggleSubmenu = (id) => setOpenSubmenu(openSubmenu === id ? null : id);

  /* Sub-items compartidos entre sidebar abierto y menú flotante */
  const renderSubItems = (submenu) =>
    submenu.map((sub) => {
      const SubIcon = sub.icon;
      const isSubActive = location.pathname === sub.path;
      const isHov = hoveredSub === sub.path;
      return (
        <NavLink
          key={sub.path}
          to={sub.path}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '10px',
            padding: '8px 12px',
            borderRadius: '8px',
            fontSize: '12px',
            letterSpacing: '0.03em',
            textDecoration: 'none',
            whiteSpace: 'nowrap',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            transition: 'background 150ms, color 150ms',
            background: isSubActive ? C.bgActive : isHov ? C.bgHover : 'transparent',
            color:      isSubActive || isHov ? C.textPrimary : C.textSub,
            fontWeight: isSubActive ? 500 : 400,
          }}
          onMouseEnter={() => setHoveredSub(sub.path)}
          onMouseLeave={() => setHoveredSub(null)}
        >
          <SubIcon style={{
            width: '14px', height: '14px', flexShrink: 0,
            opacity: isSubActive ? 1 : 0.85,
            color: isSubActive ? C.iconActive : 'inherit',
          }} />
          <span style={{ overflow: 'hidden', textOverflow: 'ellipsis' }}>{sub.label}</span>
        </NavLink>
      );
    });

  return (
    <>
      {/* Estilos mínimos que el inline no puede cubrir (scrollbar, hover mediaqueries) */}
      <style>{`
        #sb-nav::-webkit-scrollbar       { width: 4px; }
        #sb-nav::-webkit-scrollbar-thumb { background: ${C.borderAccent}55; border-radius: 4px; }
        #sb-nav::-webkit-scrollbar-track { background: transparent; }
      `}</style>

      {/* Backdrop móvil */}
      {isOpen && (
        <div
          onClick={() => setIsOpen(false)}
          style={{
            position: 'fixed', inset: 0, zIndex: 40,
            background: 'rgba(0,0,0,0.5)',
            backdropFilter: 'blur(4px)',
          }}
        />
      )}

      <aside style={{
        position: 'fixed', top: 0, left: 0, zIndex: 50,
        height: '100vh',
        display: 'flex', flexDirection: 'column',
        backgroundColor: C.bg,
        borderRight: `1px solid ${C.border}`,
        transition: 'width 300ms ease, transform 300ms ease',
        width: isOpen ? '256px' : '80px',
        overflowX: 'hidden',
      }}>

        {/* ── Cabecera ── */}
        <div style={{
          padding: '16px',
          borderBottom: `1px solid ${C.borderAccent}33`,
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          height: '80px', flexShrink: 0,
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', overflow: 'hidden' }}>
            {/* Logo box */}
            <div style={{
              width: '36px', height: '36px', flexShrink: 0,
              backgroundColor: '#102341',
              border: `1px solid ${C.borderAccent}`,
              borderRadius: '12px',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
            }}>
              <span style={{ fontWeight: 800, color: C.textPrimary, fontSize: '14px', letterSpacing: '0.05em' }}>A</span>
            </div>

            {isOpen && (
              <div style={{ display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                <p style={{ margin: 0, fontWeight: 700, color: C.textPrimary, fontSize: '14px', letterSpacing: '0.05em', lineHeight: 1.2 }}>
                  ANTUAN ERP
                </p>
                <p style={{ margin: 0, fontSize: '10px', color: C.textMuted, letterSpacing: '0.1em', fontWeight: 600, textTransform: 'uppercase' }}>
                  División Textil
                </p>
              </div>
            )}
          </div>

          {isOpen && (
            <button
              onClick={() => setIsOpen(false)}
              onMouseEnter={() => setHoveredChev(true)}
              onMouseLeave={() => setHoveredChev(false)}
              aria-label="Cerrar menú"
              style={{
                padding: '6px', border: 'none', borderRadius: '8px', cursor: 'pointer',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                background: hoveredChev ? C.bgHover : 'transparent',
                color:      hoveredChev ? C.textPrimary : C.textMuted,
                transition: 'background 150ms, color 150ms',
              }}
            >
              <ChevronLeft style={{ width: '16px', height: '16px' }} />
            </button>
          )}
        </div>

        {/* ── Navegación ── */}
        <nav id="sb-nav" style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', padding: '12px' }}>

          {/* Botón expandir (sidebar colapsado) */}
          {!isOpen && (
            <button
              onClick={() => setIsOpen(true)}
              onMouseEnter={() => setHoveredExp(true)}
              onMouseLeave={() => setHoveredExp(false)}
              aria-label="Expandir menú"
              style={{
                width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center',
                padding: '10px', marginBottom: '8px', border: `1px solid ${C.borderAccent}66`,
                borderRadius: '12px', cursor: 'pointer',
                background: hoveredExp ? C.bgHover : C.bgPanel,
                color:      hoveredExp ? C.textPrimary : C.textMuted,
                transition: 'background 150ms, color 150ms',
              }}
            >
              <ChevronRight style={{ width: '16px', height: '16px' }} />
            </button>
          )}

          <div style={{ display: 'flex', flexDirection: 'column', gap: '2px' }}>
            {menuItems.map((item) => {
              const Icon = item.icon;
              const hasSubmenu    = !!item.submenu?.length;
              const isSubmenuOpen = openSubmenu === item.id;
              const isActive =
                location.pathname === item.path ||
                (hasSubmenu && item.submenu.some((s) => location.pathname === s.path));

              const itemStyle = {
                width: '100%', boxSizing: 'border-box',
                display: 'flex', alignItems: 'center', gap: '12px',
                padding: '10px 12px', borderRadius: '12px',
                border: isActive ? `1px solid ${C.borderAccent}` : '1px solid transparent',
                background: isActive
                  ? 'linear-gradient(to right, #163A63, #204B7A)'
                  : isSubmenuOpen ? C.bgHover : 'transparent',
                color:      isActive ? C.textPrimary : item.disabled ? C.textDisabled : C.textSub,
                fontWeight: isActive ? 600 : 400,
                boxShadow:  isActive ? `0 4px 16px ${C.shadow}` : 'none',
                opacity:    item.disabled ? 0.4 : 1,
                cursor:     item.disabled ? 'not-allowed' : 'pointer',
                transition: 'background 150ms, color 150ms',
                textDecoration: 'none',
              };

              const iconColor = isActive ? C.iconActive : isSubmenuOpen ? C.iconOpen : C.textMuted;

              return (
                <div
                  key={item.id || item.path}
                  style={{ position: 'relative' }}
                  onMouseEnter={() => !isOpen && setHoveredItem(item.id)}
                  onMouseLeave={() => !isOpen && setHoveredItem(null)}
                >
                  {/* Con submenú */}
                  {hasSubmenu ? (
                    <button
                      style={{ ...itemStyle, textAlign: 'left' }}
                      onClick={() => {
                        if (item.disabled) return;
                        if (!isOpen) { setIsOpen(true); setOpenSubmenu(item.id); }
                        else toggleSubmenu(item.id);
                      }}
                    >
                      <Icon style={{ width: '20px', height: '20px', flexShrink: 0, color: iconColor }} />
                      {isOpen && (
                        <>
                          <span style={{ fontSize: '14px', fontWeight: 500, flex: 1, textAlign: 'left', letterSpacing: '0.02em', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                            {item.label}
                          </span>
                          {item.disabled
                            ? <span style={{ fontSize: '9px', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.08em', color: C.textDisabled, background: 'rgba(5,13,26,0.4)', padding: '2px 6px', borderRadius: '6px', whiteSpace: 'nowrap' }}>Pronto</span>
                            : <ChevronRight style={{ width: '14px', height: '14px', flexShrink: 0, color: C.textMuted, transform: isSubmenuOpen ? 'rotate(90deg)' : 'none', transition: 'transform 200ms' }} />
                          }
                        </>
                      )}
                    </button>

                  ) : item.disabled ? (
                    /* Deshabilitado */
                    <div style={itemStyle}>
                      <Icon style={{ width: '20px', height: '20px', flexShrink: 0, color: C.textDisabled }} />
                      {isOpen && (
                        <>
                          <span style={{ fontSize: '14px', fontWeight: 500, flex: 1, letterSpacing: '0.02em', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                            {item.label}
                          </span>
                          <span style={{ fontSize: '9px', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.08em', color: C.textDisabled, background: 'rgba(5,13,26,0.4)', padding: '2px 6px', borderRadius: '6px', whiteSpace: 'nowrap' }}>Pronto</span>
                        </>
                      )}
                    </div>

                  ) : (
                    /* NavLink directo */
                    <NavLink to={item.path} style={itemStyle}>
                      <Icon style={{ width: '20px', height: '20px', flexShrink: 0, color: iconColor }} />
                      {isOpen && (
                        <span style={{ fontSize: '14px', fontWeight: 500, letterSpacing: '0.02em', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                          {item.label}
                        </span>
                      )}
                    </NavLink>
                  )}

                  {/* Submenú desplegado (sidebar abierto) */}
                  {isOpen && hasSubmenu && isSubmenuOpen && (
                    <div style={{
                      marginLeft: '20px', marginTop: '4px',
                      borderLeft: `2px solid ${C.borderAccent}55`,
                      paddingLeft: '12px',
                      display: 'flex', flexDirection: 'column', gap: '2px',
                    }}>
                      {renderSubItems(item.submenu)}
                    </div>
                  )}

                  {/* Submenú flotante (sidebar colapsado) */}
                  {!isOpen && hasSubmenu && hoveredItem === item.id && (
                    <div style={{
                      position: 'absolute', left: 'calc(100% - 4px)', top: 0, marginLeft: '12px',
                      backgroundColor: C.bgPanel, border: `1px solid ${C.borderAccent}`,
                      borderRadius: '16px', padding: '8px', width: '240px',
                      boxShadow: '0 8px 32px rgba(0,0,0,0.6)', zIndex: 50,
                    }}>
                      <div style={{
                        padding: '8px 12px', borderBottom: `1px solid ${C.borderAccent}33`, marginBottom: '4px',
                        fontSize: '11px', fontWeight: 700, color: C.textSub,
                        textTransform: 'uppercase', letterSpacing: '0.08em',
                      }}>
                        {item.label}
                      </div>
                      <div style={{ display: 'flex', flexDirection: 'column', gap: '2px' }}>
                        {renderSubItems(item.submenu)}
                      </div>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </nav>

        {/* ── Footer ── */}
        <div style={{ padding: '12px', borderTop: `1px solid ${C.borderAccent}22`, background: 'rgba(0,0,0,0.1)', flexShrink: 0 }}>
          <div style={{
            display: 'flex', alignItems: 'center', gap: '12px', padding: '8px', borderRadius: '12px',
            justifyContent: isOpen ? 'flex-start' : 'center',
            background: isOpen ? C.bgPanel : 'transparent',
            border:     isOpen ? `1px solid ${C.borderAccent}44` : 'none',
          }}>
            <div style={{
              width: '36px', height: '36px', flexShrink: 0,
              background: C.bgActive, border: `1px solid ${C.borderAccent}`,
              borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
            }}>
              <span style={{ fontWeight: 600, color: C.textPrimary, fontSize: '12px' }}>AJ</span>
            </div>
            {isOpen && (
              <div style={{ flex: 1, minWidth: 0 }}>
                <p style={{ margin: 0, fontSize: '12px', fontWeight: 600, color: C.textPrimary, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  Antuan Andrés Jury
                </p>
                <p style={{ margin: 0, fontSize: '10px', color: C.textMuted, fontWeight: 500, letterSpacing: '0.08em', textTransform: 'uppercase' }}>
                  Jefe de Planta
                </p>
              </div>
            )}
          </div>
        </div>

      </aside>
    </>
  );
}