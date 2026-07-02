import {
    Building2,
    Trash2,
    Plus,
    Calculator,
    Edit3,
    Users,
    Eye,
    ArrowRight
} from 'lucide-react';



import { useState } from 'react';
import { toast } from 'sonner';

import EVNActionBar from "./EVNActionBar";
import EVNVinculacionesPanel from "./EVNVinculacionesPanel";
import EVNResumenSidebar from "./EVNResumenSidebar";
import QuotationSelectionModal from "./QuotationSelectionModal";
import CosteoSelectionModal from "./CosteoSelectionModal";
import FirmaAprobacionModal from "./FirmaAprobacionModal";
import { useEVNState, parseId, DEFAULT_ITEM } from '../../../hooks/useEVNState';
import { EvaluacionNegocioService } from '../../../remote/service/EvaluacionNegocioService';
import { useAuth } from '../../../contexts/AuthContext';

export default function DetalleEVN({ initialEval, onBack, mode = 'create' }) {
    const isReadOnly = mode === 'view';
    const { user } = useAuth();
    const [cerrarModal, setCerrarModal] = useState({ open: false });
    const [cerrando, setCerrando] = useState(false);
    const {
        items, setItems,
        otrosCostos, setOtrosCostos,
        solicitud, setSolicitud,
        evalData, setEvalData,
        vinculados, setVinculados,
        selectedSCOSIds,
        selectedSCOTIds,
        isSaving,
        showQuotationModal, setShowQuotationModal,
        availableQuotations,
        pendingSCOS,
        totals,

        costeosDisponibles,
        showCosteoModal,
        costeoModalItemId,
        loadingCosteos,
        openCosteoSelector,
        closeCosteoSelector,
        handleSelectCosteo,

        handleUpdateItem,
        handleBulkLink,
        applySCOSQuotation,
        toggleDocSelection,
        handleGenerarPropuesta,

        proveedores,
        clientes,
        vendedores,
        solicitudesCostos
    } = useEVNState(initialEval);

    const onGenerarPropuestaInterno = async () => {
        const success = await handleGenerarPropuesta();
        if (success) onBack();
    };

    const handleConfirmCerrar = async ({ aprobador, observacion }) => {
        const id = initialEval?.evaluacionNegocioId || initialEval?.id;
        if (!id) return;
        setCerrando(true);
        try {
            await EvaluacionNegocioService.cerrar(id, aprobador, observacion);
            toast.success('EVN cerrada correctamente');
            setCerrarModal({ open: false });
            onBack();
        } catch (error) {
            toast.error('Error al cerrar: ' + (error.response?.data?.message || error.message));
        } finally {
            setCerrando(false);
        }
    };

    return (
        <div className="min-h-screen bg-muted pb-20 animate-in fade-in duration-500">
            <EVNActionBar
                initialEval={initialEval}
                totals={totals}
                items={items}
                otrosCostos={otrosCostos}
                solicitud={solicitud}
                evalData={evalData}
                mode={mode}
                isSaving={isSaving}
                onBack={onBack}
                onGenerarPropuesta={onGenerarPropuestaInterno}
                onCerrarEVN={() => setCerrarModal({ open: true })}
            />

            <div className="max-w-[1700px] mx-auto px-4 md:px-8 space-y-6">
                {/* Banner solo lectura */}
                {isReadOnly && (
                    <div className="flex items-center gap-3 px-5 py-3 bg-muted border border-border rounded-2xl text-muted-foreground text-[11px] font-black uppercase tracking-widest">
                        <Eye className="w-4 h-4 text-muted-foreground shrink-0" />
                        Modo visualización — esta evaluación no puede ser modificada desde esta vista
                    </div>
                )}

                <div className="bg-card rounded-2xl shadow-sm border border-border p-4 flex flex-wrap items-center justify-between gap-4 animate-in slide-in-from-top-4 duration-500">
                    <div className="flex items-center gap-6 flex-1">
                        <div className="flex items-center bg-muted px-4 py-2 rounded-xl border border-border shadow-sm focus-within:ring-2 focus-within:ring-brand-indigo/20 transition-all flex-1 max-w-[300px]">
                            <Users className="w-4 h-4 text-brand-indigo mr-3" />
                            <div className="flex-1">
                                <p className="text-[8px] font-black text-muted-foreground uppercase tracking-widest leading-none mb-1">Ejecutivo Comercial</p>
                                <select
                                    className="w-full bg-transparent border-none text-xs font-black text-brand-indigo uppercase italic p-0 focus:ring-0 outline-none cursor-pointer disabled:cursor-default disabled:opacity-70"
                                    value={parseId(solicitud.vendedorId || initialEval?.vendedorId) || ''}
                                    onChange={(e) => setSolicitud(prev => ({ ...prev, vendedorId: parseInt(e.target.value) }))}
                                    disabled={isReadOnly}
                                >
                                    <option value="">Seleccionar...</option>
                                    {vendedores.map(v => (
                                        <option key={v.id} value={v.id}>
                                            {v.nombreUsuario ? `${v.nombreUsuario} ${v.apellidosUsuario || ''}`.trim() : v.codigoVendedor}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        <div className="flex items-center bg-muted px-4 py-2 rounded-xl border border-border shadow-sm focus-within:ring-2 focus-within:ring-brand-indigo/20 transition-all flex-1 max-w-[400px]">
                            <Building2 className="w-4 h-4 text-muted-foreground mr-3" />
                            <div className="flex-1">
                                <p className="text-[8px] font-black text-muted-foreground uppercase tracking-widest leading-none mb-1">Cliente Solicitante</p>
                                <select
                                    className="w-full bg-transparent border-none text-xs font-black text-foreground uppercase p-0 focus:ring-0 outline-none cursor-pointer disabled:cursor-default disabled:opacity-70"
                                    value={parseId(solicitud.clienteId || initialEval?.clienteId) || ''}
                                    disabled={isReadOnly}
                                    onChange={(e) => {
                                        const id = parseInt(e.target.value);
                                        const c = clientes.find(cli => (cli.clienteId || cli.id) === id);
                                        setSolicitud(prev => ({
                                            ...prev,
                                            clienteId: id,
                                            clienteNombre: c ? (c.razonSocial || `${c.nombreCliente} ${c.apellidoCliente || ''}`.trim()) : ''
                                        }));
                                    }}
                                >
                                    <option value="">Seleccionar Cliente...</option>
                                    {clientes.map(c => (
                                        <option key={c.clienteId || c.id} value={c.clienteId || c.id}>{c.razonSocial || `${c.nombreCliente} ${c.apellidoCliente || ''}`.trim()}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </div>

                    <div className="flex items-center gap-8 pl-6 border-l border-border">
                        <div className="min-w-[250px]">
                            <p className="text-[8px] font-black text-muted-foreground uppercase tracking-widest leading-none mb-1">Referencia de Negocio</p>
                            <div className="flex items-center">
                                <Edit3 className="w-3.5 h-3.5 text-muted-foreground/50 mr-2" />
                                <input
                                    type="text"
                                    placeholder="Ingrese referencia..."
                                    className="bg-transparent border-none text-xs font-black text-foreground uppercase p-0 focus:ring-0 w-full placeholder:text-muted-foreground/30 disabled:opacity-70 disabled:cursor-default"
                                    value={evalData.referencia}
                                    onChange={(e) => setEvalData({ ...evalData, referencia: e.target.value.toUpperCase() })}
                                    disabled={isReadOnly}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-card rounded-[2rem] shadow-sm border border-border overflow-hidden">
                    <div className="p-6 border-b border-border flex items-center bg-muted/30">
                        <div className="flex items-center space-x-3">
                            <div className="w-2 h-6 bg-brand-indigo rounded-full" />
                            <h3 className="text-sm font-black text-foreground uppercase tracking-widest">
                                Matriz de Precios y Costos Unitarios
                            </h3>
                        </div>
                    </div>

                    <EVNVinculacionesPanel
                        isReadOnly={isReadOnly}
                        solicitudesCostos={solicitudesCostos}
                        vinculados={vinculados}
                        setVinculados={setVinculados}
                        selectedSCOSIds={selectedSCOSIds}
                        selectedSCOTIds={selectedSCOTIds}
                        toggleDocSelection={toggleDocSelection}
                        onBulkLink={handleBulkLink}
                    />


                    <div className="overflow-x-auto min-h-[400px]">
                        <table className="w-full text-left border-collapse min-w-[2000px]" aria-label="Matriz de precios y costos">
                            <thead>
                                <tr className="bg-sidebar text-sidebar-foreground border-b border-sidebar-border">
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">#</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground">Cant</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">P. Venta 20% MG</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-success bg-black/20">P. Venta Neto</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground">P. Venta Total</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">Tipo</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground">Código Interno</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">Proveedor</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground">Producto</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">Código Prov.</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground">Modelo</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">Género</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground">Tela</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted">Composición</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground bg-black/10">Costo Prod</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground bg-black/10">Costo Logo</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground bg-black/10">Costo OT</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-primary bg-black/20">Costos Grales %</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-foreground bg-black/30">Costo Unit.</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-white bg-black/45">Costo Total</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-warning bg-black/20">MG s/ Costo</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-warning bg-black/20">MG s/ Venta</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-warning bg-black/20">MG Venta $</th>
                                    <th className="px-4 py-4 text-[10px] font-black uppercase tracking-wider text-sidebar-muted text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {totals.itemsConCostos.map((item, idx) => (
                                    <tr key={item.id} className="hover:bg-muted/80 border-b border-border group transition-all duration-150">
                                        <td className="px-4 py-4 text-xs font-bold text-muted-foreground italic">{idx + 1}</td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="number"
                                                className="w-16 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-xs font-semibold text-foreground outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.cant}
                                                onChange={(e) => handleUpdateItem(item.id, 'cant', e.target.value)}
                                                disabled={isReadOnly}
                                            />
                                        </td>
                                        <td className="px-4 py-4 text-[11px] font-bold text-muted-foreground italic">
                                            ${(item.precioVenta20MG || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-4 py-4 bg-success/5">
                                            <div className="flex items-center">
                                                <span className="text-success font-extrabold mr-1 text-[11px]">$</span>
                                                <input
                                                    type="number"
                                                    className="w-24 px-2.5 py-2 bg-card border border-success/30 hover:border-success/50 focus:border-success focus:ring-2 focus:ring-success/10 rounded-xl text-xs font-bold text-success outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                    value={item.precioVentaNeto}
                                                    onChange={(e) => handleUpdateItem(item.id, 'precioVentaNeto', e.target.value)}
                                                    disabled={isReadOnly}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-4 py-4 text-xs font-extrabold text-foreground tracking-tight">
                                            ${(item.precioVentaTotal || 0).toLocaleString('es-CL')}
                                        </td>
                                        <td className="px-4 py-4">
                                            <select
                                                className="px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground uppercase outline-none transition-all cursor-pointer disabled:cursor-default disabled:opacity-60"
                                                value={item.tipo}
                                                onChange={(e) => handleUpdateItem(item.id, 'tipo', e.target.value)}
                                                disabled={isReadOnly}
                                            >
                                                <option value="SC">SC</option>
                                                <option value="SCI">SCI</option>
                                                <option value="OP">OP</option>
                                            </select>
                                        </td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="text"
                                                className="w-28 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground uppercase outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.codigoInterno}
                                                onChange={(e) => handleUpdateItem(item.id, 'codigoInterno', e.target.value)}
                                                placeholder="Código"
                                                disabled={isReadOnly}
                                            />
                                            {item.tipo === 'OP' && !isReadOnly && (
                                                <div className="mt-1.5 animate-in fade-in duration-200">
                                                    <button
                                                        type="button"
                                                        onClick={() => openCosteoSelector(item.id)}
                                                        className={`w-full px-2 py-1.5 rounded-lg text-[9px] font-black uppercase tracking-wider transition-all flex items-center justify-center gap-1 shadow-sm ${item.costeoId
                                                            ? 'bg-warning/10 text-warning border border-warning/20 hover:bg-warning/20'
                                                            : 'bg-brand-indigo text-white hover:bg-brand-indigo/90'}`}
                                                    >
                                                        <Calculator className="w-3 h-3" />
                                                        {item.costeoId ? `Costeo #${item.costeoId} ✓` : 'Vincular costeo'}
                                                    </button>
                                                </div>
                                            )}
                                            {item.tipo === 'OP' && isReadOnly && item.costeoId && (
                                                <div className="mt-1.5">
                                                    <span className="inline-flex items-center gap-1 px-2 py-1 bg-warning/10 border border-warning/20 text-warning rounded-lg text-[9px] font-black uppercase">
                                                        <Calculator className="w-3 h-3" />
                                                        Costeo #{item.costeoId}
                                                    </span>
                                                </div>
                                            )}
                                        </td>
                                        <td className="px-4 py-4">
                                            <select
                                                className="px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground uppercase outline-none transition-all cursor-pointer disabled:cursor-default disabled:opacity-60"
                                                value={item.proveedorId || ''}
                                                onChange={(e) => {
                                                    const pId = e.target.value;
                                                    const p = proveedores.find(prov => String(prov.proveedorId || prov.id) === String(pId));
                                                    handleUpdateItem(item.id, 'proveedorId', pId);
                                                    handleUpdateItem(item.id, 'proveedor', p ? (p.nombreProveedor || p.nombre) : '');
                                                }}
                                                disabled={isReadOnly}
                                            >
                                                <option value="">Prov.</option>
                                                {proveedores.map(p => (
                                                    <option key={p.proveedorId || p.id} value={p.proveedorId || p.id}>
                                                        {p.nombreProveedor || p.nombre}
                                                    </option>
                                                ))}
                                            </select>
                                        </td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="text"
                                                className="w-36 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-medium text-foreground uppercase outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.producto}
                                                onChange={(e) => handleUpdateItem(item.id, 'producto', e.target.value)}
                                                placeholder="Producto"
                                                disabled={isReadOnly}
                                            />
                                        </td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="text"
                                                className="w-28 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-medium text-foreground uppercase outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.codigoProveedor}
                                                onChange={(e) => handleUpdateItem(item.id, 'codigoProveedor', e.target.value)}
                                                placeholder="Cod. Prov"
                                                disabled={isReadOnly}
                                            />
                                        </td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="text"
                                                className="w-32 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-medium text-foreground uppercase outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.modelo}
                                                onChange={(e) => handleUpdateItem(item.id, 'modelo', e.target.value)}
                                                placeholder="Modelo"
                                                disabled={isReadOnly}
                                            />
                                        </td>
                                        <td className="px-4 py-4">
                                            <select
                                                className="px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground uppercase outline-none transition-all cursor-pointer disabled:cursor-default disabled:opacity-60"
                                                value={item.genero}
                                                onChange={(e) => handleUpdateItem(item.id, 'genero', e.target.value)}
                                                disabled={isReadOnly}
                                            >
                                                <option value="">Gén.</option>
                                                <option value="Masculino">Masc</option>
                                                <option value="Femenino">Fem</option>
                                                <option value="Unisex">Unis</option>
                                            </select>
                                        </td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="text"
                                                className="w-28 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-medium text-foreground uppercase outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.tela}
                                                onChange={(e) => handleUpdateItem(item.id, 'tela', e.target.value)}
                                                placeholder="Tela"
                                                disabled={isReadOnly}
                                            />
                                        </td>
                                        <td className="px-4 py-4">
                                            <input
                                                type="text"
                                                className="w-36 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-medium text-foreground uppercase outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={item.composicion}
                                                onChange={(e) => handleUpdateItem(item.id, 'composicion', e.target.value)}
                                                placeholder="Comp."
                                                disabled={isReadOnly}
                                            />
                                        </td>
                                        <td className="px-4 py-4 bg-muted/50">
                                            <div className="flex items-center">
                                                <span className="text-muted-foreground text-[11px] font-bold mr-1">$</span>
                                                <input
                                                    type="number"
                                                    className="w-24 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                    value={item.costoProducto}
                                                    onChange={(e) => handleUpdateItem(item.id, 'costoProducto', e.target.value)}
                                                    disabled={isReadOnly}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-4 py-4 bg-muted/50">
                                            <div className="flex items-center">
                                                <span className="text-muted-foreground text-[11px] font-bold mr-1">$</span>
                                                <input
                                                    type="number"
                                                    className="w-24 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                    value={item.costoLogo}
                                                    onChange={(e) => handleUpdateItem(item.id, 'costoLogo', e.target.value)}
                                                    disabled={isReadOnly}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-4 py-4 bg-muted/50">
                                            <div className="flex items-center">
                                                <span className="text-muted-foreground text-[11px] font-bold mr-1">$</span>
                                                <input
                                                    type="number"
                                                    className="w-24 px-2.5 py-2 bg-card border border-border hover:border-border-strong focus:border-brand-indigo focus:ring-2 focus:ring-brand-indigo/10 rounded-xl text-[11px] font-bold text-foreground outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                    value={item.costoOrdenTrabajo}
                                                    onChange={(e) => handleUpdateItem(item.id, 'costoOrdenTrabajo', e.target.value)}
                                                    disabled={isReadOnly}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-4 py-4 bg-primary/5 text-xs font-bold text-primary italic">
                                            ${(item.costosGenerales || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-4 py-4 bg-muted text-xs font-extrabold text-foreground tracking-tight">
                                            ${(item.costoTotalUnitario || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-4 py-4 bg-sidebar text-xs font-black text-white tracking-wider">
                                            ${(item.costoTotal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-4 py-4 bg-warning/5 text-xs font-bold text-warning">
                                            {((item.mgSobreCosto || 0) * 100).toFixed(2)}%
                                        </td>
                                        <td className="px-4 py-4 bg-warning/5 text-xs font-bold text-warning">
                                            {((item.mgSobreVenta || 0) * 100).toFixed(2)}%
                                        </td>
                                        <td className="px-4 py-4 bg-warning/10 text-xs font-extrabold text-warning tracking-tight">
                                            ${(item.mgSobreVentaPesos || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-4 py-4 text-center">
                                            {!isReadOnly && (
                                                <button
                                                    onClick={() => setItems(items.filter(i => i.id !== item.id))}
                                                    className="p-2.5 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-xl transition-all"
                                                >
                                                    <Trash2 className="w-4 h-4" />
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot className="sticky bottom-0">
                                <tr className="bg-sidebar text-sidebar-foreground font-extrabold text-[11px] uppercase tracking-wider border-t border-sidebar-border">
                                    <td className="px-4 py-5 italic border-r border-sidebar-border" colSpan="1">TOTAL</td>
                                    <td className="px-4 py-5 border-r border-sidebar-border text-white font-black">
                                        {totals.itemsConCostos.reduce((sum, item) => sum + (item.cant || 0), 0)}
                                    </td>
                                    <td colSpan="2" className="border-r border-sidebar-border"></td>
                                    <td className="px-4 py-5 border-r border-sidebar-border text-base font-black text-success bg-black/20">
                                        ${(totals.subtotalVenta || 0).toLocaleString('es-CL')}
                                    </td>
                                    <td colSpan="9" className="border-r border-sidebar-border"></td>
                                    <td className="px-4 py-5 border-r border-sidebar-border bg-black/35 text-center font-bold text-white">
                                        ${(totals.itemsConCostos.reduce((sum, item) => sum + ((item.costoProducto || 0) * (item.cant || 0)), 0)).toLocaleString('es-CL')}
                                    </td>
                                    <td className="px-4 py-5 border-r border-sidebar-border bg-black/35 text-center font-bold text-white">
                                        ${(totals.itemsConCostos.reduce((sum, item) => sum + ((item.costoLogo || 0) * (item.cant || 0)), 0)).toLocaleString('es-CL')}
                                    </td>
                                    <td className="px-4 py-5 border-r border-sidebar-border bg-black/35 text-center font-bold text-white">
                                        <div className="flex flex-col items-center">
                                            <span>${(totals.itemsConCostos.reduce((sum, item) => sum + ((item.costoOrdenTrabajo || 0) * (item.cant || 0)), 0)).toLocaleString('es-CL')}</span>
                                            <span className="text-[8px] text-brand-violet font-extrabold tracking-wider mt-0.5 opacity-90">+ CINTA (${(totals.totalPC || 0).toLocaleString('es-CL')})</span>
                                        </div>
                                    </td>
                                    <td className="px-4 py-5 border-r border-sidebar-border bg-black/35 text-center font-bold text-primary">
                                        ${(totals.prorrateoLineal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}/u
                                    </td>
                                    <td colSpan="1" className="border-r border-sidebar-border"></td>
                                    <td className="px-4 py-5 border-r border-sidebar-border bg-black/50 text-base font-black text-white">
                                        ${(totals.totalCostoGeneral || 0).toLocaleString('es-CL')}
                                    </td>
                                    <td className="px-4 py-5 bg-black/40 text-center font-black border-l border-sidebar-border" colSpan="3">
                                        <div className="flex flex-col gap-0.5">
                                            <span className="text-warning text-xs">MARGEN: <span className="text-warning text-base">{totals.margenPorc}%</span></span>
                                            <span className="text-warning text-xs">NETO: <span className="text-white font-black">${(totals.margenPesos || 0).toLocaleString('es-CL')}</span></span>
                                        </div>
                                    </td>
                                    <td className={`flex items-center justify-center h-full transition-all ${isReadOnly ? 'bg-sidebar-popup' : 'bg-brand-indigo hover:bg-brand-indigo/90'}`}>
                                        {!isReadOnly && (
                                            <button
                                                onClick={() => setItems([...items, { ...DEFAULT_ITEM, id: Date.now(), numero: items.length + 1 }])}
                                                className="w-10 h-10 bg-white/20 hover:bg-white/30 rounded-xl transition-all flex items-center justify-center group"
                                            >
                                                <Plus className="w-5 h-5 text-white group-hover:scale-125 transition-all" />
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 pb-10">
                    <div className="lg:col-span-2 space-y-8">
                        <div className="bg-card rounded-[2rem] shadow-sm border border-border p-8">
                            <div className="flex items-center space-x-3 mb-8">
                                <div className="w-2 h-6 bg-brand-indigo rounded-full" />
                                <h3 className="text-sm font-black text-foreground uppercase tracking-widest italic">Otros Costos y Gastos Operacionales</h3>
                            </div>

                            {/* Grid de gastos adicionales — 7 campos */}
                            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-7 gap-4 mb-10">
                                {[
                                    { key: 'garantiaSeriedad',         label: 'Garantía Seriedad', prefix: '$' },
                                    { key: 'garantiaFielCumplimiento', label: 'Garantía Cumplimiento', prefix: '$' },
                                    { key: 'flete',                    label: 'Flete Especial', prefix: '$' },
                                    { key: 'certificacion',            label: 'Certificación', prefix: '$' },
                                    { key: 'muestras',                 label: 'Muestras Físicas', prefix: '$' },
                                    { key: 'entregaPersonalizada',     label: 'Entrega Personalizada', prefix: '$' },
                                    { key: 'porcentajeComision',       label: '% Comisión Ejecutivo', prefix: '%' },
                                ].map(({ key, label, prefix }) => (
                                    <div key={key}>
                                        <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-1.5 ml-1 h-8 flex items-end">{label}</p>
                                        <div className="relative">
                                            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground/50 text-[10px] font-black pointer-events-none">{prefix}</span>
                                            <input
                                                type="number"
                                                step={key === 'porcentajeComision' ? "0.1" : "1"}
                                                className="w-full pl-6 pr-3 py-2.5 bg-muted border border-border rounded-xl text-xs font-black text-foreground focus:ring-2 focus:ring-brand-indigo/20 focus:border-brand-indigo/40 outline-none transition-all disabled:opacity-60 disabled:cursor-default"
                                                value={otrosCostos[key] ?? 0}
                                                onChange={(e) => setOtrosCostos({ ...otrosCostos, [key]: parseFloat(e.target.value) || 0 })}
                                                disabled={isReadOnly}
                                            />
                                        </div>
                                    </div>
                                ))}
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                                {/* Toma de Tallaje */}
                                <div className="p-6 bg-brand-indigo/10 rounded-[2rem] border border-brand-indigo/20 relative group overflow-hidden">
                                    <div className="absolute top-0 right-0 w-32 h-32 bg-brand-indigo/10 rounded-full -mr-16 -mt-16 group-hover:scale-110 transition-transform duration-700" />
                                    <div className="relative">
                                        <p className="text-[10px] font-black text-brand-indigo uppercase tracking-widest mb-6 flex items-center">
                                            <Users className="w-4 h-4 mr-2" />
                                            Toma de Tallaje
                                        </p>
                                        <div className="grid grid-cols-2 gap-4">
                                            {[
                                                { field: 'diasRecinto',        label: 'Días x Recinto' },
                                                { field: 'persRecinto',        label: 'Personal x recinto' },
                                                { field: 'colacionPorPersona', label: 'Colaccion x persona' },
                                                { field: 'asignacionPorPersona', label: 'Asignacion x persona' },
                                                { field: 'peajes',             label: 'Peajes' },
                                                { field: 'bencinaPorLitro',    label: 'Bencina $/Lt' },
                                                { field: 'kmTotal',            label: 'Km Totales(+10%)' },
                                                { field: 'rendimiento',        label: 'Rendimiento KM/LT' },
                                                { field: 'cantRecintos',       label: 'Recintos' },
                                            ].map(({ field, label }) => (
                                                <div key={field}>
                                                    <p className="text-[8px] font-black text-brand-indigo/70 uppercase mb-1 h-6 flex items-end">{label}</p>
                                                    <input
                                                        type="number"
                                                        className="w-full px-3 py-2 bg-card border border-brand-indigo/20 rounded-xl text-[11px] font-black disabled:opacity-60 disabled:cursor-default"
                                                        value={otrosCostos.tomaTallaje[field]}
                                                        onChange={(e) => setOtrosCostos({ ...otrosCostos, tomaTallaje: { ...otrosCostos.tomaTallaje, [field]: parseFloat(e.target.value) || 0 } })}
                                                        disabled={isReadOnly}
                                                    />
                                                </div>
                                            ))}
                                        </div>
                                        <div className="mt-6 pt-4 border-t border-brand-indigo/20 flex justify-between items-center">
                                            <div className="text-[7px] space-y-0.5">
                                                <p className="font-black text-brand-indigo/60 uppercase">Personal: ${totals.costoPersonalTT.toLocaleString('es-CL')}</p>
                                                <p className="font-black text-brand-indigo/60 uppercase">Movilidad: ${totals.costoMovilizacionTT.toLocaleString('es-CL')}</p>
                                                <p className="font-black text-brand-indigo/60 uppercase">Recintos x viaje: ${totals.costoRecintosTT.toLocaleString('es-CL')}</p>
                                            </div>
                                            <div className="bg-brand-indigo text-white px-4 py-2 rounded-xl text-center shadow-lg shadow-brand-indigo/20">
                                                <p className="text-[7px] font-black uppercase opacity-80">Total TT</p>
                                                <p className="text-sm font-black">${totals.totalTT.toLocaleString('es-CL')}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                {/* Pegado de Cinta */}
                                <div className="p-6 bg-brand-violet/10 rounded-[2rem] border border-brand-violet/20 relative group overflow-hidden">
                                    <div className="absolute top-0 right-0 w-32 h-32 bg-brand-violet/10 rounded-full -mr-16 -mt-16 group-hover:scale-110 transition-transform duration-700" />
                                    <div className="relative">
                                        <p className="text-[10px] font-black text-brand-violet uppercase tracking-widest mb-4 flex items-center">
                                            <Calculator className="w-4 h-4 mr-2" />
                                            Costos Pegado de Cinta
                                        </p>
                                        <div className="space-y-4">
                                            {(otrosCostos.pegadoCinta || []).map((itemCinta, idx) => (
                                                <div key={itemCinta.id} className="bg-card p-3 rounded-2xl border border-brand-violet/20 shadow-sm flex flex-col gap-2">
                                                    <p className="text-[9px] font-black text-brand-violet uppercase italic">{itemCinta.etiqueta}</p>
                                                    <div className="grid grid-cols-3 gap-2">
                                                        <div>
                                                            <p className="text-[7px] font-black text-muted-foreground uppercase mb-0.5">Cinta $</p>
                                                            <input
                                                                type="number"
                                                                className="w-full px-1.5 py-1 bg-card border border-brand-violet/20 rounded text-[9px] font-bold disabled:opacity-60 disabled:cursor-default"
                                                                value={itemCinta.costoCinta}
                                                                onChange={(e) => {
                                                                    const newLista = [...otrosCostos.pegadoCinta];
                                                                    newLista[idx].costoCinta = parseFloat(e.target.value) || 0;
                                                                    setOtrosCostos({ ...otrosCostos, pegadoCinta: newLista });
                                                                }}
                                                                disabled={isReadOnly}
                                                            />
                                                        </div>
                                                        <div>
                                                            <p className="text-[7px] font-black text-muted-foreground uppercase mb-0.5">MO $</p>
                                                            <input
                                                                type="number"
                                                                className="w-full px-1.5 py-1 bg-card border border-brand-violet/20 rounded text-[9px] font-bold disabled:opacity-60 disabled:cursor-default"
                                                                value={itemCinta.costoMO}
                                                                onChange={(e) => {
                                                                    const newLista = [...otrosCostos.pegadoCinta];
                                                                    newLista[idx].costoMO = parseFloat(e.target.value) || 0;
                                                                    setOtrosCostos({ ...otrosCostos, pegadoCinta: newLista });
                                                                }}
                                                                disabled={isReadOnly}
                                                            />
                                                        </div>
                                                        <div>
                                                            <p className="text-[7px] font-black text-muted-foreground uppercase mb-0.5">MTS</p>
                                                            <input
                                                                type="number"
                                                                step="0.1"
                                                                className="w-full px-1.5 py-1 bg-card border border-brand-violet/20 rounded text-[9px] font-bold disabled:opacity-60 disabled:cursor-default"
                                                                value={itemCinta.mtsCinta}
                                                                onChange={(e) => {
                                                                    const newLista = [...otrosCostos.pegadoCinta];
                                                                    newLista[idx].mtsCinta = parseFloat(e.target.value) || 0;
                                                                    setOtrosCostos({ ...otrosCostos, pegadoCinta: newLista });
                                                                }}
                                                                disabled={isReadOnly}
                                                            />
                                                        </div>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                        <div className="mt-4 pt-4 border-t border-brand-violet/20 flex justify-between items-center">
                                            <div className="text-left">
                                                <p className="text-[8px] font-black text-brand-violet/70 uppercase leading-none">Total Cinta</p>
                                                <p className="text-[8px] text-brand-violet/50 italic font-bold">Sumado a costos OT</p>
                                            </div>
                                            <div className="bg-brand-violet text-white px-4 py-2 rounded-xl text-center shadow-lg shadow-brand-violet/20">
                                                <p className="text-[7px] font-black uppercase opacity-80 leading-none mb-1">Subtotal</p>
                                                <p className="text-sm font-black">${totals.totalPC.toLocaleString('es-CL')}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="mt-8 pt-8 border-t border-brand-indigo/20 flex justify-between items-center bg-brand-indigo/5 p-4 rounded-2xl">
                                <div>
                                    <p className="text-[10px] font-black text-brand-indigo/70 uppercase tracking-[0.2em]">Total Otros Costos (Auxiliares)</p>
                                    <p className="text-[10px] text-brand-indigo/50 font-bold italic">Suma de todas las secciones auxiliares</p>
                                </div>
                                <p className="text-3xl font-black text-brand-indigo tracking-tighter">${totals.totalOtrosCostos.toLocaleString('es-CL')}</p>
                            </div>
                        </div>


                    </div>

                    <div className="space-y-6">
                        <EVNResumenSidebar totals={totals} otrosCostos={otrosCostos} />
                    </div>
                </div>


                <QuotationSelectionModal
                    open={showQuotationModal}
                    pendingSCOS={pendingSCOS}
                    availableQuotations={availableQuotations}
                    onApply={applySCOSQuotation}
                    onClose={() => setShowQuotationModal(false)}
                />

                <CosteoSelectionModal
                    open={showCosteoModal}
                    costeos={costeosDisponibles}
                    loading={loadingCosteos}
                    currentCosteoId={items.find(i => i.id === costeoModalItemId)?.costeoId || null}
                    onSelect={handleSelectCosteo}
                    onClose={closeCosteoSelector}
                />

                <FirmaAprobacionModal
                    open={cerrarModal.open}
                    title="Cerrar Evaluación de Negocio"
                    description="Al cerrar la EVN ya no podrá usarse como plantilla ni generar nuevas Notas de Venta. Esta acción es definitiva."
                    accion="Cerrar y firmar"
                    accentColor="slate"
                    defaultAprobador={user?.name || ''}
                    onClose={() => setCerrarModal({ open: false })}
                    onConfirm={handleConfirmCerrar}
                    isSubmitting={cerrando}
                />
            </div>
        </div>
    );
}
