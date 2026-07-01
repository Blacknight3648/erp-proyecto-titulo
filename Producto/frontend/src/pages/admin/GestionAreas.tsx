import React, { useState, useMemo } from "react";
import { Search, Plus, Edit2, Trash2, Layout, Briefcase, FileText } from "lucide-react";
import { Toaster, toast } from "sonner";
import { confirmDelete } from "../../utils/confirmDelete";
import { useAreas } from "../../hooks/useAreas";

import { Card, CardHeader, CardTitle, CardContent } from "../../ui/card";
import { Input } from "../../ui/input";
import { Textarea } from "../../ui/textarea";
import { Button } from "../../ui/button";

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
    <div className="min-h-[calc(100vh-72px)] bg-background p-6 lg:p-8 font-sans antialiased text-foreground">
      <Toaster position="top-right" richColors closeButton />

      <div className="max-w-7xl mx-auto space-y-6">
        
        {/* ENCABEZADO EMPRESARIAL */}
        <div className="flex flex-col md:flex-row md:items-center justify-between border-b border-border pb-5 gap-4">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-sidebar rounded-xl flex items-center justify-center text-white shadow-sm">
              <Layout size={24} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-foreground tracking-tight">
                Gestión de Áreas
              </h1>
              <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider mt-0.5">
                Administración de departamentos y áreas funcionales de la organización
              </p>
            </div>
          </div>
        </div>

        {/* CONTENEDOR PRINCIPAL: DOS PANELES */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
          
          {/* PANEL IZQUIERDO: FORMULARIO REGISTRO/EDICIÓN */}
          <Card className="lg:col-span-4 sticky top-6 overflow-hidden">
            <CardHeader className="flex flex-row justify-between items-center bg-slate-50/50 border-b border-border py-4 px-6">
              <CardTitle className="text-sm font-bold text-slate-700 uppercase tracking-wider">
                {selectedArea ? "Editar Área" : "Registrar Nueva Área"}
              </CardTitle>
              {selectedArea && (
                <Button 
                  variant="link"
                  size="sm"
                  onClick={handleResetForm}
                  className="text-xs text-blue-600 hover:underline font-medium uppercase p-0 h-auto"
                >
                  Cancelar edición
                </Button>
              )}
            </CardHeader>
            
            <form onSubmit={handleSave}>
              <CardContent className="p-6 space-y-5">
                <div>
                  <label className="block text-xs font-bold text-muted-foreground uppercase mb-2 tracking-wide">
                    Nombre del Área <span className="text-red-500">*</span>
                  </label>
                  <Input
                    type="text"
                    placeholder="EJ: MARKETING, FINANZAS, SISTEMAS..."
                    value={formData.nombre}
                    onChange={(e) => handleInputChange("nombre", e.target.value)}
                    className="uppercase"
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-muted-foreground uppercase mb-2 tracking-wide">
                    Descripción
                  </label>
                  <Textarea
                    rows={4}
                    placeholder="ESCRIBA LOS DETALLES O PROPÓSITOS DEL ÁREA..."
                    value={formData.descripcion}
                    onChange={(e) => handleInputChange("descripcion", e.target.value)}
                    className="uppercase resize-none"
                  />
                </div>

                <div className="pt-2 flex gap-3">
                  {!selectedArea && (
                    <Button
                      type="button"
                      variant="secondary"
                      onClick={() => setFormData({ nombre: "", descripcion: "" })}
                      className="w-1/3 uppercase text-xs"
                    >
                      Limpiar
                    </Button>
                  )}
                  <Button
                    type="submit"
                    className={`flex-1 uppercase text-xs ${
                      selectedArea 
                        ? "bg-amber-600 hover:bg-amber-700 text-white" 
                        : "bg-blue-600 hover:bg-blue-700 text-white"
                    }`}
                  >
                    {selectedArea ? <Edit2 size={16} /> : <Plus size={16} />}
                    {selectedArea ? "Guardar Cambios" : "Crear Área"}
                  </Button>
                </div>
              </CardContent>
            </form>
          </Card>

          {/* PANEL DERECHO: BUSCADOR Y LISTA NAVEGABLE */}
          <div className="lg:col-span-8 space-y-4">
            
            {/* Buscador Limpio de una Sola Línea */}
            <div className="bg-white rounded-xl border border-border shadow-sm flex items-center px-4 py-1 focus-within:ring-2 focus-within:ring-primary/25 transition-all">
              <Search size={18} className="text-muted-foreground mr-3 shrink-0" />
              <input
                type="text"
                placeholder="Filtro rápido por nombre de área..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-transparent border-none outline-none py-3 text-sm text-foreground placeholder-muted-foreground uppercase"
              />
              {searchTerm && (
                <Button 
                  variant="ghost"
                  size="sm"
                  onClick={() => setSearchTerm("")}
                  className="text-xs text-muted-foreground hover:text-foreground font-medium px-2 uppercase"
                >
                  Limpiar
                </Button>
              )}
            </div>

            {/* Contenedor del listado */}
            <Card className="overflow-hidden">
              <CardHeader className="px-6 py-4 bg-slate-50 border-b border-border flex flex-row items-center gap-2">
                <Briefcase size={16} className="text-slate-500" />
                <CardTitle className="text-sm font-bold text-slate-700 uppercase tracking-wider">
                  Listado de Áreas Activas
                </CardTitle>
                <span className="ml-auto bg-slate-200 text-slate-700 text-xs font-bold px-2.5 py-0.5 rounded-full">
                  {filteredAreas.length}
                </span>
              </CardHeader>

              <CardContent className="p-0 divide-y divide-border max-h-[500px] overflow-y-auto custom-scrollbar">
                {loading ? (
                  <div className="p-12 text-center text-muted-foreground">
                    <div className="flex flex-col items-center gap-3">
                      <div className="w-6 h-6 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
                      <p className="text-sm font-medium uppercase">Cargando registros empresariales...</p>
                    </div>
                  </div>
                ) : filteredAreas.length === 0 ? (
                  <div className="p-12 text-center text-muted-foreground">
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
                            <span className="text-[10px] font-mono font-bold text-muted-foreground bg-slate-100 border border-border px-2 py-0.5 rounded tracking-wider uppercase">
                              ID: {id}
                            </span>
                            <h3 className="font-bold text-foreground text-base group-hover:text-primary transition-colors truncate uppercase">
                              {area.nombre}
                            </h3>
                          </div>
                          <p className="text-sm text-muted-foreground line-clamp-2 pr-4 uppercase">
                            {area.descripcion || (
                              <span className="italic text-slate-300 text-xs normal-case">Sin descripción asignada</span>
                            )}
                          </p>
                        </div>

                        {/* Botones de acción minimalistas */}
                        <div className="flex items-center gap-1 opacity-60 group-hover:opacity-100 transition-opacity shrink-0" onClick={(e) => e.stopPropagation()}>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleSelectArea(area)}
                            className="p-2 text-muted-foreground hover:text-foreground hover:bg-slate-100 rounded-lg transition-all"
                            title="Editar"
                          >
                            <Edit2 size={15} />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDelete(id)}
                            className="p-2 text-red-500 hover:bg-red-50 rounded-lg transition-all"
                            title="Eliminar"
                          >
                            <Trash2 size={15} />
                          </Button>
                        </div>
                      </div>
                    );
                  })
                )}
              </CardContent>
            </Card>

          </div>
        </div>

      </div>
    </div>
  );
};

export default GestionAreas;