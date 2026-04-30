import { useState, useCallback, useEffect } from "react";
import { api } from "../remote/service/api";
import { toast } from "sonner";

export const useAreas = () => {
  const [areas, setAreas] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchAreas = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get("/areas");
      setAreas(res.data);
    } catch (error) {
      console.error(error);
      toast.error("No se pudieron cargar las áreas");
    } finally {
      setLoading(false);
    }
  }, []);

  const createArea = async (areaData) => {
    try {
      await api.post("/areas", areaData);
      toast.success("Área creada correctamente");
      fetchAreas();
      return true;
    } catch (error) {
      toast.error("Error al crear área");
      return false;
    }
  };

  const updateArea = async (areaId, areaData) => {
    if (!areaId || areaId === "undefined") {
      console.error("Error: Intentando actualizar área sin ID válido", { areaId, areaData });
      toast.error("No se puede actualizar: ID de área no encontrado");
      return false;
    }
    try {
      await api.put(`/areas/${areaId}`, areaData);
      toast.success("Área actualizada correctamente");
      fetchAreas();
      return true;
    } catch (error) {
      console.error("Error al actualizar área:", error);
      toast.error("Error al actualizar el área");
      return false;
    }
  };

  const deleteArea = async (areaId) => {
    if (!areaId || areaId === "undefined") {
      console.error("Error: Intentando eliminar área sin ID válido", { areaId });
      toast.error("No se puede eliminar: ID de área no encontrado");
      return false;
    }
    try {
      await api.delete(`/areas/${areaId}`);
      toast.success("Área eliminada correctamente");
      fetchAreas();
      return true;
    } catch (error) {
      console.error("Error al eliminar área:", error);
      toast.error("Error al eliminar el área");
      return false;
    }
  };

  useEffect(() => {
    fetchAreas();
  }, [fetchAreas]);

  return {
    areas,
    loading,
    fetchAreas,
    createArea,
    updateArea,
    deleteArea,
  };
};
