import React, { useState } from 'react';
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
    CardDescription
} from "../../../ui/card";
import { Badge } from "../../../ui/badge";
import { Button } from "../../../ui/button";
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
    Calendar,
    Package,
    Info,
    CheckCircle2,
    Hash,
    Layers,
    MessageSquareQuote,
    ShoppingBag,
    ShoppingCart,
    Lock,
    Tag,
    GitBranch,
    Edit3,
    Box,
    AlertTriangle
} from 'lucide-react';
import { ModificarItemModal } from './ModificarItemModal';
import { JustificacionModal } from './JustificacionModal';

const STATUS_BADGE = {
    BORRADOR: 'bg-warning/10 text-warning border-warning/20',
    APROBADA: 'bg-success/10 text-success border-success/20',
    CERRADA:  'bg-muted text-muted-foreground border-border',
};

export default function HCVisualizacion({ hc, onBack, onAprobar, onCerrar, onModificar, formatCLP, modificarItemHC }) {
    const [itemToModify, setItemToModify] = useState<any>(null);
    const [itemToShowJustificacion, setItemToShowJustificacion] = useState<any>(null);

    if (!hc) return (
        <div className="p-10 text-center bg-card/50 backdrop-blur-md rounded-3xl border border-dashed border-border">
            <p className="text-muted-foreground font-bold uppercase tracking-widest text-xs">Cargando datos de la hoja de compra...</p>
        </div>
    );

    const totalEstimado = (hc.items || []).reduce(
        (acc, curr) => acc + (parseFloat(curr.cantidadRequerida || 0) * parseFloat(curr.precioEstimado || 0)),
        0
    );
    const totalUnidades = (hc.items || []).reduce(
        (acc, curr) => acc + parseFloat(curr.cantidadRequerida || 0),
        0
    );

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
                            <h2 className="text-4xl font-black text-foreground tracking-tighter uppercase">HC #{hc.id}</h2>
                            <Badge className={`px-4 h-7 rounded-full font-black text-[9px] uppercase tracking-widest border-2 ${STATUS_BADGE[hc.status] || ''}`}>
                                {hc.status || '—'}
                            </Badge>
                        </div>
                        <p className="text-[10px] text-muted-foreground font-black uppercase tracking-[0.2em] mt-2 italic">
                            {hc.numero || 'Sin número asignado'} · Generada {hc.fechaCreacion || '—'}
                        </p>
                    </div>
                </div>

                <div className="flex gap-3">
                    {hc.status === 'BORRADOR' && onAprobar && (
                        <Button
                            onClick={() => onAprobar(hc.id)}
                            className="bg-success hover:bg-success/90 text-white rounded-2xl px-8 h-12 font-black text-xs uppercase tracking-widest shadow-2xl shadow-success/20 transition-all active:scale-95 flex items-center gap-2"
                        >
                            <CheckCircle2 className="w-4 h-4" />
                            Aprobar HC
                        </Button>
                    )}
                    {hc.status === 'APROBADA' && onModificar && (
                        <Button
                            onClick={() => onModificar(hc.id)}
                            className="bg-brand-indigo hover:bg-brand-indigo/90 text-white rounded-2xl px-8 h-12 font-black text-xs uppercase tracking-widest shadow-2xl shadow-brand-indigo/20 transition-all active:scale-95 flex items-center gap-2"
                        >
                            <ShoppingCart className="w-4 h-4" />
                            Gestionar Compra
                        </Button>
                    )}
                    {hc.status === 'APROBADA' && onCerrar && (
                        <Button
                            onClick={() => onCerrar(hc.id)}
                            className="bg-foreground hover:bg-foreground/90 text-background rounded-2xl px-8 h-12 font-black text-xs uppercase tracking-widest shadow-2xl shadow-foreground/10 transition-all active:scale-95 flex items-center gap-2"
                        >
                            <Lock className="w-4 h-4" />
                            Cerrar HC
                        </Button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Content */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Identification Card */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                        <CardHeader className="p-10 pb-4 border-b border-border">
                            <div className="flex items-center gap-4">
                                <div className="bg-brand-indigo/10 p-2 rounded-xl">
                                    <Layers className="w-5 h-5 text-brand-indigo" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-foreground uppercase tracking-widest">Trazabilidad</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-muted-foreground mt-1">Vinculación con OP y versión del Costeo</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-10 grid grid-cols-1 md:grid-cols-3 gap-8">
                            <ReadOnlyField icon={Package} label="Orden de Producción" value={hc.idOP != null ? `OP #${hc.idOP}` : '—'} />
                            <ReadOnlyField icon={GitBranch} label="Versión de Costeo" value={hc.raw?.costeoVersionId ? `v${hc.raw.costeoVersionId}` : '—'} />
                            <ReadOnlyField icon={Hash} label="Folio" value={hc.numero || '—'} />
                        </CardContent>
                    </Card>

                    {/* Supplies Table */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                        <CardHeader className="p-10 pb-4 border-b border-border">
                            <div className="flex items-center gap-4">
                                <div className="bg-success/10 p-2 rounded-xl">
                                    <ShoppingBag className="w-5 h-5 text-success" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-foreground uppercase tracking-widest">Insumos Requeridos</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-muted-foreground mt-1">Calculados desde Costeo × Cantidad OP</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-0">
                            <Table>
                                <TableHeader className="bg-muted/50">
                                    <TableRow className="border-border hover:bg-transparent">
                                        <TableHead className="pl-8 font-black text-[9px] uppercase tracking-widest text-muted-foreground py-5">Insumo</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground">Tipo</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Stock Bodega</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Cant. Requerida</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Cant. a Comprar</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-right pr-6">Precio Ref.</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-center">Estado</TableHead>
                                        {(hc.status === 'BORRADOR' || hc.status === 'APROBADA') && (
                                            <TableHead className="font-black text-[9px] uppercase tracking-widest text-muted-foreground text-right pr-8">Acción</TableHead>
                                        )}
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {(hc.items || []).map((item, idx) => {
                                        const cantReq = Number(Number(item.cantidadRequerida || 0).toFixed(2));
                                        const cantComp = item.cantidadAComprar !== null && item.cantidadAComprar !== undefined ? Number(Number(item.cantidadAComprar).toFixed(2)) : cantReq;
                                        const isMod = item.modificado || cantComp !== cantReq;

                                        return (
                                            <TableRow key={item.id ?? idx} className="border-border hover:bg-card transition-all group">
                                                <TableCell className="pl-8 py-5">
                                                    <div className="flex items-center gap-3">
                                                        <div className="w-1.5 h-1.5 rounded-full bg-border group-hover:bg-brand-indigo transition-colors" />
                                                        <span className="font-bold text-foreground text-xs uppercase tracking-tight group-hover:text-brand-indigo transition-colors">{item.insumo || '—'}</span>
                                                    </div>
                                                </TableCell>
                                                <TableCell>
                                                    <Badge variant="outline" className="rounded-lg font-black text-[8px] uppercase tracking-widest border-border bg-card/50 text-muted-foreground py-1.5">
                                                        <Tag className="w-2.5 h-2.5 mr-1" />
                                                        {item.tipoInsumo || '—'}
                                                    </Badge>
                                                </TableCell>
                                                <TableCell className="text-center font-bold text-xs tabular-nums">
                                                    {Number(item.cantidadStock || 0) > 0 ? (
                                                        <Badge className="bg-success/10 text-success border-success/20 text-[10px] font-mono font-bold px-2 py-0.5 shadow-sm">
                                                            {Number(Number(item.cantidadStock).toFixed(2))} unid.
                                                        </Badge>
                                                    ) : (
                                                        <span className="text-muted-foreground font-mono text-[11px]">0.00</span>
                                                    )}
                                                </TableCell>
                                                <TableCell className="text-center font-bold text-xs text-muted-foreground tabular-nums">
                                                    {cantReq}
                                                </TableCell>
                                                <TableCell className="text-center font-black text-xs tabular-nums">
                                                    <span className={isMod ? "text-warning font-extrabold bg-warning/10 px-2 py-1 rounded-md border border-warning/20" : "text-foreground"}>
                                                        {cantComp}
                                                    </span>
                                                </TableCell>
                                                <TableCell className="text-right pr-6 font-black text-xs text-brand-indigo tabular-nums">
                                                    {formatCLP ? formatCLP(item.precioEstimado || 0) : (item.precioEstimado ?? '—')}
                                                </TableCell>
                                                <TableCell className="text-center">
                                                    {isMod ? (
                                                        <button
                                                            type="button"
                                                            onClick={() => setItemToShowJustificacion(item)}
                                                            aria-label={`Ver justificación de modificación de ${item.insumo || 'insumo'}`}
                                                            className="inline-block"
                                                        >
                                                            <Badge className="bg-gradient-to-r from-warning to-destructive text-white border-0 font-black text-[9px] uppercase tracking-widest px-2 py-0.5 shadow-md cursor-pointer hover:brightness-110 focus-visible:ring-2 focus-visible:ring-warning focus-visible:ring-offset-2 transition-all flex items-center gap-1">
                                                                <AlertTriangle className="w-2.5 h-2.5" />
                                                                MODIFICADO
                                                            </Badge>
                                                        </button>
                                                    ) : (
                                                        <Badge variant="outline" className="bg-muted text-muted-foreground border-border text-[9px] font-bold">
                                                            Original
                                                        </Badge>
                                                    )}
                                                </TableCell>
                                                {(hc.status === 'BORRADOR' || hc.status === 'APROBADA') && (
                                                    <TableCell className="text-right pr-8">
                                                        <Button
                                                            size="sm"
                                                            variant="outline"
                                                            onClick={() => setItemToModify(item)}
                                                            className="h-8 text-xs font-bold border-border hover:border-brand-indigo hover:bg-brand-indigo/10 hover:text-brand-indigo transition-all rounded-xl px-3"
                                                        >
                                                            <Edit3 className="w-3.5 h-3.5 mr-1.5" />
                                                            Modificar
                                                        </Button>
                                                    </TableCell>
                                                )}
                                            </TableRow>
                                        );
                                    })}
                                </TableBody>
                            </Table>
                            {(!hc.items || hc.items.length === 0) && (
                                <div className="py-20 text-center space-y-4">
                                    <div className="bg-muted p-6 rounded-full inline-block">
                                        <ShoppingBag className="w-10 h-10 text-muted-foreground/40" />
                                    </div>
                                    <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">Esta HC no tiene insumos</p>
                                </div>
                            )}
                        </CardContent>
                    </Card>

                    {/* Observations */}
                    {hc.raw?.observaciones && (
                        <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                            <CardHeader className="p-8 pb-4 border-b border-border">
                                <div className="flex items-center gap-3">
                                    <MessageSquareQuote className="w-5 h-5 text-brand-indigo" />
                                    <h4 className="text-[10px] font-black text-foreground uppercase tracking-widest">Observaciones</h4>
                                </div>
                            </CardHeader>
                            <CardContent className="p-8 pt-6">
                                <p className="text-[11px] text-muted-foreground font-medium italic leading-relaxed">
                                    {hc.raw.observaciones}
                                </p>
                            </CardContent>
                        </Card>
                    )}
                </div>

                {/* Left Action Column */}
                <div className="space-y-8">
                    {/* Summary Panel */}
                    <div className="p-8 bg-sidebar rounded-[2.5rem] text-white shadow-2xl shadow-brand-indigo/10 space-y-8 border border-sidebar-border">
                        <div className="flex items-center justify-between border-b border-sidebar-border pb-6">
                            <h3 className="text-xs font-black uppercase tracking-[0.2em] text-brand-indigo">Resumen</h3>
                            <div className="w-2 h-2 rounded-full bg-brand-indigo animate-pulse" />
                        </div>

                        <div className="space-y-4">
                            <div className="flex flex-col gap-1">
                                <span className="text-[10px] font-black text-sidebar-muted uppercase tracking-widest">Inversión Estimada</span>
                                <span className="text-3xl font-black text-white tracking-tighter tabular-nums">{formatCLP ? formatCLP(totalEstimado) : totalEstimado}</span>
                            </div>
                            <div className="pt-4 border-t border-sidebar-border space-y-3">
                                <StatItem icon={Package} label="Items" value={hc.items?.length ?? 0} />
                                <StatItem icon={ShoppingBag} label="Unidades Totales" value={totalUnidades} />
                                <StatItem icon={CheckCircle2} label="Estado" value={hc.status || '—'} />
                            </div>
                        </div>
                    </div>

                    <div className="bg-brand-indigo/10 border border-brand-indigo/20 rounded-[2rem] p-8 space-y-4">
                        <div className="flex items-center gap-3">
                            <Info className="w-4 h-4 text-brand-indigo" />
                            <h4 className="text-[9px] font-black text-brand-indigo uppercase tracking-[0.15em]">Próximo Paso</h4>
                        </div>
                        <p className="text-[10px] text-brand-indigo/70 font-bold uppercase tracking-tight leading-relaxed">
                            {hc.status === 'BORRADOR' && 'Revisa los insumos y aprueba para habilitar consolidación en OCs.'}
                            {hc.status === 'APROBADA' && 'Esta HC ya puede consolidarse en una Orden de Compra por proveedor.'}
                            {hc.status === 'CERRADA' && 'Esta HC está cerrada. Ya no admite cambios.'}
                        </p>
                    </div>
                </div>
            </div>

            <ModificarItemModal
                isOpen={!!itemToModify}
                onClose={() => setItemToModify(null)}
                item={itemToModify}
                idHC={hc.id}
                onSave={async (idHC, idHCItem, payload) => {
                    if (modificarItemHC) {
                        await modificarItemHC(idHC, idHCItem, payload);
                    }
                }}
                formatCLP={formatCLP || ((v) => `$${v}`)}
            />

            <JustificacionModal
                isOpen={!!itemToShowJustificacion}
                onClose={() => setItemToShowJustificacion(null)}
                item={itemToShowJustificacion}
            />
        </motion.div>
    );
}

function ReadOnlyField({ icon: Icon, label, value }) {
    return (
        <div className="space-y-3 group px-4 py-2 border-l-2 border-border hover:border-brand-indigo transition-colors">
            <div className="flex items-center gap-2">
                <Icon className="w-3.5 h-3.5 text-muted-foreground group-hover:text-brand-indigo transition-colors" />
                <label className="text-[8px] font-black text-muted-foreground uppercase tracking-[0.15em]">{label}</label>
            </div>
            <p className="text-xs font-black text-foreground uppercase tracking-tight">{value || "---"}</p>
        </div>
    );
}

function StatItem({ icon: Icon, label, value }) {
    return (
        <div className="flex items-center justify-between group">
            <div className="flex items-center gap-3">
                <div className="p-2 bg-sidebar-popup rounded-xl group-hover:bg-sidebar-active-bg transition-colors">
                    <Icon className="w-3.5 h-3.5 text-sidebar-muted" />
                </div>
                <span className="text-[9px] font-black text-sidebar-muted uppercase tracking-widest">{label}</span>
            </div>
            <span className="text-[10px] font-black text-white uppercase italic">{value}</span>
        </div>
    );
}
