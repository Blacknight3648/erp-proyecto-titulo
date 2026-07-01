import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    LayoutDashboard,
    Plus,
    Search,
    Filter,
    MoreVertical,
    Calendar,
    ArrowRight,
    Users,
    AlertCircle,
    List,
    Trello
} from 'lucide-react';
import { useComercial } from '../../hooks/useComercial';
import CostoBadge from './costing/CostoBadge';
import SpecsCard from '../../ui/data-display/SpecsCard';
import ProgressCard from '../../ui/data-display/ProgressCard';

const COLUMNS = [
    { id: 'Pendiente Costeo', title: 'Pendiente Costeo', color: 'border-orange-500' },
    { id: 'Costeado', title: 'Costeado', color: 'border-blue-500' },
    { id: 'Costo Aprobado', title: 'Costo Aprobado', color: 'border-indigo-500' },
    { id: 'Evaluado', title: 'Evaluado', color: 'border-green-500' }
];

const STAGES = [
    { id: 'Pendiente Costeo', label: 'Solicitud', key: 'solicitud' },
    { id: 'Costeado', label: 'Costeado', key: 'costeo' },
    { id: 'Costo Aprobado', label: 'Aprobado', key: 'aprobacion' },
    { id: 'Evaluado', label: 'Evaluado', key: 'evaluacion' }
];

