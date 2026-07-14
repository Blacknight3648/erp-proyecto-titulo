import { useState, useEffect } from 'react';
import {
    Search,
    Link as LinkIcon,
    AlertCircle,
    Clock,
    MoreHorizontal,
    Loader2,
    X,
    FileText,
    Settings,
    Layers,
    Factory,
    Wrench,
    ClipboardList,
    ShoppingBag,
    ShoppingCart,
    CheckCircle2,
    ArrowRight
} from 'lucide-react';
import { NotaVentaService } from '../../remote/service/NotaVentaService';

const ESTADOS_FINALES = ['FINALIZADA', 'COMPLETO', 'COMPLETADA', 'ENTREGADA', 'APROBADA', 'RECEPCIONADA', 'ADJUDICADA', 'CERRADA'];
const ESTADOS_RECHAZO = ['RECHAZADA', 'RECHAZADO', 'CANCELADA'];

/**
 * Resuelve el estado agregado de una NV a partir de sus Ordenes de Produccion.
 * Prioridad: si alguna OP esta DETENIDA o CANCELADA, se muestra esa (requiere
 * atencion). Si todas estan COMPLETADA, se muestra COMPLETADA. En cualquier
 * otro caso, se muestra la primera OP en curso (EN_PROCESO/PENDIENTE).
 */
function resolverEstadoAgregado(ordenesProduccion, estadoNV) {
    if (!ordenesProduccion || ordenesProduccion.length === 0) {
        return estadoNV;
    }

    const conAtencion = ordenesProduccion.find(op => op.estado === 'DETENIDA' || op.estado === 'CANCELADA');
    if (conAtencion) return conAtencion.estado;

    const todasCompletadas = ordenesProduccion.every(op => op.estado === 'COMPLETADA');
    if (todasCompletadas) return 'COMPLETADA';

    const enProceso = ordenesProduccion.find(op => op.estado === 'EN_PROCESO');
    if (enProceso) return 'EN_PROCESO';

    return ordenesProduccion[0].estado;
}

function estiloEstado(estado) {
    if (estado === 'COMPLETADA' || estado === 'ENTREGADA' || estado === 'APROBADA' || estado === 'RECEPCIONADA' || estado === 'ADJUDICADA') {
        return 'bg-success/10 text-success border-success/20';
    }
    if (estado === 'CANCELADA' || estado === 'DETENIDA' || estado === 'RECHAZADA' || estado === 'RECHAZADO') {
        return 'bg-destructive/10 text-destructive border-destructive/20';
    }
    if (estado === 'EN_PROCESO' || estado === 'EN_PRODUCCION' || estado === 'ENVIADA' || estado === 'RECEPCIONADA_PARCIAL') {
        return 'bg-primary/10 text-primary border-primary/20';
    }
    return 'bg-warning/10 text-warning border-warning/20';
}

/**
 * A partir de la traza plana que devuelve el backend para UNA NV (SCOS, Costeo,
 * EVN, NV, OP(s), OT(s)) arma un modelo anidado: cada OP lleva su propia Hoja
 * de Compra y sus Ordenes de Compra adentro (reutilizando lo que ya trae el
 * resumen, sin llamadas extra), en vez de una lista plana.
 */
function construirRamaNV(traceBase, nvResumenRow) {
    const nvDoc = (traceBase || []).find(d => d.tipoDocumento === 'Nota Venta') || {
        id: nvResumenRow.idNV, numero: nvResumenRow.numeroNV, estado: nvResumenRow.estado,
        fecha: nvResumenRow.fechaEmision, motivoRechazo: nvResumenRow.motivoRechazo, fechaRechazo: nvResumenRow.fechaRechazo,
    };
    const ops = (traceBase || [])
        .filter(d => d.tipoDocumento === 'Orden Producción')
        .map(opDoc => {
            const opFull = (nvResumenRow.ordenesProduccion || []).find(o => String(o.idOP) === String(opDoc.id));
            return { ...opDoc, hojaCompra: opFull?.hojaCompra || null, ordenesCompra: opFull?.ordenesCompra || [] };
        });
    const ot = (traceBase || []).filter(d => d.tipoDocumento === 'Orden Trabajo (OT)');
    return { nv: nvDoc, ops, ot };
}

