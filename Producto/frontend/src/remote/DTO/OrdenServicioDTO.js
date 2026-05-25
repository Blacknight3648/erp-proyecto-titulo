import { DespachoOSDTO } from './DespachoOSDTO';
import { RecepcionOSDTO } from './RecepcionOSDTO';

/**
 * DTO para Orden de Servicio (taller externo).
 * Espejo de OrdenServicioDTO.java del backend.
 *
 * Estados: EMITIDA | EN_PROCESO | RECEPCIONADA | CERRADA
 * Tipos:   BORDADO | ESTAMPADO | LAVADO | SUBLIMADO | OTRO
 */
export class OrdenServicioDTO {
    constructor(data = {}) {
        this.idOS = data.idOS ?? null;
        this.numeroOS = data.numeroOS ?? '';
        this.opId = data.opId ?? null;
        this.proveedorId = data.proveedorId ?? null;
        this.tipoServicio = data.tipoServicio ?? 'OTRO';
        this.estado = data.estado ?? 'EMITIDA';
        this.fechaEmision = data.fechaEmision ?? null;
        this.fechaEntregaEstimada = data.fechaEntregaEstimada ?? null;
        this.descripcionTrabajo = data.descripcionTrabajo ?? '';
        this.cantidadPactada = data.cantidadPactada ?? 0;
        this.precioUnitario = data.precioUnitario ?? 0;
        this.totalNeto = data.totalNeto ?? 0;
        this.observaciones = data.observaciones ?? '';
        this.despachos = Array.isArray(data.despachos)
            ? data.despachos.map(d => new DespachoOSDTO(d))
            : [];
        this.recepciones = Array.isArray(data.recepciones)
            ? data.recepciones.map(r => new RecepcionOSDTO(r))
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new OrdenServicioDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new OrdenServicioDTO(item));
    }

    get totalDespachado() {
        return this.despachos.reduce((acc, d) => acc + Number(d.cantidadDespachada || 0), 0);
    }

    get totalRecibido() {
        return this.recepciones.reduce((acc, r) => acc + Number(r.cantidadRecibida || 0), 0);
    }
}
