import { useState, useEffect } from "react";
import { toast } from "sonner";
import { api } from "../remote/service/api";

export const useProveedores = () => {
   const [proveedores, setProveedores] = useState([]);
   const [loading, setLoading] = useState(true);

   const loadProveedores = async () => {
      try {
         const response = await api.get("/proveedores");
         const mapped = (response.data || []).map(p => {
            const id = p.proveedorId || p.id;
            const nombre = p.razonSocialProveedor || p.nombreProveedor || p.nombre || '';
            const contacto = p.contactos?.[0];
            const dir = p.direccion?.[0];
            return {
               ...p,
               id: id,
               proveedorId: id,
               nombreProveedor: nombre,
               nombre: nombre,
               razonSocialProveedor: nombre,
               emailProveedor: p.emailProveedor || contacto?.emailContacto || "",
               telefonoProveedor: p.telefonoProveedor || contacto?.telefonoContacto || "",
               contactoProveedor: p.contactoProveedor || contacto?.nombreContacto || "",
               direccionProveedor: p.direccionProveedor || (dir ? `${dir.calle} ${dir.numero || ""}`.trim() : "")
            };
         });
         setProveedores(mapped);
      } catch (error) {
         console.error("Error al cargar proveedores:", error);
         toast.error("Error de conexión al cargar la lista de proveedores");
      } finally {
         setLoading(false);
      }
   };

   const buildPayload = (proveedor) => {
      const email = proveedor.emailProveedor || proveedor.email || "";
      const telefono = proveedor.telefonoProveedor || proveedor.telefono || "";
      const contactoNombre = proveedor.contactoProveedor || proveedor.contacto || "";
      const calle = proveedor.direccionProveedor || "Sin dirección";
      const existingContacto = proveedor.contactos?.[0];
      const existingDireccion = proveedor.direccion?.[0];

      return {
         runProveedor: proveedor.runProveedor,
         razonSocialProveedor: proveedor.razonSocialProveedor,
         tipoProveedor: proveedor.tipoProveedor || "",
         horarioAtencion: proveedor.horarioAtencion || "",
         sigla: typeof proveedor.sigla === "string" ? proveedor.sigla : (proveedor.sigla?.nombre || ""),
         activo: proveedor.activo ?? true,
         giro: proveedor.giro?.giroId ? { giroId: proveedor.giro.giroId } : null,
         contactos: [
            {
               ...(existingContacto?.idContacto ? { idContacto: existingContacto.idContacto } : {}),
               nombreContacto: contactoNombre,
               telefonoContacto: telefono,
               emailContacto: email,
               tipoContacto: existingContacto?.tipoContacto || { tipoContactoId: 1 }
            }
         ],
         direccion: [
            {
               ...(existingDireccion?.direccionId ? { direccionId: existingDireccion.direccionId } : {}),
               calle: calle,
               numero: "S/N",
               tipoDireccion: existingDireccion?.tipoDireccion || { tipoDireccionId: 1 },
               comuna: existingDireccion?.comuna || { comunaId: 1 }
            }
         ]
      };
   };

   const createProveedor = async (proveedor) => {
      try {
         await api.post("/proveedores", buildPayload(proveedor));
         await loadProveedores();
         toast.success("Proveedor registrado exitosamente");
      } catch (error) {
         console.error("Error al registrar proveedor:", error);
         const msg = error.response?.data?.message || "Error al registrar el proveedor. Verifique los datos.";
         toast.error(msg);
      }
   };

   const updateProveedor = async (proveedor) => {
      const id = proveedor.proveedorId || proveedor.id;
      if (!id) {
         toast.error("No se puede actualizar: ID de proveedor no encontrado");
         return;
      }
      try {
         await api.put(`/proveedores/${id}`, buildPayload({ ...proveedor, proveedorId: id }));
         await loadProveedores();
         toast.success("Información del proveedor actualizada");
      } catch (error) {
         console.error("Error al actualizar proveedor:", error);
         const msg = error.response?.data?.message || "Error al actualizar los datos del proveedor";
         toast.error(msg);
      }
   };

   const deleteProveedor = async (id) => {
      if (!id) {
         toast.error("No se puede eliminar: ID de proveedor no encontrado");
         return;
      }
      try {
         await api.delete(`/proveedores/${id}`);
         setProveedores(prev => prev.filter(p => p.proveedorId !== id));
         toast.success("Proveedor eliminado correctamente");
      } catch (error) {
         console.error("Error al eliminar proveedor:", error);
         toast.error("No se pudo eliminar el proveedor");
      }
   };

   const toggleProveedor = async (proveedor) => {
      const id = proveedor.proveedorId || proveedor.id;
      if (!id) {
         toast.error("No se puede cambiar estado: ID de proveedor no encontrado");
         return;
      }
      try {
         await api.put(`/proveedores/${id}`, buildPayload({ ...proveedor, proveedorId: id, activo: !proveedor.activo }));
         await loadProveedores();
         toast.success(`Proveedor ${!proveedor.activo ? "activado" : "suspendido"} con éxito`);
      } catch (error) {
         console.error("Error al cambiar estado del proveedor:", error);
         toast.error("No se pudo cambiar el estado del proveedor");
      }
   };

   useEffect(() => {
      loadProveedores();
   }, []);

   return {
      proveedores,
      loading,
      createProveedor,
      updateProveedor,
      deleteProveedor,
      toggleProveedor,
   };
};
