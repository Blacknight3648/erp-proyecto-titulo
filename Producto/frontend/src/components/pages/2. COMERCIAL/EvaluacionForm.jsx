import React from 'react';
import {
    CreditCard,
    Truck,
    ShieldAlert,
    Calendar,
    Wallet,
    Percent
} from 'lucide-react';

export default function EvaluacionForm({ data, onChange }) {
    const handleConditionChange = (field, value) => {
        onChange({
            ...data,
            condiciones: {
                ...data.condiciones,
                [field]: value
            }
        });
    };

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="space-y-6">
                <div className="bg-gray-50/50 p-6 rounded-[2rem] border border-gray-100">
                    <div className="flex items-center space-x-3 mb-6">
                        <div className="w-8 h-8 bg-indigo-100 rounded-lg flex items-center justify-center">
                            <Wallet className="w-4 h-4 text-indigo-600" />
                        </div>
                        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest">Estructura Pagos</h4>
                    </div>

                    <div className="space-y-4">
                        <div>
                            <div className="flex justify-between items-center mb-1 ml-1">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Anticipo / Pie (%)</label>
                                <span className="text-xs font-black text-indigo-600">{data.condiciones.anticipo}%</span>
                            </div>
                            <input
                                type="range"
                                min="0"
                                max="100"
                                step="5"
                                value={data.condiciones.anticipo}
                                onChange={(e) => handleConditionChange('anticipo', parseInt(e.target.value))}
                                className="w-full h-1.5 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-indigo-600"
                            />
                        </div>

                        <div className="bg-white p-4 rounded-xl border border-gray-200 flex justify-between items-center">
                            <span className="text-[10px] font-bold text-gray-400 uppercase">Saldo a Entrega</span>
                            <span className="text-xs font-black text-gray-700">{100 - data.condiciones.anticipo}%</span>
                        </div>
                    </div>
                </div>

                <div className="bg-gray-50/50 p-6 rounded-[2rem] border border-gray-100">
                    <div className="flex items-center space-x-3 mb-6">
                        <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                            <Truck className="w-4 h-4 text-blue-600" />
                        </div>
                        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest">Logística</h4>
                    </div>

                    <div className="space-y-4">
                        <select
                            className="w-full px-4 py-3 bg-white border border-gray-200 rounded-xl text-xs font-bold focus:ring-2 focus:ring-blue-500 transition-all outline-none"
                            value={data.condiciones.flete}
                            onChange={(e) => handleConditionChange('flete', e.target.value)}
                        >
                            <option value="Cliente">Flete por cuenta del Cliente</option>
                            <option value="Incluido">Flete Incluido en el precio</option>
                            <option value="Por cobrar">Envío por cobrar (Starken/Chileexpress)</option>
                        </select>
                    </div>
                </div>
            </div>

            <div className="space-y-6">
                <div className="bg-gray-50/50 p-6 rounded-[2rem] border border-gray-100">
                    <div className="flex items-center space-x-3 mb-6">
                        <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                            <ShieldAlert className="w-4 h-4 text-green-600" />
                        </div>
                        <h4 className="text-sm font-black text-gray-800 uppercase tracking-widest">Garantía / Plazo</h4>
                    </div>

                    <div className="space-y-4">
                        <div>
                            <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1 mb-2 block">Tiempo Garantía</label>
                            <input
                                type="text"
                                className="w-full px-4 py-3 bg-white border border-gray-200 rounded-xl text-xs font-bold focus:ring-2 focus:ring-green-500 transition-all outline-none"
                                value={data.condiciones.garantia}
                                onChange={(e) => handleConditionChange('garantia', e.target.value)}
                                placeholder="Ej: 30 días, 6 meses..."
                            />
                        </div>

                        <div>
                            <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest ml-1 mb-2 block">Compromiso Entrega</label>
                            <div className="relative">
                                <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                                <input
                                    type="date"
                                    className="w-full pl-10 pr-4 py-3 bg-white border border-gray-200 rounded-xl text-xs font-bold focus:ring-2 focus:ring-green-500 transition-all outline-none"
                                    value={data.condiciones.plazoEntrega}
                                    onChange={(e) => handleConditionChange('plazoEntrega', e.target.value)}
                                />
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
}
