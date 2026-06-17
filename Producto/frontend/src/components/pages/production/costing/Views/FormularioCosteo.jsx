import React from 'react';
import { 
    ChevronLeft, 
    Save, 
    Layers, 
    Activity, 
    Coins, 
    FileText, 
    ChevronUp, 
    ChevronDown, 
    TrendingDown,
    ShieldCheck
} from 'lucide-react';
import CosteoTable from '../../../commercial/costing/components/shared/CosteoTable';
import { FIELD_LABELS } from '../../../../../hooks/usePlantillas';

export default function FormularioCosteo({
    onBack,
    selectedRecord,
    currentSolicitud,
    costeoVersion,
    handleValidateCostos,
    totalMateriales,
    totalMO,
    totalCostosFijos,
    totalGeneral,
    clientes,
    activeTab,
    setActiveTab,
    insumos,
    handleUpdateItem,
    handleRemoveItem,
    handleAddItem,
    showTelasSCOS,
    setShowTelasSCOS,
    showAccesoriosSCOS,
    setShowAccesoriosSCOS,
    moPrenda, setMoPrenda,
    moCinta, setMoCinta,
    moCosturaSellada, setMoCosturaSellada,
    moAcolchado, setMoAcolchado,
    costoHilo, setCostoHilo,
    costoMoPropia, setCostoMoPropia,
    costoGratificacion, setCostoGratificacion,
    costoEtiqueta, setCostoEtiqueta,
    costoEmbalaje, setCostoEmbalaje,
    costoFlete, setCostoFlete
}) {
    const TabButton = ({ id, label, icon: Icon, color = 'green' }) => (
        <button
            onClick={() => setActiveTab(id)}
            className={`flex items-center gap-2 px-6 py-4 border-b-2 transition-all whitespace-nowrap ${activeTab === id
                ? `border-${color}-500 text-${color}-600 bg-${color}-50/30`
                : 'border-transparent text-gray-400 hover:text-gray-600 hover:bg-gray-50'
                }`}
        >
            <Icon className={`w-4 h-4 ${activeTab === id ? `text-${color}-600` : 'text-gray-400'}`} />
            <span className="text-[10px] font-black uppercase tracking-widest">{label}</span>
        </button>
    );

    return (
        <div className="min-h-screen bg-transparent pt-4 pb-4 pr-4 pl-1 animate-in slide-in-from-bottom-8 duration-700">
            {/* Header & Financial Summary Unified - Vertical Tiered Layout */}
            <div className="max-w-full pr-96 mb-10 animate-in fade-in slide-in-from-top-4 duration-1000 space-y-4">
                {/* Tier 1: Navigation & Title */}
                <div className="bg-white p-6 rounded-[2.5rem] border border-gray-100 shadow-xl flex items-center justify-between ring-1 ring-black/[0.02]">
                    <div className="flex items-center gap-6 w-full relative">
                        <button
                            onClick={onBack}
                            className="w-12 h-12 bg-gray-50 rounded-2xl flex items-center justify-center hover:bg-gray-100 transition-all group"
                        >
                            <ChevronLeft className="w-6 h-6 text-gray-400 group-hover:text-green-600 group-hover:-translate-x-0.5 transition-all" />
                        </button>
                        <div>
                            <div className="flex items-center gap-3">
                                <h1 className="text-3xl font-black text-gray-800 tracking-tight italic">
                                    {selectedRecord ? `Análisis ${currentSolicitud?.numero || selectedRecord.id}` : 'Nuevo Análisis de Costos'}
                                </h1>
                                <div className="flex bg-blue-50 px-4 py-1.5 rounded-full border border-blue-100 items-center gap-2 shadow-sm">
                                    <span className="text-[11px] font-black text-blue-500 uppercase tracking-widest leading-none">{currentSolicitud?.clienteNombre || 'S/N'}</span>
                                </div>
                                {costeoVersion != null && (
                                    <div className="flex bg-indigo-50 px-4 py-1.5 rounded-full border border-indigo-100 items-center gap-2 shadow-sm">
                                        <span className="text-[11px] font-black text-indigo-500 uppercase tracking-widest leading-none">Versión v{costeoVersion}</span>
                                    </div>
                                )}
                            </div>
                            <p className="text-[11px] font-bold text-gray-400 uppercase tracking-widest mt-1 italic leading-none">Determinación de costo directo y materiales</p>

                            {(selectedRecord || currentSolicitud) && (
                                <button
                                    onClick={handleValidateCostos}
                                    className="absolute top-1/2 right-0 translate-y-[calc(-50%-4px)] group h-[64px] bg-green-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-lg shadow-green-100 hover:bg-green-700 transition-all flex items-center justify-self-end gap-3 active:scale-95 px-6 max-w-[160px]"
                                >
                                    <Save className="w-5 h-5 group-hover:rotate-12 transition-transform flex-shrink-0" />
                                    <span className="leading-tight text-center">Finalizar y Guardar</span>
                                </button>
                            )}
                        </div>
                    </div>
                </div>

                {/* Tier 2: Financial Summary & Actions - Compressed Single Row Layout */}
                <div className="bg-white p-6 rounded-[2.5rem] border border-gray-100 shadow-xl flex items-center justify-between gap-10 ring-1 ring-black/[0.02]">
                    {/* Metrics Group */}
                    <div className="flex items-center gap-16 px-4">
                        {[
                            { label: "Materiales", value: totalMateriales, color: "blue" },
                            { label: "Mano de Obra", value: totalMO, color: "green" },
                            { label: "Costos Fijos", value: totalCostosFijos, color: "orange" }
                        ].map((stat, i) => (
                            <div key={i} className="flex flex-col min-w-[100px]">
                                <span className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">{stat.label}</span>
                                <div className="flex items-baseline gap-1">
                                    <span className="text-gray-400 text-sm font-bold italic">$</span>
                                    <span className="text-xl font-black text-slate-900 tracking-tighter italic">
                                        {stat.value.toLocaleString('es-CL')}
                                    </span>
                                </div>
                                <div
                                    className="h-1 w-8 mt-2 rounded-full"
                                    style={{ backgroundColor: stat.color === 'blue' ? '#60a5fa' : stat.color === 'green' ? '#4ade80' : '#fb923c' }}
                                />
                            </div>
                        ))}
                    </div>

                    {/* Total & Action Group */}
                    <div className="flex items-center gap-4 border-l border-gray-100 pl-8">
                        <div className="flex flex-col bg-slate-900 px-8 py-4 rounded-3xl shadow-lg relative overflow-hidden group min-w-[180px]">
                            <div className="absolute inset-0 bg-green-500/10 opacity-0 group-hover:opacity-100 transition-opacity" />
                            <span className="text-[8px] font-black text-green-400 uppercase tracking-widest relative z-10">Costo total de producción</span>
                            <div className=" flex items-baseline gap-1 relative z-10 text-white leading-none mt-1">
                                <span className="text-lg font-bold italic opacity-50">$</span>
                                <span className="text-3xl font-black tracking-tighter italic">
                                    {totalGeneral.toLocaleString('es-CL')}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="max-w-full grid grid-cols-1 xl:grid-cols-4 gap-8 pr-96">
                <div className="xl:col-span-4 space-y-8">
                    {/* Main Form Area */}
                    <div className="bg-white p-8 rounded-[2.5rem] shadow-xl border border-gray-100">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
                            <div className="space-y-6">
                                <div className="space-y-2">
                                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">Cliente Solicitante</label>
                                    <input
                                        type="text"
                                        disabled
                                        className="w-full p-4 bg-gray-50 border-none rounded-2xl text-md font-bold text-gray-400 cursor-not-allowed"
                                        value={currentSolicitud?.clienteNombre || clientes.find(c => (c.clienteId || c.id)?.toString() === currentSolicitud?.clienteId?.toString())?.nombreCliente || 'N/A'}
                                    />
                                </div>
                                <div className="space-y-2">
                                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">Vendedor Asociado</label>
                                    <input
                                        type="text"
                                        disabled
                                        className="w-full p-4 bg-gray-50 border-none rounded-2xl text-md font-bold text-gray-400 cursor-not-allowed"
                                        value={currentSolicitud?.vendedorNombre || currentSolicitud?.vendedorId || 'N/A'}
                                    />
                                </div>
                            </div>
                            <div className="space-y-6">
                                <div className="space-y-2">
                                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">Artículo / Especificación</label>
                                    <input
                                        type="text"
                                        disabled
                                        className="w-full p-4 bg-gray-900 border-none rounded-2xl text-md font-bold text-gray-400 cursor-not-allowed"
                                        value={currentSolicitud?.articuloDescripcion || 'N/A'}
                                    />
                                </div>
                                <div className="space-y-2">
                                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">Cantidad Requerida</label>
                                    <input
                                        type="text"
                                        disabled
                                        className="w-full p-4 bg-blue-50 border-none rounded-2xl text-lg font-black text-blue-400 cursor-not-allowed"
                                        value={`${currentSolicitud?.cantidad || 0} und`}
                                    />
                                </div>
                            </div>
                        </div>

                        {/* Tabs Navigation */}
                        <div className="bg-white rounded-3xl border border-gray-100 overflow-hidden mb-8">
                            <div className="flex bg-gray-50/50 overflow-x-auto">
                                <TabButton id="materiales" label="Materiales" icon={Layers} color="green" />
                                <TabButton id="mo" label="Mano de Obra" icon={Activity} color="blue" />
                                <TabButton id="costosFijos" label="Costos Fijos" icon={Coins} color="orange" />
                            </div>

                            <div className="p-6 min-h-[400px]">
                                {activeTab === 'materiales' && (
                                    <div className="space-y-12">
                                        <div className="space-y-4">
                                            <CosteoTable
                                                title="Detalle de telas"
                                                insumos={insumos.filter(i => i.categoryId === 'telas')}
                                                onUpdateItem={handleUpdateItem}
                                                onRemoveItem={handleRemoveItem}
                                                onAddItem={() => handleAddItem('telas')}
                                            />

                                            {/* SCOS Telas Colapsable */}
                                            <button
                                                onClick={() => setShowTelasSCOS(!showTelasSCOS)}
                                                className="w-full flex items-center justify-between p-3 bg-orange-50/50 rounded-xl border border-orange-100 hover:bg-orange-100/50 transition-all group"
                                            >
                                                <span className="text-[9px] font-black text-orange-600 uppercase tracking-widest flex items-center gap-2">
                                                    <FileText className="w-3.5 h-3.5" />
                                                    Solicitud de costos - Telas
                                                </span>
                                                {showTelasSCOS ? <ChevronUp className="w-3.5 h-3.5 text-orange-400" /> : <ChevronDown className="w-3.5 h-3.5 text-orange-400" />}
                                            </button>

                                            {showTelasSCOS && (
                                                <div className="grid grid-cols-1 gap-2 animate-in slide-in-from-top-2 duration-300">
                                                    {(currentSolicitud?.plantillas?.[0]?.telas || []).map((t, i) => (
                                                        <div key={i} className="flex flex-wrap items-center gap-4 p-4 bg-orange-50/20 rounded-2xl border border-orange-100/30">
                                                            <div className="flex-1 min-w-[200px]">
                                                                <span className="text-[8px] font-black text-orange-400 uppercase tracking-widest block">Aplicación / Tela</span>
                                                                <span className="text-xs font-bold text-gray-700 uppercase">{t.aplicacion}: {t.nombre}</span>
                                                            </div>
                                                            <div className="w-20">
                                                                <span className="text-[8px] font-black text-orange-400 uppercase tracking-widest block">Peso</span>
                                                                <span className="text-xs font-bold text-gray-700 uppercase">{t.peso || '0'} {t.unidadMedida || 'gr'}</span>
                                                            </div>
                                                        </div>
                                                    ))}
                                                    {(currentSolicitud?.plantillas?.[0]?.telas || []).length === 0 && (
                                                        <div className="text-center py-4 bg-gray-50 rounded-xl text-[10px] text-gray-400 font-bold uppercase italic border border-dashed border-gray-100">Sin telas en solicitud</div>
                                                    )}
                                                </div>
                                            )}
                                        </div>

                                        <div className="space-y-4">
                                            <CosteoTable
                                                title="Detalle de accesorios"
                                                insumos={insumos.filter(i => i.categoryId === 'accesorios')}
                                                onUpdateItem={handleUpdateItem}
                                                onRemoveItem={handleRemoveItem}
                                                onAddItem={() => handleAddItem('accesorios')}
                                            />

                                            {/* SCOS Accesorios Colapsable */}
                                            <button
                                                onClick={() => setShowAccesoriosSCOS(!showAccesoriosSCOS)}
                                                className="w-full flex items-center justify-between p-3 bg-blue-50/50 rounded-xl border border-blue-100 hover:bg-blue-100/50 transition-all group"
                                            >
                                                <span className="text-[9px] font-black text-blue-600 uppercase tracking-widest flex items-center gap-2">
                                                    <FileText className="w-3.5 h-3.5" />
                                                    Solicitud de costos - Accesorios
                                                </span>
                                                {showAccesoriosSCOS ? <ChevronUp className="w-3.5 h-3.5 text-blue-400" /> : <ChevronDown className="w-3.5 h-3.5 text-blue-400" />}
                                            </button>

                                            {showAccesoriosSCOS && (
                                                <div className="grid grid-cols-1 gap-2 animate-in slide-in-from-top-2 duration-300">
                                                    {(currentSolicitud?.plantillas?.[0]?.accesorios || []).map((a, i) => (
                                                        <div key={i} className="flex items-center gap-4 p-4 bg-blue-50/20 rounded-2xl border border-blue-100/30">
                                                            <div className="flex-1">
                                                                <span className="text-[8px] font-black text-blue-400 uppercase tracking-widest block">Tipo / Accesorio</span>
                                                                <span className="text-xs font-bold text-gray-700 uppercase">{a.tipo}: {a.nombreAccesorio}</span>
                                                            </div>
                                                            <div className="w-24 text-right">
                                                                <span className="text-[8px] font-black text-blue-400 uppercase tracking-widest block">Cantidad</span>
                                                                <span className="text-sm font-black text-blue-600 italic">{a.cantidad || 0} un</span>
                                                            </div>
                                                        </div>
                                                    ))}
                                                    {(currentSolicitud?.plantillas?.[0]?.accesorios || []).length === 0 && (
                                                        <div className="text-center py-4 bg-gray-50 rounded-xl text-[10px] text-gray-400 font-bold uppercase italic border border-dashed border-gray-100">Sin accesorios en solicitud</div>
                                                    )}
                                                </div>
                                            )}
                                        </div>

                                        <CosteoTable
                                            title="Detalle de logotipos"
                                            insumos={insumos.filter(i => i.categoryId === 'logotipo')}
                                            onUpdateItem={handleUpdateItem}
                                            onRemoveItem={handleRemoveItem}
                                            onAddItem={() => handleAddItem('logotipo')}
                                        />
                                        <CosteoTable
                                            title="Detalle Insumos"
                                            insumos={insumos.filter(i => i.categoryId === 'insumos')}
                                            onUpdateItem={handleUpdateItem}
                                            onRemoveItem={handleRemoveItem}
                                            onAddItem={() => handleAddItem('insumos')}
                                        />
                                    </div>
                                )}

                                {activeTab === 'mo' && (
                                    <div className="space-y-8 p-4">
                                        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                                            {[
                                                { label: "M.O. Prenda", value: moPrenda, setter: setMoPrenda },
                                                { label: "M.O. Cinta", value: moCinta, setter: setMoCinta },
                                                { label: "M.O. Costura Sellada", value: moCosturaSellada, setter: setMoCosturaSellada },
                                                { label: "M.O. Acolchado", value: moAcolchado, setter: setMoAcolchado }
                                            ].map((field, idx) => (
                                                <div key={idx} className="space-y-2">
                                                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">{field.label}</label>
                                                    <div className="relative">
                                                        <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 font-bold">$</span>
                                                        <input
                                                            type="number"
                                                            min="0"
                                                            value={field.value}
                                                            onChange={(e) => field.setter(Math.max(0, parseFloat(e.target.value) || 0))}
                                                            className="w-full pl-8 pr-4 py-4 bg-gray-50 border-none rounded-2xl text-md font-black text-gray-700 focus:ring-2 focus:ring-blue-500 transition-all outline-none"
                                                        />
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                        <div className="mt-8 p-6 bg-blue-50 rounded-2xl border border-blue-100 flex justify-between items-center">
                                            <span className="text-sm font-black text-blue-800 uppercase italic">Total Mano de Obra Directa</span>
                                            <span className="text-2xl font-black text-blue-600 tracking-tight">${totalMO.toLocaleString('es-CL')}</span>
                                        </div>
                                    </div>
                                )}
                                {activeTab === "costosFijos" && (
                                    <div className="space-y-8 p-4">
                                        <div className="bg-gray-50 p-8 rounded-[2.5rem] border border-gray-100">
                                            <div className="flex items-center gap-4 mb-8">
                                                <div className="w-12 h-12 bg-orange-100 rounded-2xl flex items-center justify-center">
                                                    <TrendingDown className="w-6 h-6 text-orange-600" />
                                                </div>
                                                <div>
                                                    <h4 className="text-xl font-black text-gray-800 uppercase italic tracking-tighter">Desglose de Costos Fijos</h4>
                                                    <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mt-1">Gastos indirectos y operativos por unidad</p>
                                                </div>
                                            </div>

                                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                                                {[
                                                    { label: "Hilo", value: costoHilo, setter: setCostoHilo },
                                                    { label: "M.O. Prenda (Simple)", value: costoMoPropia, setter: setCostoMoPropia },
                                                    { label: "M.O. Gratificación", value: costoGratificacion, setter: setCostoGratificacion },
                                                    { label: "Etiqueta", value: costoEtiqueta, setter: setCostoEtiqueta },
                                                    { label: "Embalaje", value: costoEmbalaje, setter: setCostoEmbalaje },
                                                    { label: "Flete", value: costoFlete, setter: setCostoFlete }
                                                ].map((field, i) => (
                                                    <div key={i} className="space-y-2">
                                                        <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">{field.label}</label>
                                                        <div className="relative">
                                                            <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 font-bold">$</span>
                                                            <input
                                                                type="number"
                                                                min="0"
                                                                value={field.value}
                                                                onChange={(e) => field.setter(Math.max(0, parseFloat(e.target.value) || 0))}
                                                                className="w-full pl-8 pr-4 py-4 bg-white border border-gray-100 rounded-2xl text-md font-black text-gray-700 focus:ring-2 focus:ring-orange-500 transition-all outline-none"
                                                            />
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>

                                            <div className="mt-12 p-8 bg-gray-900 rounded-3xl flex justify-between items-center text-white shadow-xl">
                                                <div>
                                                    <span className="text-[10px] font-black uppercase text-orange-400 italic tracking-widest block mb-1">Costo Fijo Total Acumulado</span>
                                                    <span className="text-sm font-bold text-gray-400 uppercase italic">Basado en 6 indicadores operativos</span>
                                                </div>
                                                <div className="text-right">
                                                    <span className="text-3xl font-black text-white tracking-tight italic underline decoration-orange-500 decoration-4 underline-offset-8">${totalCostosFijos.toLocaleString('es-CL')}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>

                {/* Floating Technical Specification Sheet */}
                <div className="fixed right-8 top-36 w-[400px] z-40 animate-in slide-in-from-right-8 duration-1000">
                    <div className="bg-slate-900 p-8 rounded-[2.5rem] shadow-2xl border border-slate-700 text-white ring-1 ring-white/10 ring-inset flex flex-col h-[calc(100vh-180px)]">
                        <div className="flex items-center gap-4 mb-6 shrink-0">
                            <div className="w-10 h-10 bg-white/10 rounded-xl flex items-center justify-center">
                                <FileText className="w-5 h-5 text-orange-400" />
                            </div>
                            <div>
                                <h4 className="text-sm font-black uppercase tracking-widest leading-none">Especificación Técnica</h4>
                                <p className="text-[9px] font-bold uppercase mt-1 text-slate-400 italic">Consulta Proceso SCOS</p>
                            </div>
                        </div>

                        <div className="space-y-4 overflow-y-auto pr-2 custom-scrollbar flex-1">
                            {/* Tallaje General */}
                            {currentSolicitud?.tallaje && (
                                <div className="p-5 bg-gradient-to-br from-orange-500/10 to-transparent rounded-3xl border border-orange-500/20 mb-6 shrink-0">
                                    <span className="text-[9px] font-black text-orange-500 uppercase tracking-[0.2em] block mb-2">TALLAJE DEFINIDO</span>
                                    <div className="flex items-center gap-3">
                                        <div className="w-8 h-8 bg-orange-500 rounded-lg flex items-center justify-center text-white font-black text-xs">T</div>
                                        <span className="text-sm font-black text-slate-100 uppercase italic tracking-tight">{currentSolicitud.tallaje}</span>
                                    </div>
                                </div>
                            )}

                            {(currentSolicitud?.plantillas || []).map((p, idx) => (
                                <div key={idx} className="space-y-4 pt-4 border-t border-slate-800 shrink-0">
                                    <h5 className="text-[10px] font-black text-orange-400 uppercase tracking-[0.2em] mb-3">RECETA: {p.nombrePrenda || p.nombre}</h5>
                                    <div className="grid grid-cols-1 gap-3">
                                        {Object.entries(FIELD_LABELS).map(([key, label]) => {
                                            if (['nombre', 'nombrePrenda', 'obsModelo'].includes(key)) return null;
                                            const val = p[key];
                                            if (!val) return null;

                                            return (
                                                <div key={key} className="flex flex-col p-4 bg-slate-800/50 rounded-2xl border border-slate-700/50 group/item">
                                                    <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest block mb-1">{label}</span>
                                                    <span className="text-[11px] font-bold text-slate-100 uppercase leading-relaxed">{val}</span>

                                                    {/* Materiales Vinculados */}
                                                    {p.vinculos?.filter(v => v.fieldName === key).length > 0 && (
                                                        <div className="mt-3 flex flex-wrap gap-2 pt-3 border-t border-slate-700/30">
                                                            {p.vinculos.filter(v => v.fieldName === key).map((v, vIdx) => (
                                                                <div key={vIdx} className={`flex items-center gap-2 px-2.5 py-1.5 rounded-lg border text-[9px] font-bold uppercase tracking-tight ${v.materialType === 'TELA'
                                                                    ? 'bg-orange-500/10 border-orange-500/30 text-orange-400'
                                                                    : 'bg-blue-500/10 border-blue-500/30 text-blue-400'
                                                                    }`}>
                                                                    <div className={`w-1.5 h-1.5 rounded-full ${v.materialType === 'TELA' ? 'bg-orange-400' : 'bg-blue-400'}`} />
                                                                    {v.cantidad} {v.materialType === 'TELA' ? 'Mts' : 'Und'} • {
                                                                        v.materialType === 'TELA'
                                                                            ? (p.telas?.find(t => t.id === v.materialId)?.nombre || 'Tela')
                                                                            : (p.accesorios?.find(a => a.id === v.materialId)?.nombreAccesorio || 'Accesorio')
                                                                    }
                                                                </div>
                                                            ))}
                                                        </div>
                                                    )}
                                                </div>
                                            );
                                        })}
                                        {p.obsModelo && (
                                            <div className="flex flex-col p-5 bg-indigo-600 rounded-2xl shadow-lg border border-indigo-500 shrink-0">
                                                <span className="text-[9px] font-black text-indigo-200 uppercase tracking-widest block mb-2">OBSERVACIONES</span>
                                                <span className="text-[11px] font-medium leading-relaxed italic text-indigo-50">"{p.obsModelo}"</span>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ))}
                            {(!currentSolicitud?.plantillas || currentSolicitud.plantillas.length === 0) && (
                                <div className="text-center py-10 opacity-30 italic text-[10px] font-bold uppercase tracking-widest">No hay ficha adjunta</div>
                            )}
                        </div>

                        {/* Validation Small Card at bottom of sheet */}
                        <div className="mt-6 pt-6 border-t border-slate-800 shrink-0">
                            <div className="flex items-center gap-3">
                                <ShieldCheck className="w-5 h-5 text-blue-400 opacity-50" />
                                <p className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Costos para Evaluación Comercial</p>
                            </div>
                        </div>
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
}
