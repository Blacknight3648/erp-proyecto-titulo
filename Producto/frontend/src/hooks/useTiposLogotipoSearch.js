import { useState, useEffect, useCallback } from 'react';

export function useTiposLogotipoSearch() {
    const [tipos, setTipos] = useState([]);
    const [loading] = useState(false);

    useEffect(() => {
        setTipos(['BORDADO', 'ESTAMPADO', 'SUBLIMADO', 'SERIGRAFÍA', 'TRANSFER', 'VINILO', 'LASER', 'BORDADO 3D']);
    }, []);

    const search = useCallback(
        (query) => {
            if (!query || !query.trim()) return tipos;
            const q = query.toLowerCase().trim();
            return tipos.filter(t => t.toLowerCase().includes(q));
        },
        [tipos]
    );

    const exists = useCallback(
        (nombre) => tipos.some(t => t.toUpperCase() === (nombre || '').trim().toUpperCase()),
        [tipos]
    );

    const createTipo = useCallback((nombre) => {
        const nombreTrimmed = (nombre || '').trim().toUpperCase();
        if (!nombreTrimmed) return null;
        if (exists(nombreTrimmed)) return nombreTrimmed;

        setTipos(prev =>
            prev.some(t => t === nombreTrimmed) ? prev : [...prev, nombreTrimmed]
        );
        return nombreTrimmed;
    }, [exists]);

    return { tipos, loading, search, exists, createTipo };
}
