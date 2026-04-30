import React from "react";
import { Briefcase, Trash2, Edit } from "lucide-react";

const AreaCard = ({ area, onEdit, onDelete }) => {
  return (
    <div className="group relative bg-white/80 backdrop-blur-md rounded-2xl border border-slate-200 overflow-hidden hover:shadow-2xl hover:shadow-blue-500/10 transition-all duration-500 hover:-translate-y-1">
      <div className="p-6">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-4">
            <div className="p-3 bg-blue-50 rounded-xl text-blue-600 group-hover:bg-blue-600 group-hover:text-white transition-colors duration-500">
              <Briefcase size={24} />
            </div>
            <div>
              <h3 className="text-lg font-bold text-slate-800">{area.nombre}</h3>
              <p className="text-sm text-slate-500 mt-1">{area.descripcion}</p> {/* <-- mostramos la descripción */}
              <p className="text-xs text-slate-400 mt-1">ID: {area.areaId}</p>
            </div>
          </div>
        </div>

        <div className="mt-6 flex items-center justify-end gap-2 border-t border-slate-100 pt-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
          <button
            onClick={() => onEdit(area)}
            className="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
          >
            <Edit size={16} />
            Editar
          </button>
          <button
            onClick={() => onDelete(area.areaId)}
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

export default AreaCard;