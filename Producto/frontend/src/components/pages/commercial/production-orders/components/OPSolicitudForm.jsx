import React from 'react';
import { 
    Save, 
    X, 
    User, 
    Tag, 
    Calendar, 
    Package, 
    Plus, 
    Trash2,
    Briefcase,
    BadgePercent,
    Calculator,
    Info,
    AlertCircle
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

export default function OPSolicitudForm({ 
    formData, 
    setFormData, 
    onCancel, 
    onSave, 
    isSubmitting, 
    clientes, 
    nextNumbers,
    formatCLP 
}) {
    
    const addItem = () => {
        const newItems = [...(formData.items || []), { 
            nombreProducto: '', 
            talla: '', 
            color: '', 
            cantidad: 0, 
            precioEstimado: 0 
        }];
        setFormData({ ...formData, items: newItems });
    };

    const removeItem = (index) => {
        const newItems = formData.items.filter((_, i) => i !== index);
        setFormData({ ...formData, items: newItems });
    };

    const updateItem = (index, field, value) => {
        const newItems = [...formData.items];
        newItems[index][field] = value;
        setFormData({ ...formData, items: newItems });
    };

    const totalUnits = (formData.items || []).reduce((acc, curr) => acc + (parseInt(curr.cantidad) || 0), 0);
    const totalAmount = (formData.items || []).reduce((acc, curr) => acc + ((parseInt(curr.cantidad) || 0) * (parseInt(curr.precioEstimado) || 0)), 0);

    return (
        <motion.div 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-8 pb-20"
        >
            {/* Header / Action Bar */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 bg-white/40 backdrop-blur-xl p-6 rounded-[2rem] border border-white/20 shadow-2xl shadow-indigo-100/20">
                <div className="flex items-center gap-4">
                    <div className="bg-indigo-600 p-3 rounded-2xl shadow-lg shadow-indigo-200">
                        <Plus className="w-6 h-6 text-white" />
                    </div>
                    <div>
                        <h2 className="text-2xl font-black text-slate-800 tracking-tight uppercase">Nueva Solicitud OP</h2>
                        <p className="text-[10px] text-slate-400 font-black uppercase tracking-[0.2em] mt-1">
                            Apertura de Orden de Producción Comercial
                        </p>
                    </div>
                </div>
                <div className="flex items-center gap-3">
                    <Button
                        variant="ghost"
                        onClick={onCancel}
                        className="rounded-2xl px-6 font-black text-xs uppercase tracking-widest text-slate-400 hover:text-slate-600 transition-all"
                    >
                        Cancelar
                    </Button>
                    <Button
                        onClick={onSave}
                        disabled={isSubmitting}
                        className="bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl px-8 h-12 font-black text-xs uppercase tracking-widest shadow-2xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2"
                    >
                        <Save className="w-4 h-4" />
                        {isSubmitting ? 'Procesando...' : 'Emitir Orden'}
                    </Button>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Main Form Area */}
                <div className="lg:col-span-2 space-y-8">
                    {/* General Information Card */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-hidden">
                        <CardHeader className="p-8 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-3">
                                <div className="bg-slate-100 p-2 rounded-xl">
                                    <Briefcase className="w-5 h-5 text-slate-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-xs font-black text-slate-800 uppercase tracking-widest">Identificadores del Negocio</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-tighter text-slate-400 mt-1">Referencia y asignación de cliente</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-8 grid grid-cols-1 md:grid-cols-2 gap-8">
                            <FormField icon={User} label="Cliente Responsable">
                                <select 
                                    className="w-full h-12 bg-white border border-slate-100 rounded-2xl px-4 text-xs font-black uppercase text-slate-700 focus:ring-2 focus:ring-indigo-500 transition-all outline-none appearance-none cursor-pointer"
                                    value={formData.clienteId || ''}
                                    onChange={(e) => setFormData({...formData, clienteId: e.target.value, cliente: e.target.options[e.target.selectedIndex].text})}
                                >
                                    <option value="">Seleccione Entidad...</option>
                                    {clientes?.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
                                </select>
                            </FormField>

                            <FormField icon={Tag} label="Vínculo Nota de Venta (NV)">
                                <Input 
                                    placeholder="ID O NUMERO DE NV"
                                    className="h-12 bg-white border-slate-100 rounded-2xl px-4 font-black text-xs uppercase placeholder:text-slate-300"
                                    value={formData.notaVentaId || ''}
                                    onChange={(e) => setFormData({...formData, notaVentaId: e.target.value})}
                                />
                            </FormField>

                            <FormField icon={Calendar} label="Compromiso Ético/Entrega">
                                <Input 
                                    type="date" 
                                    className="h-12 bg-white border-slate-100 rounded-2xl px-4 font-black text-xs uppercase"
                                    value={formData.fechaEntregaProgramada || ''}
                                    onChange={(e) => setFormData({...formData, fechaEntregaProgramada: e.target.value})}
                                />
                            </FormField>

                            <FormField icon={Package} label="Identificador Sugerido">
                                <div className="h-12 flex items-center px-5 bg-indigo-50/50 border border-indigo-100 rounded-2xl text-xs font-black text-indigo-600 tracking-widest italic animate-pulse">
                                    {nextNumbers?.op || 'AUTO-FOLIO'}
                                </div>
                            </FormField>
                        </CardContent>
                    </Card>

                    {/* Breakdown Calculation Area */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-hidden">
                        <CardHeader className="p-8 pb-4 border-b border-slate-50 flex flex-row items-center justify-between">
                            <div className="flex items-center gap-3">
                                <div className="bg-slate-100 p-2 rounded-xl">
                                    <Calculator className="w-5 h-5 text-slate-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-xs font-black text-slate-800 uppercase tracking-widest">Matriz de Itemizado</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-tighter text-slate-400 mt-1">Configuración técnica de prendas</CardDescription>
                                </div>
                            </div>
                            <Button 
                                variant="outline"
                                onClick={addItem}
                                className="rounded-xl border-indigo-100 text-indigo-600 hover:bg-indigo-50 font-black text-[10px] uppercase tracking-widest h-9"
                            >
                                <Plus className="w-3.5 h-3.5 mr-2" />
                                Agregar Item
                            </Button>
                        </CardHeader>
                        <CardContent className="p-8 space-y-4">
                            <div className="space-y-4">
                                <AnimatePresence mode='popLayout'>
                                    {formData.items?.map((item, index) => (
                                        <motion.div 
                                            initial={{ opacity: 0, x: -10 }}
                                            animate={{ opacity: 1, x: 0 }}
                                            exit={{ opacity: 0, scale: 0.95 }}
                                            key={index} 
                                            className="group relative grid grid-cols-12 gap-4 p-6 bg-white rounded-3xl border border-slate-100 hover:border-indigo-200 hover:shadow-lg transition-all items-end"
                                        >
                                            <div className="col-span-5 space-y-1.5">
                                                <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest ml-1 flex items-center gap-1">
                                                    <Info className="w-2.5 h-2.5" />
                                                    Producto / Descripción
                                                </label>
                                                <Input 
                                                    className="h-11 bg-slate-50/50 border-none rounded-xl px-4 text-xs font-black text-slate-800 uppercase focus-visible:ring-indigo-100"
                                                    value={item.nombreProducto}
                                                    onChange={(e) => updateItem(index, 'nombreProducto', e.target.value)}
                                                    placeholder="EJ: POLERA TACTO ALGODON"
                                                />
                                            </div>
                                            <div className="col-span-2 space-y-1.5">
                                                <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest text-center block">Talla</label>
                                                <Input 
                                                    className="h-11 bg-slate-50/50 border-none rounded-xl px-2 text-xs font-black text-center text-slate-700 uppercase focus-visible:ring-indigo-100"
                                                    value={item.talla}
                                                    onChange={(e) => updateItem(index, 'talla', e.target.value)}
                                                />
                                            </div>
                                            <div className="col-span-2 space-y-1.5">
                                                <label className="text-[8px] font-black text-slate-400 uppercase tracking-widest text-center block">Color</label>
                                                <Input 
                                                    className="h-11 bg-slate-50/50 border-none rounded-xl px-2 text-xs font-black text-center text-slate-500 uppercase focus-visible:ring-indigo-100"
                                                    value={item.color}
                                                    onChange={(e) => updateItem(index, 'color', e.target.value)}
                                                />
                                            </div>
                                            <div className="col-span-2 space-y-1.5">
                                                <label className="text-[8px] font-black text-indigo-400 uppercase tracking-widest text-center block italic underline underline-offset-4 decoration-indigo-100">Cantidad</label>
                                                <Input 
                                                    type="number"
                                                    className="h-11 bg-indigo-50/30 border-none rounded-xl px-2 text-xs font-black text-center text-indigo-600 focus-visible:ring-indigo-200"
                                                    value={item.cantidad}
                                                    onChange={(e) => updateItem(index, 'cantidad', parseInt(e.target.value) || 0)}
                                                />
                                            </div>
                                            <div className="col-span-1 flex justify-center pb-1">
                                                <Button 
                                                    variant="ghost" 
                                                    size="icon"
                                                    onClick={() => removeItem(index)}
                                                    className="text-slate-300 hover:text-red-500 hover:bg-red-50 rounded-xl transition-all"
                                                >
                                                    <Trash2 className="w-4 h-4" />
                                                </Button>
                                            </div>
                                        </motion.div>
                                    ))}
                                </AnimatePresence>
                                
                                {(!formData.items || formData.items.length === 0) && (
                                    <div className="py-20 text-center bg-slate-50/50 rounded-[2rem] border border-dashed border-slate-100">
                                        <AlertCircle className="w-8 h-8 text-slate-300 mx-auto mb-3" />
                                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Inicie agregando líneas de producción</p>
                                    </div>
                                )}
                            </div>
                        </CardContent>
                    </Card>
                </div>

                {/* Tracking Area Sidebar */}
                <div className="space-y-8">
                    {/* Notes Config Card */}
                    <Card className="rounded-[2.5rem] bg-slate-900 border-none shadow-2xl shadow-indigo-200/50 p-2 overflow-hidden border">
                        <CardHeader className="p-8 pb-4">
                             <CardTitle className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.2em] mb-4 flex items-center gap-2">
                                <MessageSquareQuote className="w-4 h-4" />
                                Observaciones Críticas
                             </CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-6">
                             <Textarea 
                                rows={6}
                                placeholder="Especifique detalles técnicos, requerimientos de tela o urgencias..."
                                className="w-full bg-indigo-800/20 border-indigo-700/30 rounded-[1.5rem] p-5 text-xs text-indigo-50 font-medium italic placeholder:text-indigo-400 focus-visible:ring-indigo-500 focus-visible:ring-offset-0 border-none transition-all resize-none shadow-inner"
                                value={formData.observaciones || ''}
                                onChange={(e) => setFormData({...formData, observaciones: e.target.value})}
                             />
                             <div className="p-5 bg-indigo-400/10 rounded-2xl border border-indigo-400/20 flex gap-3">
                                <AlertCircle className="w-4 h-4 text-indigo-300 shrink-0 mt-0.5" />
                                <p className="text-[9px] text-indigo-300 font-bold uppercase leading-relaxed italic">
                                    Esta información será la base para la planificación en planta. Sea preciso.
                                </p>
                             </div>
                        </CardContent>
                    </Card>

                    {/* Summary Visualization Card (The "Excel" feedback) */}
                    <Card className="rounded-[2.5rem] border-white/40 bg-white shadow-2xl shadow-slate-200/40 p-1 border">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">Resumen de Carga</CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-2 space-y-6">
                            <div className="text-center">
                                <p className="text-[10px] font-black text-indigo-500 uppercase tracking-widest mb-1 italic">Total Unidades</p>
                                <div className="text-5xl font-black text-slate-800 tracking-tighter tabular-nums">
                                    {totalUnits}
                                </div>
                                <Badge className="bg-slate-100 text-slate-500 border-none mt-4 font-black text-[9px] tracking-widest px-3">
                                    ESTIMACIÓN PRE-PLANTA
                                </Badge>
                            </div>
                            
                            <div className="pt-6 border-t border-slate-50 space-y-3">
                                <div className="flex justify-between items-center text-[9px] font-black text-slate-400 uppercase">
                                    <span>Líneas Activas</span>
                                    <span className="text-slate-700">{formData.items?.length || 0}</span>
                                </div>
                                <div className="flex justify-between items-center text-[9px] font-black text-slate-400 uppercase">
                                    <span>Densidad Lote</span>
                                    <span className="text-slate-700 font-black">{totalUnits > 100 ? 'ALTA' : 'NORMAL'}</span>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </div>
            </div>
        </motion.div>
    );
}

function FormField({ icon: Icon, label, children }) {
    return (
        <div className="space-y-3 group">
            <div className="flex items-center gap-2 px-1">
                <Icon className="w-3.5 h-3.5 text-indigo-500 group-focus-within:scale-110 transition-transform" />
                <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.15em]">{label}</label>
            </div>
            {children}
        </div>
    );
}

function MessageSquareQuote(props) {
    return (
        <svg
            {...props}
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
        >
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
            <path d="m8 11 2 2 4-4" />
        </svg>
    );
}
