import { Navigate, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard, DollarSign, Target, FileText,
  Briefcase, Settings, Scissors, ShoppingCart, ClipboardList,
  Factory, Users, Shield, Truck, BarChart3, Activity,
  ArrowRight, Wallet,
} from 'lucide-react';

/* ─────────────────────────────────────────
   CONFIGURACIÓN POR MÓDULO
───────────────────────────────────────── */
const CONFIGS = {

  comercial: {
    label: 'Área Comercial',
    description: 'Gestiona el ciclo completo de cotizaciones, negocios, notas de venta y proyectos comerciales de la empresa.',
    icon: Wallet,
    heroBg: 'linear-gradient(135deg, rgba(14,165,233,0.10) 0%, rgba(99,102,241,0.05) 100%)',
    accentColor: '#0ea5e9',
    iconBg: 'rgba(14,165,233,0.10)',
    iconBorder: 'rgba(14,165,233,0.20)',
    iconColor: '#0ea5e9',
    btnBg: '#0ea5e9',
    primaryRoute: '/comercial/tablero',
    tabs: [
      {
        path: '/comercial/tablero',
        label: 'Tablero Comercial',
        description: 'Vista Kanban del embudo de cotización y costeo',
        icon: LayoutDashboard,
      },
      {
        path: '/comercial/solicitudes-costos',
        label: 'Solicitudes de Costos',
        description: 'Gestión y aprobación de costos de proyectos',
        icon: DollarSign,
      },
      {
        path: '/comercial/solicitudes-cotizaciones',
        label: 'Solicitudes de Cotizaciones',
        description: 'Gestión y aprobación de solicitudes de cotizaciones de proyectos',
        icon: FileText,
      },
      {
        path: '/comercial/administracion-negocios',
        label: 'Gestión de Negocios',
        description: 'Seguimiento de negocios y oportunidades activas',
        icon: Target,
      },
      {
        path: '/registros-nv',
        label: 'Notas de Venta',
        description: 'Registro y seguimiento de notas de venta emitidas',
        icon: FileText,
      },
      {
        path: '/comercial/gestion-proyectos',
        label: 'Gestión de Proyectos',
        description: 'Administración y estado de proyectos en curso',
        icon: Briefcase,
      },
      {
        path: '/comercial/gestion-plantillas',
        label: 'Plantillas',
        description: 'Plantillas de documentos y cotizaciones reutilizables',
        icon: Settings,
      },
      {
        path: '/comercial/ordenes-produccion',
        label: 'Órdenes de Producción',
        description: 'Órdenes de producción generadas desde el área comercial',
        icon: Scissors,
      },
    ],
  },

  produccion: {
    label: 'Producción',
    description: 'Controla órdenes de producción, materiales, costos de materia prima y el seguimiento completo de planta.',
    icon: Factory,
    heroBg: 'linear-gradient(135deg, rgba(249,115,22,0.10) 0%, rgba(251,191,36,0.05) 100%)',
    accentColor: '#f97316',
    iconBg: 'rgba(249,115,22,0.10)',
    iconBorder: 'rgba(249,115,22,0.20)',
    iconColor: '#f97316',
    btnBg: '#f97316',
    primaryRoute: '/dashboard-op',
    tabs: [
      {
        path: '/dashboard-op',
        label: 'Dashboard Producción',
        description: 'Resumen ejecutivo del área productiva de la planta',
        icon: Factory,
      },
      {
        path: '/produccion/tablero-op',
        label: 'Seguimiento de OP',
        description: 'Kanban de órdenes de producción activas y pendientes',
        icon: LayoutDashboard,
      },
      {
        path: '/produccion/ordenes',
        label: 'Órdenes de Producción',
        description: 'Listado completo y gestión de todas las OP generadas',
        icon: Scissors,
      },
      {
        path: '/op-registro',
        label: 'Registro de OP',
        description: 'Creación y registro de nuevas órdenes de producción',
        icon: ClipboardList,
      },
      {
        path: '/produccion/costeo-mp',
        label: 'Costeo de Materia Prima',
        description: 'Análisis de costos y valorización de materiales',
        icon: DollarSign,
      },
      {
        path: '/produccion/hoja-compra',
        label: 'Hojas de Compra',
        description: 'Generación de hojas de compra asociadas a cada OP',
        icon: ClipboardList,
      },
      {
        path: '/produccion/compras',
        label: 'Compras de Producción',
        description: 'Gestión de compras y adquisiciones para producción',
        icon: ShoppingCart,
      },
    ],
  },

  'gestion-usuarios': {
    label: 'Gestión de Usuarios',
    description: 'Administra colaboradores, clientes, proveedores, vendedores, roles y la estructura organizacional.',
    icon: Users,
    heroBg: 'linear-gradient(135deg, rgba(139,92,246,0.10) 0%, rgba(236,72,153,0.05) 100%)',
    accentColor: '#8b5cf6',
    iconBg: 'rgba(139,92,246,0.10)',
    iconBorder: 'rgba(139,92,246,0.20)',
    iconColor: '#8b5cf6',
    btnBg: '#8b5cf6',
    primaryRoute: '/gestion-usuarios/colaboradores',
    tabs: [
      {
        path: '/gestion-usuarios/colaboradores',
        label: 'Colaboradores',
        description: 'Gestión del equipo interno, operarios y trabajadores',
        icon: Users,
      },
      {
        path: '/gestion-usuarios/clientes',
        label: 'Clientes',
        description: 'Base de datos y perfiles de clientes de la empresa',
        icon: Users,
      },
      {
        path: '/gestion-usuarios/proveedores',
        label: 'Proveedores',
        description: 'Registro y gestión de proveedores y abastecedores',
        icon: Truck,
      },
      {
        path: '/gestion-usuarios/vendedores',
        label: 'Vendedores',
        description: 'Equipo comercial, vendedores y agentes externos',
        icon: BarChart3,
      },
      {
        path: '/admin/areas',
        label: 'Áreas y Departamentos',
        description: 'Estructura organizacional y departamentos de la empresa',
        icon: Briefcase,
      },
      {
        path: '/admin/roles',
        label: 'Gestión de Roles',
        description: 'Roles, permisos y niveles de acceso al sistema',
        icon: Shield,
      },
    ],
  },

  trazabilidad: {
    label: 'Trazabilidad Crítica',
    description: 'Seguimiento completo del ciclo de vida de lotes de producción, despachos y logística de entrega.',
    icon: Activity,
    heroBg: 'linear-gradient(135deg, rgba(16,185,129,0.10) 0%, rgba(6,182,212,0.05) 100%)',
    accentColor: '#10b981',
    iconBg: 'rgba(16,185,129,0.10)',
    iconBorder: 'rgba(16,185,129,0.20)',
    iconColor: '#10b981',
    btnBg: '#10b981',
    primaryRoute: '/trazabilidad/completa',
    tabs: [
      {
        path: '/trazabilidad/completa',
        label: 'Trazabilidad de Lote',
        description: 'Seguimiento completo por número de lote desde producción hasta entrega',
        icon: Activity,
      },
      {
        path: '/trazabilidad/global',
        label: 'Despachos y Logística',
        description: 'Control y estado de despachos hacia clientes finales',
        icon: Truck,
      },
    ],
  },

};

