import React from 'react';
import { X, Activity, Clock, ArrowRight, CheckCircle2 } from 'lucide-react';

export default function DashboardCosteos({ show, onClose, dashboardStats }) {
    if (!show) return null;

    return (
        <div className="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-slate-900/60 backdrop-blur-md animate-in fade-in duration-300">
            <div className="bg-white w-full max-w-4xl rounded-[3.5rem] p-12 shadow-2xl animate-in zoom-in-95 duration-300 relative border border-white/50">
                <button
                    onClick={onClose}
                    className="absolute top-10 right-10 p-4 bg-gray-50 rounded-2xl hover:bg-red-50 hover:text-red-500 transition-all active:scale-90"
                >
                    <X className="w-6 h-6 text-gray-400 shadow-sm" />
                </button>

                <div className="mb-12">
                    <div className="flex items-center space-x-4 mb-3">
                        <div className="w-14 h-14 bg-green-600 rounded-3xl flex items-center justify-center shadow-xl shadow-green-100">
                            <Activity className="w-7 h-7 text-white" />
                        </div>
                        <div>
                            <h2 className="text-3xl font-black text-gray-800 tracking-tighter italic uppercase">Dashboard de Rendimiento</h2>
                            <p className="text-[10px] font-black text-gray-400 uppercase tracking-[0.3em] ml-1">Estado actual de análisis de costos</p>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    {/* Pendientes */}
                    <div className="bg-gray-50 p-8 rounded-[2.5rem] border border-gray-100 hover:shadow-xl hover:-translate-y-1 transition-all duration-500 flex flex-col h-[400px]">
                        <div className="flex items-center justify-between mb-6">
                            <div className="w-12 h-12 bg-amber-100 rounded-2xl flex items-center justify-center text-amber-600">
                                <Clock className="w-6 h-6" />
                            </div>
                            <div className="text-right">
                                <p className="text-[10px] font-black text-amber-600 bg-amber-50 px-3 py-1 rounded-full uppercase tracking-widest border border-amber-100 inline-block">{dashboardStats.pending}</p>
                            </div>
                        </div>
                        <h4 className="text-[11px] font-black text-gray-400 uppercase tracking-widest mb-4">Costeos Pendientes</h4>

                        <div className="flex-1 overflow-y-auto pr-2 space-y-2 custom-scrollbar">
                            {dashboardStats.pendingItems.length > 0 ? (
                                dashboardStats.pendingItems.map((item, idx) => (
                                    <div key={idx} className="bg-white p-3 rounded-xl border border-gray-100 shadow-sm flex items-center justify-between group">
                                        <span className="text-[10px] font-black text-gray-700">{item.id_solicitud || item.id}</span>
                                        <ArrowRight className="w-3 h-3 text-gray-300 group-hover:text-amber-500 transition-colors" />
                                    </div>
                                ))
                            ) : (
                                <p className="text-[10px] font-bold text-gray-300 italic uppercase">Sin pendientes</p>
                            )}
                        </div>
                    </div>

                    {/* En Proceso */}
                    <div className="bg-blue-50/50 p-8 rounded-[2.5rem] border border-blue-100/50 hover:shadow-xl hover:-translate-y-1 transition-all duration-500 flex flex-col h-[400px]">
                        <div className="flex items-center justify-between mb-6">
                            <div className="w-12 h-12 bg-blue-100 rounded-2xl flex items-center justify-center text-blue-600">
                                <Activity className="w-6 h-6" />
                            </div>
                            <div className="text-right">
                                <p className="text-[10px] font-black text-blue-600 bg-blue-50 px-3 py-1 rounded-full uppercase tracking-widest border border-blue-100 inline-block">{dashboardStats.inProgress}</p>
                            </div>
                        </div>
                        <h4 className="text-[11px] font-black text-gray-400 uppercase tracking-widest mb-4">En Proceso</h4>

                        <div className="flex-1 overflow-y-auto pr-2 space-y-2 custom-scrollbar">
                            {dashboardStats.inProgressItems.length > 0 ? (
                                dashboardStats.inProgressItems.map((item, idx) => (
                                    <div key={idx} className="bg-white p-3 rounded-xl border border-blue-100/50 shadow-sm flex items-center justify-between group">
                                        <span className="text-[10px] font-black text-gray-700">{item.id}</span>
                                        <ArrowRight className="w-3 h-3 text-gray-300 group-hover:text-blue-500 transition-colors" />
                                    </div>
                                ))
                            ) : (
                                <p className="text-[10px] font-bold text-gray-300 italic uppercase">Sin procesos</p>
                            )}
                        </div>
                    </div>

                    {/* Terminados */}
                    <div className="bg-green-50/50 p-8 rounded-[2.5rem] border border-green-100/50 hover:shadow-xl hover:-translate-y-1 transition-all duration-500 flex flex-col h-[400px]">
                        <div className="flex items-center justify-between mb-6">
                            <div className="w-12 h-12 bg-green-600 rounded-2xl flex items-center justify-center text-white shadow-lg shadow-green-100">
                                <CheckCircle2 className="w-6 h-6" />
                            </div>
                            <div className="text-right">
                                <p className="text-[10px] font-black text-green-600 bg-green-50 px-3 py-1 rounded-full uppercase tracking-widest border border-green-100 inline-block">{dashboardStats.finished}</p>
                            </div>
                        </div>
                        <h4 className="text-[11px] font-black text-gray-400 uppercase tracking-widest mb-4">Costos Cerrados</h4>

                        <div className="flex-1 overflow-y-auto pr-2 space-y-2 custom-scrollbar">
                            {dashboardStats.finishedItems.length > 0 ? (
                                dashboardStats.finishedItems.map((item, idx) => (
                                    <div key={idx} className="bg-white p-3 rounded-xl border border-green-100/50 shadow-sm flex items-center justify-between group">
                                        <span className="text-[10px] font-black text-gray-700">{item.id}</span>
                                        <ArrowRight className="w-3 h-3 text-gray-300 group-hover:text-green-500 transition-colors" />
                                    </div>
                                ))
                            ) : (
                                <p className="text-[10px] font-bold text-gray-300 italic uppercase">Sin cerrados</p>
                            )}
                        </div>
                    </div>
                </div>

                <div className="mt-12 p-8 bg-gray-900 rounded-[2.5rem] text-white flex items-center justify-between">
                    <div>
                        <h5 className="text-sm font-black italic uppercase tracking-widest mb-1">Eficiencia Operativa</h5>
                        <p className="text-[10px] text-gray-400 font-bold uppercase tracking-widest">Ratio de conversión de solicitudes a costeos</p>
                    </div>
                    <div className="text-3xl font-black italic tracking-tighter text-green-400">
                        {Math.round((dashboardStats.finished / (dashboardStats.pending + dashboardStats.finished || 1)) * 100)}%
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
                    .custom-scrollbar::-webkit-scrollbar-thumb:hover {
                        background: #cbd5e1;
                    }
                `}</style>
            </div>
        </div>
    );
};
