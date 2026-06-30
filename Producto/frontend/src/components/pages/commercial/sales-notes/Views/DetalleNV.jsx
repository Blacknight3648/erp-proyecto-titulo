import React, { useEffect, useState } from 'react';
import {
    FileText,
    ShoppingCart,
    CheckCircle2,
    ArrowRight,
    Clock,
    User,
    Package,
    ExternalLink,
    TrendingUp,
    AlertCircle,
    ClipboardList,
    Layers,
    ArrowLeft,
    Calendar,
    ChevronDown,
    History,
    Truck,
    Box
} from 'lucide-react';
import { NotaVentaService } from '../../../../../remote/service/NotaVentaService';
import { OrdenProduccionService } from '../../../../../remote/service/OrdenProduccionService';
import { TrazabilidadService } from '../../../../../remote/service/TrazabilidadService';

const FASES_ORDEN = ['CORTE', 'CONFECCION', 'BORDADO', 'ESTAMPADO', 'TERMINACION'];
const FASE_LABEL = {
    CORTE: 'Corte',
    CONFECCION: 'Confección',
    BORDADO: 'Bordado',
    ESTAMPADO: 'Estampado',
    TERMINACION: 'Terminación'
};

export default function DetalleNV({
    selectedNV,
    setSelectedNV,
    registros,
    setView
}) {
    const nv = registros.find(r => String(r.idNV || r.id) === String(selectedNV) || String(r.numeroNV) === String(selectedNV)) || registros[0];

    const [avance, setAvance] = useState(null);
    const [trazaOP, setTrazaOP] = useState(null);

    useEffect(() => {
        let cancelado = false;

        async function cargarTrazabilidad() {
            if (!nv?.idNV) {
                setAvance(null);
                setTrazaOP(null);
                return;
            }
            try {
                const trace = await NotaVentaService.getTrazabilidad(nv.idNV);
                const opTrace = (trace || []).find(t => t.tipoDocumento === 'Orden Producción');
                if (!opTrace) {
                    if (!cancelado) {
                        setAvance(null);
                        setTrazaOP(null);
                    }
                    return;
                }
                const [avanceOP, trazaOPData] = await Promise.all([
                    OrdenProduccionService.getAvance(opTrace.id),
                    TrazabilidadService.obtenerPorOP(opTrace.id)
                ]);
                if (!cancelado) {
                    setAvance(avanceOP);
                    setTrazaOP(trazaOPData);
                }
            } catch (err) {
                console.error('Error cargando trazabilidad de la NV:', err);
                if (!cancelado) {
                    setAvance(null);
                    setTrazaOP(null);
                }
            }
        }

        cargarTrazabilidad();
        return () => { cancelado = true; };
    }, [nv?.idNV]);

    if (!nv) return <div>No se encontró la información de la NV</div>;

    const data = {
        id: nv.numeroNV || nv.idNV || nv.id,
        client: nv.nombreCliente || nv.cliente,
        date: nv.fechaEmision || nv.fecha,
        total: nv.montoTotal || nv.total,
        status: nv.estado || 'En Proceso',
        items: (nv.items || []).map((item, idx) => ({
            id: item.idItem || item.id || idx,
            garment: item.articuloDescripcion || item.garment || item.nombreProducto || 'Prenda',
            fabric: item.tela || 'Algodón 100%',
            color: item.color || 'N/A',
            size: item.size || item.talla || 'M',
            quantity: item.cantidad || item.qty || 0,
            price: item.precioUnitario || 0,
            supplier: item.nombreProveedor || item.supplier || 'PEDIENTE'
        })),
    };

    const ordenesTrabajo = avance?.ordenesTrabajo || [];
    const porcentajeGlobal = avance?.porcentajeGlobal ?? 0;

    const productionTracking = FASES_ORDEN
        .map(fase => ordenesTrabajo.filter(ot => ot.fase === fase))
        .filter(ots => ots.length > 0)
        .map(ots => {
            const fase = ots[0].fase;
            const totalUnidades = ots.reduce((acc, ot) => acc + (ot.cantidadTotal || 0), 0);
            const totalProcesado = ots.reduce((acc, ot) => acc + (ot.cantidadProducida || 0) + (ot.cantidadMerma || 0), 0);
            const progress = totalUnidades > 0 ? Math.round((totalProcesado / totalUnidades) * 100) : 0;
            return {
                stage: FASE_LABEL[fase] || fase,
                status: progress >= 100 ? 'Completado' : progress > 0 ? 'En Proceso' : 'Pendiente',
                progress
            };
        });

    const hojaCompra = trazaOP?.hojaCompra || null;
    const ordenesCompra = trazaOP?.ordenesCompra || [];
    const estadoOP = trazaOP?.estadoOP || null;

    return (
        <div className="max-w-6xl mx-auto space-y-8 animate-in slide-in-from-right-8 duration-700 p-6 pb-24">
            <button
                onClick={() => { setSelectedNV(null); setView('list'); }}
                className="flex items-center text-[10px] font-black uppercase text-gray-400 hover:text-blue-600 transition-colors tracking-[0.2em]"
            >
                <ArrowLeft className="w-4 h-4 mr-2" /> Volver a la Lista
            </button>

            <div className="flex flex-col md:flex-row justify-between items-start md:items-center bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100">
                <div>
                    <div className="flex items-center space-x-2 mb-2">
                        <span className="bg-blue-50 text-blue-600 text-[10px] font-black px-3 py-1 rounded-full uppercase tracking-widest border border-blue-100">
                            Vista 360
                        </span>
                        <span className="text-gray-300 text-[10px]">•</span>
                        <span className="text-gray-400 text-[10px] font-bold uppercase tracking-widest">Creado el {data.date}</span>
                    </div>
                    <h1 className="text-4xl font-black text-gray-800 tracking-tighter uppercase italic flex items-center">
                        <FileText className="w-10 h-10 mr-4 text-blue-600" />
                        Detalle <span className="text-blue-600 ml-2 pr-1">{data.id}</span>
                    </h1>
                </div>
                <div className="mt-6 md:mt-0 flex items-center space-x-4">
                    <div className="text-right">
                        <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Monto Total (Neto)</p>
                        <p className="text-2xl font-black text-gray-800 tracking-tighter">${(data.total ?? 0).toLocaleString()}</p>
                    </div>
                    <div className={`p-4 rounded-2xl shadow-xl font-black text-xs uppercase tracking-widest ${data.status === 'Completado' || data.status === 'Entregado' ? 'bg-green-500 shadow-green-100 text-white' : 'bg-blue-600 shadow-blue-100 text-white animate-pulse'}`}>
                        {data.status}
                    </div>
                </div>
            </div>

            <div className="bg-slate-900 p-10 rounded-[3rem] shadow-2xl relative overflow-hidden">
                <div className="absolute top-0 right-0 w-64 h-64 bg-blue-600/10 rounded-full -mr-32 -mt-32 blur-3xl"></div>
                <h3 className="text-xs font-black text-blue-400 uppercase tracking-[0.2em] mb-10 flex items-center">
                    <TrendingUp className="w-4 h-4 mr-3" /> Flujo de Vida de la Orden
                </h3>
                <div className="flex flex-col md:flex-row items-center justify-between relative">
                    <div className="absolute top-1/2 left-0 w-full h-1 bg-white/5 -translate-y-1/2 hidden md:block"></div>
                    {[
                        { icon: FileText, label: 'Nota Venta', id: data.id, active: true },
                        { icon: ShoppingCart, label: 'Hoja de Compra', id: hojaCompra?.numeroHC || 'Pendiente', active: !!hojaCompra },
                        { icon: CheckCircle2, label: 'Orden Compra', id: ordenesCompra.length > 0 ? `${ordenesCompra.length} OC` : 'Pendiente', active: ordenesCompra.length > 0 },
                        { icon: Package, label: 'Producción', id: estadoOP || 'Pendiente', active: estadoOP === 'EN_PROCESO' || estadoOP === 'COMPLETADA' },
                        { icon: Box, label: 'Bodega / Empaque', id: 'Revisado', active: data.status === 'Entregado' || data.status === 'Completado' },
                        { icon: Truck, label: 'Pedido Entregado', id: 'Despachado', active: data.status === 'Entregado' || data.status === 'Completado' }
                    ].map((step, idx) => (
                        <div key={idx} className="relative z-10 flex flex-col items-center group mb-8 md:mb-0">
                            <div className={`w-14 h-14 md:w-16 md:h-16 rounded-2xl flex items-center justify-center transition-all duration-500 ${step.active ? 'bg-blue-600 shadow-[0_0_30px_rgba(37,99,235,0.4)] scale-110' : 'bg-white/10 group-hover:bg-white/20'}`}>
                                <step.icon className={`w-6 h-6 md:w-7 md:h-7 ${step.active ? 'text-white' : 'text-white/40'}`} />
                            </div>
                            <div className="text-center mt-4">
                                <p className="text-white font-black text-[9px] md:text-[10px] uppercase tracking-widest opacity-90">{step.label}</p>
                                <p className={`text-[8px] md:text-[9px] font-bold uppercase tracking-tight mt-1 ${step.active ? 'text-blue-400' : 'text-white/30'}`}>{step.id}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="bg-white p-10 rounded-[3.5rem] shadow-sm border border-gray-100 group hover:border-indigo-100 transition-all">
                <div className="flex justify-between items-center mb-10">
                    <div className="flex items-center space-x-4">
                        <div className="w-12 h-12 bg-indigo-50 rounded-2xl flex items-center justify-center shadow-sm shadow-indigo-100">
                            <ShoppingCart className="w-6 h-6 text-indigo-600" />
                        </div>
                        <div>
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-[0.2em] leading-none mb-2">Abastecimiento Relacionado</h3>
                            <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Documento origen y gestión de compras</p>
                        </div>
                    </div>
                </div>

                <div className="mb-10 overflow-hidden border border-gray-100 rounded-[2rem]">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-indigo-50/50">
                                <th className="p-5 text-[9px] font-black text-indigo-600 uppercase tracking-widest">Prenda / Tipo</th>
                                <th className="p-5 text-[9px] font-black text-indigo-600 uppercase tracking-widest">Especificaciones</th>
                                <th className="p-5 text-[9px] font-black text-indigo-600 uppercase tracking-widest text-center">Talla</th>
                                <th className="p-5 text-[9px] font-black text-indigo-600 uppercase tracking-widest text-center">Cant.</th>
                                <th className="p-5 text-[9px] font-black text-indigo-600 uppercase tracking-widest">Proveedor</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data.items.map((item, idx) => (
                                <tr key={idx} className="border-t border-gray-50 hover:bg-gray-50/50 transition-colors">
                                    <td className="p-5">
                                        <div className="flex items-center space-x-3">
                                            <div className="w-8 h-8 bg-indigo-100 rounded-lg flex items-center justify-center">
                                                <Package className="w-4 h-4 text-indigo-600" />
                                            </div>
                                            <span className="text-[11px] font-black text-gray-800 uppercase">{item.garment}</span>
                                        </div>
                                    </td>
                                    <td className="p-5">
                                        <span className="text-[10px] font-bold text-gray-500 uppercase tracking-wide bg-white px-3 py-1 rounded-full border border-gray-100">{item.fabric} - {item.color}</span>
                                    </td>
                                    <td className="p-5 text-center">
                                        <span className="text-[11px] font-black text-gray-700 bg-gray-100 px-3 py-1 rounded-lg">{item.size}</span>
                                    </td>
                                    <td className="p-5 text-center text-[11px] font-black text-indigo-600">{item.quantity}</td>
                                    <td className="p-5">
                                        <span className="text-[10px] font-black text-gray-600 uppercase">{item.supplier}</span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="bg-white p-10 rounded-[3.5rem] shadow-sm border border-gray-100 flex flex-col">
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-12">
                    <div className="flex items-center space-x-4">
                        <div className="w-12 h-12 bg-amber-50 rounded-2xl flex items-center justify-center shadow-sm shadow-amber-100">
                            <ClipboardList className="w-6 h-6 text-amber-600" />
                        </div>
                        <div>
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-[0.2em] leading-none mb-2">Avance de Producción</h3>
                            <p className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Trazabilidad detallada por etapa</p>
                        </div>
                    </div>

                    <div className="flex-1 max-w-xl bg-gray-50/80 p-6 rounded-[2rem] border border-gray-100/50">
                        <div className="flex justify-between items-center mb-3">
                            <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Progreso Global</span>
                            <span className="text-sm font-black text-blue-600">{Math.round(porcentajeGlobal)}%</span>
                        </div>
                        <div className="w-full h-3 bg-gray-200 rounded-full overflow-hidden shadow-inner">
                            <div
                                className="h-full bg-gradient-to-r from-blue-500 to-indigo-600 transition-all duration-1000"
                                style={{ width: `${Math.round(porcentajeGlobal)}%` }}
                            ></div>
                        </div>
                    </div>
                </div>

                {productionTracking.length === 0 ? (
                    <div className="text-center py-10 bg-gray-50/50 rounded-[2rem] border border-dashed border-gray-100">
                        <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest italic">Producción aún no iniciada</span>
                    </div>
                ) : (
                    <div className="relative pt-8 pb-12">
                        <div className="absolute top-[4.5rem] left-0 w-full h-1 bg-gray-100 rounded-full -z-0"></div>
                        <div className="flex justify-between relative z-10 text-center">
                            {productionTracking.map((stage, idx) => (
                                <div key={idx} className="flex flex-col items-center flex-1">
                                    <div className={`w-14 h-14 rounded-2xl border-4 border-white shadow-xl flex items-center justify-center mb-4 ${stage.status === 'Completado' ? 'bg-green-500 text-white' : stage.status === 'En Proceso' ? 'bg-blue-600 text-white animate-pulse' : 'bg-gray-300 text-white'}`}>
                                        {stage.status === 'Completado' ? <CheckCircle2 className="w-7 h-7" /> : <span className="text-lg font-black">{idx + 1}</span>}
                                    </div>
                                    <h4 className="text-[11px] font-black uppercase text-gray-800 mb-1">{stage.stage}</h4>
                                    <span className={`text-[9px] font-black uppercase ${stage.status === 'Completado' ? 'text-green-600' : stage.status === 'En Proceso' ? 'text-blue-600' : 'text-gray-400'}`}>{stage.status}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
