import { NavLink, useLocation, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard, DollarSign, Target, FileText,
  Briefcase, Settings, Scissors, ShoppingCart, ClipboardList,
  Factory, Users, Shield, Truck, BarChart3, Activity,
  Home, ChevronRight,
} from 'lucide-react';

/* ─────────────────────────────────────────
   TOKENS DE COLOR (sincronizados con Sidebar)
───────────────────────────────────────── */
const C = {
  bg:          '#090d16',
  bgHover:     'rgba(255,255,255,0.04)',
  bgActiveTab: 'rgba(56,189,248,0.07)',
  border:      'rgba(255,255,255,0.05)',
  borderDiv:   'rgba(255,255,255,0.04)',
  textPrimary: '#ffffff',
  textSub:     '#94a3b8',
  textMuted:   '#475569',
  iconActive:  '#38bdf8',
  accent:      '#38bdf8',
};

/* ─────────────────────────────────────────
   CONFIGURACIÓN POR MÓDULO
───────────────────────────────────────── */
const MODULE_TABS = {
  comercial: {
    label:     'Área Comercial',
    homeRoute: '/comercial/tablero',
    matchPaths: ['/comercial/', '/registros-nv', '/detalle-nv'],
    tabs: [
      { path: '/comercial/tablero',                label: 'Tablero',         icon: LayoutDashboard },
      { path: '/comercial/solicitudes-costos',      label: 'Sol. Costos',     icon: DollarSign      },
      { path: '/comercial/administracion-negocios', label: 'Gest. Negocios',  icon: Target          },
      { path: '/registros-nv',                      label: 'Notas de Venta',  icon: FileText        },
      { path: '/comercial/gestion-proyectos',       label: 'Proyectos',       icon: Briefcase       },
      { path: '/comercial/gestion-plantillas',      label: 'Plantillas',      icon: Settings        },
      { path: '/comercial/ordenes-produccion',      label: 'Órd. Producción', icon: Scissors        },
    ],
  },
  produccion: {
    label:     'Producción',
    homeRoute: '/dashboard-op',
    matchPaths: ['/produccion/', '/dashboard-op', '/op-registro'],
    tabs: [
      { path: '/dashboard-op',           label: 'Dashboard',       icon: Factory      },
      { path: '/produccion/tablero-op',  label: 'Seguimiento OP',  icon: LayoutDashboard },
      { path: '/produccion/ordenes',     label: 'Órdenes',         icon: Scissors     },
      { path: '/op-registro',            label: 'Registro OP',     icon: ClipboardList },
      { path: '/produccion/costeo-mp',   label: 'Costeo MP',       icon: DollarSign   },
      { path: '/produccion/hoja-compra', label: 'Hojas de Compra', icon: ClipboardList },
      { path: '/produccion/compras',     label: 'Compras',         icon: ShoppingCart },
    ],
  },
  usuarios: {
    label:     'Gestión de Usuarios',
    homeRoute: '/gestion-usuarios/colaboradores',
    matchPaths: ['/gestion-usuarios/', '/gestion-usuarios', '/admin/areas', '/admin/roles'],
    tabs: [
      { path: '/gestion-usuarios/colaboradores', label: 'Colaboradores', icon: Users     },
      { path: '/gestion-usuarios/clientes',      label: 'Clientes',      icon: Users     },
      { path: '/gestion-usuarios/proveedores',   label: 'Proveedores',   icon: Truck     },
      { path: '/gestion-usuarios/vendedores',    label: 'Vendedores',    icon: BarChart3 },
      { path: '/admin/areas',                    label: 'Áreas',         icon: Briefcase },
      { path: '/admin/roles',                    label: 'Roles',         icon: Shield    },
    ],
  },
  trazabilidad: {
    label:     'Trazabilidad Crítica',
    homeRoute: '/trazabilidad/completa',
    matchPaths: ['/trazabilidad/'],
    tabs: [
      { path: '/trazabilidad/completa', label: 'Traz. de Lote',    icon: Activity },
      { path: '/trazabilidad/global',   label: 'Despachos y Log.', icon: Truck    },
    ],
  },
  admin: {
    label:     'Administración',
    homeRoute: '/admin/datos-maestros',
    matchPaths: ['/admin/datos-maestros'],
    tabs: [
      { path: '/admin/datos-maestros', label: 'Datos Maestros', icon: Settings },
    ],
  },
};

/* ─────────────────────────────────────────
   HELPER EXPORTADO — usado también en App.jsx
   para calcular el margen top del <main>
───────────────────────────────────────── */
export function getActiveModule(pathname) {
  for (const [key, mod] of Object.entries(MODULE_TABS)) {
    const hit = mod.matchPaths.some(p => {
      if (p.endsWith('/')) return pathname.startsWith(p) || pathname === p.slice(0, -1);
      return pathname === p || pathname.startsWith(p + '/');
    });
    if (hit) return { key, ...mod };
  }
  return null;
}

