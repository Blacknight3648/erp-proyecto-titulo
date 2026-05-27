import React from "react";
import { UserCheck, Trash2, Edit3 } from "lucide-react";

const VendedorCard = ({ vendedor, onEdit, onDelete }) => {
  const fullName = [vendedor.nombreUsuario, vendedor.apellidosUsuario].filter(Boolean).join(" ");
  const isActive = vendedor.activo !== false;

  return (
    <div className="group bg-white rounded-[2.5rem] p-8 shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border border-gray-100 flex flex-col h-full relative overflow-hidden">
      {/* Decoración de fondo */}
      <div className={`absolute top-0 right-0 w-32 h-32 -mr-8 -mt-8 rounded-full blur-3xl opacity-10 transition-colors duration-500 ${isActive ? "bg-emerald-500" : "bg-rose-500"}`}></div>

      <div className="flex justify-between items-start mb-6 relative z-10">
        <div className={`w-16 h-16 rounded-3xl flex items-center justify-center shadow-lg transition-transform duration-500 group-hover:scale-110 ${isActive ? "bg-emerald-50 text-emerald-600 shadow-emerald-100" : "bg-gray-50 text-gray-400 shadow-gray-100"}`}>
          <UserCheck size={32} strokeWidth={2.5} />
        </div>

        <div className="flex gap-2">
          <button
            onClick={() => onEdit(vendedor)}
            className="p-3 bg-gray-50 text-gray-400 rounded-2xl hover:bg-black hover:text-white transition-all duration-300 shadow-sm"
            title="Editar"
          >
            <Edit3 size={16} />
          </button>
          <button
            onClick={() => onDelete(vendedor.id)}
            className="p-3 bg-rose-50 text-rose-400 rounded-2xl hover:bg-rose-600 hover:text-white transition-all duration-300 shadow-sm"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      <div className="relative z-10 space-y-4 flex-1">
        <div>
          <h3 className="text-xl font-black text-gray-800 leading-tight mb-1 group-hover:text-emerald-600 transition-colors">
            {fullName || <span className="text-gray-400 italic font-normal">Sin nombre</span>}
          </h3>
          <div className="flex items-center text-[10px] font-black text-gray-400 uppercase tracking-widest gap-2">
            <span className="text-emerald-500">ID</span>
            <span>{vendedor.usuarioId}</span>
          </div>
        </div>

        <div className="pt-4 border-t border-gray-50">
          <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">
            Código Vendedor
          </p>
          <p className="text-lg font-black text-gray-800">
            {vendedor.codigoVendedor || "—"}
          </p>
        </div>
      </div>

      <div className="mt-8 flex items-center justify-between relative z-10">
        <div className={`px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest flex items-center gap-2 ${isActive ? "bg-emerald-50 text-emerald-600" : "bg-rose-50 text-rose-600"}`}>
          <div className={`w-1.5 h-1.5 rounded-full animate-pulse ${isActive ? "bg-emerald-600" : "bg-rose-600"}`}></div>
          {isActive ? "Activo" : "Inactivo"}
        </div>

        <div className="w-8 h-8 rounded-full border-2 border-white bg-gray-100 flex items-center justify-center text-[10px] font-black text-gray-400">
          {vendedor.nombreUsuario?.charAt(0)}
        </div>
      </div>
    </div>
  );
};

export default VendedorCard;
