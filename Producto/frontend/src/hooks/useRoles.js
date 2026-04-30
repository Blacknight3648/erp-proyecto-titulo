import { useState, useCallback, useEffect } from "react";
import { api } from "../remote/service/api";
import { toast } from "sonner";

export const useRoles = () => {
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchRoles = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get("/roles");
      setRoles(res.data);
    } catch (error) {
      console.error(error);
      toast.error("No se pudieron cargar los roles");
    } finally {
      setLoading(false);
    }
  }, []);

  const createRole = async (roleData) => {
    try {
      await api.post("/roles", roleData);
      toast.success("Rol creado correctamente");
      fetchRoles();
      return true;
    } catch (error) {
      toast.error("Error al crear rol");
      return false;
    }
  };

  const updateRole = async (id, roleData) => {
    try {
      await api.put(`/roles/${id}`, roleData);
      toast.success("Rol actualizado");
      fetchRoles();
      return true;
    } catch (error) {
      toast.error("Error al actualizar rol");
      return false;
    }
  };

  const deleteRole = async (id) => {
    try {
      await api.delete(`/roles/${id}`);
      toast.success("Rol eliminado");
      fetchRoles();
      return true;
    } catch (error) {
      toast.error("Error al eliminar rol");
      return false;
    }
  };

  const assignPermissions = async (roleId, permissionIds) => {
    try {
      await api.put(`/roles/${roleId}/permisos`, permissionIds);
      toast.success("Permisos asignados correctamente");
      return true;
    } catch (error) {
      toast.error("Error al asignar permisos");
      return false;
    }
  };

  useEffect(() => {
    fetchRoles();
  }, [fetchRoles]);

  return {
    roles,
    loading,
    fetchRoles,
    createRole,
    updateRole,
    deleteRole,
    assignPermissions,
  };
};
