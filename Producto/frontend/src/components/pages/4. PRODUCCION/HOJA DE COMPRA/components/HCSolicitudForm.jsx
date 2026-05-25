import React from 'react';
import {
    Save,
    ShoppingCart,
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
import { motion } from "framer-motion";

/**
 * Formulario para generar una HC desde una OP.
 *
 * El backend ÚNICAMENTE necesita `opId`. Los items se calculan automáticamente
 * a partir del Costeo asociado a la OP (cantidadRequerida = consumo × cantidadOP).
 * Por eso este formulario es minimalista — no se editan items a mano.
 */
export default function HCSolicitudForm({
    formData, setFormData, onCancel, onSave, isSubmitting,
}) {
    const opId = formData?.opId ?? '';
    const opIdValido = Number.isFinite(Number(opId)) && Number(opId) > 0;

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
                        <h2 className="text-3xl font-black text-slate-800 tracking-tight uppercase">Generar Hoja de Compra</h2>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] mt-1">A partir de una Orden de Producción aprobada</p>
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
                        disabled={isSubmitting || !opIdValido}
                        className="px-10 h-12 bg-indigo-600 hover:bg-slate-900 text-white rounded-2xl font-black text-xs uppercase tracking-widest shadow-2xl shadow-indigo-100 transition-all active:scale-95 flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <Save className="w-4 h-4" />
                        {isSubmitting ? 'Generando...' : 'Generar HC'}
                    </Button>
                </div>
            </div>

            {/* Form */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <div className="lg:col-span-2 space-y-8">
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-hidden">
                        <CardHeader className="p-10 pb-4 border-b border-slate-50">
                            <div className="flex items-center gap-4">
                                <div className="bg-indigo-50 p-2 rounded-xl">
                                    <Hash className="w-6 h-6 text-indigo-600" />
                                </div>
                                <div>
                                    <CardTitle className="text-sm font-black text-slate-800 uppercase tracking-[0.1em]">Orden de Producción Origen</CardTitle>
                                    <CardDescription className="text-[9px] font-bold uppercase tracking-widest text-slate-400 mt-1">La OP debe tener una versión de Costeo vinculada</CardDescription>
                                </div>
                            </div>
                        </CardHeader>
                        <CardContent className="p-10 space-y-6">
                            <div className="space-y-4">
                                <div className="flex items-center gap-3 px-1">
                                    <Hash className="w-4 h-4 text-indigo-600" />
                                    <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">ID Orden de Producción (opId)</label>
                                </div>
                                <Input
                                    type="number"
                                    min={1}
                                    className="h-14 bg-white/80 border-slate-100 rounded-2xl px-5 font-black text-sm text-slate-700"
                                    placeholder="Ej: 42"
                                    value={opId}
                                    onChange={(e) => setFormData({ ...formData, opId: e.target.value ? Number(e.target.value) : '' })}
                                />
                                <p className="text-[10px] text-slate-400 font-bold italic">
                                    Ingresa el ID numérico de la OP. El sistema cargará automáticamente los items desde el Costeo congelado.
                                </p>
                            </div>
                        </CardContent>
                    </Card>
                </div>

                <div className="space-y-8">
                    <Card className="rounded-[2.5rem] bg-slate-900 border-none shadow-2xl p-4 overflow-hidden relative">
                        <CardHeader className="p-8 pb-4 relative z-10">
                            <CardTitle className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.25em] mb-4">¿Cómo funciona?</CardTitle>
                        </CardHeader>
                        <CardContent className="p-8 pt-0 space-y-6 relative z-10">
                            <div className="space-y-3 text-[10px] text-slate-300 font-bold uppercase tracking-tight leading-relaxed">
                                <p>1. La OP debe tener una versión de Costeo vinculada.</p>
                                <p>2. El sistema lee los insumos del Costeo y multiplica el consumo por la cantidad total de la OP.</p>
                                <p>3. La HC nace en estado <span className="text-amber-400">BORRADOR</span>.</p>
                                <p>4. Una vez revisada, debes <span className="text-emerald-400">APROBARLA</span> para poder generar OCs.</p>
                            </div>

                            <div className="pt-6 border-t border-slate-800 flex items-start gap-4">
                                <AlertCircle className="w-5 h-5 text-amber-500 shrink-0" />
                                <p className="text-[9px] text-slate-400 font-bold leading-relaxed uppercase tracking-tighter italic">
                                    Nota: No es posible regenerar una HC si la OP ya tiene una asociada.
                                </p>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="rounded-[2.5rem] bg-white/40 backdrop-blur-md border border-white/50 p-8 space-y-3 shadow-xl shadow-slate-200/50">
                        <div className="flex items-center gap-3">
                            <Info className="w-4 h-4 text-indigo-500" />
                            <h4 className="text-[10px] font-black text-slate-800 uppercase tracking-widest">Validaciones</h4>
                        </div>
                        <ul className="text-[10px] text-slate-500 font-bold uppercase tracking-tight leading-relaxed list-disc pl-5 space-y-1">
                            <li>OP existente</li>
                            <li>OP con costeoVersionId no nulo</li>
                            <li>OP sin HC previa (relación 1:1)</li>
                        </ul>
                    </Card>
                </div>
            </div>
        </motion.div>
    );
}
