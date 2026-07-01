import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { ChevronLeft, ClipboardList, AlertCircle, History, Construction, FileText } from 'lucide-react';

const routeConfigs = {
  '/comercial/registros-sc': {
    title: 'Solicitudes de',
    highlight: 'Compra',
    icon: ClipboardList,
    description: 'Estamos construyendo una nueva experiencia para la gestión de solicitudes de compra. Próximamente disponible.',
    badge: 'Módulo en Desarrollo'
  },
  '/comercial/cotizaciones': {
    title: 'Solicitud de',
    highlight: 'Cotizaciones',
    icon: FileText,
    description: 'Estamos construyendo una nueva experiencia para la gestión de solicitudes de cotización. Próximamente disponible.',
    badge: 'Módulo en Desarrollo'
  },
  '/adquisiciones/estado-sc': {
    title: 'Solicitudes de',
    highlight: 'Compra',
    icon: ClipboardList,
    description: 'Estamos construyendo una nueva experiencia para el seguimiento de solicitudes de compra. Próximamente disponible.',
    badge: 'Módulo en Desarrollo'
  },
  '/trazabilidad/alertas': {
    title: 'Alertas de',
    highlight: 'Calidad',
    icon: AlertCircle,
    description: 'Estamos construyendo una nueva experiencia para la gestión y monitoreo de alertas de calidad. Próximamente disponible.',
    badge: 'Módulo en Desarrollo'
  },
  '/trazabilidad/historial': {
    title: 'Auditoría',
    highlight: 'Histórica',
    icon: History,
    description: 'Estamos construyendo una nueva experiencia para la auditoría histórica de prendas y procesos. Próximamente disponible.',
    badge: 'Módulo en Desarrollo'
  }
};

const fallbackConfig = {
  title: 'Módulo en',
  highlight: 'Mantención',
  icon: Construction,
  description: 'Estamos trabajando en el desarrollo de este módulo. Próximamente disponible.',
  badge: 'En Mantención'
};

export default function UnderMaintenance() {
  const location = useLocation();
  const navigate = useNavigate();

  // Find configuration for the current path, or fall back to standard construction view
  const config = routeConfigs[location.pathname] || fallbackConfig;
  const IconComponent = config.icon;

  return (
    <div className="min-h-[80vh] flex items-center justify-center p-6 animate-in fade-in zoom-in duration-700">
      <div className="max-w-md w-full bg-card p-12 rounded-[3.5rem] shadow-[var(--shadow-xl)] border border-border text-center relative overflow-hidden">
        {/* Decorative background element */}
        <div className="absolute top-0 right-0 w-32 h-32 bg-accent rounded-full -mr-16 -mt-16 opacity-50"></div>

        <div className="relative z-10">
          <div className="w-24 h-24 bg-accent rounded-[2rem] flex items-center justify-center mx-auto mb-8 shadow-sm">
            <IconComponent className="w-12 h-12 text-primary animate-bounce" />
          </div>

          <h1 className="text-4xl font-black text-foreground tracking-tighter mb-4 italic uppercase">
            {config.title} <span className="text-primary">{config.highlight}</span>
          </h1>

          <div className="inline-flex items-center px-4 py-2 bg-warning-bg rounded-full mb-8">
            <span className="relative flex h-2 w-2 mr-3">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-warning opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-warning"></span>
            </span>
            <span className="text-[10px] font-black text-warning uppercase tracking-[0.2em]">
              {config.badge}
            </span>
          </div>

          <p className="text-muted-foreground font-bold leading-relaxed mb-10">
            {config.description}
          </p>

          <button
            onClick={() => navigate(-1)}
            className="flex items-center justify-center gap-3 w-full py-5 bg-foreground text-background rounded-[2rem] font-black text-xs uppercase tracking-[0.2em] hover:opacity-90 hover:-translate-y-1 transition-all shadow-[var(--shadow-xl)]"
          >
            <ChevronLeft className="w-4 h-4" />
            Volver Atrás
          </button>
        </div>
      </div>
    </div>
  );
}
