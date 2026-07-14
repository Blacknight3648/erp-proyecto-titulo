import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { AlertTriangle, X, FileText } from 'lucide-react';
import { Button } from "../../../ui/button";
import { Badge } from "../../../ui/badge";

interface JustificacionModalProps {
    isOpen: boolean;
    onClose: () => void;
    item: any | null;
}

export const JustificacionModal: React.FC<JustificacionModalProps> = ({ isOpen, onClose, item }) => {
    if (!isOpen || !item) return null;

    return (
        <AnimatePresence>
            {isOpen && (
                <div
                    className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-foreground/70 backdrop-blur-sm"
                    role="dialog"
                    aria-modal="true"
                    aria-labelledby="justificacion-modal-title"
                    onClick={onClose}
                >
                    <motion.div
                        initial={{ opacity: 0, scale: 0.95, y: 10 }}
                        animate={{ opacity: 1, scale: 1, y: 0 }}
                        exit={{ opacity: 0, scale: 0.95, y: 10 }}
                        transition={{ duration: 0.2 }}
                        className="relative w-full max-w-lg bg-sidebar border border-sidebar-border rounded-2xl shadow-2xl overflow-hidden text-sidebar-foreground"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div className="flex items-center justify-between px-6 py-5 border-b border-sidebar-border bg-sidebar-popup">
                            <div className="flex items-center gap-3">
                                <div className="bg-warning/10 p-2 rounded-xl">
                                    <AlertTriangle className="w-5 h-5 text-warning" />
                                </div>
                                <div>
                                    <h3 id="justificacion-modal-title" className="text-lg font-semibold tracking-tight text-white">
                                        Insumo Modificado
                                    </h3>
                                    <p className="text-sm text-sidebar-muted mt-0.5">
                                        {item.insumo} <span className="text-sidebar-muted">({item.tipoInsumo})</span>
                                    </p>
                                </div>
                            </div>
                            <button
                                onClick={onClose}
                                aria-label="Cerrar"
                                className="p-2 rounded-lg text-sidebar-muted hover:text-white hover:bg-sidebar-popup transition-colors"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </div>

                        <div className="p-6 space-y-4">
                            <div className="flex items-center gap-2 text-sm font-bold text-warning uppercase tracking-wider">
                                <FileText className="w-4 h-4" />
                                Justificación
                            </div>
                            <p className="text-base text-white leading-relaxed bg-sidebar-popup/60 border border-sidebar-border rounded-xl p-4">
                                {item.justificacionModificacion || 'No se registró una justificación para esta modificación.'}
                            </p>
                        </div>

                        <div className="flex items-center justify-end gap-3 px-6 py-4 border-t border-sidebar-border">
                            <Button
                                type="button"
                                onClick={onClose}
                                className="bg-warning hover:bg-warning/90 text-white rounded-xl px-6 h-11"
                            >
                                Entendido
                            </Button>
                        </div>
                    </motion.div>
                </div>
            )}
        </AnimatePresence>
    );
};
