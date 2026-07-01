import React from "react";
import { UserCheck, Trash2, Edit3 } from "lucide-react";

const VendedorCard = ({ vendedor, onEdit, onDelete }) => {
  const fullName = [vendedor.nombreUsuario, vendedor.apellidosUsuario].filter(Boolean).join(" ");
  const isActive = vendedor.activo !== false;

  return (
    <div className="group bg-card rounded-[2.5rem] p-8 shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border border-border flex flex-col h-full relative overflow-hidden">
      {/* Decoración de fondo */}
      <div className={`absolute top-0 right-0 w-32 h-32 -mr-8 -mt-8 rounded-full blur-3xl opacity-10 transition-colors duration-500 ${isActive ? "bg-success" : "bg-destructive"}`}></div>

      <div className="flex justify-between items-start mb-6 relative z-10">
        <div className={`w-16 h-16 rounded-3xl flex items-center justify-center shadow-lg transition-transform duration-500 group-hover:scale-110 ${isActive ? "bg-success-bg text-success shadow-success-bg" : "bg-muted text-muted-foreground"}`}>
          <UserCheck size={32} strokeWidth={2.5} />
        </div>

        <div className="flex gap-2">
          <button
            onClick={() => onEdit(vendedor)}
            className="p-3 bg-muted text-muted-foreground rounded-2xl hover:bg-foreground hover:text-white transition-all duration-300 shadow-sm"
            title="Editar"
          >
            <Edit3 size={16} />
          </button>
          <button
            onClick={() => onDelete(vendedor.id)}
            className="p-3 bg-destructive/10 text-destructive rounded-2xl hover:bg-destructive hover:text-white transition-all duration-300 shadow-sm"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      <div className="relative z-10 space-y-4 flex-1">
        <div>
          <h3 className="text-xl font-black text-foreground leading-tight mb-1 group-hover:text-success transition-colors">
            {fullName || <span className="text-muted-foreground italic font-normal">Sin nombre</span>}
          </h3>
          <div className="flex items-center text-[10px] font-black text-muted-foreground uppercase tracking-widest gap-2">
            <span className="text-success">ID</span>
            <span>{vendedor.usuarioId}</span>
          </div>
        </div>

        <div className="pt-4 border-t border-border">
          <p className="text-[10px] font-black text-muted-foreground uppercase tracking-widest mb-1">
            Código Vendedor
          </p>
          <p className="text-lg font-black text-foreground">
            {vendedor.codigoVendedor || "—"}
          </p>
        </div>
      </div>

      <div className="mt-8 flex items-center justify-between relative z-10">
        <div className={`px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest flex items-center gap-2 ${isActive ? "bg-success-bg text-success" : "bg-destructive/10 text-destructive"}`}>
          <div className={`w-1.5 h-1.5 rounded-full animate-pulse ${isActive ? "bg-success" : "bg-destructive"}`}></div>
          {isActive ? "Activo" : "Inactivo"}
        </div>

        <div className="w-8 h-8 rounded-full border-2 border-white bg-muted flex items-center justify-center text-[10px] font-black text-muted-foreground">
          {vendedor.nombreUsuario?.charAt(0)}
        </div>
      </div>
    </div>
  );
};

export default VendedorCard;
