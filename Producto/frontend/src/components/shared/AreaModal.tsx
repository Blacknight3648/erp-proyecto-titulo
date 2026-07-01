import React, { useState, useEffect } from "react";
import { X, LayoutDashboard, Type } from "lucide-react";

const AreaModal = ({ isOpen, onClose, onSave, area }) => {
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");

  useEffect(() => {
    if (area) {
      setNombre(area.nombre || "");
      setDescripcion(area.descripcion || "");
    } else {
      setNombre("");
      setDescripcion("");
    }
  }, [area, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({ nombre, descripcion });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-6 bg-foreground/60 backdrop-blur-md animate-in fade-in duration-300">
      <div className="bg-card rounded-[2rem] shadow-2xl w-full max-w-[420px] overflow-hidden animate-in zoom-in-[0.98] slide-in-from-bottom-4 duration-300 flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between px-8 py-6 border-b border-border bg-card">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-accent text-accent-foreground rounded-xl flex items-center justify-center">
              <LayoutDashboard size={20} strokeWidth={2.5} />
            </div>
            <h2 className="text-xl font-bold text-foreground">
              {area ? "Editar Área" : "Registrar Nueva Área"}
            </h2>
          </div>
          <button 
            onClick={onClose} 
            className="p-2.5 hover:bg-muted rounded-xl transition-all text-muted-foreground hover:text-foreground"
          >
            <X size={20} strokeWidth={2.5} />
          </button>
        </div>

        {/* Body */}
        <form onSubmit={handleSubmit} className="p-8 space-y-6 flex-1 flex flex-col">
          <div className="space-y-6 flex-1">
            {/* Nombre */}
            <div className="space-y-2">
              <label className="text-[13px] font-bold text-muted-foreground uppercase tracking-wider">
                Nombre del Área
              </label>
              <div className="relative flex items-center group">
                <div className="absolute left-4 text-muted-foreground group-focus-within:text-accent-foreground transition-colors">
                  <Type size={18} strokeWidth={2.5} />
                </div>
                <input
                  type="text"
                  required
                  value={nombre}
                  onChange={(e) => setNombre(e.target.value.toUpperCase())}
                  placeholder="Ej: MARKETING, FINANZAS..."
                  style={{ textTransform: 'uppercase' }}
                  className="w-full pl-11 pr-4 py-3.5 bg-muted border-2 border-border hover:border-border rounded-2xl focus:outline-none focus:ring-4 focus:ring-primary/10 focus:border-primary focus:bg-card transition-all text-foreground font-bold placeholder:font-medium"
                />
              </div>
            </div>

            {/* Descripción */}
            <div className="space-y-2">
              <label className="text-[13px] font-bold text-muted-foreground uppercase tracking-wider">
                Descripción
              </label>
              <textarea
                value={descripcion}
                onChange={(e) => setDescripcion(e.target.value.toUpperCase())}
                placeholder="Descripción del área"
                style={{ textTransform: 'uppercase' }}
                className="w-full px-5 py-4 bg-muted border-2 border-border hover:border-border rounded-2xl focus:outline-none focus:ring-4 focus:ring-primary/10 focus:border-primary focus:bg-card transition-all text-foreground font-medium resize-none placeholder:font-medium"
                rows={4}
              />
            </div>
          </div>

          {/* Footer actions */}
          <div className="mt-8 flex gap-3 pt-6 border-t border-border">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-3.5 border-2 border-border hover:border-border-strong text-muted-foreground font-bold rounded-2xl hover:bg-muted transition-all active:scale-[0.98]"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-3.5 bg-primary text-white font-bold rounded-2xl hover:bg-primary shadow-xl shadow-primary/30 transition-all active:scale-[0.98]"
            >
              {area ? "Guardar Cambios" : "Crear Área"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AreaModal;