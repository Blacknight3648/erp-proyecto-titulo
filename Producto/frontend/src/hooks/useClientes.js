import { useState, useEffect } from "react";
import { toast } from "sonner";
import { api } from "../remote/service/api";

export function useClientes() {

   const [clientes, setClientes] = useState([]);
   const [loading, setLoading] = useState(true);

   const loadClientes = async () => {
      try {
         const response = await api.get("/clientes");
         const mapped = (response.data || []).map(c => {
            const id = c.clienteId || c.id;
            const contacto = c.contactos?.[0];
            const direccion = c.direcciones?.[0];
            return {
               ...c,
               clienteId: id,
               id: id,
               correoCliente: c.correoCliente || contacto?.emailContacto || "",
               telefonoCliente: c.telefonoCliente || contacto?.telefonoContacto || "",
               contactoCliente: c.contactoCliente || contacto?.nombreContacto || "",
               direccionCliente: c.direccionCliente || (direccion ? `${direccion.calle} ${direccion.numero || ""}`.trim() : "")
            };
         });
         setClientes(mapped);
      } catch (error) {
         console.error("Error al cargar clientes:", error);
         toast.error("No se pudo conectar con el servidor para cargar clientes");
      } finally {
         setLoading(false);
      }
   };

   const buildPayload = (cliente) => {
      const email = cliente.correoCliente || cliente.correo || "";
      const telefono = cliente.telefonoCliente || cliente.telefono || "";
      const contactoNombre = cliente.contactoCliente || cliente.contacto || "";
      const calle = cliente.direccionCliente || "Sin dirección";
      const existingContacto = cliente.contactos?.[0];
      const existingDireccion = cliente.direcciones?.[0];

      return {
         runCliente: cliente.runCliente,
         razonSocial: cliente.razonSocial,
         sigla: typeof cliente.sigla === "string" ? cliente.sigla : (cliente.sigla?.nombre || ""),
         activo: cliente.activo ?? true,
         giro: cliente.giro?.giroId ? { giroId: cliente.giro.giroId } : null,
         contactos: [
            {
               ...(existingContacto?.idContacto ? { idContacto: existingContacto.idContacto } : {}),
               nombreContacto: contactoNombre,
               telefonoContacto: telefono,
               emailContacto: email,
               tipoContacto: existingContacto?.tipoContacto || { tipoContactoId: 1 }
            }
         ],
         direcciones: [
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

   const createCliente = async (cliente) => {
      try {
         await api.post("/clientes", buildPayload(cliente));
         await loadClientes();
         toast.success("Cliente registrado con éxito");
      } catch (error) {
         console.error("Error al crear cliente:", error);
         const msg = error.response?.data?.message || "Error al registrar el cliente. Verifique los datos.";
         toast.error(msg);
      }
   };

   const updateCliente = async (cliente) => {
      const id = cliente.clienteId || cliente.id;
      if (!id) {
         toast.error("No se puede actualizar: ID de cliente no encontrado");
         return;
      }
      try {
         await api.put(`/clientes/${id}`, buildPayload({ ...cliente, clienteId: id }));
         await loadClientes();
         toast.success("Información del cliente actualizada");
      } catch (error) {
         console.error("Error al actualizar cliente:", error);
         const msg = error.response?.data?.message || "No se pudo actualizar la información del cliente";
         toast.error(msg);
      }
   };

   const deleteCliente = async (id) => {
      if (!id) {
         toast.error("No se puede eliminar: ID de cliente no encontrado");
         return;
      }
      try {
         await api.delete(`/clientes/${id}`);
         setClientes(prev => prev.filter(c => c.clienteId !== id));
         toast.success("Cliente eliminado exitosamente");
      } catch (error) {
         console.error("Error al eliminar cliente:", error);
         toast.error("No se pudo eliminar el cliente seleccionado");
      }
   };

   const toggleCliente = async (cliente) => {
      const id = cliente.clienteId || cliente.id;
      if (!id) {
         toast.error("No se puede cambiar estado: ID de cliente no encontrado");
         return;
      }
      try {
         await api.put(`/clientes/${id}`, buildPayload({ ...cliente, clienteId: id, activo: !cliente.activo }));
         await loadClientes();
         toast.success(`Cliente ${!cliente.activo ? "activado" : "desactivado"} correctamente`);
      } catch (error) {
         console.error("Error al cambiar estado del cliente:", error);
         toast.error("Error al cambiar el estado de activación del cliente");
      }
   };

   useEffect(() => {
      loadClientes();
   }, []);

   return {
      clientes,
      loading,
      createCliente,
      updateCliente,
      deleteCliente,
      toggleCliente,
   };
}
