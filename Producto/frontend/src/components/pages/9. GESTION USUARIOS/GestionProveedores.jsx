"use client";

import { useState } from "react";
import { Truck } from "lucide-react";
import { Toaster } from "sonner";

import CrudGridLayout from "../../layout/CrudGridLayout";
import { useProveedores } from "../../../hooks/useProveedores";
import ProveedorCard from "../../ui/ProveedorCard";
import ProveedorModal from "../../ui/ProveedorModal";

import { normalizeProveedor } from "../../../utils/helpers/normalizeProveedor";

export default function ProveedoresView() {

    const {
        proveedores,
        loading,
        createProveedor,
        updateProveedor,
        deleteProveedor,
        toggleProveedor
    } = useProveedores();

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [proveedorToEdit, setProveedorToEdit] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [filterStatus, setFilterStatus] = useState("Todos");

    // Filtrado de proveedores
    const filteredProveedores = proveedores.filter((p) => {

        const search = searchTerm.toLowerCase();

        const matchesSearch =
            p.nombreProveedor?.toLowerCase()?.includes(search) ||
            p.rutProveedor?.toLowerCase()?.includes(search) ||
            p.categoria?.toLowerCase()?.includes(search);

        const status = p.activo ? "Activo" : "Suspendido";

        const matchesFilter =
            filterStatus === "Todos" || filterStatus === status;

        return matchesSearch && matchesFilter;
    });

    // Abrir modal crear
    const handleCreate = () => {
        setProveedorToEdit(null);
        setIsModalOpen(true);
    };

    // Abrir modal editar
    const handleEdit = (proveedor) => {
        setProveedorToEdit(proveedor);
        setIsModalOpen(true);
    };

    // Guardar proveedor
    const handleSaveProveedor = (proveedorData) => {

        const normalizedData = normalizeProveedor(proveedorData);

        if (proveedorToEdit) {

            updateProveedor({
                ...normalizedData,
                proveedorId: proveedorToEdit.proveedorId
            });

        } else {

            createProveedor(normalizedData);

        }

        setProveedorToEdit(null);
        setIsModalOpen(false);
    };

    // Cerrar modal
    const handleCloseModal = () => {
        setProveedorToEdit(null);
        setIsModalOpen(false);
    };

    return (
        <>
            <Toaster richColors position="top-right" />

            <CrudGridLayout
                title="Proveedores"
                subtitle="Directorio Central y Control de Cuentas"
                icon={Truck}
                createLabel="Registrar Nuevo Proveedor"
                onCreateClick={handleCreate}
                searchPlaceholder="Buscar por nombre, RUT o categoría..."
                searchTerm={searchTerm}
                onSearchChange={(value) => setSearchTerm(value)}
                filterStatus={filterStatus}
                onFilterChange={(value) => setFilterStatus(value)}
                loading={loading}
                items={filteredProveedores}
                renderItem={(proveedor) => (
                    <ProveedorCard
                        key={proveedor.proveedorId}
                        proveedor={proveedor}
                        onDelete={deleteProveedor}
                        onToggle={toggleProveedor}
                        onEdit={handleEdit}
                    />
                )}
            />

            {isModalOpen && (
                <ProveedorModal
                    onClose={handleCloseModal}
                    onSave={handleSaveProveedor}
                    proveedorToEdit={proveedorToEdit}
                />
            )}
        </>
    );
}