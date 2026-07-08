import React, { useState, useEffect } from "react";
import { X, UserCheck, Save } from "lucide-react";
import { useColaboradores } from "../../hooks/useColaboradores";

const VendedorModal = ({ isOpen, onClose, onSave, vendedorToEdit }) => {
  const { colaboradores, loading: loadingColaboradores } = useColaboradores();

  const [usuarioId, setUsuarioId] = useState("");
  const [codigoVendedor, setCodigoVendedor] = useState("");

  useEffect(() => {
    if (vendedorToEdit) {
      setUsuarioId(vendedorToEdit.usuarioId ? String(vendedorToEdit.usuarioId) : "");
      setCodigoVendedor(vendedorToEdit.codigoVendedor || "");
    } else {
      setUsuarioId("");
      setCodigoVendedor("");
    }
  }, [vendedorToEdit, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({
      usuarioId: Number(usuarioId),
      codigoVendedor: codigoVendedor.trim(),
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 overlay-backdrop">
      <div className="bg-card w-full max-w-md rounded-2xl shadow-xl border border-border overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-border">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-brand-indigo/10 rounded-xl text-brand-indigo">
              <UserCheck size={20} />
            </div>
            <h2 className="text-lg font-bold text-foreground">
              {vendedorToEdit ? "Editar Vendedor" : "Registrar Nuevo Vendedor"}
            </h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-xl transition-all"
          >
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Usuario */}
          <div>
            <label className="block text-xs font-semibold text-muted-foreground mb-1.5">
              Colaborador <span className="text-destructive">*</span>
            </label>
            <select
              required
              value={usuarioId}
              onChange={(e) => setUsuarioId(e.target.value)}
              disabled={loadingColaboradores}
              className="w-full px-4 py-2.5 bg-muted border border-border rounded-xl text-sm outline-none focus:ring-2 focus:ring-brand-indigo/20 focus:border-brand-indigo transition-all disabled:opacity-60 disabled:cursor-not-allowed"
            >
              <option value="">
                {loadingColaboradores ? "Cargando colaboradores..." : "— Seleccionar colaborador —"}
              </option>
              {colaboradores.map((c) => (
                <option key={c.usuarioId} value={c.usuarioId}>
                  {[c.usuarioNombre, c.usuarioApellidos].filter(Boolean).join(" ")} — {c.usuarioRun || c.usuarioEmail}
                </option>
              ))}
            </select>
          </div>

          {/* Código Vendedor */}
          <div>
            <label className="block text-xs font-semibold text-muted-foreground mb-1.5">
              Código de Vendedor <span className="text-destructive">*</span>
            </label>
            <input
              type="text"
              required
              value={codigoVendedor}
              onChange={(e) => setCodigoVendedor(e.target.value)}
              placeholder="Ej: VND-001, V-100..."
              className="w-full px-4 py-2.5 bg-muted border border-border rounded-xl text-sm outline-none focus:ring-2 focus:ring-brand-indigo/20 focus:border-brand-indigo transition-all"
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-brand-indigo hover:opacity-90 text-white rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all shadow-sm hover:shadow-md active:scale-[0.98]"
          >
            <Save className="w-4 h-4" />
            {vendedorToEdit ? "Guardar Cambios" : "Registrar Vendedor"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default VendedorModal;
