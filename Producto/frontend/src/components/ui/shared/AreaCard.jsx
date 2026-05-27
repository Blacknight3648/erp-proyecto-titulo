import React from "react";
import { Briefcase, Trash2, Edit3 } from "lucide-react";

const AreaCard = ({ area, onEdit, onDelete }) => {
  return (
    <div className="group bg-white rounded-[2.5rem] p-8 shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border border-gray-100 flex flex-col h-full relative overflow-hidden">
      {/* Decoración de fondo */}
      <div className="absolute top-0 right-0 w-32 h-32 -mr-8 -mt-8 rounded-full blur-3xl opacity-10 bg-blue-500 transition-colors duration-500"></div>

      <div className="flex justify-between items-start mb-6 relative z-10">
        <div className="w-16 h-16 rounded-3xl flex items-center justify-center shadow-lg bg-blue-50 text-blue-600 shadow-blue-100 transition-transform duration-500 group-hover:scale-110">
          <Briefcase size={32} strokeWidth={2.5} />
        </div>

        <div className="flex gap-2">
          <button
            onClick={() => onEdit(area)}
            className="p-3 bg-gray-50 text-gray-400 rounded-2xl hover:bg-black hover:text-white transition-all duration-300 shadow-sm"
            title="Editar"
          >
            <Edit3 size={16} />
          </button>
          <button
            onClick={() => onDelete(area.areaId || area.id)}
            className="p-3 bg-rose-50 text-rose-400 rounded-2xl hover:bg-rose-600 hover:text-white transition-all duration-300 shadow-sm"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      <div className="relative z-10 space-y-4 flex-1">
        <div>
          <h3 className="text-xl font-black text-gray-800 leading-tight mb-1 group-hover:text-blue-600 transition-colors">
            {area.nombre}
          </h3>
          <div className="flex items-center text-[10px] font-black text-gray-400 uppercase tracking-widest gap-2">
            <span className="text-blue-500">ID</span>
            <span>{area.areaId || area.id}</span>
          </div>
        </div>

        {area.descripcion && (
          <div className="pt-4 border-t border-gray-50">
            <p className="text-sm font-bold text-gray-500 leading-relaxed">
              {area.descripcion}
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default AreaCard;
