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
                    <h1 className="text-4xl font-black text-foreground tracking-tight italic flex items-center">
                        <ClipboardList className="w-10 h-10 mr-4 text-accent-foreground" />
                        {title}
                    </h1>
                    <p className="text-muted-foreground font-bold text-xs uppercase tracking-widest mt-2 ml-1">
                        {subtitle}
                    </p>
                </div>
                <div className="flex flex-col sm:flex-row gap-3">
                    {onCreateCosto && (
                        <button
                            onClick={onCreateCosto}
                            className="flex items-center px-6 py-3 bg-primary text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl shadow-accent hover:bg-primary hover:-translate-y-0.5 transition-all"
                        >
                            <Plus className="w-4 h-4 mr-2" />
                            Nueva Solicitud de Costos
                        </button>
                    )}
                    {onCreateCotizacion && (
                        <button
                            onClick={onCreateCotizacion}
                            className="flex items-center px-6 py-3 bg-card border-2 border-primary text-accent-foreground rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-sm hover:bg-accent hover:-translate-y-0.5 transition-all"
                        >
                            <Calculator className="w-4 h-4 mr-2" />
                            Crear solicitud
                        </button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-4 bg-card p-4 rounded-[2rem] shadow-sm border border-border">
                <div className="lg:col-span-3 relative">
                    <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar por cliente, ID o prenda..."
                        className="w-full pl-11 pr-4 py-3 bg-muted border-none rounded-2xl text-sm font-bold focus:ring-2 focus:ring-primary transition-all outline-none"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {filteredRecords.map((record) => (
                    <div
                        key={`${record.id}-${record.tipo}`}
                        className="group bg-card p-6 rounded-[2.5rem] border-2 border-border hover:border-primary hover:shadow-2xl hover:shadow-accent transition-all relative overflow-hidden"
                    >
                        <div
                            onClick={() => onOpen(record)}
                            className="cursor-pointer"
                        >
                            <div className="flex justify-between items-start mb-4">
                                <div>
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">
                                            #{record.numero || record.id}
                                        </span>
                                        <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-tighter border ${record.tipo?.toUpperCase() === 'SCOT'
                                            ? 'bg-accent text-accent-foreground border-accent'
                                            : 'bg-warning-bg text-warning border-warning-bg'
                                            }`}>
                                            {record.tipo}
                                        </span>
                                    </div>
                                    <h3 className="text-md font-black text-foreground group-hover:text-accent-foreground transition-colors uppercase leading-tight mt-1">
                                        {record.articuloDescripcion}
                                    </h3>
                                </div>
                            </div>

                            <div className="space-y-3 mb-4">
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-3 h-3 text-muted-foreground" />
                                    <span className="text-[10px] font-bold text-muted-foreground">
                                        {record.fecha}
                                    </span>
                                </div>
                            </div>

                            <div className="flex items-center justify-between text-[10px] font-black uppercase tracking-widest text-accent-foreground opacity-0 group-hover:opacity-100 transition-all">
                                <span>Ver Ficha</span>
                                <ChevronRight className="w-4 h-4" />
                            </div>
                        </div>

                        {/* Acciones */}
                        <div className="flex gap-2 mt-4 pt-4 border-t border-border">
                            {onDownloadPDF && (
                                <button
                                    onClick={(e) => { e.stopPropagation(); onDownloadPDF(record); }}
                                    className="flex items-center gap-1 px-3 py-1.5 text-[9px] font-black uppercase tracking-widest text-muted-foreground hover:text-accent-foreground hover:bg-accent rounded-xl transition-all"
                                >
                                    <Download className="w-3 h-3" /> PDF
                                </button>
                            )}
                            {onDelete && (
                                <button
                                    onClick={(e) => { e.stopPropagation(); onDelete(record); }}
                                    className="flex items-center gap-1 px-3 py-1.5 text-[9px] font-black uppercase tracking-widest text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-xl transition-all"
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
