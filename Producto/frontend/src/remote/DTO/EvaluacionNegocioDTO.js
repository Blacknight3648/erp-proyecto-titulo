import { ItemEVNDTO } from "./ItemEVNDTO";

/**
 * DTO para representar una Evaluación de Negocio.
 * Basado en EVNResponse.java
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

    // Costos adicionales (mapeo plano desde gastosAdicionales)
    this.garantiaSeriedad = 0;
    this.garantiaFielCumplimiento = 0;
    this.flete = 0;
    this.modificacionPrenda = 0;
    this.certificacion = 0;
    this.muestras = 0;
    this.entregaPersonalizada = 0;

    this.gastosAdicionales = data.gastosAdicionales || [];
    this.gastosAdicionales.forEach(g => {
      const monto = g.monto || 0;
      if (g.tipoGasto === 'FLETE') this.flete = monto;
      if (g.tipoGasto === 'GARANTIA_SERIEDAD') this.garantiaSeriedad = monto;
      if (g.tipoGasto === 'GARANTIA_CUMPLIMIENTO') this.garantiaFielCumplimiento = monto;
      if (g.tipoGasto === 'CERTIFICACION') this.certificacion = monto;
      if (g.tipoGasto === 'MUESTRAS_FISICAS') this.muestras = monto;
      if (g.tipoGasto === 'ENTREGA_PERSONALIZADA') this.entregaPersonalizada = monto;
    });

    // Toma de tallaje — objeto estructurado desde el backend (3FN)
    this.tomaTallaje = data.tomaTallaje || null;

    // Pegado de cinta — detalles como [{clave, valor}]
    this.pegadoCintaDetalles = data.pegadoCintaDetalles || [];

    this.items = (data.items || []).map(item => new ItemEVNDTO(item));
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
