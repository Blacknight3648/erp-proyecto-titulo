/**
 * Espejo del enum EstadoCosteo del backend.
 *
 * Ciclo de vida:
 *   BORRADOR  → COSTEADO, RECHAZADO
 *   COSTEADO  → APROBADO, RECHAZADO, BORRADOR (reproceso)
 *   APROBADO  → (terminal)
 *   RECHAZADO → BORRADOR (reproceso)
 */
export const EstadoCosteo = Object.freeze({
    BORRADOR:  'BORRADOR',
    COSTEADO:  'COSTEADO',
    APROBADO:  'APROBADO',
    RECHAZADO: 'RECHAZADO',
});

/** Etiqueta legible para mostrar en la UI. */
export const ESTADO_COSTEO_LABEL = Object.freeze({
    [EstadoCosteo.BORRADOR]:  'Borrador',
    [EstadoCosteo.COSTEADO]:  'Costeado',
    [EstadoCosteo.APROBADO]:  'Aprobado',
    [EstadoCosteo.RECHAZADO]: 'Rechazado',
});

/**
 * Devuelve true si la transición origen → destino es válida,
 * replicando la lógica del backend `EstadoCosteo.puedeTransicionarA`.
 */
export function puedeTransicionarA(origen, destino) {
    if (!origen || !destino) return false;
    switch (origen) {
        case EstadoCosteo.BORRADOR:
            return destino === EstadoCosteo.COSTEADO || destino === EstadoCosteo.RECHAZADO;
        case EstadoCosteo.COSTEADO:
            return destino === EstadoCosteo.APROBADO || destino === EstadoCosteo.RECHAZADO || destino === EstadoCosteo.BORRADOR;
        case EstadoCosteo.APROBADO:
            return false;
        case EstadoCosteo.RECHAZADO:
            return destino === EstadoCosteo.BORRADOR;
        default:
            return false;
    }
}

export default EstadoCosteo;
