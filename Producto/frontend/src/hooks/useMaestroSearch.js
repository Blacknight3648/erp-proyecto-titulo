import { useState, useEffect, useCallback } from 'react';
import { api, BACKEND_URL } from '../remote/service/api';
import { toast } from 'sonner';

/**
 * Hook genérico para campos de autocompletado con maestros.
 * Carga opciones desde /maestros/articulos/tipo/{tipo} y permite crear nuevas.
 * Si el endpoint no responde o no hay datos, usa la lista de fallback provista.
 *
 * @param {string} tipo            - Tipo de artículo en maestros (e.g. "LOGO_NOMBRE")
 * @param {string[]} fallbackOpts  - Opciones por defecto si la BD está vacía
 */
export function useMaestroSearch(tipo, fallbackOpts = []) {
  const [items, setItems]   = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!tipo) return;
    const fetch = async () => {
      try {
        setLoading(true);
        const res  = await api.get(`${BACKEND_URL}/api/v3/maestros/articulos/tipo/${tipo}`);
        const data = Array.isArray(res.data) ? res.data : [];
        const names = data.map(d => d.nombreArticulo || d.nombre || d).filter(Boolean);
        setItems(names.length > 0 ? names : fallbackOpts);
      } catch {
        setItems(fallbackOpts);
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, [tipo]);  // eslint-disable-line react-hooks/exhaustive-deps

  const search = useCallback(
    (query) => {
      if (!query?.trim()) return items;
      const q = query.toLowerCase().trim();
      return items.filter(t => t.toLowerCase().includes(q));
    },
    [items]
  );

  const exists = useCallback(
    (nombre) =>
      items.some(t => t.toUpperCase() === (nombre || '').trim().toUpperCase()),
    [items]
  );

  const create = useCallback(
    async (nombre) => {
      const val = (nombre || '').trim().toUpperCase();
      if (!val || exists(val)) return val;
      try {
        setLoading(true);
        const codigo = `${tipo.slice(0, 4)}${Date.now()}`.slice(0, 20);
        await api.post(`${BACKEND_URL}/api/v3/maestros/articulos`, {
          codigoArticulo: codigo,
          nombreArticulo: val,
          tipoArticulo:   tipo,
          activo: true,
        });
        setItems(prev => [...prev, val]);
        toast.success(`"${val}" registrado en maestros`);
      } catch {
        setItems(prev => prev.includes(val) ? prev : [...prev, val]);
        toast.warning(`"${val}" agregado sin registrar en maestros`);
      } finally {
        setLoading(false);
      }
      return val;
    },
    [tipo, exists]
  );

  return { items, loading, search, exists, create };
}
