import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import {
    AlertTriangle,
    X,
    CheckCircle2,
    Box,
    Calculator,
    DollarSign,
    FileText
} from 'lucide-react';
import { Button } from "../../../ui/button";
import { Badge } from "../../../ui/badge";

interface ModificarItemModalProps {
    isOpen: boolean;
    onClose: () => void;
    item: any | null;
    idHC: number;
    onSave: (idHC: number, idHCItem: number, payload: { cantidadAComprar: number; precioUnitarioRef: number; justificacionModificacion: string; cantidadStock?: number }) => Promise<void>;
    formatCLP: (val: number) => string;
}

export const ModificarItemModal: React.FC<ModificarItemModalProps> = ({
    isOpen,
    onClose,
    item,
    idHC,
    onSave,
    formatCLP
}) => {
    const [cantidadAComprar, setCantidadAComprar] = useState<number | string>('');
    const [precioUnitarioRef, setPrecioUnitarioRef] = useState<number | string>('');
    const [cantidadStock, setCantidadStock] = useState<number | string>('');
    const [justificacion, setJustificacion] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (item) {
            const cantInicial = item.cantidadAComprar !== null && item.cantidadAComprar !== undefined
                ? item.cantidadAComprar
                : item.cantidadRequerida || 0;
            setCantidadAComprar(Number(Number(cantInicial).toFixed(2)));
            setPrecioUnitarioRef(Number(Number(item.precioEstimado || item.precioUnitarioRef || 0).toFixed(2)));
            setCantidadStock(Number(Number(item.cantidadStock !== null && item.cantidadStock !== undefined ? item.cantidadStock : 0).toFixed(2)));
            setJustificacion(item.justificacionModificacion || '');
            setError(null);
        }
    }, [item]);

    if (!isOpen || !item) return null;

    const cantOriginal = Number(Number(item.cantidadRequerida || 0).toFixed(2));
    const cantStock = Number(Number(cantidadStock || 0).toFixed(2));
    const cantActual = Number(Number(cantidadAComprar || 0).toFixed(2));
    const precioOriginal = Number(Number(item.precioEstimado || item.precioUnitarioRef || 0).toFixed(2));
    const precioActual = Number(Number(precioUnitarioRef || 0).toFixed(2));

    const isModified = cantActual !== cantOriginal || precioActual !== precioOriginal || cantStock > 0 || item.modificado;
    const isJustificationValid = justificacion.trim().length > 0;
    const canSubmit = !isModified || isJustificationValid;

    const handleStockChange = (val: string) => {
        setCantidadStock(val);
        const stockNum = Number(val) || 0;
        const nuevaCant = Number(Math.max(0, cantOriginal - stockNum).toFixed(2));
        setCantidadAComprar(nuevaCant);
        if (!justificacion.trim() && stockNum > 0) {
            setJustificacion(`Ajuste manual de stock en bodega (${stockNum.toFixed(2)} unid.) registrado por Jefatura de Producción.`);
        }
    };

    const applyStockDiscount = () => {
        const nuevaCant = Number(Math.max(0, cantOriginal - cantStock).toFixed(2));
        setCantidadAComprar(nuevaCant);
        if (!justificacion.trim()) {
            setJustificacion(`Se descuenta stock disponible en bodega (${cantStock.toFixed(2)} unid.) del requerimiento original.`);
        }
    };

    const resetToOriginal = () => {
        setCantidadAComprar(cantOriginal);
        setPrecioUnitarioRef(precioOriginal);
        setCantidadStock(0);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!canSubmit) return;
        setLoading(true);
        setError(null);
        try {
            await onSave(idHC, item.id, {
                cantidadAComprar: Number(Number(cantidadAComprar).toFixed(2)),
                precioUnitarioRef: Number(Number(precioUnitarioRef).toFixed(2)),
                justificacionModificacion: justificacion.trim(),
                cantidadStock: Number(Number(cantidadStock).toFixed(2))
            });
            onClose();
        } catch (err: any) {
            const backendMsg = err?.response?.data?.mensaje || err?.response?.data?.message;
            setError(backendMsg || err?.message || 'Ocurrió un error al guardar las modificaciones.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <AnimatePresence>
            {isOpen && (
                <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
                    <motion.div
                        initial={{ opacity: 0, scale: 0.95, y: 10 }}
                        animate={{ opacity: 1, scale: 1, y: 0 }}
                        exit={{ opacity: 0, scale: 0.95, y: 10 }}
                        transition={{ duration: 0.2 }}
                        className="relative w-full max-w-2xl bg-[#0b1329] border border-slate-800 rounded-2xl shadow-2xl overflow-hidden text-slate-100"
                    >
                        {/* Cabecera */}
                        <div className="flex items-center justify-between px-6 py-5 border-b border-slate-800/60 bg-[#0d1630]">
                            <div>
                                <h3 className="text-lg font-semibold tracking-tight text-white flex items-center gap-2">
                                    Validar y Modificar Ítem
                                    <Badge className="bg-slate-800 text-slate-300 border-slate-700 font-mono text-xs">
                                        ID #{item.id}
                                    </Badge>
                                </h3>
                                <p className="text-sm text-slate-400 mt-0.5">
                                    {item.insumo} <span className="text-slate-500">({item.tipoInsumo})</span>
                                </p>
                            </div>
                            <button
                                onClick={onClose}
                                className="p-2 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </div>

                        <form onSubmit={handleSubmit} className="p-6 space-y-6">
                            {/* Panel Informativo de Tarjetas */}
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                <div className="p-4 rounded-xl bg-slate-800/30 border border-slate-800 flex flex-col justify-between min-h-[100px]">
                                    <div className="flex items-center gap-2 text-[11px] font-bold text-slate-400 uppercase tracking-wider">
                                        <Calculator className="w-3.5 h-3.5 text-blue-400" />
                                        Demanda Costeo
                                    </div>
                                    <div className="mt-2 text-2xl font-bold text-white font-mono">
                                        {cantOriginal} <span className="text-xs font-normal text-slate-400">unid.</span>
                                    </div>
                                    <div className="mt-1 text-xs text-slate-500">
                                        Consumo × Cantidad OP
                                    </div>
                                </div>

                                <div className="p-4 rounded-xl bg-slate-800/30 border border-slate-800 flex flex-col justify-between min-h-[100px]">
                                    <div className="flex items-center gap-2 text-[11px] font-bold text-slate-400 uppercase tracking-wider">
                                        <Box className="w-3.5 h-3.5 text-emerald-400" />
                                        Stock en Bodega
                                    </div>
                                    <div className="mt-2 text-2xl font-bold text-emerald-400 font-mono">
                                        {cantStock} <span className="text-xs font-normal text-slate-400">unid.</span>
                                    </div>
                                    <div className="mt-1 text-xs text-slate-500">
                                        Sin stock registrado
                                    </div>
                                </div>

                                <div className="p-4 rounded-xl bg-slate-800/30 border border-slate-800 flex flex-col justify-between min-h-[100px]">
                                    <div className="flex items-center gap-2 text-[11px] font-bold text-slate-400 uppercase tracking-wider">
                                        <DollarSign className="w-3.5 h-3.5 text-amber-400" />
                                        Precio Ref. Costeo
                                    </div>
                                    <div className="mt-2 text-2xl font-bold text-amber-400 font-mono">
                                        {formatCLP(precioOriginal)}
                                    </div>
                                    <div className="mt-1 text-xs text-slate-500">
                                        Presupuestado en versión
                                    </div>
                                </div>
                            </div>

                            {/* Inputs de Modificación (Arreglado y Alineado) */}
                            <div className="p-5 rounded-xl bg-[#070d1e] border border-slate-800/80 space-y-4">
                                <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
                                    {/* Stock en bodega */}
                                    <div className="flex flex-col justify-between h-full space-y-2">
                                        <label className="text-[11px] font-bold text-emerald-400 uppercase tracking-wider flex flex-wrap justify-between items-center gap-1 min-h-[16px]">
                                            <span>STOCK EN BODEGA</span>
                                            <span className="text-slate-500 text-[10px] font-normal normal-case">AJUSTE MANUAL</span>
                                        </label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={cantidadStock}
                                            onChange={(e) => handleStockChange(e.target.value)}
                                            className="w-full bg-[#0d1630] border border-emerald-500/30 rounded-xl px-4 py-3 text-emerald-400 font-mono text-base focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all"
                                            placeholder="0"
                                        />
                                    </div>

                                    {/* Cantidad a comprar */}
                                    <div className="flex flex-col justify-between h-full space-y-2">
                                        <label className="text-[11px] font-bold text-slate-300 uppercase tracking-wider flex flex-wrap justify-between items-center gap-1 min-h-[16px]">
                                            <span>CANTIDAD A COMPRAR</span>
                                        </label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={cantidadAComprar}
                                            onChange={(e) => setCantidadAComprar(e.target.value)}
                                            className="w-full bg-[#121b36] border border-slate-700/60 rounded-xl px-4 py-3 text-white font-mono text-base focus:outline-none focus:border-slate-500 transition-all"
                                            required
                                        />
                                    </div>

                                    {/* Precio Ref */}
                                    <div className="flex flex-col justify-between h-full space-y-2">
                                        <label className="text-[11px] font-bold text-slate-300 uppercase tracking-wider flex flex-wrap justify-between items-center gap-1 min-h-[16px]">
                                            <span>PRECIO REF.</span>
                                            {precioActual !== precioOriginal && (
                                                <span className="text-amber-400 text-[10px] font-medium normal-case">DIFIERE COSTEO</span>
                                            )}
                                        </label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={precioUnitarioRef}
                                            onChange={(e) => setPrecioUnitarioRef(e.target.value)}
                                            className="w-full bg-[#121b36] border border-slate-700/60 rounded-xl px-4 py-3 text-white font-mono text-base focus:outline-none focus:border-slate-500 transition-all"
                                            required
                                        />
                                    </div>
                                </div>

                                {/* Enlace Restablecer */}
                                {isModified && (
                                    <div className="flex justify-end pt-1">
                                        <button
                                            type="button"
                                            onClick={resetToOriginal}
                                            className="text-xs text-slate-400 hover:text-amber-400 underline underline-offset-4 transition-colors"
                                        />
                                        Restablecer valores originales de costeo
                                    </div>
                                )}
                            </div>

                            {/* Campo de Justificación Obligatorio */}
                            <div className="space-y-2">
                                <label className="text-xs font-semibold text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
                                    <FileText className="w-4 h-4 text-amber-500" />
                                    <span>Justificación de Modificación</span>
                                    {isModified && (
                                        <span className="text-rose-500 font-bold text-[11px] normal-case">* (OBLIGATORIO AL MODIFICAR)</span>
                                    )}
                                </label>
                                <textarea
                                    rows={3}
                                    value={justificacion}
                                    onChange={(e) => setJustificacion(e.target.value)}
                                    placeholder="El proveedor no está disponible"
                                    className={`w-full bg-[#070d1e] border rounded-xl p-3.5 text-sm text-white placeholder-slate-600 focus:outline-none focus:ring-1 transition-all ${
                                        isModified && !isJustificationValid
                                            ? 'border-rose-500 focus:ring-rose-500 bg-rose-950/5'
                                            : 'border-slate-800 focus:ring-slate-700'
                                    }`}
                                />
                                {isModified && !isJustificationValid && (
                                    <p className="text-xs text-rose-400 flex items-center gap-1.5 mt-1">
                                        <AlertTriangle className="w-3.5 h-3.5 flex-shrink-0" />
                                        Debe justificar la diferencia para auditar correctamente los desvíos.
                                    </p>
                                )}
                            </div>

                            {/* Alerta de bloqueo/error inferior */}
                            {error && (
                                <div className="p-3.5 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 text-sm flex items-center gap-2.5">
                                    <AlertTriangle className="w-4 h-4 flex-shrink-0 text-rose-400" />
                                    <span>{error}</span>
                                </div>
                            )}

                            {/* Botones de Acción */}
                            <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-800/60">
                                <Button
                                    type="button"
                                    variant="outline"
                                    onClick={onClose}
                                    className="bg-slate-900 border-slate-800 text-slate-300 hover:bg-slate-800 hover:text-white rounded-xl px-5 h-11"
                                >
                                    Cancelar
                                </Button>
                                <Button
                                    type="submit"
                                    disabled={loading || !canSubmit}
                                    className="flex items-center gap-2 px-6 h-11 rounded-xl font-medium bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white border-0 shadow-lg transition-all"
                                >
                                    {loading ? (
                                        <span>Guardando...</span>
                                    ) : (
                                        <>
                                            <CheckCircle2 className="w-4 h-4" />
                                            <span>Guardar Modificación</span>
                                        </>
                                    )}
                                </Button>
                            </div>
                        </form>
                    </motion.div>
                </div>
            )}
        </AnimatePresence>
    );
};