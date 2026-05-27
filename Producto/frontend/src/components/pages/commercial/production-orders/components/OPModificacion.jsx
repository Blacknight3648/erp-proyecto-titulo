import React from 'react';
import { 
    Save, 
    X, 
    Calendar, 
    AlertCircle, 
    ClipboardList, 
    Clock,
    User,
    CheckCircle2,
    Lock,
    TriangleAlert
} from 'lucide-react';
import { Button } from "../../../../ui/button";
import { 
    Card, 
    CardContent, 
    CardHeader, 
    CardTitle, 
    CardDescription 
} from "../../../../ui/card";
import { Badge } from "../../../../ui/badge";
import { Input } from "../../../../ui/input";
import { Textarea } from "../../../../ui/textarea";
import { motion } from "framer-motion";

export default function OPModificacion({ formData, setFormData, onCancel, onSave, isSubmitting }) {
    
    return (
        <motion.div 
            initial={{ opacity: 0, scale: 0.98 }}
            animate={{ opacity: 1, scale: 1 }}
            className="space-y-8 pb-20"
        >
            {/* Critical Edit Banner */}
            <div className="relative overflow-hidden bg-amber-500/10 backdrop-blur-xl border-2 border-amber-500/20 rounded-[2.5rem] p-8 flex flex-col md:flex-row items-center gap-6 shadow-2xl shadow-amber-900/10">
                <div className="absolute top-0 right-0 p-4 opacity-5">
                    <TriangleAlert className="w-40 h-40 text-amber-500" />
                </div>
                
                <div className="bg-amber-500 p-4 rounded-3xl shadow-lg shadow-amber-500/20 animate-pulse">
                    <TriangleAlert className="w-8 h-8 text-white" />
                </div>
                <div className="flex-1 relative z-10 text-center md:text-left">
                    <h2 className="text-2xl font-black text-amber-900 tracking-tight uppercase">Control de Modificación Crítica</h2>
                    <p className="text-[10px] text-amber-700 font-bold uppercase tracking-[0.2em] mt-2 italic">
                        AVISO: La persistencia de estos datos reemplazará permanentemente el registro histórico de producción.
                    </p>
                </div>
                <div className="flex gap-3 relative z-10">
                    <Button
                        variant="outline"
                        onClick={onCancel}
                        className="bg-white border-amber-200 text-amber-700 rounded-2xl h-12 px-6 font-black text-xs uppercase tracking-widest hover:bg-amber-50 transition-all shadow-sm active:scale-95"
                    >
                        Descartar
                    </Button>
                    <Button
                        onClick={onSave}
                        disabled={isSubmitting}
                        className="bg-amber-600 hover:bg-amber-700 text-white rounded-2xl h-12 px-8 font-black text-xs uppercase tracking-widest shadow-xl shadow-amber-200 transition-all active:scale-95 flex items-center gap-2"
                    >
                        <Save className="w-4 h-4" />
                        {isSubmitting ? 'Sincronizando...' : 'Confirmar Cambios'}
                    </Button>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Editable Section Area */}
                <div className="lg:col-span-2 space-y-8">
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-hidden">
                        <CardHeader className="p-8 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-3">
                                <div className="bg-indigo-50 p-2 rounded-xl">
                                    <ClipboardList className="w-5 h-5 text-indigo-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-xs font-black text-slate-800 uppercase tracking-widest">Atributos Editables</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-tighter text-slate-400 mt-1">Sincronización con planificación de planta</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-8 space-y-8">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                                <ModField icon={Calendar} label="Actualizar Compromiso Entrega">
                                    <Input 
                                        type="date" 
                                        className="h-12 bg-white border-slate-100 rounded-2xl px-4 font-black text-xs uppercase text-slate-700 focus:ring-2 focus:ring-indigo-500 transition-all"
                                        value={formData.fechaEntregaProgramada || ''}
                                        onChange={(e) => setFormData({...formData, fechaEntregaProgramada: e.target.value})}
                                    />
                                </ModField>

                                <ModField icon={CheckCircle2} label="Reajuste Status Operativo">
                                    <select 
                                        className="w-full h-12 bg-white border border-slate-200 rounded-2xl px-4 text-xs font-black uppercase text-slate-700 focus:ring-2 focus:ring-indigo-500 transition-all outline-none appearance-none cursor-pointer"
                                        value={formData.estado || 'PENDIENTE'}
                                        onChange={(e) => setFormData({...formData, estado: e.target.value})}
                                    >
                                        <option value="PENDIENTE">PENDIENTE</option>
                                        <option value="EN_PROCESO">EN PROCESO</option>
                                        <option value="PAUSADO">PAUSADO</option>
                                        <option value="COMPLETADO">COMPLETADO</option>
                                    </select>
                                </ModField>
                            </div>

                            <ModField icon={ClipboardList} label="Observaciones Finales (Overwrite Mode)">
                                <Textarea 
                                    rows={5}
                                    className="w-full bg-white border-slate-100 rounded-[1.5rem] p-6 text-xs text-slate-600 italic font-medium focus-visible:ring-indigo-500 transition-all resize-none shadow-inner"
                                    value={formData.observaciones || ''}
                                    onChange={(e) => setFormData({...formData, observaciones: e.target.value})}
                                />
                            </ModField>
                        </CardContent>
                    </Card>

                    <div className="bg-slate-50 border border-slate-100 rounded-[2rem] p-8 flex items-center gap-4 group">
                        <Lock className="w-5 h-5 text-slate-400 group-hover:text-indigo-500 transition-colors" />
                        <p className="text-[10px] text-slate-400 font-black uppercase tracking-widest italic group-hover:text-slate-600 transition-colors">
                            RESTRICCIÓN: La modificación de ítems (tallas/cantidades) está bloqueada para garantizar la coherencia con el pedido comercial de origen.
                        </p>
                    </div>
                </div>

                {/* Fixed Data Sidebar */}
                <div className="space-y-8">
                    <Card className="rounded-[2.5rem] bg-slate-900 border-none shadow-2xl p-2 overflow-hidden border">
                        <CardHeader className="p-8 pb-4">
                            <CardTitle className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mb-4">Metadatos de Solo Lectura</CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-6">
                            <ReadOnlyStat icon={User} label="Mandante Comercial" value={formData.cliente} />
                            <ReadOnlyStat icon={Hash} label="Folio Registro" value={formData.id || formData.idOP} />
                            <ReadOnlyStat icon={Clock} label="Hito de Apertura" value={formData.fechaInicio || "No registrado"} />
                        </CardContent>
                    </Card>
                </div>
            </div>
        </motion.div>
    );
}

