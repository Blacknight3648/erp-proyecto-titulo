import React from 'react';
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
    CardDescription
} from "../../../../ui/card";
import { Badge } from "../../../../ui/badge";
import { Button } from "../../../../ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
} from "../../../../ui/table";
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
    MessageSquareQuote,
    ShoppingBag,
    Truck,
    FilePlus2
} from 'lucide-react';
import { toast } from 'sonner';

export default function HCVisualizacion({ hcId, hc, onBack, onEdit, formatCLP }) {
    if (!hc) return (
        <div className="p-10 text-center bg-white/50 backdrop-blur-md rounded-3xl border border-dashed border-slate-200">
            <p className="text-slate-400 font-bold uppercase tracking-widest text-xs">Cargando datos de la hoja de compra...</p>
        </div>
    );

    const handleGenerateOCOP = (itemId) => {
        toast.success("Solicitud de OC generada", {
            description: "La orden de compra ha sido enviada al módulo de Adquisiciones.",
            className: "rounded-2xl font-black uppercase tracking-widest text-[10px] bg-slate-900 text-white border-none shadow-2xl"
        });
    };

    const totalBudget = hc.items?.reduce((acc, curr) => acc + (parseFloat(curr.cantidadRequerida || 0) * parseFloat(curr.precioEstimado || 0)), 0) || 0;

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
                            <h2 className="text-4xl font-black text-slate-900 tracking-tighter uppercase">{hc.id}</h2>
                            <Badge
                                className={`
                                    px-4 h-7 rounded-full font-black text-[9px] uppercase tracking-widest border-2
                                    ${hc.status === 'PENDIENTE' ? 'bg-amber-50 text-amber-600 border-amber-100' : 'bg-emerald-50 text-emerald-600 border-emerald-100'}
                                `}
                            >
                                {hc.status || 'PENDIENTE'}
                            </Badge>
                        </div>
                        <p className="text-[10px] text-slate-400 font-black uppercase tracking-[0.2em] mt-2 italic">
                            Reporte de Adquisiciones y Suministros
                        </p>
                    </div>
                </div>

                <Button
                    onClick={onEdit}
                    className="bg-slate-900 hover:bg-black text-white rounded-2xl px-8 h-12 font-black text-xs uppercase tracking-widest shadow-2xl shadow-slate-200 transition-all active:scale-95 flex items-center gap-2"
                >
                    <Edit3 className="w-4 h-4" />
                    Solicitar Cambio
                </Button>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Content */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Identification Card */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 overflow-hidden border">
                        <CardHeader className="p-10 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-4">
                                <div className="bg-indigo-50 p-2 rounded-xl">
                                    <Layers className="w-5 h-5 text-indigo-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-widest">Atributos del Negocio</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">Estructura comercial y trazabilidad</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-10 grid grid-cols-1 md:grid-cols-3 gap-8">
                            <ReadOnlyField icon={User} label="Cliente Mandante" value={hc.cliente} />
                            <ReadOnlyField icon={Hash} label="Folio Nota de Venta" value={hc.folioNV} />
                            <ReadOnlyField icon={Package} label="Orden Producción" value={hc.idOP} />
                        </CardContent>
                    </Card>

                    {/* Supplies Table */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 overflow-hidden border">
                        <CardHeader className="p-10 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-4">
                                <div className="bg-emerald-50 p-2 rounded-xl">
                                    <ShoppingBag className="w-5 h-5 text-emerald-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-widest">Insumos Críticos para Procura</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">Listado valorizado de materiales requeridos</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-0">
                            <Table>
                                <TableHeader className="bg-slate-50/50">
                                    <TableRow className="border-slate-100 hover:bg-transparent">
                                        <TableHead className="pl-10 font-black text-[9px] uppercase tracking-widest text-slate-400 py-5">Insumo Requerido</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400">Proveedor</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-center">Unidad</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-center">Cantidad</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-right">P. Unitario Est.</TableHead>
                                        <TableHead className="font-black text-[9px] uppercase tracking-widest text-slate-400 text-center pr-10 whitespace-nowrap">Gestión de Compra</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {hc.items?.map((item, idx) => (
                                        <TableRow key={idx} className="border-slate-50 hover:bg-white transition-all group">
                                            <TableCell className="pl-10 py-6">
                                                <div className="flex items-center gap-3">
                                                    <div className="w-1.5 h-1.5 rounded-full bg-slate-200 group-hover:bg-indigo-500 transition-colors" />
                                                    <span className="font-bold text-slate-700 text-xs uppercase tracking-tight group-hover:text-indigo-600 transition-colors">{item.insumo}</span>
                                                </div>
                                            </TableCell>
                                            <TableCell>
                                                <div className="flex items-center gap-2">
                                                    <Badge variant="outline" className="rounded-lg font-black text-[8px] uppercase tracking-widest border-slate-100 bg-white/50 text-slate-500 py-1.5">
                                                        {item.proveedor || "Sin Asignar"}
                                                    </Badge>
                                                </div>
                                            </TableCell>
                                            <TableCell className="text-center">
                                                <span className="px-3 py-1 bg-slate-100 rounded-lg font-black text-[8px] text-slate-400 uppercase tracking-[0.2em] inline-block border border-slate-200/50 shadow-inner">
                                                    {item.unidad}
                                                </span>
                                            </TableCell>
                                            <TableCell className="text-center font-black text-xs text-slate-800">
                                                {item.cantidadRequerida}
                                            </TableCell>
                                            <TableCell className="text-right font-black text-xs text-indigo-600 tabular-nums">
                                                {formatCLP(item.precioEstimado)}
                                            </TableCell>
                                            <TableCell className="text-center pr-10">
                                                <Button
                                                    variant="outline"
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        handleGenerateOCOP(item.id);
                                                    }}
                                                    className="h-10 rounded-xl font-black text-[10px] uppercase tracking-widest border-indigo-100 bg-indigo-50/50 text-indigo-600 hover:bg-slate-900 hover:text-white hover:border-slate-900 transition-all active:scale-95 flex items-center gap-2 mx-auto"
                                                >
                                                    <FilePlus2 className="w-3.5 h-3.5" />
                                                    Generar OC
                                                </Button>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </CardContent>
                    </Card>

                    {/* Observations */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 overflow-hidden border">
                        <CardHeader className="p-8 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-3">
                                <MessageSquareQuote className="w-5 h-5 text-indigo-500" />
                                <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Notas Adicionales</h4>
                            </div>
                        </CardHeader>
                        <CardContent className="p-8 pt-6">
                            <p className="text-[11px] text-slate-500 font-medium italic leading-relaxed">
                                {hc.observaciones || "Sin observaciones adicionales registradas para este documento."}
                            </p>
                        </CardContent>
                    </Card>
                </div>

                {/* Sidebar Summary */}
                <div className="space-y-8">
                    <Card className="rounded-[2.5rem] bg-slate-900 border-none shadow-2xl p-4 overflow-hidden">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em] mb-4">Resumen Presupuestario</CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-8">
                            <div className="flex flex-col gap-1">
                                <span className="text-[10px] font-black text-slate-500 uppercase tracking-widest">Inversión Estimada Total</span>
                                <span className="text-3xl font-black text-white tracking-tighter tabular-nums">{formatCLP(totalBudget)}</span>
                                <Badge variant="outline" className="w-fit mt-2 border-emerald-500/30 text-emerald-400 bg-emerald-500/5 font-black text-[9px] uppercase tracking-widest px-3">Base Imponible Est.</Badge>
                            </div>

                            <div className="space-y-4 pt-8 border-t border-slate-800">
                                <StatItem icon={Calendar} label="Fecha Emisión" value={hc.fechaCreacion} />
                                <StatItem icon={Truck} label="Proveedor Líder" value={hc.proveedorPrincipal} />
                                <StatItem icon={CheckCircle2} label="Status Sistema" value={hc.status} />
                            </div>
                        </CardContent>
                    </Card>

                    <div className="bg-indigo-50/50 border border-indigo-100 rounded-[2rem] p-8 space-y-4">
                        <div className="flex items-center gap-3">
                            <Info className="w-4 h-4 text-indigo-500" />
                            <h4 className="text-[9px] font-black text-indigo-600 uppercase tracking-[0.15em]">Garantía de Suministro</h4>
                        </div>
                        <p className="text-[10px] text-indigo-500/70 font-bold uppercase tracking-tight leading-relaxed">
                            Este documento sirve como base técnica para la emisión de las Órdenes de Compra (OC). Cualquier discrepancia en el stock debe ser reportada antes de la compra.
                        </p>
                    </div>
                </div>
            </div>
        </motion.div>
    );
}

function ReadOnlyField({ icon: Icon, label, value }) {
    return (
        <div className="space-y-3 group px-4 py-2 border-l-2 border-slate-100 hover:border-indigo-500 transition-colors">
            <div className="flex items-center gap-2">
                <Icon className="w-3.5 h-3.5 text-slate-400 group-hover:text-indigo-600 transition-colors" />
                <label className="text-[8px] font-black text-slate-400 uppercase tracking-[0.15em]">{label}</label>
            </div>
            <p className="text-xs font-black text-slate-700 uppercase tracking-tight">{value || "---"}</p>
        </div>
    );
}

function StatItem({ icon: Icon, label, value }) {
    return (
        <div className="flex items-center justify-between group">
            <div className="flex items-center gap-3">
                <div className="p-2 bg-slate-800 rounded-xl group-hover:bg-slate-700 transition-colors">
                    <Icon className="w-3.5 h-3.5 text-slate-500" />
                </div>
                <span className="text-[9px] font-black text-slate-500 uppercase tracking-widest">{label}</span>
            </div>
            <span className="text-[10px] font-black text-slate-300 uppercase italic">{value}</span>
        </div>
    );
}
