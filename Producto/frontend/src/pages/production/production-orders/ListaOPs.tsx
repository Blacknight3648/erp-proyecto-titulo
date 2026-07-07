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
    getClientName,
    seguimientoDetails,
    searchTerm, setSearchTerm,
    clientFilter, setClientFilter
}) {
    // Get unique clients for the dropdown
    const uniqueClients = [...new Set(ordenes.map(op => getClientName(op)))].filter(c => c && c !== '-');

    // Filter the ordenes array
    const filteredOrdenes = ordenes.filter(op => {
        const matchesSearch = !searchTerm ||
            (op.numeroOP || `OP-${op.id}`).toLowerCase().includes(searchTerm.toLowerCase()) ||
            getClientName(op).toLowerCase().includes(searchTerm.toLowerCase());

        const matchesClient = !clientFilter || getClientName(op) === clientFilter;

        return matchesSearch && matchesClient;
    });

    return (
        <div className="max-w-2xl mx-auto bg-muted/50 min-h-[calc(100vh-120px)] p-4 pb-24 relative animate-in fade-in duration-700">
            <h1 className="text-3xl font-black text-primary mb-8 text-center tracking-tight uppercase italic">Registro de Producción</h1>

            <div className="bg-card p-6 rounded-3xl shadow-sm border border-border mb-8 space-y-4">
                <div className="p-4 bg-muted/50 rounded-2xl border border-border text-center">
                    <p className="text-muted-foreground font-medium text-sm">Selecciona una OP reciente para actualizar sus estados:</p>
                </div>

                <div className="space-y-3">
                    <select
                        className="w-full p-4 bg-card border border-border rounded-2xl text-[10px] font-black uppercase tracking-widest focus:ring-2 focus:ring-primary outline-none appearance-none cursor-pointer"
                        value={clientFilter}
                        onChange={(e) => setClientFilter(e.target.value)}
                    >
                        <option value="">Filtrar por Cliente...</option>
                        {uniqueClients.map((client, idx) => (
                            <option key={idx} value={client}>{client}</option>
                        ))}
                    </select>

                    <div className="relative">
                        <input
                            type="text"
                            placeholder="Buscar OP o Cliente..."
                            className="w-full p-4 bg-card border border-border rounded-2xl text-sm font-medium focus:ring-2 focus:ring-primary outline-none placeholder:text-muted-foreground shadow-inner"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <button
                        onClick={() => setIsSelectionMode(!isSelectionMode)}
                        className={`w-full py-4 font-black text-xs uppercase tracking-widest rounded-2xl border transition-all shadow-sm ${isSelectionMode ? 'bg-destructive/10 text-destructive border-destructive/20' : 'bg-primary/10 text-primary border-primary/20 hover:bg-primary hover:text-primary-foreground'}`}
                    >
                        {isSelectionMode ? 'Cancelar Selección' : 'Modificación múltiple'}
                    </button>
                </div>
            </div>

            <div className="flex justify-between items-center mb-4 ml-2">
                <h3 className="text-[10px] font-black text-muted-foreground uppercase tracking-[0.2em]">OPs en tu Estación</h3>
                {isSelectionMode && selectedOPIds.length > 0 && (
                    <button
                        onClick={handleBulkEdit}
                        className="bg-primary text-primary-foreground px-6 py-2 rounded-full text-[10px] font-black uppercase tracking-widest shadow-lg shadow-primary/20 animate-in zoom-in"
                    >
                        Continuar ({selectedOPIds.length})
                    </button>
                )}
            </div>

            <div className="space-y-2 max-h-[500px] overflow-y-auto pr-2 custom-scrollbar">
                {filteredOrdenes.map((op) => {
                    const isSelected = selectedOP?.id === op.id;
                    const isMultiSelected = selectedOPIds.includes(op.id);

                    return (
                        <div
                            key={op.id}
                            onClick={() => isSelectionMode ? toggleSelection(op.id) : setSelectedOP(op)}
                            className={`bg-card p-6 rounded-[2rem] shadow-sm border-2 transition-all cursor-pointer group ${isSelectionMode && isMultiSelected ? 'border-primary ring-4 ring-primary/10 shadow-lg' : isSelected ? 'border-primary ring-4 ring-primary/10 shadow-lg' : 'border-border hover:border-border-strong hover:shadow-md'}`}
                        >
                            <div className="flex justify-between items-start mb-1">
                                <div className="flex items-center space-x-4">
                                    {isSelectionMode && (
                                        <div className={`w-6 h-6 rounded-lg border-2 flex items-center justify-center transition-all ${isMultiSelected ? 'bg-primary border-primary' : 'border-border bg-muted'}`}>
                                            {isMultiSelected && <CheckCircle2 className="w-4 h-4 text-primary-foreground" />}
                                        </div>
                                    )}
                                    <div>
                                        <h4 className="font-black text-primary text-lg uppercase tracking-tight">{op.numeroOP || `OP-${op.id}`}</h4>
                                        <p className="text-[11px] font-bold text-muted-foreground uppercase tracking-tight">{getClientName(op)}</p>
                                    </div>
                                </div>
                                <div className="flex flex-col items-end">
                                    <span className="text-[10px] font-black text-destructive bg-destructive/10 px-2 py-0.5 rounded-full">{op.progreso}%</span>
                                </div>
                            </div>

                            <div className="mt-3">
                                <div className="w-full bg-muted h-1.5 rounded-full overflow-hidden shadow-inner">
                                    <div
                                        className="bg-primary h-full rounded-full transition-all duration-1000"
                                        style={{ width: `${op.progreso}%` }}
                                    ></div>
                                </div>
                            </div>

                            <div className="mt-4 flex justify-between items-center text-[10px] font-bold text-muted-foreground">
                                <span>{op.fechaInicio || '-'}</span>
                                <span className="uppercase">{op.estado || '-'}</span>
                            </div>

                            {!isSelectionMode && isSelected && (
                                <div className="mt-6 pt-6 border-t border-dashed border-border grid grid-cols-2 gap-4 animate-in fade-in slide-in-from-top-4">
                                    <button
                                        onClick={(e) => { e.stopPropagation(); handleModificarRegistro(); }}
                                        className="flex items-center justify-center p-4 bg-primary text-primary-foreground rounded-2xl text-[10px] font-black uppercase tracking-widest shadow-xl shadow-primary/20"
                                    >
                                        <CheckCircle2 className="w-3.5 h-3.5 mr-2" /> Modificar Registro
                                    </button>
                                    <button
                                        onClick={(e) => { e.stopPropagation(); handleVerDetalles(); }}
                                        className="flex items-center justify-center p-4 bg-card border-2 border-border text-foreground rounded-2xl text-[10px] font-black uppercase tracking-widest hover:bg-muted transition-all"
                                    >
                                        <ClipboardList className="w-3.5 h-3.5 mr-2" /> Detalles de OP
                                    </button>
                                </div>
                            )}
                        </div>
                    );
                })}
                {filteredOrdenes.length === 0 && (
                    <p className="text-center py-10 text-muted-foreground font-bold italic uppercase tracking-widest text-xs">
                        No hay Ordenes de Producción registradas que coincidan con la búsqueda
                    </p>
                )}
            </div>

            <div className="fixed bottom-0 left-0 right-0 bg-card border-t border-border p-4 flex justify-around items-center lg:hidden z-10 shadow-[0_-4px_20px_rgba(0,0,0,0.05)]">
                <div className="flex flex-col items-center text-primary bg-primary/10 px-4 py-2 rounded-2xl transition-all">
                    <Factory className="w-6 h-6" />
                    <span className="text-[10px] mt-1 font-black uppercase tracking-tighter">Planta</span>
                </div>
                <div className="flex flex-col items-center text-muted-foreground p-2 opacity-50">
                    <ClipboardList className="w-6 h-6" />
                    <span className="text-[10px] mt-1 font-bold">Órdenes</span>
                </div>
                <div className="flex flex-col items-center text-muted-foreground p-2 opacity-50">
                    <CheckCircle2 className="w-6 h-6" />
                    <span className="text-[10px] mt-1 font-bold">Historial</span>
                </div>
            </div>
        </div>
    );
}
