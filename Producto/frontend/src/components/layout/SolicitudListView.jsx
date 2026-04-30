import { ClipboardList, Plus, Calculator, Search, Calendar, ChevronRight, Trash2, Download } from 'lucide-react';

export default function SolicitudListView({
    title = "Solicitudes",
    subtitle = "Gestión de Fichas Técnicas y Preventa",
    searchTerm,
    setSearchTerm,
    filteredRecords,
    onCreateCosto,
    onCreateCotizacion,
    onOpen,
    onDelete,
    onDownloadPDF,
}) {
    return (
        <div className="max-w-7xl mx-auto p-6 space-y-8 animate-in fade-in duration-700">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                    <h1 className="text-4xl font-black text-gray-800 tracking-tight italic flex items-center">
                        <ClipboardList className="w-10 h-10 mr-4 text-blue-600" />
                        {title}
                    </h1>
                    <p className="text-gray-400 font-bold text-xs uppercase tracking-widest mt-2 ml-1">
                        {subtitle}
                    </p>
                </div>
                <div className="flex flex-col sm:flex-row gap-3">
                    {onCreateCosto && (
                        <button
                            onClick={onCreateCosto}
                            className="flex items-center px-6 py-3 bg-blue-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                        >
                            <Plus className="w-4 h-4 mr-2" />
                            Nueva Solicitud de Costos
                        </button>
                    )}
                    {onCreateCotizacion && (
                        <button
                            onClick={onCreateCotizacion}
                            className="flex items-center px-6 py-3 bg-white border-2 border-blue-600 text-blue-600 rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-sm hover:bg-blue-50 hover:-translate-y-0.5 transition-all"
                        >
                            <Calculator className="w-4 h-4 mr-2" />
                            Crear solicitud
                        </button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-4 bg-white p-4 rounded-[2rem] shadow-sm border border-gray-100">
                <div className="lg:col-span-3 relative">
                    <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar por cliente, ID o prenda..."
                        className="w-full pl-11 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-sm font-bold focus:ring-2 focus:ring-blue-500 transition-all outline-none"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {filteredRecords.map((record) => (
                    <div
                        key={`${record.id}-${record.tipo}`}
                        className="group bg-white p-6 rounded-[2.5rem] border-2 border-gray-50 hover:border-blue-500 hover:shadow-2xl hover:shadow-blue-50 transition-all relative overflow-hidden"
                    >
                        <div
                            onClick={() => onOpen(record)}
                            className="cursor-pointer"
                        >
                            <div className="flex justify-between items-start mb-4">
                                <div>
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] font-black text-gray-300 uppercase tracking-widest">
                                            #{record.numero || record.id}
                                        </span>
                                        <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-tighter border ${record.tipo?.toUpperCase() === 'SCOT'
                                            ? 'bg-blue-50 text-blue-600 border-blue-100'
                                            : 'bg-orange-50 text-orange-600 border-orange-100'
                                            }`}>
                                            {record.tipo}
                                        </span>
                                    </div>
                                    <h3 className="text-md font-black text-gray-800 group-hover:text-blue-600 transition-colors uppercase leading-tight mt-1">
                                        {record.articuloDescripcion}
                                    </h3>
                                </div>
                            </div>

                            <div className="space-y-3 mb-4">
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-3 h-3 text-gray-400" />
                                    <span className="text-[10px] font-bold text-gray-500">
                                        {record.fecha}
                                    </span>
                                </div>
                            </div>

                            <div className="flex items-center justify-between text-[10px] font-black uppercase tracking-widest text-blue-600 opacity-0 group-hover:opacity-100 transition-all">
                                <span>Ver Ficha</span>
                                <ChevronRight className="w-4 h-4" />
                            </div>
                        </div>

                        {/* Acciones */}
                        <div className="flex gap-2 mt-4 pt-4 border-t border-gray-50">
                            {onDownloadPDF && (
                                <button
                                    onClick={(e) => { e.stopPropagation(); onDownloadPDF(record); }}
                                    className="flex items-center gap-1 px-3 py-1.5 text-[9px] font-black uppercase tracking-widest text-gray-500 hover:text-blue-600 hover:bg-blue-50 rounded-xl transition-all"
                                >
                                    <Download className="w-3 h-3" /> PDF
                                </button>
                            )}
                            {onDelete && (
                                <button
                                    onClick={(e) => { e.stopPropagation(); onDelete(record); }}
                                    className="flex items-center gap-1 px-3 py-1.5 text-[9px] font-black uppercase tracking-widest text-gray-500 hover:text-red-600 hover:bg-red-50 rounded-xl transition-all"
                                >
                                    <Trash2 className="w-3 h-3" /> Eliminar
                                </button>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
