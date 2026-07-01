import { NavLink, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import {
  BarChart3, Wallet, ShoppingCart, Factory, Package, Users, Activity
} from 'lucide-react';

/* Reutilizamos los tokens de color del sistema para consistencia visual */
const C = {
  bgPanel:      '#0e1424',
  bgHover:      '#172036',
  bgActive:     '#1e2942',
  border:       'rgba(255,255,255,0.04)',
  textPrimary:  '#ffffff',
  textSub:      '#94a3b8',
  textMuted:    '#64748b',
  textDisabled: '#334155',
  iconActive:   '#38bdf8',
};

/* Módulos principales extraídos directamente de tus rutas */
const modules = [
  { path: '/', label: 'Dashboard General', icon: BarChart3 },
  { path: '/comercial', id: 'comercial', label: 'Área Comercial', icon: Wallet },
  { path: '/adquisiciones', id: 'adquisiciones', label: 'Adquisiciones', icon: ShoppingCart, disabled: true },
  { path: '/produccion', id: 'produccion', label: 'Producción', icon: Factory },
  { path: '/bodega', label: 'Inventario y Bodega', icon: Package, disabled: true },
  { path: '/contabilidad', label: 'Finanzas y Contabilidad', icon: Wallet, disabled: true },
  { path: '/gestion-usuarios', id: 'usuarios', label: 'Gestión de Usuarios', icon: Users },
  { path: '/trazabilidad', id: 'trazabilidad', label: 'Trazabilidad Crítica', icon: Activity },
];

export function WelcomeHero() {
  const { user } = useAuth();
  const location = useLocation();

  return (
    <div className="w-full flex flex-col gap-6 p-6 rounded-2xl border border-slate-800/60 bg-[#0e1424] shadow-xl">
      
      {/* ── 1. BLOQUE DE BIENVENIDA (SERIO / CORPORATIVO) ── */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800/50 pb-5">
        <div className="flex items-center gap-4">
          {/* Avatar discreto */}
          <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-slate-800/50 border border-slate-700/40 text-slate-300 font-semibold text-sm">
            {user?.name ? user.name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase() : 'AD'}
          </div>

          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-xl font-bold tracking-tight text-white">
                ERP de Gestión
              </h1>
              <span className="inline-flex items-center h-2 w-2 rounded-full bg-emerald-500 shadow-[0_0_8px_rgba(16,185,129,0.6)]" />
            </div>
          </div>
        </div>
      </div>

      {/* ── 2. TAP BAR DE MÓDULOS PRINCIPALES (VISTA HORIZONTAL) ── */}
      <div className="w-full overflow-x-auto pb-1" id="modules-tapbar">
        <style>{`
          #modules-tapbar::-webkit-scrollbar { height: 4px; }
          #modules-tapbar::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.06); border-radius: 2px; }
          #modules-tapbar::-webkit-scrollbar-track { background: transparent; }
        `}</style>

        <div className="flex items-center gap-2 min-w-max">
          {modules.map((mod) => {
            const Icon = mod.icon;
            
            // Lógica exacta de coincidencia de ruta activa
            const isTabActive = mod.path === '/' 
              ? location.pathname === '/' 
              : location.pathname.startsWith(mod.path);

            if (mod.disabled) {
              return (
                <div
                  key={mod.path}
                  className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-transparent opacity-35 cursor-not-allowed text-[13px] font-medium"
                  style={{ color: C.textDisabled }}
                >
                  <Icon className="w-4 h-4 shrink-0" />
                  <span>{mod.label}</span>
                  <span className="text-[9px] font-semibold bg-slate-900 px-1.5 py-0.5 rounded text-slate-600">Pronto</span>
                </div>
              );
            }

            return (
              <NavLink
                key={mod.path}
                to={mod.path}
                className="flex items-center gap-2 px-4 py-2.5 rounded-xl border text-[13px] font-medium transition-all duration-200 select-none no-underline"
                style={{
                  background: isTabActive ? C.bgActive : 'transparent',
                  borderColor: isTabActive ? C.border : 'transparent',
                  color: isTabActive ? C.textPrimary : C.textSub,
                }}
                onMouseEnter={(e) => {
                  if (!isTabActive) {
                    e.currentTarget.style.backgroundColor = C.bgHover;
                    e.currentTarget.style.color = C.textPrimary;
                  }
                }}
                onMouseLeave={(e) => {
                  if (!isTabActive) {
                    e.currentTarget.style.backgroundColor = 'transparent';
                    e.currentTarget.style.color = C.textSub;
                  }
                }}
              >
                <Icon 
                  className="w-4 h-4 shrink-0 transition-colors" 
                  style={{ color: isTabActive ? C.iconActive : C.textMuted }}
                />
                <span>{mod.label}</span>
              </NavLink>
            );
          })}
        </div>
      </div>

    </div>
  );
}