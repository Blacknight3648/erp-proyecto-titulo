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
import { Plus, Search, Eye, Filter, ListFilter, ClipboardList } from "lucide-react";

export default function ListaOP({
    registros,
    activeTab,
    setActiveTab,
    searchTerm,
    setSearchTerm,
    handleOpenForm
}) {

    // Filtrado por tab y búsqueda
    const filteredRegistros = registros?.filter(op => {
        const matchesSearch =
            op.cliente?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            op.id?.toLowerCase().includes(searchTerm.toLowerCase());

        if (activeTab === 'all') return matchesSearch;
        return matchesSearch && op.status?.toUpperCase() === activeTab.toUpperCase();
    });

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
        >
            {/* Header Area */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-card/50 backdrop-blur-md p-6 rounded-[2rem] border border-border shadow-xl shadow-brand-indigo/10">
                <div className="flex items-center gap-4">
                    <div className="bg-brand-indigo p-3 rounded-2xl shadow-lg shadow-brand-indigo/30">
                        <ClipboardList className="w-6 h-6 text-white" />
                    </div>
                    <div>
                        <h2 className="text-2xl font-black text-foreground tracking-tight">Órdenes de Producción</h2>
                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Control Comercial de Fabricación</p>
                    </div>
                </div>

                <div className="flex items-center gap-3">
                    <div className="relative group">
                        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground group-focus-within:text-brand-indigo transition-colors" />
                        <Input
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            placeholder="Buscar por cliente o ID..."
                            className="pl-10 w-[300px] bg-card border-border rounded-xl focus-visible:ring-brand-indigo font-bold text-xs"
                        />
                    </div>
                    <Button
                        onClick={() => handleOpenForm({}, 'create')}
                        className="bg-brand-indigo hover:bg-brand-indigo/90 text-white rounded-xl px-6 font-black text-xs uppercase tracking-widest shadow-xl shadow-brand-indigo/20 transition-all active:scale-95"
                    >
                        <Plus className="w-4 h-4 mr-2" />
                        Nueva OP
                    </Button>
                </div>
            </div>

            {/* Filters Area */}
            <div className="flex items-center justify-between">
                <Tabs value={activeTab} onValueChange={setActiveTab} className="w-auto">
                    <TabsList className="bg-card/50 backdrop-blur-md border border-border p-1 rounded-xl h-12">
                        <TabsTrigger value="all" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-foreground data-[state=active]:text-background">Todas</TabsTrigger>
                        <TabsTrigger value="PENDIENTE" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-warning data-[state=active]:text-white">Pendientes</TabsTrigger>
                        <TabsTrigger value="EN_PROCESO" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-primary data-[state=active]:text-white">En Proceso</TabsTrigger>
                        <TabsTrigger value="COMPLETADO" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-success data-[state=active]:text-white">Completadas</TabsTrigger>
                    </TabsList>
                </Tabs>

                <Button variant="outline" className="rounded-xl border-border bg-card/50 font-black text-[10px] uppercase tracking-widest flex items-center gap-2">
                    <ListFilter className="w-4 h-4" />
                    Más Filtros
                </Button>
            </div>

            {/* Table Area */}
            <div className="bg-card/80 backdrop-blur-xl rounded-[2.5rem] border border-border shadow-2xl shadow-foreground/5 overflow-hidden">
                <Table>
                    <TableHeader className="bg-muted/50">
                        <TableRow className="hover:bg-transparent border-border">
                            <TableHead className="w-[150px] font-black text-[10px] uppercase tracking-[0.15em] text-muted-foreground pl-8">ID Registro</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.15em] text-muted-foreground">Cliente / Entidad</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.15em] text-muted-foreground">Estado Operativo</TableHead>
                            <TableHead className="text-right font-black text-[10px] uppercase tracking-[0.15em] text-muted-foreground pr-8">Gestión</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        <AnimatePresence mode='popLayout'>
                            {filteredRegistros?.map((op, index) => (
                                <motion.tr
                                    layout
                                    initial={{ opacity: 0, x: -20 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    exit={{ opacity: 0, scale: 0.95 }}
                                    transition={{ delay: index * 0.05 }}
                                    key={op.id || op.idOP}
                                    className="group hover:bg-brand-indigo/5 border-border transition-all cursor-pointer"
                                    onClick={() => handleOpenForm(op, 'view')}
                                >
                                    <TableCell className="pl-8 py-5">
                                        <span className="font-black text-xs text-brand-indigo bg-brand-indigo/10 px-3 py-1.5 rounded-lg border border-brand-indigo/20 shadow-sm">
                                            {op.id || op.idOP}
                                        </span>
                                    </TableCell>
                                    <TableCell>
                                        <div className="flex flex-col">
                                            <span className="font-bold text-foreground text-sm group-hover:text-brand-indigo transition-colors uppercase">{op.cliente}</span>
                                            <span className="text-[10px] text-muted-foreground font-medium">Ref: Producción Comercial</span>
                                        </div>
                                    </TableCell>
                                    <TableCell>
                                        <Badge
                                            variant="secondary"
                                            className={`
                                                px-3 py-1 rounded-lg font-black text-[9px] uppercase tracking-widest border-2
                                                ${op.status?.toUpperCase() === 'PENDIENTE' ? 'bg-warning/10 text-warning border-warning/20' : ''}
                                                ${op.status?.toUpperCase() === 'EN_PROCESO' ? 'bg-primary/10 text-primary border-primary/20' : ''}
                                                ${op.status?.toUpperCase() === 'COMPLETADO' ? 'bg-success/10 text-success border-success/20' : ''}
                                            `}
                                        >
                                            {op.status || 'SIN ESTADO'}
                                        </Badge>
                                    </TableCell>
                                    <TableCell className="text-right pr-8">
                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            className="rounded-xl hover:bg-brand-indigo hover:text-white transition-all"
                                        >
                                            <Eye className="w-4 h-4" />
                                        </Button>
                                    </TableCell>
                                </motion.tr>
                            ))}
                        </AnimatePresence>
                    </TableBody>
                </Table>

                {(!filteredRegistros || filteredRegistros.length === 0) && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="py-24 flex flex-col items-center justify-center text-center space-y-4"
                    >
                        <div className="bg-muted p-6 rounded-full border border-border">
                            <Search className="w-12 h-12 text-muted-foreground/40" />
                        </div>
                        <div>
                            <p className="text-muted-foreground font-black text-xs uppercase tracking-widest">No se encontraron registros</p>
                            <p className="text-muted-foreground/70 text-[10px] mt-1">Intenta con otros términos de búsqueda o filtros</p>
                        </div>
                    </motion.div>
                )}
            </div>
        </motion.div>
    );
}