/* ─────────────────────────────────────────
   COMPONENTE PRINCIPAL
───────────────────────────────────────── */
export default function ModuleLandingPage({ moduleId }) {
  const navigate = useNavigate();
  const config = CONFIGS[moduleId];

  if (!config) return <Navigate to="/" replace />;

  const {
    label, description, icon: ModuleIcon,
    heroBg, accentColor, iconBg, iconBorder, iconColor,
    btnBg, primaryRoute, tabs,
  } = config;

  return (
    <div>

      {/* ── Hero ─────────────────────────────────────────────── */}
      <div
        className="relative rounded-3xl p-8 mb-8 overflow-hidden border border-white/40"
        style={{
          background: heroBg,
          boxShadow: `0 0 60px -20px ${accentColor}25`,
        }}
      >
        {/* Círculos decorativos de fondo */}
        <div
          className="absolute -top-12 -right-12 w-52 h-52 rounded-full pointer-events-none"
          style={{ background: `radial-gradient(circle, ${accentColor}18, transparent 70%)` }}
        />
        <div
          className="absolute -bottom-8 left-1/3 w-36 h-36 rounded-full pointer-events-none"
          style={{ background: `radial-gradient(circle, ${accentColor}08, transparent 70%)` }}
        />

        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
          {/* Icono + Título + Descripción */}
          <div className="flex items-start gap-5">
            <div
              className="w-16 h-16 rounded-2xl flex items-center justify-center flex-shrink-0"
              style={{ background: iconBg, border: `1.5px solid ${iconBorder}` }}
            >
              <ModuleIcon style={{ width: '28px', height: '28px', color: iconColor }} />
            </div>

            <div>
              <p className="text-[10px] font-black uppercase tracking-[0.15em] text-muted-foreground mb-1.5">
                Módulo del sistema
              </p>
              <h1 className="text-3xl font-black text-foreground tracking-tight leading-tight">
                {label}
              </h1>
              <p className="text-sm text-muted-foreground mt-2 max-w-lg leading-relaxed">
                {description}
              </p>
            </div>
          </div>

          {/* CTA principal */}
          <button
            onClick={() => navigate(primaryRoute)}
            className="flex items-center gap-2.5 px-7 py-3.5 text-white rounded-2xl font-black text-sm tracking-wide hover:-translate-y-0.5 hover:brightness-110 transition-all flex-shrink-0 whitespace-nowrap"
            style={{
              background: btnBg,
              boxShadow: `0 8px 24px -4px ${accentColor}50`,
            }}
          >
            Acceder al módulo
            <ArrowRight style={{ width: '16px', height: '16px' }} />
          </button>
        </div>
      </div>

      {/* ── Grid de acceso rápido ─────────────────────────────── */}
      <p className="text-[10px] font-black uppercase tracking-[0.15em] text-muted-foreground mb-5">
        Acceso rápido
      </p>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        {tabs.map((tab) => {
          const TabIcon = tab.icon;
          return (
            <button
              key={tab.path}
              onClick={() => navigate(tab.path)}
              className="group text-left bg-card border border-border rounded-2xl p-5 hover:border-border hover:shadow-xl hover:-translate-y-1.5 transition-all duration-200 relative overflow-hidden"
              style={{ boxShadow: 'var(--shadow-xs)' }}
            >
              {/* Barra de acento superior */}
              <div
                className="absolute top-0 left-0 right-0 h-[2.5px] opacity-0 group-hover:opacity-100 transition-opacity duration-200"
                style={{ background: `linear-gradient(90deg, ${accentColor}, ${accentColor}60)` }}
              />

              {/* Burbuja decorativa */}
              <div
                className="absolute -top-6 -right-6 w-20 h-20 rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-300"
                style={{ background: `radial-gradient(circle, ${accentColor}08, transparent)` }}
              />

              {/* Icono */}
              <div
                className="w-11 h-11 rounded-xl flex items-center justify-center mb-4 transition-transform duration-200 group-hover:scale-110"
                style={{ background: iconBg, border: `1px solid ${iconBorder}` }}
              >
                <TabIcon style={{ width: '19px', height: '19px', color: iconColor }} />
              </div>

              {/* Texto */}
              <h3 className="text-sm font-black text-foreground mb-1.5 leading-tight group-hover:text-foreground transition-colors">
                {tab.label}
              </h3>
              <p className="text-[11px] text-muted-foreground font-medium leading-relaxed">
                {tab.description}
              </p>

              {/* Acción hover */}
              <div
                className="flex items-center gap-1 mt-3 opacity-0 group-hover:opacity-100 -translate-x-1 group-hover:translate-x-0 transition-all duration-200"
              >
                <span
                  className="text-[9px] font-black uppercase tracking-widest"
                  style={{ color: accentColor }}
                >
                  Abrir
                </span>
                <ArrowRight style={{ width: '10px', height: '10px', color: accentColor }} />
              </div>
            </button>
          );
        })}
      </div>

    </div>
  );
}
