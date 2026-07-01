import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
} from "../../../ui/table";
import { Badge } from "../../../ui/badge";
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Tabs, TabsList, TabsTrigger } from "../../../ui/tabs";
import { motion, AnimatePresence } from "framer-motion";
import { Plus, Search, Eye, ShoppingBag, ShoppingCart, CheckCircle2, Lock } from "lucide-react";

const STATUS_BADGE = {
    BORRADOR: 'bg-amber-50 text-amber-600 border-amber-100',
    APROBADA: 'bg-emerald-50 text-emerald-600 border-emerald-100',
    CERRADA:  'bg-slate-100 text-slate-500 border-slate-200',
};

export default function ListaHC({
    registros,
    activeTab,
    setActiveTab,
    searchTerm,
    setSearchTerm,
    handleOpenForm,
    aprobar,
    cerrar,
    onModificar,
    loading,
    error,
}) {

    const filteredRegistros = (registros || []).filter(hc => {
        const term = (searchTerm || '').toLowerCase();
        const matchesSearch =
            String(hc.id ?? '').toLowerCase().includes(term) ||
            (hc.numero || '').toLowerCase().includes(term) ||
            String(hc.idOP ?? '').toLowerCase().includes(term);

        if (activeTab === 'all') return matchesSearch;
        return matchesSearch && (hc.status || '').toUpperCase() === activeTab.toUpperCase();
    });

    return (
        <motion.div
            initial={{ opacity: 0, scale: 0.98 }}
            animate={{ opacity: 1, scale: 1 }}
            className="space-y-6"
        >
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 bg-white/40 backdrop-blur-xl p-8 rounded-[2.5rem] border border-white/20 shadow-2xl shadow-indigo-100/20">
                <div className="flex items-center gap-5">
                    <div className="bg-indigo-600 p-4 rounded-3xl shadow-xl shadow-indigo-200 animate-pulse-slow">
                        <ShoppingBag className="w-8 h-8 text-white" />
                    </div>
                    <div>
                        <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">Hojas de Compra</h2>
                        <div className="flex items-center gap-2 mt-1">
                            <span className="h-1.5 w-1.5 rounded-full bg-emerald-500 animate-pulse" />
                            <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em]">Gestión de Suministros MTO</p>
                        </div>
                    </div>
                </div>

                <div className="flex items-center gap-4">
                    <div className="relative group">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 group-focus-within:text-indigo-600 transition-colors" />
                        <Input
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            placeholder="Folio HC, número, OP..."
                            className="pl-12 w-[350px] h-12 bg-white/60 border-slate-100 rounded-2xl focus-visible:ring-indigo-600 font-bold text-xs shadow-inner"
                        />
                    </div>
                    <Button
                        onClick={() => handleOpenForm({ items: [] }, 'create')}
                        className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl h-12 px-8 font-black text-xs uppercase tracking-widest shadow-xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2"
                    >
                        <Plus className="w-4 h-4" />
                        Generar HC
                    </Button>
                </div>
            </div>

            {/* Filter Tabs */}
            <div className="flex items-center justify-between px-2">
                <Tabs value={activeTab} onValueChange={setActiveTab} className="w-auto">
                    <TabsList className="bg-slate-900/5 backdrop-blur-sm border border-slate-200 p-1 rounded-2xl h-14">
                        <TabsTrigger value="all" className="rounded-xl font-black text-[10px] uppercase tracking-widest px-6 data-[state=active]:bg-slate-900 data-[state=active]:text-white transition-all">Global</TabsTrigger>
                        <TabsTrigger value="BORRADOR" className="rounded-xl font-black text-[10px] uppercase tracking-widest px-6 data-[state=active]:bg-amber-500 data-[state=active]:text-white transition-all">Borrador</TabsTrigger>
                        <TabsTrigger value="APROBADA" className="rounded-xl font-black text-[10px] uppercase tracking-widest px-6 data-[state=active]:bg-emerald-600 data-[state=active]:text-white transition-all">Aprobadas</TabsTrigger>
                        <TabsTrigger value="CERRADA" className="rounded-xl font-black text-[10px] uppercase tracking-widest px-6 data-[state=active]:bg-slate-700 data-[state=active]:text-white transition-all">Cerradas</TabsTrigger>
                    </TabsList>
                </Tabs>

                <div className="flex items-center gap-3">
                    <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest px-4 py-2 bg-white/40 rounded-full border border-white/50 shadow-sm">
                        Mostrando: <span className="text-indigo-600">{filteredRegistros.length}</span> Registros
                    </p>
                </div>
            </div>

            {/* Error banner */}
            {error && (
                <div className="bg-red-50 border border-red-200 text-red-700 text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {error}
                </div>
            )}

            {/* Table */}
            <div className="bg-white/60 backdrop-blur-2xl rounded-[3rem] border border-white/40 shadow-2xl shadow-slate-200/50 overflow-hidden">
                <Table>
                    <TableHeader className="bg-slate-50/50">
                        <TableRow className="hover:bg-transparent border-slate-100">
                            <TableHead className="w-[200px] font-black text-[10px] uppercase tracking-[0.2em] text-slate-400 pl-10 py-6">ID / Folio</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.2em] text-slate-400">Origen</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.2em] text-slate-400 text-center">Items</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.2em] text-slate-400 text-center">Status</TableHead>
                            <TableHead className="text-right font-black text-[10px] uppercase tracking-[0.2em] text-slate-400 pr-10">Acciones</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        <AnimatePresence mode='popLayout'>
                            {filteredRegistros.map((hc, index) => (
                                <motion.tr
                                    layout
                                    initial={{ opacity: 0, y: 10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    exit={{ opacity: 0, scale: 0.95 }}
                                    transition={{ delay: index * 0.05 }}
                                    key={hc.id}
                                    className="group hover:bg-white/80 border-slate-50 transition-all cursor-pointer"
                                    onClick={() => handleOpenForm(hc, 'view')}
                                >
                                    <TableCell className="pl-10 py-6">
                                        <div className="flex flex-col">
                                            <span className="font-black text-sm text-indigo-600 tracking-tight">HC #{hc.id}</span>
                                            <span className="text-[8px] font-black text-slate-400 uppercase tracking-widest mt-1">{hc.numero || '—'}</span>
                                            <span className="text-[8px] font-bold text-slate-300 mt-0.5">{hc.fechaCreacion || '—'}</span>
                                        </div>
                                    </TableCell>
                                    <TableCell>
                                        <div className="flex items-center gap-2">
                                            <Badge variant="outline" className="text-[8px] font-black uppercase text-slate-500 border-slate-200 bg-slate-50">OP: {hc.idOP ?? '—'}</Badge>
                                        </div>
                                    </TableCell>
                                    <TableCell className="text-center">
                                        <span className="font-black text-sm text-slate-700">{hc.items?.length ?? 0}</span>
                                    </TableCell>
                                    <TableCell className="text-center">
                                        <Badge className={`px-4 h-7 rounded-full font-black text-[8px] uppercase tracking-widest border-2 ${STATUS_BADGE[hc.status] || ''}`}>
                                            {hc.status || '—'}
                                        </Badge>
                                    </TableCell>
                                    <TableCell className="text-right pr-10">
                                        <div className="flex items-center justify-end gap-1" onClick={(e) => e.stopPropagation()}>
                                            {hc.status === 'BORRADOR' && aprobar && (
                                                <Button
                                                    variant="ghost"
                                                    size="icon"
                                                    title="Aprobar HC"
                                                    onClick={() => aprobar(hc.id)}
                                                    className="rounded-xl hover:bg-emerald-600 hover:text-white transition-all"
                                                >
                                                    <CheckCircle2 className="w-4 h-4" />
                                                </Button>
                                            )}
                                            {hc.status === 'APROBADA' && onModificar && (
                                                <Button
                                                    variant="ghost"
                                                    size="icon"
                                                    title="Gestionar Compra (OC)"
                                                    onClick={() => onModificar(hc.id)}
                                                    className="rounded-xl hover:bg-indigo-600 hover:text-white transition-all"
                                                >
                                                    <ShoppingCart className="w-4 h-4" />
                                                </Button>
                                            )}
                                            {hc.status === 'APROBADA' && cerrar && (
                                                <Button
                                                    variant="ghost"
                                                    size="icon"
                                                    title="Cerrar HC"
                                                    onClick={() => cerrar(hc.id)}
                                                    className="rounded-xl hover:bg-slate-700 hover:text-white transition-all"
                                                >
                                                    <Lock className="w-4 h-4" />
                                                </Button>
                                            )}
                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                title="Ver detalle"
                                                onClick={() => handleOpenForm(hc, 'view')}
                                                className="rounded-xl hover:bg-indigo-600 hover:text-white transition-all"
                                            >
                                                <Eye className="w-4 h-4" />
                                            </Button>
                                        </div>
                                    </TableCell>
                                </motion.tr>
                            ))}
                        </AnimatePresence>
                    </TableBody>
                </Table>

                {!loading && filteredRegistros.length === 0 && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="py-32 flex flex-col items-center justify-center text-center space-y-6"
                    >
                        <div className="bg-slate-50 p-8 rounded-full border-2 border-dashed border-slate-100">
                            <Search className="w-16 h-16 text-slate-200" />
                        </div>
                        <div>
                            <p className="text-slate-400 font-black text-xs uppercase tracking-[0.3em]">Sin resultados operativos</p>
                            <p className="text-slate-300 text-[10px] mt-2 font-bold uppercase">Genera una HC desde una OP para comenzar</p>
                        </div>
                    </motion.div>
                )}

                {loading && (
                    <div className="py-12 flex justify-center">
                        <p className="text-slate-400 font-black text-[10px] uppercase tracking-widest">Cargando...</p>
                    </div>
                )}
            </div>
        </motion.div>
    );
}
