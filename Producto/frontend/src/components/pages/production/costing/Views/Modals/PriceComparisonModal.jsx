import React from 'react';
import { X, TrendingUp, Download, Activity } from 'lucide-react';

export default function PriceComparisonModal({ show, onClose }) {
    if (!show) return null;

    return (
        <div className="fixed inset-0 z-[110] flex items-center justify-center p-6 bg-slate-900/40 backdrop-blur-md animate-in fade-in duration-300">
            <div className="bg-white w-full max-w-5xl rounded-[3.5rem] shadow-2xl relative overflow-hidden animate-in zoom-in-95 duration-300 flex flex-col max-h-[90vh]">
                {/* Header del Modal */}
                <div className="bg-indigo-600 p-8 text-white">
                    <button
                        onClick={onClose}
                        className="absolute top-8 right-8 p-2 bg-white/10 rounded-full hover:bg-white/20 transition-all active:scale-95"
                    >
                        <X className="w-5 h-5" />
                    </button>
                    <div className="flex items-center space-x-4">
                        <div className="p-3 bg-white/10 rounded-2xl">
                            <TrendingUp className="w-6 h-6" />
                        </div>
                        <div>
                            <h3 className="text-2xl font-black tracking-tight uppercase italic underline decoration-indigo-400 decoration-4 underline-offset-4">Análisis Comparativo de Precios</h3>
                            <p className="text-[10px] font-black text-indigo-200 uppercase tracking-[0.3em] mt-2 ml-1">Evaluación de costos históricos por producto y proveedor</p>
                        </div>
                    </div>
                </div>

                {/* Filtros de Comparación */}
                <div className="p-8 bg-gray-50 border-b border-gray-100 grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Periodo de Análisis</label>
                        <select className="w-full p-4 bg-white border border-gray-100 rounded-2xl font-bold text-xs uppercase tracking-widest outline-none focus:ring-2 focus:ring-indigo-500 transition-all appearance-none cursor-pointer">
                            <option>Último Trimestre</option>
                            <option>Último Semestre</option>
                            <option>Año 2024 Completo</option>
                            <option>Personalizado...</option>
                        </select>
                    </div>
                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Producto / Insumo</label>
                        <select className="w-full p-4 bg-white border border-gray-100 rounded-2xl font-bold text-xs uppercase tracking-widest outline-none focus:ring-2 focus:ring-indigo-500 transition-all appearance-none cursor-pointer">
                            <option>Todos los productos</option>
                            <option>Tela Jersey 24/1</option>
                            <option>Piqué Lacoste</option>
                            <option>Gabardina 8oz</option>
                            <option>Oxford 60/40</option>
                        </select>
                    </div>
                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1">Proveedor Base</label>
                        <select className="w-full p-4 bg-white border border-gray-100 rounded-2xl font-bold text-xs uppercase tracking-widest outline-none focus:ring-2 focus:ring-indigo-500 transition-all appearance-none cursor-pointer">
                            <option>Todos los proveedores</option>
                            <option>TEXTIL PACÍFICO</option>
                            <option>TELAS COLÓN</option>
                            <option>IMPORTADORA SANTIAGO</option>
                        </select>
                    </div>
                </div>

                {/* Tabla de Resultados */}
                <div className="flex-1 overflow-y-auto p-8 custom-scrollbar bg-white">
                    <table className="w-full text-left border-separate border-spacing-y-3">
                        <thead>
                            <tr className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">
                                <th className="px-6 py-2">Fecha / ID</th>
                                <th className="px-6 py-2">Producto Analizado</th>
                                <th className="px-6 py-2">Proveedor</th>
                                <th className="px-6 py-2 text-right">Precio / Costo</th>
                                <th className="px-6 py-2 text-center">Variación</th>
                            </tr>
                        </thead>
                        <tbody>
                            {[
                                { id: 'SCOS-26001', date: '12/02/2026', product: 'Piqué Lacoste', supplier: 'TEXTIL PACÍFICO', price: 5800, variance: 0, trend: 'stable' },
                                { id: 'SCOS-26015', date: '05/01/2026', product: 'Piqué Lacoste', supplier: 'TELAS COLÓN', price: 6150, variance: 6.03, trend: 'up' },
                                { id: 'SCOS-25980', date: '20/12/2025', product: 'Piqué Lacoste', supplier: 'TEXTIL PACÍFICO', price: 5650, variance: -2.58, trend: 'down' },
                                { id: 'SCOS-25940', date: '15/11/2025', product: 'Piqué Lacoste', supplier: 'IMPORTADORA SANTIAGO', price: 6300, variance: 8.62, trend: 'up' },
                            ].map((row, i) => (
                                <tr key={i} className="group hover:bg-indigo-50/50 transition-all decoration-indigo-100">
                                    <td className="px-6 py-5 bg-gray-50/50 rounded-l-2xl border-y border-l border-gray-100 group-hover:bg-white group-hover:border-indigo-100">
                                        <div className="text-xs font-black text-gray-800">{row.id}</div>
                                        <div className="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">{row.date}</div>
                                    </td>
                                    <td className="px-6 py-5 bg-gray-50/50 border-y border-gray-100 group-hover:bg-white group-hover:border-indigo-100">
                                        <span className="text-[11px] font-black text-gray-600 uppercase tracking-tight">{row.product}</span>
                                    </td>
                                    <td className="px-6 py-5 bg-gray-50/50 border-y border-gray-100 group-hover:bg-white group-hover:border-indigo-100">
                                        <span className="text-[10px] font-black text-indigo-500 bg-indigo-50 px-3 py-1 rounded-full uppercase tracking-widest border border-indigo-100">{row.supplier}</span>
                                    </td>
                                    <td className="px-6 py-5 bg-gray-50/50 border-y border-gray-100 group-hover:bg-white group-hover:border-indigo-100 text-right">
                                        <span className="text-lg font-black text-gray-900 tracking-tighter italic">
                                            <span className="text-xs text-gray-400 not-italic mr-1">$</span>
                                            {row.price.toLocaleString()}
                                        </span>
                                    </td>
                                    <td className="px-6 py-5 bg-gray-50/50 rounded-r-2xl border-y border-r border-gray-100 group-hover:bg-white group-hover:border-indigo-100 text-center">
                                        <div className={`flex items-center justify-center gap-1 font-black text-[10px] uppercase tracking-widest ${row.trend === 'up' ? 'text-red-500' : row.trend === 'down' ? 'text-emerald-500' : 'text-gray-400'
                                            }`}>
                                            {row.trend === 'up' ? <TrendingUp className="w-3 h-3 rotate-45" /> : row.trend === 'down' ? <TrendingUp className="w-3 h-3 -rotate-45" /> : <Activity className="w-3 h-3" />}
                                            {row.variance !== 0 ? `${row.variance > 0 ? '+' : ''}${row.variance}%` : 'Base'}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* Footer del Modal */}
                <div className="p-8 bg-gray-50 border-t border-gray-100 flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                        <span className="w-3 h-3 bg-indigo-600 rounded-full animate-pulse" />
                        <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest italic">Análisis basado en 42 registros históricos validos</span>
                    </div>
                    <div className="flex space-x-3">
                        <button
                            onClick={onClose}
                            className="px-6 py-3 text-[10px] font-black text-gray-400 uppercase tracking-widest hover:text-indigo-600 transition-colors"
                        >
                            Cerrar Ventana
                        </button>
                        <button className="px-8 py-3 bg-gray-900 text-white rounded-xl text-[10px] font-black uppercase tracking-widest shadow-xl shadow-gray-200 hover:bg-black transition-all">
                            <Download className="w-4 h-4 mr-2 inline" /> Descargar Informe
                        </button>
                    </div>
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
    );
};
