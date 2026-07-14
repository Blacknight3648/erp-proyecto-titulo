import { useState, useCallback, useEffect } from "react";
import { api } from "../remote/service/api";
import { toast } from "sonner";

export const usePermisos = () => {
  const [permisos, setPermisos] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchPermisos = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get("/permisos");
      setPermisos(res.data);
    } catch (error) {
      console.error(error);
      toast.error("No se pudieron cargar los permisos");
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchPermisosPorRol = useCallback(async (roleId) => {
    try {
      const res = await api.get(`/roles/${roleId}/permisos`);
      return res.data;
    } catch (error) {
      toast.error("Error al cargar permisos del rol");
      return [];
    }
  }, []);

  const createPermiso = async (data) => {
    try {
      const res = await api.post("/permisos", data);
      setPermisos((prev) => [...prev, res.data]);
      return res.data;
    } catch (error) {
      console.error(error);
      toast.error("Error al crear el permiso");
      throw error;
    }
  };

  const updatePermiso = async (id, data) => {
    try {
      const res = await api.put(`/permisos/${id}`, data);
      setPermisos((prev) => prev.map((p) => (p.id === id ? res.data : p)));
      return res.data;
    } catch (error) {
      console.error(error);
      toast.error("Error al actualizar el permiso");
      throw error;
    }
  };

  const deletePermiso = async (id) => {
    try {
      await api.delete(`/permisos/${id}`);
      setPermisos((prev) => prev.filter((p) => p.id !== id));
    } catch (error) {
      console.error(error);
      toast.error("Error al eliminar el permiso");
      throw error;
    }
  };

  useEffect(() => {
    fetchPermisos();
  }, [fetchPermisos]);

  return {
    permisos,
    loading,
    fetchPermisos,
    fetchPermisosPorRol,
    createPermiso,
    updatePermiso,
    deletePermiso
  };
};
