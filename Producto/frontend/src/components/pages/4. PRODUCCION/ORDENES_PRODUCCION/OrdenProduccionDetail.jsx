import { useMemo } from 'react';
import { 
    ChevronLeft, 
    User, 
    Calendar, 
    Tag, 
    DollarSign, 
    ClipboardList, 
    Layers, 
    Palette, 
    Scissors,
    ShieldCheck,
    CheckCircle2,
    Info
} from 'lucide-react';
import { 
    mockOperaciones, 
    mockNVs, 
    mockEvaluacionesNegocio, 
    mockSolicitudesCostos 
} from '../../../../data/mockData';

export default function OrdenProduccionDetail({ opId, onBack }) {
    // 1. Obtener datos base de la OP
    const op = useMemo(() => mockOperaciones.find(o => o.idOP === opId), [opId]);
    
    // 2. Obtener datos vinculados
    const nv = useMemo(() => mockNVs.find(n => n.idOP === opId || n.numeroNV === op?.notaVentaId), [op]);
    const evn = useMemo(() => mockEvaluacionesNegocio.find(e => e.clienteNombre === op?.cliente || e.articulo === op?.producto), [op]);
    const costeo = useMemo(() => mockSolicitudesCostos.find(c => c.idSolicitud === op?.idOP || c.articuloDescripcion === op?.producto), [op]);

    if (!op) return <div className="p-10 text-center text-red-500 font-black">OP NO ENCONTRADA</div>;

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-20">
            {/* Cabecera de Detalle */}
            <div className="flex items-center gap-6">
                <button
                    onClick={onBack}
                    className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-blue-600 active:scale-95"
                >
                    <ChevronLeft className="w-5 h-5" />
                </button>
                <div className="flex-1">
                    <div className="flex items-center gap-3">
                        <h2 className="text-2xl font-black text-gray-800 tracking-tight italic uppercase">Detalle OP: {opId}</h2>
                        <span className="px-3 py-1 bg-blue-50 text-blue-600 text-[10px] font-black rounded-full border border-blue-100 uppercase tracking-widest">
                            {op.estado}
                        </span>
                    </div>
                    <p className="text-[10px] text-gray-500 font-black uppercase tracking-widest mt-1">
                        Vinculada a NV: {op.notaVentaId} • Cliente: {op.cliente}
                    </p>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Columna Izquierda: Información General */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Tarjeta de Información Comercial Integrada */}
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-50 bg-gray-50/50">
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                                <Info className="w-4 h-4 text-blue-600" />
                                Información General del Negocio
                            </h3>
                        </div>
                        <div className="p-8 grid grid-cols-1 md:grid-cols-2 gap-8">
                            <div className="space-y-6">
                                <InfoItem icon={User} label="Cliente" value={nv?.cliente || op.cliente} />
                                <InfoItem icon={User} label="Vendedor" value={nv?.vendedor || "No asignado"} />
                                <InfoItem icon={Calendar} label="Fecha Entrega NV" value={nv?.fechaEmision || "Sin fecha"} />
                                <InfoItem icon={Tag} label="Artículo" value={op.producto} />
                            </div>
                            <div className="space-y-6">
                                <InfoItem 
                                    icon={DollarSign} 
                                    label="Precio Venta (EVN)" 
                                    value={evn ? `$${evn.costoFinalAcordado?.toLocaleString()}` : "Sin dato EVN"} 
                                    color="text-green-600"
                                />
                                <InfoItem 
                                    icon={DollarSign} 
                                    label="Costo Estimado (Costeo)" 
                                    value={`$${(costeo?.telas?.reduce((acc, t) => acc + (t.costoTotal || 0), 0) || 0).toLocaleString()}`} 
                                    color="text-blue-600"
                                />
                                <InfoItem icon={CheckCircle2} label="Requiere Muestra" value={costeo?.esMuestra ? "SÍ" : "NO"} />
                                <InfoItem icon={Layers} label="Cantidad Total" value={costeo?.cantidad || "Por definir"} />
                            </div>
                        </div>
                    </div>

                    {/* Tallaje Detallado (desde NV) */}
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-50">
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                                <Scissors className="w-4 h-4 text-indigo-600" />
                                Tallaje Detallado (Nota de Venta)
                            </h3>
                        </div>
                        <div className="overflow-x-auto">
                            <table className="w-full text-left">
                                <thead className="bg-gray-50 border-b border-gray-100">
                                    <tr>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest">Modelo / Item</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Talla</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Color</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cantidad</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-50">
                                    {nv?.items?.map((item, idx) => (
                                        <tr key={idx} className="hover:bg-gray-50 transition-colors">
                                            <td className="px-8 py-4 text-xs font-black text-gray-700 uppercase">{item.nombreProducto}</td>
                                            <td className="px-8 py-4 text-center text-xs font-black text-gray-900 italic">{item.talla}</td>
                                            <td className="px-8 py-4 text-center text-xs font-bold text-gray-500 uppercase">{item.color}</td>
                                            <td className="px-8 py-4 text-center text-xs font-black text-blue-600">{item.cantidad}</td>
                                        </tr>
                                    ))}
                                    {(!nv?.items || nv.items.length === 0) && (
                                        <tr>
                                            <td colSpan="4" className="py-8 text-center text-gray-300 font-bold italic uppercase text-[10px]">No hay tallaje registrado en la NV</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    {/* Ficha Técnica: Telas y Accesorios (desde Costeo) */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                        <FichaTecnicaCard title="Detalle Telas" items={costeo?.telas} icon={Layers} />
                        <FichaTecnicaCard title="Detalle Accesorios" items={costeo?.accesorios} icon={Palette} />
                    </div>
                </div>

                {/* Columna Derecha: Detalles Técnicos y Logotipos */}
                <div className="space-y-8">
                    {/* Logotipos */}
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-50 bg-amber-50/30">
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                                <ShieldCheck className="w-4 h-4 text-amber-600" />
                                Detalle Logotipos
                            </h3>
                        </div>
                        <div className="p-6 space-y-4">
                            {costeo?.logotipo?.map((logo, idx) => (
                                <div key={idx} className="bg-gray-50 p-4 rounded-3xl border border-gray-100 flex items-start gap-3">
                                    <div className="bg-white p-2 rounded-xl shadow-sm">
                                        <Palette className="w-4 h-4 text-amber-500" />
                                    </div>
                                    <div>
                                        <p className="text-[11px] font-black text-gray-800 uppercase tracking-tight">{logo.descripcion}</p>
                                        <p className="text-[9px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Ubicación: {logo.ubicacion || "Por definir"}</p>
                                        <div className="mt-2 flex items-center gap-2">
                                            <span className="text-[9px] font-black text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full uppercase tracking-tighter">Coste: ${logo.precioUnitario || 0}</span>
                                        </div>
                                    </div>
                                </div>
                            ))}
                            {(!costeo?.logotipo || costeo.logotipo.length === 0) && (
                                <p className="text-center py-6 text-gray-300 font-bold italic text-[10px] uppercase">Sin logotipos registrados</p>
                            )}
                        </div>
                    </div>

                    {/* Plantilla y Otros Insumos */}
                    <div className="bg-slate-900 text-white rounded-[2.5rem] p-8 shadow-xl shadow-slate-200">
                         <h3 className="text-xs font-black text-blue-400 uppercase tracking-widest mb-6 flex items-center gap-2">
                            <ClipboardList className="w-4 h-4" />
                            Especificaciones Plantilla
                        </h3>
                        <div className="space-y-6">
                            <div className="border-l-2 border-blue-500/30 pl-4 py-1">
                                <p className="text-[9px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Nombre Plantilla</p>
                                <p className="text-sm font-bold italic">{costeo?.articuloDescripcion || "Carga desde Ficha..."}</p>
                            </div>
                            <div className="border-l-2 border-slate-700 pl-4 py-1">
                                <p className="text-[9px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Observaciones Técnicas</p>
                                <p className="text-xs text-gray-300 leading-relaxed italic line-clamp-4">
                                    {op.obsTaller || "Sin observaciones específicas de taller central..."}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

function InfoItem({ icon: Icon, label, value, color = "text-gray-900" }) {
    return (
        <div className="flex items-center gap-3 group transition-all">
            <div className="bg-gray-50 p-3 rounded-2xl group-hover:bg-blue-50 transition-colors">
                <Icon className="w-4 h-4 text-gray-400 group-hover:text-blue-500" />
            </div>
            <div>
                <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest">{label}</p>
                <p className={`text-sm font-black tracking-tight uppercase ${color}`}>{value}</p>
            </div>
        </div>
    );
}

function FichaTecnicaCard({ title, items, icon: Icon }) {
    return (
        <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
            <div className="p-6 border-b border-gray-50 flex items-center justify-between">
                <h3 className="text-[11px] font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                    <Icon className="w-3.5 h-3.5 text-blue-500" />
                    {title}
                </h3>
                <span className="text-[9px] font-black bg-blue-50 text-blue-600 px-2 py-0.5 rounded-full border border-blue-100">
                    {items?.length || 0} Ítems
                </span>
            </div>
            <div className="divide-y divide-gray-50">
                {items?.map((item, idx) => (
                    <div key={idx} className="p-4 hover:bg-gray-50/50 transition-colors">
                        <p className="text-[11px] font-black text-gray-700 uppercase tracking-tight">{item.descripcion}</p>
                        <div className="flex justify-between items-center mt-1">
                            <span className="text-[9px] font-bold text-gray-400 uppercase">{item.nombreProveedor}</span>
                            <span className="text-[10px] font-black text-blue-500 italic">{item.consumo} {item.unidadMedida}</span>
                        </div>
                    </div>
                ))}
                {(!items || items.length === 0) && (
                    <p className="p-8 text-center text-gray-300 font-bold italic text-[10px] uppercase tracking-widest">Sin registros</p>
                )}
            </div>
        </div>
    );
}
