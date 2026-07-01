import {
  User,
  Mail,
  Tag,
  Phone,
  Briefcase,
  ToggleLeft,
  ToggleRight,
  Trash2,
  Edit3,
} from "lucide-react";

import { formatRUN } from "../../utils/validations";

export default function ClienteCard({ cliente, onDelete, onToggle, onEdit }) {
  const isActive = cliente.activo;

  const run = cliente.runCliente ? formatRUN(cliente.runCliente) : "Sin RUN";

  const initial = cliente.razonSocial?.charAt(0)?.toUpperCase() || "?";

  const siglaLabel =
    cliente.sigla?.siglaAbreviatura ||
    cliente.sigla?.descripcionSigla ||
    "Sin sigla";

  const giroLabel = cliente.giro?.descripcionGiro || "Sin giro";

  return (
    <div className="group bg-card rounded-[2.5rem] p-8 shadow-sm hover:shadow-2xl hover:-translate-y-2 transition-all duration-500 border border-border flex flex-col h-full relative overflow-hidden">
      <div
        className={`absolute top-0 right-0 w-32 h-32 -mr-8 -mt-8 rounded-full blur-3xl opacity-10 transition-colors duration-500 ${
          isActive ? "bg-brand-indigo" : "bg-destructive"
        }`}
      ></div>

      <div className="flex justify-between items-start mb-6 relative z-10">
        <div
          className={`w-16 h-16 rounded-3xl flex items-center justify-center shadow-lg transition-transform duration-500 group-hover:scale-110 ${
            isActive
              ? "bg-brand-indigo/10 text-brand-indigo shadow-brand-indigo/10"
              : "bg-muted text-muted-foreground"
          }`}
        >
          <User size={30} strokeWidth={2.5} />
        </div>

        <div className="flex gap-2">
          {onEdit && (
            <button
              onClick={() => onEdit(cliente)}
              className="p-3 bg-muted text-muted-foreground rounded-2xl hover:bg-foreground hover:text-white transition-all duration-300 shadow-sm"
              title="Editar cliente"
            >
              <Edit3 size={16} />
            </button>
          )}

          <button
            onClick={() => onToggle(cliente)}
            className={`p-3 rounded-2xl transition-all duration-300 shadow-sm ${
              isActive
                ? "bg-brand-indigo/10 text-brand-indigo hover:bg-brand-indigo hover:text-white"
                : "bg-destructive/10 text-destructive hover:bg-destructive hover:text-white"
            }`}
            title={isActive ? "Suspender cliente" : "Activar cliente"}
          >
            {isActive ? <ToggleRight size={16} /> : <ToggleLeft size={16} />}
          </button>

          <button
            onClick={() => onDelete(cliente.clienteId)}
            className="p-3 bg-destructive/10 text-destructive rounded-2xl hover:bg-destructive hover:text-white transition-all duration-300 shadow-sm"
            title="Eliminar cliente"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      <div className="relative z-10 space-y-4 flex-1">
        <div>
          <h3 className="text-xl font-black text-foreground leading-tight mb-1 group-hover:text-brand-indigo transition-colors">
            {cliente.razonSocial || "Cliente sin razón social"}
          </h3>

          <div className="flex items-center text-[10px] font-black text-muted-foreground uppercase tracking-widest gap-2">
            <span className="text-brand-indigo">RUN</span>
            <span>{run}</span>
          </div>
        </div>

        <div className="pt-4 border-t border-border space-y-3">
          <div className="flex items-center text-sm font-bold text-muted-foreground gap-3">
            <Tag size={16} className="text-muted-foreground" />
            <span className="px-3 py-1 bg-muted rounded-lg text-[10px] uppercase tracking-widest text-muted-foreground">
              {siglaLabel}
            </span>
          </div>

          <div className="flex items-center text-sm font-bold text-muted-foreground gap-3">
            <Briefcase size={16} className="text-muted-foreground" />
            <span className="truncate">{giroLabel}</span>
          </div>

          <div className="flex items-center text-sm font-bold text-muted-foreground gap-3">
            <Mail size={16} className="text-muted-foreground" />
            <span className="truncate">
              {cliente.correoCliente || "Sin correo"}
            </span>
          </div>

          <div className="flex items-center text-sm font-bold text-muted-foreground gap-3">
            <Phone size={16} className="text-muted-foreground" />
            <span>{cliente.telefonoCliente || "Sin teléfono"}</span>
          </div>
        </div>
      </div>

      <div className="mt-8 flex items-center justify-between relative z-10">
        <div
          className={`px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest flex items-center gap-2 ${
            isActive
              ? "bg-brand-indigo/10 text-brand-indigo"
              : "bg-destructive/10 text-destructive"
          }`}
        >
          <div
            className={`w-1.5 h-1.5 rounded-full animate-pulse ${
              isActive ? "bg-brand-indigo" : "bg-destructive"
            }`}
          ></div>
          {isActive ? "Activo" : "Suspendido"}
        </div>

        <div className="flex -space-x-2">
          <div className="w-8 h-8 rounded-full border-2 border-white bg-brand-indigo/10 flex items-center justify-center text-[10px] font-black text-brand-indigo">
            {initial}
          </div>
        </div>
      </div>
    </div>
  );
}
