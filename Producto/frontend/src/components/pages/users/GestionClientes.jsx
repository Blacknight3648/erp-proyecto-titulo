"use client";

import { useState } from "react";
import { Users } from "lucide-react";
import { Toaster } from "sonner";

import CrudGridLayout from "../../layout/CrudGridLayout";
import { useClientes } from "../../../hooks/useClientes";
import ClienteCard from "../../ui/shared/ClienteCard";
import ClienteModal from "../../ui/shared/ClienteModal";

import { normalizeCliente } from "../../../utils/helpers/normalizeCliente";

export default function GestionClientes() {

  const {
    clientes,
    loading,
    createCliente,
    updateCliente,
    deleteCliente,
    toggleCliente
  } = useClientes();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [clienteToEdit, setClienteToEdit] = useState(null);

  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("Todos");

  const filteredClientes = clientes.filter((c) => {

    const search = searchTerm.toLowerCase();

    const matchesSearch =
      c.razonSocial?.toLowerCase()?.includes(search) ||
      c.runCliente?.toLowerCase()?.includes(search) ||
      c.sigla?.descripcionSigla?.toLowerCase()?.includes(search) ||
      c.sigla?.siglaAbreviatura?.toLowerCase()?.includes(search) ||
      c.giro?.descripcionGiro?.toLowerCase()?.includes(search);

    const status = c.activo ? "Activo" : "Suspendido";

    const matchesFilter =
      filterStatus === "Todos" || filterStatus === status;

    return matchesSearch && matchesFilter;

  });

  const handleCreate = () => {

    setClienteToEdit(null);
    setIsModalOpen(true);

  };

  const handleEdit = (cliente) => {

    setClienteToEdit(cliente);
    setIsModalOpen(true);

  };

  const handleSaveCliente = (data) => {

    const normalizedData = normalizeCliente(data);

    if (clienteToEdit?.clienteId) {

      updateCliente({
        ...normalizedData,
        clienteId: clienteToEdit.clienteId
      });

    } else {

      createCliente(normalizedData);

    }

    setClienteToEdit(null);
    setIsModalOpen(false);

  };

  const handleCloseModal = () => {

    setClienteToEdit(null);
    setIsModalOpen(false);

  };

  return (
    <>
      <Toaster position="top-right" richColors />

      <CrudGridLayout
        title="Gestión Maestro de Clientes"
        subtitle="Directorio Central y Control de Cuentas"
        icon={Users}
        createLabel="Registrar Nuevo Cliente"
        onCreateClick={handleCreate}
        searchPlaceholder="Buscar por razón social, RUN, sigla o giro..."
        searchTerm={searchTerm}
        onSearchChange={(value) => setSearchTerm(value)}
        filterStatus={filterStatus}
        onFilterChange={(value) => setFilterStatus(value)}
        loading={loading}
        items={filteredClientes}
        renderItem={(cliente) => (
          <ClienteCard
            key={cliente.clienteId}
            cliente={cliente}
            onDelete={deleteCliente}
            onToggle={toggleCliente}
            onEdit={handleEdit}
          />
        )}
      />

      {isModalOpen && (
        <ClienteModal
          onClose={handleCloseModal}
          onSave={handleSaveCliente}
          clienteToEdit={clienteToEdit}
        />
      )}
    </>
  );
}