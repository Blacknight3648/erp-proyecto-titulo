import { useState, useEffect, useCallback } from 'react';
import { api, BACKEND_URL } from '../remote/service/api';
import { toast } from 'sonner';

/**
 * Hook para gestionar los tipos de logotipo/bordado en la base de datos.
 * Permite buscar tipos existentes y crear nuevos directamente desde el campo.
 */
export function useTiposLogotipoSearch() {
    const [tipos, setTipos] = useState([]);
    const [loading, setLoading] = useState(false);

    // Intentamos cargar desde el endpoint de maestros con tipo LOGOTIPO.
    // Si el backend no tiene ese endpoint aún, usamos una lista local predeterminada.
    useEffect(() => {
        const fetchTipos = async () => {
            try {
                setLoading(true);
                const res = await api.get(`${BACKEND_URL}/api/v3/maestros/articulos/tipo/LOGOTIPO`);
                const data = Array.isArray(res.data) ? res.data : [];
                if (data.length > 0) {
                    setTipos(data.map(t => t.nombreArticulo || t.nombre || t));
                } else {
                    // Fallback: tipos predeterminados mientras la BD está vacía
                    setTipos(['BORDADO', 'ESTAMPADO', 'SUBLIMADO', 'SERIGRAFÍA', 'TRANSFER', 'VINILO', 'LASER', 'BORDADO 3D']);
                }
            } catch {
                // Sin endpoint aún: cargar predeterminados locales
                setTipos(['BORDADO', 'ESTAMPADO', 'SUBLIMADO', 'SERIGRAFÍA', 'TRANSFER', 'VINILO', 'LASER', 'BORDADO 3D']);
            } finally {
                setLoading(false);
            }
        };
        fetchTipos();
    }, []);

    /** Filtra tipos por query */
    const search = useCallback(
        (query) => {
            if (!query || !query.trim()) return tipos;
            const q = query.toLowerCase().trim();
            return tipos.filter(t => t.toLowerCase().includes(q));
        },
        [tipos]
    );

    /** Verifica si ya existe un tipo con ese nombre (case-insensitive) */
    const exists = useCallback(
        (nombre) => tipos.some(t => t.toUpperCase() === (nombre || '').trim().toUpperCase()),
        [tipos]
    );

    /**
     * Crea un nuevo tipo en maestros y lo agrega a la lista local.
     * Si el backend no responde, igual lo agrega localmente.
     */
    const createTipo = useCallback(async (nombre) => {
        const nombreTrimmed = (nombre || '').trim().toUpperCase();
        if (!nombreTrimmed) return null;
        if (exists(nombreTrimmed)) return nombreTrimmed;

        try {
            setLoading(true);
            const codigo = `LG${Date.now()}`.slice(0, 20);
            await api.post(`${BACKEND_URL}/api/v3/maestros/articulos`, {
                codigoArticulo: codigo,
                nombreArticulo: nombreTrimmed,
                tipoArticulo: 'LOGOTIPO',
                activo: true,
            });
            setTipos(prev => [...prev, nombreTrimmed]);
            toast.success(`"${nombreTrimmed}" registrado en maestros`);
            return nombreTrimmed;
        } catch {
            // Igual lo agregamos localmente para que el usuario pueda usarlo
            setTipos(prev =>
                prev.some(t => t === nombreTrimmed) ? prev : [...prev, nombreTrimmed]
            );
            toast.warning(`"${nombreTrimmed}" agregado sin registrar en maestros`);
            return nombreTrimmed;
        } finally {
            setLoading(false);
        }
    }, [exists]);

    return { tipos, loading, search, exists, createTipo };
}
