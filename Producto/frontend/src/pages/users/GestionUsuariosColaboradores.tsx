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
    <div className="min-h-screen bg-[#f8f9fd] p-6 sm:p-8 font-sans antialiased text-slate-800">
      <Toaster position="top-right" richColors />

      {/* --- HEADER PRINCIPAL (Inspirado en la barra superior morada/blanca) --- */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8 bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-[#635bff]/10 text-[#635bff] rounded-xl">
            <Users className="w-6 h-6" />
          </div>
          <div>
            <h1 className="text-xl font-bold text-slate-900">Gestión de Colaboradores</h1>
            <p className="text-sm text-slate-500">Administración de usuarios internos del ERP</p>
          </div>
        </div>

        <button
          onClick={handleOpenNew}
          className="inline-flex items-center justify-center gap-2 bg-[#635bff] hover:bg-[#5249d3] text-white px-5 py-2.5 rounded-xl text-sm font-semibold transition-all shadow-sm hover:shadow-md"
        >
          <Plus className="w-4 h-4" />
          Registrar Nuevo Colaborador
        </button>
      </div>

      {/* --- SECCIÓN DE FILTROS --- */}
      <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
        <div className="p-5 border-b border-slate-100 flex flex-col sm:flex-row gap-4 items-center justify-between bg-slate-50/50">
          {/* Barra de búsqueda */}
          <div className="relative w-full sm:max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Buscar por nombre, email, rol o área..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-white border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 focus:border-[#635bff] transition-all"
            />
          </div>

          {/* Selector de Estado */}
          <div className="flex items-center gap-2 w-full sm:w-auto justify-end">
            <span className="text-xs font-medium text-slate-500 uppercase tracking-wider">Estado:</span>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="bg-white border border-slate-200 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#635bff]/20 focus:border-[#635bff] transition-all cursor-pointer"
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
            <div className="p-12 text-center text-slate-500 text-sm animate-pulse">
              Cargando colaboradores...
            </div>
          ) : filteredColaboradores.length === 0 ? (
            <div className="p-12 text-center text-slate-400 text-sm">
              No se encontraron colaboradores que coincidan con la búsqueda.
            </div>
          ) : (
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50/50 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  <th className="p-4 pl-6">Colaborador</th>
                  <th className="p-4">RUN / Identificación</th>
                  <th className="p-4">Rol y Área</th>
                  <th className="p-4 text-center">Estado</th>
                  <th className="p-4 pr-6 text-right">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filteredColaboradores.map((c) => {
                  const id = c.usuarioId || c.id;
                  const isActive = c.enabled ?? c.activo;
                  const fullName = `${c.usuarioNombre || c.nombre || ''} ${c.usuarioApellidos || ''}`;
                  const email = c.usuarioEmail || c.email;
                  const run = c.usuarioRun || c.run || "N/A";

                  return (
                    <tr key={id} className="hover:bg-slate-50/70 transition-colors group">
                      {/* Nombre e Email */}
                      <td className="p-4 pl-6">
                        <div className="flex items-center gap-3">
                          <div className="w-10 h-10 rounded-full bg-slate-100 text-[#635bff] font-bold text-sm flex items-center justify-center border border-slate-200">
                            {fullName.charAt(0).toUpperCase()}
                          </div>
                          <div>
                            <div className="font-semibold text-slate-900 text-sm">{fullName}</div>
                            <div className="text-xs text-slate-500">{email}</div>
                          </div>
                        </div>
                      </td>

                      {/* RUN */}
                      <td className="p-4 text-sm text-slate-600 font-medium">
                        {run}
                      </td>

                      {/* Rol y Área */}
                      <td className="p-4">
                        <div className="text-sm font-medium text-slate-800">{c.roles?.[0]?.nombre || c.rol || "Sin Rol"}</div>
                        <div className="text-xs text-slate-400">{c.areas?.[0]?.nombre || c.area || "Sin Área"}</div>
                      </td>

                      {/* Estado con Badge (Estilo "Paid" de tu imagen) */}
                      <td className="p-4 text-center">
                        <button
                          onClick={() => toggleColaborador(id)}
                          title="Click para cambiar estado"
                          className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold transition-all border ${
                            isActive
                              ? "bg-emerald-50 border-emerald-200 text-emerald-700 hover:bg-emerald-100"
                              : "bg-amber-50 border-amber-200 text-amber-700 hover:bg-amber-100"
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
                            className="p-2 text-slate-400 hover:text-[#635bff] hover:bg-slate-100 rounded-lg transition-colors"
                            title="Editar"
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDelete(id)}
                            className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors"
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