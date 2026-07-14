import { useState, useEffect, useCallback } from 'react';
import { api } from '../remote/service/api';
import { toast } from 'sonner';

export function useModelosSearch() {
    const [modelos, setModelos] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const { data } = await api.get(`/maestros/modelos`);
                setModelos(Array.isArray(data) ? data : []);
            } catch (err) {
                console.error('[useModelosSearch] fetch error:', err);
                setModelos([]);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const search = useCallback((query) => {
        if (!query || !query.trim()) return modelos;
        const q = query.toLowerCase().trim();
        return modelos.filter(m => (m.nombreModelo || '').toLowerCase().includes(q));
    }, [modelos]);

    const createModelo = useCallback(async (nombre) => {
        const nombreTrimmed = (nombre || '').trim().toUpperCase();
        if (!nombreTrimmed) return null;

        try {
            setLoading(true);
            const { data } = await api.post(`/maestros/modelos`, {
                nombreModelo: nombreTrimmed,
            });
            setModelos(prev => [...prev, data]);
            toast.success(`"${nombreTrimmed}" registrado en maestros`);
            return data;
        } catch (err) {
            console.error('[useModelosSearch] create error:', err);
            toast.warning(`"${nombreTrimmed}" agregado a la solicitud sin registrar en maestros`);
            return null;
        } finally {
            setLoading(false);
        }
    }, []);

    return { modelos, loading, search, createModelo };
}