function ModField({ icon: Icon, label, children }) {
    return (
        <div className="space-y-3 group">
            <div className="flex items-center gap-2 px-1">
                <Icon className="w-3.5 h-3.5 text-indigo-600 group-focus-within:scale-110 transition-transform" />
                <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.15em]">{label}</label>
            </div>
            {children}
        </div>
    );
}

function ReadOnlyStat({ icon: Icon, label, value }) {
    return (
        <div className="flex items-center gap-4 py-3 border-l-2 border-slate-800 pl-5 group hover:border-indigo-600 transition-colors">
            <div className="bg-slate-800 p-2 rounded-xl group-hover:bg-indigo-900 transition-colors">
                <Icon className="w-3.5 h-3.5 text-slate-500 group-hover:text-indigo-400 transition-colors" />
            </div>
            <div>
                <p className="text-[8px] font-black text-slate-500 uppercase tracking-[0.15em] mb-0.5">{label}</p>
                <p className="text-xs font-black italic text-slate-100 uppercase tracking-tight">{value}</p>
            </div>
        </div>
    );
}

function Hash(props) {
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
            <line x1="4" x2="20" y1="9" y2="9" />
            <line x1="4" x2="20" y1="15" y2="15" />
            <line x1="10" x2="8" y1="3" y2="21" />
            <line x1="16" x2="14" y1="3" y2="21" />
        </svg>
    );
}
