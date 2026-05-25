/**
 * DTO para recepción de una OS (recibe el trabajo del taller).
 * Espejo de RecepcionOSDTO.java del backend.
 */
export class RecepcionOSDTO {
    constructor(data = {}) {
        this.idRecepcion = data.idRecepcion ?? null;
        this.osId = data.osId ?? null;
        this.fechaRecepcion = data.fechaRecepcion ?? null;
        this.cantidadRecibida = data.cantidadRecibida ?? 0;
        this.cantidadConforme = data.cantidadConforme ?? 0;
        this.cantidadDefectuosa = data.cantidadDefectuosa ?? 0;
        this.responsable = data.responsable ?? '';
        this.observaciones = data.observaciones ?? '';
    }
}
