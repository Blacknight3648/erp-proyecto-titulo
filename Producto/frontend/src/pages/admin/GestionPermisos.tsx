import React, { useState, useMemo } from "react";
import { Search, Plus, Edit2, Trash2, KeyRound, FileText, Loader2, ShieldCheck } from "lucide-react";
import { Toaster, toast } from "sonner";
import { confirmDelete } from "../../utils/confirmDelete";
import { usePermisos } from "../../hooks/usePermisos";

const GestionPermisos = () => {
  const { permisos, loading, createPermiso, updatePermiso, deletePermiso } = usePermisos();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedPermiso, setSelectedPermiso] = useState<any>(null);
  
  const [formData, setFormData] = useState({ 
    nombre: "", 
    descripcion: "",
    modulo: "GENERAL"
  });

  const modulosDisponibles = [
    "GENERAL", "CLIENTES", "PROVEEDORES", "COLABORADORES", 
    "VENDEDORES", "ROLES", "AREAS", "PRODUCCION", 
    "COMERCIAL", "REPORTES", "TRAZABILIDAD"
  ];

  const filteredPermisos = useMemo(() => {
    return permisos.filter((permiso: any) =>
      permiso.nombre?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      permiso.modulo?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [permisos, searchTerm]);

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value.toUpperCase() 
    }));
  };

  const handleSelectPermiso = (permiso: any) => {
    setSelectedPermiso(permiso);
    setFormData({
      nombre: (permiso.nombre || "").toUpperCase(),
      descripcion: (permiso.descripcion || "").toUpperCase(),
      modulo: (permiso.modulo || "GENERAL").toUpperCase()
    });
  };

  const handleResetForm = () => {
    setSelectedPermiso(null);
    setFormData({ nombre: "", descripcion: "", modulo: "GENERAL" });
  };

  const handleDelete = (id: number) => {
    confirmDelete("¿Está seguro de eliminar este permiso? Esto podría afectar a los roles que ya lo tienen asignado.", async () => {
      try {
        await deletePermiso(id);
        if (selectedPermiso?.id === id) {
          handleResetForm();
        }
        toast.success("Permiso eliminado correctamente");
      } catch (error) {
        console.error("Error al eliminar:", error);
      }
    });
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.nombre.trim()) {
      toast.error("El nombre del permiso es requerido");
      return;
    }

    const finalData = {
      nombre: formData.nombre.trim().toUpperCase(),
      descripcion: formData.descripcion.trim().toUpperCase(),
      modulo: formData.modulo.toUpperCase()
    };

    try {
      if (selectedPermiso) {
        await updatePermiso(selectedPermiso.id, finalData);
        toast.success("Permiso actualizado correctamente");
      } else {
        await createPermiso(finalData);
        toast.success("Permiso creado correctamente");
      }
      handleResetForm();
    } catch (error) {
      console.error("Error al guardar:", error);
      toast.error("Ocurrió un error al guardar el permiso");
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
              <KeyRound className="w-5 h-5 stroke-[1.75]" />
            </div>
            <div>
              <h1 className="text-xl font-semibold tracking-tight text-zinc-900">
                Gestión de Permisos
              </h1>
              <p className="text-xs text-zinc-500 font-medium mt-0.5">
                Administración centralizada de accesos y autorizaciones del sistema
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
                {selectedPermiso ? "Editar Permiso" : "Registrar Nuevo Permiso"}
              </h2>
              {selectedPermiso && (
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
                  Clave del Permiso <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  placeholder="EJ: VER_CLIENTES, CREAR_ROLES..."
                  value={formData.nombre}
                  onChange={(e) => handleInputChange("nombre", e.target.value)}
                  className="w-full bg-zinc-50/50 border border-zinc-200 rounded-xl px-3 py-2 text-xs font-semibold uppercase placeholder:text-zinc-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all font-mono"
                  required
                />
              </div>

              <div>
                <label className="block text-[11px] font-bold text-zinc-500 uppercase mb-2 tracking-wider">
                  Módulo <span className="text-red-500">*</span>
                </label>
                <select
                  value={formData.modulo}
                  onChange={(e) => handleInputChange("modulo", e.target.value)}
                  className="w-full bg-zinc-50/50 border border-zinc-200 rounded-xl px-3 py-2 text-xs font-semibold uppercase text-zinc-700 focus:bg-white focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all cursor-pointer"
                  required
                >
                  {modulosDisponibles.map(mod => (
                    <option key={mod} value={mod}>{mod}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-[11px] font-bold text-zinc-500 uppercase mb-2 tracking-wider">
                  Descripción
                </label>
                <textarea
                  rows={4}
                  placeholder="EXPLICA QUÉ HACE EXACTAMENTE ESTE PERMISO..."
                  value={formData.descripcion}
                  onChange={(e) => handleInputChange("descripcion", e.target.value)}
                  className="w-full bg-zinc-50/50 border border-zinc-200 rounded-xl px-3 py-2 text-xs font-medium uppercase placeholder:text-zinc-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all resize-none"
                />
              </div>

              <div className="pt-2 flex gap-3">
                {!selectedPermiso && (
                  <button
                    type="button"
                    onClick={() => setFormData({ nombre: "", descripcion: "", modulo: "GENERAL" })}
                    className="w-1/3 py-2.5 rounded-xl font-bold uppercase tracking-wide text-[10px] text-zinc-500 bg-zinc-100 hover:bg-zinc-200 transition-colors"
                  >
                    Limpiar
                  </button>
                )}
                <button
                  type="submit"
                  className={`flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl font-bold uppercase tracking-wide text-[10px] text-white shadow-sm transition-all active:scale-[0.98] ${
                    selectedPermiso
                      ? "bg-amber-500 hover:bg-amber-600 shadow-amber-500/20"
                      : "bg-zinc-900 hover:bg-zinc-800"
                  }`}
                >
                  {selectedPermiso ? <Edit2 size={14} /> : <Plus size={14} />}
                  {selectedPermiso ? "Guardar Cambios" : "Crear Permiso"}
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
                placeholder="Filtro rápido por nombre o módulo..."
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
                  <ShieldCheck size={14} className="text-zinc-400 stroke-[2]" />
                  <h2 className="text-[11px] font-bold text-zinc-400 uppercase tracking-widest">
                    Catálogo de Permisos Disponibles
                  </h2>
                </div>
                <span className="bg-zinc-200/50 text-zinc-500 text-[10px] font-bold px-2.5 py-0.5 rounded-full">
                  {filteredPermisos.length}
                </span>
              </div>

              <div className="divide-y divide-zinc-100 max-h-[600px] overflow-y-auto custom-scrollbar">
                {loading ? (
                  <div className="p-16 flex flex-col items-center justify-center gap-3 text-zinc-400">
                    <Loader2 className="w-6 h-6 animate-spin text-zinc-300" />
                    <p className="text-[11px] font-bold uppercase tracking-widest">Cargando permisos desde el servidor...</p>
                  </div>
                ) : filteredPermisos.length === 0 ? (
                  <div className="p-16 flex flex-col items-center gap-2 text-center">
                    <FileText size={28} className="text-zinc-300 stroke-[1.5] mb-2" />
                    <p className="font-bold text-[11px] text-zinc-400 uppercase tracking-widest">No se encontraron resultados</p>
                    <p className="text-[10px] text-zinc-400/80 uppercase font-medium">Prueba con otro término o crea un nuevo permiso de sistema.</p>
                  </div>
                ) : (
                  filteredPermisos.map((permiso: any) => {
                    const id = permiso.id;
                    const isSelected = selectedPermiso?.id === id;
                    
                    return (
                      <div 
                        key={id} 
                        className={`group p-5 flex items-start justify-between gap-4 transition-colors hover:bg-zinc-50/80 cursor-pointer ${
                          isSelected ? "bg-zinc-50 border-l-4 border-l-zinc-900 pl-4" : "border-l-4 border-l-transparent"
                        }`}
                        onClick={() => handleSelectPermiso(permiso)}
                      >
                        <div className="space-y-1 min-w-0 flex-1">
                          <div className="flex items-center gap-2.5 mb-1.5">
                            <span className="text-[9px] font-mono font-bold text-zinc-500 bg-zinc-100 border border-zinc-200/60 px-1.5 py-0.5 rounded tracking-widest uppercase">
                              ID: {id}
                            </span>
                            <span className="text-[9px] font-bold text-indigo-600 bg-indigo-50 border border-indigo-100 px-1.5 py-0.5 rounded tracking-widest uppercase">
                              {permiso.modulo || 'GENERAL'}
                            </span>
                          </div>
                          <h3 className="font-bold text-zinc-900 text-sm group-hover:text-black transition-colors truncate uppercase font-mono tracking-tight">
                            {permiso.nombre}
                          </h3>
                          <p className="text-xs text-zinc-500 line-clamp-2 pr-4 font-medium uppercase mt-1">
                            {permiso.descripcion || (
                              <span className="text-[10px] text-zinc-400 font-medium">SIN DESCRIPCIÓN ASIGNADA</span>
                            )}
                          </p>
                        </div>

                        {/* Botones de acción */}
                        <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity shrink-0" onClick={(e) => e.stopPropagation()}>
                          <button
                            onClick={() => handleSelectPermiso(permiso)}
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

export default GestionPermisos;
