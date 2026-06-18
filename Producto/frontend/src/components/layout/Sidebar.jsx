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
   TOKENS DE COLOR (MODERNIZADOS)
───────────────────────────────────────── */
const C = {
  bg:           '#090d16',
  bgPanel:      '#0e1424',
  bgHover:      '#172036',
  bgActive:     '#1e2942',
  border:       'rgba(255,255,255,0.04)',
  borderAccent: 'rgba(56,189,248,0.15)',
  textPrimary:  '#ffffff',
  textSub:      '#94a3b8',
  textMuted:    '#64748b',
  textDisabled: '#334155',
  iconActive:   '#38bdf8',
  iconOpen:     '#7dd3fc',
  shadow:       'rgba(0,0,0,0.4)',
};

/* ─────────────────────────────────────────
   DATOS DEL MENÚ
───────────────────────────────────────── */
const menuItems = [
  { path: '/', label: 'Dashboard General', icon: BarChart3 },
  {
    id: 'comercial', label: 'Área Comercial', icon: Wallet,
    submenu: [
      { path: '/comercial/tablero',            label: 'Tablero Comercial',       icon: LayoutDashboard },
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
      { path: '/op-registro',            label: 'Órdenes de Producción',     icon: ClipboardList },
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

export default function Sidebar({ isOpen, setIsOpen }) {
  const location = useLocation();
  const [openSubmenu, setOpenSubmenu]   = useState(null);
  const [hoveredItem, setHoveredItem]   = useState(null);
  const [hoveredSub,  setHoveredSub]    = useState(null);
  const [hoveredChev, setHoveredChev]   = useState(false);
  const [hoveredExp,  setHoveredExp]    = useState(false);

  const toggleSubmenu = (id) => setOpenSubmenu(openSubmenu === id ? null : id);

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
            fontSize: '13px',
            textDecoration: 'none',
            whiteSpace: 'nowrap',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            transition: 'all 200ms cubic-bezier(0.4, 0, 0.2, 1)',
            background: isSubActive ? C.bgActive : isHov ? C.bgHover : 'transparent',
            color:      isSubActive || isHov ? C.textPrimary : C.textSub,
          }}
          onMouseEnter={() => setHoveredSub(sub.path)}
          onMouseLeave={() => setHoveredSub(null)}
        >
          <SubIcon style={{
            width: '15px',
            height: '15px',
            flexShrink: 0,
            color: isSubActive ? C.iconActive : C.textMuted,
            transition: 'color 200ms',
          }} />
          <span style={{ overflow: 'hidden', textOverflow: 'ellipsis' }}>{sub.label}</span>
        </NavLink>
      );
    });

  return (
    <>
      <style>{`
        #sb-nav::-webkit-scrollbar       { width: 3px; }
        #sb-nav::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.08); border-radius: 4px; }
        #sb-nav::-webkit-scrollbar-track { background: transparent; }
      `}</style>

      {isOpen && (
        <div
          onClick={() => setIsOpen(false)}
          style={{
            position: 'fixed', inset: 0, zIndex: 40,
            background: 'rgba(0,0,0,0.3)',
            backdropFilter: 'blur(8px)',
          }}
        />
      )}

      <aside style={{
        position: 'fixed', top: 0, left: 0, zIndex: 50,
        height: '100vh',
        display: 'flex', flexDirection: 'column',
        backgroundColor: C.bg,
        borderRight: `1px solid ${C.border}`,
        transition: 'width 250ms cubic-bezier(0.4, 0, 0.2, 1)',
        width: isOpen ? '260px' : '72px',
        overflowX: 'hidden',
      }}>

        {/* ── Cabecera ── */}
        <div style={{
          padding: '0 16px',
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          height: '72px', flexShrink: 0,
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', overflow: 'hidden' }}>
            <div style={{
              width: '32px', height: '32px', flexShrink: 0,
              backgroundColor: C.bgPanel,
              border: `1px solid ${C.border}`,
              borderRadius: '8px',
              display: 'flex', alignItems: 'center', justifyCenter: 'center',
              justifyContent: 'center'
            }}>
              <span style={{ fontWeight: 700, color: C.textPrimary, fontSize: '13px' }}>A</span>
            </div>

            {isOpen && (
              <div style={{ display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                <p style={{ margin: 0, fontWeight: 600, color: C.textPrimary, fontSize: '13px', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  Atuan S.A.
                </p>
                <p style={{ margin: 0, fontSize: '10px', color: C.textMuted, fontWeight: 500, textTransform: 'uppercase', letterSpacing: '0.02em' }}>
                  Gestión Corporativa
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
                padding: '6px', border: 'none', borderRadius: '6px', cursor: 'pointer',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                background: hoveredChev ? C.bgHover : 'transparent',
                color:      hoveredChev ? C.textPrimary : C.textMuted,
                transition: 'all 150ms',
              }}
            >
              <ChevronLeft style={{ width: '15px', height: '15px' }} />
            </button>
          )}
        </div>

        {/* ── Navegación ── */}
        <nav id="sb-nav" style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', padding: '12px 8px' }}>

          {!isOpen && (
            <button
              onClick={() => setIsOpen(true)}
              onMouseEnter={() => setHoveredExp(true)}
              onMouseLeave={() => setHoveredExp(false)}
              aria-label="Expandir menú"
              style={{
                width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center',
                padding: '8px', marginBottom: '12px', border: `1px solid ${C.border}`,
                borderRadius: '8px', cursor: 'pointer',
                background: hoveredExp ? C.bgHover : C.bgPanel,
                color:      hoveredExp ? C.textPrimary : C.textMuted,
                transition: 'all 150ms',
              }}
            >
              <ChevronRight style={{ width: '14px', height: '14px' }} />
            </button>
          )}

          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
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
                padding: '10px 12px', borderRadius: '8px',
                background: isActive ? C.bgActive : isSubmenuOpen ? C.bgHover : 'transparent',
                color:      isActive ? C.textPrimary : item.disabled ? C.textDisabled : C.textSub,
                fontWeight: isActive ? 500 : 400,
                opacity:    item.disabled ? 0.35 : 1,
                cursor:     item.disabled ? 'not-allowed' : 'pointer',
                transition: 'all 150ms cubic-bezier(0.4, 0, 0.2, 1)',
                textDecoration: 'none',
                border: 'none',
              };

              const iconColor = isActive ? C.iconActive : isSubmenuOpen ? C.iconOpen : C.textMuted;

              return (
                <div
                  key={item.id || item.path}
                  style={{ position: 'relative' }}
                  onMouseEnter={() => !isOpen && setHoveredItem(item.id)}
                  onMouseLeave={() => !isOpen && setHoveredItem(null)}
                >
                  {/* Item Con Submenú */}
                  {hasSubmenu ? (
                    <button
                      style={{ ...itemStyle, textAlign: 'left' }}
                      onClick={() => {
                        if (item.disabled) return;
                        if (!isOpen) { setIsOpen(true); setOpenSubmenu(item.id); }
                        else toggleSubmenu(item.id);
                      }}
                    >
                      <Icon style={{ width: '18px', height: '18px', flexShrink: 0, color: iconColor }} />
                      {isOpen && (
                        <>
                          <span style={{ fontSize: '13.5px', flex: 1, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                            {item.label}
                          </span>
                          {item.disabled
                            ? <span style={{ fontSize: '8px', fontWeight: 600, color: C.textDisabled, background: 'rgba(255,255,255,0.02)', padding: '2px 4px', borderRadius: '4px' }}>Pronto</span>
                            : <ChevronRight style={{ width: '13px', height: '13px', flexShrink: 0, color: C.textMuted, transform: isSubmenuOpen ? 'rotate(90deg)' : 'none', transition: 'transform 200ms' }} />
                          }
                        </>
                      )}
                    </button>

                  ) : item.disabled ? (
                    /* Deshabilitado */
                    <div style={itemStyle}>
                      <Icon style={{ width: '18px', height: '18px', flexShrink: 0, color: C.textDisabled }} />
                      {isOpen && (
                        <>
                          <span style={{ fontSize: '13.5px', flex: 1, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                            {item.label}
                          </span>
                          <span style={{ fontSize: '8px', fontWeight: 600, color: C.textDisabled, background: 'rgba(255,255,255,0.02)', padding: '2px 4px', borderRadius: '4px' }}>Pronto</span>
                        </>
                      )}
                    </div>

                  ) : (
                    /* NavLink Directo */
                    <NavLink to={item.path} style={itemStyle}>
                      <Icon style={{ width: '18px', height: '18px', flexShrink: 0, color: iconColor }} />
                      {isOpen && (
                        <span style={{ fontSize: '13.5px', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                          {item.label}
                        </span>
                      )}
                    </NavLink>
                  )}

                  {/* Submenú Desplegado (Sidebar Abierto) */}
                  {isOpen && hasSubmenu && isSubmenuOpen && (
                    <div style={{
                      marginLeft: '20px', marginTop: '4px',
                      borderLeft: `1px solid rgba(255,255,255,0.04)`,
                      paddingLeft: '10px',
                      display: 'flex', flexDirection: 'column', gap: '2px',
                    }}>
                      {renderSubItems(item.submenu)}
                    </div>
                  )}

                  {/* Submenú Flotante Estilo Píldora (Sidebar Cerrado) */}
                  {!isOpen && hasSubmenu && hoveredItem === item.id && (
                    <div style={{
                      position: 'absolute', left: '100%', top: '-4px', marginLeft: '8px',
                      backgroundColor: C.bgPanel, border: `1px solid ${C.border}`,
                      borderRadius: '10px', padding: '6px', width: '220px',
                      boxShadow: '0 12px 24px -4px rgba(0,0,0,0.4)', zIndex: 50,
                      backdropFilter: 'blur(16px)'
                    }}>
                      <div style={{
                        padding: '6px 12px', marginBottom: '4px',
                        fontSize: '11px', fontWeight: 600, color: C.textMuted,
                        textTransform: 'uppercase', letterSpacing: '0.04em',
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

        {/* ── Footer / Perfil Operario ── */}
        <div style={{ padding: '10px', borderTop: `1px solid ${C.border}`, flexShrink: 0 }}>
          <div style={{
            display: 'flex', alignItems: 'center', gap: '10px', padding: '6px', borderRadius: '8px',
            justifyContent: isOpen ? 'flex-start' : 'center',
            background: isOpen ? C.bgPanel : 'transparent',
            border: isOpen ? `1px solid ${C.border}` : 'none',
          }}>
            <div style={{
              width: '28px', height: '28px', flexShrink: 0,
              background: C.bgActive, border: `1px solid ${C.border}`,
              borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
            }}>
              <span style={{ fontWeight: 500, color: C.textPrimary, fontSize: '11px' }}>AJ</span>
            </div>
            {isOpen && (
              <div style={{ flex: 1, minWidth: 0 }}>
                <p style={{ margin: 0, fontSize: '12px', fontWeight: 500, color: C.textPrimary, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  Antuan Andrés Jury
                </p>
                <p style={{ margin: 0, fontSize: '10px', color: C.textMuted, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
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