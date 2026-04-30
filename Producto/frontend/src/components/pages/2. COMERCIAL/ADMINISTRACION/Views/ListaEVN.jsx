import React, { useState, useMemo, useEffect } from 'react';
import {
    Target,
    Plus,
    Clock,
    CheckCircle,
    Search,
    Filter,
    CheckCircle2,
    ExternalLink,
    Wrench,
    Calculator,
    FileText
} from 'lucide-react';
import { EvaluacionNegocioService } from '../../../../../remote/service/EvaluacionNegocioService';
import { toast } from 'sonner';

export default function ListaEVN({ onNueva, onEditar, onVer }) {
    const [evaluaciones, setEvaluaciones] = useState([]);
    const [loading, setLoading] = useState(false);
    const [activeTab, setActiveTab] = useState('Activo');
    const [searchTerm, setSearchTerm] = useState('');

    const loadEvaluations = async () => {
        setLoading(true);
        try {
            const data = await EvaluacionNegocioService.getAll();
            setEvaluaciones(data || []);
        } catch (error) {
            console.error("Error loading evaluations:", error);
            toast.error("Error al cargar evaluaciones");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadEvaluations();
    }, []);

    const filteredEvaluations = useMemo(() => {
        return evaluaciones.filter(ev => {
            const estadoFormateado = ev.estado === 'BORRADOR' ? 'ACTIVA' : ev.estado;
            const matchesTab = (activeTab === 'Activo' && (estadoFormateado === 'ACTIVA' || ev.estado === 'ACTIVO' || ev.estado === 'BORRADOR' || ev.estado === 'ADJUDICADA')) ||
                (activeTab === 'Cerrado' && (estadoFormateado === 'CERRADA' || ev.estado === 'CERRADO' || ev.estado === 'RECHAZADA' || ev.estado === 'PERDIDA'));
            const matchesSearch =
                String(ev.clienteNombre || "").toLowerCase().includes(searchTerm.toLowerCase()) ||
                String(ev.evaluacionNegocioId || "").includes(searchTerm) ||
                String(ev.numeroEvn || ev.numero || "").includes(searchTerm);
            return matchesTab && matchesSearch;
        });
    }, [evaluaciones, activeTab, searchTerm]);

    const handleAdjudicar = async (ev, e) => {
        e.stopPropagation();
        if (!window.confirm(`¿Adjudicar la EVN "${ev.referencia || ev.evaluacionNegocioId}"? Esta acción no se puede deshacer.`)) return;
        try {
            await EvaluacionNegocioService.adjudicar(ev.evaluacionNegocioId || ev.id);
            toast.success("EVN adjudicada con éxito");
            loadEvaluations();
        } catch (error) {
            toast.error('Error al adjudicar: ' + (error.response?.data?.message || error.message));
        }
    };

    return (
        <div className="min-h-screen bg-gray-50/50 p-8 animate-in fade-in duration-700">
            <div className="max-w-[1600px] mx-auto">
                {/* Header */}
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6 mb-10">
                    <div>
                        <div className="flex items-center space-x-3 mb-2">
                            <div className="w-10 h-10 bg-indigo-600 rounded-2xl flex items-center justify-center shadow-lg shadow-indigo-200">
                                <Target className="w-5 h-5 text-white" />
                            </div>
                            <h1 className="text-3xl font-black text-gray-800 tracking-tight italic">Administración de Negocios</h1>
                        </div>
                        <p className="text-sm text-gray-500 font-bold uppercase tracking-widest mt-1 ml-1">Acuerdo de condiciones y formalización de ventas</p>
                    </div>

                    <div className="flex items-center gap-3">
                        <button
                            onClick={onNueva}
                            className="flex items-center gap-2 px-6 py-3 bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl text-[11px] font-black uppercase tracking-widest shadow-lg shadow-indigo-200 transition-all hover:scale-105 active:scale-95"
                        >
                            <Plus className="w-4 h-4" />
                            Nueva EVN
                        </button>
                        <div className="flex bg-white p-1.5 rounded-2xl border border-gray-100 shadow-sm">
                            <button
                                onClick={() => setActiveTab('Activo')}
                                className={`flex items-center px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === 'Activo' ? 'bg-indigo-600 text-white shadow-md' : 'text-gray-400 hover:bg-gray-50'}`}
                            >
                                <Clock className="w-3.5 h-3.5 mr-2" />
                                Activos
                            </button>
                            <button
                                onClick={() => setActiveTab('Cerrado')}
                                className={`flex items-center px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === 'Cerrado' ? 'bg-indigo-600 text-white shadow-md' : 'text-gray-400 hover:bg-gray-50'}`}
                            >
                                <CheckCircle className="w-3.5 h-3.5 mr-2" />
                                Cerrados
                            </button>
                        </div>
                    </div>
                </div>

                {/* Filters */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8 bg-white p-4 rounded-[2.5rem] shadow-sm border border-gray-100">
                    <div className="md:col-span-3 relative">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                        <input
                            type="text"
                            placeholder="Buscar evaluación por ID, cliente o artículo..."
                            className="w-full pl-11 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-xs font-bold focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>
                    <button className="flex items-center justify-center gap-2 h-11 bg-white border border-gray-100 rounded-2xl text-xs font-black uppercase tracking-widest text-gray-400 hover:bg-gray-50 transition-all">
                        <Filter className="w-4 h-4" />
                        Más filtros
                    </button>
                </div>

                {/* Evaluations Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 xl:grid-cols-2 gap-8">
                    {/* Card: Nueva EVN */}
                    <div
                        onClick={onNueva}
                        className="group bg-white p-8 rounded-[3rem] border-2 border-dashed border-indigo-200 hover:border-indigo-500 hover:bg-indigo-50/30 hover:shadow-xl hover:shadow-indigo-50 transition-all cursor-pointer flex flex-col items-center justify-center min-h-[280px]"
                    >
                        <div className="w-16 h-16 bg-indigo-100 group-hover:bg-indigo-600 rounded-[1.5rem] flex items-center justify-center mb-5 transition-all shadow-sm group-hover:shadow-lg group-hover:shadow-indigo-200 group-hover:scale-110">
                            <Plus className="w-8 h-8 text-indigo-500 group-hover:text-white transition-colors" />
                        </div>
                        <p className="text-xs font-black text-indigo-400 group-hover:text-indigo-600 uppercase tracking-widest transition-colors">Nueva Evaluación</p>
                        <p className="text-[10px] font-bold text-gray-300 mt-1 uppercase tracking-wider">EVN en blanco</p>
                    </div>

                    {filteredEvaluations.map((ev) => {
                        const isAdjudicada = ev.estado === 'ADJUDICADA' || ev.estado === 'CERRADA' || ev.estado === 'CERRADO';
                        const estadoColor = isAdjudicada
                            ? 'bg-emerald-100 text-emerald-700'
                            : ev.estado === 'BORRADOR' || ev.estado === 'ACTIVO' || ev.estado === 'ACTIVA'
                                ? 'bg-amber-100 text-amber-700'
                                : 'bg-gray-100 text-gray-500';
                        return (
                            <div
                                key={ev.evaluacionNegocioId}
                                className="bg-white p-7 rounded-[2.5rem] border border-gray-100 hover:shadow-xl hover:shadow-gray-100 transition-all flex flex-col"
                            >
                                <div className="flex justify-between items-start mb-5">
                                    <span className="text-[11px] font-black text-indigo-600 bg-indigo-50 px-2.5 py-1 rounded-xl shadow-sm border border-indigo-100 uppercase tracking-widest">
                                        {(() => {
                                            const raw = (ev.numeroEvn || ev.numero || "").toString();
                                            // Si es el timestamp largo, tomamos los últimos 5. Si es nuevo, ya viene corto.
                                            const code = raw.replace('EVN-', '');
                                            return `EVN-${code}`;
                                        })()}
                                    </span>
                                    <span className={`px-3 py-1 rounded-full text-[9px] font-black uppercase tracking-widest ${estadoColor}`}>
                                        {ev.estado || 'Borrador'}
                                    </span>
                                </div>

                                <div className="mb-6 flex-1">
                                    <h4 className="text-xl font-black text-gray-800 leading-tight uppercase mb-1">
                                        {ev.clienteNombre || 'Sin cliente'}
                                    </h4>
                                    <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest flex items-center gap-1 italic">
                                        GESTIONADO POR: <span className="text-indigo-500 not-italic">{ev.vendedorNombre || 'SISTEMA'}</span>
                                    </p>
                                </div>

                                <div className="grid grid-cols-2 gap-3 mb-5">
                                    <div className="bg-gray-50 px-3 py-2.5 rounded-2xl">
                                        <p className="text-[9px] font-black text-gray-400 uppercase mb-0.5">Monto Total</p>
                                        <p className="text-sm font-black text-gray-800">${(ev.montoTotal || 0).toLocaleString('es-CL')}</p>
                                    </div>
                                    <div className="bg-indigo-50 px-3 py-2.5 rounded-2xl">
                                        <p className="text-[9px] font-black text-indigo-400 uppercase mb-0.5">Margen</p>
                                        <p className="text-sm font-black text-indigo-600">{ev.margenGanancia || 0}%</p>
                                    </div>
                                </div>

                                {(ev.costeoId || ev.solicitudCotizacionId) && (
                                    <div className="flex flex-wrap gap-1.5 mb-4">
                                        {ev.costeoId && (
                                            <span className="flex items-center gap-1 px-2.5 py-1 bg-amber-50
                                                            border border-amber-200 text-amber-700 rounded-full
                                                            text-[9px] font-black uppercase tracking-widest">
                                                <Calculator className="w-2.5 h-2.5" />
                                                SCOS #{ev.costeoId}
                                            </span>
                                        )}
                                        {ev.solicitudCotizacionId && (
                                            <span className="flex items-center gap-1 px-2.5 py-1 bg-blue-50
                                                            border border-blue-200 text-blue-700 rounded-full
                                                            text-[9px] font-black uppercase tracking-widest">
                                                <FileText className="w-2.5 h-2.5" />
                                                SCOT #{ev.solicitudCotizacionId}
                                            </span>
                                        )}
                                    </div>
                                )}

                                {/* Botones contextuales */}
                                <div className="flex flex-col gap-2 pt-4 border-t border-gray-50">
                                    <div className="flex gap-2">
                                        <button
                                            onClick={() => onVer(ev)}
                                            className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <ExternalLink className="w-3 h-3" />
                                            Ver Detalle
                                        </button>
                                        <button
                                            onClick={() => onEditar(ev)}
                                            className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <Wrench className="w-3 h-3" />
                                            Ajustar
                                        </button>
                                    </div>
                                    {!isAdjudicada && (
                                        <button
                                            onClick={(e) => handleAdjudicar(ev, e)}
                                            className="w-full flex items-center justify-center gap-2 px-3 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                        >
                                            <CheckCircle2 className="w-3.5 h-3.5" />
                                            Adjudicar
                                        </button>
                                    )}
                                </div>
                            </div>
                        );
                    })}

                    {filteredEvaluations.length === 0 && (
                        <div className="col-span-full py-32 flex flex-col items-center justify-center bg-white border-2 border-dashed border-gray-100 rounded-[4rem]">
                            <Target className="w-16 h-16 text-gray-100 mb-6" />
                            <p className="text-sm font-black text-gray-300 uppercase tracking-widest">No se encontraron evaluaciones de negocio</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
