import { useState, useEffect } from 'react';
import { X, Check, Loader2, AlertCircle } from 'lucide-react';
import { OrdenProduccionService } from '../../../remote/service/OrdenProduccionService';
import { toast } from 'sonner';

export default function RegistrarAvanceModal({ isOpen, onClose, avance, opId, onSuccess }) {
    const [formData, setFormData] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (avance) {
            setFormData({
                fechaRecepcionOp: avance.fechaRecepcionOp || '',
                finTizado: avance.finTizado || '',
                recepcionCompras: avance.recepcionCompras || '',
                inicioCorte: avance.inicioCorte || '',
                finCorte: avance.finCorte || '',
                inicioLogo: avance.inicioLogo || '',
                estadoIdaLogo: avance.estadoIdaLogo || '',
                regresoLogo: avance.regresoLogo || '',
                estadoRecLogo: avance.estadoRecLogo || '',
                finTallerExterno: avance.finTallerExterno || '',
                calidadTaller: avance.calidadTaller || '',
                obsTaller: avance.obsTaller || '',
                finTerminacion: avance.finTerminacion || '',
                finPersonalizado: avance.finPersonalizado || ''
            });
        }
    }, [avance]);

    if (!isOpen) return null;

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setIsSubmitting(true);
        
        try {
            // Convirtiendo strings vacios a null
            const payload = Object.fromEntries(
                Object.entries(formData).map(([k, v]) => [k, v === '' ? null : v])
            );

            await OrdenProduccionService.actualizarSeguimiento(opId, payload);
            toast.success(`Seguimiento actualizado con éxito en OP #${opId}`);
            onSuccess();
            onClose();
        } catch (err) {
            console.error(err);
            setError(err.response?.data?.message || "Ocurrió un error al actualizar el seguimiento");
            toast.error("Error al actualizar seguimiento");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-foreground/60 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-card rounded-[2rem] shadow-2xl w-full max-w-3xl overflow-hidden border border-border flex flex-col animate-in zoom-in-95 duration-300">

                {/* Header */}
                <div className="px-8 py-6 border-b border-border flex items-center justify-between bg-muted/50">
                    <div>
                        <h3 className="text-xl font-black text-foreground tracking-tight italic">
                            Actualizar Seguimiento OP
                        </h3>
                        <p className="text-[11px] text-muted-foreground font-bold uppercase tracking-widest mt-1">
                            OP #{opId}
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2.5 bg-card border border-border text-muted-foreground hover:text-foreground rounded-xl hover:bg-muted transition-colors shadow-sm"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                {/* Body */}
                <div className="p-8 overflow-y-auto max-h-[70vh]">
                    {error && (
                        <div className="mb-6 p-4 bg-destructive/10 border border-destructive/20 rounded-2xl flex items-start gap-3">
                            <AlertCircle className="w-5 h-5 text-destructive shrink-0 mt-0.5" />
                            <p className="text-xs font-bold text-destructive uppercase">{error}</p>
                        </div>
                    )}

                    <form id="avance-form" onSubmit={handleSubmit} className="space-y-8">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Fecha Recepción OP</label>
                                <input type="date" name="fechaRecepcionOp" value={formData.fechaRecepcionOp} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Fin de Tizado</label>
                                <input type="date" name="finTizado" value={formData.finTizado} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2 opacity-60">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Estado OC (MP) [Auto]</label>
                                <input type="date" disabled value={avance?.fechaEstadoOcMp || ''} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-muted-foreground cursor-not-allowed" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Recepción Compras</label>
                                <input type="date" name="recepcionCompras" value={formData.recepcionCompras} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>

                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Inicio Corte</label>
                                <input type="date" name="inicioCorte" value={formData.inicioCorte} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Fin Corte</label>
                                <input type="date" name="finCorte" value={formData.finCorte} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Inicio Logo</label>
                                <input type="date" name="inicioLogo" value={formData.inicioLogo} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Estado Ida Logo</label>
                                <select name="estadoIdaLogo" value={formData.estadoIdaLogo} onChange={handleChange} className="w-full px-4 py-2.5 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all appearance-none">
                                    <option value="">Seleccione estado...</option>
                                    <option value="IDA_COMPLETA">Ida Completa</option>
                                    <option value="IDA_PARCIAL">Ida Parcial</option>
                                    <option value="NA">N/A</option>
                                </select>
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Regreso Logo</label>
                                <input type="date" name="regresoLogo" value={formData.regresoLogo} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Estado Rec. Logo</label>
                                <select name="estadoRecLogo" value={formData.estadoRecLogo} onChange={handleChange} className="w-full px-4 py-2.5 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all appearance-none">
                                    <option value="">Seleccione estado...</option>
                                    <option value="RECEPCION_COMPLETA">Recepción Completa</option>
                                    <option value="RECEPCION_PARCIAL">Recepción Parcial</option>
                                    <option value="NA">N/A</option>
                                </select>
                            </div>
                            
                            <div className="space-y-2 opacity-60">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Inicio Taller Externo [Auto]</label>
                                <input type="date" disabled value={avance?.inicioTallerExterno || ''} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-muted-foreground cursor-not-allowed" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Fin Taller Externo</label>
                                <input type="date" name="finTallerExterno" value={formData.finTallerExterno} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Calidad Taller</label>
                                <select name="calidadTaller" value={formData.calidadTaller} onChange={handleChange} className="w-full px-4 py-2.5 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all appearance-none">
                                    <option value="">Seleccione estado...</option>
                                    <option value="APROBADO">Aprobado</option>
                                    <option value="RECHAZADO">Rechazado</option>
                                    <option value="CON_OBSERVACIONES">Con Observaciones</option>
                                </select>
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Fin Terminación / Fin OP</label>
                                <input type="date" name="finTerminacion" value={formData.finTerminacion} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>
                            
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Fin Personalizado</label>
                                <input type="date" name="finPersonalizado" value={formData.finPersonalizado} onChange={handleChange} className="w-full px-4 py-2 bg-muted border border-border rounded-xl text-sm font-black text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all" />
                            </div>

                        </div>
                        
                        <div className="space-y-2">
                            <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block">Observaciones Taller</label>
                            <textarea
                                name="obsTaller"
                                value={formData.obsTaller}
                                onChange={handleChange}
                                rows={2}
                                className="w-full px-4 py-3 bg-muted border border-border rounded-xl text-sm font-medium text-foreground focus:bg-card focus:ring-2 focus:ring-primary outline-none transition-all resize-none"
                                placeholder="Anotaciones adicionales..."
                                maxLength={1000}
                            />
                        </div>
                    </form>
                </div>

                {/* Footer */}
                <div className="px-8 py-5 border-t border-border bg-muted/50 flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={onClose}
                        disabled={isSubmitting}
                        className="px-6 py-2.5 rounded-xl text-xs font-black text-muted-foreground hover:bg-muted transition-colors uppercase tracking-widest"
                    >
                        Cancelar
                    </button>
                    <button
                        type="submit"
                        form="avance-form"
                        disabled={isSubmitting}
                        className="px-6 py-2.5 rounded-xl text-xs font-black text-white bg-primary hover:bg-primary-hover active:bg-primary transition-colors uppercase tracking-widest flex items-center gap-2 shadow-md hover:shadow-lg disabled:opacity-70 disabled:cursor-not-allowed"
                    >
                        {isSubmitting ? (
                            <><Loader2 className="w-4 h-4 animate-spin" /> Guardando...</>
                        ) : (
                            <><Check className="w-4 h-4" /> Registrar Avance</>
                        )}
                    </button>
                </div>
            </div>
        </div>
    );
}
