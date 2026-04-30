import { useState, useCallback, useEffect } from 'react';
import { api } from '../remote/service/api';

export const FIELD_LABELS = {
    nombre:             'Título Especificación',
    descripcion:        'Descripción General',
    nombrePrenda:       'Nombre de Prenda',
    forro:              'Forro',
    relleno:            'Relleno',
    gorro:              'Gorro',
    cuello:             'Cuello',
    abotonaduraCierre:  'Abotonadura / Cierre',
    cortesAplicaciones: 'Cortes y Aplicaciones',
    fuelles:            'Fuelles',
    mangas:             'Mangas',
    pretinasRuedo:      'Pretinas / Ruedo',
    bolsillos:          'Bolsillos',
    cintaDetalle:       'Cinta Detalle',
    logoDetalle:        'Logo Detalle',
    colorForro:         'Color Forro',
    accesoriosDetalle:  'Accesorios Detalle',
    obsModelo:          'Obs. del Modelo',
    tallaje:            'Tallaje',
};

export const ALL_FIELDS = new Set(Object.keys(FIELD_LABELS));

export function usePlantillas() {
    const [cache, setCache] = useState({});
    const [plantillas, setPlantillas] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchAll = useCallback(async () => {
        try {
            setLoading(true);
            const { data } = await api.get('/configuracion-plantillas');
            setPlantillas(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error("[usePlantillas] Error fetching all:", err);
            setError(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchAll();
    }, [fetchAll]);

    const getCamposForArticulo = useCallback(async (articuloDescripcion = '') => {
        const tipo = (articuloDescripcion || '').split(' - ')[0].trim().toUpperCase();

        if (!tipo) return new Set(ALL_FIELDS);
        if (cache[tipo]) return cache[tipo];

        // Intento encontrar en las plantillas ya cargadas antes de llamar al API
        const localMatch = plantillas.find(p => p.nombrePrenda?.toUpperCase() === tipo);
        if (localMatch?.camposActivos) {
            const campos = new Set(localMatch.camposActivos);
            setCache(prev => ({ ...prev, [tipo]: campos }));
            return campos;
        }

        try {
            setLoading(true);
            const { data } = await api.get(
                `/configuracion-plantillas/by-nombre`,
                { params: { nombre: tipo } }
            );

            const campos = data?.camposActivos
                ? new Set(data.camposActivos)
                : new Set(ALL_FIELDS);

            setCache(prev => ({ ...prev, [tipo]: campos }));
            return campos;
        } catch (err) {
            console.error(`[usePlantillas] Error al obtener campos para "${tipo}":`, err);
            return new Set(ALL_FIELDS);
        } finally {
            setLoading(false);
        }
    }, [cache, plantillas]);

    const save = useCallback(async (dto) => {
        try {
            setLoading(true);
            const { data } = await api.post('/configuracion-plantillas', dto);
            setPlantillas(prev => {
                const index = prev.findIndex(p => p.id === data.id);
                if (index >= 0) {
                    const next = [...prev];
                    next[index] = data;
                    return next;
                }
                return [...prev, data];
            });
            // Limpiar cache para forzar recarga de campos
            if (dto.nombrePrenda) {
                setCache(prev => {
                    const next = { ...prev };
                    delete next[dto.nombrePrenda.toUpperCase()];
                    return next;
                });
            }
            return data;
        } catch (err) {
            console.error("[usePlantillas] Error saving:", err);
            throw err;
        } finally {
            setLoading(false);
        }
    }, []);

    const remove = useCallback(async (id) => {
        try {
            setLoading(true);
            await api.delete(`/configuracion-plantillas/${id}`);
            setPlantillas(prev => prev.filter(p => p.id !== id));
            setCache({}); // Limpiar cache general
        } catch (err) {
            console.error("[usePlantillas] Error deleting:", err);
            throw err;
        } finally {
            setLoading(false);
        }
    }, []);

    return {
        getCamposForArticulo,
        plantillas,
        configuraciones: plantillas, // Alias para compatibilidad con GestionPlantillas
        save,
        remove,
        loading,
        error
    };
}
