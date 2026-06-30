import React, { useState, useMemo } from "react";
import { Search, Plus, Edit2, Trash2, ShieldCheck, FileText, KeyRound } from "lucide-react";
import { Toaster, toast } from "sonner";
import { confirmDelete } from "../../../utils/confirmDelete.jsx";
import { useRoles } from "../../../hooks/useRoles";
import { useNavigate } from "react-router-dom";

import { Card, CardHeader, CardTitle, CardContent } from "../../ui/card";
import { Input } from "../../ui/input";
import { Button } from "../../ui/button";

const GestionRoles = () => {
  const { roles, loading, createRole, updateRole, deleteRole } = useRoles();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedRol, setSelectedRol] = useState(null);
  const [formData, setFormData] = useState({ nombre: "", descripcion: "" });
  const navigate = useNavigate();

  const filteredRoles = useMemo(() => {
    return roles.filter((rol) =>
      rol.nombre?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [roles, searchTerm]);

  const handleInputChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value.toUpperCase() }));
  };

  const handleSelectRol = (rol) => {
    setSelectedRol(rol);
    setFormData({
      nombre: (rol.nombre || "").toUpperCase(),
      descripcion: (rol.descripcion || "").toUpperCase(),
    });
  };

  const handleResetForm = () => {
    setSelectedRol(null);
    setFormData({ nombre: "", descripcion: "" });
  };

  const handleDelete = (id) => {
    confirmDelete("¿Está seguro de eliminar este rol?", async () => {
      try {
        await deleteRole(id);
        if (selectedRol?.id === id) handleResetForm();
      } catch (error) {
        console.error("Error al eliminar:", error);
      }
    });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!formData.nombre.trim()) {
      toast.error("El nombre del rol es requerido");
      return;
    }

    const finalData = {
      nombre: formData.nombre.trim().toUpperCase(),
      descripcion: formData.descripcion.trim().toUpperCase(),
    };

    try {
      if (selectedRol) {
        await updateRole(selectedRol.id, finalData);
        toast.success("Rol actualizado correctamente");
      } else {
        await createRole(finalData);
        toast.success("Rol creado correctamente");
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

        {/* ENCABEZADO */}
        <div className="flex flex-col md:flex-row md:items-center justify-between border-b border-border pb-5 gap-4">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-sidebar rounded-xl flex items-center justify-center text-white shadow-sm">
              <ShieldCheck size={24} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-foreground tracking-tight">
                Gestión de Roles
              </h1>
              <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider mt-0.5">
                Administración de perfiles y niveles de acceso
              </p>
            </div>
          </div>
        </div>

        {/* DOS PANELES */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">

          {/* PANEL IZQUIERDO: FORMULARIO */}
          <Card className="lg:col-span-4 sticky top-6 overflow-hidden">
            <CardHeader className="flex flex-row justify-between items-center bg-slate-50/50 border-b border-border py-4 px-6">
              <CardTitle className="text-sm font-bold text-slate-700 uppercase tracking-wider">
                {selectedRol ? "Editar Rol" : "Registrar Nuevo Rol"}
              </CardTitle>
              {selectedRol && (
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
                    Nombre del Rol <span className="text-red-500">*</span>
                  </label>
                  <Input
                    type="text"
                    placeholder="EJ: JEFE_COMERCIAL, VENDEDOR..."
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
                  <textarea
                    rows={4}
                    placeholder="DESCRIBA LAS RESPONSABILIDADES O ALCANCE DEL ROL..."
                    value={formData.descripcion}
                    onChange={(e) => handleInputChange("descripcion", e.target.value)}
                    className="uppercase resize-none w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                  />
                </div>

                <div className="pt-2 flex gap-3">
                  {!selectedRol && (
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
                      selectedRol
                        ? "bg-amber-600 hover:bg-amber-700 text-white"
                        : "bg-blue-600 hover:bg-blue-700 text-white"
                    }`}
                  >
                    {selectedRol ? <Edit2 size={16} /> : <Plus size={16} />}
                    {selectedRol ? "Guardar Cambios" : "Crear Rol"}
                  </Button>
                </div>
              </CardContent>
            </form>
          </Card>

          {/* PANEL DERECHO: BUSCADOR Y LISTA */}
          <div className="lg:col-span-8 space-y-4">

            <div className="bg-white rounded-xl border border-border shadow-sm flex items-center px-4 py-1 focus-within:ring-2 focus-within:ring-primary/25 transition-all">
              <Search size={18} className="text-muted-foreground mr-3 shrink-0" />
              <input
                type="text"
                placeholder="Filtro rápido por nombre de rol..."
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

            <Card className="overflow-hidden">
              <CardHeader className="px-6 py-4 bg-slate-50 border-b border-border flex flex-row items-center gap-2">
                <ShieldCheck size={16} className="text-slate-500" />
                <CardTitle className="text-sm font-bold text-slate-700 uppercase tracking-wider">
                  Listado de Roles Activos
                </CardTitle>
                <span className="ml-auto bg-slate-200 text-slate-700 text-xs font-bold px-2.5 py-0.5 rounded-full">
                  {filteredRoles.length}
                </span>
              </CardHeader>

              <CardContent className="p-0 divide-y divide-border max-h-[500px] overflow-y-auto custom-scrollbar">
                {loading ? (
                  <div className="p-12 text-center text-muted-foreground">
                    <div className="flex flex-col items-center gap-3">
                      <div className="w-6 h-6 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
                      <p className="text-sm font-medium uppercase">Cargando roles del sistema...</p>
                    </div>
                  </div>
                ) : filteredRoles.length === 0 ? (
                  <div className="p-12 text-center text-muted-foreground">
                    <div className="flex flex-col items-center gap-2">
                      <FileText size={32} className="text-slate-300" strokeWidth={1.5} />
                      <p className="font-medium text-sm text-slate-500 uppercase">No se encontraron resultados</p>
                      <p className="text-xs text-slate-400 uppercase">Prueba con otro término o añade un nuevo rol a la izquierda.</p>
                    </div>
                  </div>
                ) : (
                  filteredRoles.map((rol) => {
                    const isSelected = selectedRol?.id === rol.id;

                    return (
                      <div
                        key={rol.id}
                        className={`group p-5 flex items-start justify-between gap-4 transition-all hover:bg-slate-50 cursor-pointer ${
                          isSelected ? "bg-blue-50/50 border-l-4 border-blue-600 pl-4" : ""
                        }`}
                        onClick={() => handleSelectRol(rol)}
                      >
                        <div className="space-y-1 min-w-0 flex-1">
                          <div className="flex items-center gap-2.5">
                            <span className="text-[10px] font-mono font-bold text-muted-foreground bg-slate-100 border border-border px-2 py-0.5 rounded tracking-wider uppercase">
                              ID: {rol.id}
                            </span>
                            <h3 className="font-bold text-foreground text-base group-hover:text-primary transition-colors truncate uppercase">
                              {rol.nombre}
                            </h3>
                          </div>
                          <p className="text-sm text-muted-foreground line-clamp-2 pr-4 uppercase">
                            {rol.descripcion || (
                              <span className="italic text-slate-300 text-xs normal-case">Sin descripción asignada</span>
                            )}
                          </p>
                        </div>

                        <div className="flex items-center gap-1 opacity-60 group-hover:opacity-100 transition-opacity shrink-0" onClick={(e) => e.stopPropagation()}>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/admin/roles/${rol.id}/permisos`)}
                            className="p-2 text-purple-500 hover:text-purple-700 hover:bg-purple-50 rounded-lg transition-all"
                            title="Gestionar permisos"
                          >
                            <KeyRound size={15} />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleSelectRol(rol)}
                            className="p-2 text-muted-foreground hover:text-foreground hover:bg-slate-100 rounded-lg transition-all"
                            title="Editar"
                          >
                            <Edit2 size={15} />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDelete(rol.id)}
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

export default GestionRoles;
