import React from 'react';
import { FileText, ChevronRight, Save, Plus, Trash2, Wrench, CheckCircle2, AlertCircle } from 'lucide-react';
import { pdfService } from '../../../remote/service/pdfService';

export default function FormularioNV({
    formData,
    setFormData,
    isReadOnly,
    sourceEVN,
    nextNumbers,
    clientes,
    loadingClientes,
    vendedores,
    proveedores,
    addItem,
    removeItem,
    updateItem,
    updateSize,
    handleConfirmNV,
    totalItems,
    totalAmount,
    isSubmitting,
    submitStatus,
    setView
}) {
    return (
        <div className="animate-in fade-in duration-500 space-y-8 bg-muted/50 min-h-screen p-6 pb-24">
            <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between mb-10 bg-card p-8 rounded-[2.5rem] shadow-sm border border-border">
                <div className="flex items-center space-x-6">
                    <button onClick={() => setView('list')} className="p-3 hover:bg-muted rounded-2xl transition-all group">
                        <ChevronRight className="w-6 h-6 rotate-180 text-muted-foreground group-hover:-translate-x-1 transition-transform" />
                    </button>
                    <div className="w-14 h-14 bg-primary/10 rounded-2xl flex items-center justify-center shadow-sm">
                        <FileText className="w-8 h-8 text-primary" />
                    </div>
                    <div>
                        <h2 className="text-3xl font-black text-foreground tracking-tight italic">
                            {isReadOnly ? 'Detalle Nota de Venta' : sourceEVN ? 'Generar desde Plantilla' : 'Nueva Nota de Venta'}
                        </h2>
                        <div className="flex items-center space-x-3 mt-1">
                            {sourceEVN && (
                                <span className="text-xs font-black text-primary bg-primary/10 px-3 py-1 rounded-full uppercase tracking-widest">
                                    Plantilla EVN: #{sourceEVN}
                                </span>
                            )}
                        </div>
                    </div>
                </div>
                <div className="flex gap-4 mt-6 md:mt-0">
                    <div className="bg-primary/5 px-6 py-3 rounded-2xl border border-primary/20 flex items-center space-x-4">
                        <div>
                            <p className="text-[9px] font-black text-primary/70 uppercase tracking-widest">Folio Correlativo</p>
                            <p className="text-2xl font-black text-primary italic font-mono">#{isReadOnly ? formData.numeroNV : nextNumbers.nv}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto space-y-8">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <div className="bg-card p-6 rounded-[2rem] border border-border shadow-sm">
                        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block mb-3">Cliente Receptor <span className="text-destructive">*</span></label>
                        <select
                            className="w-full bg-muted border-none rounded-xl p-3 outline-none font-bold text-sm appearance-none"
                            value={formData.clienteId}
                            onChange={(e) => setFormData({ ...formData, clienteId: e.target.value })}
                            disabled={isReadOnly || !!sourceEVN || loadingClientes}
                        >
                            <option value="">{loadingClientes ? 'Cargando clientes...' : 'Seleccione un cliente...'}</option>
                            {clientes.map(c => (
                                <option key={c.clienteId} value={c.clienteId}>
                                    {c.razonSocial || `${c.nombreCliente} ${c.apellidoCliente}`.trim()} ({c.runCliente})
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="bg-card p-6 rounded-[2rem] border border-border shadow-sm">
                        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest block mb-3">Vendedor</label>
                        <select
                            className="w-full bg-muted border-none rounded-xl p-3 outline-none font-bold text-sm appearance-none"
                            value={formData.vendedorId}
                            onChange={(e) => setFormData({ ...formData, vendedorId: e.target.value })}
                            disabled={isReadOnly}
                        >
                            <option value="">Seleccionar...</option>
                            {vendedores.map(v => (
                                <option key={v.idVendedor} value={v.idVendedor}>
                                    {v.nombreVendedor}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="bg-card p-6 rounded-[2rem] border border-border shadow-sm grid grid-cols-2 gap-4">
                        <div>
                            <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest mb-3 block">Emisión</label>
                            <div className="text-sm font-black text-muted-foreground">{isReadOnly ? formData.fechaEmision : new Date().toLocaleDateString()}</div>
                        </div>
                        <div>
                            <label className="text-[10px] font-black text-primary uppercase tracking-widest mb-3 block italic">Entrega</label>
                            <input type="date" className="w-full bg-primary/5 border-none rounded-xl p-2 font-black text-xs text-primary outline-none" value={formData.fechaEntregaEstimada} onChange={(e) => setFormData({ ...formData, fechaEntregaEstimada: e.target.value })} disabled={isReadOnly} />
                        </div>
                    </div>
                    <div className="p-6 rounded-[2rem] border transition-all bg-card text-muted-foreground">
                        <label className="text-[10px] font-black uppercase tracking-widest block mb-3">¿Lleva Personalizado?</label>
                        <button onClick={() => !isReadOnly && setFormData({ ...formData, esKit: !formData.esKit })} className="flex items-center space-x-3 w-full">
                            <div className={`w-10 h-6 rounded-full relative ${formData.esKit ? 'bg-primary' : 'bg-muted'}`}>
                                <div className={`absolute top-1 w-4 h-4 bg-card rounded-full transition-all ${formData.esKit ? 'right-1' : 'left-1'}`} />
                            </div>
                            <span className={`text-xs font-black uppercase tracking-widest ${formData.esKit ? 'text-primary' : 'text-muted-foreground'}`}>
                                {formData.esKit ? 'Sí' : 'No'}
                            </span>
                        </button>
                        {formData.esKit && (
                            <div className="mt-4">
                                <textarea
                                    className="w-full bg-primary/5 border-none rounded-xl p-3 text-[10px] font-bold text-primary uppercase outline-none placeholder:text-primary/40 resize-none"
                                    placeholder="Especificar armado del personalizado..."
                                    value={formData.detalleKit || ''}
                                    onChange={(e) => setFormData({ ...formData, detalleKit: e.target.value.toUpperCase() })}
                                    readOnly={isReadOnly}
                                    rows="2"
                                />
                            </div>
                        )}
                    </div>
                </div>

                <div className="bg-card border border-border rounded-[3rem] shadow-sm overflow-hidden">
                    <div className="p-8 bg-muted/50 border-b border-border flex justify-between items-center">
                        <div className="flex items-center space-x-3"><FileText className="w-5 h-5 text-muted-foreground" /><h3 className="font-black text-sm uppercase text-foreground italic">Configuración Técnica de Ítems</h3></div>
                        {!isReadOnly && (
                            <button onClick={addItem} className="px-6 py-3 bg-primary text-primary-foreground rounded-2xl font-black text-xs uppercase hover:bg-foreground hover:text-background transition-all flex items-center space-x-2">
                                <Plus className="w-4 h-4" />
                                <span>Añadir Prenda</span>
                            </button>
                        )}
                    </div>

                    <div className="p-8 space-y-6">
                        {formData.items.map((item) => (
                            <div key={item.id} className="p-8 rounded-[2.5rem] border-2 border-border hover:border-primary/30 transition-all space-y-6 relative group">
                                <div className="flex items-center gap-2 p-1 bg-muted/50 rounded-xl w-fit">
                                    {['OP', 'SC', 'SCI'].map(type => (
                                        <button key={type} disabled={isReadOnly} onClick={() => updateItem(item.id, 'tipoItem', type)} className={`px-4 py-1.5 rounded-lg text-[9px] font-black uppercase tracking-widest transition-all ${item.tipoItem === type ? 'bg-primary text-primary-foreground shadow-md' : 'text-muted-foreground'}`}>
                                            {type === 'OP' ? 'Orden de Producción(OP)' : type === 'SC' ? 'Solicitud de Compra(SC)' : 'Solicitud de Compra Internacional(SCI)'}
                                        </button>
                                    ))}
                                </div>
                                <div className="grid grid-cols-12 gap-6">
                                    <div className="col-span-3">
                                        <div className="flex items-center justify-between mb-2">
                                            <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest block">Producto <span className="text-destructive">*</span></label>
                                            {item.codigoInterno && (
                                                <span className="text-[8px] font-black bg-warning/10 text-warning px-2 py-0.5 rounded-full border border-warning/20 uppercase tracking-tighter">
                                                    Ref: {item.codigoInterno}
                                                </span>
                                            )}
                                        </div>
                                        <input className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none" value={item.nombreProducto} onChange={(e) => updateItem(item.id, 'nombreProducto', e.target.value.toUpperCase())} readOnly={isReadOnly} />
                                    </div>
                                    <div className="col-span-2">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Modelo</label>
                                        <input className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none" value={item.modelo} onChange={(e) => updateItem(item.id, 'modelo', e.target.value.toUpperCase())} readOnly={isReadOnly} />
                                    </div>
                                    <div className="col-span-2">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Tela</label>
                                        <input className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none" value={item.tela} onChange={(e) => updateItem(item.id, 'tela', e.target.value.toUpperCase())} readOnly={isReadOnly} />
                                    </div>
                                    <div className="col-span-2">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Género <span className="text-destructive">*</span></label>
                                        <select className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none appearance-none" value={item.genero} onChange={(e) => updateItem(item.id, 'genero', e.target.value)} disabled={isReadOnly}>
                                            <option value="Unisex">Unisex</option><option value="Masculino">Masculino</option><option value="Femenino">Femenino</option>
                                        </select>
                                    </div>
                                    <div className="col-span-1">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block text-center">Cant.</label>
                                        <button onClick={() => !isReadOnly && updateItem(item.id, 'showSizeMenu', !item.showSizeMenu)} className="w-full p-3 bg-foreground text-background rounded-xl text-[10px] font-black shadow-lg">
                                            {item.quantity} und
                                        </button>
                                        {item.showSizeMenu && !isReadOnly && (
                                            <div className="absolute right-0 mt-2 w-40 bg-card rounded-2xl shadow-2xl border border-border p-4 z-20 animate-in fade-in zoom-in-95">
                                                {['XS', 'S', 'M', 'L', 'XL'].map(size => (
                                                    <div key={size} className="flex items-center justify-between mb-2">
                                                        <span className="text-[10px] font-bold text-muted-foreground w-6">{size}</span>
                                                        <input type="number" value={item.sizes[size]} onChange={(e) => updateSize(item.id, size, e.target.value)} className="w-16 p-1 bg-muted border-none rounded-lg text-xs font-black text-center" />
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                    <div className="col-span-2">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">P. Unitario</label>
                                        <div className="relative">
                                            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground text-xs font-bold">$</span>
                                            <input type="number" value={item.unitPrice} onChange={(e) => updateItem(item.id, 'unitPrice', parseFloat(e.target.value) || 0)} className="w-full pl-6 pr-3 py-3 bg-primary/5 border-none rounded-xl text-xs font-black text-primary outline-none" readOnly={isReadOnly} />
                                        </div>
                                    </div>
                                </div>
                                <div className="grid grid-cols-12 gap-6 pt-4 border-t border-border">
                                    <div className="col-span-3">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Color</label>
                                        <input className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none" value={item.color || ''} onChange={(e) => updateItem(item.id, 'color', e.target.value.toUpperCase())} readOnly={isReadOnly} />
                                    </div>
                                    <div className="col-span-3">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Composición</label>
                                        <input className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none" value={item.composicion || ''} onChange={(e) => updateItem(item.id, 'composicion', e.target.value.toUpperCase())} readOnly={isReadOnly} />
                                    </div>
                                    <div className="col-span-3">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Logo</label>
                                        <select className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none appearance-none" value={item.logo} onChange={(e) => updateItem(item.id, 'logo', e.target.value)} disabled={isReadOnly}>
                                            <option value="N/A">N/A</option><option value="Bordado">Bordado</option><option value="Estampado">Estampado</option><option value="Bordado y Estampado">Bordado y Estampado</option>
                                        </select>
                                    </div>
                                    <div className="col-span-3">
                                        <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest mb-2 block">Proveedor Sugerido</label>
                                        <select className="w-full p-3 bg-muted border-none rounded-xl text-[11px] font-bold uppercase outline-none appearance-none"
                                            value={item.proveedorId || ''}
                                            onChange={(e) => updateItem(item.id, 'proveedorId', e.target.value)}
                                            disabled={isReadOnly}
                                        >
                                            <option value="">Seleccionar...</option>
                                            {proveedores.map(p => (
                                                <option key={p.proveedorId || p.id} value={p.proveedorId || p.id}>{p.nombreProveedor || p.nombre}</option>
                                            ))}
                                        </select>
                                    </div>
                                </div>
                                <div className="flex items-center justify-end gap-3 pt-4 border-t border-border">
                                    <button onClick={() => updateItem(item.id, 'requiereOt', !item.requiereOt)} className={`flex items-center space-x-2 px-4 py-2 rounded-xl transition-all ${item.requiereOt ? 'bg-warning text-white' : 'bg-muted text-muted-foreground'}`}>
                                        <Wrench className="w-4 h-4" /><span className="text-[10px] font-black uppercase">Requerimientos OT</span>
                                    </button>
                                    {!isReadOnly && (
                                        <button onClick={() => removeItem(item.id)} className="p-2 text-destructive/60 hover:text-destructive transition-all">
                                            <Trash2 className="w-5 h-5" />
                                        </button>
                                    )}
                                </div>
                                {item.requiereOt && (
                                    <div className="mt-4 p-6 bg-warning/5 border-2 border-dashed border-warning/20 rounded-3xl animate-in slide-in-from-top-4">
                                        <textarea rows="2" placeholder="Describa requerimientos especiales..." className="w-full bg-transparent border-none text-xs font-bold text-foreground uppercase outline-none resize-none" value={item.detalleOt || ''} onChange={(e) => updateItem(item.id, 'detalleOt', e.target.value.toUpperCase())} readOnly={isReadOnly} />
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>

                {!isReadOnly && (
                    <div className="bg-sidebar rounded-[3rem] p-10 text-white shadow-2xl flex flex-col lg:flex-row gap-10 items-center justify-between">
                        <div className="flex gap-10">
                            <div><p className="text-[10px] font-black text-sidebar-primary uppercase tracking-widest mb-2 italic">Total Prendas</p><p className="text-4xl font-black font-mono">{totalItems.toLocaleString()}</p></div>
                            <div className="border-l border-sidebar-border pl-10"><p className="text-[10px] font-black text-success uppercase tracking-widest mb-2 italic">Total Neto (CLP)</p><p className="text-4xl font-black font-mono text-success">${totalAmount.toLocaleString()}</p></div>
                        </div>
                        <div className="flex gap-4">
                            <button onClick={() => setView('list')} className="px-10 py-5 bg-white/5 hover:bg-white/10 text-white rounded-3xl text-[10px] uppercase font-black tracking-widest">Cancelar</button>
                            <button
                                onClick={() => pdfService.generateNV(formData)}
                                disabled={formData.items.length === 0}
                                className="px-8 py-5 bg-sidebar-popup hover:bg-sidebar-active-bg text-white font-black rounded-3xl text-[10px] uppercase tracking-widest flex items-center gap-2"
                            >
                                <FileText className="w-4 h-4" /> PDF
                            </button>
                            <button onClick={handleConfirmNV} disabled={isSubmitting || !formData.clienteId || formData.vendedorId === '' || formData.items.length === 0} className="px-12 py-5 bg-primary hover:bg-primary-hover text-primary-foreground font-black rounded-3xl text-[10px] uppercase tracking-[0.2em] shadow-2xl flex items-center gap-4 disabled:opacity-50 transition-all">
                                {isSubmitting ? <div className="animate-spin w-4 h-4 border-2 border-white/20 border-t-white rounded-full" /> : <Save className="w-4 h-4" />}
                                <span>{isSubmitting ? 'Procesando...' : 'Emitir Nota de Venta'}</span>
                            </button>
                        </div>
                    </div>
                )}
            </div>

            {submitStatus && (
                <div className={`fixed bottom-10 right-10 p-6 rounded-3xl shadow-3xl flex items-center gap-4 animate-in slide-in-from-right-10 duration-500 z-50 ${submitStatus.type === 'success' ? 'bg-success text-white' : 'bg-destructive text-white'}`}>
                    {submitStatus.type === 'success' ? <CheckCircle2 className="w-6 h-6" /> : <AlertCircle className="w-6 h-6" />}
                    <p className="font-black text-sm uppercase tracking-widest">{submitStatus.message}</p>
                </div>
            )}
        </div>
    );
}
