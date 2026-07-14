import { useState, useEffect } from 'react';
import {
    Search,
    ChevronRight,
    FileText,
    Settings,
    ShoppingCart,
    Factory,
    Truck,
    CheckCircle2,
    AlertCircle,
    Clock,
    ArrowRight,
    ClipboardList,
    Layers,
    User,
    Calendar,
    Wrench
} from 'lucide-react';

export default function TimelineGlobal() {
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedNV, setSelectedNV] = useState(null);
    const [loading, setLoading] = useState(false);
    const [traceData, setTraceData] = useState([]);

    const BACKEND_URL = import.meta.env.VITE_API_URL || "";

    const handleSearch = async () => {
        if (!searchQuery) return;
        setLoading(true);
        try {
            const response = await fetch(`${BACKEND_URL}/api/v1/comercial/notas-venta/${searchQuery}/trazabilidad`);
            if (response.ok) {
                const data = await response.json();
                setTraceData(data);

                // Also fetch NV details for header info
                const nvResponse = await fetch(`${BACKEND_URL}/api/v1/comercial/notas-venta/${searchQuery}`);
                if (nvResponse.ok) {
                    const nvData = await nvResponse.json();
                    setSelectedNV(nvData);
                }
            } else {
                alert('No se encontró la Nota de Venta');
                setTraceData([]);
                setSelectedNV(null);
            }
        } catch (error) {
            console.error('Error fetching traceability:', error);
        } finally {
            setLoading(false);
        }
    };

    const getIconByDocType = (type) => {
        switch (type) {
            case 'Solicitud Costos': return <ClipboardList className="w-5 h-5" />;
            case 'Costeo': return <Settings className="w-5 h-5" />;
            case 'Evaluación Negocio': return <Layers className="w-5 h-5" />;
            case 'Nota Venta': return <FileText className="w-5 h-5" />;
            case 'Orden Producción': return <Factory className="w-5 h-5" />;
            case 'Orden Trabajo (OT)': return <Wrench className="w-5 h-5" />;
            default: return <FileText className="w-5 h-5" />;
        }
    };

    const getColorByDocType = (type) => {
        switch (type) {
            case 'Solicitud Costos': case 'Costeo': return 'bg-muted text-muted-foreground border-border';
            case 'Evaluación Negocio': return 'bg-brand-indigo/10 text-brand-indigo border-brand-indigo/20';
            case 'Nota Venta': return 'bg-primary/10 text-primary border-primary/20';
            case 'Orden Producción': return 'bg-warning/10 text-warning border-warning/20';
            case 'Orden Trabajo (OT)': return 'bg-warning/10 text-warning border-warning/30';
            default: return 'bg-muted text-muted-foreground border-border';
        }
    };

    const esEstadoRechazado = (estado) =>
        estado === 'RECHAZADA' || estado === 'RECHAZADO' || estado === 'CANCELADA';

    return (
        <div className="max-w-6xl mx-auto space-y-8 animate-in fade-in duration-700 pb-20">
            {/* Header / Search */}
            <div className="bg-card p-10 rounded-[3rem] shadow-xl shadow-primary/5 border border-border relative overflow-hidden">
                <div className="absolute top-0 right-0 w-64 h-64 bg-primary/10 blur-[100px] -z-10"></div>
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
                    <div>
                        <h1 className="text-4xl font-black text-foreground tracking-tight italic flex items-center">
                            <Truck className="w-10 h-10 mr-4 text-primary" />
                            Trazabilidad Global
                        </h1>
                        <p className="text-muted-foreground font-bold text-xs uppercase tracking-[0.2em] mt-3 ml-1">Monitoreo 360° del ciclo de vida del producto</p>
                    </div>

                    <div className="flex bg-muted p-2 rounded-3xl border border-border shadow-inner w-full md:w-auto">
                        <input
                            type="text"
                            placeholder="Ingrese N° de Nota de Venta (ej: 1)"
                            className="bg-transparent border-none px-6 py-3 text-sm font-bold outline-none flex-1 md:w-64"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                        />
                        <button
                            onClick={handleSearch}
                            disabled={loading}
                            className="bg-primary hover:bg-primary-hover text-primary-foreground px-8 py-3 rounded-2xl font-black tracking-widest text-xs uppercase shadow-lg shadow-primary/20 transition-all flex items-center"
                        >
                            {loading ? <Clock className="w-4 h-4 animate-spin" /> : <Search className="w-4 h-4" />}
                        </button>
                    </div>
                </div>
            </div>

            {selectedNV && (
                <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
                    {/* Sidebar: NV Info */}
                    <div className="lg:col-span-1 space-y-6">
                        <div className="bg-sidebar text-white p-8 rounded-[2.5rem] shadow-2xl relative overflow-hidden">
                            <div className="absolute top-0 right-0 w-32 h-32 bg-primary/20 blur-[50px]"></div>
                            <h2 className="text-[10px] font-black uppercase tracking-[0.3em] text-sidebar-primary mb-6">Información General</h2>

                            <div className="space-y-6 relative z-10">
                                <div className="flex items-center space-x-4">
                                    <div className="bg-white/10 p-3 rounded-2xl">
                                        <User className="w-5 h-5 text-sidebar-primary" />
                                    </div>
                                    <div>
                                        <p className="text-[8px] font-black text-white/40 uppercase tracking-widest">Cliente ID</p>
                                        <p className="text-sm font-black">{selectedNV.clienteId}</p>
                                    </div>
                                </div>

                                <div className="flex items-center space-x-4">
                                    <div className="bg-white/10 p-3 rounded-2xl">
                                        <Calendar className="w-5 h-5 text-sidebar-primary" />
                                    </div>
                                    <div>
                                        <p className="text-[8px] font-black text-white/40 uppercase tracking-widest">Entrega Estimada</p>
                                        <p className="text-sm font-black">{selectedNV.fechaEntregaEstimada}</p>
                                    </div>
                                </div>

                                <div className="pt-6 border-t border-white/10">
                                    <div className={`inline-flex items-center px-4 py-2 rounded-full text-[10px] font-black uppercase tracking-widest ${selectedNV.estado === 'FINALIZADA' ? 'bg-success/20 text-success border border-success/30' : 'bg-primary/20 text-sidebar-primary border border-primary/30'
                                        }`}>
                                        {selectedNV.estado}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="bg-card p-8 rounded-[2.5rem] border border-border shadow-sm">
                            <h3 className="text-[10px] font-black uppercase tracking-[0.2em] text-muted-foreground mb-6 flex items-center">
                                <AlertCircle className="w-3 h-3 mr-2" /> Leyenda del Flujo
                            </h3>
                            <div className="space-y-4">
                                {['Comercial', 'Producción'].map(step => (
                                    <div key={step} className="flex items-center space-x-3">
                                        <div className={`w-2 h-2 rounded-full ${step === 'Comercial' ? 'bg-primary' : 'bg-warning'
                                            }`}></div>
                                        <span className="text-xs font-bold text-foreground">{step}</span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>

                    {/* Main Timeline */}
                    <div className="lg:col-span-3 bg-card p-12 rounded-[3rem] border border-border shadow-sm relative">
                        <h2 className="text-2xl font-black text-foreground tracking-tight italic mb-12 flex items-center">
                            <ArrowRight className="w-6 h-6 mr-3 text-primary" />
                            Línea de Tiempo del Documento
                        </h2>

                        <div className="relative">
                            {/* Vertical Line */}
                            <div className="absolute left-8 top-0 bottom-0 w-0.5 bg-border"></div>

                            {/* Timeline Items */}
                            <div className="space-y-12">
                                {traceData.length > 0 ? traceData.map((doc, idx) => (
                                    <div key={idx} className="relative pl-24 group">
                                        {/* Connector Circle */}
                                        <div className={`absolute left-[26px] top-6 w-4 h-4 rounded-full border-4 border-card z-10 shadow-sm transition-transform group-hover:scale-125 ${esEstadoRechazado(doc.estado) ? 'bg-destructive' : doc.estado === 'FINALIZADA' || doc.estado === 'COMPLETO' ? 'bg-success' : 'bg-primary'
                                            }`}></div>

                                        <div className={`p-8 rounded-[2rem] border-2 transition-all group-hover:shadow-xl group-hover:border-primary/20 ${getColorByDocType(doc.tipoDocumento)}`}>
                                            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                                                <div className="flex items-center space-x-6">
                                                    <div className="p-4 bg-card/60 rounded-2xl shadow-sm border border-card">
                                                        {getIconByDocType(doc.tipoDocumento)}
                                                    </div>
                                                    <div>
                                                        <p className="text-[10px] font-black uppercase tracking-[0.2em] opacity-60 mb-1">{doc.tipoDocumento}</p>
                                                        <h4 className="text-xl font-black tracking-tight">{doc.numero}</h4>
                                                    </div>
                                                </div>

                                                <div className="flex items-center space-x-8">
                                                    <div className="text-right">
                                                        <p className="text-[10px] font-black uppercase tracking-[0.2em] opacity-40 mb-1">Estado Actual</p>
                                                        <div className="flex items-center justify-end space-x-2">
                                                            {doc.estado === 'FINALIZADA' || doc.estado === 'COMPLETO' ? (
                                                                <CheckCircle2 className="w-3.5 h-3.5 text-success" />
                                                            ) : (
                                                                <Clock className="w-3.5 h-3.5 text-primary" />
                                                            )}
                                                            <span className="text-xs font-black uppercase tracking-widest">{doc.estado}</span>
                                                        </div>
                                                    </div>

                                                    {doc.fecha && (
                                                        <div className="text-right border-l border-black/5 pl-8">
                                                            <p className="text-[10px] font-black uppercase tracking-[0.2em] opacity-40 mb-1">Fecha</p>
                                                            <p className="text-sm font-black italic">{doc.fecha}</p>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>

                                            {doc.motivoRechazo && (
                                                <div className="mt-6 flex items-start space-x-2 text-xs text-destructive bg-destructive/10 border border-destructive/30 rounded-2xl px-4 py-3">
                                                    <AlertCircle className="w-4 h-4 mt-0.5 shrink-0" />
                                                    <span>{doc.motivoRechazo}{doc.fechaRechazo ? ` (${doc.fechaRechazo})` : ''}</span>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )) : (
                                    <div className="text-center py-20 text-muted-foreground">
                                        <Search className="w-12 h-12 mx-auto mb-4 opacity-20" />
                                        <p className="text-xs font-black uppercase tracking-widest">Ingrese una Nota de Venta para ver su flujo</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {!selectedNV && !loading && (
                <div className="bg-primary/5 border border-primary/20 rounded-[3rem] p-20 text-center animate-in zoom-in-95 duration-700">
                    <div className="max-w-md mx-auto">
                        <div className="w-20 h-20 bg-primary text-primary-foreground rounded-[2rem] flex items-center justify-center mx-auto mb-8 shadow-2xl shadow-primary/30">
                            <Search className="w-8 h-8" />
                        </div>
                        <h2 className="text-2xl font-black text-foreground tracking-tight italic mb-4">¿Qué desea rastrear hoy?</h2>
                        <p className="text-muted-foreground font-medium mb-10">Ingrese un número de nota de venta para visualizar todo el ecosistema de documentos asociados y su estado real.</p>
                        <div className="flex gap-4 justify-center">
                            <span className="px-4 py-2 bg-card rounded-xl text-[10px] font-black text-muted-foreground uppercase border border-border">Consultas Rápidas</span>
                            <span className="px-4 py-2 bg-card rounded-xl text-[10px] font-black text-muted-foreground uppercase border border-border">Historico OP</span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
