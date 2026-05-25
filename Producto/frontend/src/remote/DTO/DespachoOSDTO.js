/**
 * DTO para representar un despacho de OS (envío al taller externo).
 * Espejo de DespachoOSDTO.java del backend.
 */
export class DespachoOSDTO {
    constructor(data = {}) {
        this.idDespacho = data.idDespacho ?? null;
        this.osId = data.osId ?? null;
        this.fechaDespacho = data.fechaDespacho ?? null;
        this.cantidadDespachada = data.cantidadDespachada ?? 0;
        this.descripcion = data.descripcion ?? '';
        this.responsable = data.responsable ?? '';
        this.observaciones = data.observaciones ?? '';
    }
}
