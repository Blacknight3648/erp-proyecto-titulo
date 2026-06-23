import React, { useState, useEffect, useRef } from 'react';
import {
    Save,
    ShoppingCart,
    Info,
    AlertCircle,
    AlertTriangle,
    CheckCircle2,
    Hash,
    Search,
    ChevronDown
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
import { motion, AnimatePresence } from "framer-motion";
import { OrdenProduccionService } from '../../../../../remote/service/OrdenProduccionService';
import { HojaCompraService } from '../../../../../remote/service/HojaCompraService';

/**
 * Formulario para generar una HC desde una OP.
 *
 * El backend ÚNICAMENTE necesita `opId`. Los items se calculan automáticamente
 * a partir del Costeo asociado a la OP (cantidadRequerida = consumo × cantidadOP).
 * Este formulario es minimalista — no se editan items a mano.
 */
export default function HCSolicitudForm({
    formData, setFormData, onCancel, onSave, isSubmitting,
}) {
    const opId = formData?.opId ?? '';
    const opIdValido = Number.isFinite(Number(opId)) && Number(opId) > 0;

    const [availableOPs, setAvailableOPs] = useState([]);
    const [existingHCs, setExistingHCs] = useState([]);
    const [loading, setLoading] = useState(false);
    const [opSearch, setOpSearch] = useState('');
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const [ops, hcs] = await Promise.all([
                    OrdenProduccionService.getAll(),
                    HojaCompraService.getAll()
                ]);
                setAvailableOPs(ops || []);
                setExistingHCs(hcs || []);
            } catch (error) {
                console.error("Error loading data in form:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    // Sincronizar el texto del input cuando cambia opId en formData
    useEffect(() => {
        if (opId && availableOPs.length > 0) {
            const op = availableOPs.find(o => Number(o.idOP) === Number(opId));
            if (op) {
                // Si el input no está enfocado, colocamos el formato bonito
                if (document.activeElement !== document.getElementById('op-combobox-input')) {
                    setOpSearch(`#${op.idOP} - ${op.numeroOP || `OP-${op.idOP}`}`);
                }
            } else {
                setOpSearch(String(opId));
            }
        } else if (!opId) {
            setOpSearch('');
        }
    }, [opId, availableOPs]);

    const handleInputChange = (val) => {
        setOpSearch(val);
        setIsDropdownOpen(true);

        const cleanedVal = val.trim();
        // Si escribe un ID exacto o número de OP exacto de la lista
        const matched = availableOPs.find(op => 
            String(op.idOP) === cleanedVal || 
            (op.numeroOP && op.numeroOP.toLowerCase() === cleanedVal.toLowerCase())
        );

        if (matched) {
            setFormData({ ...formData, opId: matched.idOP });
        } else if (/^\d+$/.test(cleanedVal)) {
            setFormData({ ...formData, opId: Number(cleanedVal) });
        } else {
            setFormData({ ...formData, opId: '' });
        }
    };

    const handleSelectOP = (op) => {
        setFormData({ ...formData, opId: op.idOP });
        setOpSearch(`#${op.idOP} - ${op.numeroOP || `OP-${op.idOP}`}`);
        setIsDropdownOpen(false);
    };

    // Filtrado de la lista según el input del usuario
    const filteredOPs = availableOPs.filter(op => {
        const search = opSearch.toLowerCase().replace(/^#/, '');
        if (!search || search.includes(' - ')) return true; // Mostrar todos si está vacío o ya seleccionado
        return (
            String(op.idOP || '').toLowerCase().includes(search) ||
            String(op.numeroOP || '').toLowerCase().includes(search) ||
            String(op.notaVentaId || '').toLowerCase().includes(search) ||
            String(op.estado || '').toLowerCase().includes(search)
        );
    });

    // Cálculos y validaciones para la vista previa
    const selectedOP = availableOPs.find(op => Number(op.idOP) === Number(opId));
    const yaTieneHC = opIdValido && existingHCs.some(hc => Number(hc.opId) === Number(opId));
    const tieneCosteo = selectedOP ? selectedOP.costeoVersionId != null : false;
    const canSubmit = opIdValido && selectedOP && tieneCosteo && !yaTieneHC;

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
                        disabled={isSubmitting || !canSubmit}
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
                    <Card className="rounded-[2.5rem] border-white/40 bg-white/60 backdrop-blur-md shadow-2xl shadow-slate-200/50 border overflow-visible">
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
                        <CardContent className="p-10 space-y-8 overflow-visible">
                            <div className="space-y-4 relative" ref={dropdownRef}>
                                <div className="flex items-center justify-between">
                                    <div className="flex items-center gap-3 px-1">
                                        <Hash className="w-4 h-4 text-indigo-600" />
                                        <label className="text-[9px] font-black text-slate-400 uppercase tracking-[0.2em]">Escribir o Buscar OP disponible</label>
                                    </div>
                                    {loading && (
                                        <span className="text-[9px] text-indigo-500 font-black uppercase tracking-wider animate-pulse">Cargando datos...</span>
                                    )}
                                </div>
                                
                                <div className="relative">
                                    <Input
                                        id="op-combobox-input"
                                        type="text"
                                        className="h-14 bg-white/80 border-slate-100 rounded-2xl px-5 pr-12 font-black text-sm text-slate-700 shadow-sm transition-all focus:border-indigo-300 focus:bg-white"
                                        placeholder="Ej: 42 o busque por código de OP..."
                                        value={opSearch}
                                        onChange={(e) => handleInputChange(e.target.value)}
                                        onFocus={() => setIsDropdownOpen(true)}
                                        onBlur={() => {
                                            // Delay para permitir que se ejecute el evento click/mousedown de las opciones
                                            setTimeout(() => setIsDropdownOpen(false), 200);
                                        }}
                                    />
                                    <div className="absolute right-4 top-1/2 -translate-y-1/2 flex items-center gap-2 text-slate-400 pointer-events-none">
                                        <Search className="w-5 h-5" />
                                        <ChevronDown className={`w-4 h-4 transition-transform duration-200 ${isDropdownOpen ? 'rotate-180' : ''}`} />
                                    </div>

                                    {/* Panel Desplegable Flotante */}
                                    <AnimatePresence>
                                        {isDropdownOpen && (
                                            <motion.div
                                                initial={{ opacity: 0, y: 10 }}
                                                animate={{ opacity: 1, y: 0 }}
                                                exit={{ opacity: 0, y: 10 }}
                                                transition={{ duration: 0.15 }}
                                                className="absolute z-50 w-full mt-2 bg-white/95 backdrop-blur-md border border-slate-150 rounded-2xl shadow-2xl overflow-hidden max-h-60 overflow-y-auto custom-scrollbar"
                                            >
                                                {filteredOPs.length > 0 ? (
                                                    filteredOPs.map(op => {
                                                        const isSelected = Number(op.idOP) === Number(opId);
                                                        return (
                                                            <div
                                                                key={op.idOP}
                                                                onMouseDown={() => handleSelectOP(op)}
                                                                className={`px-5 py-3.5 hover:bg-slate-50 flex items-center justify-between cursor-pointer border-b border-slate-100 transition-colors last:border-0 ${
                                                                    isSelected ? 'bg-indigo-50/40 text-indigo-700 font-extrabold' : ''
                                                                }`}
                                                            >
                                                                <div className="flex items-center gap-3">
                                                                    <div className={`w-8 h-8 rounded-lg flex items-center justify-center font-black text-xs ${
                                                                        isSelected ? 'bg-indigo-600 text-white shadow-sm' : 'bg-slate-100 text-slate-500'
                                                                    }`}>
                                                                        #{op.idOP}
                                                                    </div>
                                                                    <div>
                                                                        <span className="text-xs font-black text-slate-800 tracking-tight block">
                                                                            {op.numeroOP || `OP-${op.idOP}`}
                                                                        </span>
                                                                        <span className="text-[8px] font-bold text-slate-400 uppercase tracking-widest block mt-0.5">
                                                                            Nota Venta: #{op.notaVentaId || '—'}
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                                <span className={`px-2 py-0.5 rounded-full text-[8px] font-black uppercase tracking-widest ${
                                                                    op.estado === 'APROBADA'
                                                                        ? 'bg-emerald-50 text-emerald-700 border border-emerald-100'
                                                                        : op.estado === 'BORRADOR'
                                                                            ? 'bg-amber-50 text-amber-700 border border-amber-100'
                                                                            : 'bg-slate-50 text-slate-500 border border-slate-150'
                                                                }`}>
                                                                    {op.estado}
                                                                </span>
                                                            </div>
                                                        );
                                                    })
                                                ) : (
                                                    <div className="py-6 text-center text-xs font-bold text-slate-400 uppercase tracking-widest">
                                                        No se encontraron OPs disponibles
                                                    </div>
                                                )}
                                            </motion.div>
                                        )}
                                    </AnimatePresence>
                                </div>
                            </div>

                            {/* Vista Previa y Validaciones en Tiempo Real */}
                            {opIdValido && (
                                <motion.div
                                    initial={{ opacity: 0, scale: 0.98 }}
                                    animate={{ opacity: 1, scale: 1 }}
                                    className="pt-6 border-t border-slate-100"
                                >
                                    {selectedOP ? (
                                        <div className="rounded-2xl border border-slate-150 p-6 bg-slate-50/50 space-y-4 transition-all">
                                            <div className="flex justify-between items-start">
                                                <div>
                                                    <h4 className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Vista Previa de la OP</h4>
                                                    <h3 className="text-base font-black text-slate-800 tracking-tight mt-1">
                                                        {selectedOP.numeroOP || `OP-${selectedOP.idOP}`}
                                                    </h3>
                                                    <p className="text-[9px] font-bold text-slate-500 uppercase tracking-wider mt-1">
                                                        ID Sistema: #{selectedOP.idOP} | Nota Venta: #{selectedOP.notaVentaId || '—'}
                                                    </p>
                                                </div>
                                                <div className="flex flex-col items-end gap-1.5">
                                                    <span className={`px-2.5 py-1 rounded-full text-[8px] font-black uppercase tracking-widest ${
                                                        selectedOP.estado === 'APROBADA'
                                                            ? 'bg-emerald-50 text-emerald-700 border border-emerald-100'
                                                            : 'bg-slate-50 text-slate-500 border border-slate-150'
                                                    }`}>
                                                        {selectedOP.estado}
                                                    </span>
                                                </div>
                                            </div>

                                            {/* Validaciones específicas */}
                                            <div className="space-y-2.5 pt-2 border-t border-slate-200/50">
                                                {/* Validación 1: Versión de Costeo */}
                                                <div className="flex items-center gap-2.5 text-xs">
                                                    {tieneCosteo ? (
                                                        <CheckCircle2 className="w-4.5 h-4.5 text-emerald-500 shrink-0" />
                                                    ) : (
                                                        <AlertTriangle className="w-4.5 h-4.5 text-amber-500 shrink-0" />
                                                    )}
                                                    <span className={`font-bold uppercase tracking-wide text-[10px] ${tieneCosteo ? 'text-slate-600' : 'text-amber-700 font-extrabold'}`}>
                                                        {tieneCosteo 
                                                            ? `Costeo Vinculado: Versión #${selectedOP.costeoVersionId}`
                                                            : "Sin versión de costeo vinculada (Requerido para generar HC)"}
                                                    </span>
                                                </div>

                                                {/* Validación 2: HC Duplicada */}
                                                <div className="flex items-center gap-2.5 text-xs">
                                                    {!yaTieneHC ? (
                                                        <CheckCircle2 className="w-4.5 h-4.5 text-emerald-500 shrink-0" />
                                                    ) : (
                                                        <AlertCircle className="w-4.5 h-4.5 text-rose-500 shrink-0" />
                                                    )}
                                                    <span className={`font-bold uppercase tracking-wide text-[10px] ${!yaTieneHC ? 'text-slate-600' : 'text-rose-700 font-extrabold'}`}>
                                                        {!yaTieneHC 
                                                            ? "Sin Hoja de Compra previa (Relación 1:1 correcta)"
                                                            : "Esta OP ya cuenta con una Hoja de Compra asociada"}
                                                    </span>
                                                </div>
                                            </div>

                                            {/* Indicador Final de Habilitación */}
                                            {canSubmit ? (
                                                <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-3 flex items-center gap-2 text-emerald-800 text-[10px] font-black uppercase tracking-wider">
                                                    <CheckCircle2 className="w-4 h-4 shrink-0 text-emerald-600" />
                                                    <span>OP válida y aprobada. Listo para generar la Hoja de Compra.</span>
                                                </div>
                                            ) : (
                                                <div className="bg-rose-50 border border-rose-200 rounded-xl p-3 flex items-center gap-2 text-rose-800 text-[10px] font-black uppercase tracking-wider">
                                                    <AlertCircle className="w-4 h-4 shrink-0 text-rose-600" />
                                                    <span>No cumple con los requisitos mínimos del sistema.</span>
                                                </div>
                                            )}
                                        </div>
                                    ) : (
                                        <div className="rounded-2xl border border-rose-100 p-5 bg-rose-50/20 flex items-center gap-3">
                                            <AlertTriangle className="w-5 h-5 text-rose-500 shrink-0" />
                                            <div>
                                                <p className="text-[10px] font-black text-rose-800 uppercase tracking-widest">OP no encontrada</p>
                                                <p className="text-[9px] font-bold text-rose-500 uppercase tracking-wider mt-0.5">
                                                    El ID #{opId} no corresponde a ninguna Orden de Producción registrada.
                                                </p>
                                            </div>
                                        </div>
                                    )}
                                </motion.div>
                            )}
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
