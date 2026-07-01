import React, { useState, useEffect } from "react";
import { X } from "lucide-react";
import { useAreas } from "../../hooks/useAreas";

const RolModal = ({ isOpen, onClose, onSave, rol }) => {
  const { areas } = useAreas();
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [areaId, setAreaId] = useState("");

  useEffect(() => {
    if (rol) {
      setNombre(rol.nombre || "");
      setDescripcion(rol.descripcion || "");
      setAreaId(rol.areaId ? String(rol.areaId) : "");
    } else {
      setNombre("");
      setDescripcion("");
      setAreaId("");
    }
  }, [rol, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({
      nombre,
      descripcion,
      areaId: areaId ? Number(areaId) : null,
      permisosIds: rol?.permisosIds || [],
    });
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-foreground/60 backdrop-blur-sm animate-in fade-in duration-300">
      <div className="bg-card rounded-3xl shadow-2xl w-full max-w-md overflow-hidden animate-in zoom-in-95 duration-300">
        <div className="flex items-center justify-between p-6 border-b border-border">
          <h2 className="text-xl font-bold text-foreground">
            {rol ? "Editar Rol" : "Registrar Nuevo Rol"}
          </h2>
          <button onClick={onClose} className="p-2 hover:bg-muted rounded-full transition-colors text-muted-foreground">
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Nombre */}
          <div>
            <label className="block text-sm font-semibold text-foreground mb-2">
              Nombre del Rol <span className="text-destructive">*</span>
            </label>
            <input
              type="text"
              required
              value={nombre}
              onChange={(e) => setNombre(e.target.value.toUpperCase())}
              placeholder="Ej: Administrador, Vendedor, Operador..."
              style={{ textTransform: 'uppercase' }}
              className="w-full px-4 py-3 bg-muted border border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-indigo/20 focus:border-brand-indigo transition-all"
            />
          </div>

          {/* Descripción */}
          <div>
            <label className="block text-sm font-semibold text-foreground mb-2">
              Descripción
            </label>
            <textarea
              value={descripcion}
              onChange={(e) => setDescripcion(e.target.value.toUpperCase())}
              placeholder="Descripción del rol y sus responsabilidades..."
              style={{ textTransform: 'uppercase' }}
              rows={3}
              className="w-full px-4 py-3 bg-muted border border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-indigo/20 focus:border-brand-indigo transition-all resize-none"
            />
          </div>

          {/* Área */}
          <div>
            <label className="block text-sm font-semibold text-foreground mb-2">
              Área
            </label>
            <select
              value={areaId}
              onChange={(e) => setAreaId(e.target.value)}
              className="w-full px-4 py-3 bg-muted border border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-indigo/20 focus:border-brand-indigo transition-all"
            >
              <option value="">— Sin área asignada —</option>
              {areas.map((area) => (
                <option key={area.areaId} value={area.areaId}>
                  {area.nombre}
                </option>
              ))}
            </select>
          </div>

          <div className="mt-6 flex gap-3">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-3 border border-border text-muted-foreground font-semibold rounded-xl hover:bg-muted transition-all"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-3 bg-brand-indigo text-white font-semibold rounded-xl hover:bg-brand-indigo shadow-lg shadow-brand-indigo/30 transition-all"
            >
              {rol ? "Guardar Cambios" : "Crear Rol"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RolModal;
