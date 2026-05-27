import React, { useState, useEffect } from "react";
import { X, UserCheck } from "lucide-react";
import { useColaboradores } from "../../../hooks/useColaboradores";

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
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in duration-300">
      <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md overflow-hidden animate-in zoom-in-95 duration-300">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-100">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-emerald-100 rounded-xl text-emerald-600">
              <UserCheck size={20} />
            </div>
            <h2 className="text-xl font-bold text-slate-800">
              {vendedorToEdit ? "Editar Vendedor" : "Registrar Nuevo Vendedor"}
            </h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-slate-50 rounded-full transition-colors text-slate-400"
          >
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Usuario */}
          <div>
            <label className="block text-sm font-semibold text-slate-700 mb-2">
              Colaborador <span className="text-red-500">*</span>
            </label>
            <select
              required
              value={usuarioId}
              onChange={(e) => setUsuarioId(e.target.value)}
              disabled={loadingColaboradores}
              className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 transition-all disabled:opacity-60"
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
            <label className="block text-sm font-semibold text-slate-700 mb-2">
              Código de Vendedor <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              required
              value={codigoVendedor}
              onChange={(e) => setCodigoVendedor(e.target.value)}
              placeholder="Ej: VND-001, V-100..."
              className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 transition-all"
            />
          </div>

          {/* Botones */}
          <div className="mt-6 flex gap-3">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-3 border border-slate-200 text-slate-600 font-semibold rounded-xl hover:bg-slate-50 transition-all"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-3 bg-emerald-600 text-white font-semibold rounded-xl hover:bg-emerald-700 shadow-lg shadow-emerald-500/30 transition-all"
            >
              {vendedorToEdit ? "Guardar Cambios" : "Registrar Vendedor"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default VendedorModal;
