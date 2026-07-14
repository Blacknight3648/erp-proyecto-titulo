import React from 'react';
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
import { motion, AnimatePresence } from "framer-motion";
import {
    ChevronLeft,
    User,
    Calendar,
    Tag,
    ClipboardList,
    Package,
    Info,
    CheckCircle2,
    Clock,
    DollarSign,
    Box,
    Hash,
    Layers,
    Edit3,
    MessageSquareQuote
} from 'lucide-react';

export default function OPVisualizacion({ opId, op, onBack, onEdit, formatCLP }) {
    if (!op) return (
        <div className="p-10 text-center bg-card/50 backdrop-blur-md rounded-3xl border border-dashed border-border">
            <p className="text-muted-foreground font-bold uppercase tracking-widest text-xs">Cargando datos de la orden...</p>
        </div>
    );

    const totalUnits = op.items?.reduce((acc, curr) => acc + (parseInt(curr.cantidad) || 0), 0) || 0;

    return (
        <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-8 pb-20"
        >
            {/* Top Navigation & Status Bar */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 bg-card/40 backdrop-blur-xl p-6 rounded-[2rem] border border-border shadow-2xl shadow-brand-indigo/10">
                <div className="flex items-center gap-5">
                    <Button
                        variant="outline"
                        size="icon"
                        onClick={onBack}
                        className="rounded-2xl border-border bg-card/80 hover:bg-card transition-all shadow-sm active:scale-90"
                    >
                        <ChevronLeft className="w-5 h-5 text-muted-foreground" />
                    </Button>
                    <div>
                        <div className="flex items-center gap-3">
                            <h2 className="text-2xl font-black text-foreground tracking-tight uppercase">Control OP: {op.id || op.idOP}</h2>
                            <Badge
                                variant="outline"
                                className={`
                                    px-3 py-1 rounded-lg font-black text-[9px] uppercase tracking-[0.15em] border-2
                                    ${op.estado === 'PENDIENTE' ? 'bg-warning/10 text-warning border-warning/20' : 'bg-success/10 text-success border-success/20'}
                                `}
                            >
                                {op.estado || 'PENDIENTE'}
                            </Badge>
                        </div>
                        <p className="text-[10px] text-muted-foreground font-black uppercase tracking-[0.2em] mt-1 italic">
                            Modo Lectura de Alta Fidelidad
                        </p>
                    </div>
                </div>

                <Button
                    onClick={onEdit}
                    className="bg-foreground hover:bg-foreground/90 text-background rounded-2xl px-8 h-12 font-black text-xs uppercase tracking-widest shadow-2xl shadow-foreground/10 transition-all active:scale-95 flex items-center gap-2"
                >
                    <Edit3 className="w-4 h-4" />
                    Solicitar Modificación
                </Button>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Content Area */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Business Data Card */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                        <CardHeader className="p-8 pb-4 border-b border-border">
                            <div className="flex items-center gap-3">
                                <div className="bg-brand-indigo/10 p-2 rounded-xl">
                                    <Layers className="w-5 h-5 text-brand-indigo" />
                                </div>
                                <div>
                                    <CardTitle className="text-xs font-black text-foreground uppercase tracking-widest">Información Estructural</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-tighter text-muted-foreground mt-1">Datos maestros del requerimiento</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-8 pt-6 grid grid-cols-1 md:grid-cols-2 gap-8">
                            <div className="space-y-6">
                                <InfoDisplay icon={User} label="Cliente Responsable" value={op.cliente || "No especificado"} />
                                <InfoDisplay icon={Calendar} label="Apertura Registro" value={op.fechaInicio || "Pendiente"} />
                                <InfoDisplay icon={Clock} label="Factis Programada" value={op.fechaEntregaProgramada || "Sin definir"} color="text-brand-indigo" />
                            </div>
                            <div className="space-y-6">
                                <InfoDisplay icon={Tag} label="Vínculo NV" value={op.notaVentaId || "Independiente"} />
                                <InfoDisplay icon={Package} label="Estado Logístico" value={op.estadoLogistico || "En proceso de carga"} />
                                <div className="flex items-center gap-4 pt-4 border-t border-border">
                                    <div className="bg-success/10 p-3 rounded-2xl">
                                        <CheckCircle2 className="w-4 h-4 text-success" />
                                    </div>
                                    <div>
                                        <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-0.5">Validación</p>
                                        <p className="text-xs font-black text-success uppercase">Aprobada para Producción</p>
                                    </div>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    {/* Production Items Card */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-2xl shadow-foreground/5 overflow-hidden border">
                        <CardHeader className="p-8 pb-4 border-b border-border flex flex-row items-center justify-between">
                            <div className="flex items-center gap-3">
                                <div className="bg-muted p-2 rounded-xl">
                                    <Box className="w-5 h-5 text-muted-foreground" />
                                </div>
                                <div>
                                    <CardTitle className="text-xs font-black text-foreground uppercase tracking-widest">Configuración de Producto</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-tighter text-muted-foreground mt-1">Variantes y cantidades autorizadas</CardDescription>
                                </div>
                            </div>
                            <Badge variant="secondary" className="bg-foreground text-background rounded-lg font-black text-[9px] px-3">{op.items?.length || 0} POSICIONES</Badge>
                        </CardHeader>
                        <Table>
                            <TableHeader className="bg-muted/30">
                                <TableRow className="hover:bg-transparent border-border">
                                    <TableHead className="pl-8 pb-4 font-black text-[9px] uppercase tracking-widest text-muted-foreground">Descripción / SKU</TableHead>
                                    <TableHead className="pb-4 text-center font-black text-[9px] uppercase tracking-widest text-muted-foreground">Dimensión</TableHead>
                                    <TableHead className="pb-4 text-center font-black text-[9px] uppercase tracking-widest text-muted-foreground">Tonalidad</TableHead>
                                    <TableHead className="pr-8 pb-4 text-right font-black text-[9px] uppercase tracking-widest text-muted-foreground">Total Un.</TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody>
                                {op.items?.map((item, idx) => (
                                    <TableRow key={idx} className="hover:bg-brand-indigo/5 border-border transition-colors">
                                        <TableCell className="pl-8 py-4">
                                            <p className="text-xs font-black text-foreground uppercase">{item.nombreProducto || 'ARTÍCULO SIN DESCRIPCIÓN'}</p>
                                            <p className="text-[9px] text-muted-foreground font-bold uppercase mt-0.5 tracking-tighter">REF: {item.codigo || 'SKU-000'}</p>
                                        </TableCell>
                                        <TableCell className="text-center font-black text-xs text-muted-foreground">{item.talla || '-'}</TableCell>
                                        <TableCell className="text-center">
                                            <Badge variant="outline" className="bg-card text-[9px] font-black tracking-widest border-border text-muted-foreground uppercase">{item.color || 'ESTÁNDAR'}</Badge>
                                        </TableCell>
                                        <TableCell className="pr-8 text-right font-black text-sm text-brand-indigo">{item.cantidad || 0}</TableCell>
                                    </TableRow>
                                ))}
                                {(!op.items || op.items.length === 0) && (
                                    <TableRow>
                                        <TableCell colSpan={4} className="py-20 text-center text-muted-foreground font-bold italic uppercase text-[10px] tracking-widest">
                                            Sin ítems vinculados a esta orden
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </Card>
                </div>

                {/* Sidebar Summary Area */}
                <div className="space-y-8">
                    {/* Financial Summary Card */}
                    <Card className="rounded-[2.5rem] bg-sidebar border-none shadow-2xl shadow-brand-indigo/20 p-2 overflow-hidden">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-brand-indigo uppercase tracking-[0.2em]">Métricas de Producción</CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-4 space-y-8">
                            <div>
                                <div className="flex items-center gap-2 mb-3">
                                    <MessageSquareQuote className="w-3.5 h-3.5 text-brand-indigo" />
                                    <p className="text-[9px] font-black text-brand-indigo uppercase tracking-widest">Nota Operativa</p>
                                </div>
                                <div className="bg-brand-indigo/10 p-5 rounded-3xl border border-brand-indigo/20">
                                    <p className="text-xs text-white/70 leading-relaxed italic font-medium">
                                        {op.observaciones || "No se han reportado especificaciones críticas adicionales por parte del área comercial."}
                                    </p>
                                </div>
                            </div>

                            <div className="space-y-4 pt-6 border-t border-brand-indigo/20">
                                <div className="flex justify-between items-end">
                                    <span className="text-[9px] font-black text-brand-indigo uppercase tracking-widest mb-1">Volumen Total</span>
                                    <span className="text-3xl font-black text-white tabular-nums">{totalUnits}</span>
                                </div>
                                <div className="flex justify-between items-center py-3 px-4 bg-brand-indigo/10 rounded-2xl border border-brand-indigo/20">
                                    <span className="text-[9px] font-black text-white/70 uppercase italic">Importancia Flujo</span>
                                    <Badge className="bg-brand-indigo text-white border-none font-bold text-[9px] tracking-widest">ALTA</Badge>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    {/* Traceability Card */}
                    <Card className="rounded-[2.5rem] border-border bg-card/60 backdrop-blur-md shadow-xl shadow-foreground/5 border p-2">
                        <CardHeader className="p-6 pb-2">
                            <CardTitle className="text-[9px] font-black text-muted-foreground uppercase tracking-[0.2em] flex items-center gap-2">
                                <Hash className="w-3 h-3" />
                                Trazabilidad Cruzada
                            </CardTitle>
                        </CardHeader>
                        <CardContent className="p-6 pt-4 space-y-3">
                            <TraceItem label="Nota de Venta" id={op.notaVentaId || 'PEND'} status="Activo" />
                            <TraceItem label="Costeo Solicitado" id="SCOS-412" status="Verificado" color="text-success" />
                            <TraceItem label="Evaluación Técnica" id="EVN-22" status="Cerrado" color="text-muted-foreground" />
                        </CardContent>
                    </Card>
                </div>
            </div>
        </motion.div>
    );
}

function InfoDisplay({ icon: Icon, label, value, color = "text-foreground" }) {
    return (
        <div className="flex items-start gap-4 group">
            <div className="bg-card p-3 rounded-2xl shadow-sm border border-border group-hover:scale-110 transition-transform">
                <Icon className="w-4 h-4 text-brand-indigo" />
            </div>
            <div>
                <p className="text-[9px] font-black text-muted-foreground uppercase tracking-[0.15em] mb-0.5">{label}</p>
                <p className={`text-sm font-black tracking-tight uppercase ${color}`}>{value}</p>
            </div>
        </div>
    );
}

function TraceItem({ label, id, status, color = "text-brand-indigo" }) {
    return (
        <div className="flex items-center justify-between p-4 rounded-2xl bg-card/50 border border-border hover:bg-card transition-all group shadow-sm">
            <div>
                <p className="text-[8px] font-black text-muted-foreground/60 uppercase tracking-tighter mb-0.5">{label}</p>
                <p className="text-[10px] font-black text-foreground uppercase group-hover:text-brand-indigo transition-colors">#{id}</p>
            </div>
            <span className={`text-[8px] font-black bg-muted px-2.5 py-1 rounded-full uppercase border border-border ${color}`}>
                {status}
            </span>
        </div>
    );
}
