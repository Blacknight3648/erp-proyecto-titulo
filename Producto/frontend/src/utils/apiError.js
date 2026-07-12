const FIELD_LABELS = {
    clienteId: 'Cliente',
    vendedorId: 'Vendedor',
    evaluacionNegocioId: 'Evaluación de Negocio',
    fechaEntregaEstimada: 'Fecha de entrega estimada',
    proveedorId: 'Proveedor',
    items: 'Ítems',
    cantidad: 'Cantidad',
    cantidadPactada: 'Cantidad pactada',
    precioUnitario: 'Precio unitario',
    tipoServicio: 'Tipo de servicio',
    descripcionTrabajo: 'Descripción del trabajo',
    observaciones: 'Observaciones',
};

function friendlyField(campo) {
    if (!campo) return 'Campo';
    if (FIELD_LABELS[campo]) return FIELD_LABELS[campo];

    // "items[0].cantidad" -> intenta mapear el último segmento ("cantidad")
    const lastSegment = campo.split('.').pop()?.replace(/\[\d+\]/g, '');
    if (lastSegment && FIELD_LABELS[lastSegment]) return FIELD_LABELS[lastSegment];

    // camelCase -> "Campo Con Espacios"
    const raw = lastSegment || campo;
    const spaced = raw.replace(/([a-z0-9])([A-Z])/g, '$1 $2');
    return spaced.charAt(0).toUpperCase() + spaced.slice(1);
}

/**
 * Extrae un mensaje de error legible desde una respuesta de axios que sigue
 * el formato StandardErrorResponse del backend ({ mensaje, erroresValidacion }).
 * Prioriza el detalle por campo cuando está disponible.
 */
export function getApiErrorMessage(error, fallback = 'Ocurrió un error inesperado') {
    const data = error?.response?.data;
    if (!data) return error?.message || fallback;

    if (Array.isArray(data.erroresValidacion) && data.erroresValidacion.length > 0) {
        return data.erroresValidacion
            .map(e => `${friendlyField(e.campo)}: ${e.motivo}`)
            .join(' | ');
    }

    return data.mensaje || error?.message || fallback;
}
