import React, { useState } from 'react';
import {
    Factory,
    Search,
    ArrowLeft
} from 'lucide-react';
import { Link } from 'react-router-dom';
import ProgressCard from '../../ui/ProgressCard';
import { mockOperaciones, mockOpDetails, mockOTs, mockAllOCs, mockOpPersonalizacion } from '../../../data/mockData';
import { calculateOPStatus } from '../../../utils/statusUtils';

const TableroOP = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [healthFilter, setHealthFilter] = useState('all'); // 'all' | 'delayed' | 'normal'

    const stages = [
        { id: 'Tizado', label: 'Tizado', key: 'tizado' },
        { id: 'Corte', label: 'Corte', key: 'fin_corte' },
        { id: 'Logo', label: 'Logo', key: 'regreso_logo' },
        { id: 'Taller Externo', label: 'Taller Ext.', key: 'fin_taller' },
        { id: 'Terminaciones', label: 'Term.', key: 'fin_op' },
        { id: 'Personalizado', label: 'Pers.', key: 'personalizado' },
        { id: 'Entrega', label: 'Entrega', key: 'entrega_bodega' },
    ];

    const filteredData = mockOperaciones.filter(op => {
        const matchesSearch = (op.idOP || op.id || '').toString().toLowerCase().includes(searchQuery.toLowerCase()) ||
            (op.cliente || '').toString().toLowerCase().includes(searchQuery.toLowerCase()) ||
            (op.producto || '').toString().toLowerCase().includes(searchQuery.toLowerCase());

        const { estadoGlobal } = calculateOPStatus(op, mockOTs, mockAllOCs, mockOpPersonalizacion);
        const isDelayed = estadoGlobal === 'ATRASADA';

        const matchesHealth = healthFilter === 'all'
            ? true
            : healthFilter === 'delayed' ? isDelayed : !isDelayed;

        return matchesSearch && matchesHealth;
    });

    return (
        <div className="max-w-[1800px] mx-auto space-y-8 animate-in fade-in duration-700 pb-12">

            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                    <div className="flex items-center space-x-2 text-blue-600 mb-2">
                        <Link to="/dashboard-op" className="hover:underline flex items-center text-xs font-bold uppercase tracking-widest">
                            <ArrowLeft className="w-3 h-3 mr-1" /> Volver a Control Planta
                        </Link>
                    </div>
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-black text-gray-800 tracking-tight flex items-center">
                                <Factory className="w-8 h-8 mr-3 text-blue-600" />
                                Seguimiento de OP
                            </h1>
                            <p className="text-gray-500 font-medium mt-1">
                                Monitoreo en tiempo real del flujo de producción
                            </p>
                        </div>
                    </div>
                </div>

                <div className="flex flex-col sm:flex-row gap-4 items-center">
                    {/* Health Filter Selectors */}
                    <div className="flex items-center bg-gray-100 p-1 rounded-2xl border border-gray-200 shadow-inner">
                        <button
                            onClick={() => setHealthFilter('all')}
                            className={`px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${healthFilter === 'all' ? 'bg-white text-gray-800 shadow-sm' : 'text-gray-400 hover:text-gray-600'}`}
                        >
                            Todas
                        </button>
                        <button
                            onClick={() => setHealthFilter('delayed')}
                            className={`px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${healthFilter === 'delayed' ? 'bg-red-600 text-white shadow-lg' : 'text-gray-400 hover:text-red-500'}`}
                        >
                            Atrasadas
                        </button>
                        <button
                            onClick={() => setHealthFilter('normal')}
                            className={`px-4 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${healthFilter === 'normal' ? 'bg-green-600 text-white shadow-lg' : 'text-gray-400 hover:text-green-600'}`}
                        >
                            Flujo Normal
                        </button>
                    </div>

                    <div className="relative group">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 group-focus-within:text-blue-600 transition-colors" />
                        <input
                            type="text"
                            placeholder="Buscar OP, Cliente, Producto..."
                            className="pl-12 pr-4 py-3 bg-white border border-gray-200 rounded-xl text-sm font-medium focus:ring-2 focus:ring-blue-500 outline-none shadow-sm w-full sm:w-80"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                </div>
            </div>

            {/* Progress List View */}
            <div className="space-y-4 max-w-6xl">
                {filteredData.map(op => (
                    <ProgressCard
                        key={op.id}
                        item={op}
                        details={mockOpDetails}
                        stages={stages}
                        type="op"
                    />
                ))}

                {filteredData.length === 0 && (
                    <div className="p-20 text-center bg-white rounded-[3rem] border border-dashed border-gray-200 shadow-sm">
                        <Search className="w-12 h-12 text-gray-200 mx-auto mb-4" />
                        <p className="text-gray-400 font-bold uppercase tracking-widest text-xs">No se encontraron registros activos</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default TableroOP;
