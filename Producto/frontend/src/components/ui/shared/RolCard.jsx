import React from "react";
import { Shield, Trash2, Edit, Key } from "lucide-react";

const RolCard = ({ rol, onEdit, onDelete, onManagePermissions }) => {
  return (
    <div className="group relative bg-white/80 backdrop-blur-md rounded-2xl border border-slate-200 overflow-hidden hover:shadow-2xl hover:shadow-indigo-500/10 transition-all duration-500 hover:-translate-y-1">
      <div className="p-6">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-4">
            <div className="p-3 bg-indigo-50 rounded-xl text-indigo-600 group-hover:bg-indigo-600 group-hover:text-white transition-colors duration-500">
              <Shield size={24} />
            </div>
            <div>
              <h3 className="text-lg font-bold text-slate-800">{rol.nombre}</h3>
              <p className="text-sm text-slate-500 mt-1">Rol de acceso al sistema</p>
            </div>
          </div>
        </div>

        <div className="mt-6 flex items-center justify-between gap-2 border-t border-slate-100 pt-4">
          <button
            onClick={() => onManagePermissions(rol)}
            className="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors"
          >
            <Key size={16} />
            Permisos
          </button>
          
          <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
            <button
              onClick={() => onEdit(rol)}
              className="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
            >
              <Edit size={16} />
              Editar
            </button>
            <button
              onClick={() => onDelete(rol.id)}
              className="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-red-600 hover:bg-red-50 rounded-lg transition-colors"
            >
              <Trash2 size={16} />
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RolCard;
