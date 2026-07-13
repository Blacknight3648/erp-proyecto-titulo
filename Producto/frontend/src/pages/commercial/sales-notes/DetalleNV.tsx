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
import { NotaVentaService } from '../../../remote/service/NotaVentaService';
import { OrdenProduccionService } from '../../../remote/service/OrdenProduccionService';
import { TrazabilidadService } from '../../../remote/service/TrazabilidadService';

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

    const [opsData, setOpsData] = useState([]);

    useEffect(() => {
        let cancelado = false;

        async function cargarTrazabilidad() {
            if (!nv?.idNV) {
                setOpsData([]);
                return;
            }
            try {
                const trace = await NotaVentaService.getTrazabilidad(nv.idNV);
                const opTraces = (trace || []).filter(t => t.tipoDocumento === 'Orden Producción');
                if (opTraces.length === 0) {
                    if (!cancelado) setOpsData([]);
                    return;
                }
                const resultados = await Promise.all(opTraces.map(async (opTrace) => {
                    const [avanceOP, trazaOPData] = await Promise.all([
                        OrdenProduccionService.getAvance(opTrace.id),
                        TrazabilidadService.obtenerPorOP(opTrace.id)
                    ]);
                    return { opId: opTrace.id, numeroOP: opTrace.numero, avance: avanceOP, trazaOP: trazaOPData };
                }));
                if (!cancelado) setOpsData(resultados);
            } catch (err) {
                console.error('Error cargando trazabilidad de la NV:', err);
                if (!cancelado) setOpsData([]);
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
            id: item.idItemNV || item.idItem || item.id || idx,
            garment: `${item.nombreProducto || item.articuloDescripcion || 'Prenda'}`.trim(),
            modelo: item.modelo || 'N/A',
            tela: item.tela || 'N/A',
            composicion: item.composicion || 'N/A',
            color: item.color || 'N/A',
            genero: item.genero || 'N/A',
            size: item.size || item.talla || 'M',
            quantity: item.cantidad || item.qty || 0,
            price: item.precioUnitario || 0,
            supplier: item.nombreProveedor || item.supplier || 'PENDIENTE',
            tipoItem: item.tipoItem || item.tipo || '-',
            llevaLogo: item.llevaLogo || 'NO',
            logoDetalle: item.logoDetalle || null,
            requiereOt: item.requiereOt || false,
            detalleOt: item.detalleOt || null,
            tallas: item.tallas || []
        })),
    };

    /**
     * Deriva de un elemento de opsData todo lo que necesita el render de esa OP
     * (avance de producción, flujo de vida, motivo de rechazo del Costeo/OCs).
     */
    function construirVistaOP({ avance, trazaOP }) {
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
        const motivoRechazoCosteo = trazaOP?.costeoVersion?.motivoRechazoCosteo || null;
        const fechaRechazoCosteo = trazaOP?.costeoVersion?.fechaRechazoCosteo || null;
        const ocsRechazadas = ordenesCompra.filter(oc => oc.estado === 'RECHAZADA' && oc.motivoRechazo);

        return {
            porcentajeGlobal,
            productionTracking,
            hojaCompra,
            ordenesCompra,
            estadoOP,
            motivoRechazoCosteo,
            fechaRechazoCosteo,
            ocsRechazadas
        };
    }

    return (
        <div className="max-w-6xl mx-auto space-y-8 animate-in slide-in-from-right-8 duration-700 p-6 pb-24">
            <button
                onClick={() => { setSelectedNV(null); setView('list'); }}
                className="flex items-center text-[10px] font-black uppercase text-muted-foreground hover:text-primary transition-colors tracking-[0.2em]"
            >
                <ArrowLeft className="w-4 h-4 mr-2" /> Volver a la Lista
            </button>

            <div className="flex flex-col md:flex-row justify-between items-start md:items-center bg-card p-8 rounded-[2.5rem] shadow-sm border border-border">
                <div>
                    <div className="flex items-center space-x-2 mb-2">
                        <span className="bg-primary/10 text-primary text-[10px] font-black px-3 py-1 rounded-full uppercase tracking-widest border border-primary/20">
                            Vista 360
                        </span>
                        <span className="text-muted-foreground/50 text-[10px]">•</span>
                        <span className="text-muted-foreground text-[10px] font-bold uppercase tracking-widest">Creado el {data.date}</span>
                    </div>
                    <h1 className="text-4xl font-black text-foreground tracking-tighter uppercase italic flex items-center">
                        <FileText className="w-10 h-10 mr-4 text-primary" />
                        Detalle <span className="text-primary ml-2 pr-1">{data.id}</span>
                    </h1>
                </div>
                <div className="mt-6 md:mt-0 flex items-center space-x-4">
                    <div className="text-right">
                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Monto Total (Neto)</p>
                        <p className="text-2xl font-black text-foreground tracking-tighter">${(data.total ?? 0).toLocaleString()}</p>
                    </div>
                    <div className={`p-4 rounded-2xl shadow-xl font-black text-xs uppercase tracking-widest ${data.status === 'Completado' || data.status === 'Entregado' ? 'bg-success shadow-success/20 text-white' : 'bg-primary shadow-primary/20 text-white animate-pulse'}`}>
                        {data.status}
                    </div>
                </div>
            </div>

            {opsData.length === 0 ? (
                <div className="bg-sidebar p-10 rounded-[3rem] shadow-2xl relative overflow-hidden">
                    <div className="absolute top-0 right-0 w-64 h-64 bg-primary/10 rounded-full -mr-32 -mt-32 blur-3xl"></div>
                    <h3 className="text-xs font-black text-sidebar-primary uppercase tracking-[0.2em] mb-10 flex items-center">
                        <TrendingUp className="w-4 h-4 mr-3" /> Flujo de Vida de la Orden
                    </h3>
                    <div className="flex flex-col md:flex-row items-center justify-between relative">
                        <div className="absolute top-1/2 left-0 w-full h-1 bg-sidebar-border -translate-y-1/2 hidden md:block"></div>
                        {[
                            { icon: FileText, label: 'Nota Venta', id: data.id, active: true },
                            { icon: ShoppingCart, label: 'Hoja de Compra', id: 'Pendiente', active: false },
                            { icon: CheckCircle2, label: 'Orden Compra', id: 'Pendiente', active: false },
                            { icon: Package, label: 'Producción', id: 'Pendiente', active: false },
                            { icon: Box, label: 'Bodega / Empaque', id: 'Revisado', active: data.status === 'Entregado' || data.status === 'Completado' },
                            { icon: Truck, label: 'Pedido Entregado', id: 'Despachado', active: data.status === 'Entregado' || data.status === 'Completado' }
                        ].map((step, idx) => (
                            <div key={idx} className="relative z-10 flex flex-col items-center group mb-8 md:mb-0">
                                <div className={`w-14 h-14 md:w-16 md:h-16 rounded-2xl flex items-center justify-center transition-all duration-500 ${step.active ? 'bg-primary shadow-primary/40 scale-110' : 'bg-white/10 group-hover:bg-white/20'}`}>
                                    <step.icon className={`w-6 h-6 md:w-7 md:h-7 ${step.active ? 'text-white' : 'text-white/40'}`} />
                                </div>
                                <div className="text-center mt-4">
                                    <p className="text-white font-black text-[9px] md:text-[10px] uppercase tracking-widest opacity-90">{step.label}</p>
                                    <p className={`text-[8px] md:text-[9px] font-bold uppercase tracking-tight mt-1 ${step.active ? 'text-sidebar-primary' : 'text-white/30'}`}>{step.id}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            ) : (
                opsData.map((opData) => {
                    const vista = construirVistaOP(opData);
                    return (
                        <div key={opData.opId} className="bg-sidebar p-10 rounded-[3rem] shadow-2xl relative overflow-hidden">
                            <div className="absolute top-0 right-0 w-64 h-64 bg-primary/10 rounded-full -mr-32 -mt-32 blur-3xl"></div>
                            <h3 className="text-xs font-black text-sidebar-primary uppercase tracking-[0.2em] mb-10 flex items-center">
                                <TrendingUp className="w-4 h-4 mr-3" /> Flujo de Vida — OP {opData.numeroOP}
                            </h3>
                            {vista.motivoRechazoCosteo && (
                                <div className="mb-8 flex items-start space-x-2 text-xs text-destructive-foreground bg-destructive/20 border border-destructive/40 rounded-2xl px-4 py-3">
                                    <AlertCircle className="w-4 h-4 mt-0.5 shrink-0" />
                                    <span>Costeo rechazado: {vista.motivoRechazoCosteo}{vista.fechaRechazoCosteo ? ` (${vista.fechaRechazoCosteo})` : ''}</span>
                                </div>
                            )}
                            {vista.ocsRechazadas.length > 0 && (
                                <div className="mb-8 space-y-2">
                                    {vista.ocsRechazadas.map(oc => (
                                        <div key={oc.idOC} className="flex items-start space-x-2 text-xs text-destructive-foreground bg-destructive/20 border border-destructive/40 rounded-2xl px-4 py-3">
                                            <AlertCircle className="w-4 h-4 mt-0.5 shrink-0" />
                                            <span>OC {oc.numeroOC} rechazada: {oc.motivoRechazo}{oc.fechaRechazo ? ` (${oc.fechaRechazo})` : ''}</span>
                                        </div>
                                    ))}
                                </div>
                            )}
                            <div className="flex flex-col md:flex-row items-center justify-between relative">
                                <div className="absolute top-1/2 left-0 w-full h-1 bg-sidebar-border -translate-y-1/2 hidden md:block"></div>
                                {[
                                    { icon: FileText, label: 'Nota Venta', id: data.id, active: true },
                                    { icon: ShoppingCart, label: 'Hoja de Compra', id: vista.hojaCompra?.numeroHC || 'Pendiente', active: !!vista.hojaCompra },
                                    { icon: CheckCircle2, label: 'Orden Compra', id: vista.ordenesCompra.length > 0 ? `${vista.ordenesCompra.length} OC` : 'Pendiente', active: vista.ordenesCompra.length > 0 },
                                    { icon: Package, label: 'Producción', id: vista.estadoOP || 'Pendiente', active: vista.estadoOP === 'EN_PROCESO' || vista.estadoOP === 'COMPLETADA' },
                                    { icon: Box, label: 'Bodega / Empaque', id: 'Revisado', active: data.status === 'Entregado' || data.status === 'Completado' },
                                    { icon: Truck, label: 'Pedido Entregado', id: 'Despachado', active: data.status === 'Entregado' || data.status === 'Completado' }
                                ].map((step, idx) => (
                                    <div key={idx} className="relative z-10 flex flex-col items-center group mb-8 md:mb-0">
                                        <div className={`w-14 h-14 md:w-16 md:h-16 rounded-2xl flex items-center justify-center transition-all duration-500 ${step.active ? 'bg-primary shadow-primary/40 scale-110' : 'bg-white/10 group-hover:bg-white/20'}`}>
                                            <step.icon className={`w-6 h-6 md:w-7 md:h-7 ${step.active ? 'text-white' : 'text-white/40'}`} />
                                        </div>
                                        <div className="text-center mt-4">
                                            <p className="text-white font-black text-[9px] md:text-[10px] uppercase tracking-widest opacity-90">{step.label}</p>
                                            <p className={`text-[8px] md:text-[9px] font-bold uppercase tracking-tight mt-1 ${step.active ? 'text-sidebar-primary' : 'text-white/30'}`}>{step.id}</p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    );
                })
            )}

            <div className="bg-card p-10 rounded-[3.5rem] shadow-sm border border-border group hover:border-brand-indigo/30 transition-all">
                <div className="flex justify-between items-center mb-10">
                    <div className="flex items-center space-x-4">
                        <div className="w-12 h-12 bg-brand-indigo/10 rounded-2xl flex items-center justify-center shadow-sm shadow-brand-indigo/10">
                            <ShoppingCart className="w-6 h-6 text-brand-indigo" />
                        </div>
                        <div>
                            <h3 className="text-sm font-black text-foreground uppercase tracking-[0.2em] leading-none mb-2">Abastecimiento Relacionado</h3>
                            <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest">Documento origen y gestión de compras</p>
                        </div>
                    </div>
                </div>

                <div className="mb-10 overflow-hidden border border-border rounded-[2rem]">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-brand-indigo/5">
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest">Prenda / Tipo</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest text-center">Origen</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest">Especificaciones</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest text-center">Talla</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest text-center">Género</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest text-center">Cant.</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest text-center">Precio U.</th>
                                <th className="p-5 text-[9px] font-black text-brand-indigo uppercase tracking-widest">Proveedor</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data.items.map((item, idx) => (
                                <tr key={idx} className="border-t border-border hover:bg-muted/50 transition-colors">
                                    <td className="p-5">
                                        <div className="flex items-center space-x-3">
                                            <div className="w-8 h-8 bg-brand-indigo/10 rounded-lg flex items-center justify-center">
                                                <Package className="w-4 h-4 text-brand-indigo" />
                                            </div>
                                            <span className="text-[11px] font-black text-foreground uppercase">{item.garment}</span>
                                        </div>
                                    </td>
                                    <td className="p-5 text-center">
                                        <span className={`text-[10px] font-black uppercase tracking-widest px-3 py-1.5 rounded-lg border ${item.tipoItem === 'OP' ? 'bg-primary/10 text-primary border-primary/20' : item.tipoItem === 'SC' ? 'bg-warning/10 text-warning border-warning/20' : item.tipoItem === 'SCI' ? 'bg-success/10 text-success border-success/20' : 'bg-muted text-muted-foreground border-border'}`}>
                                            {item.tipoItem}
                                        </span>
                                    </td>
                                    <td className="p-5">
                                        <div className="flex flex-col space-y-2 items-start">
                                            <div className="grid grid-cols-[min-content_1fr] gap-x-3 gap-y-1 text-[10px] uppercase">
                                                <span className="font-black text-muted-foreground text-right">Modelo:</span>
                                                <span className="font-bold text-foreground">{item.modelo}</span>
                                                <span className="font-black text-muted-foreground text-right">Tela:</span>
                                                <span className="font-bold text-foreground">{item.tela}</span>
                                                <span className="font-black text-muted-foreground text-right">Comp:</span>
                                                <span className="font-bold text-foreground">{item.composicion}</span>
                                                <span className="font-black text-muted-foreground text-right">Color:</span>
                                                <span className="font-bold text-foreground">{item.color}</span>
                                            </div>

                                            {(item.llevaLogo !== 'NO' || item.requiereOt) && (
                                                <div className="flex flex-wrap gap-2 mt-1">
                                                    {item.llevaLogo !== 'NO' && (
                                                        <div className="flex flex-col max-w-[150px]">
                                                            <span className="text-[9px] font-black text-brand-indigo bg-brand-indigo/10 px-2 py-0.5 rounded border border-brand-indigo/20 uppercase">Logo: {item.llevaLogo}</span>
                                                            {item.logoDetalle && <span className="text-[8px] text-muted-foreground mt-0.5 leading-tight truncate" title={item.logoDetalle}>{item.logoDetalle}</span>}
                                                        </div>
                                                    )}
                                                    {item.requiereOt && (
                                                        <div className="flex flex-col max-w-[150px]">
                                                            <span className="text-[9px] font-black text-warning bg-warning/10 px-2 py-0.5 rounded border border-warning/20 uppercase">Req. OT</span>
                                                            {item.detalleOt && <span className="text-[8px] text-muted-foreground mt-0.5 leading-tight truncate" title={item.detalleOt}>{item.detalleOt}</span>}
                                                        </div>
                                                    )}
                                                </div>
                                            )}
                                        </div>
                                    </td>
                                    <td className="p-5 text-center">
                                        <div className="flex flex-wrap gap-2 justify-center max-w-[120px] mx-auto">
                                            {item.tallas && item.tallas.length > 0 ? (
                                                item.tallas.map((t, i) => (
                                                    <span key={i} className="text-[10px] font-black text-foreground bg-muted px-2 py-1 rounded-md whitespace-nowrap">
                                                        {t.talla}: <span className="text-brand-indigo">{t.cantidad}</span>
                                                    </span>
                                                ))
                                            ) : (
                                                <span className="text-[11px] font-black text-foreground bg-muted px-3 py-1 rounded-lg">
                                                    {item.size}
                                                </span>
                                            )}
                                        </div>
                                    </td>
                                    <td className="p-5 text-center">
                                        <span className="text-[10px] font-bold text-muted-foreground uppercase">{item.genero}</span>
                                    </td>
                                    <td className="p-5 text-center text-[11px] font-black text-brand-indigo">{item.quantity}</td>
                                    <td className="p-5 text-center">
                                        <span className="text-[11px] font-bold text-foreground">${Number(item.price).toLocaleString()}</span>
                                    </td>
                                    <td className="p-5">
                                        <span className="text-[10px] font-black text-muted-foreground uppercase">{item.supplier}</span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {opsData.length === 0 ? (
                <div className="bg-card p-10 rounded-[3.5rem] shadow-sm border border-border flex flex-col">
                    <div className="flex items-center space-x-4 mb-12">
                        <div className="w-12 h-12 bg-warning/10 rounded-2xl flex items-center justify-center shadow-sm shadow-warning/10">
                            <ClipboardList className="w-6 h-6 text-warning" />
                        </div>
                        <div>
                            <h3 className="text-sm font-black text-foreground uppercase tracking-[0.2em] leading-none mb-2">Avance de Producción</h3>
                            <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest">Trazabilidad detallada por etapa</p>
                        </div>
                    </div>
                    <div className="text-center py-10 bg-muted/50 rounded-[2rem] border border-dashed border-border">
                        <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">Producción aún no iniciada</span>
                    </div>
                </div>
            ) : (
                opsData.map((opData) => {
                    const vista = construirVistaOP(opData);
                    return (
                        <div key={opData.opId} className="bg-card p-10 rounded-[3.5rem] shadow-sm border border-border flex flex-col">
                            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-12">
                                <div className="flex items-center space-x-4">
                                    <div className="w-12 h-12 bg-warning/10 rounded-2xl flex items-center justify-center shadow-sm shadow-warning/10">
                                        <ClipboardList className="w-6 h-6 text-warning" />
                                    </div>
                                    <div>
                                        <h3 className="text-sm font-black text-foreground uppercase tracking-[0.2em] leading-none mb-2">Avance de Producción — OP {opData.numeroOP}</h3>
                                        <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest">Trazabilidad detallada por etapa</p>
                                    </div>
                                </div>

                                <div className="flex-1 max-w-xl bg-muted/80 p-6 rounded-[2rem] border border-border/50">
                                    <div className="flex justify-between items-center mb-3">
                                        <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Progreso Global</span>
                                        <span className="text-sm font-black text-primary">{Math.round(vista.porcentajeGlobal)}%</span>
                                    </div>
                                    <div className="w-full h-3 bg-muted rounded-full overflow-hidden shadow-inner">
                                        <div
                                            className="h-full bg-gradient-to-r from-primary to-brand-indigo transition-all duration-1000"
                                            style={{ width: `${Math.round(vista.porcentajeGlobal)}%` }}
                                        ></div>
                                    </div>
                                </div>
                            </div>

                            {vista.productionTracking.length === 0 ? (
                                <div className="text-center py-10 bg-muted/50 rounded-[2rem] border border-dashed border-border">
                                    <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">Producción aún no iniciada</span>
                                </div>
                            ) : (
                                <div className="relative pt-8 pb-12">
                                    <div className="absolute top-[4.5rem] left-0 w-full h-1 bg-border rounded-full -z-0"></div>
                                    <div className="flex justify-between relative z-10 text-center">
                                        {vista.productionTracking.map((stage, idx) => (
                                            <div key={idx} className="flex flex-col items-center flex-1">
                                                <div className={`w-14 h-14 rounded-2xl border-4 border-card shadow-xl flex items-center justify-center mb-4 ${stage.status === 'Completado' ? 'bg-success text-white' : stage.status === 'En Proceso' ? 'bg-primary text-white animate-pulse' : 'bg-muted-foreground/30 text-white'}`}>
                                                    {stage.status === 'Completado' ? <CheckCircle2 className="w-7 h-7" /> : <span className="text-lg font-black">{idx + 1}</span>}
                                                </div>
                                                <h4 className="text-[11px] font-black uppercase text-foreground mb-1">{stage.stage}</h4>
                                                <span className={`text-[9px] font-black uppercase ${stage.status === 'Completado' ? 'text-success' : stage.status === 'En Proceso' ? 'text-primary' : 'text-muted-foreground'}`}>{stage.status}</span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    );
                })
            )}
        </div>
    );
}
