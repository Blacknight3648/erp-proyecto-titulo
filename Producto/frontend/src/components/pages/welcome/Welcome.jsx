import { useNavigate } from 'react-router-dom';
import {
    Building2,
    Users,
    FolderKanban,
    Package,
    ShieldCheck,
    BarChart3
} from 'lucide-react';
import { useAuth } from '../../../contexts/AuthContext';
import { WelcomeHero } from '../home/WelcomeHero';
import AdminDashboard from '../home/AdminDashboard';
import { AdminFastReports } from '../home/AdminFastReports';
import ModuleCard from '../home/ModuleCard';

const MODULES = [
    {
        title: 'Proyectos Antuan SA',
        description: 'Visión general, métricas y KPIs principales de la organización.',
        icon: Building2,
        path: '/comercial/administracion-negocios',
        color: 'from-blue-600 to-indigo-700',
    },
    {
        title: 'Gestión de Usuarios',
        description: 'Administración de accesos, perfiles, roles y permisos del personal.',
        icon: Users,
        path: '/gestion-usuarios/colaboradores',
        color: 'from-violet-600 to-purple-700',
    },
    {
        title: 'Proyectos',
        description: 'Control, planificación y seguimiento de proyectos activos e inactivos.',
        icon: FolderKanban,
        path: '/comercial/gestion-proyectos',
        color: 'from-indigo-500 to-blue-600',
    },
    {
        title: 'Activos',
        description: 'Gestión de inventario físico, maquinaria, equipos y recursos.',
        icon: Package,
        path: '/produccion/tablero-op',
        color: 'from-amber-500 to-orange-600',
    },
    {
        title: 'Trazabilidad',
        description: 'Seguimiento integral de procesos, control de lotes y flujo operativo.',
        icon: ShieldCheck,
        path: '/trazabilidad/completa',
        color: 'from-emerald-500 to-teal-600',
    },
    {
        title: 'Comercial',
        description: 'Gestión de ventas, cotizaciones, análisis de costos y facturación.',
        icon: BarChart3,
        path: '/comercial/tablero',
        color: 'from-cyan-600 to-blue-600',
    },
];

const QUICK_NAV = {
    reportes: '/comercial/tablero',
    comercial: '/comercial/administracion-negocios',
    produccion: '/produccion/tablero-op',
    usuarios: '/gestion-usuarios/colaboradores',
};

export default function Welcome() {
    const { user } = useAuth();
    const navigate = useNavigate();

    return (
        <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 animate-in fade-in slide-in-from-bottom-4 duration-700">

            {/* Hero de bienvenida */}
            <WelcomeHero
                userName={user?.name ?? 'Usuario'}
                role="Administrador del Sistema"
            />

            {/* KPIs del sistema */}
            <AdminDashboard
                stats={{ usuarios: 2, registros: 3, sistema: 'v1.0.0', auditoria: '●' }}
            />

            {/* Cuerpo principal: módulos + acceso rápido */}
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
