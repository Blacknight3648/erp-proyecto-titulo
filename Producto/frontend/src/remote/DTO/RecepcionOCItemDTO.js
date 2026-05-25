/**
 * DTO de un ítem dentro de una recepción de OC.
 * Espejo de RecepcionOCItemDTO.java del backend.
 */
export class RecepcionOCItemDTO {
    constructor(data = {}) {
        this.idRecepcionItem = data.idRecepcionItem ?? null;
        this.recepcionId = data.recepcionId ?? null;
        this.ocItemId = data.ocItemId ?? null;
        this.cantidadRecibida = data.cantidadRecibida ?? 0;
        this.cantidadConforme = data.cantidadConforme ?? 0;
        this.cantidadRechazada = data.cantidadRechazada ?? 0;
        this.motivoRechazo = data.motivoRechazo ?? '';
    }
}
