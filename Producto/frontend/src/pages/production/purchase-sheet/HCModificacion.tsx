import React, { useEffect, useState } from 'react';
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
    CardDescription
} from "../../../ui/card";
import { Badge } from "../../../ui/badge";
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Textarea } from "../../../ui/textarea";
import { Checkbox } from "../../../ui/checkbox";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
} from "../../../ui/table";
import { motion } from "framer-motion";
import {
    ChevronLeft,
    ShoppingCart,
    CheckCircle2,
    AlertCircle,
    Tag,
    Truck,
    Building2,
    Loader2,
    Plus,
    X,
    PackageCheck,
    XCircle,
    RotateCcw
} from 'lucide-react';
import { useProveedores } from '../../../hooks/useProveedores';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../ui/select';
import ComboSearchField from '../../../components/shared/ComboSearchField';
import CustomSelectField from '../../../components/shared/CustomSelectField';
import ReingresoOCModal from '../../../components/shared/ReingresoOCModal';
import { OrdenCompraService } from '../../../remote/service/OrdenCompraService';
import { clampNonNegative } from '../../../utils/validations';

const STATUS_BADGE = {
    BORRADOR: 'bg-warning/10 text-warning border-warning/20',
    APROBADA: 'bg-success/10 text-success border-success/20',
    CERRADA:  'bg-muted text-muted-foreground border-border',
};

const TIPO_INSUMO_OPCIONES = [
    { value: 'TELA', label: 'Tela' },
    { value: 'ACCESORIO', label: 'Accesorio' },
];

const NUEVO_ITEM_VACIO = { tipoInsumo: 'ACCESORIO', articuloId: null, nombreInsumo: '', cantidadRequerida: '', precioUnitarioRef: '' };

