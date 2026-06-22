import React, { useState, useMemo } from 'react';
import {
    DollarSign,
    PieChart,
    TrendingUp,
    Search,
    Filter,
    X,
    Layers,
    Calculator,
    ClipboardList,
    CheckCircle2,
    XCircle,
    RotateCcw
} from 'lucide-react';
import EstadoCosteo from '../../../../../remote/DTO/EstadoCosteo';

// Mapeo único estado → etiqueta + estilos del badge. Cubre el ciclo de vida del
// Costeo (BORRADOR/COSTEADO/APROBADO/RECHAZADO) y los estados legacy de SCOS.
const ESTADO_BADGE = {
    BORRADOR:  { label: 'Borrador',  className: 'bg-gray-100 text-gray-600 ring-1 ring-gray-200' },
    COSTEADO:  { label: 'Costeado',  className: 'bg-blue-100 text-blue-600 ring-1 ring-blue-200' },
    APROBADO:  { label: '✓ Aprobado', className: 'bg-emerald-100 text-emerald-700 ring-1 ring-emerald-200' },
    RECHAZADO: { label: 'Rechazado', className: 'bg-red-100 text-red-600 ring-1 ring-red-200' },
    // Legacy SCOS
    APROBADA:          { label: '✓ Aprobado', className: 'bg-emerald-100 text-emerald-700 ring-1 ring-emerald-200' },
    'COSTEO REALIZADO': { label: 'Costeado', className: 'bg-blue-100 text-blue-600 ring-1 ring-blue-200' },
    PENDIENTE:         { label: 'Pendiente', className: 'bg-amber-100 text-amber-600' },
};

function badgeFor(estado) {
    if (!estado) return { label: 'Pendiente', className: 'bg-amber-100 text-amber-600' };
    return ESTADO_BADGE[estado]
        ?? ESTADO_BADGE[estado.toUpperCase?.()]
        ?? { label: estado, className: 'bg-amber-100 text-amber-600' };
}

