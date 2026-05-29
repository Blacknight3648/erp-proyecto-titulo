"use client";

import { useState } from "react";
import { UserCheck } from "lucide-react";
import { Toaster } from "sonner";
import { confirmDelete } from "../../../utils/confirmDelete.jsx";

import CrudGridLayout from "../../layout/CrudGridLayout";
import { useVendedores } from "../../../hooks/useVendedores";
import VendedorCard from "../../ui/shared/VendedorCard";
import VendedorModal from "../../ui/shared/VendedorModal";

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

  const filteredVendedores = vendedores.filter((v) => {
    const search = searchTerm.toLowerCase();
    const fullName = [v.nombreUsuario, v.apellidosUsuario].filter(Boolean).join(" ").toLowerCase();
    return (
      fullName.includes(search) ||
      v.codigoVendedor?.toLowerCase().includes(search)
    );
  });

  const handleCreate = () => {
    setVendedorToEdit(null);
    setIsModalOpen(true);
  };

  const handleEdit = (vendedor) => {
    setVendedorToEdit(vendedor);
    setIsModalOpen(true);
  };

  const handleSave = async (data) => {
    if (vendedorToEdit?.id) {
      await updateVendedor(vendedorToEdit.id, data);
    } else {
      await createVendedor(data);
    }
    setVendedorToEdit(null);
    setIsModalOpen(false);
  };

  const handleDelete = (id) => {
    confirmDelete("¿Está seguro de eliminar este vendedor?", () => deleteVendedor(id));
  };

  const handleClose = () => {
    setVendedorToEdit(null);
    setIsModalOpen(false);
  };

  return (
    <>
      <Toaster position="top-right" richColors />

      <CrudGridLayout
        title="Gestión de Vendedores"
        subtitle="Administración del equipo de ventas"
        icon={UserCheck}
        createLabel="Registrar Nuevo Vendedor"
        onCreateClick={handleCreate}
        searchPlaceholder="Buscar por nombre o código de vendedor..."
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        loading={loading}
        items={filteredVendedores}
        renderItem={(vendedor) => (
          <VendedorCard
            key={vendedor.id}
            vendedor={vendedor}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        )}
      />

      <VendedorModal
        isOpen={isModalOpen}
        onClose={handleClose}
        onSave={handleSave}
        vendedorToEdit={vendedorToEdit}
      />
    </>
  );
}
