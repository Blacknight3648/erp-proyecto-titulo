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
import { WelcomeHero, WelcomeFooter } from '../home/WelcomeHero';
import ModuleCard from '../home/ModuleCard';

export default function Welcome() {

    const { user } = useAuth();
    const navigate = useNavigate();

    const modules = [
        {
            title: 'Proyectos Antuan SA',
            description: 'Visión general, métricas y KPIs principales de la organización.',
            icon: Building2,
            path: '/dashboard',
            color: 'from-blue-600 to-indigo-700',
        },
        {
            title: 'Gestión de Usuarios',
            description: 'Administración de accesos, perfiles, roles y permisos del personal.',
            icon: Users,
            path: '/usuarios',
            color: 'from-violet-600 to-purple-700',
        },
        {
            title: 'Proyectos',
            description: 'Control, planificación y seguimiento de proyectos activos e inactivos.',
            icon: FolderKanban,
            path: '/proyectos',
            color: 'from-indigo-500 to-blue-600',
        },
        {
            title: 'Activos',
            description: 'Gestión de inventario físico, maquinaria, equipos y recursos.',
            icon: Package,
            path: '/activos',
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
        }
    ];

    return (
        <div className="max-w-7xl mx-auto space-y-8 py-8 px-4 animate-in fade-in slide-in-from-bottom-4 duration-700">

            {/* Modules Grid */}
            <div>
                <div className="mb-6 flex items-center justify-between">
                    <div>
                        <h2 className="text-xl font-bold text-slate-800 tracking-tight">
                            Panel de Control Principal
                        </h2>
                        <p className="text-sm text-slate-500 mt-1">
                            Seleccione un módulo para comenzar a operar
                        </p>
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {modules.map((module, idx) => (
                        <ModuleCard
                            key={idx}
                            module={module}
                            onClick={() => navigate(module.path)}
                        />
                    ))}
                </div>
            </div>

            {/* Footer Status */}
            <WelcomeFooter />

        </div>
    );
}