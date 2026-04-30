import React, { useState } from "react";
import CrudGridLayout from "../../layout/CrudGridLayout";
import { toast, Toaster } from "sonner";
import { Layout } from "lucide-react";

import { useAreas } from "../../../hooks/useAreas";
import AreaCard from "../../ui/AreaCard";
import AreaModal from "../../ui/AreaModal";

const GestionAreas = () => {
  const { areas, loading, createArea, updateArea, deleteArea } = useAreas();
  const [searchTerm, setSearchTerm] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedArea, setSelectedArea] = useState(null);

  const filteredAreas = areas.filter((area) =>
    area.nombre?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreate = () => {
    setSelectedArea(null);
    setIsModalOpen(true);
  };

  const handleEdit = (area) => {
    setSelectedArea(area);
    setIsModalOpen(true);
  };

  const handleSave = async (areaData) => {
    try {
      if (selectedArea) {
        const areaId = selectedArea.areaId || selectedArea.id;
        await updateArea(areaId, areaData);
      } else {
        await createArea(areaData);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Está seguro de eliminar esta área?")) {
      try {
        await deleteArea(id);
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <>
      <Toaster position="top-right" richColors />

      <CrudGridLayout
        title="Gestión de Áreas"
        subtitle="Administración de departamentos y áreas funcionales"
        icon={Layout}
        createLabel="Registrar Nueva Área"
        onCreateClick={handleCreate}
        searchPlaceholder="Buscar por nombre de área..."
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        loading={loading}
        items={filteredAreas}
        renderItem={(area, index) => (
          <AreaCard
            key={area.areaId || area.id || `area-${index}`}
            area={area}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        )}
      />

      <AreaModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
        area={selectedArea}
      />
    </>
  );
};

export default GestionAreas;