import React, { useState } from 'react';
import { ChevronLeft, Save, Factory, Info } from 'lucide-react';

const TIPOS = ['BORDADO', 'ESTAMPADO', 'LAVADO', 'SUBLIMADO', 'OTRO'];

export default function CrearOS({ back, crear, submitting, error, formatCLP }) {
    const [form, setForm] = useState({
        opId: '',
        proveedorId: '',
        tipoServicio: 'BORDADO',
        cantidadPactada: '',
        precioUnitario: '',
        fechaEntregaEstimada: '',
        descripcionTrabajo: '',
        observaciones: '',
    });

    const set = (field, value) => setForm(prev => ({ ...prev, [field]: value }));

    const canSubmit =
        Number(form.opId) > 0 &&
        Number(form.proveedorId) > 0 &&
        Number(form.cantidadPactada) > 0 &&
        !!form.tipoServicio &&
        !submitting;

    const totalEstimado = (Number(form.cantidadPactada) || 0) * (Number(form.precioUnitario) || 0);

    const handleSubmit = async () => {
        if (!canSubmit) return;
        await crear({
            opId: Number(form.opId),
            proveedorId: Number(form.proveedorId),
            tipoServicio: form.tipoServicio,
            cantidadPactada: Number(form.cantidadPactada),
            precioUnitario: form.precioUnitario ? Number(form.precioUnitario) : 0,
            fechaEntregaEstimada: form.fechaEntregaEstimada || null,
            descripcionTrabajo: form.descripcionTrabajo || null,
            observaciones: form.observaciones || null,
        });
    };

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-32">
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-6">
                    <button onClick={back}
                        className="p-3 bg-card border border-border shadow-sm rounded-2xl hover:bg-muted transition-all text-muted-foreground hover:text-brand-indigo active:scale-95">
                        <ChevronLeft className="w-5 h-5" />
                    </button>
                    <div>
                        <h2 className="text-3xl font-black text-foreground tracking-tight uppercase">Nueva Orden de Servicio</h2>
                        <p className="text-[10px] font-black text-muted-foreground uppercase tracking-[0.2em] mt-1">Proceso externo subcontratado</p>
                    </div>
                </div>
                <button onClick={handleSubmit} disabled={!canSubmit}
                    className="px-10 h-12 bg-brand-indigo hover:bg-foreground text-white rounded-2xl font-black text-xs uppercase tracking-widest shadow-2xl shadow-brand-indigo/20 transition-all active:scale-95 flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed">
                    <Save className="w-4 h-4" />
                    {submitting ? 'Creando...' : 'Crear OS'}
                </button>
            </div>

            {error && (
                <div className="bg-destructive/10 border border-destructive/20 text-destructive text-xs font-bold uppercase tracking-widest p-4 rounded-2xl">
                    {error}
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <div className="lg:col-span-2 space-y-6">
                    <div className="bg-card rounded-[2.5rem] shadow-sm border border-border p-8 space-y-6">
                        <div className="flex items-center gap-3">
                            <Factory className="w-5 h-5 text-brand-indigo" />
                            <h3 className="text-sm font-black text-foreground uppercase tracking-widest">Datos del Servicio</h3>
                        </div>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <Field label="ID Orden de Producción (opId)">
                                <input type="number" min={1} value={form.opId} onChange={(e) => set('opId', e.target.value)}
                                    placeholder="Ej: 42"
                                    className="w-full h-12 px-4 bg-muted border border-border rounded-2xl text-sm font-black text-foreground focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                            </Field>
                            <Field label="ID Proveedor (taller)">
                                <input type="number" min={1} value={form.proveedorId} onChange={(e) => set('proveedorId', e.target.value)}
                                    placeholder="Ej: 5"
                                    className="w-full h-12 px-4 bg-muted border border-border rounded-2xl text-sm font-black text-foreground focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                            </Field>
                            <Field label="Tipo de Servicio">
                                <select value={form.tipoServicio} onChange={(e) => set('tipoServicio', e.target.value)}
                                    className="w-full h-12 px-4 bg-muted border border-border rounded-2xl text-sm font-black text-foreground focus:outline-none focus:ring-2 focus:ring-brand-indigo">
                                    {TIPOS.map(t => <option key={t} value={t}>{t}</option>)}
                                </select>
                            </Field>
                            <Field label="Fecha Entrega Estimada">
                                <input type="date" value={form.fechaEntregaEstimada} onChange={(e) => set('fechaEntregaEstimada', e.target.value)}
                                    className="w-full h-12 px-4 bg-muted border border-border rounded-2xl text-xs font-bold text-foreground focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                            </Field>
                            <Field label="Cantidad Pactada">
                                <input type="number" min={1} value={form.cantidadPactada} onChange={(e) => set('cantidadPactada', e.target.value)}
                                    placeholder="Unidades"
                                    className="w-full h-12 px-4 bg-muted border border-border rounded-2xl text-sm font-black text-foreground focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                            </Field>
                            <Field label="Precio Unitario">
                                <input type="number" min={0} step="0.01" value={form.precioUnitario} onChange={(e) => set('precioUnitario', e.target.value)}
                                    placeholder="0"
                                    className="w-full h-12 px-4 bg-muted border border-border rounded-2xl text-sm font-black text-foreground focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                            </Field>
                            <div className="md:col-span-2">
                                <Field label="Descripción del Trabajo">
                                    <textarea rows={4} value={form.descripcionTrabajo} onChange={(e) => set('descripcionTrabajo', e.target.value.toUpperCase())}
                                        placeholder="Detalle técnico del servicio a realizar..."
                                        className="w-full p-4 bg-muted border border-border rounded-2xl text-[11px] font-medium text-muted-foreground uppercase italic resize-none focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                                </Field>
                            </div>
                            <div className="md:col-span-2">
                                <Field label="Observaciones">
                                    <textarea rows={3} value={form.observaciones} onChange={(e) => set('observaciones', e.target.value.toUpperCase())}
                                        placeholder="Notas internas..."
                                        className="w-full p-4 bg-muted border border-border rounded-2xl text-[11px] font-medium text-muted-foreground uppercase italic resize-none focus:outline-none focus:ring-2 focus:ring-brand-indigo" />
                                </Field>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="space-y-6">
                    <div className="bg-sidebar rounded-[2.5rem] p-8 space-y-6 text-white">
                        <h3 className="text-[10px] font-black text-brand-indigo uppercase tracking-[0.25em]">Resumen</h3>
                        <div className="space-y-2">
                            <p className="text-[9px] font-black text-sidebar-muted uppercase tracking-widest">Total Estimado</p>
                            <p className="text-3xl font-black tracking-tighter tabular-nums text-success">{formatCLP(totalEstimado)}</p>
                        </div>
                        <div className="pt-4 border-t border-sidebar-border flex items-start gap-3">
                            <Info className="w-4 h-4 text-warning shrink-0 mt-0.5" />
                            <p className="text-[9px] font-bold text-sidebar-muted uppercase tracking-tighter italic leading-relaxed">
                                La OS nace en estado EMITIDA. Pasará a EN_PROCESO al registrar el primer despacho.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

function Field({ label, children }) {
    return (
        <div className="space-y-2">
            <label className="text-[9px] font-black text-muted-foreground uppercase tracking-widest">{label}</label>
            {children}
        </div>
    );
}