export default function ListaCosteos({
    onOpenDashboard,
    onOpenCompare,
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    recordsToDisplay,
    clientes = [],
    handleOpenForm,
    onAprobar,
    onRechazar,
    onReabrir
}) {
    // Modal de rechazo: record en curso + motivo obligatorio.
    const [rechazoRecord, setRechazoRecord] = useState(null);
    const [motivo, setMotivo] = useState('');

    // Optimización O(1) para evitar el .find en cada iteración del .map
    const clienteMap = useMemo(() => {
        const map = new Map();
        clientes.forEach(c => {
            const id = (c.clienteId || c.id)?.toString();
            if (id) map.set(id, c);
        });
        return map;
    }, [clientes]);

    const confirmarRechazo = () => {
        if (!motivo.trim()) return;
        onRechazar?.(rechazoRecord, motivo.trim());
        setRechazoRecord(null);
        setMotivo('');
    };

    return (
        <div className="max-w-full p-4 space-y-8 animate-in fade-in duration-700">
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div className="flex flex-col md:flex-row md:items-center gap-4">
                    <div>
                        <h1 className="text-4xl font-black text-gray-800 tracking-tight italic flex items-center">
                            <DollarSign className="w-10 h-10 mr-4 text-green-600" />
                            Costeos OP
                        </h1>
                        <p className="text-gray-400 font-bold text-xs uppercase tracking-widest mt-2 ml-1">
                            Análisis de Costos y Márgenes Directos
                        </p>
                    </div>
                    <button
                        onClick={onOpenDashboard}
                        className="flex items-center gap-3 px-6 py-3 bg-white border-2 border-green-500/20 text-green-600 rounded-2xl text-[10px] font-black uppercase tracking-[0.2em] hover:bg-green-500 hover:text-white hover:shadow-lg hover:shadow-green-100 transition-all active:scale-95 shadow-sm"
                    >
                        <PieChart className="w-4 h-4" />
                        Dashboard de Costeos
                    </button>
                    <button
                        onClick={onOpenCompare}
                        className="flex items-center gap-3 px-6 py-3 bg-white border-2 border-indigo-500/20 text-indigo-600 rounded-2xl text-[10px] font-black uppercase tracking-[0.2em] hover:bg-indigo-600 hover:text-white hover:shadow-lg hover:shadow-indigo-100 transition-all active:scale-95 shadow-sm"
                    >
                        <TrendingUp className="w-4 h-4" />
                        Comparativa de Precios
                    </button>
                </div>
            </div>

            {/* Filters */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 bg-white p-4 rounded-[2rem] shadow-sm border border-gray-100">
                <div className="md:col-span-2 relative">
                    <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar por cliente o ID de costeo..."
                        className="w-full pl-11 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-sm font-bold focus:ring-2 focus:ring-green-500 transition-all outline-none"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
                
                {/* Desplegable Estilizado */}
                <div className="relative">
                    <Filter className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-3 h-3 pointer-events-none" />
                    <select
                        className="w-full pl-10 pr-10 py-3 bg-gray-50 border border-transparent rounded-2xl text-xs font-black uppercase tracking-widest focus:ring-2 focus:ring-green-500 focus:bg-white transition-all outline-none appearance-none cursor-pointer text-gray-700 shadow-sm hover:bg-gray-100/70"
                        style={{
                            backgroundImage: `url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%236b7280'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2.5' d='M19 9l-7 7-7-7'/%3E%3C/svg%3E")`,
                            backgroundPosition: 'right 1rem center',
                            backgroundSize: '1rem',
                            backgroundRepeat: 'no-repeat'
                        }}
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="Todos">Todos los Estados</option>
                        <option value="BORRADOR">Borrador</option>
                        <option value="COSTEADO">Costeado</option>
                        <option value="APROBADO">Aprobado</option>
                        <option value="RECHAZADO">Rechazado</option>
                    </select>
                </div>

                <button
                    onClick={() => {
                        setSearchTerm('');
                        setStatusFilter('Todos');
                    }}
                    className="flex items-center justify-center gap-2 px-4 py-3 bg-gray-900 text-white rounded-2xl text-xs font-black uppercase tracking-widest hover:bg-gray-800 transition-all active:scale-95"
                >
                    <X className="w-3 h-3" />
                    Limpiar Filtros
                </button>
            </div>

            {/* Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {recordsToDisplay.map((record) => {
                    const recordClienteId = record.clienteId?.toString();
                    const cliente = clienteMap.get(recordClienteId);
                    
                    const displayId = record.numero || record.id;
                    const estadoCosteo = record.costeoEstado;
                    
                    const puedeAprobar = estadoCosteo === EstadoCosteo.COSTEADO;
                    const puedeRechazar = estadoCosteo === EstadoCosteo.BORRADOR || estadoCosteo === EstadoCosteo.COSTEADO;
                    const puedeReabrir = estadoCosteo === EstadoCosteo.RECHAZADO || estadoCosteo === EstadoCosteo.COSTEADO;
                    
                    return (
                        <div
                            key={displayId}
                            onClick={() => handleOpenForm(record)}
                            className="group bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 hover:border-green-500 hover:shadow-2xl hover:shadow-green-50 transition-all cursor-pointer relative overflow-hidden flex flex-col"
                        >
                            <div className="flex justify-between items-start mb-4">
                                <div className="flex-1">
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] font-black text-gray-300 uppercase tracking-widest">{displayId}</span>
                                        <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest ${badgeFor(estadoCosteo ?? record.estado).className}`}>
                                            {badgeFor(estadoCosteo ?? record.estado).label}
                                        </span>
                                        {record.costeoVersion != null && (
                                            <span className="px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest bg-indigo-50 text-indigo-500">v{record.costeoVersion}</span>
                                        )}
                                    </div>
                                    <h3 className="text-md font-black text-gray-800 group-hover:text-green-600 transition-colors uppercase leading-tight">
                                        {record.clienteNombre || cliente?.razonSocial || cliente?.nombreCliente || cliente?.nombre || 'Cliente SCOS'}
                                    </h3>
                                </div>
                            </div>

                            <div className="space-y-3 mb-6">
                                <div className="p-4 bg-gray-50 rounded-2xl flex flex-col justify-center">
                                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">Costo Total Costeo OP</p>
                                    <p className="text-xl font-black text-gray-800 tracking-tight">
                                        ${(record.costoTotal || 0).toLocaleString('es-CL')}
                                    </p>
                                </div>
                                <div className="flex items-center gap-2 px-1">
                                    <Layers className="w-3 h-3 text-gray-300" />
                                    <span className="text-[10px] font-bold text-gray-400 uppercase italic">
                                        {record.articuloDescripcion}
                                    </span>
                                </div>
                            </div>

                            <div className="flex items-center justify-between mt-auto pt-4 border-t border-gray-50">
                                <div className="flex flex-col">
                                    <p className="text-[8px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Cant. Requerida</p>
                                    <p className="text-xs font-black text-blue-600 italic">{record.cantidad || 0} und</p>
                                </div>
                                <button
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handleOpenForm(record);
                                    }}
                                    className="px-4 py-2 bg-indigo-600 text-white text-[9px] font-black rounded-xl uppercase tracking-widest hover:bg-indigo-700 transition-all flex items-center shadow-lg shadow-indigo-100"
                                >
                                    <Calculator className="w-3 h-3 mr-2" />
                                    {estadoCosteo === EstadoCosteo.APROBADO
                                        ? 'Ver Costeo'
                                        : estadoCosteo === EstadoCosteo.COSTEADO
                                        ? 'Revisar Costos'
                                        : 'Añadir Costos'}
                                </button>
                            </div>

                            {/* Decisión sobre el costeo */}
                            <div className="flex items-center gap-2 mt-3">
                                <button
                                    onClick={(e) => { e.stopPropagation(); onAprobar?.(record); }}
                                    disabled={!puedeAprobar}
                                    title={puedeAprobar ? 'Aprobar costeo' : 'Solo se puede aprobar un costeo COSTEADO'}
                                    className={`flex-1 px-3 py-2 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all ${puedeAprobar
                                        ? 'bg-emerald-600 text-white hover:bg-emerald-700 active:scale-95 shadow-lg shadow-emerald-100'
                                        : 'bg-gray-100 text-gray-300 cursor-not-allowed'}`}
                                >
                                    <CheckCircle2 className="w-3 h-3" />
                                    Aprobar
                                </button>
                                <button
                                    onClick={(e) => { e.stopPropagation(); setRechazoRecord(record); setMotivo(''); }}
                                    disabled={!puedeRechazar}
                                    title={puedeRechazar ? 'Rechazar costeo' : 'Solo se puede rechazar un costeo BORRADOR o COSTEADO'}
                                    className={`flex-1 px-3 py-2 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all ${puedeRechazar
                                        ? 'bg-red-600 text-white hover:bg-red-700 active:scale-95 shadow-lg shadow-red-100'
                                        : 'bg-gray-100 text-gray-300 cursor-not-allowed'}`}
                                >
                                    <XCircle className="w-3 h-3" />
                                    Rechazar
                                </button>
                            </div>

                            {/* Reabrir costeo */}
                            {puedeReabrir && (
                                <div className="mt-2">
                                    <button
                                        onClick={(e) => { e.stopPropagation(); onReabrir?.(record); }}
                                        title="Reabrir costeo a Borrador"
                                        className="w-full px-3 py-2 text-[9px] font-black rounded-xl uppercase tracking-widest flex items-center justify-center gap-2 transition-all bg-amber-500 text-white hover:bg-amber-600 active:scale-95 shadow-lg shadow-amber-100"
                                    >
                                        <RotateCcw className="w-3 h-3" />
                                        Reabrir
                                    </button>
                                </div>
                            )}
                        </div>
                    );
                })}
                {recordsToDisplay.length === 0 && (
                    <div className="col-span-full py-20 text-center">
                        <ClipboardList className="w-16 h-16 text-gray-100 mx-auto mb-4" />
                        <p className="text-xs font-black text-gray-300 uppercase tracking-widest italic">
                            No se encontraron solicitudes pendientes de costeo
                        </p>
                    </div>
                )}
            </div>

            {/* Modal pequeño */}
            {rechazoRecord && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4" onClick={() => setRechazoRecord(null)}>
                    <div className="bg-white w-full max-w-md rounded-[2rem] p-6 shadow-2xl" onClick={(e) => e.stopPropagation()}>
                        <div className="flex items-center gap-3 mb-4">
                            <div className="w-10 h-10 rounded-2xl bg-red-50 flex items-center justify-center">
                                <XCircle className="w-5 h-5 text-red-500" />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-tight">Rechazar costeo</h3>
                                <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">
                                    {rechazoRecord.numero || rechazoRecord.id}
                                </p>
                            </div>
                        </div>
                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest">Motivo del rechazo *</label>
                        <textarea
                            value={motivo}
                            onChange={(e) => setMotivo(e.target.value)}
                            rows={3}
                            autoFocus
                            placeholder="Describe la diferencia o el motivo del rechazo…"
                            className="w-full mt-1 p-3 bg-gray-50 border-none rounded-2xl text-sm font-bold focus:ring-2 focus:ring-red-400 outline-none resize-none"
                        />
                        <div className="flex items-center justify-end gap-2 mt-4">
                            <button
                                onClick={() => setRechazoRecord(null)}
                                className="px-4 py-2 text-[10px] font-black uppercase tracking-widest text-gray-500 rounded-xl hover:bg-gray-100 transition-all"
                            >
                                Cancelar
                            </button>
                            <button
                                onClick={confirmarRechazo}
                                disabled={!motivo.trim()}
                                className={`px-5 py-2 text-[10px] font-black uppercase tracking-widest rounded-xl transition-all ${motivo.trim()
                                    ? 'bg-red-600 text-white hover:bg-red-700 active:scale-95'
                                    : 'bg-gray-100 text-gray-300 cursor-not-allowed'}`}
                            >
                                Confirmar rechazo
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}