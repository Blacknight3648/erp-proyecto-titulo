import { useState, useCallback, useEffect } from 'react';
import { api, BACKEND_URL } from '../remote/service/api';
import { toast } from 'sonner';

export const FIELD_DESCRIPTIONS = {
    gorro:              'Indica si la prenda incorpora capucha o gorro y su mecanismo de ajuste. Define el consumo adicional de tela y los avíos de regulación.',
    cuello:             'Tipología de escote o cuello terminado. Condiciona la entretela requerida, el método de confección y el tiempo por operación.',
    abotonaduraCierre:  'Sistema de apertura de la prenda. Define el tipo y cantidad de avíos (botón, cremallera, velcro, presión) y su impacto en el costo unitario.',
    cortesAplicaciones: 'Silueta y paneles que componen la prenda. Incluye piezas contrastantes, aplicaciones técnicas o decorativas que condicionan el consumo de tela.',
    fuelles:            'Reservas de tejido plisadas en zonas de alta tensión —espalda, codos, rodillas. Críticos en prendas técnicas, laboratorio y corporativas.',
    mangas:             'Tipología, largo y método de unión al cuerpo. Define consumo de tela, tipo de costura y características funcionales de movimiento.',
    puños:              'Terminación inferior de la manga. Puede ser funcional (botón, velcro, cierre) o elástica. Impacta en el acabado visual y la practicidad.',
    pretinasRuedo:      'Terminación inferior del cuerpo o pantalón. Define el tipo de acabado, la presencia de elástico o cordón y las costuras de refuerzo perimetral.',
    bolsillos:          'Tipología y posición de bolsillos. Condiciona la funcionalidad, el consumo adicional de tela y el número de operaciones de confección.',
    obsModelo:          'Anotaciones técnicas no cubiertas por los campos anteriores: referencias de muestra, instrucciones de calidad, detalles de estampado o bordado.',
};

export const FIELD_CATALOGS = {
    gorro: [
        'Sin gorro',
        'Gorro fijo con cordón',
        'Gorro desmontable con botón',
        'Gorro con cierre oculto',
        'Capucha doble capa',
        'Capucha con tira reflectante',
        'Capucha con visera rígida',
    ],
    cuello: [
        'Cuello redondo',
        'Cuello en V',
        'Cuello polo / camisero',
        'Cuello mao / mandarín',
        'Cuello halter',
        'Cuello tortuga / cisne',
        'Cuello solapa sastre',
        'Cuello con capucha integrada',
        'Escote barco',
        'Cuello panadero',
    ],
    abotonaduraCierre: [
        'Sin cierre',
        'Botones nácar',
        'Botones metálicos dorados',
        'Botones metálicos plateados',
        'Cierre YKK invisible',
        'Cierre YKK metálico visible',
        'Cierre plástico a color',
        'Velcro técnico',
        'Broches a presión metálicos',
        'Corchetes de presión',
        'Sistema dual botón + cierre',
    ],
    cortesAplicaciones: [
        'Corte recto regular fit',
        'Corte slim fit',
        'Corte amplio / loose',
        'Corte oversize',
        'Corte entallado / ajustado',
        'Paneles contrastantes laterales',
        'Ribete perimetral en contraste',
        'Vivos en costados',
        'Refuerzo en codos y rodillas',
        'Panel transpirable en espalda',
        'Panel en malla técnica',
        'Aplicación reflectante',
    ],
    fuelles: [
        'Sin fuelle',
        'Fuelle dorsal central',
        'Fuelle lateral bilateral',
        'Fuelle en codo',
        'Fuelle en rodilla',
        'Fuelle trapezoidal bajo axila',
        'Fuelle tipo acordeón espalda baja',
    ],
    mangas: [
        'Sin manga',
        'Manga corta recta',
        'Manga corta francesa',
        'Manga larga recta',
        'Manga ¾',
        'Manga ranglan',
        'Manga con refuerzo en codo',
        'Manga con abertura y botón',
        'Manga con elástico en borde',
        'Manga bicicleta',
        'Manga dolman',
    ],
    puños: [
        'Sin puño',
        'Puño elásticado simple',
        'Puño con botón simple',
        'Puño con doble botón',
        'Puño doble francés (gemelos)',
        'Puño con velcro regulable',
        'Puño tubular en punto',
        'Puño con cierre lateral',
        'Puño con reflectante integrado',
    ],
    pretinasRuedo: [
        'Pretina elástica simple',
        'Pretina elástica con cordón exterior',
        'Pretina con pasadores de cinturón',
        'Pretina alta con forro',
        'Ruedo simple costura recta',
        'Ruedo con dobladillo reforzado',
        'Ruedo con elástico en borde',
        'Bajo abierto con abertura lateral',
        'Dobladillo ciego a máquina',
    ],
    bolsillos: [
        'Sin bolsillos',
        'Bolsillo parche rectangular',
        'Bolsillo parche con solapa y velcro',
        'Bolsillo parche con cierre YKK',
        'Bolsillo interior con cremallera',
        'Bolsillo de vivo simple',
        'Bolsillo de vivo doble',
        'Bolsillo tipo cargo lateral',
        'Bolsillo pecho izquierdo',
        'Bolsillo tipo canguro frontal',
        'Bolsillo oculto costura lateral',
    ],
    obsModelo: [],
};

export const FIELD_LABELS = {
    gorro:              'Gorro',
    cuello:             'Cuello',
    abotonaduraCierre:  'Abotonadura / Cierre',
    cortesAplicaciones: 'Cortes y Aplicaciones',
    fuelles:            'Fuelles',
    mangas:             'Mangas',
    puños:              'Puños',
    pretinasRuedo:      'Pretinas / Ruedo',
    bolsillos:          'Bolsillos',
    obsModelo:          'Obs. del Modelo',
};

export const ALL_FIELDS = new Set(Object.keys(FIELD_LABELS));

export function usePlantillas() {
    const [cache, setCache] = useState({});
    const [plantillas, setPlantillas] = useState([]);
    const [camposDisponibles, setCamposDisponibles] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchCamposDisponibles = useCallback(async () => {
        try {
            const { data } = await api.get(`${BACKEND_URL}/api/v3/comercial/plantillas`);
            // La API devuelve solo campos con activo=true (filtrado en backend)
            setCamposDisponibles(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error("[usePlantillas] Error fetching campos del catálogo:", err);
            // Fallback: dejar la lista vacía; ConfiguracionTecnica usará ALL_FIELDS
        }
    }, []);

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
        fetchCamposDisponibles();
    }, [fetchAll, fetchCamposDisponibles]);

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
            if (err.response?.status === 404) {
                // Si la configuración no existe en BD, el resultado final es el mismo (vacía).
                setPlantillas(prev => prev.map(p => p.id === id ? { ...p, camposActivos: [] } : p));
                setCache({});
                toast.success("Plantilla eliminada correctamente");
            } else {
                console.error("[usePlantillas] Error deleting:", err);
                toast.error("Error al eliminar la plantilla");
                throw err;
            }
        } finally {
            setLoading(false);
        }
    }, []);

    return {
        getCamposForArticulo,
        plantillas,
        configuraciones: plantillas, // Alias para compatibilidad con GestionPlantillas
        camposDisponibles,
        save,
        remove,
        loading,
        error
    };
}
