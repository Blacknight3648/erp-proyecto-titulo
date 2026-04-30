export type EstadoOC = 'Pendiente' | 'Recepcionada' | 'Recibida' | 'Rechazado' | 'Parcial';

export interface ItemOCPayload {
    productoId: number;
    descripcionProducto: string;
    cantidad: number;
    precioUnitario: number;
    moneda: string;
}

export interface OrdenCompra {
    idOC: string | number;
    numeroOC?: string;
    solicitudCompraId?: string | number;
    ordenProduccionId?: string | number;
    proveedor: string;
    fecha?: string;
    fechaEmision?: string;
    items: string | ItemOCPayload[];
    total?: number;
    montoTotal?: number;
    tipo?: string;
    estado: EstadoOC | string;
    motivo_rechazo?: string;
    motivoRechazo?: string;
}

export interface CotizacionAdq {
    idSolicitud: string | number;
    numero?: string;
    fecha?: string;
    clienteId?: string | number;
    vendedorId?: string | number;
    nroReferencia?: string;
    articuloId?: string;
    articuloDescripcion: string;
    esMuestra?: boolean;
    cantidad?: number;
    tipo?: 'cotizacion' | 'costeo';
    estado: 'PENDIENTE ADQUISICIÓN' | 'Aprobada' | string;
    proveedorSugerido?: string;
    proveedorCotizado?: string;
    linkReferencia?: string;
    telas?: any[];
    accesorios?: any[];
    logotipo?: any[];
    monto?: number;
    costoTotalCalculado?: any; // Puede ser Money object del back
}

export interface KPIsAdquisiciones {
    ocAtrasadas: number;
    sinRecepcion: number;
    recepError: number;
    recepParcial: number;
    entregasAtrasadas: number;
    proyeccionCierre: number;
}
