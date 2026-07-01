import { useState, useEffect } from 'react';
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
import { api } from '../../remote/service/api';

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
    const [stats, setStats] = useState({
        ventas: '$0',
        scos: 0,
        evn: 0,
        ops: 0,
        totalScos: 0,
        totalEvn: 0,
        totalOps: 0
    });
    const [salesData, setSalesData] = useState([]);
    const [profitabilityData, setProfitabilityData] = useState([]);

    useEffect(() => {
        let isMounted = true;
        const fetchDashboardData = async () => {
            try {
                const [scosRes, evnRes, nvRes, opsRes] = await Promise.all([
                    api.get('/solicitudes-costos'),
                    api.get('/comercial/evaluaciones-negocio'),
                    api.get('/comercial/notas-venta'),
                    api.get('/produccion/ordenes-produccion')
                ]);

                if (!isMounted) return;

                const scosList = scosRes.data || [];
                const evnList = evnRes.data || [];
                const nvList = nvRes.data || [];
                const opsList = opsRes.data || [];

                // 1. SCOS Pendientes
                const pendingScos = scosList.filter(sc => sc.estado === 'PENDIENTE').length;

                // 2. EVN en Evaluación
                const evalEvn = evnList.filter(ev => ev.estado === 'BORRADOR' || ev.estado === 'EVALUACION').length;

                // 3. OPs en Planta (pendientes, en proceso, detenidas)
                const activeOps = opsList.filter(op => op.estado === 'PENDIENTE' || op.estado === 'EN_PROCESO' || op.estado === 'DETENIDA').length;

                // 4. Ventas del Mes actual
                const today = new Date();
                const currentYear = today.getFullYear();
                const currentMonth = today.getMonth(); // 0-11

                const salesNotesThisMonth = nvList.filter(nv => {
                    if (!nv.fechaEmision) return false;
                    const [y, m, d] = nv.fechaEmision.split('-').map(Number);
                    return y === currentYear && (m - 1) === currentMonth;
                });

                const totalVentasMes = salesNotesThisMonth.reduce((sum, nv) => sum + (nv.montoTotal || 0), 0);

                setStats({
                    ventas: `$${Math.round(totalVentasMes).toLocaleString('es-CL')}`,
                    scos: pendingScos,
                    evn: evalEvn,
                    ops: activeOps,
                    totalScos: scosList.length,
                    totalEvn: evnList.length,
                    totalOps: opsList.length
                });

                // 5. Sales Data and Profitability Data by month (for the charts!)
                const evnMap = new Map(evnList.map(evn => [evn.id || evn.evaluacionNegocioId, evn]));
                const monthsNames = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
                const groupedByMonth = Array.from({ length: 12 }, (_, i) => ({
                    mes: monthsNames[i],
                    ventas: 0,
                    utilidad: 0,
                    rentabilidad: 0
                }));

                nvList.forEach(nv => {
                    if (!nv.fechaEmision) return;
                    const [y, m, d] = nv.fechaEmision.split('-').map(Number);
                    if (y === currentYear) {
                        const monthIndex = m - 1;
                        if (monthIndex >= 0 && monthIndex < 12) {
                            groupedByMonth[monthIndex].ventas += nv.montoTotal || 0;
                            const evn = evnMap.get(nv.evaluacionNegocioId);
                            const nvUtilidad = evn ? (evn.margenGanancia || 0) : ((nv.montoTotal || 0) * 0.25);
                            groupedByMonth[monthIndex].utilidad += nvUtilidad;
                        }
                    }
                });

                // Calculate profitability for each month
                groupedByMonth.forEach(item => {
                    item.rentabilidad = item.ventas > 0 ? parseFloat(((item.utilidad / item.ventas) * 100).toFixed(1)) : 0;
                    item.ventas = Math.round(item.ventas);
                });

                setSalesData(groupedByMonth);
                setProfitabilityData(groupedByMonth);

            } catch (error) {
                console.error("Error loading dashboard metrics:", error);
            }
        };

        fetchDashboardData();
        return () => { isMounted = false; };
    }, []);

    return (
        <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 animate-in fade-in slide-in-from-bottom-4 duration-700">

            {/* Identidad del usuario (sin saludo, sin fecha/hora) */}
            <WelcomeHero />

            {/* KPIs del sistema */}
            <AdminDashboard
                stats={stats}
                salesData={salesData}
                profitabilityData={profitabilityData}
            />

            {/* Cuerpo: módulos + acceso rápido */}
            <div className="grid grid-cols-1 gap-6 lg:grid-cols-[1fr_280px]">

                {/* Izquierda: grid de módulos */}
                <div>
                    <div className="mb-4 flex items-center justify-between">
                        <div>
                            <h2 className="text-base font-bold tracking-tight text-foreground">
                                Módulos del Sistema
                            </h2>
                            <p className="mt-0.5 text-xs text-muted-foreground">
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
