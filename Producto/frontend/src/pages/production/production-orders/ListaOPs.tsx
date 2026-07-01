import React from 'react';
import { Factory, ClipboardList, CheckCircle2 } from 'lucide-react';

export default function ListaOPs({
    ordenes,
    isSelectionMode,
    setIsSelectionMode, 
    selectedOPIds, 
    toggleSelection, 
    setSelectedOP, 
    selectedOP,
    handleModificarRegistro,
    handleVerDetalles,
    handleBulkEdit,
    getClientName
}) {
    return (
        <div className="max-w-2xl mx-auto bg-gray-50/30 min-h-[calc(100vh-120px)] p-4 pb-24 relative animate-in fade-in duration-700">
            <h1 className="text-3xl font-black text-blue-600 mb-8 text-center tracking-tight uppercase italic">Registro de Producción</h1>

            <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 mb-8 space-y-4">
                <div className="p-4 bg-gray-50/50 rounded-2xl border border-gray-100 text-center">
                    <p className="text-gray-500 font-medium text-sm">Selecciona una OP reciente para actualizar sus estados:</p>
                </div>

                <div className="space-y-3">
                    <select className="w-full p-4 bg-white border border-gray-200 rounded-2xl text-[10px] font-black uppercase tracking-widest focus:ring-2 focus:ring-blue-500 outline-none appearance-none cursor-pointer">
                        <option>Filtrar por Cliente...</option>
                    </select>

                    <div className="relative">
                        <input
                            type="text"
                            placeholder="Buscar OP o Cliente..."
                            className="w-full p-4 bg-white border border-gray-200 rounded-2xl text-sm font-medium focus:ring-2 focus:ring-blue-500 outline-none placeholder:text-gray-400 shadow-inner"
                        />
                    </div>

                    <button
                        onClick={() => setIsSelectionMode(!isSelectionMode)}
                        className={`w-full py-4 font-black text-xs uppercase tracking-widest rounded-2xl border transition-all shadow-sm ${isSelectionMode ? 'bg-red-50 text-red-600 border-red-200' : 'bg-blue-600/10 text-blue-700 border-blue-200 hover:bg-blue-600 hover:text-white'}`}
                    >
                        {isSelectionMode ? 'Cancelar Selección' : 'Modificación múltiple'}
                    </button>
                </div>
            </div>

            <div className="flex justify-between items-center mb-4 ml-2">
                <h3 className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">OPs en tu Estación</h3>
                {isSelectionMode && selectedOPIds.length > 0 && (
                    <button
                        onClick={handleBulkEdit}
                        className="bg-blue-600 text-white px-6 py-2 rounded-full text-[10px] font-black uppercase tracking-widest shadow-lg shadow-blue-200 animate-in zoom-in"
                    >
                        Continuar ({selectedOPIds.length})
                    </button>
                )}
            </div>

            <div className="space-y-2 max-h-[500px] overflow-y-auto pr-2 custom-scrollbar">
                {ordenes.map((op) => {
                    const isSelected = selectedOP?.id === op.id;
                    const isMultiSelected = selectedOPIds.includes(op.id);

                    return (
                        <div
                            key={op.id}
                            onClick={() => isSelectionMode ? toggleSelection(op.id) : setSelectedOP(op)}
                            className={`bg-white p-6 rounded-[2rem] shadow-sm border-2 transition-all cursor-pointer group ${isSelectionMode && isMultiSelected ? 'border-blue-500 ring-4 ring-blue-50 shadow-lg' : isSelected ? 'border-blue-500 ring-4 ring-blue-50 shadow-lg' : 'border-gray-50 hover:border-gray-200 hover:shadow-md'}`}
                        >
                            <div className="flex justify-between items-start mb-1">
                                <div className="flex items-center space-x-4">
                                    {isSelectionMode && (
                                        <div className={`w-6 h-6 rounded-lg border-2 flex items-center justify-center transition-all ${isMultiSelected ? 'bg-blue-600 border-blue-600' : 'border-gray-200 bg-gray-50'}`}>
                                            {isMultiSelected && <CheckCircle2 className="w-4 h-4 text-white" />}
                                        </div>
                                    )}
                                    <div>
                                        <h4 className="font-black text-blue-600 text-lg uppercase tracking-tight">{op.numeroOP || `OP-${op.id}`}</h4>
                                        <p className="text-[11px] font-bold text-gray-500 uppercase tracking-tight">{getClientName(op)}</p>
                                    </div>
                                </div>
                                <div className="flex flex-col items-end">
                                    <span className="text-[10px] font-black text-red-800 bg-red-50 px-2 py-0.5 rounded-full">{op.progreso}%</span>
                                </div>
                            </div>

                            <div className="mt-3">
                                <div className="w-full bg-gray-100 h-1.5 rounded-full overflow-hidden shadow-inner">
                                    <div
                                        className="bg-blue-600 h-full rounded-full transition-all duration-1000"
                                        style={{ width: `${op.progreso}%` }}
                                    ></div>
                                </div>
                            </div>

                            <div className="mt-4 flex justify-between items-center text-[10px] font-bold text-gray-300">
                                <span>{op.fechaInicio || '-'}</span>
                                <span className="uppercase">{op.estado || '-'}</span>
                            </div>

                            {!isSelectionMode && isSelected && (
                                <div className="mt-6 pt-6 border-t border-dashed grid grid-cols-2 gap-4 animate-in fade-in slide-in-from-top-4">
                                    <button
                                        onClick={(e) => { e.stopPropagation(); handleModificarRegistro(); }}
                                        className="flex items-center justify-center p-4 bg-blue-600 text-white rounded-2xl text-[10px] font-black uppercase tracking-widest shadow-xl shadow-blue-200"
                                    >
                                        <CheckCircle2 className="w-3.5 h-3.5 mr-2" /> Modificar Registro
                                    </button>
                                    <button
                                        onClick={(e) => { e.stopPropagation(); handleVerDetalles(); }}
                                        className="flex items-center justify-center p-4 bg-white border-2 border-gray-100 text-gray-700 rounded-2xl text-[10px] font-black uppercase tracking-widest hover:bg-gray-50 transition-all"
                                    >
                                        <ClipboardList className="w-3.5 h-3.5 mr-2" /> Detalles de OP
                                    </button>
                                </div>
                            )}
                        </div>
                    );
                })}
                {ordenes.length === 0 && (
                    <p className="text-center py-10 text-gray-300 font-bold italic uppercase tracking-widest text-xs">
                        No hay Ordenes de Producción registradas
                    </p>
                )}
            </div>

            <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-100 p-4 flex justify-around items-center lg:hidden z-10 shadow-[0_-4px_20px_rgba(0,0,0,0.05)]">
                <div className="flex flex-col items-center text-blue-600 bg-blue-50 px-4 py-2 rounded-2xl transition-all">
                    <Factory className="w-6 h-6" />
                    <span className="text-[10px] mt-1 font-black uppercase tracking-tighter">Planta</span>
                </div>
                <div className="flex flex-col items-center text-gray-400 p-2 opacity-50">
                    <ClipboardList className="w-6 h-6" />
                    <span className="text-[10px] mt-1 font-bold">Órdenes</span>
                </div>
                <div className="flex flex-col items-center text-gray-400 p-2 opacity-50">
                    <CheckCircle2 className="w-6 h-6" />
                    <span className="text-[10px] mt-1 font-bold">Historial</span>
                </div>
            </div>
            <style>{`
                .custom-scrollbar::-webkit-scrollbar {
                    width: 4px;
                }
                .custom-scrollbar::-webkit-scrollbar-track {
                    background: transparent;
                }
                .custom-scrollbar::-webkit-scrollbar-thumb {
                    background: #e2e8f0;
                    border-radius: 10px;
                }
            `}</style>
        </div>
    );
}
