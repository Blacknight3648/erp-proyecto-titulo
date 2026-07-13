/**
 * Resumen de una Nota de Venta con sus Ordenes de Produccion asociadas,
 * usado por el listado general de trazabilidad (TrazabilidadNV).
 * Espejo de NVTrazabilidadResumenDTO.java del backend.
 */
export class NVTrazabilidadResumenDTO {
    constructor(data = {}) {
        this.idNV = data.idNV ?? null;
        this.numeroNV = data.numeroNV ?? '';
        this.clienteId = data.clienteId ?? null;
        this.clienteNombre = data.clienteNombre ?? '';
        this.estado = data.estado ?? null;
        this.fechaEmision = data.fechaEmision ?? null;
        this.motivoRechazo = data.motivoRechazo ?? null;
        this.fechaRechazo = data.fechaRechazo ?? null;

        this.evaluacionNegocioId = data.evaluacionNegocioId ?? null;
        this.numeroEVN = data.numeroEVN ?? null;
        this.estadoEVN = data.estadoEVN ?? null;
        this.motivoRechazoEVN = data.motivoRechazoEVN ?? null;
        this.fechaRechazoEVN = data.fechaRechazoEVN ?? null;

        this.ordenesProduccion = Array.isArray(data.ordenesProduccion)
            ? data.ordenesProduccion.map(op => ({
                idOP: op.idOP ?? null,
                numeroOP: op.numeroOP ?? '',
                estado: op.estado ?? null,
                fechaEntregaProgramada: op.fechaEntregaProgramada ?? null,
                hojaCompra: op.hojaCompra ? {
                    idHC: op.hojaCompra.idHC ?? null,
                    numeroHC: op.hojaCompra.numeroHC ?? '',
                    estado: op.hojaCompra.estado ?? null,
                } : null,
                ordenesCompra: Array.isArray(op.ordenesCompra)
                    ? op.ordenesCompra.map(oc => ({
                        idOC: oc.idOC ?? null,
                        numeroOC: oc.numeroOC ?? '',
                        estado: oc.estado ?? null,
                        motivoRechazo: oc.motivoRechazo ?? null,
                        fechaRechazo: oc.fechaRechazo ?? null,
                    }))
                    : [],
            }))
            : [];
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new NVTrazabilidadResumenDTO(item));
    }
}
