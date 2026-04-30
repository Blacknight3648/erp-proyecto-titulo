/**
 * Utilidades compartidas para el manejo de formularios y limpieza de datos.
 */

/**
 * Extrae el identificador numérico de un string o número.
 * Útil para limpiar IDs que vienen con prefijos (ej: "SCOS-26001" -> 26001).
 * @param {string|number} id 
 * @returns {number|null}
 */
export const parseId = (id) => {
    if (!id) return null;
    if (typeof id === 'number') return id;
    const match = id.toString().match(/\d+/);
    return match ? parseInt(match[0], 10) : null;
};
