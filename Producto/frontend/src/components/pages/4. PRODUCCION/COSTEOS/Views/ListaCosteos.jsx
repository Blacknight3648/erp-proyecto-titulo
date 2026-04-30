import React from 'react';
import { 
    DollarSign, 
    PieChart, 
    TrendingUp, 
    Search, 
    Filter, 
    X, 
    Layers, 
    Calculator, 
    ClipboardList 
} from 'lucide-react';

export default function ListaCosteos({ 
    onOpenDashboard, 
    onOpenCompare, 
    searchTerm, 
    setSearchTerm, 
    statusFilter, 
    setStatusFilter, 
    recordsToDisplay, 
    clientes, 
    handleOpenForm 
}) {
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
                <div className="relative">
                    <Filter className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-3 h-3" />
                    <select
                        className="w-full pl-10 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-xs font-black uppercase tracking-widest focus:ring-2 focus:ring-green-500 transition-all outline-none appearance-none cursor-pointer"
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="Todos">Todos los Estados</option>
                        <option value="Pendiente">Pendiente Producción</option>
                        <option value="Costeado">Costeado</option>
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
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 ">
                {recordsToDisplay.map((record) => {
                    const cliente = clientes.find(c => (c.clienteId || c.id)?.toString() === record.clienteId?.toString());
                    const displayId = record.numero || record.id;
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
                                        <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest ${record.estado === 'Costeado' ? 'bg-green-100 text-green-600' : 'bg-amber-100 text-amber-600'
                                            }`}>
                                            {record.estado}
                                        </span>
                                    </div>
                                    <h3 className="text-md font-black text-gray-800 group-hover:text-green-600 transition-colors uppercase leading-tight">
                                        {record.clienteNombre || cliente?.nombreCliente || cliente?.nombre || 'Cliente SCOS'}
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
                                    {record.estado === 'Costeado' ? 'Revisar Costos' : 'Añadir Costos'}
                                </button>
                            </div>
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
        </div>
    );
}
