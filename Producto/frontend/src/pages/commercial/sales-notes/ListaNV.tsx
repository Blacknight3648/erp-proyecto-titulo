import React, { useState } from 'react';
import { ClipboardList, Plus, Search, Calendar, ChevronRight, ArrowRight, FileText, X } from 'lucide-react';
import NuevaNVModal from './NuevaNVModal';

export default function ListaNV({
    registros,
    evaluaciones,
    loadingComercial,
    activeTab,
    setActiveTab,
    searchTerm,
    setSearchTerm,
    handleOpenForm,
    evnModal,
    setEvnModal
}) {
    const [nuevaModalOpen, setNuevaModalOpen] = useState(false);

    // Solo las EVN ADJUDICADA pueden usarse como plantilla para una NV.
    const evnPlantillas = (evaluaciones || []).filter(e => e.estado === 'ADJUDICADA');

    return (
        <div className="max-w-7xl mx-auto p-6 space-y-8 animate-in fade-in duration-700">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                    <h1 className="text-4xl font-black text-foreground tracking-tight italic flex items-center uppercase">
                        <ClipboardList className="w-10 h-10 mr-4 text-primary" />
                        Gestión Notas de Venta
                    </h1>
                </div>
                <button
                    onClick={() => setNuevaModalOpen(true)}
                    className="flex items-center px-6 py-3 bg-primary text-primary-foreground rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl hover:bg-primary-hover transition-all"
                >
                    <Plus className="w-4 h-4 mr-2" /> Nueva Nota de Venta
                </button>
            </div>

            <div className="flex flex-col md:flex-row gap-6 items-center justify-between bg-card p-4 rounded-[2rem] shadow-sm border border-border">
                <div className="flex p-1 bg-muted rounded-2xl shrink-0">
                    {['registros', 'plantillas'].map(tab => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={`px-6 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${activeTab === tab ? 'bg-card text-primary shadow-sm' : 'text-muted-foreground'}`}
                        >
                            {tab === 'registros' ? 'Registros Activos' : 'Plantillas EVN'}
                        </button>
                    ))}
                </div>
                <div className="relative flex-1 max-w-md w-full">
                    <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar..."
                        className="w-full pl-11 pr-4 py-3 bg-muted border-none rounded-2xl text-sm font-bold focus:ring-2 focus-within:ring-primary transition-all outline-none"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            {loadingComercial ? (
                <div className="flex justify-center py-20">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
                </div>
            ) : activeTab === 'registros' ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {registros
                        .filter(r => String(r.numeroNV || r.folio || r.id).includes(searchTerm) || (r.clienteNombre || r.cliente)?.toLowerCase().includes(searchTerm.toLowerCase()))
                        .map((record) => (
                        <div
                            key={record.idNV || record.id}
                            onClick={() => handleOpenForm(record, record.estado === 'BORRADOR' ? 'edit' : 'view')}
                            className="group bg-card p-6 rounded-[2.5rem] border-2 border-border hover:border-primary hover:shadow-2xl transition-all cursor-pointer relative overflow-hidden"
                        >
                            <div className="flex justify-between items-start mb-4">
                                <div>
                                    <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">{record.numeroNV || `NV-${record.idNV || record.id}`}</span>
                                    <h3 className="text-md font-black text-foreground group-hover:text-primary uppercase mt-1">{record.clienteNombre}</h3>
                                </div>
                                {record.estado === 'BORRADOR'
                                    ? <span className="px-3 py-1 bg-warning/10 text-warning rounded-full text-[9px] font-black uppercase">Borrador</span>
                                    : <span className="px-3 py-1 bg-success/10 text-success rounded-full text-[9px] font-black uppercase">Emitida</span>
                                }
                            </div>
                            <div className="space-y-3 mb-6">
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-3 h-3 text-muted-foreground" />
                                    <span className="text-[10px] font-bold text-muted-foreground">{record.fechaEmision || record.fecha}</span>
                                </div>
                                <div className="p-3 bg-muted rounded-xl">
                                    <p className="text-xs font-bold text-foreground">{record.items?.length || 0} Ítems registrados</p>
                                </div>
                            </div>
                            <div className="flex items-center justify-between text-[10px] font-black uppercase text-primary opacity-0 group-hover:opacity-100 transition-all">
                                <span>{record.estado === 'BORRADOR' ? 'Editar Borrador' : 'Ver Detalle'}</span>
                                <ChevronRight className="w-4 h-4" />
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {evaluaciones
                        // Solo una EVN ADJUDICADA puede usarse como plantilla para una NV
                        // (una EVN CERRADA u otro estado es rechazada por el backend).
                        .filter(e => e.estado === 'ADJUDICADA')
                        .filter(e => String(e.evaluacionNegocioId || e.id).includes(searchTerm) || (e.clienteNombre || e.cliente)?.toLowerCase().includes(searchTerm.toLowerCase()))
                        .map((evn) => (
                        <div key={evn.evaluacionNegocioId || evn.id} className="bg-card p-6 rounded-[2.5rem] border border-border hover:shadow-xl transition-all flex flex-col">
                            <div className="flex justify-between items-start mb-4">
                                <div>
                                    <span className="text-[10px] font-black text-primary uppercase tracking-widest">EVN-{evn.evaluacionNegocioId || evn.id}</span>
                                    <h3 className="text-md font-black text-foreground uppercase mt-1">{evn.clienteNombre || evn.cliente || 'Sin cliente'}</h3>
                                    <p className="text-[10px] text-muted-foreground font-bold">{evn.referencia || evn.numeroEvn || 'Sin referencia'}</p>
                                </div>
                                <span className={`px-3 py-1 rounded-full text-[9px] font-black uppercase ${evn.estado === 'ADJUDICADA' ? 'bg-success/10 text-success' : 'bg-warning/10 text-warning'
                                    }`}>{evn.estado || 'Borrador'}</span>
                            </div>
                            <div className="p-3 bg-primary/5 rounded-xl mb-4 flex-1">
                                <p className="text-xs font-bold text-muted-foreground">{evn.items?.length || 0} ítems disponibles</p>
                            </div>
                            <button
                                onClick={() => setEvnModal({ open: true, evn, selectedIds: new Set() })}
                                className="w-full flex items-center justify-center gap-2 px-4 py-2.5 bg-primary hover:bg-primary-hover text-primary-foreground rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                            >
                                <ArrowRight className="w-3.5 h-3.5" />
                                Usar como Plantilla
                            </button>
                        </div>
                    ))}
                    {evaluaciones.length === 0 && (
                        <div className="col-span-full py-20 flex flex-col items-center justify-center bg-card rounded-[2.5rem] border border-dashed border-border">
                            <FileText className="w-12 h-12 text-muted-foreground/30 mb-4" />
                            <p className="text-sm font-black text-muted-foreground uppercase tracking-widest">No hay evaluaciones disponibles</p>
                        </div>
                    )}
                </div>
            )}

            {/* Modal Selección de Ítems EVN */}
            {evnModal.open && evnModal.evn && (
                <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
                    <div className="absolute inset-0 bg-foreground/40 backdrop-blur-sm" onClick={() => setEvnModal({ open: false, evn: null, selectedIds: new Set() })} />
                    <div className="relative bg-card rounded-[2.5rem] shadow-2xl w-full max-w-2xl max-h-[80vh] flex flex-col overflow-hidden">
                        <div className="flex items-center justify-between p-6 border-b border-border">
                            <div>
                                <span className="text-[10px] font-black text-primary uppercase tracking-widest">EVN-{evnModal.evn.evaluacionNegocioId || evnModal.evn.id}</span>
                                <h2 className="text-lg font-black text-foreground uppercase">{evnModal.evn.clienteNombre || evnModal.evn.cliente}</h2>
                                <p className="text-xs text-muted-foreground font-bold">{evnModal.evn.referencia || 'Sin referencia'}</p>
                            </div>
                            <button onClick={() => setEvnModal({ open: false, evn: null, selectedIds: new Set() })} className="w-10 h-10 bg-muted rounded-xl flex items-center justify-center hover:bg-muted/70 transition-all">
                                <X className="w-4 h-4 text-muted-foreground" />
                            </button>
                        </div>

                        <div className="px-6 py-3 bg-muted flex items-center gap-3 border-b border-border">
                            <input
                                type="checkbox"
                                className="rounded accent-primary"
                                checked={evnModal.selectedIds.size === (evnModal.evn.items || []).length && (evnModal.evn.items || []).length > 0}
                                onChange={() => {
                                    const allIds = new Set((evnModal.evn.items || []).map((_, i) => i));
                                    const allSelected = evnModal.selectedIds.size === (evnModal.evn.items || []).length;
                                    setEvnModal(prev => ({ ...prev, selectedIds: allSelected ? new Set() : allIds }));
                                }}
                            />
                            <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">
                                Seleccionar todos ({evnModal.selectedIds.size}/{(evnModal.evn.items || []).length})
                            </span>
                        </div>

                        <div className="overflow-y-auto flex-1">
                            {(evnModal.evn.items || []).length === 0 ? (
                                <div className="py-16 text-center">
                                    <p className="text-sm font-bold text-muted-foreground">Esta EVN no tiene ítems registrados</p>
                                </div>
                            ) : (
                                (evnModal.evn.items || []).map((item, idx) => (
                                    <label key={item.id || idx} className={`flex items-center gap-4 px-6 py-4 border-b border-border cursor-pointer hover:bg-primary/5 transition-colors ${evnModal.selectedIds.has(idx) ? 'bg-primary/10' : ''}`}>
                                        <input
                                            type="checkbox"
                                            className="rounded accent-primary flex-shrink-0"
                                            checked={evnModal.selectedIds.has(idx)}
                                            onChange={() => {
                                                setEvnModal(prev => {
                                                    const next = new Set(prev.selectedIds);
                                                    if (next.has(idx)) next.delete(idx);
                                                    else next.add(idx);
                                                    return { ...prev, selectedIds: next };
                                                });
                                            }}
                                        />
                                        <div className="flex-1 min-w-0">
                                            <p className="text-sm font-black text-foreground truncate">{item.descripcion || item.nombreProducto || `Ítem ${idx + 1}`}</p>
                                            <div className="flex gap-4 mt-0.5">
                                                <span className="text-[10px] font-bold text-muted-foreground">{item.cant || item.cantidad || 0} uds</span>
                                                {item.modelo && <span className="text-[10px] font-bold text-muted-foreground">Modelo: {item.modelo}</span>}
                                                {item.tela && <span className="text-[10px] font-bold text-muted-foreground">Tela: {item.tela}</span>}
                                                <span className="text-[10px] font-black text-primary uppercase">{item.tipo || item.tipoItem || 'SC'}</span>
                                            </div>
                                        </div>
                                        <span className="text-sm font-black text-foreground flex-shrink-0">${(item.precioVentaNeto || item.precioUnitario || 0).toLocaleString('es-CL')}</span>
                                    </label>
                                ))
                            )}
                        </div>

                        <div className="flex items-center justify-between p-6 border-t border-border bg-muted/50">
                            <span className="text-xs font-black text-muted-foreground uppercase">{evnModal.selectedIds.size} ítems seleccionados</span>
                            <div className="flex gap-3">
                                <button
                                    onClick={() => setEvnModal({ open: false, evn: null, selectedIds: new Set() })}
                                    className="px-5 py-2.5 bg-muted hover:bg-muted/70 text-foreground rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                >
                                    Cancelar
                                </button>
                                <button
                                    onClick={() => {
                                        const selectedItems = (evnModal.evn.items || []).filter((_, idx) => evnModal.selectedIds.has(idx));
                                        handleOpenForm({ ...evnModal.evn, items: selectedItems }, 'template');
                                        setEvnModal({ open: false, evn: null, selectedIds: new Set() });
                                    }}
                                    className="px-5 py-2.5 bg-primary hover:bg-primary-hover text-primary-foreground rounded-xl text-[10px] font-black uppercase tracking-widest transition-all"
                                >
                                    {evnModal.selectedIds.size === 0
                                        ? 'Continuar sin ítems →'
                                        : `Cargar ${evnModal.selectedIds.size} Ítems →`}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* Modal inicial: toda NV se origina desde una EVN adjudicada */}
            <NuevaNVModal
                open={nuevaModalOpen}
                onClose={() => setNuevaModalOpen(false)}
                evaluaciones={evnPlantillas}
                onSelectEVN={(evn) => {
                    setNuevaModalOpen(false);
                    setEvnModal({ open: true, evn, selectedIds: new Set() });
                }}
            />
        </div>
    );
}
