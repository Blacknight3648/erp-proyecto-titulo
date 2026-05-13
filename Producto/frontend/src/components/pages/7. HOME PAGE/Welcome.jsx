import { useNavigate } from 'react-router-dom';
import {
    ShoppingCart,
    Factory,
    ShieldCheck,
    BarChart3
} from 'lucide-react';
import { useAuth } from '../../../contexts/AuthContext';
import { WelcomeHero, WelcomeFooter } from '../../ui/home/WelcomeHero';
import ModuleCard from '../../ui/home/ModuleCard';

export default function Welcome() {
    const { user } = useAuth();
    const navigate = useNavigate();

    const modules = [
        {
            title: 'Comercial',
            description: 'Inteligencia de ventas, costeo avanzado y formalización de negocios.',
            icon: BarChart3,
            path: '/comercial/tablero',
            color: 'from-blue-600 to-indigo-700',
            bg: 'bg-blue-50',
            text: 'text-blue-600'
        },
        {
            title: 'Producción',
            description: 'Monitoreo de planta en tiempo real y gestión de órdenes de producción.',
            icon: Factory,
            path: '/dashboard-op',
            color: 'from-amber-500 to-orange-600',
            bg: 'bg-amber-50',
            text: 'text-amber-600'
        },
        {
            title: 'Trazabilidad',
            description: 'Seguimiento unificado del ciclo de vida del pedido (Venta → Producción).',
            icon: ShieldCheck,
            path: '/trazabilidad/completa',
            color: 'from-emerald-500 to-teal-600',
            bg: 'bg-emerald-50',
            text: 'text-emerald-600'
        }
    ];

    return (
        <div className="max-w-7xl mx-auto space-y-12 py-8 px-4 animate-in fade-in slide-in-from-bottom-4 duration-1000">
            {/* Hero Section */}
            <WelcomeHero userName={user?.name?.split(' ')[0] || 'Usuario'} />

            {/* Modules Grid */}
            <div>
                <div className="mb-6">
                    <p className="text-[10px] font-black text-gray-400 tracking-widest uppercase mb-2">
                        Centros de Operación
                    </p>
                    <h2 className="text-2xl font-black text-gray-900 italic">Navegación Modular Inteligente</h2>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 max-w-6xl">
                    {modules.map((module, idx) => (
                        <ModuleCard
                            key={idx}
                            module={module}
                            onClick={() => navigate(module.path)}
                        />
                    ))}
                </div>
            </div>

            {/* Quick Access Footer Info */}
            <WelcomeFooter />
        </div>
    );
}
