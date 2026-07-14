"use client";

import { useState } from "react";
import { Search, Plus, Edit2, Trash2, Loader2, UserCheck, UserX, Briefcase } from "lucide-react";
import { Toaster } from "sonner";
import { confirmDelete } from "../../utils/confirmDelete";

import { useVendedores } from "../../hooks/useVendedores";
import VendedorModal from "../../components/shared/VendedorModal";

export default function GestionVendedores() {
  const {
    vendedores,
    loading,
    createVendedor,
    updateVendedor,
    deleteVendedor,
  } = useVendedores();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [vendedorToEdit, setVendedorToEdit] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("Todos");

  const filteredVendedores = vendedores.filter((v) => {
    const search = searchTerm.toLowerCase();
    const fullName = [v.nombreUsuario, v.apellidosUsuario].filter(Boolean).join(" ").toLowerCase();
    const matchesSearch =
      fullName.includes(search) ||
      v.codigoVendedor?.toLowerCase().includes(search);

    const isActive = v.activo !== false;
    const matchesStatus =
      filterStatus === "Todos" ||
      (filterStatus === "Activo" && isActive) ||
      (filterStatus === "Suspendido" && !isActive);

    return matchesSearch && matchesStatus;
  });

  const handleCreate = () => {
    setVendedorToEdit(null);
    setIsModalOpen(true);
  };

  const handleEdit = (vendedor: any) => {
    setVendedorToEdit(vendedor);
    setIsModalOpen(true);
  };

  const handleSave = async (data: any) => {
    if (vendedorToEdit?.id) {
      await updateVendedor(vendedorToEdit.id, data);
    } else {
      await createVendedor(data);
    }
    setVendedorToEdit(null);
    setIsModalOpen(false);
  };

  const handleDelete = (id: number) => {
    confirmDelete("¿Está seguro de eliminar este vendedor?", () => deleteVendedor(id));
  };

  const handleClose = () => {
    setVendedorToEdit(null);
    setIsModalOpen(false);
  };

  return (
    <div className="min-h-screen bg-zinc-50/50 px-4 py-8 sm:px-8 font-sans antialiased text-zinc-900 selection:bg-zinc-200">
      <Toaster position="top-right" richColors />

      <div className="max-w-7xl mx-auto space-y-6">
        
        {/* --- HEADER PRINCIPAL --- */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 border-b border-zinc-200/60">
          <div className="flex items-center gap-3.5">
            <div className="p-2.5 bg-white border border-zinc-200 shadow-sm rounded-xl text-zinc-700">
              <Briefcase className="w-5 h-5 stroke-[1.75]" />
            </div>
            <div>
              <h1 className="text-xl font-semibold tracking-tight text-zinc-900">Gestión de Vendedores</h1>
              <p className="text-xs text-zinc-500 font-medium mt-0.5">Administración del equipo de ventas y códigos asignados</p>
            </div>
          </div>

          <button
            onClick={handleCreate}
            className="inline-flex items-center justify-center gap-2 bg-zinc-900 hover:bg-zinc-800 text-white px-4 py-2.5 rounded-xl text-xs font-semibold tracking-wide transition-all shadow-sm hover:shadow active:scale-[0.98]"
          >
            <Plus className="w-4 h-4" />
            Nuevo Vendedor
          </button>
        </div>

        {/* --- BARRA DE BÚSQUEDA Y FILTROS --- */}
        <div className="bg-white rounded-2xl border border-zinc-200/80 shadow-[0_2px_8px_-3px_rgba(0,0,0,0.05)] overflow-hidden">
          <div className="p-5 flex flex-col sm:flex-row gap-4 items-center justify-between">
            {/* Barra de búsqueda */}
            <div className="relative w-full sm:max-w-md">
              <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-400 stroke-[2]" />
              <input
                type="text"
                placeholder="Buscar por nombre o código de vendedor..."
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
        </div>

        {/* --- CONTENEDOR DE TARJETAS (CARDS) --- */}
        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 gap-2 text-zinc-400 text-xs font-semibold uppercase tracking-wider">
            <Loader2 className="w-5 h-5 animate-spin text-zinc-500" />
            Cargando vendedores...
          </div>
        ) : filteredVendedores.length === 0 ? (
          <div className="text-center py-20 text-zinc-400 text-xs font-medium border-2 border-dashed border-zinc-200 rounded-2xl bg-zinc-50/50">
            No se encontraron vendedores para los criterios seleccionados.
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
            {filteredVendedores.map((v) => {
              const id = v.id || v.usuarioId;
              const isActive = v.activo !== false;
              const fullName = [v.nombreUsuario, v.apellidosUsuario].filter(Boolean).join(" ").trim() || "Usuario sin nombre";
              const initial = fullName.charAt(0).toUpperCase();
              const codigo = v.codigoVendedor || "Sin código";

              return (
                <div 
                  key={id} 
                  className="group bg-white rounded-2xl border border-zinc-200/80 shadow-sm hover:shadow-md transition-all flex flex-col overflow-hidden relative"
                >
                  {/* Etiqueta superior de estado sutil */}
                  <div className={`absolute top-0 left-0 w-full h-1 ${isActive ? "bg-emerald-400" : "bg-amber-400"}`}></div>
                  
                  <div className="p-5 flex flex-col flex-1">
                    <div className="flex items-start justify-between mb-4">
                      {/* Avatar e Inicial */}
                      <div className="w-10 h-10 rounded-xl bg-zinc-100 text-zinc-700 font-bold text-sm flex items-center justify-center shrink-0 border border-zinc-200/40 uppercase">
                        {initial}
                      </div>

                      {/* Botones de acción (ocultos por defecto, visibles en hover en desktop o siempre en mobile) */}
                      <div className="flex items-center gap-1 opacity-100 sm:opacity-0 group-hover:opacity-100 transition-opacity">
                        <button
                          onClick={() => handleEdit(v)}
                          className="p-1.5 text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 rounded-lg transition-all"
                          title="Editar vendedor"
                        >
                          <Edit2 className="w-3.5 h-3.5 stroke-[2]" />
                        </button>
                        <button
                          onClick={() => handleDelete(id)}
                          className="p-1.5 text-zinc-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all"
                          title="Eliminar vendedor"
                        >
                          <Trash2 className="w-3.5 h-3.5 stroke-[2]" />
                        </button>
                      </div>
                    </div>

                    {/* Información Principal */}
                    <div className="flex-1 space-y-1">
                      <h3 className="font-semibold text-zinc-900 text-sm uppercase truncate pr-2" title={fullName}>
                        {fullName}
                      </h3>
                      <p className="text-xs text-zinc-500 font-medium">
                        Cód: <span className="font-mono tracking-tight font-semibold text-zinc-700">{codigo}</span>
                      </p>
                    </div>
                  </div>

                  {/* Footer de la tarjeta */}
                  <div className="px-5 py-3 border-t border-zinc-100 bg-zinc-50/50 flex justify-between items-center mt-auto">
                    <div className="text-[10px] font-bold tracking-widest uppercase text-zinc-400">
                      ID: {id}
                    </div>
                    <div className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-md text-[9px] font-bold tracking-wide uppercase border ${
                        isActive
                          ? "bg-emerald-50 border-emerald-200/60 text-emerald-700"
                          : "bg-amber-50 border-amber-200/60 text-amber-700"
                    }`}>
                      {isActive ? (
                        <>
                          <UserCheck className="w-2.5 h-2.5 stroke-[2.5]" /> Activo
                        </>
                      ) : (
                        <>
                          <UserX className="w-2.5 h-2.5 stroke-[2.5]" /> Suspendido
                        </>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

      <VendedorModal
        isOpen={isModalOpen}
        onClose={handleClose}
        onSave={handleSave}
        vendedorToEdit={vendedorToEdit}
      />
    </div>
  );
}
