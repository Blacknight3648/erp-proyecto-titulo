"use client";

import { useState } from "react";
import { Users, Search, Plus, Edit2, Trash2, UserCheck, UserX, Loader2 } from "lucide-react";
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
    <div className="min-h-screen bg-zinc-50/50 px-4 py-8 sm:px-8 font-sans antialiased text-zinc-900 selection:bg-zinc-200">
      <Toaster position="top-right" richColors />

      <div className="max-w-7xl mx-auto space-y-6">
        
        {/* --- HEADER PRINCIPAL --- */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 border-b border-zinc-200/60">
          <div className="flex items-center gap-3.5">
            <div className="p-2.5 bg-white border border-zinc-200 shadow-sm rounded-xl text-zinc-700">
              <Users className="w-5 h-5 stroke-[1.75]" />
            </div>
            <div>
              <h1 className="text-xl font-semibold tracking-tight text-zinc-900">Gestión de Colaboradores</h1>
              <p className="text-xs text-zinc-500 font-medium mt-0.5">Administración de usuarios internos y permisos del ERP</p>
            </div>
          </div>

          <button
            onClick={handleOpenNew}
            className="inline-flex items-center justify-center gap-2 bg-zinc-900 hover:bg-zinc-800 text-white px-4 py-2.5 rounded-xl text-xs font-semibold tracking-wide transition-all shadow-sm hover:shadow active:scale-[0.98]"
          >
            <Plus className="w-4 h-4" />
            Nuevo Colaborador
          </button>
        </div>

        {/* --- CONTENEDOR PRINCIPAL / FILTROS --- */}
        <div className="bg-white rounded-2xl border border-zinc-200/80 shadow-[0_2px_8px_-3px_rgba(0,0,0,0.05)] overflow-hidden">
          <div className="p-5 flex flex-col sm:flex-row gap-4 items-center justify-between border-b border-zinc-100">
            
            {/* Barra de búsqueda */}
            <div className="relative w-full sm:max-w-md">
              <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-400 stroke-[2]" />
              <input
                type="text"
                placeholder="Buscar por nombre, email, rol o área..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 bg-zinc-50/50 hover:bg-zinc-50 border border-zinc-200 rounded-xl text-xs font-medium placeholder:text-zinc-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all"
              />
            </div>

            {/* Selector de Estado */}
            <div className="flex items-center gap-2.5 w-full sm:w-auto justify-end">
              <span className="text-[11px] font-bold text-zinc-400 uppercase tracking-wider">Estado:</span>
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="bg-white border border-zinc-200 rounded-xl px-3 py-2 text-xs font-medium text-zinc-700 shadow-sm focus:outline-none focus:ring-2 focus:ring-zinc-900/5 focus:border-zinc-500 transition-all cursor-pointer"
              >
                <option value="Todos">Todos</option>
                <option value="Activo">Activos</option>
                <option value="Suspendido">Suspendidos</option>
              </select>
            </div>
          </div>

          {/* --- TABLA DE COLABORADORES --- */}
          <div className="overflow-x-auto">
            {loading ? (
              <div className="p-16 flex flex-col items-center justify-center gap-2 text-zinc-400 text-xs font-semibold uppercase tracking-wider">
                <Loader2 className="w-5 h-5 animate-spin text-zinc-500" />
                Cargando información...
              </div>
            ) : filteredColaboradores.length === 0 ? (
              <div className="p-16 text-center text-zinc-400 text-xs font-medium">
                No se encontraron colaboradores para los criterios seleccionados.
              </div>
            ) : (
              <table className="w-full text-left border-collapse table-fixed min-w-[700px]">
                <thead>
                  <tr className="border-b border-zinc-100 bg-zinc-50/70 text-[10px] font-bold text-zinc-400 uppercase tracking-widest">
                    <th className="p-4 pl-6 w-[35%]">Colaborador</th>
                    <th className="p-4 w-[20%]">RUN / Identificación</th>
                    <th className="p-4 w-[25%]">Rol y Área</th>
                    <th className="p-4 w-[10%] text-center">Estado</th>
                    <th className="p-4 pr-6 w-[10%] text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-zinc-100/70">
                  {filteredColaboradores.map((c) => {
                    const id = c.usuarioId || c.id;
                    const isActive = c.enabled ?? c.activo;
                    const fullName = `${c.usuarioNombre || c.nombre || ''} ${c.usuarioApellidos || ''}`.trim() || "Usuario sin nombre";
                    const initial = fullName.charAt(0).toUpperCase();
                    const email = c.usuarioEmail || c.email || "Sin email registrado";
                    const run = c.usuarioRun || c.run || "—";
                    const rolLabel = c.roles?.[0]?.nombre || c.rol || "Sin Rol";
                    const areaLabel = c.areas?.[0]?.nombre || c.area || "Sin Área";

                    return (
                      <tr key={id} className="hover:bg-zinc-50/50 transition-colors group">
                        
                        {/* Colaborador e Email */}
                        <td className="p-4 pl-6 truncate">
                          <div className="flex items-center gap-3">
                            <div className="w-9 h-9 rounded-xl bg-zinc-100 text-zinc-700 font-bold text-xs flex items-center justify-center shrink-0 border border-zinc-200/40 uppercase">
                              {initial}
                            </div>
                            <div className="truncate">
                              <div className="font-semibold text-zinc-900 text-xs truncate uppercase">{fullName}</div>
                              <div className="text-[11px] text-zinc-400 truncate mt-0.5 font-medium">{email}</div>
                            </div>
                          </div>
                        </td>

                        {/* RUN */}
                        <td className="p-4 text-xs text-zinc-500 font-mono font-medium tracking-tight">
                          {run}
                        </td>

                        {/* Rol y Área */}
                        <td className="p-4 truncate">
                          <div className="text-xs font-semibold text-zinc-800 truncate uppercase">{rolLabel}</div>
                          <div className="text-[11px] text-zinc-400 truncate mt-0.5 font-medium uppercase">{areaLabel}</div>
                        </td>

                        {/* Estado con Badge Minimalista */}
                        <td className="p-4 text-center">
                          <button
                            onClick={() => toggleColaborador(id)}
                            title="Cambiar estado operacional"
                            className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-lg text-[10px] font-bold tracking-wide uppercase transition-all border ${
                              isActive
                                ? "bg-emerald-50 border-emerald-200/60 text-emerald-700 hover:bg-emerald-100/80"
                                : "bg-amber-50 border-amber-200/60 text-amber-700 hover:bg-amber-100/80"
                            }`}
                          >
                            {isActive ? (
                              <>
                                <UserCheck className="w-3 h-3 stroke-[2.5]" /> Activo
                              </>
                            ) : (
                              <>
                                <UserX className="w-3 h-3 stroke-[2.5]" /> Suspendido
                              </>
                            )}
                          </button>
                        </td>

                        {/* Acciones Sutiles */}
                        <td className="p-4 pr-6 text-right">
                          <div className="flex items-center justify-end gap-1">
                            <button
                              onClick={() => handleOpenEdit(c)}
                              className="p-2 text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 rounded-lg transition-all"
                              title="Editar colaborador"
                            >
                              <Edit2 className="w-3.5 h-3.5 stroke-[2]" />
                            </button>
                            <button
                              onClick={() => handleDelete(id)}
                              className="p-2 text-zinc-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all"
                              title="Eliminar colaborador"
                            >
                              <Trash2 className="w-3.5 h-3.5 stroke-[2]" />
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
      </div>

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