export default function HCModificacion({ hc, onBack, onConsolidarLote, onAgregarItemManual, onRechazarOC, onReingresarOC, ocsById, formatCLP }) {
    const { proveedores, loading: loadingProveedores } = useProveedores();
    const [proveedorId, setProveedorId] = useState('');
    const [fechaEntrega, setFechaEntrega] = useState('');
    const [observaciones, setObservaciones] = useState('');
    const [selectedIds, setSelectedIds] = useState(() => new Set());
    const [draftGroups, setDraftGroups] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [localError, setLocalError] = useState(null);
    const [successMsg, setSuccessMsg] = useState(null);
    const [showAddInsumoForm, setShowAddInsumoForm] = useState(false);
    const [nuevoItem, setNuevoItem] = useState(NUEVO_ITEM_VACIO);
    const [addingInsumo, setAddingInsumo] = useState(false);
    const [rechazoTarget, setRechazoTarget] = useState(null);
    const [motivoRechazo, setMotivoRechazo] = useState('');
    const [rechazandoOC, setRechazandoOC] = useState(false);
    const [reingresandoOCId, setReingresandoOCId] = useState(null);
    const [reingresoOC, setReingresoOC] = useState(null);
    const [cargandoReingreso, setCargandoReingreso] = useState(false);
    const [reingresoError, setReingresoError] = useState(null);

    if (!hc) return (
        <div className="p-10 text-center bg-card/50 backdrop-blur-md rounded-3xl border border-dashed border-border">
            <p className="text-muted-foreground font-bold uppercase tracking-widest text-xs">Cargando datos de la hoja de compra...</p>
        </div>
    );

    const items = hc.items || [];
    const draftItemIds = new Set(draftGroups.flatMap(g => g.items.map(i => i.id)));
    const itemsDisponibles = items.filter(i => !i.ocId && !draftItemIds.has(i.id));
    const itemsAsignados = items.filter(i => !!i.ocId);

    const toggleItem = (id) => {
        setSelectedIds(prev => {
            const next = new Set(prev);
            if (next.has(id)) next.delete(id); else next.add(id);
            return next;
        });
    };

    const totalSeleccionado = items
        .filter(i => selectedIds.has(i.id))
        .reduce((acc, i) => {
            const cant = i.cantidadAComprar !== null && i.cantidadAComprar !== undefined ? i.cantidadAComprar : i.cantidadRequerida;
            return acc + (parseFloat(cant || 0) * parseFloat(i.precioEstimado || 0));
        }, 0);

    const puedeAgregarGrupo = hc.status === 'APROBADA' && proveedorId && selectedIds.size > 0 && !submitting;
    const puedeGenerarLote = hc.status === 'APROBADA' && draftGroups.length > 0 && !submitting;

    const handleAgregarGrupo = () => {
        if (!puedeAgregarGrupo) return;
        const proveedor = proveedores.find(p => String(p.proveedorId) === String(proveedorId));
        const itemsGrupo = items
            .filter(i => selectedIds.has(i.id))
            .map(i => {
                const cant = i.cantidadAComprar !== null && i.cantidadAComprar !== undefined ? i.cantidadAComprar : i.cantidadRequerida;
                return { id: i.id, insumo: i.insumo, cantidadRequerida: cant, precioEstimado: i.precioEstimado };
            });

        setDraftGroups(prev => [...prev, {
            id: `grupo-${Date.now()}`,
            proveedorId: Number(proveedorId),
            proveedorNombre: proveedor?.nombreProveedor || `Proveedor #${proveedorId}`,
            items: itemsGrupo,
            totalEstimado: itemsGrupo.reduce((acc, i) => acc + (parseFloat(i.cantidadRequerida || 0) * parseFloat(i.precioEstimado || 0)), 0),
            fechaEntrega,
            observaciones,
        }]);
        setSelectedIds(new Set());
        setProveedorId('');
        setFechaEntrega('');
        setObservaciones('');
        setLocalError(null);
    };

    const handleQuitarGrupo = (id) => {
        setDraftGroups(prev => prev.filter(g => g.id !== id));
    };

    const handleGenerarLote = async () => {
        if (!puedeGenerarLote) return;
        setSubmitting(true);
        setLocalError(null);
        setSuccessMsg(null);
        const cantidad = draftGroups.length;
        try {
            await onConsolidarLote(draftGroups.map(g => ({
                proveedorId: g.proveedorId,
                hcItemIds: g.items.map(i => i.id),
                fechaEntregaEstimada: g.fechaEntrega || undefined,
                observaciones: g.observaciones || undefined,
            })));
            setDraftGroups([]);
            setSuccessMsg(`${cantidad} Orden${cantidad === 1 ? '' : 'es'} de Compra generada${cantidad === 1 ? '' : 's'} correctamente.`);
        } catch (e) {
            setLocalError(e?.response?.data?.message || e?.message || 'Error generando las Órdenes de Compra');
        } finally {
            setSubmitting(false);
        }
    };

    const puedeAgregarInsumo = hc.status === 'APROBADA'
        && nuevoItem.nombreInsumo.trim().length > 0
        && Number(nuevoItem.cantidadRequerida) > 0
        && Number(nuevoItem.precioUnitarioRef) >= 0
        && !addingInsumo;

    const handleAgregarInsumo = async () => {
        if (!puedeAgregarInsumo) return;
        setAddingInsumo(true);
        setLocalError(null);
        try {
            await onAgregarItemManual(hc.id, {
                tipoInsumo: nuevoItem.tipoInsumo,
                articuloId: nuevoItem.articuloId,
                nombreInsumo: nuevoItem.nombreInsumo.trim().toUpperCase(),
                cantidadRequerida: Number(nuevoItem.cantidadRequerida),
                precioUnitarioRef: Number(nuevoItem.precioUnitarioRef),
            });
            setNuevoItem(NUEVO_ITEM_VACIO);
            setShowAddInsumoForm(false);
        } catch (e) {
            setLocalError(e?.response?.data?.message || e?.message || 'Error agregando el insumo no presupuestado');
        } finally {
            setAddingInsumo(false);
        }
    };

    const confirmarRechazoOC = async () => {
        if (!rechazoTarget || !motivoRechazo.trim()) return;
        setRechazandoOC(true);
        setLocalError(null);
        try {
            await onRechazarOC(rechazoTarget.ocId, motivoRechazo.trim());
            setSuccessMsg(`Orden de Compra ${rechazoTarget.numeroOC || ''} rechazada correctamente.`);
            setRechazoTarget(null);
            setMotivoRechazo('');
        } catch (e) {
            setLocalError(e?.response?.data?.message || e?.message || 'Error rechazando la Orden de Compra');
        } finally {
            setRechazandoOC(false);
        }
    };

    const handleAbrirReingreso = async (ocId) => {
        setLocalError(null);
        setReingresoError(null);
        setCargandoReingreso(true);
        setReingresandoOCId(ocId);
        try {
            const oc = await OrdenCompraService.getById(ocId);
            setReingresoOC(oc);
        } catch (e) {
            setLocalError(e?.response?.data?.message || e?.message || 'Error cargando la Orden de Compra');
        } finally {
            setCargandoReingreso(false);
            setReingresandoOCId(null);
        }
    };

    /**
     * Resuelve el estado "vivo" de un ítem ya vinculado a OC: proveedor, cantidad
     * comprada y precio unitario reales, leídos desde la OC actual (ocsById) en vez
     * de los campos snapshot del HCItem (que solo se escriben una vez al consolidar
     * y no se actualizan cuando la OC se reingresa con cambios).
     */
    const resolveVinculoOC = (item) => {
        const ocInfo = ocsById?.[item.ocId];
        const ocItem = ocInfo?.items?.find(oi => (oi.hcLinks || []).some(l => l.hcItemId === item.id));
        const proveedorNombre = proveedores.find(p => String(p.proveedorId) === String(ocInfo?.proveedorId))?.nombreProveedor
            || item.proveedorNombre;
        return { ocInfo, ocItem, proveedorNombre };
    };

    const confirmarReingreso = async (payload) => {
        setReingresandoOCId(reingresoOC.idOC);
        setReingresoError(null);
        try {
            await onReingresarOC(reingresoOC.idOC, payload.proveedorId, payload.itemsCambiados);
            setSuccessMsg(`Orden de Compra ${reingresoOC.numeroOC || ''} reingresada correctamente.`);
            setReingresoOC(null);
        } catch (e) {
            setReingresoError(e?.response?.data?.message || e?.message || 'Error reingresando la Orden de Compra');
        } finally {
            setReingresandoOCId(null);
        }
    };

    return (
        <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            className="space-y-8 pb-32"
        >
            {/* Nav & Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div className="flex items-center gap-6">
                    <Button
                        variant="ghost"
                        size="icon"
                        onClick={onBack}
                        className="rounded-2xl h-12 w-12 bg-card/50 backdrop-blur-md border border-border hover:bg-card hover:rotate-[-10deg] transition-all shadow-xl shadow-foreground/5"
                    >
                        <ChevronLeft className="w-6 h-6 text-muted-foreground" />
                    </Button>
                    <div>
                        <div className="flex items-center gap-3">
                            <h2 className="text-4xl font-black text-foreground tracking-tighter uppercase">Gestión de Compra · HC #{hc.id}</h2>
                            <Badge className={`px-4 h-7 rounded-full font-black text-[9px] uppercase tracking-widest border-2 ${STATUS_BADGE[hc.status] || ''}`}>
                                {hc.status || '—'}
                            </Badge>
                        </div>
                        <p className="text-[10px] text-muted-foreground font-black uppercase tracking-[0.2em] mt-2 italic">
                            {hc.numero || 'Sin número asignado'} · OP #{hc.idOP ?? '—'}
                        </p>
                    </div>
                </div>
            </div>

            {hc.status !== 'APROBADA' && (
                <div className="bg-warning/10 border border-warning/20 text-warning text-xs font-bold uppercase tracking-widest p-6 rounded-2xl flex items-center gap-3">
                    <AlertCircle className="w-5 h-5 shrink-0" />
                    Solo las Hojas de Compra en estado APROBADA pueden consolidarse en una Orden de Compra.
                </div>
            )}

            {localError && (
                <div className="bg-destructive/10 border border-destructive/20 text-destructive text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {localError}
                </div>
            )}
            {successMsg && (
                <div className="bg-success/10 border border-success/20 text-success text-xs font-bold uppercase tracking-widest p-4 rounded-2xl flex items-center gap-3">
                    <CheckCircle2 className="w-5 h-5 shrink-0" />
                    {successMsg}
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Content */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Items disponibles */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                        <CardHeader className="p-10 pb-4 border-b border-border">
                            <div className="flex items-center justify-between gap-4">
                                <div className="flex items-center gap-4">
                                    <div className="bg-success/10 p-2 rounded-xl">
                                        <ShoppingCart className="w-5 h-5 text-success" />
                                    </div>
                                    <div>
                                        <CardTitle className="text-sm font-black text-foreground uppercase tracking-widest">Insumos Disponibles para Compra</CardTitle>
                                        <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-muted-foreground mt-1">Selecciona los insumos y asigna un proveedor para agregarlos a la tanda</CardDescription>
                                    </div>
                                </div>
                                {hc.status === 'APROBADA' && (
                                    <Button
                                        variant="outline"
                                        onClick={() => setShowAddInsumoForm(s => !s)}
                                        className="rounded-xl h-10 px-4 text-[10px] font-black uppercase tracking-widest border-border text-muted-foreground hover:text-brand-indigo hover:border-brand-indigo/40 flex items-center gap-1.5 shrink-0"
                                    >
                                        <Plus className="w-3.5 h-3.5" /> Insumo no presupuestado
                                    </Button>
                                )}
                            </div>

                            {showAddInsumoForm && (
                                <div className="mt-6 p-5 bg-muted/60 border border-border rounded-2xl grid grid-cols-1 md:grid-cols-12 gap-3 items-end">
                                    <div className="md:col-span-2">
                                        <label className="text-[8px] font-black text-muted-foreground uppercase tracking-widest block mb-1.5">Tipo</label>
                                        <CustomSelectField
                                            value={nuevoItem.tipoInsumo}
                                            onChange={(val) => setNuevoItem(p => ({ ...p, tipoInsumo: val, articuloId: null, nombreInsumo: '' }))}
                                            options={TIPO_INSUMO_OPCIONES}
                                        />
                                    </div>
                                    <div className="md:col-span-4">
                                        <label className="text-[8px] font-black text-muted-foreground uppercase tracking-widest block mb-1.5">Insumo</label>
                                        <ComboSearchField
                                            tipo={nuevoItem.tipoInsumo}
                                            value={nuevoItem.nombreInsumo}
                                            onChange={(val) => setNuevoItem(p => ({ ...p, nombreInsumo: val }))}
                                            onSelectArticulo={(articulo) => setNuevoItem(p => ({ ...p, articuloId: articulo?.idArticulo ?? null }))}
                                            placeholder="Buscar o crear insumo..."
                                        />
                                    </div>
                                    <div className="md:col-span-2">
                                        <label className="text-[8px] font-black text-muted-foreground uppercase tracking-widest block mb-1.5">Cantidad</label>
                                        <Input type="number" min={0} value={nuevoItem.cantidadRequerida}
                                            onChange={(e) => setNuevoItem(p => ({ ...p, cantidadRequerida: String(clampNonNegative(e.target.value)) }))}
                                            className="h-10 bg-card border-border rounded-xl text-xs font-black text-right tabular-nums" />
                                    </div>
                                    <div className="md:col-span-2">
                                        <label className="text-[8px] font-black text-muted-foreground uppercase tracking-widest block mb-1.5">Precio Ref.</label>
                                        <Input type="number" min={0} value={nuevoItem.precioUnitarioRef}
                                            onChange={(e) => setNuevoItem(p => ({ ...p, precioUnitarioRef: String(clampNonNegative(e.target.value)) }))}
                                            className="h-10 bg-card border-border rounded-xl text-xs font-black text-right tabular-nums" />
                                    </div>
                                    <div className="md:col-span-2 flex gap-1.5">
                                        <Button onClick={handleAgregarInsumo} disabled={!puedeAgregarInsumo}
                                            className="flex-1 h-10 rounded-xl text-[10px] font-black uppercase tracking-widest bg-brand-indigo hover:bg-brand-indigo/90 text-white disabled:opacity-40">
                                            {addingInsumo ? <Loader2 className="w-3.5 h-3.5 animate-spin" /> : 'Agregar'}
                                        </Button>
                                        <Button variant="ghost" onClick={() => { setShowAddInsumoForm(false); setNuevoItem(NUEVO_ITEM_VACIO); }}
                                            className="h-10 w-10 p-0 rounded-xl text-muted-foreground hover:text-destructive">
                                            <X className="w-4 h-4" />
                                        </Button>
                                    </div>
                                </div>
                            )}
                        </CardHeader>
                        <CardContent className="p-0">
                            <Table>
                                <TableHeader className="bg-muted/50">
                                    <TableRow className="border-border hover:bg-transparent">
                                        <TableHead className="pl-10 w-[60px]"></TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground py-5">Insumo</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground">Tipo</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground">Proveedor Ref.</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Cant. a Comprar</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-right pr-10">Precio Ref.</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {itemsDisponibles.map((item) => {
                                        const cantComp = item.cantidadAComprar !== null && item.cantidadAComprar !== undefined ? item.cantidadAComprar : item.cantidadRequerida;
                                        const isMod = item.modificado || cantComp !== item.cantidadRequerida;
                                        return (
                                        <TableRow key={item.id} className="border-border hover:bg-card transition-all group">
                                            <TableCell className="pl-10 py-6">
                                                <Checkbox
                                                    checked={selectedIds.has(item.id)}
                                                    onCheckedChange={() => toggleItem(item.id)}
                                                    disabled={hc.status !== 'APROBADA'}
                                                />
                                            </TableCell>
                                            <TableCell className="py-6">
                                                <div className="flex items-center gap-2">
                                                    <span className="font-bold text-foreground text-xs uppercase tracking-tight group-hover:text-brand-indigo transition-colors">{item.insumo || '—'}</span>
                                                    {isMod && (
                                                        <Badge className="bg-warning text-white text-[8px] px-1.5 py-0">MOD</Badge>
                                                    )}
                                                    {item.presupuestado === false && (
                                                        <Badge className="bg-brand-indigo text-white text-[8px] px-1.5 py-0">NO PRESUP.</Badge>
                                                    )}
                                                </div>
                                            </TableCell>
                                            <TableCell>
                                                <Badge variant="outline" className="rounded-lg font-black text-[8px] uppercase tracking-widest border-border bg-card/50 text-muted-foreground py-1.5">
                                                    <Tag className="w-2.5 h-2.5 mr-1" />
                                                    {item.tipoInsumo || '—'}
                                                </Badge>
                                            </TableCell>
                                            <TableCell>
                                                {item.proveedorNombre ? (
                                                    <span className="inline-flex items-center gap-1.5 text-[10px] font-bold text-muted-foreground uppercase tracking-wide">
                                                        <Truck className="w-3 h-3 text-muted-foreground/60 shrink-0" />
                                                        {item.proveedorNombre}
                                                    </span>
                                                ) : (
                                                    <span className="text-[10px] font-bold text-muted-foreground/40 uppercase tracking-wide">—</span>
                                                )}
                                            </TableCell>
                                            <TableCell className="text-center font-black text-xs text-foreground tabular-nums">
                                                <span className={isMod ? "text-warning font-extrabold" : ""}>{cantComp ?? '—'}</span>
                                            </TableCell>
                                            <TableCell className="text-right pr-10 font-black text-xs text-brand-indigo tabular-nums">
                                                {formatCLP ? formatCLP(item.precioEstimado || 0) : (item.precioEstimado ?? '—')}
                                            </TableCell>
                                        </TableRow>
                                        );
                                    })}
                                </TableBody>
                            </Table>
                            {itemsDisponibles.length === 0 && (
                                <div className="py-16 text-center space-y-4">
                                    <div className="bg-muted p-6 rounded-full inline-block">
                                        <ShoppingCart className="w-10 h-10 text-muted-foreground/40" />
                                    </div>
                                    <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
                                        Todos los insumos de esta HC ya están vinculados a una Orden de Compra
                                    </p>
                                </div>
                            )}
                        </CardContent>
                    </Card>

                    {/* Items ya vinculados a OC */}
                    {itemsAsignados.length > 0 && (
                        <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                            <CardHeader className="p-10 pb-4 border-b border-border">
                                <div className="flex items-center gap-4">
                                    <div className="bg-brand-indigo/10 p-2 rounded-xl">
                                        <Truck className="w-5 h-5 text-brand-indigo" />
                                    </div>
                                    <div>
                                        <CardTitle className="text-sm font-black text-foreground uppercase tracking-widest">Insumos ya Vinculados a OC</CardTitle>
                                        <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-muted-foreground mt-1">Estos insumos ya forman parte de una Orden de Compra emitida</CardDescription>
                                    </div>
                                </div>
                            </CardHeader>
                            <CardContent className="p-0">
                                <Table>
                                    <TableHeader className="bg-muted/50">
                                        <TableRow className="border-border hover:bg-transparent">
                                            <TableHead className="pl-10 font-black text-[9px] uppercase tracking-widest text-muted-foreground py-5">Insumo</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Cant. Requerida</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Cant. Comprada</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-right">Valor de Venta</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground">Proveedor</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-right pr-10">Orden de Compra</TableHead>
                                        </TableRow>
                                    </TableHeader>
                                    <TableBody>
                                        {itemsAsignados.map((item) => {
                                            const { ocItem, proveedorNombre } = resolveVinculoOC(item);
                                            return (
                                            <TableRow key={item.id} className="border-border hover:bg-card transition-all">
                                                <TableCell className="pl-10 py-6 font-bold text-foreground text-xs uppercase tracking-tight">
                                                    <div className="flex items-center gap-2">
                                                        <span>{item.insumo || '—'}</span>
                                                        {item.presupuestado === false && (
                                                            <Badge className="bg-brand-indigo text-white text-[8px] px-1.5 py-0">NO PRESUP.</Badge>
                                                        )}
                                                    </div>
                                                </TableCell>
                                                <TableCell className="text-center font-black text-xs text-foreground tabular-nums">
                                                    {item.cantidadRequerida ?? '—'}
                                                </TableCell>
                                                <TableCell className="text-center font-black text-xs text-foreground tabular-nums">
                                                    {ocItem?.cantidadComprada ?? '—'}
                                                </TableCell>
                                                <TableCell className="text-right font-black text-xs text-brand-indigo tabular-nums">
                                                    {ocItem?.precioUnitario != null
                                                        ? (formatCLP ? formatCLP(ocItem.precioUnitario) : ocItem.precioUnitario)
                                                        : '—'}
                                                </TableCell>
                                                <TableCell className="font-bold text-[10px] uppercase tracking-widest text-muted-foreground">
                                                    {proveedorNombre || '—'}
                                                </TableCell>
                                                <TableCell className="text-right pr-10">
                                                    {(() => {
                                                        const ocInfo = ocsById?.[item.ocId];
                                                        const estadoOC = ocInfo?.estado;
                                                        return (
                                                            <div className="flex items-center justify-end gap-2">
                                                                <Badge className={`px-3 h-7 rounded-full font-black text-[9px] uppercase tracking-widest border-2 ${
                                                                    estadoOC === 'RECHAZADA'
                                                                        ? 'bg-destructive/10 text-destructive border-destructive/20'
                                                                        : 'bg-brand-indigo/10 text-brand-indigo border-brand-indigo/20'
                                                                }`}>
                                                                    {item.numeroOC || `OC #${item.ocId}`}
                                                                </Badge>
                                                                {estadoOC === 'EMITIDA' && (
                                                                    <button
                                                                        onClick={() => setRechazoTarget({ ocId: item.ocId, numeroOC: item.numeroOC })}
                                                                        title="Rechazar Orden de Compra"
                                                                        className="shrink-0 p-1.5 rounded-lg text-muted-foreground hover:text-destructive hover:bg-destructive/10 transition-all"
                                                                    >
                                                                        <XCircle className="w-4 h-4" />
                                                                    </button>
                                                                )}
                                                                {estadoOC === 'RECHAZADA' && (
                                                                    <button
                                                                        onClick={() => handleAbrirReingreso(item.ocId)}
                                                                        disabled={cargandoReingreso && reingresandoOCId === item.ocId}
                                                                        title="Reingresar Orden de Compra"
                                                                        className="shrink-0 p-1.5 rounded-lg text-muted-foreground hover:text-brand-indigo hover:bg-brand-indigo/10 transition-all disabled:opacity-40"
                                                                    >
                                                                        {cargandoReingreso && reingresandoOCId === item.ocId
                                                                            ? <Loader2 className="w-4 h-4 animate-spin" />
                                                                            : <RotateCcw className="w-4 h-4" />}
                                                                    </button>
                                                                )}
                                                            </div>
                                                        );
                                                    })()}
                                                </TableCell>
                                            </TableRow>
                                            );
                                        })}
                                    </TableBody>
                                </Table>
                            </CardContent>
                        </Card>
                    )}
                </div>

                {/* Sidebar: Form de armado + tanda de OC */}
                <div className="space-y-8">
                    <Card className="rounded-[2.5rem] bg-sidebar border-none shadow-2xl p-4 overflow-hidden">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-sidebar-muted uppercase tracking-[0.25em] mb-1 flex items-center gap-2">
                                <Building2 className="w-4 h-4 text-brand-indigo" />
                                Agregar Grupo a la Tanda
                            </CardTitle>
                            <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-sidebar-muted">
                                Asigna los insumos seleccionados a un proveedor
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-6">
                            <div className="space-y-3">
                                <label className="text-[9px] font-black text-sidebar-muted uppercase tracking-[0.2em]">Proveedor</label>
                                <Select
                                    value={proveedorId}
                                    onValueChange={setProveedorId}
                                    disabled={hc.status !== 'APROBADA' || loadingProveedores}
                                >
                                    <SelectTrigger className="w-full h-12 bg-sidebar-popup border border-sidebar-border rounded-2xl px-4 font-bold text-xs text-white focus:outline-none focus:ring-2 focus:ring-brand-indigo disabled:opacity-50">
                                        <SelectValue placeholder={loadingProveedores ? 'Cargando proveedores...' : 'Selecciona un proveedor'} />
                                    </SelectTrigger>
                                    <SelectContent>
                                        {proveedores.map((p) => (
                                            <SelectItem key={p.proveedorId} value={String(p.proveedorId)}>{p.nombreProveedor}</SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                            </div>

                            <div className="space-y-3">
                                <label className="text-[9px] font-black text-sidebar-muted uppercase tracking-[0.2em]">Fecha Entrega Estimada</label>
                                <Input
                                    type="date"
                                    value={fechaEntrega}
                                    onChange={(e) => setFechaEntrega(e.target.value)}
                                    disabled={hc.status !== 'APROBADA'}
                                    className="h-12 bg-sidebar-popup border-sidebar-border rounded-2xl px-4 font-bold text-xs text-white focus-visible:ring-brand-indigo"
                                />
                            </div>

                            <div className="space-y-3">
                                <label className="text-[9px] font-black text-sidebar-muted uppercase tracking-[0.2em]">Observaciones</label>
                                <Textarea
                                    value={observaciones}
                                    onChange={(e) => setObservaciones(e.target.value)}
                                    disabled={hc.status !== 'APROBADA'}
                                    placeholder="Notas para el proveedor..."
                                    className="bg-sidebar-popup border-sidebar-border rounded-2xl px-4 py-3 font-bold text-xs text-white min-h-[80px]"
                                />
                            </div>

                            <div className="pt-6 border-t border-sidebar-border space-y-4">
                                <div className="flex items-center justify-between">
                                    <span className="text-[10px] font-black text-sidebar-muted uppercase tracking-widest">Items seleccionados</span>
                                    <span className="text-sm font-black text-white tabular-nums">{selectedIds.size}</span>
                                </div>
                                <div className="flex items-center justify-between">
                                    <span className="text-[10px] font-black text-sidebar-muted uppercase tracking-widest">Total estimado</span>
                                    <span className="text-lg font-black text-white tracking-tighter tabular-nums">{formatCLP ? formatCLP(totalSeleccionado) : totalSeleccionado}</span>
                                </div>
                            </div>

                            <Button
                                onClick={handleAgregarGrupo}
                                disabled={!puedeAgregarGrupo}
                                className="w-full bg-white/10 hover:bg-white/20 text-white rounded-2xl h-14 font-black text-xs uppercase tracking-widest transition-all active:scale-95 flex items-center justify-center gap-2 disabled:opacity-40 disabled:cursor-not-allowed"
                            >
                                <Plus className="w-4 h-4" />
                                Agregar a la Tanda
                            </Button>
                        </CardContent>
                    </Card>

                    {/* Tanda de OC a generar */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-foreground uppercase tracking-[0.25em] mb-1 flex items-center gap-2">
                                <PackageCheck className="w-4 h-4 text-brand-indigo" />
                                Tanda de Órdenes de Compra ({draftGroups.length})
                            </CardTitle>
                            <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-muted-foreground">
                                Se generarán todas juntas, en una sola transacción
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-4">
                            {draftGroups.length === 0 ? (
                                <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest italic text-center py-6">
                                    Aún no hay grupos en la tanda
                                </p>
                            ) : (
                                <div className="space-y-3">
                                    {draftGroups.map((g) => (
                                        <div key={g.id} className="bg-muted border border-border rounded-2xl p-4 space-y-2">
                                            <div className="flex items-start justify-between gap-2">
                                                <div>
                                                    <p className="text-xs font-black text-foreground uppercase tracking-tight">{g.proveedorNombre}</p>
                                                    <p className="text-[9px] font-bold text-muted-foreground uppercase tracking-widest mt-0.5">{g.items.length} insumo{g.items.length === 1 ? '' : 's'}{g.fechaEntrega ? ` · Entrega ${g.fechaEntrega}` : ''}</p>
                                                </div>
                                                <button
                                                    onClick={() => handleQuitarGrupo(g.id)}
                                                    disabled={submitting}
                                                    className="shrink-0 p-1.5 rounded-lg text-muted-foreground hover:text-destructive hover:bg-destructive/10 transition-all disabled:opacity-40"
                                                    title="Quitar grupo de la tanda"
                                                >
                                                    <X className="w-3.5 h-3.5" />
                                                </button>
                                            </div>
                                            <p className="text-sm font-black text-brand-indigo tabular-nums">
                                                {formatCLP ? formatCLP(g.totalEstimado) : g.totalEstimado}
                                            </p>
                                        </div>
                                    ))}
                                </div>
                            )}

                            <Button
                                onClick={handleGenerarLote}
                                disabled={!puedeGenerarLote}
                                className="w-full bg-brand-indigo hover:bg-brand-indigo/90 text-white rounded-2xl h-14 font-black text-xs uppercase tracking-widest shadow-2xl shadow-brand-indigo/20 transition-all active:scale-95 flex items-center justify-center gap-2 disabled:opacity-40 disabled:cursor-not-allowed"
                            >
                                {submitting ? (
                                    <Loader2 className="w-4 h-4 animate-spin" />
                                ) : (
                                    <ShoppingCart className="w-4 h-4" />
                                )}
                                {submitting
                                    ? 'Generando...'
                                    : `Generar ${draftGroups.length} Orden${draftGroups.length === 1 ? '' : 'es'} de Compra`}
                            </Button>
                        </CardContent>
                    </Card>
                </div>
            </div>

            {rechazoTarget && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-sm p-4"
                    onClick={() => { setRechazoTarget(null); setMotivoRechazo(''); }}>
                    <div className="bg-white w-full max-w-md rounded-[2rem] p-6 shadow-2xl animate-in zoom-in-95 duration-200"
                        onClick={(e) => e.stopPropagation()}>
                        <div className="flex items-center gap-3 mb-4">
                            <div className="bg-rose-100 p-2 rounded-xl">
                                <XCircle className="w-5 h-5 text-rose-600" />
                            </div>
                            <div>
                                <h3 className="text-sm font-black text-slate-800 uppercase tracking-tight">Rechazar Orden de Compra</h3>
                                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">
                                    {rechazoTarget.numeroOC || `OC #${rechazoTarget.ocId}`}
                                </p>
                            </div>
                        </div>
                        <Textarea
                            rows={4}
                            value={motivoRechazo}
                            onChange={(e) => setMotivoRechazo(e.target.value)}
                            placeholder="Motivo del rechazo (obligatorio)..."
                            className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-2xl text-xs font-medium text-slate-700 outline-none focus:ring-2 focus:ring-rose-400 resize-none mb-4"
                        />
                        <div className="flex justify-end gap-2">
                            <button onClick={() => { setRechazoTarget(null); setMotivoRechazo(''); }}
                                className="px-4 h-10 rounded-xl text-[10px] font-black text-slate-500 hover:text-slate-700 uppercase tracking-widest">
                                Cancelar
                            </button>
                            <button onClick={confirmarRechazoOC} disabled={!motivoRechazo.trim() || rechazandoOC}
                                className="px-5 h-10 rounded-xl text-[10px] font-black text-white bg-rose-600 hover:bg-rose-700 uppercase tracking-widest flex items-center gap-1.5 disabled:opacity-50 disabled:cursor-not-allowed">
                                <XCircle className="w-3.5 h-3.5" /> {rechazandoOC ? 'Rechazando...' : 'Confirmar Rechazo'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {reingresoOC && (
                <ReingresoOCModal
                    oc={reingresoOC}
                    proveedores={proveedores}
                    submitting={reingresandoOCId === reingresoOC.idOC}
                    error={reingresoError}
                    onClose={() => { setReingresoOC(null); setReingresoError(null); }}
                    onConfirm={confirmarReingreso}
                />
            )}
        </motion.div>
    );
}