/** Tarjeta de una sola etapa (EVN, NV, SCOS, Costeo, OP, HC u OC). */
function NodoEtapa({ icono: Icono, tipo, numero, estado, fecha, motivoRechazo, fechaRechazo }) {
    const esFinal = ESTADOS_FINALES.includes(estado);
    const esRechazo = ESTADOS_RECHAZO.includes(estado);
    return (
        <div className={`min-w-[168px] rounded-2xl border p-4 shrink-0 ${esRechazo ? 'bg-destructive/5 border-destructive/20' : 'bg-muted/40 border-border'}`}>
            <div className="flex items-center gap-2 mb-1.5">
                <div className="p-1.5 bg-card rounded-lg border border-border">
                    <Icono className="w-3.5 h-3.5 text-foreground" />
                </div>
                <p className="text-[8px] font-black uppercase tracking-widest text-muted-foreground">{tipo}</p>
            </div>
            <p className="text-xs font-black text-foreground mb-1.5">{numero}</p>
            <div className="flex items-center gap-1.5 flex-wrap">
                <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase border ${estiloEstado(estado)}`}>{estado}</span>
                {esFinal && !esRechazo && <CheckCircle2 className="w-3 h-3 text-success" />}
            </div>
            {fecha && <p className="text-[9px] text-muted-foreground font-bold mt-1.5">{fecha}</p>}
            {motivoRechazo && (
                <div className="mt-2 flex items-start gap-1 text-[9px] text-destructive">
                    <AlertCircle className="w-3 h-3 mt-0.5 shrink-0" />
                    <span>{motivoRechazo}{fechaRechazo ? ` (${fechaRechazo})` : ''}</span>
                </div>
            )}
        </div>
    );
}

/** Flecha conectora entre dos etapas horizontales. */
function Conector() {
    return <ArrowRight className="w-4 h-4 text-muted-foreground/50 mt-8 shrink-0" />;
}

/**
 * Columna de una etapa: si hay un solo item, la tarjeta va sola; si hay más
 * de uno, se despliega siempre visible como una rama vertical debajo (no
 * colapsable), cada uno con su propia tarjeta.
 */
function ColumnaEtapa({ items, render }) {
    if (!items || items.length === 0) return null;
    if (items.length === 1) return render(items[0], 0);
    return (
        <div className="border-l-2 border-primary/30 pl-3 space-y-2">
            {items.map((item, i) => <div key={i}>{render(item, i)}</div>)}
        </div>
    );
}

/** Rama de una OP: OP → Hoja de Compra → Ordenes de Compra (vertical si hay más de una). */
function RamaOP({ op }) {
    return (
        <div className="flex items-start gap-3">
            <NodoEtapa icono={Factory} tipo="Orden Producción" numero={op.numero} estado={op.estado} fecha={op.fecha} motivoRechazo={op.motivoRechazo} fechaRechazo={op.fechaRechazo} />
            {op.hojaCompra && (
                <>
                    <Conector />
                    <NodoEtapa icono={ShoppingBag} tipo="Hoja Compra" numero={op.hojaCompra.numeroHC} estado={op.hojaCompra.estado} />
                    {op.ordenesCompra.length > 0 && (
                        <>
                            <Conector />
                            <ColumnaEtapa
                                items={op.ordenesCompra}
                                render={oc => (
                                    <NodoEtapa icono={ShoppingCart} tipo="Orden Compra" numero={oc.numeroOC} estado={oc.estado} motivoRechazo={oc.motivoRechazo} fechaRechazo={oc.fechaRechazo} />
                                )}
                            />
                        </>
                    )}
                </>
            )}
        </div>
    );
}

/** Rama de una NV: NV → (OPs, cada una con su propia sub-rama; y OTs aparte). */
function RamaNV({ branch }) {
    const tieneProduccion = branch.ops.length > 0 || branch.ot.length > 0;
    return (
        <div className="flex items-start gap-3 overflow-x-auto pb-2">
            <NodoEtapa icono={FileText} tipo="Nota Venta" numero={branch.nv.numero} estado={branch.nv.estado} fecha={branch.nv.fecha} motivoRechazo={branch.nv.motivoRechazo} fechaRechazo={branch.nv.fechaRechazo} />
            {tieneProduccion && <Conector />}
            <div className="space-y-3">
                {branch.ops.map((op, i) => <RamaOP key={i} op={op} />)}
                {branch.ot.length > 0 && (
                    <ColumnaEtapa
                        items={branch.ot}
                        render={ot => <NodoEtapa icono={Wrench} tipo="Orden Trabajo (OT)" numero={ot.numero} estado={ot.estado} />}
                    />
                )}
            </div>
        </div>
    );
}

export default function TrazabilidadNV() {
    const [searchQuery, setSearchQuery] = useState('');
    const [statusFilter, setStatusFilter] = useState('Todos');
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [detalleNV, setDetalleNV] = useState(null);
    const [detalleModel, setDetalleModel] = useState(null);
    const [detalleLoading, setDetalleLoading] = useState(false);
    const [detalleError, setDetalleError] = useState(null);

    useEffect(() => {
        let mounted = true;
        setLoading(true);
        NotaVentaService.getTrazabilidadResumen()
            .then(result => {
                if (mounted) setData(result);
            })
            .catch(() => {
                if (mounted) setError('No se pudo cargar la trazabilidad de Notas de Venta.');
            })
            .finally(() => {
                if (mounted) setLoading(false);
            });
        return () => { mounted = false; };
    }, []);

    const filteredData = data.filter(nv => {
        const matchesSearch =
            (nv.numeroNV || '').toString().toLowerCase().includes(searchQuery.toLowerCase()) ||
            (nv.clienteNombre || '').toString().toLowerCase().includes(searchQuery.toLowerCase());

        const estadoAgregado = resolverEstadoAgregado(nv.ordenesProduccion, nv.estado);
        const matchesFilter = statusFilter === 'Todos' || estadoAgregado === statusFilter;

        return matchesSearch && matchesFilter;
    });

    /**
     * Abre el detalle completo: si la EVN de esta NV generó más de una NV,
     * se traen y se muestran TODAS por separado (cada una con su propia
     * rama de OP → HC → OC), agrupadas bajo la misma EVN compartida.
     */
    async function abrirDetalle(nv) {
        setDetalleNV(nv);
        setDetalleModel(null);
        setDetalleError(null);
        setDetalleLoading(true);
        try {
            const hermanas = nv.evaluacionNegocioId
                ? data.filter(x => x.evaluacionNegocioId === nv.evaluacionNegocioId)
                : [nv];
            const traces = await Promise.all(hermanas.map(x => NotaVentaService.getTrazabilidad(x.idNV)));

            const nvBranches = hermanas.map((x, i) => construirRamaNV(traces[i], x));
            const primerTrace = traces.find(t => (t || []).length > 0) || [];
            const evnDoc = primerTrace.find(d => d.tipoDocumento === 'Evaluación Negocio') || null;
            const scos = primerTrace.filter(d => d.tipoDocumento === 'Solicitud Costos');
            const costeo = primerTrace.filter(d => d.tipoDocumento === 'Costeo');

            setDetalleModel({ evn: evnDoc, scos, costeo, nvBranches });
        } catch {
            setDetalleError('No se pudo cargar la trazabilidad completa de esta Nota de Venta.');
        } finally {
            setDetalleLoading(false);
        }
    }

    function cerrarDetalle() {
        setDetalleNV(null);
        setDetalleModel(null);
        setDetalleError(null);
    }

    return (
        <div className="max-w-7xl mx-auto space-y-8 animate-in fade-in duration-700 pb-12">

            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                    <h1 className="text-3xl font-black text-foreground tracking-tight flex items-center">
                        <LinkIcon className="w-8 h-8 mr-3 text-primary" />
                        Trazabilidad NV
                    </h1>
                    <p className="text-muted-foreground font-medium mt-2">
                        Vista unificada del ciclo de vida de cada orden (EVN → NV → OP → HC → OC)
                    </p>
                </div>

                {/* Search & Filter */}
                <div className="flex flex-col sm:flex-row gap-4">
                    <div className="relative group">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground group-focus-within:text-primary transition-colors" />
                        <input
                            type="text"
                            placeholder="Buscar por NV, Cliente..."
                            className="pl-12 pr-4 py-3 bg-card border border-border rounded-xl text-sm font-medium focus:ring-2 focus:ring-primary focus:border-transparent outline-none shadow-sm w-full sm:w-64"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <select
                        className="px-4 py-3 bg-card border border-border rounded-xl text-sm font-medium focus:ring-2 focus:ring-primary outline-none shadow-sm"
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    >
                        <option value="Todos">Todos los Estados</option>
                        <option value="PENDIENTE">Pendiente</option>
                        <option value="EN_PROCESO">En Proceso</option>
                        <option value="COMPLETADA">Completada</option>
                        <option value="DETENIDA">Detenida</option>
                        <option value="CANCELADA">Cancelada</option>
                    </select>
                </div>
            </div>

            {/* Traceability Table */}
            <div className="bg-card rounded-[2rem] shadow-sm border border-border overflow-hidden">
                {loading ? (
                    <div className="p-12 text-center">
                        <Loader2 className="w-8 h-8 text-primary animate-spin mx-auto mb-4" />
                        <h3 className="text-muted-foreground font-bold uppercase tracking-widest text-sm">Cargando trazabilidad...</h3>
                    </div>
                ) : error ? (
                    <div className="p-12 text-center">
                        <AlertCircle className="w-12 h-12 text-destructive/60 mx-auto mb-4" />
                        <h3 className="text-destructive font-bold uppercase tracking-widest text-sm">{error}</h3>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-muted border-b border-border text-xs font-black text-muted-foreground uppercase tracking-widest">
                                    <th className="p-6">Evaluación Negocio (EVN)</th>
                                    <th className="p-6">Nota Venta (NV)</th>
                                    <th className="p-6">Producción (OP → HC → OC)</th>
                                    <th className="p-6 text-center">Estado General</th>
                                    <th className="p-6"></th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-border text-sm">
                                {filteredData.map((nv) => {
                                    const estadoAgregado = resolverEstadoAgregado(nv.ordenesProduccion, nv.estado);
                                    return (
                                        <tr key={nv.idNV} className="group hover:bg-primary/5 transition-colors">
                                            {/* EVN Column */}
                                            <td className="p-6 align-top">
                                                {nv.numeroEVN ? (
                                                    <>
                                                        <div className={`inline-flex items-center space-x-2 border px-3 py-1.5 rounded-lg shadow-sm ${estiloEstado(nv.estadoEVN)}`}>
                                                            <span className="text-xs font-bold">{nv.numeroEVN}</span>
                                                        </div>
                                                        <div className="text-[10px] text-muted-foreground mt-1 uppercase font-bold">{nv.estadoEVN}</div>
                                                        {nv.motivoRechazoEVN && (
                                                            <div className="mt-2 flex items-start space-x-1 text-[10px] text-destructive bg-destructive/5 border border-destructive/20 rounded-lg px-2 py-1 max-w-[220px]">
                                                                <AlertCircle className="w-3 h-3 mt-0.5 shrink-0" />
                                                                <span>{nv.motivoRechazoEVN}{nv.fechaRechazoEVN ? ` (${nv.fechaRechazoEVN})` : ''}</span>
                                                            </div>
                                                        )}
                                                    </>
                                                ) : (
                                                    <span className="text-muted-foreground text-xs">-</span>
                                                )}
                                            </td>

                                            {/* NV Column */}
                                            <td className="p-6 align-top">
                                                <div className="font-bold text-foreground flex items-center space-x-2">
                                                    <span className="text-primary">{nv.numeroNV}</span>
                                                </div>
                                                <div className="text-xs text-muted-foreground mt-1 font-medium">{nv.clienteNombre}</div>
                                                <div className="text-[10px] text-muted-foreground mt-2 flex items-center">
                                                    <Clock className="w-3 h-3 mr-1" /> {nv.fechaEmision}
                                                </div>
                                                {nv.motivoRechazo && (
                                                    <div className="mt-2 flex items-start space-x-1 text-[10px] text-destructive bg-destructive/5 border border-destructive/20 rounded-lg px-2 py-1">
                                                        <AlertCircle className="w-3 h-3 mt-0.5 shrink-0" />
                                                        <span>{nv.motivoRechazo}{nv.fechaRechazo ? ` (${nv.fechaRechazo})` : ''}</span>
                                                    </div>
                                                )}
                                            </td>

                                            {/* OP → HC → OC Column */}
                                            <td className="p-6 align-top">
                                                {nv.ordenesProduccion && nv.ordenesProduccion.length > 0 ? (
                                                    <div className="space-y-3">
                                                        {nv.ordenesProduccion.map(op => (
                                                            <div key={op.idOP} className="space-y-1.5">
                                                                <div className={`inline-flex items-center space-x-2 border px-3 py-1.5 rounded-lg shadow-sm ${estiloEstado(op.estado)}`}>
                                                                    <div className={`w-2 h-2 rounded-full ${op.estado === 'COMPLETADA' ? 'bg-success' : op.estado === 'EN_PROCESO' ? 'bg-primary' : op.estado === 'CANCELADA' || op.estado === 'DETENIDA' ? 'bg-destructive' : 'bg-warning'}`}></div>
                                                                    <span className="text-xs font-bold">{op.numeroOP}</span>
                                                                </div>

                                                                {op.hojaCompra && (
                                                                    <div className="ml-4 flex items-center space-x-2 text-[10px] text-muted-foreground">
                                                                        <span className="font-bold uppercase">HC {op.hojaCompra.numeroHC}</span>
                                                                        <span className={`px-2 py-0.5 rounded-full border text-[9px] font-black uppercase ${estiloEstado(op.hojaCompra.estado)}`}>{op.hojaCompra.estado}</span>
                                                                    </div>
                                                                )}

                                                                {op.ordenesCompra && op.ordenesCompra.length > 0 && (
                                                                    <div className="ml-4 space-y-1">
                                                                        {op.ordenesCompra.map(oc => (
                                                                            <div key={oc.idOC} className="flex items-center space-x-2 text-[10px] text-muted-foreground">
                                                                                <span className="font-bold uppercase">OC {oc.numeroOC}</span>
                                                                                <span className={`px-2 py-0.5 rounded-full border text-[9px] font-black uppercase ${estiloEstado(oc.estado)}`}>{oc.estado}</span>
                                                                                {oc.estado === 'RECHAZADA' && oc.motivoRechazo && (
                                                                                    <span className="text-destructive" title={`${oc.motivoRechazo}${oc.fechaRechazo ? ` (${oc.fechaRechazo})` : ''}`}>
                                                                                        <AlertCircle className="w-3 h-3" />
                                                                                    </span>
                                                                                )}
                                                                            </div>
                                                                        ))}
                                                                    </div>
                                                                )}
                                                            </div>
                                                        ))}
                                                    </div>
                                                ) : (
                                                    <span className="text-muted-foreground text-xs">-</span>
                                                )}
                                            </td>

                                            {/* Status Column */}
                                            <td className="p-6 align-top text-center">
                                                <div className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-bold border ${estiloEstado(estadoAgregado)}`}>
                                                    {estadoAgregado}
                                                </div>
                                            </td>

                                            <td className="p-6 align-top text-right">
                                                <button
                                                    onClick={() => abrirDetalle(nv)}
                                                    title="Ver trazabilidad completa"
                                                    className="text-muted-foreground hover:text-primary transition-colors"
                                                >
                                                    <MoreHorizontal className="w-5 h-5" />
                                                </button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>

                        {filteredData.length === 0 && (
                            <div className="p-12 text-center">
                                <AlertCircle className="w-12 h-12 text-muted-foreground/40 mx-auto mb-4" />
                                <h3 className="text-muted-foreground font-bold uppercase tracking-widest text-sm">No se encontraron registros</h3>
                            </div>
                        )}
                    </div>
                )}
            </div>

            {detalleNV && (
                <div className="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-6" onClick={cerrarDetalle}>
                    <div
                        className="bg-card rounded-[2.5rem] shadow-2xl border border-border w-full max-w-6xl max-h-[85vh] overflow-y-auto p-10"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div className="flex items-start justify-between mb-10">
                            <div>
                                <h2 className="text-2xl font-black text-foreground tracking-tight italic flex items-center">
                                    <ArrowRight className="w-6 h-6 mr-3 text-primary" />
                                    Trazabilidad Completa — {detalleNV.numeroEVN || detalleNV.numeroNV}
                                </h2>
                                <p className="text-muted-foreground font-bold text-xs uppercase tracking-widest mt-2">
                                    {detalleNV.clienteNombre} · Secuencia completa del negocio
                                    {detalleModel?.nvBranches?.length > 1 && ` · ${detalleModel.nvBranches.length} Notas de Venta bajo esta EVN`}
                                </p>
                            </div>
                            <button onClick={cerrarDetalle} className="text-muted-foreground hover:text-primary transition-colors">
                                <X className="w-6 h-6" />
                            </button>
                        </div>

                        {detalleLoading ? (
                            <div className="py-16 text-center">
                                <Loader2 className="w-8 h-8 text-primary animate-spin mx-auto mb-4" />
                            </div>
                        ) : detalleError ? (
                            <div className="py-16 text-center">
                                <AlertCircle className="w-10 h-10 text-destructive/60 mx-auto mb-4" />
                                <p className="text-destructive text-sm font-bold uppercase tracking-widest">{detalleError}</p>
                            </div>
                        ) : !detalleModel || detalleModel.nvBranches.length === 0 ? (
                            <div className="py-16 text-center text-muted-foreground text-sm font-bold uppercase tracking-widest">
                                Sin documentos asociados
                            </div>
                        ) : (
                            <div className="flex items-start gap-3 overflow-x-auto pb-4">
                                <ColumnaEtapa
                                    items={detalleModel.scos}
                                    render={s => <NodoEtapa icono={ClipboardList} tipo="Solicitud Costos" numero={s.numero} estado={s.estado} fecha={s.fecha} motivoRechazo={s.motivoRechazo} fechaRechazo={s.fechaRechazo} />}
                                />
                                {detalleModel.scos.length > 0 && <Conector />}

                                <ColumnaEtapa
                                    items={detalleModel.costeo}
                                    render={c => <NodoEtapa icono={Settings} tipo="Costeo" numero={c.numero} estado={c.estado} fecha={c.fecha} motivoRechazo={c.motivoRechazo} fechaRechazo={c.fechaRechazo} />}
                                />
                                {detalleModel.costeo.length > 0 && <Conector />}

                                {detalleModel.evn && (
                                    <>
                                        <NodoEtapa icono={Layers} tipo="Evaluación Negocio" numero={detalleModel.evn.numero} estado={detalleModel.evn.estado} fecha={detalleModel.evn.fecha} motivoRechazo={detalleModel.evn.motivoRechazo} fechaRechazo={detalleModel.evn.fechaRechazo} />
                                        <Conector />
                                    </>
                                )}

                                {/* Rama vertical: una fila por cada NV que comparte esta EVN */}
                                <div className="border-l-2 border-primary/30 pl-3 space-y-5">
                                    {detalleModel.nvBranches.map((branch, i) => <RamaNV key={i} branch={branch} />)}
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
