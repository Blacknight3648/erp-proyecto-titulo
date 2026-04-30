import React from "react";
import {
  User,
  Mail,
  Tag,
  Phone,
  ToggleLeft,
  ToggleRight,
  Trash2,
  Edit3
} from "lucide-react";

import { formatRUN } from "../../utils/validations";

export default function ClienteCard({ cliente, onDelete, onToggle, onEdit }) {
  const isActive = cliente.activo;

  const run = cliente.runCliente
    ? formatRUN(cliente.runCliente)
    : "Sin RUN";

  const initial = cliente.nombreCliente?.charAt(0)?.toUpperCase() || "?";

  return (
    <div className="group bg-white rounded-[2.5rem] p-8 shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border border-gray-100 flex flex-col h-full relative overflow-hidden">

      {/* Glow decorativo */}
      <div
        className={`absolute top-0 right-0 w-32 h-32 -mr-8 -mt-8 rounded-full blur-3xl opacity-10 transition-colors duration-500 ${
          isActive ? "bg-indigo-500" : "bg-rose-500"
        }`}
      ></div>

      {/* Header */}
      <div className="flex justify-between items-start mb-6 relative z-10">
        
        {/* Avatar */}
        <div
          className={`w-16 h-16 rounded-3xl flex items-center justify-center shadow-lg transition-transform duration-500 group-hover:scale-110 ${
            isActive
              ? "bg-indigo-50 text-indigo-600 shadow-indigo-100"
              : "bg-gray-50 text-gray-400 shadow-gray-100"
          }`}
        >
          <User size={30} strokeWidth={2.5} />
        </div>

        {/* Acciones */}
        <div className="flex gap-2">

          {onEdit && (
            <button
              onClick={() => onEdit(cliente)}
              className="p-3 bg-gray-50 text-gray-400 rounded-2xl hover:bg-black hover:text-white transition-all duration-300 shadow-sm"
              title="Editar cliente"
            >
              <Edit3 size={16} />
            </button>
          )}

          <button
            onClick={() => onToggle(cliente)}
            className={`p-3 rounded-2xl transition-all duration-300 shadow-sm ${
              isActive
                ? "bg-indigo-50 text-indigo-600 hover:bg-indigo-600 hover:text-white"
                : "bg-rose-50 text-rose-600 hover:bg-rose-600 hover:text-white"
            }`}
            title={isActive ? "Suspender cliente" : "Activar cliente"}
          >
            {isActive ? <ToggleRight size={16} /> : <ToggleLeft size={16} />}
          </button>

          <button
            onClick={() => onDelete(cliente.clienteId)}
            className="p-3 bg-rose-50 text-rose-400 rounded-2xl hover:bg-rose-600 hover:text-white transition-all duration-300 shadow-sm"
            title="Eliminar cliente"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      {/* Info principal */}
      <div className="relative z-10 space-y-4 flex-1">

        <div>
          <h3 className="text-xl font-black text-gray-800 leading-tight mb-1 group-hover:text-indigo-600 transition-colors">
            {cliente.nombreCliente || "Cliente sin nombre"}
          </h3>

          <div className="flex items-center text-[10px] font-black text-gray-400 uppercase tracking-widest gap-2">
            <span className="text-indigo-500">RUN</span>
            <span>{run}</span>
          </div>
        </div>

        {/* Datos */}
        <div className="pt-4 border-t border-gray-50 space-y-3">

          {/* Segmento */}
          <div className="flex items-center text-sm font-bold text-gray-500 gap-3">
            <Tag size={16} className="text-gray-300" />
            <span className="px-3 py-1 bg-gray-100 rounded-lg text-[10px] uppercase tracking-widest text-gray-600">
              {cliente.segmento || "Sin segmento"}
            </span>
          </div>

          {/* Email */}
          <div className="flex items-center text-sm font-bold text-gray-500 gap-3">
            <Mail size={16} className="text-gray-300" />
            <span className="truncate">
              {cliente.email || "Sin correo"}
            </span>
          </div>

          {/* Teléfono */}
          <div className="flex items-center text-sm font-bold text-gray-500 gap-3">
            <Phone size={16} className="text-gray-300" />
            <span>
              {cliente.telefono || "Sin teléfono"}
            </span>
          </div>

        </div>
      </div>

      {/* Footer */}
      <div className="mt-8 flex items-center justify-between relative z-10">

        {/* Estado */}
        <div
          className={`px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest flex items-center gap-2 ${
            isActive
              ? "bg-indigo-50 text-indigo-600"
              : "bg-rose-50 text-rose-600"
          }`}
        >
          <div
            className={`w-1.5 h-1.5 rounded-full animate-pulse ${
              isActive ? "bg-indigo-600" : "bg-rose-600"
            }`}
          ></div>

          {isActive ? "Activo" : "Suspendido"}
        </div>

        {/* Avatar inicial */}
        <div className="flex -space-x-2">
          <div className="w-8 h-8 rounded-full border-2 border-white bg-indigo-100 flex items-center justify-center text-[10px] font-black text-indigo-600">
            {initial}
          </div>
        </div>
      </div>
    </div>
  );
}