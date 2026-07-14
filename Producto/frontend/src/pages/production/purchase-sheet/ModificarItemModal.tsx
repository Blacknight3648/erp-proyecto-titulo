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
import { clampNonNegative } from "../../../utils/validations";

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
                <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-foreground/70 backdrop-blur-sm">
                    <motion.div
                        initial={{ opacity: 0, scale: 0.95, y: 10 }}
                        animate={{ opacity: 1, scale: 1, y: 0 }}
                        exit={{ opacity: 0, scale: 0.95, y: 10 }}
                        transition={{ duration: 0.2 }}
                        className="relative w-full max-w-2xl bg-sidebar border border-sidebar-border rounded-2xl shadow-2xl overflow-hidden text-sidebar-foreground"
                    >
                        {/* Cabecera */}
                        <div className="flex items-center justify-between px-6 py-5 border-b border-sidebar-border bg-sidebar-popup">
                            <div>
                                <h3 className="text-lg font-semibold tracking-tight text-white flex items-center gap-2">
                                    Validar y Modificar Ítem
                                    <Badge className="bg-sidebar-popup text-sidebar-foreground border-sidebar-border font-mono text-xs">
                                        ID #{item.id}
                                    </Badge>
                                </h3>
                                <p className="text-sm text-sidebar-muted mt-0.5">
                                    {item.insumo} <span className="text-sidebar-muted">({item.tipoInsumo})</span>
                                </p>
                            </div>
                            <button
                                onClick={onClose}
                                className="p-2 rounded-lg text-sidebar-muted hover:text-white hover:bg-sidebar-popup transition-colors"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </div>

                        <form onSubmit={handleSubmit} className="p-6 space-y-6">
                            {/* Panel Informativo de Tarjetas */}
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                <div className="p-4 rounded-xl bg-sidebar-popup/30 border border-sidebar-border flex flex-col justify-between min-h-[100px]">
                                    <div className="flex items-center gap-2 text-[11px] font-bold text-sidebar-muted uppercase tracking-wider">
                                        <Calculator className="w-3.5 h-3.5 text-sidebar-primary" />
                                        Demanda Costeo
                                    </div>
                                    <div className="mt-2 text-2xl font-bold text-white font-mono">
                                        {cantOriginal} <span className="text-xs font-normal text-sidebar-muted">unid.</span>
                                    </div>
                                    <div className="mt-1 text-xs text-sidebar-muted">
                                        Consumo × Cantidad OP
                                    </div>
                                </div>

                                <div className="p-4 rounded-xl bg-sidebar-popup/30 border border-sidebar-border flex flex-col justify-between min-h-[100px]">
                                    <div className="flex items-center gap-2 text-[11px] font-bold text-sidebar-muted uppercase tracking-wider">
                                        <Box className="w-3.5 h-3.5 text-success" />
                                        Stock en Bodega
                                    </div>
                                    <div className="mt-2 text-2xl font-bold text-success font-mono">
                                        {cantStock} <span className="text-xs font-normal text-sidebar-muted">unid.</span>
                                    </div>
                                    <div className="mt-1 text-xs text-sidebar-muted">
                                        Sin stock registrado
                                    </div>
                                </div>

                                <div className="p-4 rounded-xl bg-sidebar-popup/30 border border-sidebar-border flex flex-col justify-between min-h-[100px]">
                                    <div className="flex items-center gap-2 text-[11px] font-bold text-sidebar-muted uppercase tracking-wider">
                                        <DollarSign className="w-3.5 h-3.5 text-warning" />
                                        Precio Ref. Costeo
                                    </div>
                                    <div className="mt-2 text-2xl font-bold text-warning font-mono">
                                        {formatCLP(precioOriginal)}
                                    </div>
                                    <div className="mt-1 text-xs text-sidebar-muted">
                                        Presupuestado en versión
                                    </div>
                                </div>
                            </div>

                            {/* Inputs de Modificación (Arreglado y Alineado) */}
                            <div className="p-5 rounded-xl bg-sidebar-popup/60 border border-sidebar-border space-y-4">
                                <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
                                    {/* Stock en bodega */}
                                    <div className="flex flex-col justify-between h-full space-y-2">
                                        <label className="text-[11px] font-bold text-success uppercase tracking-wider flex flex-wrap justify-between items-center gap-1 min-h-[16px]">
                                            <span>STOCK EN BODEGA</span>
                                            <span className="text-sidebar-muted text-[10px] font-normal normal-case">AJUSTE MANUAL</span>
                                        </label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={cantidadStock}
                                            onChange={(e) => handleStockChange(String(clampNonNegative(e.target.value)))}
                                            className="w-full bg-sidebar-popup border border-success/30 rounded-xl px-4 py-3 text-success font-mono text-base focus:outline-none focus:border-success focus:ring-1 focus:ring-success transition-all"
                                            placeholder="0"
                                        />
                                    </div>

                                    {/* Cantidad a comprar */}
                                    <div className="flex flex-col justify-between h-full space-y-2">
                                        <label className="text-[11px] font-bold text-sidebar-foreground uppercase tracking-wider flex flex-wrap justify-between items-center gap-1 min-h-[16px]">
                                            <span>CANTIDAD A COMPRAR</span>
                                        </label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={cantidadAComprar}
                                            onChange={(e) => setCantidadAComprar(clampNonNegative(e.target.value))}
                                            className="w-full bg-sidebar-popup border border-sidebar-border rounded-xl px-4 py-3 text-white font-mono text-base focus:outline-none focus:border-sidebar-muted transition-all"
                                            required
                                        />
                                    </div>

                                    {/* Precio Ref */}
                                    <div className="flex flex-col justify-between h-full space-y-2">
                                        <label className="text-[11px] font-bold text-sidebar-foreground uppercase tracking-wider flex flex-wrap justify-between items-center gap-1 min-h-[16px]">
                                            <span>PRECIO REF.</span>
                                            {precioActual !== precioOriginal && (
                                                <span className="text-warning text-[10px] font-medium normal-case">DIFIERE COSTEO</span>
                                            )}
                                        </label>
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            value={precioUnitarioRef}
                                            onChange={(e) => setPrecioUnitarioRef(clampNonNegative(e.target.value))}
                                            className="w-full bg-sidebar-popup border border-sidebar-border rounded-xl px-4 py-3 text-white font-mono text-base focus:outline-none focus:border-sidebar-muted transition-all"
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
                                            className="text-xs text-sidebar-muted hover:text-warning underline underline-offset-4 transition-colors"
                                        >
                                            Restablecer valores originales de costeo
                                        </button>
                                    </div>
                                )}
                            </div>

                            {/* Campo de Justificación Obligatorio */}
                            <div className="space-y-2">
                                <label className="text-xs font-semibold text-sidebar-foreground uppercase tracking-wider flex items-center gap-1.5">
                                    <FileText className="w-4 h-4 text-warning" />
                                    <span>Justificación de Modificación</span>
                                    {isModified && (
                                        <span className="text-destructive font-bold text-[11px] normal-case">* (OBLIGATORIO AL MODIFICAR)</span>
                                    )}
                                </label>
                                <textarea
                                    rows={3}
                                    value={justificacion}
                                    onChange={(e) => setJustificacion(e.target.value)}
                                    placeholder="El proveedor no está disponible"
                                    className={`w-full bg-sidebar-popup/60 border rounded-xl p-3.5 text-sm text-white placeholder-sidebar-muted focus:outline-none focus:ring-1 transition-all ${
                                        isModified && !isJustificationValid
                                            ? 'border-destructive focus:ring-destructive bg-destructive/5'
                                            : 'border-sidebar-border focus:ring-sidebar-border'
                                    }`}
                                />
                                {isModified && !isJustificationValid && (
                                    <p className="text-xs text-destructive flex items-center gap-1.5 mt-1">
                                        <AlertTriangle className="w-3.5 h-3.5 flex-shrink-0" />
                                        Debe justificar la diferencia para auditar correctamente los desvíos.
                                    </p>
                                )}
                            </div>

                            {/* Alerta de bloqueo/error inferior */}
                            {error && (
                                <div className="p-3.5 rounded-xl bg-destructive/10 border border-destructive/20 text-destructive text-sm flex items-center gap-2.5">
                                    <AlertTriangle className="w-4 h-4 flex-shrink-0 text-destructive" />
                                    <span>{error}</span>
                                </div>
                            )}

                            {/* Botones de Acción */}
                            <div className="flex items-center justify-end gap-3 pt-4 border-t border-sidebar-border">
                                <Button
                                    type="button"
                                    variant="outline"
                                    onClick={onClose}
                                    className="bg-sidebar border-sidebar-border text-sidebar-foreground hover:bg-sidebar-popup hover:text-white rounded-xl px-5 h-11"
                                >
                                    Cancelar
                                </Button>
                                <Button
                                    type="submit"
                                    disabled={loading || !canSubmit}
                                    className="flex items-center gap-2 px-6 h-11 rounded-xl font-medium bg-warning hover:bg-warning/90 text-white border-0 shadow-lg transition-all"
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
