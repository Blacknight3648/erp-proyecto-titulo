import React from 'react';
import { 
    Save, 
    X, 
    Plus, 
    Trash2, 
    ShoppingCart, 
    ClipboardList, 
    Truck, 
    Calculator,
    Info,
    AlertCircle,
    Hash
} from 'lucide-react';
import { Button } from "../../../../ui/button";
import { Input } from "../../../../ui/input";
import { 
    Card, 
    CardContent, 
    CardHeader, 
    CardTitle, 
    CardDescription 
} from "../../../../ui/card";
import { Badge } from "../../../../ui/badge";
import { Textarea } from "../../../../ui/textarea";
import { motion, AnimatePresence } from "framer-motion";

export default function HCSolicitudForm({ 
    formData, setFormData, onCancel, onSave, isSubmitting, 
    addItem, removeItem, updateItem, 
    totalItems, totalBudget, formatCLP
}) {
    
    return (
        <motion.div 
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-8 pb-32"
        >
            {/* Action Bar */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 bg-white/40 backdrop-blur-3xl p-8 rounded-[3rem] border-2 border-white/50 shadow-2xl shadow-indigo-100/30">
                <div className="flex items-center gap-5">
                    <div className="bg-slate-900 p-4 rounded-3xl shadow-xl shadow-slate-200">
                        <ShoppingCart className="w-8 h-8 text-white" />
                    </div>
                    <div>
                        <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">Nueva Hoja de Compra</h2>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-1">Sincronización de Insumos & Suministros</p>
                    </div>
                </div>

                <div className="flex gap-4">
                    <Button
                        variant="ghost"
                        onClick={onCancel}
                        className="px-8 h-12 rounded-2xl font-black text-xs uppercase tracking-widest text-slate-500 hover:bg-white hover:text-indigo-600 transition-all active:scale-95"
                    >
                        Cancelar
                    </Button>
                    <Button
                        onClick={onSave}
                        disabled={isSubmitting}
                        className="px-10 h-12 bg-indigo-600 hover:bg-slate-900 text-white rounded-2xl font-black text-xs uppercase tracking-widest shadow-2xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2"
                    >
                        <Save className="w-4 h-4" />
                        {isSubmitting ? 'Procesando...' : 'Confirmar Registro'}
                    </Button>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Form Area */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Identification Section */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-hidden">
                        <CardHeader className="p-10 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-4">
                                <div className="bg-indigo-50 p-2 rounded-xl">
                                    <ClipboardList className="w-6 h-6 text-indigo-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-[0.1em]">Referencia & Nexo Comercial</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">Vinculación con nota de venta y orden de producción</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-10 grid grid-cols-1 md:grid-cols-2 gap-10">
                            <FormField icon={Hash} label="Folio NV Asociada">
                                <Input 
                                    className="h-14 bg-white/80 border-slate-100 rounded-2xl px-5 font-black text-xs text-slate-700 uppercase"
                                    placeholder="Ej: NV-XXXX"
                                    value={formData.folioNV || ''}
                                    onChange={(e) => setFormData({...formData, folioNV: e.target.value})}
                                />
                            </FormField>
                            <FormField icon={Info} label="ID Hoja de Producción (OP)">
                                <Input 
                                    className="h-14 bg-white/80 border-slate-100 rounded-2xl px-5 font-black text-xs text-slate-700 uppercase"
                                    placeholder="Ej: OP-XXXX"
                                    value={formData.idOP || ''}
                                    onChange={(e) => setFormData({...formData, idOP: e.target.value})}
                                />
                            </FormField>
                            <div className="md:col-span-2">
                                <FormField icon={Info} label="Nombre del Cliente / Entidad">
                                    <Input 
                                        className="h-14 bg-white/80 border-slate-100 rounded-2xl px-5 font-bold text-xs text-slate-700 uppercase"
                                        placeholder="Nombre oficial del cliente"
                                        value={formData.cliente || ''}
                                        onChange={(e) => setFormData({...formData, cliente: e.target.value})}
                                    />
                                </FormField>
                            </div>
                        </CardContent>
                    </Card>

                    {/* Supplies Table Section */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-hidden">
                        <CardHeader className="p-10 pb-4 border-b border-slate-50 flex flex-row items-center justify-between">
                            <div className="flex items-center gap-4">
                                <div className="bg-emerald-50 p-2 rounded-xl">
                                    <Truck className="w-6 h-6 text-emerald-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-[0.1em]">Detalle de Insumos & Suministros</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">Listado de materiales requeridos para la fabricación</CardDescription>
                                </div>
                            </div>
                            <Button 
                                onClick={addItem}
                                className="bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl h-10 px-6 font-black text-[10px] uppercase tracking-widest transition-all hover:scale-105 active:scale-95"
                            >
                                <Plus className="w-3.5 h-3.5 mr-2" />
                                Añadir Insumo
                            </Button>
                        </CardHeader>
                        <CardContent className="p-0">
                            <div className="overflow-x-auto">
                                <table className="w-full">
                                    <thead className="bg-slate-50/70 border-b border-slate-100">
                                        <tr>
                                            <th className="px-8 py-5 text-left text-[9px] font-black text-slate-400 uppercase tracking-widest">Insumo / Descripción</th>
                                            <th className="px-4 py-5 text-center text-[9px] font-black text-slate-400 uppercase tracking-widest">Unidad</th>
                                            <th className="px-4 py-5 text-center text-[9px] font-black text-slate-400 uppercase tracking-widest w-32">Cantidad</th>
                                            <th className="px-4 py-5 text-right text-[9px] font-black text-slate-400 uppercase tracking-widest w-40">Precio Est.</th>
                                            <th className="px-8 py-5 text-right text-[9px] font-black text-slate-400 uppercase tracking-widest">Opción</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y divide-slate-50">
                                        <AnimatePresence>
                                            {(formData.items || []).map((item, index) => (
                                                <motion.tr 
                                                    initial={{ opacity: 0, x: -10 }}
                                                    animate={{ opacity: 1, x: 0 }}
                                                    exit={{ opacity: 0, x: 10 }}
                                                    key={item.id}
                                                    className="group hover:bg-indigo-50/50 transition-colors"
                                                >
                                                    <td className="px-8 py-4">
                                                        <Input 
                                                            className="h-10 bg-white border-transparent focus:border-indigo-100 rounded-xl px-4 font-bold text-xs uppercase"
                                                            placeholder="Nombre del insumo..."
                                                            value={item.insumo}
                                                            onChange={(e) => updateItem(item.id, 'insumo', e.target.value)}
                                                        />
                                                    </td>
                                                    <td className="px-4 py-4">
                                                        <select 
                                                            className="h-10 bg-white border border-transparent rounded-xl px-2 font-black text-[10px] uppercase text-slate-500 outline-none focus:border-indigo-100 cursor-pointer"
                                                            value={item.unidad}
                                                            onChange={(e) => updateItem(item.id, 'unidad', e.target.value)}
                                                        >
                                                            <option value="MTS">MTS</option>
                                                            <option value="UNDS">UNDS</option>
                                                            <option value="CONOS">CONOS</option>
                                                            <option value="KG">KG</option>
                                                            <option value="PACKS">PACKS</option>
                                                        </select>
                                                    </td>
                                                    <td className="px-4 py-4">
                                                        <Input 
                                                            type="number"
                                                            className="h-10 bg-white border-transparent text-center font-black text-xs"
                                                            value={item.cantidadRequerida}
                                                            onChange={(e) => updateItem(item.id, 'cantidadRequerida', e.target.value)}
                                                        />
                                                    </td>
                                                    <td className="px-4 py-4">
                                                        <Input 
                                                            type="number"
                                                            className="h-10 bg-white border-transparent text-right font-black text-xs text-indigo-600"
                                                            value={item.precioEstimado}
                                                            onChange={(e) => updateItem(item.id, 'precioEstimado', e.target.value)}
                                                        />
                                                    </td>
                                                    <td className="px-8 py-4 text-right">
                                                        <Button 
                                                            variant="ghost" 
                                                            size="icon"
                                                            onClick={() => removeItem(item.id)}
                                                            className="text-slate-300 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition-all"
                                                        >
                                                            <Trash2 className="w-4 h-4" />
                                                        </Button>
                                                    </td>
                                                </motion.tr>
                                            ))}
                                        </AnimatePresence>
                                    </tbody>
                                </table>
                                {(!formData.items || formData.items.length === 0) && (
                                    <div className="py-20 text-center space-y-4">
                                        <div className="bg-slate-50 p-6 rounded-full inline-block">
                                            <ShoppingCart className="w-10 h-10 text-slate-200" />
                                        </div>
                                        <p className="text-[10px] font-black text-slate-300 uppercase tracking-widest italic">Inicia el listado cargando el primer insumo</p>
                                    </div>
                                )}
                            </div>
                        </CardContent>
                    </Card>
                </div>

                {/* Sidebar Stats Area */}
                <div className="space-y-8">
                    <Card className="rounded-[2.5rem] bg-slate-900 border-none shadow-2xl p-4 overflow-hidden relative">
                        <div className="absolute top-0 right-0 p-8 opacity-5">
                            <Calculator className="w-40 h-40 text-white" />
                        </div>
                        <CardHeader className="p-8 pb-4 relative z-10">
                            <CardTitle className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.25em] mb-4">Métricas de Presupuesto</CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-10 relative z-10">
                            <StatBox label="Total Insumos" value={totalItems} sublabel="Items Únicos" color="indigo" />
                            <StatBox label="Inversión Est." value={formatCLP(totalBudget)} sublabel="Presupuesto Bruto" color="emerald" />
                            
                            <div className="pt-6 border-t border-slate-800 flex items-start gap-4">
                                <AlertCircle className="w-5 h-5 text-amber-500 shrink-0" />
                                <p className="text-[9px] text-slate-400 font-bold leading-relaxed uppercase tracking-tighter italic">
                                    Nota: Los precios mostrados son estimaciones basadas en la última SCOS sincronizada. El valor final dependerá de la OC emitida.
                                </p>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="rounded-[2.5rem] bg-white/40 backdrop-blur-md border border-white/50 p-10 space-y-6 shadow-xl shadow-slate-200/50">
                        <div className="flex items-center gap-3">
                            <Info className="w-4 h-4 text-indigo-500" />
                            <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Observaciones de Procura</h4>
                        </div>
                        <Textarea 
                            rows={6}
                            className="bg-white/80 border-slate-100 rounded-[1.5rem] p-6 text-[11px] text-slate-600 italic font-medium focus-visible:ring-indigo-500 transition-all resize-none shadow-inner"
                            placeholder="Instrucciones adicionales para adquisiciones..."
                            value={formData.observaciones || ''}
                            onChange={(e) => setFormData({...formData, observaciones: e.target.value})}
                        />
                    </Card>
                </div>
            </div>
        </motion.div>
    );
}

function FormField({ icon: Icon, label, children }) {
    return (
        <div className="space-y-4 group">
            <div className="flex items-center gap-3 px-1">
                <Icon className="w-4 h-4 text-indigo-600 transition-transform group-focus-within:scale-110" />
                <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">{label}</label>
            </div>
            {children}
        </div>
    );
}

function StatBox({ label, value, sublabel, color }) {
    const colorClasses = {
        indigo: 'border-indigo-500/30 text-indigo-400',
        emerald: 'border-emerald-500/30 text-emerald-400',
    };

    return (
        <div className={`p-6 border-l-4 rounded-2xl bg-slate-800/40 ${colorClasses[color]}`}>
            <p className="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">{label}</p>
            <div className="flex items-baseline gap-2">
                <span className="text-2xl font-black text-white tracking-tighter">{value}</span>
            </div>
            <p className="text-[8px] font-bold uppercase tracking-widest text-slate-600 mt-1 italic">{sublabel}</p>
        </div>
    );
}
