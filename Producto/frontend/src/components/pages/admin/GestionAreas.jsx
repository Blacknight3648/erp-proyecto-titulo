import React, { useState, useMemo } from "react";
import { Layout, Plus } from "lucide-react";
import { Toaster } from "sonner";

import CrudGridLayout from "../../layout/CrudGridLayout";
import { useAreas } from "../../../hooks/useAreas";
import AreaCard from "../../ui/shared/AreaCard";
import AreaModal from "../../ui/shared/AreaModal";

const GestionAreas = () => {
  const { areas, loading, createArea, updateArea, deleteArea } = useAreas();
  const [searchTerm, setSearchTerm] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedArea, setSelectedArea] = useState(null);

  // Optimizamos el filtrado con useMemo para evitar cálculos innecesarios en cada render
  const filteredAreas = useMemo(() => {
    return areas.filter((area) =>
      area.nombre?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [areas, searchTerm]);

  const handleOpenModal = (area = null) => {
    setSelectedArea(area);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedArea(null);
  };

  const handleSave = async (areaData) => {
    try {
      if (selectedArea) {
        const areaId = selectedArea.areaId || selectedArea.id;
        await updateArea(areaId, areaData);
      } else {
        await createArea(areaData);
      }
      handleCloseModal();
    } catch (error) {
      // El manejo de errores ya debería estar en el hook, 
      // pero aquí puedes capturar casos específicos.
      console.error("Error al guardar:", error);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50/50 p-4 md:p-8">
      <Toaster position="top-right" richColors closeButton />

      <CrudGridLayout
        title="Gestión de Áreas"
        subtitle="Administración de departamentos y áreas funcionales de la organización"
        icon={Layout}
        createLabel="Nueva Área"
        onCreateClick={() => handleOpenModal()}
        searchPlaceholder="Buscar por nombre de área..."
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        loading={loading}
        items={filteredAreas}
        // Agregamos una prop de espaciado si tu layout lo permite
        className="gap-6" 
        renderItem={(area) => (
          <AreaCard
            key={area.areaId || area.id}
            area={area}
            onEdit={() => handleOpenModal(area)}
            onDelete={() => deleteArea(area.areaId || area.id)}
          />
        )}
      />

      <AreaModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSave={handleSave}
        area={selectedArea}
      />
    </div>
  );
};

export default GestionAreas;