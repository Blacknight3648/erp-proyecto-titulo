import {
    mockNVs,
    mockComercial,
    mockAllOCs,
    mockOperaciones,
    mockOpDetails,
    mockOTs,
    mockOpPersonalizacion
} from '../data/mockData';

export const useDataLookup = () => {

    // 1. Obtener Nota de Venta (NV)
    const getNV = (nvId) => {
        return mockNVs.find(nv => nv.id === nvId) || null;
    };

    // 2. Obtener Solicitud de Compra (SC)
    // Se asume que mockComercial contiene las SCs
    const getSC = (scId) => {
        const sc = mockComercial.find(s => (s.idSC || s.id) === scId);
        if (!sc) return null;

        // Encontrar NV vinculada (si existe lógica directa o inferida)
        // Por ahora, algunas SC tienen campo 'op' pero no 'nv' directo en mockComercial
        // Podríamos buscar en mockNVs qué NV generó esta SC si hubiera enlace
        return sc;
    };

    // 3. Obtener Orden de Compra (OC)
    const getOC = (ocId) => {
        const oc = mockAllOCs.find(o => (o.idOC || o.id_oc) === ocId);
        if (!oc) return null;
        return oc;
    };

    // 4. Obtener Orden de Producción (OP)
    const getOP = (opId) => {
        const op = mockOperaciones.find(o => (o.idOP || o.id) === opId);
        if (!op) return null;

        const opIdLookup = op.idOP || op.id;
        const details = mockOpDetails[opIdLookup] || {};
        return { ...op, ...details };
    };

    // 5. Obtener OT
    const getOT = (otId) => {
        return mockOTs.find(ot => ot.id === otId) || null;
    };

    // 6. Obtener Personalización
    const getPersonalizacion = (opId) => {
        return mockOpPersonalizacion.find(p => (p.ordenProduccionId || p.idOP) === opId) || null;
    };

    // 7. Obtener Trazabilidad Completa (Árbol)
    const getTrazabilidad = (nvId) => {
        const nv = getNV(nvId);
        if (!nv) return null;

        // Buscar SCs relacionadas a esta NV (Mock data structure matching)
        // En mockData actual, no hay link directo NV->SC explícito en arrays, 
        // pero mockAllOCs tiene 'sc' y 'op'.
        // Vamos a inferir relaciones basadas en los campos disponibles.

        // Estrategia: 
        // 1. NV es la raíz.
        // 2. Buscar OPs que referencien esta NV (o derivar de alguna forma).
        //    Actualmente mockOperaciones no tiene 'nv'.
        // 3. Buscar OCs que tengan 'sc'.

        // Para este prototipo, vamos a simular la búsqueda:

        // Simulación: Buscar OPs vinculadas (por coincidencia de string o mock logic)
        // En un caso real, existiría una tabla intermedia.

        // Buscar OCs asociadas indirectamente (si tuviéramos SC vinculada a NV)
        // Buscar SCs asociadas a esta NV
        const relatedSC = mockComercial.filter(sc => (sc.notaVentaId || sc.nv_id) === nv.id);

        // Buscar OPs vinculadas a esta NV
        const relatedOP = mockOperaciones.filter(op => (op.notaVentaId || op.nv_id) === nv.id);

        // Buscar OCs asociadas a las SCs encontradas (Adquisiciones)
        const relatedOC_SC = mockAllOCs.filter(oc => relatedSC.some(sc => (sc.idSC || sc.id) === (oc.solicitudCompraId || oc.idSC)));

        // Buscar OCs/OSs asociadas a las OPs encontradas (Producción)
        const relatedOC_OP = mockAllOCs.filter(oc => relatedOP.some(op => (op.idOP || op.id) === (oc.ordenProduccionId || oc.idOP)));

        // Buscar OTs y Personalizaciones vinculadas a las OPs
        const relatedOTs = mockOTs.filter(ot => relatedOP.some(op => (op.idOP || op.id) === (ot.ordenProduccionId || ot.idOP)));
        const relatedPersonalizacion = mockOpPersonalizacion.filter(p => relatedOP.some(op => (op.idOP || op.id) === (p.ordenProduccionId || p.idOP)));

        return {
            nv,
            relatedSC,
            relatedOC_SC,
            relatedOP,
            relatedOC_OP,
            relatedOTs,
            relatedPersonalizacion
        };
    };

    // 6. Alertas: Regla del 3 (OC Pendiente > 3 días)
    const getAlertasReglaDelTres = () => {
        // Mock logic: fechas actuales estáticas en mockData. 
        // Asumimos 'hoy' = 12/02/2026 para el cálculo
        const HOY = new Date('2026-02-12');

        return mockAllOCs.filter(oc => {
            if (oc.estado !== 'Pendiente') return false;

            // Parse fecha DD/MM/YYYY
            const fechaStr = oc.fechaEmision || oc.fecha;
            let fechaEmision;
            if (fechaStr.includes('/')) {
                const [d, m, y] = fechaStr.split('/');
                fechaEmision = new Date(`${y}-${m}-${d}`);
            } else {
                fechaEmision = new Date(fechaStr);
            }
            const diffTime = Math.abs(HOY - fechaEmision);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

            return diffDays > 3;
        });
    };

    return {
        getNV,
        getSC,
        getOC,
        getOP,
        getOT,
        getPersonalizacion,
        getTrazabilidad,
        getAlertasReglaDelTres
    };
};