export default function TableroComercial() {
    const navigate = useNavigate();
    const [searchTerm, setSearchTerm] = useState('');
    const [viewMode, setViewMode] = useState('kanban'); // 'kanban' | 'list'
    const { notasVenta, solicitudesCostos, evaluacionesNegocio, load, loading } = useComercial();
    const [allSolicitudes, setAllSolicitudes] = useState([]);

    React.useEffect(() => {
        load();
    }, []);

    React.useEffect(() => {
        // SCOS PENDIENTE → "Pendiente Costeo"
        // SCOS APROBADA  → "Costeado" (el costeo fue aprobado, aún sin EVN)
        const mapEstadoSCOS = (estado) => {
            if (estado === 'PENDIENTE') return 'Pendiente Costeo';
            if (estado === 'APROBADA')  return 'Costeado';
            return 'Pendiente Costeo';
        };

        // EVN BORRADOR/EVALUACION → "Costeado" (en proceso de evaluación)
        // EVN APROBADA/ADJUDICADA → "Costo Aprobado" (aprobada comercialmente)
        const mapEstadoEVN = (estado) => {
            if (estado === 'BORRADOR' || estado === 'EVALUACION') return 'Costeado';
            if (estado === 'APROBADA' || estado === 'ADJUDICADA') return 'Costo Aprobado';
            return null; // RECHAZADA, CANCELADA, CERRADA no aparecen en el tablero
        };

        const scMapped = solicitudesCostos.map(sc => ({
            ...sc,
            idSolicitud: sc.numero || `SCOS-${sc.id}`,
            clienteNombre: sc.clienteNombre || 'S/N',
            estado: mapEstadoSCOS(sc.estado)
        }));

        const evnMapped = (evaluacionesNegocio || [])
            .map(evn => {
                const estadoColumna = mapEstadoEVN(evn.estado);
                if (!estadoColumna) return null;
                return {
                    ...evn,
                    idSolicitud: evn.numero || `EVN-${evn.id}`,
                    clienteNombre: evn.clienteNombre || 'S/N',
                    estado: estadoColumna
                };
            })
            .filter(Boolean);

        const nvMapped = notasVenta.map(nv => ({
            ...nv,
            idSolicitud: nv.numeroNV || `NV-${nv.idNV || nv.id}`,
            clienteNombre: nv.clienteNombre || 'S/N',
            estado: 'Evaluado'
        }));

        setAllSolicitudes([...scMapped, ...evnMapped, ...nvMapped]);
    }, [notasVenta, solicitudesCostos, evaluacionesNegocio]);

    const filteredItems = allSolicitudes.filter(item => {
        const clienteNombre = item.clienteNombre || 'S/N';
        const search = searchTerm.toLowerCase();
        return (clienteNombre || '').toLowerCase().includes(search) ||
            (item.idSolicitud || String(item.id) || '').toLowerCase().includes(search);
    });

    const adaptedItems = filteredItems.map(item => ({
        ...item,
        cliente: item.clienteNombre || 'S/N',
        notaVentaId: item.idSolicitud || String(item.id) || '---',
        idSolicitud: item.idSolicitud || String(item.id)
    }));

    const getColumnItems = (columnId) => {
        return adaptedItems.filter(item => item.estado === columnId);
    };


    const handleCardClick = (item) => {
        if (item.estado === 'Pendiente Costeo') navigate('/comercial/solicitudes-costos');
        if (item.estado === 'Costeado') navigate('/comercial/administracion-negocios');
        if (item.estado === 'Costo Aprobado') navigate('/comercial/administracion-negocios');
        if (item.estado === 'Evaluado') navigate('/registros-nv');
    };

    return (
        <div className="min-h-screen bg-gray-50/50 p-8">
            {/* Header Area */}
            <div className="max-w-[1600px] mx-auto mb-10">
                <div className="flex flex-col xl:flex-row justify-between items-start xl:items-center gap-6">
                    <div>
                        <div className="flex items-center space-x-3 mb-2">
                            <div className="w-10 h-10 bg-blue-600 rounded-2xl flex items-center justify-center shadow-lg shadow-blue-200">
                                <LayoutDashboard className="w-5 h-5 text-white" />
                            </div>
                            <h1 className="text-3xl font-black text-gray-800 tracking-tight italic">Tablero Comercial</h1>
                        </div>
                        <p className="text-sm text-gray-500 font-bold uppercase tracking-widest mt-1">Gestión del Embudo de Cotización y Costeo</p>
                    </div>

                    <div className="flex flex-wrap items-center gap-4 w-full xl:w-auto">
                        {/* View Switcher */}
                        <div className="flex bg-white p-1 rounded-2xl border border-gray-100 shadow-sm mr-2">
                            <button
                                onClick={() => setViewMode('kanban')}
                                className={`flex items-center px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${viewMode === 'kanban' ? 'bg-blue-600 text-white shadow-md' : 'text-gray-400 hover:bg-gray-50'}`}
                            >
                                <Trello className="w-3 h-3 mr-2" />
                                Kanban
                            </button>
                            <button
                                onClick={() => setViewMode('list')}
                                className={`flex items-center px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${viewMode === 'list' ? 'bg-blue-600 text-white shadow-md' : 'text-gray-400 hover:bg-gray-50'}`}
                            >
                                <List className="w-3 h-3 mr-2" />
                                Lista
                            </button>
                        </div>

                        <div className="relative flex-1 xl:w-80">
                            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                            <input
                                type="text"
                                className="w-full pl-11 pr-4 py-3 bg-white border border-gray-100 rounded-2xl text-xs font-bold shadow-sm focus:ring-2 focus:ring-blue-500 transition-all"
                                placeholder="Buscar negocio o cliente..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                        </div>
                        <button className="p-3 bg-white border border-gray-100 rounded-2xl shadow-sm hover:bg-gray-50 text-gray-400 transition-all">
                            <Filter className="w-5 h-5" />
                        </button>
                        <button
                            onClick={() => navigate('/comercial/solicitudes-costos')}
                            className="flex items-center px-6 py-3 bg-blue-600 text-white rounded-2xl font-black text-xs uppercase tracking-widest shadow-xl shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                        >
                            <Plus className="w-4 h-4 mr-2" />
                            Nueva Solicitud
                        </button>
                    </div>
                </div>
            </div>

            {/* Content Area */}
            <div className="max-w-[1600px] mx-auto">
                {viewMode === 'kanban' ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8 animate-in fade-in duration-500">
                        {COLUMNS.map(column => (
                            <div key={column.id} className="flex flex-col h-full">
                                <div className={`mb-6 flex justify-between items-center pb-4 border-b-2 ${column.color}`}>
                                    <div className="flex items-center space-x-2">
                                        <span className="text-sm font-black text-gray-800 uppercase tracking-widest">{column.title}</span>
                                        <span className="px-2 py-0.5 bg-gray-100 text-[10px] font-black text-gray-400 rounded-lg">
                                            {getColumnItems(column.id).length}
                                        </span>
                                    </div>
                                    <MoreVertical className="w-4 h-4 text-gray-300" />
                                </div>

                                <div className="space-y-4">
                                    {getColumnItems(column.id).map(item => (
                                        <div
                                            key={item.idSolicitud}
                                            onClick={() => handleCardClick(item)}
                                            className="bg-white p-6 rounded-[2rem] shadow-sm border border-gray-100 hover:shadow-xl hover:shadow-blue-50 hover:-translate-y-1 transition-all cursor-pointer group relative overflow-hidden"
                                        >
                                            <div className="absolute top-0 right-0 w-24 h-24 bg-gray-50 rounded-full -mr-12 -mt-12 group-hover:bg-blue-50 transition-colors" />

                                            <div className="relative z-10">
                                                <div className="flex justify-between items-start mb-4">
                                                    <span className="text-[9px] font-black text-gray-300 uppercase tracking-widest">{item.idSolicitud || item.id_solicitud || item.id}</span>
                                                    <CostoBadge status={item.estado} />
                                                </div>

                                                <h4 className="text-sm font-black text-gray-800 mb-1 group-hover:text-blue-600 transition-colors truncate">
                                                    {item.clienteNombre || 'Cliente S/N'}
                                                </h4>

                                                <div className="flex items-center text-[10px] font-bold text-gray-400 mb-4">
                                                    <Calendar className="w-3 h-3 mr-1" />
                                                    {item.fecha}
                                                </div>

                                                {(item.articuloDescripcion || item.articulo_descripcion) && (
                                                    <p className="text-[10px] font-bold text-blue-600 uppercase mb-2 line-clamp-1">
                                                        {item.articuloDescripcion || item.articulo_descripcion}
                                                    </p>
                                                )}

                                                <SpecsCard specs={item.especificaciones || {
                                                    tela: item.telas?.[0]?.descripcion || 'Ver detalle',
                                                    color: 'N/A',
                                                    gramos: 0
                                                }} />

                                                <div className="mt-6 pt-4 border-t border-gray-50 flex justify-between items-center">
                                                    <div className="flex -space-x-2">
                                                        <div className="w-7 h-7 bg-indigo-100 rounded-full border-2 border-white flex items-center justify-center text-[10px] font-black text-indigo-600">
                                                            <Users className="w-3 h-3" />
                                                        </div>
                                                    </div>
                                                    <div className="flex items-center text-[10px] font-black text-blue-600 uppercase tracking-widest opacity-0 group-hover:opacity-100 translate-x-4 group-hover:translate-x-0 transition-all">
                                                        Gestionar
                                                        <ArrowRight className="w-3 h-3 ml-1" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}

                                    {getColumnItems(column.id).length === 0 && (
                                        <div className="py-20 flex flex-col items-center justify-center border-2 border-dashed border-gray-100 rounded-[2.5rem]">
                                            <AlertCircle className="w-8 h-8 text-gray-100 mb-2" />
                                            <p className="text-[10px] font-black text-gray-200 uppercase tracking-widest">Sin Pendientes</p>
                                        </div>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="space-y-4 max-w-6xl animate-in fade-in duration-500">
                        {adaptedItems.map((item, index) => (
                            <ProgressCard
                                key={item.idSolicitud || item.id_solicitud || item.id || index}
                                item={item}
                                stages={STAGES}
                                type="comercial"
                            />
                        ))}
                        {adaptedItems.length === 0 && (
                            <div className="py-20 flex flex-col items-center justify-center bg-white border-2 border-dashed border-gray-100 rounded-[2.5rem]">
                                <AlertCircle className="w-12 h-12 text-gray-100 mb-4" />
                                <p className="text-xs font-black text-gray-300 uppercase tracking-widest">No se encontraron registros comerciales</p>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
