import React from 'react';
import { X, Activity, Clock, ArrowRight, CheckCircle2 } from 'lucide-react';

export default function DashboardCosteos({ show, onClose, dashboardStats }) {
    if (!show) return null;

    return (
        <div className="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-foreground/60 backdrop-blur-md animate-in fade-in duration-300">
            <div className="bg-card w-full max-w-4xl rounded-[3.5rem] p-12 shadow-2xl animate-in zoom-in-95 duration-300 relative border border-border">
                <button
                    onClick={onClose}
                    className="absolute top-10 right-10 p-4 bg-muted rounded-2xl hover:bg-destructive/10 hover:text-destructive transition-all active:scale-90"
                >
                    <X className="w-6 h-6 text-muted-foreground shadow-sm" />
                </button>

                <div className="mb-12">
                    <div className="flex items-center space-x-4 mb-3">
                        <div className="w-14 h-14 bg-success rounded-3xl flex items-center justify-center shadow-xl shadow-success/20">
                            <Activity className="w-7 h-7 text-white" />
                        </div>
                        <div>
                            <h2 className="text-3xl font-black text-foreground tracking-tighter italic uppercase">Dashboard de Rendimiento</h2>
                            <p className="text-[10px] font-black text-muted-foreground uppercase tracking-[0.3em] ml-1">Estado actual de análisis de costos</p>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    {/* Pendientes */}
                    <div className="bg-muted p-8 rounded-[2.5rem] border border-border hover:shadow-xl hover:-translate-y-1 transition-all duration-500 flex flex-col h-[400px]">
                        <div className="flex items-center justify-between mb-6">
                            <div className="w-12 h-12 bg-warning/10 rounded-2xl flex items-center justify-center text-warning">
                                <Clock className="w-6 h-6" />
                            </div>
                            <div className="text-right">
                                <p className="text-[10px] font-black text-warning bg-warning/10 px-3 py-1 rounded-full uppercase tracking-widest border border-warning/20 inline-block">{dashboardStats.pending}</p>
                            </div>
                        </div>
                        <h4 className="text-[11px] font-black text-muted-foreground uppercase tracking-widest mb-4">Costeos Pendientes</h4>

                        <div className="flex-1 overflow-y-auto pr-2 space-y-2 custom-scrollbar">
                            {dashboardStats.pendingItems.length > 0 ? (
                                dashboardStats.pendingItems.map((item, idx) => (
                                    <div key={idx} className="bg-card p-3 rounded-xl border border-border shadow-sm flex items-center justify-between group">
                                        <span className="text-[10px] font-black text-foreground">{item.id_solicitud || item.id}</span>
                                        <ArrowRight className="w-3 h-3 text-muted-foreground group-hover:text-warning transition-colors" />
                                    </div>
                                ))
                            ) : (
                                <p className="text-[10px] font-bold text-muted-foreground italic uppercase">Sin pendientes</p>
                            )}
                        </div>
                    </div>

                    {/* En Proceso */}
                    <div className="bg-primary/5 p-8 rounded-[2.5rem] border border-primary/10 hover:shadow-xl hover:-translate-y-1 transition-all duration-500 flex flex-col h-[400px]">
                        <div className="flex items-center justify-between mb-6">
                            <div className="w-12 h-12 bg-primary/10 rounded-2xl flex items-center justify-center text-primary">
                                <Activity className="w-6 h-6" />
                            </div>
                            <div className="text-right">
                                <p className="text-[10px] font-black text-primary bg-primary/10 px-3 py-1 rounded-full uppercase tracking-widest border border-primary/20 inline-block">{dashboardStats.inProgress}</p>
                            </div>
                        </div>
                        <h4 className="text-[11px] font-black text-muted-foreground uppercase tracking-widest mb-4">En Proceso</h4>

                        <div className="flex-1 overflow-y-auto pr-2 space-y-2 custom-scrollbar">
                            {dashboardStats.inProgressItems.length > 0 ? (
                                dashboardStats.inProgressItems.map((item, idx) => (
                                    <div key={idx} className="bg-card p-3 rounded-xl border border-primary/10 shadow-sm flex items-center justify-between group">
                                        <span className="text-[10px] font-black text-foreground">{item.id}</span>
                                        <ArrowRight className="w-3 h-3 text-muted-foreground group-hover:text-primary transition-colors" />
                                    </div>
                                ))
                            ) : (
                                <p className="text-[10px] font-bold text-muted-foreground italic uppercase">Sin procesos</p>
                            )}
                        </div>
                    </div>

                    {/* Terminados */}
                    <div className="bg-success/5 p-8 rounded-[2.5rem] border border-success/10 hover:shadow-xl hover:-translate-y-1 transition-all duration-500 flex flex-col h-[400px]">
                        <div className="flex items-center justify-between mb-6">
                            <div className="w-12 h-12 bg-success rounded-2xl flex items-center justify-center text-white shadow-lg shadow-success/20">
                                <CheckCircle2 className="w-6 h-6" />
                            </div>
                            <div className="text-right">
                                <p className="text-[10px] font-black text-success bg-success/10 px-3 py-1 rounded-full uppercase tracking-widest border border-success/20 inline-block">{dashboardStats.finished}</p>
                            </div>
                        </div>
                        <h4 className="text-[11px] font-black text-muted-foreground uppercase tracking-widest mb-4">Costos Cerrados</h4>

                        <div className="flex-1 overflow-y-auto pr-2 space-y-2 custom-scrollbar">
                            {dashboardStats.finishedItems.length > 0 ? (
                                dashboardStats.finishedItems.map((item, idx) => (
                                    <div key={idx} className="bg-card p-3 rounded-xl border border-success/10 shadow-sm flex items-center justify-between group">
                                        <span className="text-[10px] font-black text-foreground">{item.id}</span>
                                        <ArrowRight className="w-3 h-3 text-muted-foreground group-hover:text-success transition-colors" />
                                    </div>
                                ))
                            ) : (
                                <p className="text-[10px] font-bold text-muted-foreground italic uppercase">Sin cerrados</p>
                            )}
                        </div>
                    </div>
                </div>

                <div className="mt-12 p-8 bg-sidebar rounded-[2.5rem] text-sidebar-foreground flex items-center justify-between">
                    <div>
                        <h5 className="text-sm font-black italic uppercase tracking-widest mb-1 text-white">Eficiencia Operativa</h5>
                        <p className="text-[10px] text-sidebar-muted font-bold uppercase tracking-widest">Ratio de conversión de solicitudes a costeos</p>
                    </div>
                    <div className="text-3xl font-black italic tracking-tighter text-success">
                        {Math.round((dashboardStats.finished / (dashboardStats.pending + dashboardStats.finished || 1)) * 100)}%
                    </div>
                </div>
            </div>
        </div>
    );
};
