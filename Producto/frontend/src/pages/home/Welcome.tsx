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
import { useAuth } from '../../contexts/AuthContext';
import AdminDashboard from './AdminDashboard';
import { AdminFastReports } from './AdminFastReports';
import ModuleCard from './ModuleCard';
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
    COMPONENTE PRINCIPAL
───────────────────────────────────────── */
export default function Welcome() {
    const navigate = useNavigate();
    const { user } = useAuth();
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

                const scosList = (scosRes.data as any) || [];
                const evnList = (evnRes.data as any) || [];
                const nvList = (nvRes.data as any) || [];
                const opsList = (opsRes.data as any) || [];

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
                            const evn = evnMap.get(nv.evaluacionNegocioId) as any;
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
        <div className="mx-auto max-w-7xl px-4 py-8 space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
            
            {/* ── Identidad y Saludo Directo (Estilo Azia) ── */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-border pb-5">
                <div className="space-y-1">
                    <h1 className="text-2xl font-extrabold tracking-tight text-foreground">
                        Panel de Administración
                    </h1>
                    <p className="text-sm text-muted-foreground">
                        Bienvenido de vuelta, <span className="font-semibold text-foreground">{user?.name || 'Administrador'}</span>. Estado general de la planta y rendimiento comercial.
                    </p>
                </div>
            </div>

            {/* ── Panel de KPIs y Análisis Estadístico (Ancho Completo) ── */}
            <div className="pt-2">
                <AdminDashboard
                    user={user}
                    stats={stats}
                    salesData={salesData}
                    profitabilityData={profitabilityData}
                />
            </div>

            {/* ── Distribución de Módulos y Accesos Rápido ── */}
            <div className="grid grid-cols-1 gap-8 lg:grid-cols-[1fr_340px] pt-6 border-t border-border">
                
                {/* Módulos del Sistema (Grid de 3 columnas en pantallas grandes) */}
                <div className="space-y-6">
                    <div>
                        <h2 className="text-xl font-bold tracking-tight text-foreground">
                            Módulos del Sistema
                        </h2>
                        <p className="mt-1 text-sm text-muted-foreground">
                            Seleccione un área operativa para comenzar la gestión en tiempo real.
                        </p>
                    </div>

                    <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 xl:grid-cols-3">
                        {MODULES.map((module, idx) => (
                            <ModuleCard
                                key={idx}
                                module={module}
                                onClick={() => navigate(module.path)}
                            />
                        ))}
                    </div>
                </div>

                {/* Accesos rápidos de administración */}
                <div className="space-y-6">
                    <AdminFastReports
                        onNavigate={(id) => navigate(QUICK_NAV[id] ?? '/')}
                    />
                </div>

            </div>
        </div>
    );
}