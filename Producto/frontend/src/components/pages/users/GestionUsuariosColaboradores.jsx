"use client";

import { useState } from "react";
import { Users } from "lucide-react";
import { Toaster } from "sonner";
import { confirmDelete } from "../../../utils/confirmDelete.jsx";
import CrudGridLayout from "../../layout/CrudGridLayout";

import { useColaboradores } from "../../../hooks/useColaboradores";
import ColaboradorCard from "../../ui/shared/ColaboradorCard";
import ColaboradorModal from "../../ui/shared/ColaboradorModal";

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
      // Inject the ID so the hook can find it
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
    <>
      <Toaster position="top-right" richColors />
      <CrudGridLayout
        title="Gestión de Colaboradores"
        subtitle="Administración de usuarios internos del ERP"
        icon={Users}
        createLabel="Registrar Nuevo Colaborador"
        onCreateClick={handleOpenNew}
        searchPlaceholder="Buscar por nombre, email, rol o área..."
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        filterStatus={filterStatus}
        onFilterChange={setFilterStatus}
        loading={loading}
        items={filteredColaboradores}
        renderItem={(colaborador) => (
          <ColaboradorCard
            key={colaborador.usuarioId || colaborador.id || Math.random().toString()}
            colaborador={colaborador}
            onDelete={handleDelete}
            onToggle={toggleColaborador}
            onEdit={handleOpenEdit}
          />
        )}
      />

      {showModal && (
        <ColaboradorModal
          onClose={() => setShowModal(false)}
          onSave={handleSave}
          collaboratorToEdit={selectedCollaborator}
        />
      )}
    </>
  );
}
