import React, { useEffect, useState } from 'react';
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
    CardDescription
} from "../../../../ui/card";
import { Badge } from "../../../../ui/badge";
import { Button } from "../../../../ui/button";
import { Input } from "../../../../ui/input";
import { Textarea } from "../../../../ui/textarea";
import { Checkbox } from "../../../../ui/checkbox";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
} from "../../../../ui/table";
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
    PackageCheck
} from 'lucide-react';
import { useProveedores } from '../../../../../hooks/useProveedores';

const STATUS_BADGE = {
    BORRADOR: 'bg-amber-50 text-amber-600 border-amber-100',
    APROBADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:  'bg-slate-100 text-slate-500 border-slate-200',
};

export default function HCModificacion({ hc, onBack, onConsolidarLote, formatCLP }) {
    const { proveedores, loading: loadingProveedores } = useProveedores();
    const [proveedorId, setProveedorId] = useState('');
    const [fechaEntrega, setFechaEntrega] = useState('');
    const [observaciones, setObservaciones] = useState('');
    const [selectedIds, setSelectedIds] = useState(() => new Set());
    const [draftGroups, setDraftGroups] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [localError, setLocalError] = useState(null);
    const [successMsg, setSuccessMsg] = useState(null);

    if (!hc) return (
        <div className="p-10 text-center bg-white/50 backdrop-blur-md rounded-3xl border border-dashed border-slate-200">
            <p className="text-slate-400 font-bold uppercase tracking-widest text-xs">Cargando datos de la hoja de compra...</p>
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
        .reduce((acc, i) => acc + (parseFloat(i.cantidadRequerida || 0) * parseFloat(i.precioEstimado || 0)), 0);

    const puedeAgregarGrupo = hc.status === 'APROBADA' && proveedorId && selectedIds.size > 0 && !submitting;
    const puedeGenerarLote = hc.status === 'APROBADA' && draftGroups.length > 0 && !submitting;

    const handleAgregarGrupo = () => {
        if (!puedeAgregarGrupo) return;
        const proveedor = proveedores.find(p => String(p.proveedorId) === String(proveedorId));
        const itemsGrupo = items
            .filter(i => selectedIds.has(i.id))
            .map(i => ({ id: i.id, insumo: i.insumo, cantidadRequerida: i.cantidadRequerida, precioEstimado: i.precioEstimado }));

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
                        className="rounded-2xl h-12 w-12 bg-white/50 backdrop-blur-md border border-white hover:bg-white hover:rotate-[-10deg] transition-all shadow-xl shadow-slate-200/50"
                    >
                        <ChevronLeft className="w-6 h-6 text-slate-600" />
                    </Button>
                    <div>
                        <div className="flex items-center gap-3">
                            <h2 className="text-4xl font-black text-slate-900 tracking-tighter uppercase">Gestión de Compra · HC #{hc.id}</h2>
                            <Badge className={`px-4 h-7 rounded-full font-black text-[9px] uppercase tracking-widest border-2 ${STATUS_BADGE[hc.status] || ''}`}>
                                {hc.status || '—'}
                            </Badge>
                        </div>
                        <p className="text-[10px] text-slate-400 font-black uppercase tracking-[0.2em] mt-2 italic">
                            {hc.numero || 'Sin número asignado'} · OP #{hc.idOP ?? '—'}
                        </p>
                    </div>
                </div>
            </div>

            {hc.status !== 'APROBADA' && (
                <div className="bg-amber-50 border border-amber-200 text-amber-700 text-xs font-bold uppercase tracking-widest p-6 rounded-2xl flex items-center gap-3">
                    <AlertCircle className="w-5 h-5 shrink-0" />
                    Solo las Hojas de Compra en estado APROBADA pueden consolidarse en una Orden de Compra.
                </div>
            )}

            {localError && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {localError}
                </div>
            )}
            {successMsg && (
                <div className="bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl flex items-center gap-3">
                    <CheckCircle2 className="w-5 h-5 shrink-0" />
                    {successMsg}
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Content */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Items disponibles */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 overflow-hidden border">
                        <CardHeader className="p-10 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-4">
                                <div className="bg-emerald-50 p-2 rounded-xl">
                                    <ShoppingCart className="w-5 h-5 text-emerald-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-widest">Insumos Disponibles para Compra</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">Selecciona los insumos y asigna un proveedor para agregarlos a la tanda</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-0">
                            <Table>
                                <TableHeader className="bg-slate-50/50">
                                    <TableRow className="border-slate-100 hover:bg-transparent">
                                        <TableHead className="pl-10 w-[60px]"></TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 py-5">Insumo</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400">Tipo</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-center">Cant. Requerida</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-right pr-10">Precio Ref.</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {itemsDisponibles.map((item) => (
                                        <TableRow key={item.id} className="border-slate-50 hover:bg-white transition-all group">
                                            <TableCell className="pl-10 py-6">
                                                <Checkbox
                                                    checked={selectedIds.has(item.id)}
                                                    onCheckedChange={() => toggleItem(item.id)}
                                                    disabled={hc.status !== 'APROBADA'}
                                                />
                                            </TableCell>
                                            <TableCell className="py-6">
                                                <span className="font-bold text-slate-700 text-xs uppercase tracking-tight group-hover:text-indigo-600 transition-colors">{item.insumo || '—'}</span>
                                            </TableCell>
                                            <TableCell>
                                                <Badge variant="outline" className="rounded-lg font-black text-[8px] uppercase tracking-widest border-slate-100 bg-white/50 text-slate-500 py-1.5">
                                                    <Tag className="w-2.5 h-2.5 mr-1" />
                                                    {item.tipoInsumo || '—'}
                                                </Badge>
                                            </TableCell>
                                            <TableCell className="text-center font-black text-xs text-slate-800 tabular-nums">
                                                {item.cantidadRequerida ?? '—'}
                                            </TableCell>
                                            <TableCell className="text-right pr-10 font-black text-xs text-indigo-600 tabular-nums">
                                                {formatCLP ? formatCLP(item.precioEstimado || 0) : (item.precioEstimado ?? '—')}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                            {itemsDisponibles.length === 0 && (
                                <div className="py-16 text-center space-y-4">
                                    <div className="bg-slate-50 p-6 rounded-full inline-block">
                                        <ShoppingCart className="w-10 h-10 text-slate-200" />
                                    </div>
                                    <p className="text-[10px] font-black text-slate-300 uppercase tracking-widest italic">
                                        Todos los insumos de esta HC ya están vinculados a una Orden de Compra
                                    </p>
                                </div>
                            )}
                        </CardContent>
                    </Card>

                    {/* Items ya vinculados a OC */}
                    {itemsAsignados.length > 0 && (
                        <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 overflow-hidden border">
                            <CardHeader className="p-10 pb-4 border-b border-slate-50">
                                <div className="flex items-center gap-4">
                                    <div className="bg-indigo-50 p-2 rounded-xl">
                                        <Truck className="w-5 h-5 text-indigo-600" />
                                    </div>
                                    <div>
                                        <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-widest">Insumos ya Vinculados a OC</CardTitle>
                                        <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">Estos insumos ya forman parte de una Orden de Compra emitida</CardDescription>
                                    </div>
                                </div>
                            </CardHeader>
                            <CardContent className="p-0">
                                <Table>
                                    <TableHeader className="bg-slate-50/50">
                                        <TableRow className="border-slate-100 hover:bg-transparent">
                                            <TableHead className="pl-10 font-black text-[9px] uppercase tracking-widest text-slate-400 py-5">Insumo</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-center">Cant. Requerida</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400">Proveedor</TableHead>
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-right pr-10">Orden de Compra</TableHead>
                                        </TableRow>
                                    </TableHeader>
                                    <TableBody>
                                        {itemsAsignados.map((item) => (
                                            <TableRow key={item.id} className="border-slate-50 hover:bg-white transition-all">
                                                <TableCell className="pl-10 py-6 font-bold text-slate-700 text-xs uppercase tracking-tight">
                                                    {item.insumo || '—'}
                                                </TableCell>
                                                <TableCell className="text-center font-black text-xs text-slate-800 tabular-nums">
                                                    {item.cantidadRequerida ?? '—'}
                                                </TableCell>
                                                <TableCell className="font-bold text-[10px] uppercase tracking-widest text-slate-500">
                                                    {item.proveedorNombre || '—'}
                                                </TableCell>
                                                <TableCell className="text-right pr-10">
                                                    <Badge className="px-3 h-7 rounded-full font-black text-[9px] uppercase tracking-widest border-2 bg-indigo-50 text-indigo-600 border-indigo-100">
                                                        {item.numeroOC || `OC #${item.ocId}`}
                                                    </Badge>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </CardContent>
                        </Card>
                    )}
                </div>

                {/* Sidebar: Form de armado + tanda de OC */}
                <div className="space-y-8">
                    <Card className="rounded-[2.5rem] bg-slate-900 border-none shadow-2xl p-4 overflow-hidden">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em] mb-1 flex items-center gap-2">
                                <Building2 className="w-4 h-4 text-indigo-400" />
                                Agregar Grupo a la Tanda
                            </CardTitle>
                            <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-500">
                                Asigna los insumos seleccionados a un proveedor
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-6">
                            <div className="space-y-3">
                                <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">Proveedor</label>
                                <select
                                    value={proveedorId}
                                    onChange={(e) => setProveedorId(e.target.value)}
                                    disabled={hc.status !== 'APROBADA' || loadingProveedores}
                                    className="w-full h-12 bg-slate-800 border border-slate-700 rounded-2xl px-4 font-bold text-xs text-white focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:opacity-50"
                                >
                                    <option value="">{loadingProveedores ? 'Cargando proveedores...' : 'Selecciona un proveedor'}</option>
                                    {proveedores.map((p) => (
                                        <option key={p.proveedorId} value={p.proveedorId}>{p.nombreProveedor}</option>
                                    ))}
                                </select>
                            </div>

                            <div className="space-y-3">
                                <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">Fecha Entrega Estimada</label>
                                <Input
                                    type="date"
                                    value={fechaEntrega}
                                    onChange={(e) => setFechaEntrega(e.target.value)}
                                    disabled={hc.status !== 'APROBADA'}
                                    className="h-12 bg-slate-800 border-slate-700 rounded-2xl px-4 font-bold text-xs text-white focus-visible:ring-indigo-500"
                                />
                            </div>

                            <div className="space-y-3">
                                <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">Observaciones</label>
                                <Textarea
                                    value={observaciones}
                                    onChange={(e) => setObservaciones(e.target.value)}
                                    disabled={hc.status !== 'APROBADA'}
                                    placeholder="Notas para el proveedor..."
                                    className="bg-slate-800 border-slate-700 rounded-2xl px-4 py-3 font-bold text-xs text-white min-h-[80px]"
                                />
                            </div>

                            <div className="pt-6 border-t border-slate-800 space-y-4">
                                <div className="flex items-center justify-between">
                                    <span className="text-[10px] font-black text-slate-500 uppercase tracking-widest">Items seleccionados</span>
                                    <span className="text-sm font-black text-white tabular-nums">{selectedIds.size}</span>
                                </div>
                                <div className="flex items-center justify-between">
                                    <span className="text-[10px] font-black text-slate-500 uppercase tracking-widest">Total estimado</span>
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
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 overflow-hidden border">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-slate-800 uppercase tracking-[0.25em] mb-1 flex items-center gap-2">
                                <PackageCheck className="w-4 h-4 text-indigo-500" />
                                Tanda de Órdenes de Compra ({draftGroups.length})
                            </CardTitle>
                            <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400">
                                Se generarán todas juntas, en una sola transacción
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-4">
                            {draftGroups.length === 0 ? (
                                <p className="text-[10px] font-bold text-slate-300 uppercase tracking-widest italic text-center py-6">
                                    Aún no hay grupos en la tanda
                                </p>
                            ) : (
                                <div className="space-y-3">
                                    {draftGroups.map((g) => (
                                        <div key={g.id} className="bg-slate-50 border border-slate-100 rounded-2xl p-4 space-y-2">
                                            <div className="flex items-start justify-between gap-2">
                                                <div>
                                                    <p className="text-xs font-black text-slate-800 uppercase tracking-tight">{g.proveedorNombre}</p>
                                                    <p className="text-[9px] font-bold text-slate-400 uppercase tracking-widest mt-0.5">{g.items.length} insumo{g.items.length === 1 ? '' : 's'}{g.fechaEntrega ? ` · Entrega ${g.fechaEntrega}` : ''}</p>
                                                </div>
                                                <button
                                                    onClick={() => handleQuitarGrupo(g.id)}
                                                    disabled={submitting}
                                                    className="shrink-0 p-1.5 rounded-lg text-slate-400 hover:text-red-600 hover:bg-red-50 transition-all disabled:opacity-40"
                                                    title="Quitar grupo de la tanda"
                                                >
                                                    <X className="w-3.5 h-3.5" />
                                                </button>
                                            </div>
                                            <p className="text-sm font-black text-indigo-600 tabular-nums">
                                                {formatCLP ? formatCLP(g.totalEstimado) : g.totalEstimado}
                                            </p>
                                        </div>
                                    ))}
                                </div>
                            )}

                            <Button
                                onClick={handleGenerarLote}
                                disabled={!puedeGenerarLote}
                                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl h-14 font-black text-xs uppercase tracking-widest shadow-2xl shadow-indigo-500/20 transition-all active:scale-95 flex items-center justify-center gap-2 disabled:opacity-40 disabled:cursor-not-allowed"
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
        </motion.div>
    );
}
