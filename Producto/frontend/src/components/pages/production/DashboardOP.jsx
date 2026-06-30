import { useState, useEffect, useCallback } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { AlertCircle, Zap, Activity, Calendar, RefreshCw, Hammer, Scissors, Palette, Truck, Box, Clock } from 'lucide-react';
import { mockOPKPIs } from '../../../data/mockData';
import { api } from '../../../remote/service/api';

const DASHBOARD_DEFAULTS = {
    opAtrasada: 0,
    corteAtrasado: 0,
    recepcionLogoAtrasado: 0,
    envioAtrasado: 0,
    devolucionTallerAtrasada: 0,
    entregas7d: 0,
};

export default function DashboardOP() {
    const [alertas, setAlertas] = useState(DASHBOARD_DEFAULTS);
    const [loading, setLoading] = useState(true);

    const fetchDashboard = useCallback(async () => {
        setLoading(true);
        try {
            const { data } = await api.get('/reportes/dashboard-op');
            setAlertas({ ...DASHBOARD_DEFAULTS, ...data });
        } catch (err) {
            console.error('Error cargando dashboard operacional OP:', err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => { fetchDashboard(); }, [fetchDashboard]);

    const { opAtrasada, corteAtrasado, recepcionLogoAtrasado, envioAtrasado, devolucionTallerAtrasada, entregas7d } = alertas;
    const { tiemposPorEtapa, promedioPorLote, distribucionLote } = mockOPKPIs;

    // Helper for table cell colors
    const getCellColor = (value) => {
        if (value === null) return 'bg-gray-50';
        if (value <= 2) return 'bg-green-50 text-green-700 font-bold';
        if (value <= 5) return 'bg-blue-50 text-blue-700 font-bold';
        if (value <= 8) return 'bg-orange-50 text-orange-700 font-bold';
        return 'bg-red-50 text-red-700 font-bold';
    };

    return (
        <div className="space-y-8 pb-12">
            {/* Header section based on Image 1 */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div className="flex items-center space-x-4">
                    <div className="bg-white p-3 rounded-xl shadow-sm border border-gray-100">
                        <Activity className="text-blue-600 w-6 h-6" />
                    </div>
                    <div>
                        <h1 className="text-3xl font-bold text-gray-800">Control Operacional OP</h1>
                        <p className="text-gray-500 text-sm">Gestión de Manufactura y Logística</p>
                    </div>
                </div>

                <div className="flex items-center space-x-4 bg-white p-2 rounded-2xl shadow-sm border border-gray-100">
                    <div className="flex bg-gray-50 rounded-xl p-1">
                        <button className="px-4 py-1.5 text-xs font-bold text-gray-500 hover:text-gray-700">30D</button>
                        <button className="px-4 py-1.5 text-xs font-bold text-gray-700 bg-white shadow-sm rounded-lg">Este Mes</button>
                        <button className="px-4 py-1.5 text-xs font-bold text-gray-500 hover:text-gray-700">Mes Ant.</button>
                    </div>
                    <div className="h-8 w-px bg-gray-100 mx-2"></div>
                    <div className="flex items-center space-x-2 text-xs font-bold text-gray-600">
                        <Calendar className="w-4 h-4 text-gray-400" />
                        <span>10/01/2026</span>
                        <span className="text-gray-300">→</span>
                        <span>10/02/2026</span>
                        <button
                            className="ml-2 p-1.5 bg-gray-900 text-white rounded-lg disabled:opacity-50"
                            onClick={fetchDashboard}
                            disabled={loading}
                        >
                            <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
                        </button>
                    </div>
                </div>
            </div>

            {/* Snapshot Section */}
            <div>
                <div className="flex items-center space-x-2 mb-6 text-gray-400">
                    <Hammer className="w-4 h-4" />
                    <h2 className="text-xs font-bold uppercase tracking-widest">Estado Manufactura (En Tiempo Real)</h2>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {/* Alerta OC */}
                    <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-red-500 relative group hover:shadow-lg transition-all">
                        <div className="flex justify-between items-start mb-4">
                            <div>
                                <h3 className="text-gray-400 text-[10px] font-bold uppercase mb-1">Alerta OC (SIN OC &gt; 3D)</h3>
                                <p className="text-lg font-bold text-gray-700">OP Atrasada</p>
                            </div>
                            <div className="bg-red-50 p-2 rounded-full"><AlertCircle className="text-red-500 w-5 h-5" /></div>
                        </div>
                        <div className="flex items-baseline space-x-2">
                            <span className="text-5xl font-black text-red-600">{opAtrasada}</span>
                            <span className="text-gray-400 text-sm font-medium">ítems</span>
                        </div>
                        <div className="mt-8">
                            <span className="text-[10px] font-bold text-red-500 uppercase">Latencia: &gt; 3d (OC)</span>
                        </div>
                    </div>

                    {/* Corte */}
                    <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-slate-500 relative group hover:shadow-lg transition-all">
                        <div className="flex justify-between items-start mb-4">
                            <div>
                                <h3 className="text-gray-400 text-[10px] font-bold uppercase mb-1">Corte (Recepción vs Inicio Logo/Taller)</h3>
                                <p className="text-lg font-bold text-gray-700">CORTE ATRASADO</p>
                            </div>
                            <div className="bg-slate-50 p-2 rounded-full"><Scissors className="text-slate-500 w-5 h-5" /></div>
                        </div>
                        <div className="flex items-baseline space-x-2">
                            <span className="text-5xl font-black text-slate-700">{corteAtrasado}</span>
                            <span className="text-gray-400 text-sm font-medium">ítems</span>
                        </div>
                        <div className="mt-8">
                            <span className="text-[10px] font-bold text-slate-500 uppercase">Límite: 10 días</span>
                        </div>
                    </div>

                    {/* Logo */}
                    <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-orange-500 relative group hover:shadow-lg transition-all">
                        <div className="flex justify-between items-start mb-4">
                            <div>
                                <h3 className="text-gray-400 text-[10px] font-bold uppercase mb-1">Logo (Salida vs Regreso Logo)</h3>
                                <p className="text-lg font-bold text-gray-700">RECEPCION DE LOGO ATRASADO</p>
                            </div>
                            <div className="bg-orange-50 p-2 rounded-full"><Palette className="text-orange-500 w-5 h-5" /></div>
                        </div>
                        <div className="flex items-baseline space-x-2">
                            <span className="text-5xl font-black text-orange-600">{recepcionLogoAtrasado}</span>
                            <span className="text-gray-400 text-sm font-medium">ítems</span>
                        </div>
                        <div className="mt-8">
                            <span className="text-[10px] font-bold text-orange-500 uppercase">Límite: 3 días</span>
                        </div>
                    </div>

                    {/* Envío */}
                    <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-blue-500 relative group hover:shadow-lg transition-all">
                        <div className="flex justify-between items-start mb-4">
                            <div>
                                <h3 className="text-gray-400 text-[10px] font-bold uppercase mb-1">Envío (Regreso Logo vs Inicio Taller)</h3>
                                <p className="text-lg font-bold text-gray-700">ENVÍO ATRASADO</p>
                            </div>
                            <div className="bg-blue-50 p-2 rounded-full"><Truck className="text-blue-500 w-5 h-5" /></div>
                        </div>
                        <div className="flex items-baseline space-x-2">
                            <span className="text-5xl font-black text-blue-600">{envioAtrasado}</span>
                            <span className="text-gray-400 text-sm font-medium">ítems</span>
                        </div>
                        <div className="mt-8">
                            <span className="text-[10px] font-bold text-blue-500 uppercase">Límite: 2 días</span>
                        </div>
                    </div>

                    {/* Recepción */}
                    <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-red-500 relative group hover:shadow-lg transition-all">
                        <div className="flex justify-between items-start mb-4">
                            <div>
                                <h3 className="text-gray-400 text-[10px] font-bold uppercase mb-1">Recepción (Fin Taller vs Entrega)</h3>
                                <p className="text-lg font-bold text-gray-700">DEVOLUCION TALLER ATRASADA</p>
                            </div>
                            <div className="bg-red-50 p-2 rounded-full"><Box className="text-red-500 w-5 h-5" /></div>
                        </div>
                        <div className="flex items-baseline space-x-2">
                            <span className="text-5xl font-black text-red-600">{devolucionTallerAtrasada}</span>
                            <span className="text-gray-400 text-sm font-medium">ítems</span>
                        </div>
                        <div className="mt-8">
                            <span className="text-[10px] font-bold text-red-500 uppercase">Límite: 7 días</span>
                        </div>
                    </div>

                    {/* Proyección */}
                    <div className="bg-white p-6 rounded-[2rem] shadow-sm border-t-4 border-purple-500 relative group hover:shadow-lg transition-all">
                        <div className="flex justify-between items-start mb-4">
                            <div>
                                <h3 className="text-gray-400 text-[10px] font-bold uppercase mb-1">Proyección (F. Entrega Solicitada)</h3>
                                <p className="text-lg font-bold text-gray-700">Entregas (7d)</p>
                            </div>
                            <div className="bg-purple-50 p-2 rounded-full"><Calendar className="text-purple-500 w-5 h-5" /></div>
                        </div>
                        <div className="flex items-baseline space-x-2">
                            <span className="text-5xl font-black text-purple-600">{entregas7d}</span>
                            <span className="text-gray-400 text-sm font-medium">OPs</span>
                        </div>
                        <div className="mt-8">
                            <span className="text-[10px] font-bold text-purple-500 uppercase">Próxima <span className="text-purple-900">Semana</span></span>
                        </div>
                    </div>
                </div>
            </div>

            <hr className="border-gray-100" />

            {/* Bottom Charts section based on Image 2 */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 pt-4">
                {/* Tiempos por Etapa */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-gray-100">
                    <div className="flex items-center space-x-2 mb-6 text-gray-500">
                        <Clock className="w-4 h-4" />
                        <h2 className="text-xs font-bold uppercase tracking-widest">Tiempos por Etapa (Días)</h2>
                    </div>
                    <ResponsiveContainer width="100%" height={300}>
                        <BarChart data={tiemposPorEtapa} margin={{ top: 20, right: 10, left: -20, bottom: 40 }}>
                            <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
                            <XAxis
                                dataKey="name"
                                axisLine={false}
                                tickLine={false}
                                tick={{ fill: '#9ca3af', fontSize: 10 }}
                                interval={0}
                                angle={-30}
                                textAnchor="end"
                            />
                            <YAxis axisLine={false} tickLine={false} tick={{ fill: '#9ca3af', fontSize: 10 }} />
                            <Tooltip cursor={{ fill: 'transparent' }} />
                            <Bar dataKey="dias" radius={[4, 4, 0, 0]} barSize={32}>
                                {tiemposPorEtapa.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={entry.fill} />
                                ))}
                            </Bar>
                        </BarChart>
                    </ResponsiveContainer>
                    <div className="text-center text-[10px] text-gray-400 font-bold uppercase -mt-4">Días Promedio</div>
                </div>

                {/* Tabla de Promedios */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-gray-100 flex flex-col">
                    <div className="flex items-center space-x-2 mb-6 text-gray-500">
                        <Zap className="w-4 h-4" />
                        <h2 className="text-xs font-bold uppercase tracking-widest">Tiempo Promedio (Días)</h2>
                    </div>

                    <div className="overflow-x-auto flex-1">
                        <table className="w-full text-[10px]">
                            <thead>
                                <tr className="text-gray-400 font-bold uppercase">
                                    <th className="pb-3 text-left">Rango Lote</th>
                                    <th className="pb-3">1. Corte</th>
                                    <th className="pb-3">2. Logo</th>
                                    <th className="pb-3">3. Taller</th>
                                    <th className="pb-3">4. Term.</th>
                                    <th className="pb-3">5. Ciclo Total</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {promedioPorLote.map((item, idx) => (
                                    <tr key={idx} className="group">
                                        <td className="py-2.5">
                                            <div className="flex flex-col">
                                                <span className="font-bold text-gray-700">{item.rango}</span>
                                                <span className="text-[8px] text-gray-400">{item.ops} OPs</span>
                                            </div>
                                        </td>
                                        <td className={`text-center rounded-md ${getCellColor(item.corte)}`}>{item.corte || '-'}</td>
                                        <td className={`text-center rounded-md ${getCellColor(item.logo)}`}>{item.logo || '-'}</td>
                                        <td className={`text-center rounded-md ${getCellColor(item.taller)}`}>{item.taller || '-'}</td>
                                        <td className={`text-center rounded-md ${getCellColor(item.term)}`}>{item.term || '-'}</td>
                                        <td className="text-center font-bold text-gray-800">{item.total || '-'}</td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot>
                                <tr className="bg-gray-50 font-black">
                                    <td className="py-3 px-2 rounded-l-xl uppercase text-[8px] leading-tight">Promedio<br />General</td>
                                    <td className="text-center text-orange-600">5.5</td>
                                    <td className="text-center text-blue-600">3.6</td>
                                    <td className="text-center text-orange-600">5.6</td>
                                    <td className="text-center text-green-600">1.0</td>
                                    <td className="text-center text-indigo-600 border-l border-gray-200">10.5</td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <div className="mt-4 pt-4 border-t border-gray-50 grid grid-cols-2 gap-y-2 text-[8px] font-bold uppercase text-gray-400">
                        <div className="flex items-center"><div className="w-2 h-2 rounded-full bg-green-100 mr-1"></div> 0-2d (Eficiente)</div>
                        <div className="flex items-center"><div className="w-2 h-2 rounded-full bg-blue-100 mr-1"></div> 2-5d (Normal)</div>
                        <div className="flex items-center"><div className="w-2 h-2 rounded-full bg-orange-100 mr-1"></div> 5-8d (Lento)</div>
                        <div className="flex items-center"><div className="w-2 h-2 rounded-full bg-red-100 mr-1"></div> +8d (Crítico)</div>
                    </div>
                </div>

                {/* Volumen de Producción */}
                <div className="bg-white p-6 rounded-[2rem] shadow-sm border border-gray-100">
                    <div className="flex items-center space-x-2 mb-6 text-gray-500">
                        <Box className="w-4 h-4" />
                        <h2 className="text-xs font-bold uppercase tracking-widest">Volumen de Producción</h2>
                    </div>
                    <div className="mb-4">
                        <h3 className="text-xs font-medium text-gray-400">Distribución por Tamaño de Lote (Unidades)</h3>
                    </div>
                    <ResponsiveContainer width="100%" height={300}>
                        <BarChart data={distribucionLote} margin={{ top: 10, right: 10, left: -30, bottom: 20 }}>
                            <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
                            <XAxis
                                dataKey="name"
                                axisLine={false}
                                tickLine={false}
                                tick={{ fill: '#9ca3af', fontSize: 10 }}
                            />
                            <YAxis axisLine={false} tickLine={false} tick={{ fill: '#9ca3af', fontSize: 10 }} />
                            <Tooltip cursor={{ fill: '#f8fafc' }} />
                            <Bar dataKey="valor" fill="#94a3b8" radius={[4, 4, 0, 0]} barSize={24}>
                                {distribucionLote.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={index === 0 ? '#94a3b8' : '#3b82f6'} fillOpacity={index === 0 ? 0.8 : 0.6} />
                                ))}
                            </Bar>
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
}
