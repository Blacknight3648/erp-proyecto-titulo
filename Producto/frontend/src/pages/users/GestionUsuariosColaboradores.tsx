"use client";

import { useState } from "react";
import { Users, Search, Plus, Edit2, Trash2, UserCheck, UserX } from "lucide-react";
import { Toaster } from "sonner";
import { confirmDelete } from "../../utils/confirmDelete";

import { useColaboradores } from "../../hooks/useColaboradores";
import ColaboradorModal from "../../components/shared/ColaboradorModal";

export default function GestionUsuariosColaboradores() {
  const {
    colaboradores,
    loading,
    createColaborador,
    updateColaborador,
    deleteColaborador,
    toggleColaborador
  } = useColaboradores();

  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("Todos");
  const [showModal, setShowModal] = useState(false);
  const [selectedCollaborator, setSelectedCollaborator] = useState(null);

  // Filtrado de colaboradores
  const filteredColaboradores = colaboradores.filter((c) => {
    const searchLower = searchTerm.toLowerCase();
    const fullName = `${c.usuarioNombre || c.nombre || ''} ${c.usuarioApellidos || ''}`.toLowerCase();

    const matchesSearch =
      fullName.includes(searchLower) ||
      c.usuarioEmail?.toLowerCase().includes(searchLower) ||
      c.email?.toLowerCase().includes(searchLower) ||
      c.usuarioRun?.includes(searchTerm) ||
      c.run?.includes(searchTerm) ||
      c.rol?.toLowerCase().includes(searchLower) ||
      c.area?.toLowerCase().includes(searchLower);

    const isActive = c.enabled ?? c.activo;
    const status = isActive ? "Activo" : "Suspendido";
    const matchesFilter = filterStatus === "Todos" || filterStatus === status;
    return matchesSearch && matchesFilter;
  });

  const handleDelete = (uid) => {
    confirmDelete("¿Está seguro de eliminar este colaborador?", () => deleteColaborador(uid));
  };

  const handleOpenNew = () => {
    setSelectedCollaborator(null);
    setShowModal(true);
  };

  const handleOpenEdit = (colaborador) => {
    setSelectedCollaborator(colaborador);
    setShowModal(true);
  };

  const handleSave = (data) => {
    if (selectedCollaborator) {
      updateColaborador({
        ...data,
        usuarioId: selectedCollaborator.usuarioId || selectedCollaborator.id
      });
    } else {
      createColaborador(data);
    }
    setShowModal(false);
  };

  return (
    <div className="min-h-screen bg-muted p-6 sm:p-8 font-sans antialiased text-foreground">
      <Toaster position="top-right" richColors />

      {/* --- HEADER PRINCIPAL --- */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8 bg-card p-6 rounded-2xl shadow-sm border border-border">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-brand-violet/10 text-brand-violet rounded-xl">
            <Users className="w-6 h-6" />
          </div>
          <div>
            <h1 className="text-xl font-bold text-foreground">Gestión de Colaboradores</h1>
            <p className="text-sm text-muted-foreground">Administración de usuarios internos del ERP</p>
          </div>
        </div>

        <button
          onClick={handleOpenNew}
          className="inline-flex items-center justify-center gap-2 bg-brand-violet hover:bg-brand-violet/90 text-white px-5 py-2.5 rounded-xl text-sm font-semibold transition-all shadow-sm hover:shadow-md"
        >
          <Plus className="w-4 h-4" />
          Registrar Nuevo Colaborador
        </button>
      </div>

      {/* --- SECCIÓN DE FILTROS --- */}
      <div className="bg-card rounded-2xl shadow-sm border border-border overflow-hidden">
        <div className="p-5 border-b border-border flex flex-col sm:flex-row gap-4 items-center justify-between bg-muted/50">
          {/* Barra de búsqueda */}
          <div className="relative w-full sm:max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder="Buscar por nombre, email, rol o área..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-card border border-border rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-brand-violet/20 focus:border-brand-violet transition-all"
            />
          </div>

          {/* Selector de Estado */}
          <div className="flex items-center gap-2 w-full sm:w-auto justify-end">
            <span className="text-xs font-medium text-muted-foreground uppercase tracking-wider">Estado:</span>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="bg-card border border-border rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-violet/20 focus:border-brand-violet transition-all cursor-pointer"
            >
              <option value="Todos">Todos los estados</option>
              <option value="Activo">Activos</option>
              <option value="Suspendido">Suspendidos</option>
            </select>
          </div>
        </div>

        {/* --- TABLA DE USUARIOS --- */}
        <div className="overflow-x-auto">
          {loading ? (
            <div className="p-12 text-center text-muted-foreground text-sm animate-pulse">
              Cargando colaboradores...
            </div>
          ) : filteredColaboradores.length === 0 ? (
            <div className="p-12 text-center text-muted-foreground text-sm">
              No se encontraron colaboradores que coincidan con la búsqueda.
            </div>
          ) : (
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-border bg-muted/50 text-xs font-semibold text-muted-foreground uppercase tracking-wider">
                  <th className="p-4 pl-6">Colaborador</th>
                  <th className="p-4">RUN / Identificación</th>
                  <th className="p-4">Rol y Área</th>
                  <th className="p-4 text-center">Estado</th>
                  <th className="p-4 pr-6 text-right">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border">
                {filteredColaboradores.map((c) => {
                  const id = c.usuarioId || c.id;
                  const isActive = c.enabled ?? c.activo;
                  const fullName = `${c.usuarioNombre || c.nombre || ''} ${c.usuarioApellidos || ''}`;
                  const email = c.usuarioEmail || c.email;
                  const run = c.usuarioRun || c.run || "N/A";

                  return (
                    <tr key={id} className="hover:bg-muted/70 transition-colors group">
                      {/* Nombre e Email */}
                      <td className="p-4 pl-6">
                        <div className="flex items-center gap-3">
                          <div className="w-10 h-10 rounded-full bg-muted text-brand-violet font-bold text-sm flex items-center justify-center border border-border">
                            {fullName.charAt(0).toUpperCase()}
                          </div>
                          <div>
                            <div className="font-semibold text-foreground text-sm">{fullName}</div>
                            <div className="text-xs text-muted-foreground">{email}</div>
                          </div>
                        </div>
                      </td>

                      {/* RUN */}
                      <td className="p-4 text-sm text-muted-foreground font-medium">
                        {run}
                      </td>

                      {/* Rol y Área */}
                      <td className="p-4">
                        <div className="text-sm font-medium text-foreground">{c.roles?.[0]?.nombre || c.rol || "Sin Rol"}</div>
                        <div className="text-xs text-muted-foreground">{c.areas?.[0]?.nombre || c.area || "Sin Área"}</div>
                      </td>

                      {/* Estado con Badge */}
                      <td className="p-4 text-center">
                        <button
                          onClick={() => toggleColaborador(id)}
                          title="Click para cambiar estado"
                          className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold transition-all border ${
                            isActive
                              ? "bg-success/10 border-success/20 text-success hover:bg-success/20"
                              : "bg-warning/10 border-warning/20 text-warning hover:bg-warning/20"
                          }`}
                        >
                          {isActive ? (
                            <>
                              <UserCheck className="w-3.5 h-3.5" /> Activo
                            </>
                          ) : (
                            <>
                              <UserX className="w-3.5 h-3.5" /> Suspendido
                            </>
                          )}
                        </button>
                      </td>

                      {/* Acciones compactas */}
                      <td className="p-4 pr-6 text-right">
                        <div className="flex items-center justify-end gap-2 opacity-80 group-hover:opacity-100 transition-opacity">
                          <button
                            onClick={() => handleOpenEdit(c)}
                            className="p-2 text-muted-foreground hover:text-brand-violet hover:bg-muted rounded-lg transition-colors"
                            title="Editar"
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDelete(id)}
                            className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                            title="Eliminar"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* Modales heredados */}
      {showModal && (
        <ColaboradorModal
          onClose={() => setShowModal(false)}
          onSave={handleSave}
          collaboratorToEdit={selectedCollaborator}
        />
      )}
    </div>
  );
}
