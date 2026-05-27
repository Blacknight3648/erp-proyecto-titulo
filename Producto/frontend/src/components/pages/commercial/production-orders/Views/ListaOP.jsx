import React from 'react';
import { 
    Table, 
    TableBody, 
    TableCell, 
    TableHead, 
    TableHeader, 
    TableRow 
} from "../../../../ui/table";
import { Badge } from "../../../../ui/badge";
import { Button } from "../../../../ui/button";
import { Input } from "../../../../ui/input";
import { Tabs, TabsList, TabsTrigger } from "../../../../ui/tabs";
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
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-white/50 backdrop-blur-md p-6 rounded-[2rem] border border-white/20 shadow-xl shadow-indigo-100/20">
                <div className="flex items-center gap-4">
                    <div className="bg-indigo-600 p-3 rounded-2xl shadow-lg shadow-indigo-200">
                        <ClipboardList className="w-6 h-6 text-white" />
                    </div>
                    <div>
                        <h2 className="text-2xl font-black text-slate-800 tracking-tight">Órdenes de Producción</h2>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Control Comercial de Fabricación</p>
                    </div>
                </div>

                <div className="flex items-center gap-3">
                    <div className="relative group">
                        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 group-focus-within:text-indigo-600 transition-colors" />
                        <Input 
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            placeholder="Buscar por cliente o ID..."
                            className="pl-10 w-[300px] bg-white border-slate-100 rounded-xl focus-visible:ring-indigo-600 font-bold text-xs"
                        />
                    </div>
                    <Button 
                        onClick={() => handleOpenForm({}, 'create')}
                        className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl px-6 font-black text-xs uppercase tracking-widest shadow-xl shadow-indigo-100 transition-all active:scale-95"
                    >
                        <Plus className="w-4 h-4 mr-2" />
                        Nueva OP
                    </Button>
                </div>
            </div>

            {/* Filters Area */}
            <div className="flex items-center justify-between">
                <Tabs value={activeTab} onValueChange={setActiveTab} className="w-auto">
                    <TabsList className="bg-white/50 backdrop-blur-md border border-slate-100 p-1 rounded-xl h-12">
                        <TabsTrigger value="all" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-slate-900 data-[state=active]:text-white">Todas</TabsTrigger>
                        <TabsTrigger value="PENDIENTE" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-amber-500 data-[state=active]:text-white">Pendientes</TabsTrigger>
                        <TabsTrigger value="EN_PROCESO" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-blue-600 data-[state=active]:text-white">En Proceso</TabsTrigger>
                        <TabsTrigger value="COMPLETADO" className="rounded-lg font-black text-[10px] uppercase tracking-widest data-[state=active]:bg-emerald-600 data-[state=active]:text-white">Completadas</TabsTrigger>
                    </TabsList>
                </Tabs>

                <Button variant="outline" className="rounded-xl border-slate-100 bg-white/50 font-black text-[10px] uppercase tracking-widest flex items-center gap-2">
                    <ListFilter className="w-4 h-4" />
                    Más Filtros
                </Button>
            </div>

            {/* Table Area */}
            <div className="bg-white/80 backdrop-blur-xl rounded-[2.5rem] border border-white/40 shadow-2xl shadow-slate-200/50 overflow-hidden">
                <Table>
                    <TableHeader className="bg-slate-50/50">
                        <TableRow className="hover:bg-transparent border-slate-100">
                            <TableHead className="w-[150px] font-black text-[10px] uppercase tracking-[0.15em] text-slate-400 pl-8">ID Registro</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.15em] text-slate-400">Cliente / Entidad</TableHead>
                            <TableHead className="font-black text-[10px] uppercase tracking-[0.15em] text-slate-400">Estado Operativo</TableHead>
                            <TableHead className="text-right font-black text-[10px] uppercase tracking-[0.15em] text-slate-400 pr-8">Gestión</TableHead>
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
                                    className="group hover:bg-indigo-50/30 border-slate-50 transition-all cursor-pointer"
                                    onClick={() => handleOpenForm(op, 'view')}
                                >
                                    <TableCell className="pl-8 py-5">
                                        <span className="font-black text-xs text-indigo-600 bg-indigo-50 px-3 py-1.5 rounded-lg border border-indigo-100 shadow-sm">
                                            {op.id || op.idOP}
                                        </span>
                                    </TableCell>
                                    <TableCell>
                                        <div className="flex flex-col">
                                            <span className="font-bold text-slate-700 text-sm group-hover:text-indigo-600 transition-colors uppercase">{op.cliente}</span>
                                            <span className="text-[10px] text-slate-400 font-medium">Ref: Producción Comercial</span>
                                        </div>
                                    </TableCell>
                                    <TableCell>
                                        <Badge 
                                            variant="secondary" 
                                            className={`
                                                px-3 py-1 rounded-lg font-black text-[9px] uppercase tracking-widest border-2
                                                ${op.status?.toUpperCase() === 'PENDIENTE' ? 'bg-amber-50 text-amber-600 border-amber-100' : ''}
                                                ${op.status?.toUpperCase() === 'EN_PROCESO' ? 'bg-blue-50 text-blue-600 border-blue-100' : ''}
                                                ${op.status?.toUpperCase() === 'COMPLETADO' ? 'bg-emerald-50 text-emerald-600 border-emerald-100' : ''}
                                            `}
                                        >
                                            {op.status || 'SIN ESTADO'}
                                        </Badge>
                                    </TableCell>
                                    <TableCell className="text-right pr-8">
                                        <Button 
                                            variant="ghost" 
                                            size="icon"
                                            className="rounded-xl hover:bg-indigo-600 hover:text-white transition-all"
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
                        <div className="bg-slate-50 p-6 rounded-full border border-slate-100">
                            <Search className="w-12 h-12 text-slate-200" />
                        </div>
                        <div>
                            <p className="text-slate-400 font-black text-xs uppercase tracking-widest">No se encontraron registros</p>
                            <p className="text-slate-300 text-[10px] mt-1">Intenta con otros términos de búsqueda o filtros</p>
                        </div>
                    </motion.div>
                )}
            </div>
        </motion.div>
    );
}
