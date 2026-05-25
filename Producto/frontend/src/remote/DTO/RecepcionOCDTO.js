import { RecepcionOCItemDTO } from './RecepcionOCItemDTO';

/**
 * DTO de Recepción de OC (ingreso de materiales).
 * Espejo de RecepcionOCDTO.java del backend.
 */
export class RecepcionOCDTO {
    constructor(data = {}) {
        this.idRecepcion = data.idRecepcion ?? null;
        this.ocId = data.ocId ?? null;
        this.fechaRecepcion = data.fechaRecepcion ?? null;
        this.numeroGuia = data.numeroGuia ?? '';
        this.responsable = data.responsable ?? '';
        this.observaciones = data.observaciones ?? '';
        this.items = Array.isArray(data.items)
            ? data.items.map(i => new RecepcionOCItemDTO(i))
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new RecepcionOCDTO(response.data);
    }

    static listFromResponse(response) {
        if (!response || !Array.isArray(response.data)) return [];
        return response.data.map(item => new RecepcionOCDTO(item));
    }

    get totalRecibido() {
        return this.items.reduce((acc, i) => acc + Number(i.cantidadRecibida || 0), 0);
    }
}
