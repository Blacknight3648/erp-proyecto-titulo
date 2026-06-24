import React, { useState, useMemo } from "react";
import { Search, Plus, Edit2, Trash2, Layout, Briefcase, FileText } from "lucide-react";
import { Toaster, toast } from "sonner";
import { confirmDelete } from "../../../utils/confirmDelete.jsx";
import { useAreas } from "../../../hooks/useAreas";

const GestionAreas = () => {
  const { areas, loading, createArea, updateArea, deleteArea } = useAreas();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedArea, setSelectedArea] = useState(null);
  
  // Estado para el formulario (siempre visible en el panel lateral)
  const [formData, setFormData] = useState({ nombre: "", descripcion: "" });

  // Filtrado optimizado para el buscador
  const filteredAreas = useMemo(() => {
    return areas.filter((area) =>
      area.nombre?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [areas, searchTerm]);

  // Manejar el cambio en los inputs asegurando el formateo a UPPERCASE
  const handleInputChange = (field, value) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value.toUpperCase() // Forzar almacenamiento en Mayúsculas
    }));
  };

  // Manejar la selección para edición
  const handleSelectArea = (area) => {
    setSelectedArea(area);
    setFormData({
      nombre: (area.nombre || "").toUpperCase(),
      descripcion: (area.descripcion || "").toUpperCase()
    });
  };

  // Limpiar el formulario para crear uno nuevo
  const handleResetForm = () => {
    setSelectedArea(null);
    setFormData({ nombre: "", descripcion: "" });
  };

  const handleDelete = (id) => {
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

  const handleSave = async (e) => {
    e.preventDefault();
    if (!formData.nombre.trim()) {
      toast.error("El nombre del área es requerido");
      return;
    }

    // Asegurar envío en Mayúsculas limpiando espacios extras
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
    <div className="min-h-[calc(100vh-72px)] bg-[#f4f6f9] p-6 lg:p-8 font-sans antialiased text-slate-800">
      <Toaster position="top-right" richColors closeButton />

      <div className="max-w-7xl mx-auto space-y-6">
        
        {/* ENCABEZADO EMPRESARIAL */}
        <div className="flex flex-col md:flex-row md:items-center justify-between border-b border-slate-200 pb-5 gap-4">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-slate-900 rounded-xl flex items-center justify-center text-white shadow-sm">
              <Layout size={24} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-slate-900 tracking-tight">
                Gestión de Áreas
              </h1>
              <p className="text-xs text-slate-500 font-medium uppercase tracking-wider mt-0.5">
                Administración de departamentos y áreas funcionales de la organización
              </p>
            </div>
          </div>
        </div>

        {/* CONTENEDOR PRINCIPAL: DOS PANELES */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
          
          {/* PANEL IZQUIERDO: FORMULARIO REGISTRO/EDICIÓN */}
          <div className="lg:col-span-4 bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden sticky top-6">
            <div className="px-6 py-4 bg-slate-50 border-b border-slate-200 flex justify-between items-center">
              <h2 className="text-sm font-bold text-slate-700 uppercase tracking-wider">
                {selectedArea ? "Editar Área" : "Registrar Nueva Área"}
              </h2>
              {selectedArea && (
                <button 
                  onClick={handleResetForm}
                  className="text-xs text-blue-600 hover:underline font-medium uppercase"
                >
                  Cancelar edición
                </button>
              )}
            </div>
            
            <form onSubmit={handleSave} className="p-6 space-y-5">
              <div>
                <label className="block text-xs font-bold text-slate-600 uppercase mb-2 tracking-wide">
                  Nombre del Área <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  placeholder="EJ: MARKETING, FINANZAS, SISTEMAS..."
                  value={formData.nombre}
                  onChange={(e) => handleInputChange("nombre", e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-lg px-4 py-2.5 text-sm text-slate-800 placeholder-slate-400 focus:bg-white focus:ring-2 focus:ring-slate-900/10 focus:border-slate-900 transition-all outline-none uppercase"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-600 uppercase mb-2 tracking-wide">
                  Descripción
                </label>
                <textarea
                  rows="4"
                  placeholder="ESCRIBA LOS DETALLES O PROPÓSITOS DEL ÁREA..."
                  value={formData.descripcion}
                  onChange={(e) => handleInputChange("descripcion", e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-lg px-4 py-2.5 text-sm text-slate-800 placeholder-slate-400 focus:bg-white focus:ring-2 focus:ring-slate-900/10 focus:border-slate-900 transition-all outline-none resize-none uppercase"
                />
              </div>

              <div className="pt-2 flex gap-3">
                {!selectedArea && (
                  <button
                    type="button"
                    onClick={() => setFormData({ nombre: "", descripcion: "" })}
                    className="w-1/3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-sm py-2.5 rounded-lg transition-colors uppercase"
                  >
                    Limpiar
                  </button>
                )}
                <button
                  type="submit"
                  className={`flex-1 font-semibold text-sm py-2.5 rounded-lg text-white transition-all flex items-center justify-center gap-2 shadow-sm uppercase ${
                    selectedArea 
                      ? "bg-amber-600 hover:bg-amber-700" 
                      : "bg-blue-600 hover:bg-blue-700"
                  }`}
                >
                  {selectedArea ? <Edit2 size={16} /> : <Plus size={16} />}
                  {selectedArea ? "Guardar Cambios" : "Crear Área"}
                </button>
              </div>
            </form>
          </div>

          {/* PANEL DERECHO: BUSCADOR Y LISTA NAVEGABLE */}
          <div className="lg:col-span-8 space-y-4">
            
            {/* Buscador Limpio de una Sola Línea */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm flex items-center px-4 py-1 focus-within:ring-2 focus-within:ring-slate-900/10 focus-within:border-slate-400 transition-all">
              <Search size={18} className="text-slate-400 mr-3 shrink-0" />
              <input
                type="text"
                placeholder="Filtro rápido por nombre de área..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-transparent border-none outline-none py-3 text-sm text-slate-700 placeholder-slate-400 uppercase"
              />
              {searchTerm && (
                <button 
                  onClick={() => setSearchTerm("")}
                  className="text-xs text-slate-400 hover:text-slate-600 font-medium px-2 uppercase"
                >
                  Limpiar
                </button>
              )}
            </div>

            {/* Contenedor del listado */}
            <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
              <div className="px-6 py-4 bg-slate-50 border-b border-slate-200 flex items-center gap-2">
                <Briefcase size={16} className="text-slate-500" />
                <h2 className="text-sm font-bold text-slate-700 uppercase tracking-wider">
                  Listado de Áreas Activas
                </h2>
                <span className="ml-auto bg-slate-200 text-slate-700 text-xs font-bold px-2.5 py-0.5 rounded-full">
                  {filteredAreas.length}
                </span>
              </div>

              <div className="divide-y divide-slate-100 max-h-[500px] overflow-y-auto">
                {loading ? (
                  <div className="p-12 text-center text-slate-400">
                    <div className="flex flex-col items-center gap-3">
                      <div className="w-6 h-6 border-2 border-slate-900 border-t-transparent rounded-full animate-spin"></div>
                      <p className="text-sm font-medium uppercase">Cargando registros empresariales...</p>
                    </div>
                  </div>
                ) : filteredAreas.length === 0 ? (
                  <div className="p-12 text-center text-slate-400">
                    <div className="flex flex-col items-center gap-2">
                      <FileText size={32} className="text-slate-300" strokeWidth={1.5} />
                      <p className="font-medium text-sm text-slate-500 uppercase">No se encontraron resultados</p>
                      <p className="text-xs text-slate-400 uppercase">Prueba con otro término o añade una nueva área a la izquierda.</p>
                    </div>
                  </div>
                ) : (
                  filteredAreas.map((area) => {
                    const id = area.areaId || area.id;
                    const isSelected = selectedArea?.id === id || selectedArea?.areaId === id;
                    
                    return (
                      <div 
                        key={id} 
                        className={`group p-5 flex items-start justify-between gap-4 transition-all hover:bg-slate-50 cursor-pointer ${
                          isSelected ? "bg-blue-50/50 border-l-4 border-blue-600 pl-4" : ""
                        }`}
                        onClick={() => handleSelectArea(area)}
                      >
                        <div className="space-y-1 min-w-0 flex-1">
                          <div className="flex items-center gap-2.5">
                            <span className="text-[10px] font-mono font-bold text-slate-500 bg-slate-100 border border-slate-200/60 px-2 py-0.5 rounded tracking-wider uppercase">
                              ID: {id}
                            </span>
                            <h3 className="font-bold text-slate-800 text-base group-hover:text-blue-600 transition-colors truncate uppercase">
                              {area.nombre}
                            </h3>
                          </div>
                          <p className="text-sm text-slate-500 line-clamp-2 pr-4 uppercase">
                            {area.descripcion || (
                              <span className="italic text-slate-300 text-xs normal-case">Sin descripción asignada</span>
                            )}
                          </p>
                        </div>

                        {/* Botones de acción minimalistas */}
                        <div className="flex items-center gap-1 opacity-60 group-hover:opacity-100 transition-opacity shrink-0" onClick={(e) => e.stopPropagation()}>
                          <button
                            onClick={() => handleSelectArea(area)}
                            className="p-2 text-slate-500 hover:bg-slate-100 hover:text-slate-800 rounded-lg transition-all"
                            title="Editar"
                          >
                            <Edit2 size={15} />
                          </button>
                          <button
                            onClick={() => handleDelete(id)}
                            className="p-2 text-red-500 hover:bg-red-50 rounded-lg transition-all"
                            title="Eliminar"
                          >
                            <Trash2 size={15} />
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