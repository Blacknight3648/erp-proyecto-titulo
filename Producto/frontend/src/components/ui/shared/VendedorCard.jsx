import React from "react";
import { UserCheck, Trash2, Edit, BadgeCheck } from "lucide-react";

const VendedorCard = ({ vendedor, onEdit, onDelete }) => {
  const fullName = [vendedor.nombreUsuario, vendedor.apellidosUsuario].filter(Boolean).join(" ");

  return (
    <div className="group relative bg-white/80 backdrop-blur-md rounded-2xl border border-slate-200 overflow-hidden hover:shadow-2xl hover:shadow-emerald-500/10 transition-all duration-500 hover:-translate-y-1">
      {/* Color accent bar */}
      <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-emerald-400 to-teal-500" />

      <div className="p-6">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-4">
            <div className="p-3 bg-emerald-50 rounded-xl text-emerald-600 group-hover:bg-emerald-600 group-hover:text-white transition-colors duration-500">
              <UserCheck size={24} />
            </div>
            <div>
              <h3 className="text-lg font-bold text-slate-800">
                {fullName || <span className="text-slate-400 italic">Sin nombre</span>}
              </h3>
              <p className="text-sm text-slate-500 mt-0.5">
                ID Usuario: <span className="font-medium">{vendedor.usuarioId}</span>
              </p>
            </div>
          </div>

          {/* Estado activo */}
          <span
            className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-semibold ${
              vendedor.activo !== false
                ? "bg-emerald-100 text-emerald-700"
                : "bg-slate-100 text-slate-500"
            }`}
          >
            <BadgeCheck size={12} />
            {vendedor.activo !== false ? "Activo" : "Inactivo"}
          </span>
        </div>

        {/* Código vendedor */}
        <div className="mt-4 px-3 py-2 bg-slate-50 rounded-xl">
          <p className="text-xs text-slate-500 uppercase tracking-wide font-semibold">Código Vendedor</p>
          <p className="text-base font-bold text-slate-800 mt-0.5">{vendedor.codigoVendedor || "—"}</p>
        </div>

        {/* Actions */}
        <div className="mt-4 flex items-center justify-end gap-2 border-t border-slate-100 pt-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
          <button
            onClick={() => onEdit(vendedor)}
            className="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
          >
            <Edit size={16} />
            Editar
          </button>
          <button
            onClick={() => onDelete(vendedor.id)}
            className="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-red-600 hover:bg-red-50 rounded-lg transition-colors"
          >
            <Trash2 size={16} />
            Eliminar
          </button>
        </div>
      </div>
    </div>
  );
};

export default VendedorCard;