/* ─────────────────────────────────────────
   COMPONENTE PRINCIPAL
───────────────────────────────────────── */
export default function ModuleTabBar({ isSidebarOpen }) {
  const location = useLocation();
  const navigate  = useNavigate();
  const activeModule = getActiveModule(location.pathname);

  /* Sin módulo activo → no renderizar nada */
  if (!activeModule) return null;

  return (
    <>
      {/* ── Estilos de los tabs (scrollbar oculta + transiciones) ── */}
      <style>{`
        #mtb-scroll::-webkit-scrollbar { width: 0; height: 0; }
        #mtb-scroll { scrollbar-width: none; -ms-overflow-style: none; }

        .mtb-tab {
          display: flex;
          align-items: center;
          gap: 6px;
          padding: 0 15px;
          height: 100%;
          font-size: 12.5px;
          font-weight: 500;
          color: ${C.textSub};
          text-decoration: none;
          white-space: nowrap;
          flex-shrink: 0;
          position: relative;
          border: none;
          border-bottom: 2px solid transparent;
          background: transparent;
          cursor: pointer;
          transition: color 180ms cubic-bezier(0.4,0,0.2,1),
                      background 180ms cubic-bezier(0.4,0,0.2,1),
                      border-color 180ms cubic-bezier(0.4,0,0.2,1);
        }
        .mtb-tab:hover {
          color: ${C.textPrimary};
          background: ${C.bgHover};
        }
        .mtb-tab.mtb-active {
          color: ${C.textPrimary};
          background: ${C.bgActiveTab};
          border-bottom-color: ${C.accent};
          font-weight: 600;
        }
        .mtb-tab-icon {
          width: 13px;
          height: 13px;
          flex-shrink: 0;
          color: ${C.textMuted};
          transition: color 180ms;
        }
        .mtb-tab:hover .mtb-tab-icon,
        .mtb-tab.mtb-active .mtb-tab-icon {
          color: ${C.iconActive};
        }

        /* Botón Home */
        .mtb-home-btn {
          display: flex;
          align-items: center;
          gap: 7px;
          padding: 0 14px;
          height: 100%;
          background: transparent;
          border: none;
          border-bottom: 2px solid transparent;
          cursor: pointer;
          color: ${C.textMuted};
          font-size: 12px;
          font-weight: 500;
          white-space: nowrap;
          flex-shrink: 0;
          transition: color 180ms, background 180ms;
        }
        .mtb-home-btn:hover {
          color: ${C.textPrimary};
          background: ${C.bgHover};
        }
        .mtb-home-btn:hover .mtb-home-icon {
          color: ${C.iconActive};
        }
        .mtb-home-icon {
          width: 14px;
          height: 14px;
          transition: color 180ms;
          flex-shrink: 0;
        }
      `}</style>

      <header
        className={`fixed top-[76px] right-0 z-[35] transition-all duration-300 ease-in-out left-0 ${
          isSidebarOpen ? 'md:left-[260px]' : 'md:left-[72px]'
        }`}
        style={{
          height: '44px',
          backgroundColor: C.bg,
          borderBottom: `1px solid ${C.border}`,
          display: 'flex',
          alignItems: 'stretch',
        }}
      >
        {/* ── Lado Izquierdo: Home + Breadcrumb ── */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          flexShrink: 0,
          borderRight: `1px solid ${C.borderDiv}`,
        }}>
          {/* Botón Home */}
          <button
            className="mtb-home-btn"
            onClick={() => navigate('/')}
            title="Volver al Dashboard Principal"
          >
            <Home className="mtb-home-icon" />
            <span className="hidden sm:inline">Inicio</span>
          </button>

          {/* Separador chevron */}
          <ChevronRight style={{
            width: '12px', height: '12px',
            color: C.textMuted,
            flexShrink: 0,
            marginRight: '2px',
          }} />

          {/* Nombre del módulo */}
          <span style={{
            fontSize: '11px',
            fontWeight: 600,
            color: C.textMuted,
            textTransform: 'uppercase',
            letterSpacing: '0.07em',
            padding: '0 14px 0 6px',
            whiteSpace: 'nowrap',
          }}>
            {activeModule.label}
          </span>
        </div>

        {/* ── Divisor vertical fino ── */}
        <div style={{
          width: '1px',
          height: '20px',
          alignSelf: 'center',
          backgroundColor: C.borderDiv,
          flexShrink: 0,
        }} />

        {/* ── Tabs Scrollables ── */}
        <div
          id="mtb-scroll"
          style={{
            display: 'flex',
            alignItems: 'stretch',
            flex: 1,
            overflowX: 'auto',
            overflowY: 'hidden',
          }}
        >
          {activeModule.tabs.map(tab => {
            const TabIcon = tab.icon;
            return (
              <NavLink
                key={tab.path}
                to={tab.path}
                className={({ isActive }) =>
                  isActive ? 'mtb-tab mtb-active' : 'mtb-tab'
                }
              >
                <TabIcon className="mtb-tab-icon" />
                {tab.label}
              </NavLink>
            );
          })}
        </div>

        {/* ── Degradado derecho para indicar scroll ── */}
        <div style={{
          position: 'absolute',
          right: 0,
          top: 0,
          bottom: 0,
          width: '40px',
          background: `linear-gradient(to left, ${C.bg}, transparent)`,
          pointerEvents: 'none',
        }} />
      </header>
    </>
  );
}
