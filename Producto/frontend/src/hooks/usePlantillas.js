import { useState, useCallback, useEffect } from 'react';
import { api, BACKEND_URL } from '../remote/service/api';
import { toast } from 'sonner';

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
            const { data } = await api.get(`${BACKEND_URL}/api/v3/maestros/articulos/activos`);
            const mapped = (Array.isArray(data) ? data : []).map(a => ({
                id: a.idArticulo,
                idArticulo: a.idArticulo,
                nombrePrenda: a.nombreArticulo,
                nombreArticulo: a.nombreArticulo,
                camposActivos: [],
                plantillaTelas: [],
                plantillaAccesorios: []
            }));
            setPlantillas(mapped);
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

        const articulo = plantillas.find(p => p.nombre?.toUpperCase() === tipo || p.nombreArticulo?.toUpperCase() === tipo);
        if (!articulo || !articulo.idArticulo) return new Set(ALL_FIELDS);

        try {
            setLoading(true);
            const { data } = await api.get(`${BACKEND_URL}/api/v3/comercial/modelos-plantilla/articulo/${articulo.idArticulo}`);

            // El endpoint ahora devuelve UN objeto { idArticulo, nombreArticulo, camposPlantilla:[...] }.
            const lista = data?.camposPlantilla ?? [];
            if (lista.length > 0) {
                const campos = new Set(lista);
                setCache(prev => ({ ...prev, [tipo]: campos }));
                return campos;
            }
            return new Set(ALL_FIELDS);
        } catch (err) {
            // 404 (artículo sin configuración) → se usan todos los campos por defecto.
            if (err.response?.status !== 404) {
                console.error(`[usePlantillas] Error al obtener campos para "${tipo}":`, err);
            }
            return new Set(ALL_FIELDS);
        } finally {
            setLoading(false);
        }
    }, [cache, plantillas]);

    const save = useCallback(async (dto) => {
        try {
            setLoading(true);

            let idArticulo = dto.id;
            let nombreArticulo = dto.nombrePrenda;
            if (!idArticulo && nombreArticulo) {
                const found = plantillas.find(p => p.nombreArticulo?.toUpperCase() === nombreArticulo.toUpperCase());
                if (found) {
                    idArticulo = found.idArticulo || found.id;
                } else {
                    toast.error(`El artículo "${nombreArticulo}" no existe en el maestro de artículos.`);
                    return null;
                }
            }

            const backendDto = {
                idModeloPlantilla: null,
                idArticulo: idArticulo,
                camposPlantilla: dto.camposActivos || []
            };

            const { data } = await api.post(`${BACKEND_URL}/api/v3/comercial/modelos-plantilla`, backendDto);

            const frontendData = {
                id: data.idArticulo,
                idArticulo: data.idArticulo,
                nombrePrenda: data.nombreArticulo || nombreArticulo,
                nombreArticulo: data.nombreArticulo || nombreArticulo,
                camposActivos: data.camposPlantilla || [],
                plantillaTelas: [],
                plantillaAccesorios: []
            };

            setPlantillas(prev => {
                const index = prev.findIndex(p => p.id === frontendData.id);
                if (index >= 0) {
                    const next = [...prev];
                    next[index] = {
                        ...next[index],
                        ...frontendData,
                        camposActivos: frontendData.camposActivos
                    };
                    return next;
                }
                return [...prev, frontendData];
            });

            // Limpiar cache para forzar recarga de campos
            if (nombreArticulo) {
                setCache(prev => {
                    const next = { ...prev };
                    delete next[nombreArticulo.toUpperCase()];
                    return next;
                });
            }
            toast.success("Plantilla guardada correctamente");
            return frontendData;
        } catch (err) {
            console.error("[usePlantillas] Error saving:", err);
            toast.error("Error al guardar la plantilla");
            throw err;
        } finally {
            setLoading(false);
        }
    }, [plantillas]);

    const remove = useCallback(async (id) => {
        try {
            setLoading(true);
            await api.delete(`${BACKEND_URL}/api/v3/comercial/modelos-plantilla/articulo/${id}`);
            setPlantillas(prev => prev.map(p => p.id === id ? { ...p, camposActivos: [] } : p));
            setCache({}); // Limpiar cache general
            toast.success("Plantilla eliminada correctamente");
        } catch (err) {
            console.error("[usePlantillas] Error deleting:", err);
            toast.error("Error al eliminar la plantilla");
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
