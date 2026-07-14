import React from 'react';
import {
    ChevronLeft,
    Factory,
    CheckCircle2,
    Calendar,
    ExternalLink,
    Plus,
    ShoppingCart,
    Edit2,
    ClipboardList
} from 'lucide-react';
import ConfirmModal from './ConfirmModal';

export default function DetalleOP({
    selectedOP,
    view,
    setView,
    isReadOnly,
    setIsReadOnly,
    editingFieldIdx,
    handleSelectFieldInline,
    opFields,
    seguimientoDetails,
    tempValue,
    setTempValue,
    isManualCutting,
    setIsManualCutting,
    calculateTotalQty,
    handleSaveInline,
    isSubmitting,
    setEditingFieldIdx,
    showConfirmModal,
    finalizeSave,
    setShowConfirmModal,
    navigate,
    getClientName,
    onBack
}) {
    if (!selectedOP && view === 'detail') return null;

    return (
        <div className="max-w-2xl mx-auto bg-muted/50 min-h-[calc(100vh-120px)] p-4 pb-24 animate-in slide-in-from-bottom-8 duration-700 relative">

            <ConfirmModal
                show={showConfirmModal}
                onConfirm={finalizeSave}
                onCancel={() => setShowConfirmModal(false)}
            />

            <div className="bg-card p-8 rounded-[2.5rem] shadow-sm border border-border">
                <div className="text-center mb-8 p-4">
                    <h2 className="text-2xl font-medium text-muted-foreground tracking-tight">
                        {isReadOnly ? 'Detalles de OP' : 'Modificar OP'}
                        <span className="font-black text-primary text-3xl ml-1">
                            {selectedOP ? String(selectedOP.id || '').replace('OP-2024-', '') : '20549'}
                        </span>
                    </h2>
                    {selectedOP && (
                        <div className="flex flex-col items-center mt-1">
                            <p className="text-muted-foreground font-bold text-lg uppercase tracking-wider">
                                {getClientName(selectedOP)}
                            </p>
                            <div className="flex items-center space-x-3 mt-2">
                                <button
                                    onClick={() => navigate('/detalle-nv', { state: { selectedNV: selectedOP.notaVentaId || selectedOP.nv_id } })}
                                    className="flex items-center space-x-1 text-[10px] font-black text-primary uppercase tracking-widest hover:text-primary-hover hover:underline transition-all"
                                >
                                    <span>NV Origen #{selectedOP.notaVentaId || selectedOP.nv_id}</span>
                                    <ExternalLink className="w-3 h-3" />
                                </button>
                                <span className="text-muted-foreground/50">|</span>
                                <button
                                    onClick={() => navigate('/produccion/emitir-oc', { state: { op: selectedOP } })}
                                    className="flex items-center space-x-1 text-[10px] font-black text-brand-indigo uppercase tracking-widest hover:text-brand-indigo/80 transition-all bg-brand-indigo/10 px-3 py-1 rounded-full border border-brand-indigo/20"
                                >
                                    <Plus className="w-3 h-3" />
                                    <span>Solicitar OC / MP</span>
                                </button>
                            </div>
                        </div>
                    )}
                    {isReadOnly && (
                        <span className="inline-block mt-2 px-3 py-1 bg-muted text-muted-foreground text-[10px] font-bold uppercase tracking-widest rounded-full">
                            Modo Solo Lectura
                        </span>
                    )}
                </div>

                <div className="bg-primary/5 p-8 rounded-3xl border border-primary/20 mb-10 text-center relative overflow-hidden group shadow-inner">
                    <div className="absolute top-0 right-0 p-4 opacity-10 group-hover:rotate-12 transition-transform">
                        <Factory className="w-20 h-20 text-primary" />
                    </div>
                    {selectedOP?.fechaEntregaProgramada ? (
                        (() => {
                            const [y, m, d] = selectedOP.fechaEntregaProgramada.split('-');
                            const fechaFmt = `${d}/${m}/${y}`;
                            const diasRestantes = Math.ceil(
                                (new Date(selectedOP.fechaEntregaProgramada) - new Date(new Date().toDateString())) / (1000 * 60 * 60 * 24)
                            );
                            return (
                                <div className="flex flex-wrap items-center justify-center gap-3 mb-3">
                                    <span className="text-2xl">📅</span>
                                    <h3 className="text-primary font-black text-xl tracking-tighter uppercase italic">ENTREGA OP: <span className="text-foreground ml-1">{fechaFmt}</span></h3>
                                    <span className={`text-[11px] font-black px-4 py-1.5 rounded-full border shadow-sm flex items-center ${diasRestantes < 0 ? 'bg-destructive/10 text-destructive border-destructive/20' : 'bg-success/10 text-success border-success/20'}`}>
                                        {diasRestantes < 0 ? `⚠️ ${Math.abs(diasRestantes)} días de atraso` : `🏆 Quedan ${diasRestantes} días`}
                                    </span>
                                </div>
                            );
                        })()
                    ) : (
                        <div className="flex flex-wrap items-center justify-center gap-3 mb-3">
                            <span className="text-2xl">📅</span>
                            <h3 className="text-muted-foreground font-black text-xl tracking-tighter uppercase italic">Sin fecha de entrega programada</h3>
                        </div>
                    )}
                </div>

                <div className="mb-12 px-2">
                    <div className="flex justify-between items-center mb-4">
                        <span className="text-xs font-black text-muted-foreground uppercase tracking-widest leading-none">Avance General</span>
                        <span className="text-sm font-black text-foreground italic">{selectedOP?.progreso || 0}%</span>
                    </div>
                    <div className="w-full bg-muted h-3.5 rounded-full p-1 shadow-inner border border-border">
                        <div className="bg-primary h-full rounded-full transition-all duration-1000 shadow-lg shadow-primary/20" style={{ width: `${selectedOP?.progreso || 0}%` }}></div>
                    </div>
                </div>

                <div className="grid grid-cols-1 gap-4 mb-12">
                    {opFields.map((item, i) => {
                        const isEditing = editingFieldIdx === i;
                        const currentOpDetails = seguimientoDetails[selectedOP?.id] || {};
                        const fieldValue = currentOpDetails[item.key];

                        return (
                            <div
                                key={i}
                                className={`bg-card border-2 transition-all duration-500 rounded-[2rem] overflow-hidden ${isEditing ? 'border-primary ring-4 ring-primary/10 shadow-xl scale-[1.02]' : 'border-border hover:border-border-strong shadow-sm'}`}
                            >
                                <div
                                    onClick={() => !isEditing && handleSelectFieldInline(i)}
                                    className={`p-6 cursor-pointer flex justify-between items-center ${isEditing ? 'bg-primary/5' : ''}`}
                                >
                                    <div className="flex flex-col">
                                        <span className="text-sm font-black text-foreground tracking-tight leading-tight uppercase italic">{item.title}</span>
                                        {fieldValue && !isEditing && !isReadOnly && (
                                            <span className="text-[10px] font-bold text-warning italic mt-0.5 uppercase tracking-tighter">⚠️ Dato existente</span>
                                        )}
                                    </div>

                                    <div className="flex items-center">
                                        {!isEditing && (
                                            <>
                                                {fieldValue ? (
                                                    isReadOnly ? (
                                                        <div className="flex items-center text-brand-indigo text-[10px] font-black bg-brand-indigo/10 px-4 py-2 rounded-xl border border-brand-indigo/20 shadow-sm uppercase tracking-widest">
                                                            {item.type === 'date' && <><Calendar className="w-3.5 h-3.5 mr-2" /> {fieldValue}</>}
                                                            {item.type === 'select' && <span>{fieldValue}</span>}
                                                            {item.type === 'textarea' && <span className="truncate max-w-[100px]">{fieldValue}</span>}
                                                            {item.type === 'calculated_number' && <span>{fieldValue} UND</span>}
                                                        </div>
                                                    ) : (
                                                        <div className="flex items-center text-success text-[10px] font-black bg-success/10 px-4 py-2 rounded-xl border border-success/20 shadow-sm uppercase tracking-widest">
                                                            <CheckCircle2 className="w-3.5 h-3.5 mr-2" /> LISTO
                                                        </div>
                                                    )
                                                ) : (
                                                    <span className="text-muted-foreground text-[10px] font-black uppercase tracking-widest italic">PENDIENTE</span>
                                                )}
                                            </>
                                        )}
                                    </div>
                                </div>

                                {isEditing && (
                                    <div className="p-6 pt-0 space-y-5 animate-in slide-in-from-top-4 duration-300">
                                        <div className="h-px bg-border w-full mb-5"></div>
                                        {fieldValue && (
                                            <div className="bg-warning/10 p-4 rounded-xl border border-warning/20 text-center mb-4">
                                                <p className="text-warning text-[11px] font-black uppercase tracking-widest italic">Valor actual: {fieldValue}</p>
                                            </div>
                                        )}
                                        <div className="space-y-4">
                                            {item.type === 'select' ? (
                                                <select
                                                    className="w-full p-4 bg-muted border-2 border-border rounded-2xl font-black text-xs text-foreground focus:ring-4 focus:ring-primary/10 outline-none appearance-none uppercase"
                                                    onChange={(e) => setTempValue(e.target.value)}
                                                >
                                                    <option value="">Seleccione...</option>
                                                    {item.options.map((opt, idx) => (
                                                        <option key={idx} value={opt}>{opt.toUpperCase()}</option>
                                                    ))}
                                                </select>
                                            ) : item.type === 'textarea' ? (
                                                <textarea
                                                    rows="4"
                                                    placeholder="Ingrese observaciones aquí..."
                                                    className="w-full p-4 bg-muted border-2 border-border rounded-2xl font-bold text-xs text-foreground focus:ring-4 focus:ring-primary/10 outline-none uppercase resize-none shadow-inner"
                                                    onChange={(e) => setTempValue(e.target.value)}
                                                />
                                            ) : item.type === 'calculated_number' ? (
                                                <div className="space-y-4">
                                                    <div className="bg-primary/10 p-6 rounded-2xl border border-primary/20 flex justify-between items-center shadow-inner">
                                                        <div className="flex flex-col">
                                                            <span className="text-[10px] font-black text-primary/70 uppercase tracking-widest leading-none mb-1">Cálculo sugerido (NV)</span>
                                                            <span className="text-2xl font-black text-primary tracking-tighter italic">{calculateTotalQty(selectedOP?.id)} UND</span>
                                                        </div>
                                                        <button
                                                            onClick={() => { setIsManualCutting(true); setTempValue(calculateTotalQty(selectedOP?.id)); }}
                                                            className="flex items-center space-x-2 bg-card px-4 py-2 rounded-xl text-[10px] font-black text-brand-indigo uppercase tracking-widest border border-brand-indigo/20 shadow-sm"
                                                        >
                                                            <Edit2 className="w-3.5 h-3.5" />
                                                            <span>Editar</span>
                                                        </button>
                                                    </div>
                                                </div>
                                            ) : (
                                                <input
                                                    type="date"
                                                    className="w-full p-4 bg-muted border-2 border-border rounded-2xl font-black text-xs text-foreground focus:ring-4 focus:ring-primary/10 outline-none uppercase"
                                                    onChange={(e) => setTempValue(e.target.value)}
                                                />
                                            )}
                                            <div className="grid grid-cols-2 gap-3 mt-4">
                                                <button
                                                    onClick={() => handleSaveInline(item)}
                                                    disabled={isSubmitting}
                                                    className="py-4 bg-primary text-primary-foreground font-black rounded-xl text-[10px] uppercase tracking-widest shadow-lg shadow-primary/20 active:scale-95 transition-all disabled:opacity-50"
                                                >
                                                    {isSubmitting ? 'GUARDANDO...' : 'GUARDAR'}
                                                </button>
                                                <button
                                                    onClick={() => { setEditingFieldIdx(null); setIsManualCutting(false); }}
                                                    className="py-4 bg-muted text-muted-foreground font-black rounded-xl text-[10px] uppercase tracking-widest active:scale-95 transition-all"
                                                >
                                                    CANCELAR
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>

                <div className="grid grid-cols-1 gap-4">
                    <button
                        onClick={onBack}
                        className="w-full bg-foreground hover:bg-foreground/90 text-background font-black py-5 rounded-[1.5rem] shadow-xl shadow-foreground/10 transition-all text-[10px] uppercase tracking-widest border-b-4 border-foreground/70 flex items-center justify-center space-x-2"
                    >
                        <span>Volver al Listado</span>
                    </button>
                    <button
                        onClick={() => navigate('/produccion/emitir-oc', { state: { op: selectedOP } })}
                        className="w-full bg-primary hover:bg-primary-hover text-primary-foreground font-black py-5 rounded-[1.5rem] shadow-xl shadow-primary/20 transition-all text-[10px] uppercase tracking-widest border-b-4 border-primary/70 flex items-center justify-center space-x-2"
                    >
                        <ShoppingCart className="w-5 h-5 mr-3" />
                        <span>Solicitar Insumos (OC)</span>
                    </button>
                </div>
            </div>
        </div>
    );
}
