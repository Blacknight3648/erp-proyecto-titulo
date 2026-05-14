import { useState, useEffect } from "react";
import { toast } from "sonner";
import { api } from "../remote/service/api";

export function useSiglas() {
   const [siglas, setSiglas] = useState([]);
   const [loading, setLoading] = useState(true);

   const loadSiglas = async () => {
      try {
         const response = await api.get("/siglas");
         setSiglas(Array.isArray(response.data) ? response.data : []);
      } catch (error) {
         console.error("Error al cargar siglas:", error);
         if (error.response?.status !== 404) {
            toast.error("No se pudo cargar la lista de siglas");
         }
         setSiglas([]);
      } finally {
         setLoading(false);
      }
   };

   useEffect(() => {
      loadSiglas();
   }, []);

   return { siglas, loading, reload: loadSiglas };
}
