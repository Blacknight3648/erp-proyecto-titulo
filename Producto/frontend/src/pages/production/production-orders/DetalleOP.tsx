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
    mockOpDetails,
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
        <div className="max-w-2xl mx-auto bg-gray-50/50 min-h-[calc(100vh-120px)] p-4 pb-24 animate-in slide-in-from-bottom-8 duration-700 relative">
            
            <ConfirmModal 
                show={showConfirmModal} 
                onConfirm={finalizeSave} 
                onCancel={() => setShowConfirmModal(false)} 
            />

            <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100">
                <div className="text-center mb-8 p-4">
                    <h2 className="text-2xl font-medium text-gray-500 tracking-tight">
                        {isReadOnly ? 'Detalles de OP' : 'Modificar OP'}
                        <span className="font-black text-blue-600 text-3xl ml-1">
                            {selectedOP ? String(selectedOP.id || '').replace('OP-2024-', '') : '20549'}
                        </span>
                    </h2>
                    {selectedOP && (
                        <div className="flex flex-col items-center mt-1">
                            <p className="text-gray-400 font-bold text-lg uppercase tracking-wider">
                                {getClientName(selectedOP)}
                            </p>
                            <div className="flex items-center space-x-3 mt-2">
                                <button
                                    onClick={() => navigate('/detalle-nv', { state: { selectedNV: selectedOP.notaVentaId || selectedOP.nv_id } })}
                                    className="flex items-center space-x-1 text-[10px] font-black text-blue-500 uppercase tracking-widest hover:text-blue-700 hover:underline transition-all"
                                >
                                    <span>NV Origen #{selectedOP.notaVentaId || selectedOP.nv_id}</span>
                                    <ExternalLink className="w-3 h-3" />
                                </button>
                                <span className="text-gray-300">|</span>
                                <button
                                    onClick={() => navigate('/produccion/emitir-oc', { state: { op: selectedOP } })}
                                    className="flex items-center space-x-1 text-[10px] font-black text-indigo-600 uppercase tracking-widest hover:text-indigo-800 transition-all bg-indigo-50 px-3 py-1 rounded-full border border-indigo-100"
                                >
                                    <Plus className="w-3 h-3" />
                                    <span>Solicitar OC / MP</span>
                                </button>
                            </div>
                        </div>
                    )}
                    {isReadOnly && (
                        <span className="inline-block mt-2 px-3 py-1 bg-gray-100 text-gray-500 text-[10px] font-bold uppercase tracking-widest rounded-full">
                            Modo Solo Lectura
                        </span>
                    )}
                </div>

                <div className="bg-blue-50/70 p-8 rounded-3xl border border-blue-100 mb-10 text-center relative overflow-hidden group shadow-inner">
                    <div className="absolute top-0 right-0 p-4 opacity-10 group-hover:rotate-12 transition-transform">
                        <Factory className="w-20 h-20 text-blue-600" />
                    </div>
                    <div className="flex flex-wrap items-center justify-center gap-3 mb-3">
                        <span className="text-2xl">📅</span>
                        <h3 className="text-blue-700 font-black text-xl tracking-tighter uppercase italic">ENTREGA OP: <span className="text-gray-800 ml-1">25/02/2026</span></h3>
                        <span className="bg-green-100 text-green-700 text-[11px] font-black px-4 py-1.5 rounded-full border border-green-200 shadow-sm flex items-center">
                            🏆 Quedan 15 días
                        </span>
                    </div>
                    <p className="text-gray-400 text-sm font-medium uppercase tracking-tighter">(Cliente solicita para: 27/2/2026)</p>
                </div>

                <div className="mb-12 px-2">
                    <div className="flex justify-between items-center mb-4">
                        <span className="text-xs font-black text-gray-500 uppercase tracking-widest leading-none">Avance General</span>
                        <span className="text-sm font-black text-gray-800 italic">{selectedOP?.progreso || 0}%</span>
                    </div>
                    <div className="w-full bg-gray-100 h-3.5 rounded-full p-1 shadow-inner border border-gray-200/50">
                        <div className="bg-blue-600 h-full rounded-full transition-all duration-1000 shadow-lg shadow-blue-200" style={{ width: `${selectedOP?.progreso || 0}%` }}></div>
                    </div>
                </div>

                <div className="grid grid-cols-1 gap-4 mb-12">
                    {opFields.map((item, i) => {
                        const isEditing = editingFieldIdx === i;
                        const currentOpDetails = mockOpDetails[selectedOP?.id] || {};
                        const fieldValue = currentOpDetails[item.key];

                        return (
                            <div
                                key={i}
                                className={`bg-white border-2 transition-all duration-500 rounded-[2rem] overflow-hidden ${isEditing ? 'border-blue-500 ring-4 ring-blue-50 shadow-xl scale-[1.02]' : 'border-gray-100 hover:border-gray-200 shadow-sm'}`}
                            >
                                <div
                                    onClick={() => !isEditing && handleSelectFieldInline(i)}
                                    className={`p-6 cursor-pointer flex justify-between items-center ${isEditing ? 'bg-blue-50/30' : ''}`}
                                >
                                    <div className="flex flex-col">
                                        <span className="text-sm font-black text-gray-700 tracking-tight leading-tight uppercase italic">{item.title}</span>
                                        {fieldValue && !isEditing && !isReadOnly && (
                                            <span className="text-[10px] font-bold text-amber-600 italic mt-0.5 uppercase tracking-tighter">⚠️ Dato existente</span>
                                        )}
                                    </div>

                                    <div className="flex items-center">
                                        {!isEditing && (
                                            <>
                                                {fieldValue ? (
                                                    isReadOnly ? (
                                                        <div className="flex items-center text-indigo-700 text-[10px] font-black bg-indigo-50 px-4 py-2 rounded-xl border border-indigo-100 shadow-sm uppercase tracking-widest">
                                                            {item.type === 'date' && <><Calendar className="w-3.5 h-3.5 mr-2" /> {fieldValue}</>}
                                                            {item.type === 'select' && <span>{fieldValue}</span>}
                                                            {item.type === 'textarea' && <span className="truncate max-w-[100px]">{fieldValue}</span>}
                                                            {item.type === 'calculated_number' && <span>{fieldValue} UND</span>}
                                                        </div>
                                                    ) : (
                                                        <div className="flex items-center text-green-600 text-[10px] font-black bg-green-50 px-4 py-2 rounded-xl border border-green-100 shadow-sm uppercase tracking-widest">
                                                            <CheckCircle2 className="w-3.5 h-3.5 mr-2" /> LISTO
                                                        </div>
                                                    )
                                                ) : (
                                                    <span className="text-gray-300 text-[10px] font-black uppercase tracking-widest italic">PENDIENTE</span>
                                                )}
                                            </>
                                        )}
                                    </div>
                                </div>

                                {isEditing && (
                                    <div className="p-6 pt-0 space-y-5 animate-in slide-in-from-top-4 duration-300">
                                        <div className="h-px bg-gray-100 w-full mb-5"></div>
                                        {fieldValue && (
                                            <div className="bg-amber-50 p-4 rounded-xl border border-amber-100 text-center mb-4">
                                                <p className="text-amber-800 text-[11px] font-black uppercase tracking-widest italic">Valor actual: {fieldValue}</p>
                                            </div>
                                        )}
                                        <div className="space-y-4">
                                            {item.type === 'select' ? (
                                                <select
                                                    className="w-full p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl font-black text-xs text-gray-700 focus:ring-4 focus:ring-blue-50 outline-none appearance-none uppercase"
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
                                                    className="w-full p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl font-bold text-xs text-gray-700 focus:ring-4 focus:ring-blue-50 outline-none uppercase resize-none shadow-inner"
                                                    onChange={(e) => setTempValue(e.target.value)}
                                                />
                                            ) : item.type === 'calculated_number' ? (
                                                <div className="space-y-4">
                                                    <div className="bg-blue-50 p-6 rounded-2xl border border-blue-100 flex justify-between items-center shadow-inner">
                                                        <div className="flex flex-col">
                                                            <span className="text-[10px] font-black text-blue-400 uppercase tracking-widest leading-none mb-1">Cálculo sugerido (NV)</span>
                                                            <span className="text-2xl font-black text-blue-700 tracking-tighter italic">{calculateTotalQty(selectedOP?.id)} UND</span>
                                                        </div>
                                                        <button
                                                            onClick={() => { setIsManualCutting(true); setTempValue(calculateTotalQty(selectedOP?.id)); }}
                                                            className="flex items-center space-x-2 bg-white px-4 py-2 rounded-xl text-[10px] font-black text-indigo-600 uppercase tracking-widest border border-indigo-100 shadow-sm"
                                                        >
                                                            <Edit2 className="w-3.5 h-3.5" />
                                                            <span>Editar</span>
                                                        </button>
                                                    </div>
                                                </div>
                                            ) : (
                                                <input
                                                    type="date"
                                                    className="w-full p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl font-black text-xs text-gray-700 focus:ring-4 focus:ring-blue-50 outline-none uppercase"
                                                    onChange={(e) => setTempValue(e.target.value)}
                                                />
                                            )}
                                            <div className="grid grid-cols-2 gap-3 mt-4">
                                                <button
                                                    onClick={() => handleSaveInline(item)}
                                                    disabled={isSubmitting}
                                                    className="py-4 bg-blue-600 text-white font-black rounded-xl text-[10px] uppercase tracking-widest shadow-lg shadow-blue-100 active:scale-95 transition-all disabled:opacity-50"
                                                >
                                                    {isSubmitting ? 'GUARDANDO...' : 'GUARDAR'}
                                                </button>
                                                <button
                                                    onClick={() => { setEditingFieldIdx(null); setIsManualCutting(false); }}
                                                    className="py-4 bg-gray-100 text-gray-500 font-black rounded-xl text-[10px] uppercase tracking-widest active:scale-95 transition-all"
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
                        className="w-full bg-slate-600 hover:bg-slate-700 text-white font-black py-5 rounded-[1.5rem] shadow-xl shadow-slate-200 transition-all text-[10px] uppercase tracking-widest border-b-4 border-slate-800 flex items-center justify-center space-x-2"
                    >
                        <span>Volver al Listado</span>
                    </button>
                    <button
                        onClick={() => navigate('/produccion/emitir-oc', { state: { op: selectedOP } })}
                        className="w-full bg-blue-600 hover:bg-black text-white font-black py-5 rounded-[1.5rem] shadow-xl shadow-blue-100 transition-all text-[10px] uppercase tracking-widest border-b-4 border-blue-800 flex items-center justify-center space-x-2"
                    >
                        <ShoppingCart className="w-5 h-5 mr-3" />
                        <span>Solicitar Insumos (OC)</span>
                    </button>
                </div>
            </div>
        </div>
    );
}
