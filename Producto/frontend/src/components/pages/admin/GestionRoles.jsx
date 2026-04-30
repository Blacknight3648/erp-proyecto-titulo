import React, { useState } from "react";
import CrudGridLayout from "../../layout/CrudGridLayout";
import { toast, Toaster } from "sonner";
import { ShieldCheck } from "lucide-react";

import { useRoles } from "../../../hooks/useRoles";
import RolCard from "../../ui/RolCard";
import RolModal from "../../ui/RolModal";
import { useNavigate } from "react-router-dom";

const GestionRoles = () => {
  const { roles, loading, createRole, updateRole, deleteRole } = useRoles();
  const [searchTerm, setSearchTerm] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedRol, setSelectedRol] = useState(null);
  const navigate = useNavigate();

  const filteredRoles = roles.filter((rol) =>
    rol.nombre?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreate = () => {
    setSelectedRol(null);
    setIsModalOpen(true);
  };

  const handleEdit = (rol) => {
    setSelectedRol(rol);
    setIsModalOpen(true);
  };

  const handleSave = async (rolData) => {
    if (selectedRol) {
      await updateRole(selectedRol.id, rolData);
    } else {
      await createRole(rolData);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Está seguro de eliminar este rol?")) {
      await deleteRole(id);
    }
  };

  const handleManagePermissions = (rol) => {
    navigate(`/admin/roles/${rol.id}/permisos`);
  };

  return (
    <>
      <Toaster position="top-right" richColors />
      
      <CrudGridLayout
        title="Gestión de Roles"
        subtitle="Administración de perfiles y niveles de acceso"
        icon={ShieldCheck}
        createLabel="Registrar Nuevo Rol"
        onCreateClick={handleCreate}
        searchPlaceholder="Buscar por nombre de rol..."
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        loading={loading}
        items={filteredRoles}
        renderItem={(rol) => (
          <RolCard
            key={rol.id}
            rol={rol}
            onEdit={handleEdit}
            onDelete={handleDelete}
            onManagePermissions={handleManagePermissions}
          />
        )}
      />

      <RolModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
        rol={selectedRol}
      />
    </>
  );
};

export default GestionRoles;
