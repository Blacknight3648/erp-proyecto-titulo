import React from "react";
import { Shield, Trash2, Edit3, Key } from "lucide-react";

const RolCard = ({ rol, onEdit, onDelete, onManagePermissions }) => {
  return (
    <div className="group bg-card rounded-[2.5rem] p-8 shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border border-border flex flex-col h-full relative overflow-hidden">
      {/* Decoración de fondo */}
      <div className="absolute top-0 right-0 w-32 h-32 -mr-8 -mt-8 rounded-full blur-3xl opacity-10 bg-brand-indigo transition-colors duration-500"></div>

      <div className="flex justify-between items-start mb-6 relative z-10">
        <div className="w-16 h-16 rounded-3xl flex items-center justify-center shadow-lg bg-brand-indigo/10 text-brand-indigo shadow-brand-indigo/10 transition-transform duration-500 group-hover:scale-110">
          <Shield size={32} strokeWidth={2.5} />
        </div>

        <div className="flex gap-2">
          <button
            onClick={() => onEdit(rol)}
            className="p-3 bg-muted text-muted-foreground rounded-2xl hover:bg-foreground hover:text-white transition-all duration-300 shadow-sm"
            title="Editar"
          >
            <Edit3 size={16} />
          </button>
          <button
            onClick={() => onDelete(rol.id)}
            className="p-3 bg-destructive/10 text-destructive rounded-2xl hover:bg-destructive hover:text-white transition-all duration-300 shadow-sm"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      <div className="relative z-10 space-y-4 flex-1">
        <div>
          <h3 className="text-xl font-black text-foreground leading-tight mb-1 group-hover:text-brand-indigo transition-colors">
            {rol.nombre}
          </h3>
          <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">
            Rol de acceso al sistema
          </p>
        </div>
      </div>

      <div className="mt-8 relative z-10">
        <button
          onClick={() => onManagePermissions(rol)}
          className="flex items-center gap-2 px-4 py-2 bg-brand-indigo/10 text-brand-indigo rounded-2xl text-sm font-bold hover:bg-brand-indigo hover:text-white transition-all duration-300"
        >
          <Key size={16} />
          Permisos
        </button>
      </div>
    </div>
  );
};

export default RolCard;
