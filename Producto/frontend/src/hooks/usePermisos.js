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

  const fetchPermisosPorRol = async (roleId) => {
    try {
      const res = await api.get(`/roles/${roleId}/permisos`);
      return res.data;
    } catch (error) {
      toast.error("Error al cargar permisos del rol");
      return [];
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
  };
};
