import { useState, useEffect } from "react";
import { toast } from "sonner";
import { api } from "../remote/service/api";

export function useGiros() {
   const [giros, setGiros] = useState([]);
   const [loading, setLoading] = useState(true);

   const loadGiros = async () => {
      try {
         const response = await api.get("/giros");
         setGiros(Array.isArray(response.data) ? response.data : []);
      } catch (error) {
         console.error("Error al cargar giros:", error);
         if (error.response?.status !== 404) {
            toast.error("No se pudo cargar la lista de giros");
         }
         setGiros([]);
      } finally {
         setLoading(false);
      }
   };

   useEffect(() => {
      loadGiros();
   }, []);

   return { giros, loading, reload: loadGiros };
}
