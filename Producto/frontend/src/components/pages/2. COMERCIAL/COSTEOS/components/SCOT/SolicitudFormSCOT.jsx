import { useState } from "react";
import SolicitudHeader from "../../../../../../components/layout/SolicitudHeader";
import PrendasSCOTPanel from "./PrendasSCOTPanel";
import { useClientes } from "../../../../../../hooks/useClientes";
import { useVendedores } from "../../../../../../hooks/useVendedores";

const generateId = () => {
    try { return crypto.randomUUID(); }
    catch (e) { return Math.random().toString(36).substring(2, 15); }
};

export default function SolicitudFormSCOT({
                                              formData,
                                              setFormData,
                                              onSave,
                                              onBack,
                                              readOnly = false
                                          }) {
    const { clientes } = useClientes();
    const { vendedores } = useVendedores();

    const handleAddPrenda = () => {
        if (readOnly) return;
        setFormData(prev => ({
            ...prev,
            prendas: [...(prev.prendas || []), {
                id: generateId(),
                nombre: "",
                proveedorReferencia: "",
                cantidad: 0,
            }]
        }));
    };

    const handleUpdatePrenda = (id, field, value) => {
        if (readOnly) return;
        setFormData(prev => ({
            ...prev,
            prendas: prev.prendas.map(p =>
                p.id === id ? { ...p, [field]: value } : p
            )
        }));
    };

    const handleRemovePrenda = (id) => {
        if (readOnly) return;
        setFormData(prev => ({
            ...prev,
            prendas: prev.prendas.filter(p => p.id !== id)
        }));
    };

    return (
        <div className="max-w-6xl mx-auto p-6 space-y-10 animate-in fade-in duration-500">
            <SolicitudHeader
                formData={formData}
                onBack={onBack}
                onSave={onSave}
                readOnly={readOnly}
            />

            {/* Datos Generales */}
            <div className={`bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100 space-y-8 ${readOnly ? "opacity-90" : ""}`}>

                {/* Fila 1: Cliente / Vendedor */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Cliente</label>
                        <select
                            value={formData.clienteId || ""}
                            onChange={(e) => setFormData({ ...formData, clienteId: e.target.value })}
                            disabled={readOnly}
                            className={`w-full p-3 rounded-xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 ${readOnly ? "cursor-not-allowed" : ""}`}
                        >
                            <option value="">Seleccione Cliente</option>
                            {clientes.map((c) => (
                                <option key={c.clienteId} value={c.clienteId}>
                                    {c.nombreCliente} {c.apellidoCliente}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Vendedor</label>
                        <select
                            value={formData.vendedorId || ""}
                            onChange={(e) => setFormData({ ...formData, vendedorId: e.target.value })}
                            disabled={readOnly}
                            className={`w-full p-3 rounded-xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 ${readOnly ? "cursor-not-allowed" : ""}`}
                        >
                            <option value="">Seleccione Vendedor</option>
                            {vendedores.map((v) => (
                                <option key={v.id} value={v.id}>
                                    {v.nombreUsuario} {v.apellidosUsuario}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Fila 2: Es Muestra / Observaciones */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black uppercase tracking-widest text-gray-400">Observaciones</label>
                        <textarea
                            rows={3}
                            value={formData.observaciones || ""}
                            readOnly={readOnly}
                            onChange={(e) => setFormData({ ...formData, observaciones: e.target.value })}
                            className={`w-full p-3 rounded-xl bg-gray-50 border border-gray-100 text-sm font-bold outline-none focus:ring-2 focus:ring-blue-500 resize-none ${readOnly ? "cursor-not-allowed" : ""}`}
                            placeholder="Indicaciones generales para adquisiciones..."
                        />
                    </div>

                    <div className="flex items-center gap-3 pt-6">
                        <input
                            type="checkbox"
                            id="esMuestra"
                            checked={formData.esMuestra || false}
                            disabled={readOnly}
                            onChange={(e) => setFormData({ ...formData, esMuestra: e.target.checked })}
                            className={`w-5 h-5 rounded border-gray-300 text-blue-600 focus:ring-blue-500 ${readOnly ? "cursor-not-allowed" : "cursor-pointer"}`}
                        />
                        <label htmlFor="esMuestra" className="text-xs font-black text-gray-700 uppercase tracking-widest">
                            Es Muestra
                        </label>
                    </div>
                </div>
            </div>

            {/* Panel de Prendas */}
            <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100">
                <PrendasSCOTPanel
                    data={formData.prendas}
                    onAdd={handleAddPrenda}
                    onUpdate={handleUpdatePrenda}
                    onRemove={handleRemovePrenda}
                    readOnly={readOnly}
                />
            </div>
        </div>
    );
}
