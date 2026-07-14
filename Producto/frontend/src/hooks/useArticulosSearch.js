import { useState, useEffect, useCallback } from 'react';
import { api, BACKEND_URL } from '../remote/service/api';
import { toast } from 'sonner';

export function useArticulosSearch(tipo) {
    const [articulos, setArticulos] = useState([]);
    const [defaultCategoriaId, setDefaultCategoriaId] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!tipo) return;
        const fetchData = async () => {
            try {
                setLoading(true);
                const [artRes, catRes] = await Promise.all([
                    api.get(`${BACKEND_URL}/api/v1/maestros/articulos/tipo/${tipo}`),
                    api.get(`${BACKEND_URL}/api/v1/maestros/categorias-tela`),
                ]);
                setArticulos(Array.isArray(artRes.data) ? artRes.data : []);
                const cats = Array.isArray(catRes.data) ? catRes.data : [];
                if (cats.length > 0) setDefaultCategoriaId(cats[0].idCategoriaTela);
            } catch (err) {
                console.error('[useArticulosSearch] fetch error:', err);
                setArticulos([]);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [tipo]);

    const search = useCallback((query) => {
        if (!query || !query.trim()) return articulos;
        const q = query.toLowerCase().trim();
        return articulos.filter(a =>
            (a.nombreArticulo || '').toLowerCase().includes(q)
        );
    }, [articulos]);

    const createArticulo = useCallback(async (nombre) => {
        const nombreTrimmed = (nombre || '').trim().toUpperCase();
        if (!nombreTrimmed) return null;

        const prefix = tipo === 'TELA' ? 'TL' : tipo === 'PRENDA_CONFECCIONAR' ? 'PR' : 'AC';
        const codigo = `${prefix}${Date.now()}`.slice(0, 20);

        try {
            setLoading(true);
            const { data } = await api.post(`${BACKEND_URL}/api/v1/maestros/articulos`, {
                codigoArticulo: codigo,
                nombreArticulo: nombreTrimmed,
                tipoArticulo: tipo,
                idCategoriaTela: defaultCategoriaId || 1,
                activo: true,
            });
            setArticulos(prev => [...prev, data]);
            toast.success(`"${nombreTrimmed}" registrado en maestros`);
            return data;
        } catch (err) {
            console.error('[useArticulosSearch] create error:', err);
            toast.warning(`"${nombreTrimmed}" agregado a la solicitud sin registrar en maestros`);
            return null;
        } finally {
            setLoading(false);
        }
    }, [tipo, defaultCategoriaId]);

    return { articulos, loading, search, createArticulo };
}
