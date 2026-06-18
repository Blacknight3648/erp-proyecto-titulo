import { useNavigate } from 'react-router-dom';
import {
    Wallet,
    Users,
    Factory,
    Activity,
    BarChart3,
    Settings,
} from 'lucide-react';
import { WelcomeHero } from '../home/WelcomeHero';
import AdminDashboard from '../home/AdminDashboard';
import { AdminFastReports } from '../home/AdminFastReports';
import ModuleCard from '../home/ModuleCard';

/* ─────────────────────────────────────────
   MÓDULOS — apuntan a los landing pages
───────────────────────────────────────── */
const MODULES = [
    {
        title:       'Área Comercial',
        description: 'Tablero, cotizaciones, gestión de negocios, notas de venta y proyectos comerciales.',
        icon:        Wallet,
        path:        '/comercial',
        color:       'from-sky-500 to-blue-600',
    },
    {
        title:       'Producción',
        description: 'Órdenes de producción, seguimiento de planta, costeo de materia prima y compras.',
        icon:        Factory,
        path:        '/produccion',
        color:       'from-orange-500 to-amber-600',
    },
    {
        title:       'Gestión de Usuarios',
        description: 'Colaboradores, clientes, proveedores, vendedores, áreas y roles del sistema.',
        icon:        Users,
        path:        '/gestion-usuarios',
        color:       'from-violet-500 to-purple-600',
    },
    {
        title:       'Trazabilidad Crítica',
        description: 'Seguimiento integral de lotes, control de despachos y logística de entrega.',
        icon:        Activity,
        path:        '/trazabilidad',
        color:       'from-emerald-500 to-teal-600',
    },
    {
        title:       'Datos Maestros',
        description: 'Configuración de materiales, artículos, parámetros base y tablas del sistema.',
        icon:        Settings,
        path:        '/admin/datos-maestros',
        color:       'from-slate-500 to-gray-600',
    },
    {
        title:       'Reportes y KPIs',
        description: 'Métricas de ventas, márgenes, utilidad neta y análisis de rentabilidad.',
        icon:        BarChart3,
        path:        '/comercial/tablero',
        color:       'from-cyan-500 to-blue-600',
    },
];

const QUICK_NAV = {
    reportes:   '/comercial/tablero',
    comercial:  '/comercial',
    produccion: '/produccion',
    usuarios:   '/gestion-usuarios',
};

/* ─────────────────────────────────────────
   COMPONENTE
───────────────────────────────────────── */
export default function Welcome() {
    const navigate = useNavigate();

    return (
        <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 animate-in fade-in slide-in-from-bottom-4 duration-700">

            {/* Identidad del usuario (sin saludo, sin fecha/hora) */}
            <WelcomeHero />

            {/* KPIs del sistema */}
            <AdminDashboard
                stats={{ usuarios: 2, registros: 3, sistema: 'v1.0.0', auditoria: '●' }}
            />

            {/* Cuerpo: módulos + acceso rápido */}
            <div className="grid grid-cols-1 gap-6 lg:grid-cols-[1fr_280px]">

                {/* Izquierda: grid de módulos */}
                <div>
                    <div className="mb-4 flex items-center justify-between">
                        <div>
                            <h2 className="text-base font-bold tracking-tight text-slate-800">
                                Módulos del Sistema
                            </h2>
                            <p className="mt-0.5 text-xs text-slate-500">
                                Seleccione un módulo para comenzar a operar
                            </p>
                        </div>
                    </div>

                    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
                        {MODULES.map((module, idx) => (
                            <ModuleCard
                                key={idx}
                                module={module}
                                onClick={() => navigate(module.path)}
                            />
                        ))}
                    </div>
                </div>

                {/* Derecha: acceso rápido */}
                <div className="lg:pt-[52px]">
                    <AdminFastReports
                        onNavigate={(id) => navigate(QUICK_NAV[id] ?? '/')}
                    />
                </div>
            </div>
        </div>
    );
}
