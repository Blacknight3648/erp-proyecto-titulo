import { ItemEVNDTO } from "./ItemEVNDTO";

/**
 * DTO para representar una Evaluación de Negocio.
 * Basado en EvaluacionNegocio.java
 */
export class EvaluacionNegocioDTO {
  constructor(data = {}) {
    this.evaluacionNegocioId = data.id || data.evaluacionNegocioId || null;
    this.numeroEvn = data.numero || (data.numeroEvn ? data.numeroEvn.value : "");
    this.clienteId = data.clienteId || null;
    this.vendedorId = data.vendedorId || null;
    this.estado = data.estado || "BORRADOR";
    this.fechaEvaluacion = data.fechaEvaluacion || null;
    this.montoTotal = data.montoTotal || 0;
    this.currency = "CLP";
    this.margenGanancia = data.margenGanancia || 0;
    this.rentabilidadEsperada = data.rentabilidadEsperada || 0;
    this.costeoId = data.costeoId || null;
    this.solicitudCotizacionId = data.solicitudCotizacionId || null;
    this.porcentajeComision = data.porcentajeComision || 0;

    this.clienteNombre = data.clienteNombre
        || data.cliente?.nombreCliente
        || data.cliente?.nombre
        || null;

    this.referencia = data.referencia || data.descripcion || null;

    this.vendedorNombre = data.vendedorNombre
        || data.vendedor?.nombreUsuario
        || null;

    this.numeroCosteo    = data.numeroCosteo || null;
    this.numeroSolicitud = data.numeroSolicitud || null;
    this.tomaTallajeMetadata = data.tomaTallajeMetadata || null;
    this.pegadoCintaMetadata = data.pegadoCintaMetadata || null;

    // Costos adicionales (mapeo plano)
    this.garantiaSeriedad = data.garantiaSeriedad || 0;
    this.garantiaFielCumplimiento = data.garantiaFielCumplimiento || data.garantiaFiel || 0;
    this.flete = data.flete || data.fleteEspecial || 0;
    this.modificacionPrenda = data.modificacionPrenda || 0;
    this.tomaTallajeArr = data.tomaTallaje || 0; 
    this.certificacion = data.certificacion || 0;
    this.muestras = data.muestras || data.muestrasFisicas || 0;

    this.items = (data.items || []).map(item => new ItemEVNDTO(item));
    this.gastosAdicionales = data.gastosAdicionales || [];
  }

  static fromResponse(response) {
    if (!response || !response.data) return null;
    return new EvaluacionNegocioDTO(response.data);
  }

  static listFromResponse(response) {
    if (!response || !Array.isArray(response.data)) return [];
    return response.data.map((item) => new EvaluacionNegocioDTO(item));
  }
}
