import React, { useState, useMemo } from "react";
import { Search, Plus, Edit2, Trash2, Layout, Briefcase, FileText, Loader2 } from "lucide-react";
import { Toaster, toast } from "sonner";
import { confirmDelete } from "../../utils/confirmDelete";
import { useAreas } from "../../hooks/useAreas";

const GestionAreas = () => {
  const { areas, loading, createArea, updateArea, deleteArea } = useAreas();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedArea, setSelectedArea] = useState<any>(null);
  
  const [formData, setFormData] = useState({ nombre: "", descripcion: "" });

  const filteredAreas = useMemo(() => {
    return areas.filter((area) =>
      area.nombre?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [areas, searchTerm]);

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value.toUpperCase() 
    }));
  };

  const handleSelectArea = (area: any) => {
    setSelectedArea(area);
    setFormData({
      nombre: (area.nombre || "").toUpperCase(),
      descripcion: (area.descripcion || "").toUpperCase()
    });
  };

  const handleResetForm = () => {
    setSelectedArea(null);
    setFormData({ nombre: "", descripcion: "" });
  };

  const handleDelete = (id: number) => {
    confirmDelete("¿Está seguro de eliminar esta área?", async () => {
      try {
        await deleteArea(id);
        if (selectedArea?.id === id || selectedArea?.areaId === id) {
          handleResetForm();
        }
      } catch (error) {
        console.error("Error al eliminar:", error);
      }
    });
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.nombre.trim()) {
      toast.error("El nombre del área es requerido");
      return;
    }

    const finalData = {
      nombre: formData.nombre.trim().toUpperCase(),
      descripcion: formData.descripcion.trim().toUpperCase()
    };

    try {
      if (selectedArea) {
        const areaId = selectedArea.areaId || selectedArea.id;
        await updateArea(areaId, finalData);
        toast.success("Área actualizada correctamente");
      } else {
        await createArea(finalData);
        toast.success("Área creada correctamente");
      }
      handleResetForm();
    } catch (error) {
      console.error("Error al guardar:", error);
      toast.error("Ocurrió un error al guardar");
    }
  };

  return (
    <div className="min-h-screen bg-zinc-50/50 px-4 py-8 sm:px-8 font-sans antialiased text-zinc-900 selection:bg-zinc-200">
      <Toaster position="top-right" richColors closeButton />

      <div className="max-w-7xl mx-auto space-y-6">
        
        {/* ENCABEZADO EMPRESARIAL */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 border-b border-zinc-200/60">
          <div className="flex items-center gap-3.5">
            <div className="p-2.5 bg-white border border-zinc-200 shadow-sm rounded-xl text-zinc-700">
              <Layout className="w-5 h-5 stroke-[1.75]" />
            </div>
            <div>
              <h1 className="text-xl font-semibold tracking-tight text-zinc-900">
                Gestión de Áreas
              </h1>
              <p className="text-xs text-zinc-500 font-medium mt-0.5">
                Administración de departamentos y áreas funcionales de la organización
              </p>
            </div>
          </div>
        </div>

        {/* CONTENEDOR PRINCIPAL: DOS PANELES */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
          
          {/* PANEL IZQUIERDO: FORMULARIO REGISTRO/EDICIÓN */}
          <div className="lg:col-span-4 sticky top-6 bg-white rounded-2xl border border-zinc-200/80 shadow-[0_2px_8px_-3px_rgba(0,0,0,0.05)] overflow-hidden">
            <div className="flex flex-row justify-between items-center bg-zinc-50/50 border-b border-zinc-100 py-4 px-6">
              <h2 className="text-[11px] font-bold text-zinc-400 uppercase tracking-widest">
                {selectedArea ? "Editar Área" : "Registrar Nueva Área"}
              </h2>
              {selectedArea && (
                <button
                  type="button"
                  onClick={handleResetForm}
                  className="text-[10px] text-zinc-500 hover:text-zinc-900 font-bold uppercase tracking-wider transition-colors"
                >
                  Cancelar edición
                </button>
              )}
            </div>
            
            <form onSubmit={handleSave} className="p-6 space-y-5">
              <div>
                <label className="block text-[11px] font-bold text-zinc-500 uppercase mb-2 tracking-wider">
                  Nombre del Área <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  placeholder="EJ: MARKETING, FINANZAS..."
                  value={formData.nombre}
                  onChange={(e) => handleInputChange("nombre", e.target.value)}
                  className="w-full bg-zinc-50/50 border border-zinc-200 rounded-xl px-3 py-2 text-xs font-semibold uppercase placeholder:text-zinc-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all"
                  required
                />
              </div>

              <div>
                <label className="block text-[11px] font-bold text-zinc-500 uppercase mb-2 tracking-wider">
                  Descripción
                </label>
                <textarea
                  rows={4}
                  placeholder="ESCRIBA LOS DETALLES O PROPÓSITOS DEL ÁREA..."
                  value={formData.descripcion}
                  onChange={(e) => handleInputChange("descripcion", e.target.value)}
                  className="w-full bg-zinc-50/50 border border-zinc-200 rounded-xl px-3 py-2 text-xs font-medium uppercase placeholder:text-zinc-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all resize-none"
                />
              </div>

              <div className="pt-2 flex gap-3">
                {!selectedArea && (
                  <button
                    type="button"
                    onClick={() => setFormData({ nombre: "", descripcion: "" })}
                    className="w-1/3 py-2.5 rounded-xl font-bold uppercase tracking-wide text-[10px] text-zinc-500 bg-zinc-100 hover:bg-zinc-200 transition-colors"
                  >
                    Limpiar
                  </button>
                )}
                <button
                  type="submit"
                  className={`flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl font-bold uppercase tracking-wide text-[10px] text-white shadow-sm transition-all active:scale-[0.98] ${
                    selectedArea
                      ? "bg-amber-500 hover:bg-amber-600 shadow-amber-500/20"
                      : "bg-zinc-900 hover:bg-zinc-800"
                  }`}
                >
                  {selectedArea ? <Edit2 size={14} /> : <Plus size={14} />}
                  {selectedArea ? "Guardar Cambios" : "Crear Área"}
                </button>
              </div>
            </form>
          </div>

          {/* PANEL DERECHO: BUSCADOR Y LISTA NAVEGABLE */}
          <div className="lg:col-span-8 space-y-4">
            
            {/* Buscador */}
            <div className="bg-white rounded-2xl border border-zinc-200/80 shadow-[0_2px_8px_-3px_rgba(0,0,0,0.05)] flex items-center px-4 focus-within:ring-2 focus-within:ring-zinc-900/5 focus-within:border-zinc-500 transition-all overflow-hidden h-12">
              <Search size={16} className="text-zinc-400 mr-3 shrink-0 stroke-[2]" />
              <input
                type="text"
                placeholder="Filtro rápido por nombre de área..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-transparent border-none outline-none py-2 text-xs font-medium text-zinc-900 placeholder:text-zinc-400 uppercase"
              />
              {searchTerm && (
                <button 
                  onClick={() => setSearchTerm("")}
                  className="text-[10px] text-zinc-400 hover:text-zinc-900 font-bold px-2 uppercase tracking-wider"
                >
                  Limpiar
                </button>
              )}
            </div>

            {/* Contenedor del listado */}
            <div className="bg-white rounded-2xl border border-zinc-200/80 shadow-[0_2px_8px_-3px_rgba(0,0,0,0.05)] overflow-hidden flex flex-col">
              <div className="px-6 py-4 bg-zinc-50/50 border-b border-zinc-100 flex flex-row items-center justify-between">
                <div className="flex items-center gap-2">
                  <Briefcase size={14} className="text-zinc-400 stroke-[2]" />
                  <h2 className="text-[11px] font-bold text-zinc-400 uppercase tracking-widest">
                    Listado de Áreas Activas
                  </h2>
                </div>
                <span className="bg-zinc-200/50 text-zinc-500 text-[10px] font-bold px-2.5 py-0.5 rounded-full">
                  {filteredAreas.length}
                </span>
              </div>

              <div className="divide-y divide-zinc-100 max-h-[500px] overflow-y-auto custom-scrollbar">
                {loading ? (
                  <div className="p-16 flex flex-col items-center justify-center gap-3 text-zinc-400">
                    <Loader2 className="w-6 h-6 animate-spin text-zinc-300" />
                    <p className="text-[11px] font-bold uppercase tracking-widest">Cargando registros empresariales...</p>
                  </div>
                ) : filteredAreas.length === 0 ? (
                  <div className="p-16 flex flex-col items-center gap-2 text-center">
                    <FileText size={28} className="text-zinc-300 stroke-[1.5] mb-2" />
                    <p className="font-bold text-[11px] text-zinc-400 uppercase tracking-widest">No se encontraron resultados</p>
                    <p className="text-[10px] text-zinc-400/80 uppercase font-medium">Prueba con otro término o añade una nueva área.</p>
                  </div>
                ) : (
                  filteredAreas.map((area: any) => {
                    const id = area.areaId || area.id;
                    const isSelected = selectedArea?.id === id || selectedArea?.areaId === id;
                    
                    return (
                      <div 
                        key={id} 
                        className={`group p-5 flex items-start justify-between gap-4 transition-colors hover:bg-zinc-50/80 cursor-pointer ${
                          isSelected ? "bg-zinc-50 border-l-4 border-l-zinc-900 pl-4" : "border-l-4 border-l-transparent"
                        }`}
                        onClick={() => handleSelectArea(area)}
                      >
                        <div className="space-y-1 min-w-0 flex-1">
                          <div className="flex items-center gap-2.5">
                            <span className="text-[9px] font-mono font-bold text-zinc-500 bg-zinc-100 border border-zinc-200/60 px-1.5 py-0.5 rounded tracking-widest uppercase">
                              ID: {id}
                            </span>
                            <h3 className="font-bold text-zinc-900 text-sm group-hover:text-black transition-colors truncate uppercase">
                              {area.nombre}
                            </h3>
                          </div>
                          <p className="text-xs text-zinc-500 line-clamp-2 pr-4 font-medium uppercase mt-1">
                            {area.descripcion || (
                              <span className="text-[10px] text-zinc-400 font-medium">SIN DESCRIPCIÓN ASIGNADA</span>
                            )}
                          </p>
                        </div>

                        {/* Botones de acción */}
                        <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity shrink-0" onClick={(e) => e.stopPropagation()}>
                          <button
                            onClick={() => handleSelectArea(area)}
                            className="p-2 text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 rounded-lg transition-all"
                            title="Editar"
                          >
                            <Edit2 size={16} strokeWidth={2} />
                          </button>
                          <button
                            onClick={() => handleDelete(id)}
                            className="p-2 text-zinc-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all"
                            title="Eliminar"
                          >
                            <Trash2 size={16} strokeWidth={2} />
                          </button>
                        </div>
                      </div>
                    );
                  })
                )}
              </div>
            </div>

          </div>
        </div>

      </div>
    </div>
  );
};

export default GestionAreas;