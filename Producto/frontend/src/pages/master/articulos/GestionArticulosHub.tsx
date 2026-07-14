import React, { useState } from 'react';
import { Layers, Scissors, Package, Sliders, Box } from 'lucide-react';
import ParametriaTextil from './ParametriaTextil';
import GestionTelas from './GestionTelas';
import GestionInsumos from './GestionInsumos';
import GestionPrendas from './GestionPrendas';

export default function GestionArticulosHub() {
  const [activeTab, setActiveTab] = useState('telas');

  const renderContent = () => {
    switch (activeTab) {
      case 'parametria':
        return <ParametriaTextil />;
      case 'telas':
        return <GestionTelas />;
      case 'insumos':
        return <GestionInsumos />;
      case 'prendas':
        return <GestionPrendas />;
      default:
        return null;
    }
  };

  return (
    <div className="flex flex-col h-full bg-white rounded-2xl border border-zinc-200 shadow-sm overflow-hidden">
      {/* Header Hub */}
      <div className="flex items-center justify-between px-6 py-5 border-b border-zinc-200 bg-zinc-50/50 shrink-0">
        <div className="flex items-center gap-3">
          <div className="p-2.5 rounded-xl bg-zinc-900 text-white shadow-sm">
            <Box size={22} strokeWidth={1.75} />
          </div>
          <div>
            <h1 className="text-xl font-extrabold tracking-tight text-zinc-900 uppercase">
              Administrador Central de Artículos
            </h1>
            <p className="text-xs text-zinc-500 font-medium mt-0.5">
              Gestión avanzada de Telas, Insumos, Prendas y Configuración Textil
            </p>
          </div>
        </div>
      </div>

      {/* Tabs Navigation */}
      <div className="px-6 border-b border-zinc-200 flex gap-6 bg-white shrink-0">
        <button
          onClick={() => setActiveTab('telas')}
          className={`py-4 text-[11px] font-bold uppercase tracking-widest transition-all border-b-2 flex items-center gap-2 ${
            activeTab === 'telas'
              ? 'border-zinc-900 text-zinc-900'
              : 'border-transparent text-zinc-400 hover:text-zinc-600'
          }`}
        >
          <Layers size={14} />
          Catálogo de Telas
        </button>
        <button
          onClick={() => setActiveTab('insumos')}
          className={`py-4 text-[11px] font-bold uppercase tracking-widest transition-all border-b-2 flex items-center gap-2 ${
            activeTab === 'insumos'
              ? 'border-zinc-900 text-zinc-900'
              : 'border-transparent text-zinc-400 hover:text-zinc-600'
          }`}
        >
          <Scissors size={14} />
          Insumos y Accesorios
        </button>
        <button
          onClick={() => setActiveTab('prendas')}
          className={`py-4 text-[11px] font-bold uppercase tracking-widest transition-all border-b-2 flex items-center gap-2 ${
            activeTab === 'prendas'
              ? 'border-zinc-900 text-zinc-900'
              : 'border-transparent text-zinc-400 hover:text-zinc-600'
          }`}
        >
          <Package size={14} />
          Prendas (Prod. Terminado)
        </button>
        <div className="w-px h-6 bg-zinc-200 my-auto mx-2" />
        <button
          onClick={() => setActiveTab('parametria')}
          className={`py-4 text-[11px] font-bold uppercase tracking-widest transition-all border-b-2 flex items-center gap-2 ${
            activeTab === 'parametria'
              ? 'border-zinc-900 text-zinc-900'
              : 'border-transparent text-zinc-400 hover:text-zinc-600'
          }`}
        >
          <Sliders size={14} />
          Parametría Textil
        </button>
      </div>

      {/* Sub-module Workspace */}
      <div className="flex-1 bg-zinc-50/50 p-6 overflow-hidden">
        {renderContent()}
      </div>
    </div>
  );
}
